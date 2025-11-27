package com.example.multiplechoicesrs.view

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.example.multiplechoicesrs.R
import com.example.multiplechoicesrs.model.Deck
import com.example.multiplechoicesrs.view.custom.ProvideAppBarNavigationIcon
import com.example.multiplechoicesrs.view.custom.ProvideAppBarTitle

@Composable
fun AnalysisScreen(
    deck: Deck,
    navBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    ProvideAppBarTitle {
        Text("分析")
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

    Column(modifier) {
        AnalysisTabManager(deck)
    }
}