package com.example.multiplechoicesrs.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

class CheckableItem(
    val id: Int,
    val label: String,
    val initialIsChecked: Boolean = false,
) {
    var isChecked: Boolean by mutableStateOf(initialIsChecked)
}

/**
 * Add Modifier.weight(1f) if there are items below the list
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckableList(data: List<CheckableItem>, trailingContent: (@Composable () -> Unit)? = null, modifier: Modifier = Modifier) {
    val items = remember { data.toMutableStateList() }

    LazyColumn(modifier) {
        items(items) { item ->
            Row(
                modifier = Modifier
                    .clickable(onClick = {
                        item.isChecked = !item.isChecked
                    })
                    .padding(vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = item.isChecked,
                    onCheckedChange = { newCheckedState ->
                        item.isChecked = newCheckedState
                    }
                )

                Text(text = item.label, modifier = Modifier.padding(start = 8.dp))

                Spacer(modifier = Modifier.weight(1f))

                if (trailingContent != null) {
                    trailingContent()
                }
            }
        }
    }
}

fun getCheckedIds(data: List<CheckableItem>): List<Int> {
    return data.filter { checkableItem ->
        checkableItem.isChecked
    }.map { checkableItem ->
        checkableItem.id
    }
}