package com.gracodev.rickmortydemo.data.factories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.gracodev.rickmortydemo.data.repository.RickAndMortyRepository
import com.gracodev.rickmortydemo.presentation.viewmodels.MainViewModel

class MainViewModelFactory(private val rickAndMortyRepository: RickAndMortyRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(rickAndMortyRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}