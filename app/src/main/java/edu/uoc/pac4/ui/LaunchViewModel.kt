package edu.uoc.pac4.ui

import androidx.lifecycle.*
import edu.uoc.pac4.data.oauth.AuthenticationRepository

/**
 * Created by alex on 11/21/20.
 * Updated by david on 01/01/21.
 * Launch Activity View Model
 */

// This is a simple ViewModel example,
// you can connect to it from the LaunchActivity and use it
// or just remove it
class LaunchViewModel(
    private val repository: AuthenticationRepository
) : ViewModel() {

    // Live Data
    val isUserAvailable = MutableLiveData<Boolean>()

    // Public function that can be called from the view (Activity)
    fun getUserAvailability() {
        // Get User Availability from Repository
        isUserAvailable.value = repository.isUserAvailable()
    }
}