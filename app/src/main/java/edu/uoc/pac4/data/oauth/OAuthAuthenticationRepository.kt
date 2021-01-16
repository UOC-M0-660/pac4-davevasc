package edu.uoc.pac4.data.oauth

/**
 * Created by alex on 11/21/20.
 * Updated by david on 01/01/21.
 * Twitch OAuth Repository Implementation
 */

class OAuthAuthenticationRepository(
    // Add OAuth Data Source
    private val oauthDS: OAuthDataSource
) : AuthenticationRepository {

    // Get if user is logged
    override fun isUserAvailable(): Boolean {
        return oauthDS.isUserAvailable()
    }

    // Login in Twitch using authorization code
    override suspend fun login(authorizationCode: String): Boolean {
        return oauthDS.login(authorizationCode)
    }

    // Logout session clearing tokens by session manager
    override fun logout() {
        oauthDS.logout()
    }

}