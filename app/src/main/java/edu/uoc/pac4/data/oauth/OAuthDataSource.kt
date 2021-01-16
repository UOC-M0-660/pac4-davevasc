package edu.uoc.pac4.data.oauth

import android.util.Log
import edu.uoc.pac4.data.SessionManager
import edu.uoc.pac4.data.network.Endpoints
import io.ktor.client.*
import io.ktor.client.request.*

/**
 * Created by alex on 11/21/20.
 * Updated by david on 01/01/21.
 * OAuthDataSource Twitch API calls
 */

class OAuthDataSource(
        // Add http client and session manager like data sources
        private val httpClient: HttpClient,
        private val sessionManager: SessionManager
        ) {

    /** companion object for log TAG **/
    companion object {
        private const val TAG = "OAuthDataSource"
    }

    /** Gets if user is available **/
    fun isUserAvailable(): Boolean {
        return sessionManager.isUserAvailable()
    }

    /** Get and save Access and Refresh Tokens on Twitch **/
    suspend fun login(authorizationCode: String): Boolean {
        // Get Tokens from Twitch
        try {
            val response = httpClient
                    .post<OAuthTokensResponse>(Endpoints.tokenUrl) {
                        parameter("client_id", OAuthConstants.clientID)
                        parameter("client_secret", OAuthConstants.clientSecret)
                        parameter("code", authorizationCode)
                        parameter("grant_type", "authorization_code")
                        parameter("redirect_uri", OAuthConstants.redirectUri)
                    }
            // Save new Tokens
            sessionManager.saveAccessToken(response.accessToken)
            response.refreshToken?.let { sessionManager.saveRefreshToken(it) }
            return true
        } catch (t: Throwable) {
            Log.w(TAG, "Error Getting Access token", t)
            return false
        }
    }

    /** Logout session -> clear access and refresh token **/
    fun logout() {
        // Clear tokens
        sessionManager.clearAccessToken()
        sessionManager.clearRefreshToken()
    }

}