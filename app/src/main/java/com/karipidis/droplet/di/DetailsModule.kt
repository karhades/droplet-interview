package com.karipidis.droplet.di

import android.content.ContentResolver
import android.content.Context
import com.karipidis.droplet.domain.repositories.UserRepository
import com.karipidis.droplet.domain.usecases.GetUserUseCase
import com.karipidis.droplet.domain.usecases.GetUserUseCaseImpl
import com.karipidis.droplet.domain.usecases.UpdateUserUseCase
import com.karipidis.droplet.domain.usecases.UpdateUserUseCaseImpl
import com.karipidis.droplet.presentation.details.*
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val detailsModule = module {
    viewModel { DetailsViewModel(get(), get(), get(), get(), get()) }
    single { provideObserveUserUseCase(get()) }
    single { provideDetailsUserMapper(get()) }
    single { provideImageEncoder() }
    single { provideContentResolver(get()) }
    single { provideUpdateUserUseCase(get()) }
    single { provideUserMapper(get()) }
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

private fun provideContentResolver(context: Context): ContentResolver {
    return context.contentResolver
}

private fun provideUpdateUserUseCase(userRepository: UserRepository): UpdateUserUseCase {
    return UpdateUserUseCaseImpl(userRepository)
}

private fun provideUserMapper(imageEncoder: ImageEncoder): UserMapper {
    return UserMapper(imageEncoder)
}