package com.example.multiplechoicesrs.view.dialog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.multiplechoicesrs.R
import com.example.multiplechoicesrs.model.Category
import com.example.multiplechoicesrs.view.custom.CheckableItem
import com.example.multiplechoicesrs.view.custom.CheckableList
import com.example.multiplechoicesrs.view.custom.getCheckedIds

@Composable
fun FilterCategoriesDialog(
    allCategories: List<Category>,
    preselectIds: List<Int>,
    onSubmit: (categoryIds: List<Int>) -> Unit,
    onDismissRequest: () -> Unit
) {
    val list = remember { makeList(allCategories, preselectIds) }

    Dialog(onDismissRequest) {
        Card(
            modifier = Modifier.padding(16.dp),
            shape = RoundedCornerShape(16.dp),
        ) {
            Column(
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .padding(top = 20.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                horizontalAlignment = Alignment.Start
            ) {
                CheckableList(
                    data = list,
                    onCheckedChange = { },
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    TextButton(
                        onClick = { onDismissRequest() },
                        modifier = Modifier.padding(8.dp),
                    ) {
                        Text(stringResource(R.string.cancel))
                    }

                    TextButton(
                        onClick = { onSubmit(getCheckedIds(list)) },
                        modifier = Modifier.padding(8.dp),
                    ) {
                        Text(stringResource(R.string.apply_changes))
                    }
                }
            }
        }
    }
}

private fun makeList(categories: List<Category>, preselectIds: List<Int>): List<CheckableItem> {
    return categories.map {
        CheckableItem(
            id = it.categoryId,
            label = it.name,
            initialIsChecked = preselectIds.contains(it.categoryId)
        )
    }
}