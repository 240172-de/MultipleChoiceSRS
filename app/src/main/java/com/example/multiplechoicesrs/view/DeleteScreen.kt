package com.example.multiplechoicesrs.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.multiplechoicesrs.R
import com.example.multiplechoicesrs.db.DeckTableHelper
import com.example.multiplechoicesrs.model.Deck
import com.example.multiplechoicesrs.model.viewmodel.DeleteDeckUiState
import com.example.multiplechoicesrs.model.viewmodel.DeleteDeckViewModel
import com.example.multiplechoicesrs.ui.theme.RedIncorrectAnswer
import com.example.multiplechoicesrs.view.custom.ProvideAppBarNavigationIcon
import com.example.multiplechoicesrs.view.custom.ProvideAppBarTitle
import com.example.multiplechoicesrs.view.dialog.DeleteDeckDialog

@Composable
fun DeleteScreen(
    navBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    ProvideAppBarTitle {
        Text("削除")
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

    val context = LocalContext.current
    val deckTableHelper = DeckTableHelper(context)
    val deleteDeckViewModel: DeleteDeckViewModel = viewModel {
        DeleteDeckViewModel(context)
    }

    var deckToDelete: Deck? by remember { mutableStateOf(null) }

    if (deckToDelete != null) {
        DeleteDeckDialog(
            deck = deckToDelete!!,
            onClickDelete = { deck ->
                deckTableHelper.deleteDeck(deck.deckId)
                deleteDeckViewModel.getDecks()
            },
            onDismissRequest = {
                deckToDelete = null
            }
        )
    }

    Column(modifier) {
        DeleteDeckRouting(deleteDeckViewModel.deleteDeckUiState) { deck ->
            deckToDelete = deck
        }
    }

}

@Composable
fun DeleteDeckRouting(
    deleteDeckUiState: DeleteDeckUiState,
    onClickDelete: (Deck) -> Unit
) {
    when(deleteDeckUiState) {
        is DeleteDeckUiState.Loading -> LoadingScreen()
        is DeleteDeckUiState.NoData -> NoDataScreen()
        is DeleteDeckUiState.Success -> DeleteDeckList(deleteDeckUiState.decks, onClickDelete)
    }
}

@Composable
fun DeleteDeckList(
    deckList: List<Deck>,
    onClickDelete: (Deck) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(10.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        items(deckList) { deck ->
            DeleteDeckItem(deck, onClickDelete)
        }
    }
}

@Composable
fun DeleteDeckItem(
    deck: Deck,
    onClickDelete: (deck: Deck) -> Unit
) {
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

            IconButton(onClick = {
                onClickDelete(deck)
            }) {
                Icon(
                    painter = painterResource(R.drawable.baseline_remove_circle_24),
                    contentDescription = "削除",
                    tint = RedIncorrectAnswer
                )
            }
        }
    }
}