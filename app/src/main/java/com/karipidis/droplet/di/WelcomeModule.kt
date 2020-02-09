package com.karipidis.droplet.di

import com.google.firebase.auth.FirebaseAuth
import com.karipidis.droplet.domain.repositories.UserRepository
import com.karipidis.droplet.data.UserRepositoryImpl
import com.karipidis.droplet.domain.usecases.GetUserIdUseCase
import com.karipidis.droplet.domain.usecases.GetUserIdUseCaseImpl
import com.karipidis.droplet.presentation.welcome.WelcomeViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val welcomeModule = module {
    viewModel { WelcomeViewModel(get()) }
    single { provideGetUserIdUseCase(get()) }
    single { provideUserRepository(get()) }
    single { provideFirebaseAuth() }
}

private fun provideGetUserIdUseCase(userRepository: UserRepository): GetUserIdUseCase {
    return GetUserIdUseCaseImpl(userRepository)
}

private fun provideUserRepository(firebaseAuth: FirebaseAuth): UserRepository {
    return UserRepositoryImpl(firebaseAuth)
}

private fun provideFirebaseAuth(): FirebaseAuth {
    return FirebaseAuth.getInstance()
}