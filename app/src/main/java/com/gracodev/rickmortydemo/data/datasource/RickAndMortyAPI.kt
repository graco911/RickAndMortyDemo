package com.gracodev.rickmortydemo.data.datasource

import com.gracodev.rickmortydemo.data.model.CharacterResponse
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface RickAndMortyAPI {
    @GET("character/")
    fun getCharacters(@Query("page") page: Int): Deferred<Response<CharacterResponse>>
}