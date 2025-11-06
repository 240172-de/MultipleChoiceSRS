package com.example.multiplechoicesrs.view

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.multiplechoicesrs.R
import com.example.multiplechoicesrs.db.CategoryTableHelper
import com.example.multiplechoicesrs.db.DeckTableHelper
import com.example.multiplechoicesrs.db.QuestionTableHelper
import com.example.multiplechoicesrs.model.DecksJson
import com.example.multiplechoicesrs.model.ImportDecksUiState
import com.example.multiplechoicesrs.model.ImportDecksViewModel

@Composable
fun ImportDataScreen(
    navBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val deckTableHelper = DeckTableHelper(LocalContext.current)
    val categoryTableHelper = CategoryTableHelper(LocalContext.current)
    val questionTableHelper = QuestionTableHelper(LocalContext.current)

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

    val importDecksViewModel: ImportDecksViewModel = viewModel()
    ImportDecksScreen(
        importDecksUiState = importDecksViewModel.importDecksUiState
    )
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

@Composable
fun ImportDecksListScreen(decks: DecksJson, modifier: Modifier = Modifier) {
    Column(modifier) {
        Text("$decks")
        Log.d("TEST", "$decks")
    }
}

@Composable
fun LoadingScreen(modifier: Modifier = Modifier) {
    Box(
        contentAlignment= Alignment.Center,
        modifier = modifier
            .size(100.dp)
            .background(White, shape = RoundedCornerShape(8.dp))
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