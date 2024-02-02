package com.gracodev.rickmortydemo.domain.datasource

import com.gracodev.rickmortydemo.data.datasource.RickAndMortyAPI
import com.gracodev.rickmortydemo.data.model.CharacterResponse
import com.gracodev.rickmortydemo.data.repository.RickAndMortyRepository
import com.gracodev.rickmortydemo.domain.usecases.UseCaseResult
import kotlinx.coroutines.Deferred
import retrofit2.Response

class GetCharacterUseCaseImpl(private val rickAndMortyAPI: RickAndMortyAPI) :
    RickAndMortyRepository {

    private suspend fun <T : Any> executeRequest(apiCall: suspend () -> Deferred<Response<T>>): UseCaseResult<T> {
        return try {
            val response = apiCall.invoke().await()
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    UseCaseResult.Success(body)
                } else {
                    UseCaseResult.Error(Exception("Response body is null"))
                }
            } else {
                UseCaseResult.Error(Exception("Error ${response.code()}: ${response.message()}"))
            }
        } catch (ex: Exception) {
            UseCaseResult.Error(ex)
        }
    }

    override suspend fun getCharacters(page: Int): UseCaseResult<CharacterResponse> {
        return executeRequest { rickAndMortyAPI.getCharacters(page) }
    }
}