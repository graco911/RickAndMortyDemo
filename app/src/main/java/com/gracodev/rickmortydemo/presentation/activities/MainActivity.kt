package com.gracodev.rickmortydemo.presentation.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.gracodev.rickmortydemo.data.model.CharacterData
import com.gracodev.rickmortydemo.presentation.states.UIStates
import com.gracodev.rickmortydemo.presentation.viewmodels.MainViewModel
import com.gracodev.rickmortydemo.ui.theme.RickMortyDemoTheme
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {

    private val mainViewModel: MainViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            RickMortyDemoTheme {
                Surface(
                    color = MaterialTheme.colorScheme.background
                ) {
                    ScaffoldApp()
                }
            }
        }
    }

    @Composable
    fun ScaffoldApp() {
        val snackbarHostState = remember {
            SnackbarHostState()
        }
        Scaffold(
            snackbarHost = {
                SnackbarHost(hostState = snackbarHostState)
            },
            topBar = { TopAppBar() },
            content = { innerPadding ->
                CharactersScreen(
                    viewModel = mainViewModel,
                    snackbarHostState = snackbarHostState,
                    contentPadding = innerPadding
                )
            }
        )
    }

    @Composable
    fun CharactersScreen(
        viewModel: MainViewModel,
        snackbarHostState: SnackbarHostState,
        contentPadding: PaddingValues
    ) {
        val scope = rememberCoroutineScope()
        val characters by viewModel.characters.collectAsState()
        val loading by remember { mutableStateOf(false) }

        LaunchedEffect(Unit) {
            scope.launch {
                viewModel.getCharacters(1)
            }
        }

        LaunchedEffect(characters) {
            when (val uiState = characters) {
                is UIStates.Error -> {
                    snackbarHostState.showSnackbar(
                        "Error: ${uiState.message}"
                    )
                }

                is UIStates.Success -> {
                    snackbarHostState.showSnackbar("Peticion correcta")
                }
            }
        }

        when (val uiState = characters) {
            is UIStates.Loading -> {
                LoadingIndicator(!loading)
            }

            is UIStates.Success -> {
                uiState.value?.let { CharactersList(it.results) }
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    private fun TopAppBar() {
        TopAppBar(title = { Text("Rick & Morty API") })
    }

    @Composable
    fun LoadingIndicator(isVisible: Boolean) {

        AnimatedVisibility(visible = isVisible) {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CircularProgressIndicator(
                    color = MaterialTheme.colorScheme.secondary,
                    trackColor = MaterialTheme.colorScheme.surfaceVariant
                )
            }

        }
    }

    @Composable
    private fun CharactersList(characters: List<CharacterData>) {
        LazyColumn {
            items(characters) { character ->
                var expanded by remember { mutableStateOf(false) }

                Column(Modifier.clickable { expanded = !expanded }) {

                    Card(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth()
                    ) {
                        AsyncImage(
                            modifier = Modifier.fillMaxWidth(),
                            model = character.image,
                            contentDescription = "Image",
                            contentScale = ContentScale.Crop
                        )
                        Column(
                            Modifier
                                .padding(40.dp)
                                .fillMaxWidth()
                        ) {
                            Text(
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.align(Alignment.CenterHorizontally),
                                text = character.name,
                                style = MaterialTheme.typography.titleLarge
                            )
                            Divider(
                                color = Color.Black,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .width(1.dp)
                                    .padding(8.dp)
                            )
                            Text(
                                fontWeight = FontWeight.Medium,
                                modifier = Modifier.align(Alignment.CenterHorizontally),
                                text = character.species,
                                style = MaterialTheme.typography.titleMedium
                            )
                        }
                        AnimatedVisibility(
                            visible = expanded
                        ) {
                            Column(
                                Modifier.padding(40.dp)
                            ) {
                                Text(
                                    fontWeight = FontWeight.Medium,
                                    text = character.gender,
                                    style = MaterialTheme.typography.titleMedium
                                )
                                Text(
                                    fontWeight = FontWeight.Medium,
                                    text = character.status,
                                    style = MaterialTheme.typography.titleMedium
                                )
                                Text(
                                    fontWeight = FontWeight.Medium,
                                    text = character.location.name,
                                    style = MaterialTheme.typography.titleMedium
                                )
                                Text(
                                    fontWeight = FontWeight.Medium,
                                    text = character.origin.name,
                                    style = MaterialTheme.typography.titleMedium
                                )
                            }
                        }
                        Button(modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                            onClick = { expanded = !expanded }) {
                            Text(text = if (!expanded) "Ver mas" else "Ocultar detalles")
                        }
                    }
                }
            }
        }
    }
}