package com.example.multiplechoicesrs.view

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.multiplechoicesrs.R
import com.example.multiplechoicesrs.db.CategoryTableHelper
import com.example.multiplechoicesrs.db.DeckTableHelper
import com.example.multiplechoicesrs.db.QuestionTableHelper
import com.example.multiplechoicesrs.model.ConnectionData
import com.example.multiplechoicesrs.model.DecksJson
import com.example.multiplechoicesrs.model.SnackbarColor
import com.example.multiplechoicesrs.model.viewmodel.ImportDecksUiState
import com.example.multiplechoicesrs.model.viewmodel.ImportDecksViewModel
import com.example.multiplechoicesrs.model.viewmodel.ImportSettingsDialogViewModel
import com.example.multiplechoicesrs.rest.MultipleChoiceApi
import com.example.multiplechoicesrs.view.custom.CustomCard
import com.example.multiplechoicesrs.view.custom.ProvideAppBarActions
import com.example.multiplechoicesrs.view.custom.ProvideAppBarNavigationIcon
import com.example.multiplechoicesrs.view.custom.ProvideAppBarTitle
import com.example.multiplechoicesrs.view.dialog.ImportSettingsDialog
import com.example.multiplechoicesrs.view.dialog.LoadingSpinnerDialog
import com.example.multiplechoicesrs.view.dialog.UpToDateDialog
import kotlinx.coroutines.launch

@Composable
fun ImportDataScreen(
    navBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showSettingsDialog by remember { mutableStateOf(false) }
    val importDecksViewModel: ImportDecksViewModel = viewModel()
    val dialogViewModel: ImportSettingsDialogViewModel = viewModel()

    ProvideAppBarTitle {
        Text(stringResource(R.string.import_txt))
    }

    ProvideAppBarNavigationIcon {
        IconButton(
            onClick = {
                navBack()
            }
        ) {
            Icon(
                painter = painterResource(R.drawable.outline_arrow_back_24),
                contentDescription = stringResource(R.string.back)
            )
        }
    }

    ProvideAppBarActions {
        IconButton(onClick = {
            showSettingsDialog = true
        }) {
            Icon(
                painter = painterResource(R.drawable.baseline_settings_24),
                contentDescription = stringResource(R.string.settings_import)
            )
        }
    }

    if (showSettingsDialog) {
        ImportSettingsDialog(
            onSubmit = {
                showSettingsDialog = false
                dialogViewModel.updateDataStore()
                importDecksViewModel.loadDecks()
            },
            onDismissRequest = {
                showSettingsDialog = false
                dialogViewModel.resetData()
            },
            dialogViewModel = dialogViewModel
        )
    }

    Column(modifier = modifier) {
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
            decks = importDecksUiState.decks,
            connectionData = importDecksUiState.connectionData,
            modifier = modifier.fillMaxWidth()
        )
        is ImportDecksUiState.Error -> ErrorScreen( modifier = modifier.fillMaxSize())
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "LocalContextGetResourceValueCall")
@Composable
fun ImportDecksListScreen(
    decks: DecksJson,
    connectionData: ConnectionData,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val deckTableHelper = DeckTableHelper(context)
    val categoryTableHelper = CategoryTableHelper(context)
    val questionTableHelper = QuestionTableHelper(context)
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
            modifier = modifier
                .fillMaxSize()
                .padding(10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(decks.data) { deck ->
                CustomCard {
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
                                    val result = MultipleChoiceApi(connectionData).retrofitService.importDeck(deck.deckId)

                                    deckTableHelper.saveDeck(result)
                                    result.categories?.forEach {
                                        categoryTableHelper.saveCategory(it)
                                    }
                                    result.questionsJson?.forEach {
                                        questionTableHelper.saveQuestion(it)
                                    }

                                    showLoadingSpinner = false
                                    importStatus = SnackbarColor.SUCCESS
                                    snackbarHostState.showSnackbar(context.getString(R.string.import_success))
                                } catch (e: Exception) {
                                    Log.d("ERROR", e.toString())
                                    showLoadingSpinner = false
                                    importStatus = SnackbarColor.FAILURE
                                    snackbarHostState.showSnackbar(context.getString(R.string.import_error))
                                }
                            }
                        }) {
                            Icon(
                                painter = painterResource(R.drawable.baseline_download_24),
                                contentDescription = stringResource(R.string.import_txt)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ErrorScreen(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_connection_error), contentDescription = stringResource(R.string.connection_error)
        )
        Text(
            stringResource(R.string.connection_error),
            fontWeight = FontWeight.SemiBold,
            color = Color.Gray,
            modifier = Modifier.padding(16.dp)
        )
    }
}