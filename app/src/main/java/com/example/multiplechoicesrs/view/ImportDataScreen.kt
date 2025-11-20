package com.example.multiplechoicesrs.view

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.multiplechoicesrs.R
import com.example.multiplechoicesrs.db.CategoryTableHelper
import com.example.multiplechoicesrs.db.DeckTableHelper
import com.example.multiplechoicesrs.db.QuestionTableHelper
import com.example.multiplechoicesrs.model.DecksJson
import com.example.multiplechoicesrs.model.SnackbarColor
import com.example.multiplechoicesrs.rest.ImportDecksUiState
import com.example.multiplechoicesrs.rest.ImportDecksViewModel
import com.example.multiplechoicesrs.rest.MultipleChoiceApi
import com.example.multiplechoicesrs.ui.theme.GreenCorrectAnswer
import com.example.multiplechoicesrs.ui.theme.RedIncorrectAnswer
import com.example.multiplechoicesrs.view.custom.ProvideAppBarNavigationIcon
import com.example.multiplechoicesrs.view.custom.ProvideAppBarTitle
import com.example.multiplechoicesrs.view.dialog.LoadingSpinnerDialog
import com.example.multiplechoicesrs.view.dialog.UpToDateDialog
import kotlinx.coroutines.launch

@Composable
fun ImportDataScreen(
    navBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    ProvideAppBarTitle {
        Text("インポート")
    }

    ProvideAppBarNavigationIcon {
        IconButton(
            onClick = {
                navBack()
            }
        ) {
            Icon(
                painter = painterResource(R.drawable.outline_arrow_back_24),
                contentDescription = "戻る"
            )
        }
    }

    Column(modifier = modifier) {
        val importDecksViewModel: ImportDecksViewModel = viewModel()
        ImportDecksScreen(
            importDecksUiState = importDecksViewModel.importDecksUiState
        )
    }
}

@Composable
fun ImportDecksScreen(
    importDecksUiState: ImportDecksUiState,
    modifier: Modifier = Modifier,
) {
    when (importDecksUiState) {
        is ImportDecksUiState.Loading -> LoadingScreen(modifier = modifier.fillMaxSize())
        is ImportDecksUiState.Success -> ImportDecksListScreen(
            importDecksUiState.decks, modifier = modifier.fillMaxWidth()
        )
        is ImportDecksUiState.Error -> ErrorScreen( modifier = modifier.fillMaxSize())
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ImportDecksListScreen(decks: DecksJson, modifier: Modifier = Modifier) {
    val deckTableHelper = DeckTableHelper(LocalContext.current)
    val categoryTableHelper = CategoryTableHelper(LocalContext.current)
    val questionTableHelper = QuestionTableHelper(LocalContext.current)
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    var importStatus by remember { mutableStateOf(SnackbarColor.INFO) }
    var showUpToDateDialog by remember { mutableStateOf(false) }
    var showLoadingSpinner by remember { mutableStateOf(false) }

    if (showUpToDateDialog) {
        UpToDateDialog {
            showUpToDateDialog = false
        }
    }

    if (showLoadingSpinner) {
        LoadingSpinnerDialog()
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(snackbarHostState) { data ->
                Snackbar(
                    containerColor = importStatus.backgroundColor,
                    contentColor = importStatus.foregroundColor,
                    snackbarData = data
                )
            }
        }
    ) {
        LazyColumn(
            modifier = modifier.padding(10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(decks.data) { deck ->
                Card(
                    elevation = CardDefaults.cardElevation(defaultElevation = 5.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.background,
                    ),
                ) {
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth().padding(10.dp)
                    ) {
                        Text(
                            deck.name,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onBackground
                        )

                        Button(onClick = {
                            if(deckTableHelper.getDecks().any { savedDeck ->
                                savedDeck.deckId == deck.deckId &&
                                        savedDeck.versionId == deck.versionId
                            }) {
                                showUpToDateDialog = true
                                return@Button
                            }

                            coroutineScope.launch {
                                showLoadingSpinner = true

                                try {
                                    val result = MultipleChoiceApi.retrofitService.importDeck(deck.deckId)

                                    deckTableHelper.saveDeck(result)
                                    result.categories?.forEach {
                                        categoryTableHelper.saveCategory(it)
                                    }
                                    result.questionsJson?.forEach {
                                        questionTableHelper.saveQuestion(it)
                                    }

                                    showLoadingSpinner = false
                                    importStatus = SnackbarColor.SUCCESS
                                    snackbarHostState.showSnackbar("デッキがインポートされました")
                                } catch (e: Exception) {
                                    Log.d("ERROR", e.toString())
                                    showLoadingSpinner = false
                                    importStatus = SnackbarColor.FAILURE
                                    snackbarHostState.showSnackbar("障害が発生しました")
                                }
                            }
                        }) {
                            Icon(
                                painter = painterResource(R.drawable.baseline_download_24),
                                contentDescription = "インポート"
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun LoadingScreen(modifier: Modifier = Modifier) {
    Box(
        contentAlignment= Alignment.Center,
        modifier = modifier
            .size(100.dp)
            .background(MaterialTheme.colorScheme.background, shape = RoundedCornerShape(8.dp))
    ) {
        CircularProgressIndicator()
    }
}

/**
 * The home screen displaying error message with re-attempt button.
 */
@Composable
fun ErrorScreen(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_connection_error), contentDescription = ""
        )
        Text("Load failed", modifier = Modifier.padding(16.dp))
    }
}