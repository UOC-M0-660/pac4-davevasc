package edu.uoc.pac4.data.user

/**
 * Created by alex on 11/21/20.
 * Updated by david on 01/01/2021.
 * User Repository Interface
 */

interface UserRepository {

    // Get user from Twitch
    suspend fun getUser(): User?

    // Update user description on Twitch
    suspend fun updateUser(description: String): User?

    // Clear Access token by session manager
    fun clearAccessToken()

    // Clear Refresh token by session manager
    fun clearRefreshToken()

}