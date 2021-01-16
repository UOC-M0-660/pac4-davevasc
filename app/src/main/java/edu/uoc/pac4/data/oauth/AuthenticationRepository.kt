package edu.uoc.pac4.data.oauth

/**
 * Created by alex on 11/21/20.
 * Updated by david on 01/01/2021.
 * OAuth Repository Interface
 */

interface AuthenticationRepository {

    // Return if exist user logged
    fun isUserAvailable(): Boolean

    // Returns true if the user logged in successfully. False otherwise
    suspend fun login(authorizationCode: String): Boolean

    // Logout session
    fun logout()

}