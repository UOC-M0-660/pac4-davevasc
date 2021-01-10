package edu.uoc.pac4.data.user

/**
 * Created by alex on 11/21/20.
 * Updated by david on 01/01/21.
 * Twitch User Repository Implementation
 */

class TwitchUserRepository(
        private val userDS: UserDataSource
) : UserRepository {

    // Get user from Twitch
    override suspend fun getUser(): User? {
        return userDS.getUser()
    }

    // Update user description on Twitch
    override suspend fun updateUser(description: String): User? {
        return userDS.updateUserDescription(description)
    }

    // Clear access token by session manager
    override fun clearAccessToken() {
        userDS.clearAccessToken()
    }

    // Clear refresh token by session manager
    override fun clearRefreshToken() {
        userDS.clearRefreshToken()
    }

}