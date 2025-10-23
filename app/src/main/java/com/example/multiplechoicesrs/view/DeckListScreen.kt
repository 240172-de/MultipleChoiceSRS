package com.example.multiplechoicesrs.view

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun DeckListScreen(modifier: Modifier = Modifier) {
    ProvideAppBarTitle {
        Text("学習")
    }

    Column(modifier = modifier) {
        Text("Decks")
    }
}