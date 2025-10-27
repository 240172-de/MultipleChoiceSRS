package com.example.multiplechoicesrs.view

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import com.example.multiplechoicesrs.R
import com.example.multiplechoicesrs.db.DeckTableHelper
import com.example.multiplechoicesrs.model.Deck

@Composable
fun ImportDataScreen(
    navBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val deckTableHelper = DeckTableHelper(LocalContext.current)

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
        Text("Generate Testdata")

        Button(onClick = {
            deckTableHelper.saveDeck(Deck(
                1,
                "応用情報技術者試験",
                1
            ))

            deckTableHelper.saveDeck(Deck(
                2,
                "日本語能力試験",
                1
            ))
        }) {
            Text("Import")
        }
    }
}