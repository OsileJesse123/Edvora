package com.jesse.edvaro.utils


import android.app.Activity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import com.jesse.edvaro.data.model.Ride
import com.jesse.edvaro.data.model.StationCodeToRide
//import com.jesse.edvaro.data.network.Ride
import com.jesse.edvaro.presentation.viewModel.EdvoraActivityViewModelFactory
import java.text.SimpleDateFormat
import java.util.*

enum class RideState{UPCOMING, CURRENT, PAST}

private val listOfMonths = listOf("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep",
    "Oct", "Nov", "Dec")

object RideData {

    fun getNearestStation(userStation: Int, rides: List<Ride>): List<Ride> {
        val stationCodeToRides = mutableListOf<StationCodeToRide>()
        var count = 0
        val updatedListOfRides = mutableListOf<Ride>()

        rides.forEach{
                r ->
            for(path in r.stationPath){
                if (path >= userStation){
                    val codeToRide= StationCodeToRide(path, r)
                    stationCodeToRides.add(codeToRide)
                    break
                }
            }
        }

        val ridesSize = stationCodeToRides.size

        while(count < ridesSize){
            val newRide = stationCodeToRides.minByOrNull { it.code }
            newRide?.let {
                updatedListOfRides.add(it.ride)
                stationCodeToRides.remove(it)
            }
            count++
        }
        return updatedListOfRides
    }

}

//fun <T: ViewModel> Activity.obtainViewModel(viewModelClass: Class<T>): T{
//    val application = this.application as EdvoraApplication
//    val userRepo = application.userRepo
//    val rideRepo = application.rideRepo
//    val viewModelFactory = EdvoraActivityViewModelFactory(rideRepo, userRepo)
//
//    return ViewModelProvider(this as ViewModelStoreOwner, viewModelFactory).get(viewModelClass)
//}

//I assume that the Ride object date details(day, month, year and time) are upcoming
//i.e. in the future. The current date details is obtained from
//the user's device at that point in time as a Date object converted to a string.
// So when comparing both ride and date current details by subtracting current from ride, results
//greater than zero means the ride object is upcoming, less than zero
//means it is past and equals to zero means it is current

object UserCurrentDate{
    val userCurrentDate: String get() {
        val spf = SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.getDefault())
        val currentTime = Calendar.getInstance().time
        val stringCurrentTime = spf.format(currentTime)
        return formatRideDate(stringCurrentTime)
    }
}
fun isDayUpcomingOrPastOrCurrent(rideDay: String, currentDay: String): RideState{

    //the day obtained from the ride's date
    val theRideDay = rideDay.toInt()

    //the current day as obtained from the user's device
    val theCurrentDay = currentDay.toInt()

    val daysDifference = theRideDay - theCurrentDay

    return when {
        daysDifference > 0 -> {
            RideState.UPCOMING
        }
        daysDifference < 0 -> {
            RideState.PAST
        }
        else -> {
            RideState.CURRENT
        }
    }
}

fun isMonthUpcomingOrPastOrCurrent(rideMonth: String, currentMonth: String): RideState{

    val rideMonthIndex = listOfMonths.indexOf(rideMonth)
    val currentMonthIndex = listOfMonths.indexOf(currentMonth)
    val monthIndexDifference = rideMonthIndex - currentMonthIndex

    return when {
        monthIndexDifference > 0 -> RideState.UPCOMING
        monthIndexDifference < 0 -> RideState.PAST
        else -> RideState.CURRENT
    }
}

fun isYearUpcomingOrPastOrCurrent(rideYear: String, currentYear: String): RideState{
    val theRideYear = rideYear.toInt()
    val theCurrentYear = currentYear.toInt()
    val theYearDifference = theRideYear - theCurrentYear

    return when {
        theYearDifference > 0 -> RideState.UPCOMING
        theYearDifference < 0 -> RideState.PAST
        else -> RideState.CURRENT
    }
}

fun isTimeUpcomingOrPastOrCurrent(rideTime: String, currentTime: String):RideState{

    var currentHour = ""
    var rideHour = ""

    var currentMins = ""
    var rideMins = ""

    var count = 0

    for (c in currentTime) {
        if (count < 2) {
            currentHour += c
        }
        if (count > 2) {
            currentMins += c
        }
        count++
    }
    count = 0
    for (f in rideTime) {
        if (count < 2) {
            rideHour += f
        }
        if (count > 2) {
            rideMins += f
        }
        count++
    }
    val theCurrentHourToMins = (rideHour.toInt() - currentHour.toInt())
    val theCurrentMins = (rideMins.toInt() - currentMins.toInt())

    return if(theCurrentHourToMins > 0){
        RideState.UPCOMING
    } else if(theCurrentHourToMins < 0){
        RideState.PAST
    } else {
        if(theCurrentMins > 0){
            RideState.UPCOMING
        } else if (theCurrentMins < 0){
            RideState.PAST
        } else {
            RideState.CURRENT
        }
    }
}

fun formatDateToListOfDetails(date: String): List<String>{
    var day = ""
    var month = ""
    var year = ""
    var time = ""
    var count = 0
    val dateDetails = mutableListOf<String>()

    for(i in date){
        if(count < 2){
            day += i
        }
        if(count in 5..7){
            month += i
        }
        if(count in 9..12){
            year += i
        }
        if(count in 14 until date.length){
            time += i
        }
        count++
    }
    dateDetails.add(day)
    dateDetails.add(month)
    dateDetails.add(year)
    dateDetails.add(time)

    return dateDetails
}

fun isRideUpcomingOrPastOrCurrent(rideDate: String, currentDate: String): RideState{

    val day = 0
    val month = 1
    val year = 2
    val time = 3

    val rideDateDetails = formatDateToListOfDetails(rideDate)
    val currentDateDetails = formatDateToListOfDetails(currentDate)

    val yearState = isYearUpcomingOrPastOrCurrent(rideDateDetails[year], currentDateDetails[year])
    if (yearState == RideState.CURRENT){
        val monthState = isMonthUpcomingOrPastOrCurrent(rideDateDetails[month],
            currentDateDetails[month])
        if (monthState == RideState.CURRENT){
            val dayState = isDayUpcomingOrPastOrCurrent(rideDateDetails[day],
                currentDateDetails[day])
            if(dayState == RideState.CURRENT) {
                return isTimeUpcomingOrPastOrCurrent(
                    rideDateDetails[time],
                    rideDateDetails[time]
                )
            } else {
                return dayState
            }
        } else {
            return monthState
        }
    }else {
        return yearState
    }
}


fun formatRideDate(rideDate: String):String{
    var count = 0
    var updatedDate = ""
    var date = rideDate
    var spf = SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.getDefault())
    val newDate = spf.parse(date)
    spf = SimpleDateFormat("dd MMM yyyy HH:mm", Locale.getDefault())
    date = spf.format(newDate)
    for(i in date){
        if(count < 2){
            updatedDate += i
        }
        if(count == 2){
            updatedDate += when(updatedDate){
                "01" -> "st "
                "02"-> "nd "
                "03" -> "rd "
                else -> "th "
            }
        }
        if(count > 2){
            updatedDate += i
        }
        count++
    }
    return updatedDate
}

fun formatReceivedText(text: String, textLimit: Int): String{
    //Note that the count starts from 0 so text greater than text limit
    //will be truncated from 0 down to the text limit
    var formattedText = ""
    var count = 0
    return if(text.length > textLimit){
        for(t in text){
            if(count == textLimit){
                formattedText += "..."
                break
            } else {
                formattedText += t
                count ++
            }
        }
        formattedText
    } else {
        text
    }
}

fun mapStatesToCities(rides: List<Ride>): List<Map<String, List<String>>> {
    val states = rides.map { it.state }.toSet().toList()
    val statesToCities = mutableListOf<Map<String, List<String>>>()
    for(state in states){
        val cities = rides.filter { it.state == state }.map { it.city }.toSet().toList()
        val stateToCities = mapOf(state to cities)
        statesToCities.add(stateToCities)
    }
    return statesToCities
}

