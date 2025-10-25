package com.example.multiplechoicesrs.view

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.example.multiplechoicesrs.R

@Composable
fun ImportDataScreen(
    navBack: () -> Unit,
    modifier: Modifier = Modifier
) {
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
        Text("Load data")
    }
}