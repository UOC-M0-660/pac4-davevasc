package edu.uoc.pac4.data.streams

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
 * StreamsDataSource Twitch API calls
 */

class StreamsDataSource(
        // Add http client and session manager like data source
        private val httpClient: HttpClient,
        private val sessionManager: SessionManager
        ) {

    /** companion object for log TAG **/
    companion object {
        private const val TAG = "StreamsDataSource"
    }

    /** Gets Streams on Twitch **/
    @Throws(UnauthorizedException::class)
    suspend fun getStreams(cursor: String? = null): StreamsResponse? {
        try {
            return httpClient
                    .get<StreamsResponse>(Endpoints.streamsUrl) {
                        cursor?.let { parameter("after", it) }
                    }
        } catch (t: Throwable) {
            Log.w(TAG, "Error getting streams", t)
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

}