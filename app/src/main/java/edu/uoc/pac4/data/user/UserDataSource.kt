package edu.uoc.pac4.data.user

import android.util.Log
import edu.uoc.pac4.data.SessionManager
import edu.uoc.pac4.data.network.Endpoints
import edu.uoc.pac4.data.network.UnauthorizedException
import io.ktor.client.*
import io.ktor.client.features.*
import io.ktor.client.request.*

/**
 * Created by alex on 11/21/20.
 * Updated by david on 01/01/21.
 * UserDataSource Twitch API calls
 */

class UserDataSource (
        // Add http client and session manager like data source
        private val httpClient: HttpClient,
        private val sessionManager: SessionManager
        ) {

    /** companion object for log TAG **/
    companion object {
        private const val TAG = "UserDataSource"
    }

    /** Gets Current Authorized User on Twitch **/
    @Throws(UnauthorizedException::class)
    suspend fun getUser(): User? {
        try {
            val response = httpClient
                    .get<UsersResponse>(Endpoints.usersUrl)
            return response.data?.firstOrNull()
        } catch (t: Throwable) {
            Log.w(TAG, "Error getting user", t)
            // Try to handle error
            return when (t) {
                is ClientRequestException -> {
                    // Check if it's a 401 Unauthorized
                    if (t.response?.status?.value == 401) {
                        throw UnauthorizedException
                    }
                    null
                }
                else -> null
            }
        }
    }

    /** Update User Description on Twitch **/
    @Throws(UnauthorizedException::class)
    suspend fun updateUserDescription(description: String): User? {
        try {
            val response = httpClient
                    .put<UsersResponse>(Endpoints.usersUrl) {
                        parameter("description", description)
                    }
            return response.data?.firstOrNull()
        } catch (t: Throwable) {
            Log.w(TAG, "Error updating user description", t)
            // Try to handle error
            return when (t) {
                is ClientRequestException -> {
                    // Check if it's a 401 Unauthorized
                    if (t.response?.status?.value == 401) {
                        throw UnauthorizedException
                    }
                    null
                }
                else -> null
            }
        }
    }

    /** Logout session -> clear access token **/
    fun clearAccessToken() {
        sessionManager.clearAccessToken()
    }

    /** Logout session -> clear refresh token **/
    fun clearRefreshToken() {
        sessionManager.clearRefreshToken()
    }

}