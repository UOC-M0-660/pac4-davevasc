package edu.uoc.pac4.ui.login.oauth

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.uoc.pac4.data.oauth.AuthenticationRepository
import kotlinx.coroutines.launch

/**
 * Created by alex on 11/21/20.
 * Updated by david on 01/01/21.
 * OAuth Activity View Model
 */

class OAuthViewModel(
        private val repository: AuthenticationRepository
) : ViewModel() {

    // Live Data
    val login = MutableLiveData<Boolean>()

    // Public function that can be called from the view (Activity)
    fun doLogin(authorizationCode: String) {
        viewModelScope.launch {
            login.postValue(repository.login(authorizationCode))
        }
    }

    // Clear tokens
    fun logout() {
        repository.logout()
    }



}