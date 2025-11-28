package com.example.multiplechoicesrs.view

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.multiplechoicesrs.R
import com.example.multiplechoicesrs.model.Category
import com.example.multiplechoicesrs.model.Deck
import com.example.multiplechoicesrs.view.custom.CheckableItem
import com.example.multiplechoicesrs.view.custom.CheckableList
import com.example.multiplechoicesrs.view.custom.ProvideAppBarNavigationIcon
import com.example.multiplechoicesrs.view.custom.ProvideAppBarTitle
import com.example.multiplechoicesrs.view.custom.getCheckedIds
import com.example.multiplechoicesrs.view.dialog.SelectNumToStudyDialog

@Composable
fun CategoryListScreen(
    deck: Deck,
    navToStudy: (deck: Deck, categoryIdList: List<Int>, numToStudy: Int) -> Unit,
    navBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val list = remember { makeList(deck.categories!!) }

    ProvideAppBarTitle {
        Text("分野選択")
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

    var showSelectNumDialog by remember { mutableStateOf(false) }
    var isButtonEnabled by remember { mutableStateOf(false) }

    if (showSelectNumDialog) {
        SelectNumToStudyDialog(
            deck = deck,
            categoryIdList = getCheckedIds(list),
            onDismissRequest = {
                showSelectNumDialog = false
            },
            navToStudy = navToStudy)
    }

    Column(
        modifier = modifier
            .padding(horizontal = 10.dp)
            .padding(top = 5.dp, bottom = 35.dp)
    ) {
        CheckableList(
            data = list,
            onCheckedChange = {
                isButtonEnabled = getCheckedIds(list).isNotEmpty()
            },
            modifier = Modifier.weight(1f)
        )

        Button(
            modifier = Modifier.fillMaxWidth(),
            enabled = isButtonEnabled,
            onClick = {
                showSelectNumDialog = true
        }) {
            Text("学習する")
        }
    }
}

private fun makeList(categories: List<Category>): List<CheckableItem> {
    return categories.map {
        CheckableItem(
            it.categoryId,
            it.name,
            false
        )
    }
}