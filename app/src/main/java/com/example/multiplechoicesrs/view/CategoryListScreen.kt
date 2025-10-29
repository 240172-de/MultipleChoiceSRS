package com.example.multiplechoicesrs.view

import android.content.Context
import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.multiplechoicesrs.R
import com.example.multiplechoicesrs.db.CategoryTableHelper

@Composable
fun CategoryListScreen(
    deckId: Int,
    navBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val list = makeList(LocalContext.current, deckId)

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

    Column(
        modifier = modifier
            .padding(horizontal = 10.dp)
            .padding(top = 5.dp, bottom = 35.dp)
    ) {
        CheckableList(list, modifier = Modifier.weight(1f))

        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = {
            Log.d("TEST", "${getCheckedIds(list)}")
        }) {
            Text("学習する")
        }
    }
}

private fun makeList(context: Context, deckId: Int): List<CheckableItem> {
    val categoryTableHelper = CategoryTableHelper(context)
    val categories = categoryTableHelper.getCategories(deckId)

    return categories.map {
        CheckableItem(
            it.categoryId,
            it.name,
            false
        )
    }
}