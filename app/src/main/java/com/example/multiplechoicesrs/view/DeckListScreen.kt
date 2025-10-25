package com.example.multiplechoicesrs.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import com.example.multiplechoicesrs.R
import com.example.multiplechoicesrs.db.DeckTableHelper

@Composable
fun DeckListScreen(
    navToImport: () -> Unit,
    modifier: Modifier = Modifier
) {
    val deckTableHelper = DeckTableHelper(LocalContext.current)
    val decks = deckTableHelper.getDecks()

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
            Text("Decks")
        }
    }
}

@Composable
fun NoDeckDataScreen(
    navToImport: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("データがない")
        Button(onClick = {
            navToImport()
        }) {
            Text("インポート")
        }
    }
}