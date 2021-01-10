package edu.uoc.pac4.data.di

import edu.uoc.pac4.data.SessionManager
import edu.uoc.pac4.data.network.Network
import edu.uoc.pac4.data.oauth.AuthenticationRepository
import edu.uoc.pac4.data.oauth.OAuthAuthenticationRepository
import edu.uoc.pac4.data.oauth.OAuthDataSource
import edu.uoc.pac4.data.streams.StreamsDataSource
import edu.uoc.pac4.data.streams.StreamsRepository
import edu.uoc.pac4.data.streams.TwitchStreamsRepository
import edu.uoc.pac4.data.user.TwitchUserRepository
import edu.uoc.pac4.data.user.UserDataSource
import edu.uoc.pac4.data.user.UserRepository
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

/**
 * Created by alex on 11/21/20.
 * Updated by david on 01/01/2021
 * Data Dependencies definitions
 */
val dataModule = module {

    // Single instance of OAuth Repository
    single<AuthenticationRepository> { OAuthAuthenticationRepository(get()) }
    // Single instance of OAuth Data Source
    single { OAuthDataSource(get(), get()) }

    // Single instance of Streams Repository
    single<StreamsRepository> { TwitchStreamsRepository(get()) }
    // Single instance of Streams Data Source
    single { StreamsDataSource(get(), get()) }

    // Single instance of User Repository
    single<UserRepository> { TwitchUserRepository(get()) }
    // Single instance of User Data Source
    single { UserDataSource(get(), get()) }

    // Single instance of Network Source with context
    single { Network(get()).createHttpClient(androidContext()) }
    // Single instance of SessionManager Source witch context
    single { SessionManager(androidContext())}
}