package com.example.multiplechoicesrs.view

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun ImportDataScreen(modifier: Modifier = Modifier) {
    ProvideAppBarTitle {
        Text("インポート")
    }

    Column(modifier = modifier) {
        Text("Load data")
    }
}