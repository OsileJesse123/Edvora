package com.jesse.edvaro.presentation.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jesse.edvaro.data.network.Ride
import com.jesse.edvaro.data.repository.RideRepo
import com.jesse.edvaro.data.network.User
import com.jesse.edvaro.data.repository.UserRepo
import kotlinx.coroutines.launch
import com.jesse.edvaro.data.network.RideData
import com.jesse.edvaro.utils.RideState
import com.jesse.edvaro.utils.UserCurrentDate
import com.jesse.edvaro.utils.isRideUpcomingOrPastOrCurrent
import com.jesse.edvaro.utils.mapStatesToCities

class EdvoraActivityViewModel(private val rideRepo: RideRepo,
                              private val userRepo: UserRepo): ViewModel() {

    private var _nearestRides: MutableLiveData<List<Ride>> = MutableLiveData()
    val nearestRides: LiveData<List<Ride>> get() = _nearestRides
    private var _user: MutableLiveData<User> = MutableLiveData()
    val user: LiveData<User>  get() = _user

    var upcomingRides = mutableListOf<Ride>()
    var pastRides = mutableListOf<Ride>()
    var statesToCities: List<Map<String, List<String>>> = mutableListOf()

    private var _numberOfRides: MutableLiveData<List<Int>> = MutableLiveData()
    val numberOfRides: LiveData<List<Int>> = _numberOfRides

    var cities: List<String> = mutableListOf()
    var states: List<String> = mutableListOf()

    private var _citiesAndStatesList: MutableLiveData<MutableList<List<String>>> = MutableLiveData()
    val citiesAndStatesList: LiveData<MutableList<List<String>>> get() = _citiesAndStatesList

    var isFiltered = false

    init {
        setUser()
    }

    fun getDistance(stationPath: List<Int>): Int{
        return getNearestStationCode(stationPath) - (user.value?.stationCode ?: 0)
    }

    fun setTheNearestRide(theUser: User?){
        viewModelScope.launch{
            _nearestRides.value = theUser?.let{
                val theRides = rideRepo.getRides()
                RideData.getNearestStation(it.stationCode, theRides)
            }
        }
    }

    private fun setNumberOfRides(){
        val numberOfRides = mutableListOf<Int>()
        viewModelScope.launch {
            numberOfRides.add(upcomingRides.size)
            numberOfRides.add(pastRides.size)
             _numberOfRides.value = numberOfRides
        }
    }
    private fun getNearestStationCode(stationPath: List<Int>): Int{
        var finalCode = 0
        for(code in stationPath){
            if(code >= user.value?.stationCode ?: 0){
                finalCode = code
                break
            }
        }
        return finalCode
    }

    private fun setUser(){
        viewModelScope.launch {
            _user.value = userRepo.getUser()
        }
    }

    fun setTheStatesToCities(rides: List<Ride>?){
        rides?.let {
            statesToCities = mapStatesToCities(it)
        }
    }

    fun setTheCityDropDownList(position: Int, state: String): List<String>{
        return statesToCities[position][state] ?: listOf()
    }

    fun setUpcomingAndPastRides(rides: List<Ride>?){
            rides?.let{
                for(ride in it){
                    val rideState = isRideUpcomingOrPastOrCurrent(ride.date, UserCurrentDate.userCurrentDate)
                    if(rideState == RideState.UPCOMING)
                        upcomingRides.add(ride)
                    else if( rideState == RideState.PAST)
                        pastRides.add(ride)
                }
                setNumberOfRides()
            }
    }

    fun setCitiesAndStates(rides: List<Ride>?){
        rides?.let {
            theRides ->
            cities = theRides.map { it.city }.toSet().toList()
            states = theRides.map { it.state }.toSet().toList()
        }
    }

    fun updateCitiesAndStatesList(){
        _citiesAndStatesList.value = mutableListOf(cities, states)
    }

    fun filterListByStatesAndOrCities(city: String, state: String):
            List<Ride>{
        isFiltered = true
        var filteredRides: List<Ride> = listOf()
       _nearestRides.value?.let {
           filteredRides = filterByState(state, it)
           filteredRides = filterByCity(city, filteredRides)
       }
        return filteredRides
    }

    private fun filterByState(state: String, rides: List<Ride>): List<Ride>{
        return if(state.isEmpty()) rides else rides.filter { it.state == state }
    }
    private fun filterByCity(city: String, rides: List<Ride>): List<Ride>{
        return if(city.isEmpty()) rides else rides.filter { it.city == city }
    }
}