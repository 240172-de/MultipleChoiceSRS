package com.example.multiplechoicesrs.view

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.example.multiplechoicesrs.model.Deck

@Composable
fun AnalysisDeck(deck: Deck) {
    Text("Deck ${deck.name}")
}