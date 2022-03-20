package com.jesse.edvaro.presentation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.jesse.edvaro.data.repository.RideRepo
import com.jesse.edvaro.data.repository.UserRepo
import java.lang.IllegalStateException

@Suppress("UNCHECKED_CAST")
class EdvoraActivityViewModelFactory(private val rideRepo: RideRepo,
                                     private val userRepo: UserRepo
): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EdvoraActivityViewModel::class.java)){
            return EdvoraActivityViewModel(rideRepo, userRepo) as T
        }
        throw IllegalStateException("Unknown Class type")
    }
}