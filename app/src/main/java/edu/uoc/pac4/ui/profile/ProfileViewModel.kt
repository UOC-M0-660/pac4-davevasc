package edu.uoc.pac4.ui.profile

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.uoc.pac4.data.user.User
import edu.uoc.pac4.data.user.UserRepository
import kotlinx.coroutines.launch

/**
 * Created by alex on 11/21/20.
 * Updated by david on 01/01/21.
 * Profile Activity View Model
 */

class ProfileViewModel(
        private val repository: UserRepository
) : ViewModel() {

    // Live Data: user and description
    val userGetted = MutableLiveData<User>()
    val userUpdate = MutableLiveData<User>()

    // Public function that can be called from ProfileActivity
    fun getUser() {
        // Get Availability from Repository and post result
        viewModelScope.launch {
            userGetted.postValue(repository.getUser())
        }
    }
    // Public function that can be called from ProfileActivity
    fun updateUser (description: String) {
        // Get Availability from Repository and send value of user description
        viewModelScope.launch {
            userUpdate.postValue(repository.updateUser(description))
        }
    }

    // Call to clear access token
    fun clearAccessToken() {
        repository.clearAccessToken()
    }

    // Call to clear refresh token
    fun clearRefreshToken() {
        repository.clearRefreshToken()
    }

}