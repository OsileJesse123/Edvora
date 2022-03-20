package com.jesse.edvaro

import com.jesse.edvaro.data.network.Ride
import com.jesse.edvaro.data.network.RideService
import com.jesse.edvaro.data.repository.RideRepository
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class RideRepositoryTest {

    @get:Rule
    val couroutineDispatcherRule = CoroutineDispatcherRule()

    @Test
    fun test_getRides(){
        runBlockingTest {
            //Arrange
            val date = "01/04/2022 01:30 AM"
            val rides = listOf(
                Ride("Mainland",
                    "01/04/2022 01:30 AM",1,1,"", 1, "Lagos", listOf())
            )
            val expectedRides = listOf(
                Ride("Mainland",
                    "01st Apr 2022 01:30",1,1,"", 1, "Lagos", listOf())
            )
            val rideService: RideService = mock{
                whenever(it.getRides()).thenReturn(rides)
            }
            val rideRepo = RideRepository(rideService)

            //Act
            val actualRides = rideRepo.getRides()

            //Assert
            assertEquals(expectedRides, actualRides)
        }
    }
}