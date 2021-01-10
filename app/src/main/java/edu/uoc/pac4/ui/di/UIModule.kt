package edu.uoc.pac4.ui.di

import edu.uoc.pac4.ui.LaunchViewModel
import edu.uoc.pac4.ui.login.oauth.OAuthViewModel
import edu.uoc.pac4.ui.profile.ProfileViewModel
import edu.uoc.pac4.ui.streams.StreamsViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

/**
 * Created by alex on 11/21/20.
 * Updated by david on 1/01/2021
 * UI Dependencies definitions
 */
val uiModule = module {
    // LaunchViewModel ViewModel
    viewModel { LaunchViewModel(get()) }
    // OAuthViewModel ViewModel
    viewModel { OAuthViewModel(get()) }
    // StreamsViewModel ViewModel
    viewModel { StreamsViewModel(get()) }
    // ProfileViewModel ViewModel
    viewModel { ProfileViewModel(get()) }
}