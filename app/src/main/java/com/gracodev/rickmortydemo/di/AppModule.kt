package com.gracodev.rickmortydemo.di

import android.content.Context
import com.google.gson.GsonBuilder
import com.gracodev.rickmortydemo.data.datasource.RickAndMortyAPI
import com.gracodev.rickmortydemo.data.factories.MainViewModelFactory
import com.gracodev.rickmortydemo.data.repository.RickAndMortyRepository
import com.gracodev.rickmortydemo.domain.datasource.GetCharacterUseCaseImpl
import com.gracodev.rickmortydemo.presentation.interceptors.NetworkConnectionInterceptor
import com.gracodev.rickmortydemo.presentation.viewmodels.MainViewModel
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.OkHttpClient
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module
import retrofit2.CallAdapter
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

fun createAppModules(): Module = module {

    single {
        createWebService<RickAndMortyAPI>(
            okHttpClient = createHttpClient(get()),
            factory = RxJava2CallAdapterFactory.create(),
            baseUrl = "https://rickandmortyapi.com/api/"
        )
    }
    single<RickAndMortyRepository> { GetCharacterUseCaseImpl(get()) }
    factory { MainViewModelFactory(get()) }
    viewModel { MainViewModel(get()) }

}

fun createHttpClient(context: Context): OkHttpClient {
    return OkHttpClient.Builder()
        .addInterceptor(NetworkConnectionInterceptor(context))
        .readTimeout(5, TimeUnit.MINUTES)
        .retryOnConnectionFailure(true)
        .addInterceptor { chain ->
            val original = chain.request()
            val request = original.newBuilder().method(original.method, original.body).build()
            chain.proceed(request)
        }
        .build()
}

inline fun <reified T> createWebService(
    okHttpClient: OkHttpClient,
    factory: CallAdapter.Factory,
    baseUrl: String
): T {
    val retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .addCallAdapterFactory(factory)
        .client(okHttpClient)
        .build()
    return retrofit.create(T::class.java)
}