package com.jesse.edvaro

import com.jesse.edvaro.data.network.RideService
import com.jesse.edvaro.data.network.User
import com.jesse.edvaro.data.repository.UserRepository
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class UserRepositoryTest {

    @get:Rule
    val coroutineDispatcherRule = CoroutineDispatcherRule()

    @Test
    fun test_getUser(){
        runBlockingTest {
            //Arrange
            val expectedUser = User(342, "Jesse Osile", "image_url")
            val rideService: RideService = mock{
                whenever(it.getUser()).thenReturn(expectedUser)
            }
            val userRepo = UserRepository(rideService)

            //Act
            val actualUser = userRepo.getUser()

            //Assert
            assertEquals(expectedUser, actualUser)
        }
    }

}