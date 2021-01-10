package edu.uoc.pac4.data.streams

/**
 * Created by alex on 11/21/20.
 * Updated by david on 01/01/21.
 * Twitch Streams Repository Implementation
 */

class TwitchStreamsRepository(
        private val streamsDS: StreamsDataSource
) : StreamsRepository {

    // Get streams from twitch
    override suspend fun getStreams(cursor: String?): Pair<String?, List<Stream>> {
        // Get API response
        val response = streamsDS.getStreams(cursor)
        // Return pair object, cursor and streams list
        return Pair(response?.pagination?.cursor, response?.data.orEmpty())
    }

    // Clear access token by session manager
    override fun clearAccessToken() {
        streamsDS.clearAccessToken()
    }

}