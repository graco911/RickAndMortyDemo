package com.gracodev.rickmortydemo.data.repository

import com.gracodev.rickmortydemo.data.model.CharacterResponse
import com.gracodev.rickmortydemo.domain.usecases.UseCaseResult

interface RickAndMortyRepository {
    suspend fun getCharacters(page: Int): UseCaseResult<CharacterResponse>
}