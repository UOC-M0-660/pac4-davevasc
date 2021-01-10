package edu.uoc.pac4.data.network

import android.content.Context
import android.util.Log
import edu.uoc.pac4.data.oauth.OAuthTokensResponse
import edu.uoc.pac4.data.oauth.OAuthConstants
import edu.uoc.pac4.data.SessionManager
import io.ktor.client.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.features.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.features.logging.*
import io.ktor.client.request.*
import io.ktor.http.*

/**
 * Created by alex on 07/09/2020.
 */
class Network(
        // Add session manager like data source
        private val sessionManager: SessionManager
) {

    companion object {
        private const val TAG = "Network"
    }

    fun createHttpClient(context: Context): HttpClient {
        return HttpClient(OkHttp) {
            // Json
            install(JsonFeature) {
                serializer = KotlinxSerializer(json)
            }
            // Logging
            install(Logging) {
                logger = object : Logger {
                    override fun log(message: String) {
                        Log.v(TAG, message)
                    }
                }
                level = LogLevel.ALL
            }
            // Timeout
            install(HttpTimeout) {
                requestTimeoutMillis = 15000L
                connectTimeoutMillis = 15000L
                socketTimeoutMillis = 15000L
            }
            // Apply to All Requests
            defaultRequest {
                // Content Type
                if (this.method != HttpMethod.Get) contentType(ContentType.Application.Json)
                accept(ContentType.Application.Json)

                // Twitch Client ID Header
                if (!headers.contains("client-id"))
                    header("client-id", OAuthConstants.clientID)
            }

            // Add OAuth Feature
            install(OAuthFeature) {
                getToken = {
                    val accessToken = sessionManager.getAccessToken() ?: ""
                    Log.d(TAG, "Adding Bearer header with token $accessToken")
                    accessToken
                }
                refreshToken = {
                    // Remove expired access token
                    sessionManager.clearAccessToken()
                    // Launch token refresh request
                    launchTokenRefresh(context)
                }
            }
        }
    }

    private val json = kotlinx.serialization.json.Json {
        ignoreUnknownKeys = true
        isLenient = true
        encodeDefaults = false
    }

    private suspend fun launchTokenRefresh(context: Context) {
        // Get Refresh Token from data source
        sessionManager.getRefreshToken()?.let { refreshToken ->
            try {
                // Launch Refresh Request
                val response =
                        createHttpClient(context).post<OAuthTokensResponse>(Endpoints.tokenUrl) {
                            parameter("client_id", OAuthConstants.clientID)
                            parameter("client_secret", OAuthConstants.clientSecret)
                            parameter("refresh_token", refreshToken)
                            parameter("grant_type", "refresh_token")
                        }
                Log.d(TAG, "Got new Access token ${response.accessToken}")
                // Save new Tokens
                sessionManager.saveAccessToken(response.accessToken)
                response.refreshToken?.let { sessionManager.saveRefreshToken(it) }
            } catch (t: Throwable) {
                Log.d(TAG, "Error refreshing tokens", t)
                // Clear tokens
                sessionManager.clearAccessToken()
                sessionManager.clearRefreshToken()
            }
        } ?: run {
            Log.e(TAG, "No refresh token available")
            // Clear token
            sessionManager.clearAccessToken()
        }
    }
}