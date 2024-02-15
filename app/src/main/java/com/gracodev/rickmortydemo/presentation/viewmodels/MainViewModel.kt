package com.gracodev.rickmortydemo.presentation.viewmodels

import androidx.lifecycle.viewModelScope
import com.gracodev.rickmortydemo.data.model.CharacterResponse
import com.gracodev.rickmortydemo.data.repository.RickAndMortyRepository
import com.gracodev.rickmortydemo.presentation.states.UIStates
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MainViewModel(private val rickAndMortyRepository: RickAndMortyRepository) : BaseViewModel() {

    private val _characters = MutableStateFlow(UIStates<CharacterResponse>())
    val characters: StateFlow<UIStates<CharacterResponse>> = _characters


    fun getCharacters(page: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            _characters.value = UIStates.Loading
            val result = rickAndMortyRepository.getCharacters(page)
            _characters.value = result.toUIStates()
        }
    }
}