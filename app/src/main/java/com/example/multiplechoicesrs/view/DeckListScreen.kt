package com.example.multiplechoicesrs.view

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.multiplechoicesrs.R
import com.example.multiplechoicesrs.db.CategoryTableHelper
import com.example.multiplechoicesrs.db.DeckTableHelper
import com.example.multiplechoicesrs.model.Deck

@Composable
fun DeckListScreen(
    navToImport: () -> Unit,
    navToCategoryList: (deck: Deck) -> Unit,
    navToStudy: (deck: Deck, categoryIdList: List<Int>, numToStudy: Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val decks = loadDecks(LocalContext.current)

    ProvideAppBarTitle {
        Text("学習")
    }

    ProvideAppBarActions {
        IconButton(onClick = {
            navToImport()
        }) {
            Icon(
                painter = painterResource(R.drawable.baseline_download_24),
                contentDescription = "インポート"
            )
        }
    }

    Column(modifier = modifier) {
        if (decks.isEmpty()) {
            NoDeckDataScreen(navToImport)
        } else {
            DeckList(decks, navToCategoryList, navToStudy)
        }
    }
}

@Composable
fun NoDeckDataScreen(
    navToImport: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            "データがない",
            modifier = Modifier.padding(bottom = 20.dp)
        )

        Button(onClick = {
            navToImport()
        }) {
            Text("インポート画面へ")
        }
    }
}

private fun loadDecks(context: Context): List<Deck> {
    val deckTableHelper = DeckTableHelper(context)
    val categoryTableHelper = CategoryTableHelper(context)

    val decks = deckTableHelper.getDecks()

    for (deck in decks) {
        deck.categories = categoryTableHelper.getCategories(deck.deckId)
    }

    return decks
}