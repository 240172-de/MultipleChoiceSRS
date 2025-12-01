package com.example.multiplechoicesrs.view.dialog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

@Composable
fun SortDialog(
    initSortBy: SortBy,
    onSubmit: (sortBy: SortBy) -> Unit,
    onDismissRequest: () -> Unit
) {
    val radioOptions = SortBy.entries.toList()
    val (selectedOption, onOptionSelected) = remember { mutableStateOf(initSortBy.label) }

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
                Column(
                    Modifier
                        .selectableGroup()
                ) {
                    radioOptions.forEachIndexed { index, sortBy ->
                        val text = sortBy.label

                        Row(
                            Modifier
                                .fillMaxWidth()
                                .selectable(
                                    selected = (text == selectedOption),
                                    onClick = { onOptionSelected(text) },
                                    role = Role.RadioButton
                                )
                                .padding(vertical = 5.dp)
                                .padding(horizontal = 16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = (text == selectedOption),
                                onClick = null
                            )
                            Text(
                                text = text,
                                style = MaterialTheme.typography.bodyLarge,
                                modifier = Modifier.padding(start = 16.dp)
                            )
                        }
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    TextButton(
                        onClick = { onDismissRequest() },
                        modifier = Modifier.padding(8.dp),
                    ) {
                        Text("キャンセル")
                    }

                    TextButton(
                        onClick = {
                            onSubmit(radioOptions.first { it.label == selectedOption })
                        },
                        modifier = Modifier.padding(8.dp),
                    ) {
                        Text("適用")
                    }
                }
            }
        }
    }
}

enum class SortBy(
    val label: String,
    val ascending: Boolean
) {
    ALPHABETICALLY_ASC(
        label = "出典（A-Z)",
        ascending = true
    ),
    ALPHABETICALLY_DESC(
        label = "出典（Z-A)",
        ascending = false
    ),
    NUM_WRONG_ASC(
        label = "誤答数（昇順）",
        ascending = true
    ),
    NUM_WRONG_DESC(
        label = "誤答数（降順）",
        ascending = false
    )
}