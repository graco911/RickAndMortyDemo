package com.gracodev.rickmortydemo.presentation.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.gracodev.rickmortydemo.data.model.CharacterResponse
import com.gracodev.rickmortydemo.databinding.MainActivityLayoutBinding
import com.gracodev.rickmortydemo.presentation.adapters.CharacterAdapter
import com.gracodev.rickmortydemo.presentation.layouts.LoadingScreen
import com.gracodev.rickmortydemo.presentation.states.UIStates
import com.gracodev.rickmortydemo.presentation.viewmodels.MainViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {
    lateinit var dialog: LoadingScreen
    private lateinit var binding: MainActivityLayoutBinding

    private val mainViewModel: MainViewModel by viewModel()

    private val characterAdapter: CharacterAdapter by lazy {
        CharacterAdapter() {

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = MainActivityLayoutBinding.inflate(layoutInflater)
        dialog = LoadingScreen()
        setContentView(binding.root)

        setUpRecyclerView()
        setUpObservable()
    }

    private fun setUpObservable() {

        mainViewModel.getCharacterResponseResultLiveData.observe(this) { uiState ->
            validateDataResponse(uiState)
        }

        mainViewModel.getCharacters(1)
    }

    private fun validateDataResponse(uiState: UIStates<CharacterResponse>?) {
        when (uiState) {
            is UIStates.Error -> handleError(uiState)
            UIStates.Loading -> dialog.show(supportFragmentManager, "MainActivity")
            is UIStates.Success -> handleSuccess(uiState)
            else -> {}
        }
    }

    private fun handleSuccess(uiState: UIStates.Success<CharacterResponse>) {
        dialog.dismiss()
        characterAdapter.submitAll((uiState.value as CharacterResponse).results.toMutableList())
    }

    private fun handleError(uiState: UIStates.Error) {
        dialog.dismiss()
    }

    private fun setUpRecyclerView() {
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = characterAdapter
        }
    }
}