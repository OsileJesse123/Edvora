package com.jesse.edvaro

import com.jesse.edvaro.data.network.Ride
import com.jesse.edvaro.data.network.RideService
import com.jesse.edvaro.data.network.User
import com.jesse.edvaro.data.repository.RideRepository
import com.jesse.edvaro.data.repository.UserRepository
import com.jesse.edvaro.presentation.viewModel.EdvoraActivityViewModel
import com.jesse.edvaro.utils.formatRideDate
import com.jesse.edvaro.utils.mapStatesToCities
import com.jraska.livedata.test
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import java.util.stream.Stream
import org.junit.jupiter.api.Test



@ExperimentalCoroutinesApi
@ExtendWith(value = [InstantExecutorExtension::class, CoroutineDispatcherRule::class])
class EdvoraViewModelTest {

//    @get:Rule
//    val coroutineDispatcherRule = CoroutineDispatcherRule()

//    @get:Rule
//    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var rideService: RideService
    private lateinit var rideRepo: RideRepository
    private lateinit var userRepo: UserRepository
    private lateinit var viewModel: EdvoraActivityViewModel
    private lateinit var ride1: Ride
    private lateinit var ride2: Ride
    private lateinit var ride3: Ride
    private lateinit var ride4: Ride

    private lateinit var rides: List<Ride>
    private val user = User(42, "Jesse Osile", "image_url")



    @BeforeEach
    fun setup(){

        ride1 = Ride("Mainland",
            "01/04/2022 01:30 AM",1,1,
            "", 1, "Lagos", listOf(34,48,52,69,75,88))
        ride2 = Ride("Benin",
            "02/05/2023 02:30 PM",2,2,
            "", 2, "Edo", listOf(37,50,54,62,77,90))
        ride3 = Ride("Umaia",
            "03/02/2022 01:30 AM",3,3,
            "", 3, "Abia", listOf(39,52,58,73,79,93))
        ride4 = Ride("Victoria Island",
            "05/07/2024 09:31 PM",4,4,
            "", 4, "Lagos", listOf(53,58,63,79,85,99))
        rides = listOf(ride3, ride4, ride2, ride1)

        runBlockingTest {
            rideService = mock{
                whenever(it.getRides()).thenReturn(rides)
                whenever(it.getUser()).thenReturn(user)
            }
            rideRepo = RideRepository(rideService)
            userRepo = UserRepository(rideService)
            viewModel = EdvoraActivityViewModel(rideRepo, userRepo)
        }
    }

    @org.junit.jupiter.api.Test
    fun test_setUser(){

        //Act
        val updatedUser = viewModel.user.test().value()

        //Assert
        assertEquals(user, updatedUser)
    }

    @Test
    fun test_getDistance(){
        //Arrange
        val stationPath = listOf(34,48,52,69,75,88)
        val expectedDistance = 6

        //Act
        val actualDistance = viewModel.getDistance(stationPath)

        //Assert
        assertEquals(expectedDistance, actualDistance)
    }

    @Test
    fun test_setTheNearestRide(){
        //Arrange
        val expectedNearestRide = listOf(ride1, ride2, ride3, ride4)

        //Act
        viewModel.setTheNearestRide(user)
        val actualNearestRides = viewModel.nearestRides.test().value()

        //Assert
        assertEquals(expectedNearestRide, actualNearestRides)
    }

    @Test
    fun test_setTheNearestRideWhenUserIsNull(){
        //Arrange
        val expectedNearestRide = null

        //Act
        viewModel.setTheNearestRide(null)
        val actualNearestRides = viewModel.nearestRides.test().value()

        //Assert
        assertEquals(expectedNearestRide, actualNearestRides)
    }

    @Test
    fun test_setTheStatesToCities(){
        //Arrange
        val lagosMap = mapOf("Lagos" to listOf("Victoria Island", "Mainland"))
        val edoMap = mapOf("Edo" to listOf("Benin"))
        val abiaMap = mapOf("Abia" to listOf("Umaia"))
        val expectedStatesToCities = listOf(abiaMap, lagosMap, edoMap)

        //Act
        viewModel.setTheStatesToCities(rides)
        val actualStatesToCities = viewModel.statesToCities

        //Assert
        assertEquals(expectedStatesToCities, actualStatesToCities)
    }

    @Test
    fun test_setTheStatesToCitiesWhenListOfRidesIsNull(){
        //Arrange
        val expectedStatesToCities = listOf<Map<String, List<String>>>()

        //Act
        viewModel.setTheStatesToCities(null)
        val actualStatesToCities = viewModel.statesToCities

        //Assert
        assertEquals(expectedStatesToCities, actualStatesToCities)
    }

    @Test
    fun test_setTheCityDropDownList(){
        //Arrange
        val statesToCities = mapStatesToCities(rides)
        val expectedCitiesOfLagos = listOf("Victoria Island", "Mainland")
        val expectedCitiesOfEdo = listOf("Benin")
        val expectedCitiesOfAbia = listOf("Umaia")
        val expectedCitiesOfAbuja = listOf<String>()

        //Act
        viewModel.setTheStatesToCities(rides)
        val actualCitiesOfLagos = viewModel.setTheCityDropDownList(1, "Lagos")
        val actualCitiesOfEdo = viewModel.setTheCityDropDownList(2, "Edo")
        val actualCitiesOfAbia = viewModel.setTheCityDropDownList(0, "Abia")
        val actualCitiesOfAbuja = viewModel.setTheCityDropDownList(2, "Abuja")

        //Assert
        assertEquals(expectedCitiesOfAbia, actualCitiesOfAbia)
        assertEquals(expectedCitiesOfAbuja, actualCitiesOfAbuja)
        assertEquals(expectedCitiesOfLagos, actualCitiesOfLagos)
        assertEquals(expectedCitiesOfEdo, actualCitiesOfEdo)
    }

    @Test
    fun test_setUpcomingAndPastRides(){
        //Arrange
        val expectedUpcomingRides = listOf(ride4, ride2, ride1)
        expectedUpcomingRides.forEach {
            it.date = formatRideDate(it.date)
        }
        val expectedPastRides = listOf(ride3)
        expectedPastRides.forEach {
            it.date = formatRideDate(it.date)
        }


        //Act
        viewModel.setUpcomingAndPastRides(rides)
        val actualUpcomingRides = viewModel.upcomingRides
        val actualPastRides = viewModel.pastRides
        val numberOfRides = viewModel.numberOfRides.test().value()


        //Assert
        assertEquals(expectedUpcomingRides, actualUpcomingRides)
        assertEquals(expectedPastRides, actualPastRides)
        assertEquals(expectedUpcomingRides.size, numberOfRides[0])
        assertEquals(expectedPastRides.size, numberOfRides[1])
    }

    @Test
    fun test_setUpcomingAndPastRidesWhenListOfRidesIsNull(){
        //Arrange
        val expectedUpcomingRides = listOf<Ride>()
        val expectedPastRides = listOf<Ride>()

        //Act
        viewModel.setUpcomingAndPastRides(null)
        val actualUpcomingRides = viewModel.upcomingRides
        val actualPastRides = viewModel.pastRides
        val numberOfRides = viewModel.numberOfRides.value

        //Assert
        assertEquals(expectedPastRides, actualPastRides)
        assertEquals(expectedUpcomingRides, actualUpcomingRides)
        assertEquals(null, numberOfRides)
    }

    @Test
    fun test_setCitiesAndStates(){
        //Arrange
        val expectedCities = rides.map{it.city}.toSet().toList()
        val expectedStates = rides.map { it.state }.toSet().toList()

        //Act
        viewModel.setCitiesAndStates(rides)
        val actualCities = viewModel.cities
        val actualStates = viewModel.states

        //Assert
        assertEquals(expectedCities, actualCities)
        assertEquals(expectedStates, actualStates)
    }

    @Test
    fun test_setCitiesAndStatesWhenListOfRidesIsNull(){
        //Arrange
        val expectedCities = listOf<Ride>()
        val expectedStates = listOf<Ride>()

        //Act
        viewModel.setCitiesAndStates(null)
        val actualCities = viewModel.cities
        val actualStates = viewModel.states

        //Assert
        assertEquals(expectedCities, actualCities)
        assertEquals(expectedStates, actualStates)
    }

    @Test
    fun test_updateCitiesAndStatesList(){
        //Arrange
        val cities = rides.map{it.city}.toSet().toList()
        val states = rides.map { it.state }.toSet().toList()
        val expectedCitiesAndStatesList = listOf(cities, states)

        //Act
        viewModel.setCitiesAndStates(rides)
        viewModel.updateCitiesAndStatesList()
        val actualCitiesAndStatesList = viewModel.citiesAndStatesList.test().value()

        //Assert
        assertEquals(expectedCitiesAndStatesList, actualCitiesAndStatesList)
    }

    @Test
    fun test_filterListByStatesAndOrCitiesWhenStateAndCityAreNotEmpty(){
        //Arrange
        val cityName = "Victoria Island"
        val stateName = "Lagos"
        val expectedRides = listOf(ride4)

        //Act
        viewModel.setTheNearestRide(user)
        val actualRides = viewModel.filterListByStatesAndOrCities(cityName,stateName)

        //Assert
        assertEquals(expectedRides, actualRides)
    }

    @Test
    fun test_filterListByStatesAndOrCitiesWhenCityIsEmptyAndStateIsNotEmpty(){
        //Arrange
        val cityName = ""
        val stateName = "Lagos"
        val expectedRides = listOf(ride1, ride4)

        //Act
        viewModel.setTheNearestRide(user)
        val actualRides = viewModel.filterListByStatesAndOrCities(cityName,stateName)

        //Assert
        assertEquals(expectedRides, actualRides)
    }

    @Test
    fun test_filterListByStatesAndOrCitiesWhenStateIsEmptyAndCityIsNotEmpty(){
        //Arrange
        val cityName = "Victoria Island"
        val stateName = ""
        val expectedRides = listOf(ride4)

        //Act
        viewModel.setTheNearestRide(user)
        val actualRides = viewModel.filterListByStatesAndOrCities(cityName,stateName)

        //Assert
        assertEquals(expectedRides, actualRides)
    }

    @Test
    fun test_filterListByStatesAndOrCitiesWhenStateAndCityAreBothEmpty(){
        //Arrange
        val cityName = ""
        val stateName = ""
        val expectedRides = listOf(ride1, ride2, ride3, ride4)

        //Act
        viewModel.setTheNearestRide(user)
        val actualRides = viewModel.filterListByStatesAndOrCities(cityName,stateName)

        //Assert
        assertEquals(expectedRides, actualRides)
    }

    @Test
    fun test_filterListByStatesAndOrCitiesWhenListOfRidesIsNull(){
        //Arrange
        val cityName = "Mainland"
        val stateName = "Lagos"
        val expectedRides = listOf<Ride>()

        //Act
        viewModel.setTheNearestRide(null)
        val actualRides = viewModel.filterListByStatesAndOrCities(cityName,stateName)

        //Assert
        assertEquals(expectedRides, actualRides)
    }

    private companion object {
        val user = User(42, "Jesse Osile", "image_url")
        val ride4 = Ride("Victoria Island",
            "05th Jul 2024 21:31",4,4, "",
            4, "Lagos", listOf(53,58,63,79,85,99))
        val ride1 = Ride("Mainland",
            "01st Apr 2022 01:30",1,1,
            "", 1, "Lagos", listOf(34,48,52,69,75,88))
        val ride2 = Ride("Benin",
            "02nd May 2023 14:30",2,2,
            "", 2, "Edo", listOf(37,50,54,62,77,90))
        val ride3 = Ride("Umaia",
            "03rd Feb 2022 01:30",3,3,
            "", 3, "Abia", listOf(39,52,58,73,79,93))

        @JvmStatic
        fun cityAndStateArguments() = Stream.of(
            Arguments.of("Victoria Island", "Lagos", listOf(ride4), user),
            Arguments.of("", "Lagos", listOf(ride1, ride4), user),
            Arguments.of("Mainland", "", listOf(ride1), user),
            Arguments.of("", "", listOf(ride1, ride2, ride3, ride4), user),
            Arguments.of("Victoria Island", "Lagos", listOf<Ride>(), null)
        )

    }

    @ParameterizedTest(name
    = "When City is {0}, State is {1} and User is {3}," +
            " filterListByStatesAndOrCities should return {2}")
    @MethodSource("cityAndStateArguments")
    fun test_filterListByStatesAndOrCities(cityName: String, stateName: String,
                                           expectedRides: List<Ride>, user: User?){
        //Act
        viewModel.setTheNearestRide(user)
        val actualRides = viewModel.filterListByStatesAndOrCities(cityName,stateName)

        //Assert
        assertEquals(expectedRides, actualRides)
    }

}