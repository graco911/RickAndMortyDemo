package com.gracodev.rickmortydemo.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.gracodev.rickmortydemo.data.model.CharacterResponse
import com.gracodev.rickmortydemo.data.repository.RickAndMortyRepository
import com.gracodev.rickmortydemo.presentation.states.UIStates
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel(private val rickAndMortyRepository: RickAndMortyRepository) : BaseViewModel() {

    private val _getCharactersResultLiveData = MutableLiveData<UIStates<CharacterResponse>>()
    val getCharacterResponseResultLiveData:
            LiveData<UIStates<CharacterResponse>> = _getCharactersResultLiveData


    fun getCharacters(page: Int) {
        _getCharactersResultLiveData.value = UIStates.Loading
        viewModelScope.launch(Dispatchers.IO) {
            val result = rickAndMortyRepository.getCharacters(page)
            _getCharactersResultLiveData.postValue(result.toUIStates())
        }
    }

}