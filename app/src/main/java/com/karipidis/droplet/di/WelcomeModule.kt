package com.karipidis.droplet.di

import com.karipidis.droplet.domain.repositories.UserRepository
import com.karipidis.droplet.domain.usecases.GetUserIdUseCase
import com.karipidis.droplet.domain.usecases.GetUserIdUseCaseImpl
import com.karipidis.droplet.presentation.welcome.WelcomeViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val welcomeModule = module {
    viewModel { WelcomeViewModel(get()) }
    single { provideGetUserIdUseCase(get()) }
}

private fun provideGetUserIdUseCase(userRepository: UserRepository): GetUserIdUseCase {
    return GetUserIdUseCaseImpl(userRepository)
}