package com.ddr1.storyapp.di

import com.ddr1.storyapp.BuildConfig
import com.ddr1.storyapp.data.local.UserPreference
import com.ddr1.storyapp.data.remote.api.ApiService
import com.ddr1.storyapp.utils.AppConstants.TIME_OUT
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Singleton
    @Provides
    fun provideRetrofit(okHttp: OkHttpClient): Retrofit {
        return Retrofit.Builder().apply {
            addConverterFactory(GsonConverterFactory.create())
            client(okHttp)
            baseUrl(BuildConfig.BASE_URL)
        }.build()
    }

    @Provides
    @Singleton
    fun provideRequestInterceptor(userPreference: UserPreference): Interceptor =
        Interceptor { chain ->
            val token = runBlocking {
                userPreference.getUserToken().first()
            }
            val request = chain.request()
            val newBuilder = request.newBuilder()
            newBuilder.header(
                "Authorization",
                "Bearer $token"
            )
            chain.proceed(newBuilder.build())
        }

    @Singleton
    @Provides
    fun provideOkHttp(requestInterceptor: Interceptor): OkHttpClient {
        return OkHttpClient.Builder().apply {

            val loggingInterceptor = if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
            } else {
                HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.NONE)
            }

            connectTimeout(TIME_OUT, TimeUnit.SECONDS)
            readTimeout(TIME_OUT, TimeUnit.SECONDS)
            writeTimeout(TIME_OUT, TimeUnit.SECONDS)
            addInterceptor(requestInterceptor)
            addInterceptor(loggingInterceptor)
        }.build()
    }

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): ApiService = retrofit.create(ApiService::class.java)
}
