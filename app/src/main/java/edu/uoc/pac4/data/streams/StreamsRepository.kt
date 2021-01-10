package edu.uoc.pac4.data.streams

/**
 * Created by alex on 11/21/20.
 * Updated by david on 01/01/2021.
 * Streams Repository Interface
 */

interface StreamsRepository {
    // Get Streams: Returns a Pair object containing Pagination cursor and List of Streams
    suspend fun getStreams(cursor: String? = null): Pair<String?, List<Stream>>

    // Clear access token
    fun clearAccessToken()

}