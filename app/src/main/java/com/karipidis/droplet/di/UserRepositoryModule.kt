package com.karipidis.droplet.di

import android.content.Context
import androidx.room.Room
import com.google.firebase.auth.FirebaseAuth
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.karipidis.droplet.data.UserRepositoryImpl
import com.karipidis.droplet.data.local.UserDao
import com.karipidis.droplet.data.local.UserDatabase
import com.karipidis.droplet.data.remote.UserApi
import com.karipidis.droplet.data.mappers.LocalUserMapper
import com.karipidis.droplet.data.mappers.UserMapper
import com.karipidis.droplet.domain.repositories.UserRepository
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

private const val BASE_URL = "https://us-central1-test-project-mc-46908.cloudfunctions.net/"
private const val DATABASE_NAME = "users.db"

val userRepositoryModule = module {
    single { provideUserRepository(get(), get(), get(), get(), get()) }
    single { provideFirebaseAuth() }
    single { provideRetrofit(get(), get()) }
    single { provideOkHttpClient(get()) }
    single { provideHttpLoggingInterceptor() }
    single { provideGson() }
    single { provideUserApi(get()) }
    single { provideUserMapper() }
    single { provideUserDatabase(get()) }
    single { provideUserDao(get()) }
    single { provideLocalUserMapper() }
}

private fun provideUserRepository(
    firebaseAuth: FirebaseAuth,
    userApi: UserApi,
    userMapper: UserMapper,
    userDao: UserDao,
    localUserMapper: LocalUserMapper
): UserRepository {
    return UserRepositoryImpl(firebaseAuth, userApi, userMapper, userDao, localUserMapper)
}

private fun provideFirebaseAuth(): FirebaseAuth {
    return FirebaseAuth.getInstance()
}

private fun provideRetrofit(okHttpClient: OkHttpClient, gson: Gson): Retrofit {
    return Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()
}

private fun provideOkHttpClient(
    httpLoggingInterceptor: HttpLoggingInterceptor
): OkHttpClient {
    return OkHttpClient.Builder()
        .addInterceptor(httpLoggingInterceptor)
        .connectTimeout(15, TimeUnit.SECONDS)
        .readTimeout(15, TimeUnit.SECONDS)
        .build()
}

private fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
    return HttpLoggingInterceptor()
        .setLevel(HttpLoggingInterceptor.Level.BODY)
}

private fun provideGson(): Gson {
    return GsonBuilder()
        .create()
}

private fun provideUserApi(retrofit: Retrofit): UserApi {
    return retrofit.create(UserApi::class.java)
}

private fun provideUserMapper(): UserMapper {
    return UserMapper()
}

private fun provideUserDatabase(context: Context): UserDatabase {
    return Room.databaseBuilder(context, UserDatabase::class.java, DATABASE_NAME)
        .build()
}

private fun provideUserDao(userDatabase: UserDatabase): UserDao {
    return userDatabase.userDao()
}

private fun provideLocalUserMapper(): LocalUserMapper {
    return LocalUserMapper()
}