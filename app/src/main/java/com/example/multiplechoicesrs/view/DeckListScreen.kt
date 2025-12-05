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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.multiplechoicesrs.R
import com.example.multiplechoicesrs.db.CategoryTableHelper
import com.example.multiplechoicesrs.db.DeckTableHelper
import com.example.multiplechoicesrs.model.Deck
import com.example.multiplechoicesrs.view.custom.ProvideAppBarActions
import com.example.multiplechoicesrs.view.custom.ProvideAppBarTitle

@Composable
fun DeckListScreen(
    navToImport: () -> Unit,
    navToDelete: () -> Unit,
    navToCategoryList: (deck: Deck) -> Unit,
    navToStudy: (deck: Deck, categoryIdList: List<Int>, numToStudy: Int) -> Unit,
    navToAnalysis: (deck: Deck) -> Unit,
    modifier: Modifier = Modifier
) {
    val decks = loadDecks(LocalContext.current)

    ProvideAppBarTitle {
        Text(stringResource(R.string.study))
    }

    ProvideAppBarActions {
        IconButton(onClick = {
            navToImport()
        }) {
            Icon(
                painter = painterResource(R.drawable.baseline_download_24),
                contentDescription = stringResource(R.string.import_txt)
            )
        }

        IconButton(
            onClick = {
                navToDelete()
            },
            modifier = Modifier.padding(start = 10.dp)
        ) {
            Icon(
                painter = painterResource(R.drawable.baseline_delete_24),
                contentDescription = stringResource(R.string.delete)
            )
        }
    }

    Column(modifier = modifier) {
        if (decks.isEmpty()) {
            NoDeckDataScreen(navToImport)
        } else {
            DeckList(decks, navToCategoryList, navToStudy, navToAnalysis)
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
            stringResource(R.string.no_data),
            modifier = Modifier.padding(bottom = 20.dp)
        )

        Button(onClick = {
            navToImport()
        }) {
            Text(stringResource(R.string.move_to_import))
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