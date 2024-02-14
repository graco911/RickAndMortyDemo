package com.gracodev.rickmortydemo.presentation.viewmodels

import androidx.lifecycle.viewModelScope
import com.gracodev.rickmortydemo.data.model.CharacterData
import com.gracodev.rickmortydemo.data.model.CharacterResponse
import com.gracodev.rickmortydemo.data.repository.RickAndMortyRepository
import com.gracodev.rickmortydemo.domain.usecases.UseCaseResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MainViewModel(private val rickAndMortyRepository: RickAndMortyRepository) : BaseViewModel() {

    private val _characters = MutableStateFlow(emptyList<CharacterData>())
    val characters: StateFlow<List<CharacterData>> = _characters


    fun getCharacters(page: Int) {
        viewModelScope.launch(Dispatchers.IO) {

            val characterResponse = when (
                val result =
                    rickAndMortyRepository.getCharacters(page)) {
                is UseCaseResult.Success -> result.data as CharacterResponse
                else -> {
                    return@launch
                }
            }

            _characters.value = characterResponse.results
        }
    }

}