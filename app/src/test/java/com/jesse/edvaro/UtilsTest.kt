package com.jesse.edvaro


import com.jesse.edvaro.data.network.Ride
import com.jesse.edvaro.utils.*
import junit.framework.TestCase.assertEquals
import org.junit.Test

class UtilsTest {

    @Test
    fun test_formatRideDate(){
       //Arrange
        val date = "01/04/2022 01:30 AM"
        val date2 = "02/05/2022 01:30 PM"
        val expectedFormattedDate = "01st Apr 2022 01:30"
        val expectedFormattedDate2 = "02nd May 2022 13:30"

       //Act
        val actualFormattedDate = formatRideDate(date)
        val actualFormattedDate2 = formatRideDate(date2)

        val rides = listOf(
            Ride("Mainland",
                "01/04/2022 01:30 AM",1,1,"", 1, "Lagos", listOf())
        )
       //Assert
       assertEquals(expectedFormattedDate, actualFormattedDate)
       assertEquals(expectedFormattedDate2, actualFormattedDate2)
    }

    @Test
    fun test_formatDateToListOfDetails(){
        //Arrange
        val dateFormat = "01st Apr 2022 01:30"
        val expectedDateToListOfDetails = listOf("01", "Apr", "2022", "01:30")

        //Act
        val actualDateToListOfDetails = formatDateToListOfDetails(dateFormat)

        //Assert
        assertEquals(expectedDateToListOfDetails, actualDateToListOfDetails)
    }

    @Test
    fun test_isDayUpcomingOrPastOrCurrent(){
       //Arrange
       val pastRideDay = "03"
       val presentRideDay = "05"
       val upcomingRideDay = "07"
       val currentDay = "05"

      //Act
      val pastDayState = isDayUpcomingOrPastOrCurrent(pastRideDay, currentDay)
      val presentDayState = isDayUpcomingOrPastOrCurrent(presentRideDay, currentDay)
      val upcomingDayState = isDayUpcomingOrPastOrCurrent(upcomingRideDay, currentDay)

      //Assert
      assertEquals(RideState.PAST, pastDayState)
      assertEquals(RideState.CURRENT, presentDayState)
      assertEquals(RideState.UPCOMING, upcomingDayState)
    }

    @Test
    fun test_isMonthUpcomingOrPastOrCurrent(){
        //Arrange
        val pastRideMonth = "Jan"
        val presentRideMonth = "Mar"
        val upcomingRideMonth = "Apr"
        val currentMonth = "Mar"

        //Act
        val pastMonthState = isMonthUpcomingOrPastOrCurrent(pastRideMonth, currentMonth)
        val presentMonthState = isMonthUpcomingOrPastOrCurrent(presentRideMonth, currentMonth)
        val upcomingMonthState = isMonthUpcomingOrPastOrCurrent(upcomingRideMonth, currentMonth)

        //Assert
        assertEquals(RideState.PAST, pastMonthState)
        assertEquals(RideState.UPCOMING, upcomingMonthState)
        assertEquals(RideState.CURRENT, presentMonthState)
    }

    @Test
    fun test_isYearUpcomingOrPastOrCurrent(){
        //Arrange
        val pastRideYear = "2021"
        val presentRideYear = "2022"
        val upcomingRideYear = "2023"
        val currentYear = "2022"

        //Act
        val pastYearState = isYearUpcomingOrPastOrCurrent(pastRideYear, currentYear)
        val presentYearState = isYearUpcomingOrPastOrCurrent(presentRideYear, currentYear)
        val upcomingYearState = isYearUpcomingOrPastOrCurrent(upcomingRideYear, currentYear)

        //Assert
        assertEquals(RideState.PAST, pastYearState)
        assertEquals(RideState.CURRENT, presentYearState)
        assertEquals(RideState.UPCOMING, upcomingYearState)
    }

    @Test
    fun test_isTimeUpcomingOrPastOrCurrent(){
        //Arrange
        val currentTime = "15:18"
        val pastRideTime = "01:15"
        val currentRideTime = "15:18"
        val upcomingRideTime = "17:30"

        //Act
        val pastTimeState = isTimeUpcomingOrPastOrCurrent(pastRideTime, currentTime)
        val currentTimeState = isTimeUpcomingOrPastOrCurrent(currentRideTime, currentTime)
        val upcomingTimeState = isTimeUpcomingOrPastOrCurrent(upcomingRideTime, currentTime)

        //Assert
        assertEquals(RideState.PAST, pastTimeState)
        assertEquals(RideState.CURRENT, currentTimeState)
        assertEquals(RideState.UPCOMING, upcomingTimeState)
    }

    @Test
    fun test_formatReceivedText(){
        //Arrange
        val textLimit = 11
        val smallText = "Jesse"
        val largeText = "Jesse is a great developer"
        val expectedLargeText = "Jesse is a ..."

        //Act
        val formattedSmallText = formatReceivedText(smallText, textLimit)
        val formattedLargeText = formatReceivedText(largeText, textLimit)

        //Assert
        assertEquals(expectedLargeText, formattedLargeText)
        assertEquals(smallText, formattedSmallText)
    }

    @Test
    fun test_mapStatesToCities(){
        //Arrange
        val ride1 = Ride("Mainland",
            "",1,1,"", 1, "Lagos", listOf())
        val ride2 = Ride("Island",
            "",2,2,"", 2, "Lagos", listOf())
        val ride3 = Ride("Benin",
            "",3,3,"", 3, "Edo", listOf())
        val ride4 = Ride("Umaia",
            "",4,4,"", 4, "Abia", listOf())
        val rides = listOf(ride1, ride2, ride3, ride4)
        val expectedStatesToCities = listOf(mapOf("Lagos" to listOf("Mainland","Island")),
            mapOf("Edo" to listOf("Benin")), mapOf("Abia" to listOf("Umaia")))

        //Act
        val actualStatesToCities = mapStatesToCities(rides)

        //Assert
        assertEquals(expectedStatesToCities, actualStatesToCities)
    }

    @Test
    fun test_isRideUpcomingOrPastOrCurrent(){
        //Arrange
        val upcomingRideDate = "14th Mar 2023 06:08 "
        val pastRideDate = "04th Feb 2022 19:17"
        val currentRideDate = "13th Mar 2022 19:18"

        //Act
        val upcomingRideState = isRideUpcomingOrPastOrCurrent(upcomingRideDate, currentRideDate)
        val pastRideState = isRideUpcomingOrPastOrCurrent(pastRideDate, currentRideDate)
        val currentRideState = isRideUpcomingOrPastOrCurrent(currentRideDate, currentRideDate)

        //Assert
        assertEquals(RideState.UPCOMING, upcomingRideState)
        assertEquals(RideState.PAST, pastRideState)
        assertEquals(RideState.CURRENT, currentRideState)
    }
}