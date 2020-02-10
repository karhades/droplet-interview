package com.karipidis.droplet.di

import com.karipidis.droplet.domain.repositories.UserRepository
import com.karipidis.droplet.domain.usecases.GetUserUseCase
import com.karipidis.droplet.domain.usecases.GetUserUseCaseImpl
import com.karipidis.droplet.presentation.details.DetailsUserMapper
import com.karipidis.droplet.presentation.details.DetailsViewModel
import com.karipidis.droplet.presentation.details.ImageEncoder
import com.karipidis.droplet.presentation.details.ImageEncoderImpl
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val detailsModule = module {
    viewModel { DetailsViewModel(get(), get()) }
    single { provideObserveUserUseCase(get()) }
    single { provideDetailsUserMapper(get()) }
    single { provideImageEncoder() }
}

private fun provideObserveUserUseCase(userRepository: UserRepository): GetUserUseCase {
    return GetUserUseCaseImpl(userRepository)
}

private fun provideDetailsUserMapper(imageEncoder: ImageEncoder): DetailsUserMapper {
    return DetailsUserMapper(imageEncoder)
}

private fun provideImageEncoder(): ImageEncoder {
    return ImageEncoderImpl()
}