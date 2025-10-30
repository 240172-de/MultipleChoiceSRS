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
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.multiplechoicesrs.model.Deck

@Composable
fun SelectNumToStudyDialog(
    deck: Deck,
    categoryIdList: List<Int>,
    onDismissRequest: () -> Unit,
    navToStudy: (deck: Deck, categoryIdList: List<Int>) -> Unit
) {
    Dialog(onDismissRequest = onDismissRequest) {
        Card(
            modifier = Modifier.padding(16.dp),
            shape = RoundedCornerShape(16.dp),
        ) {
            Column {
                Text("Test ${deck.name}    $categoryIdList")

                Row {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                    ) {
                        TextButton(
                            onClick = { onDismissRequest() },
                            modifier = Modifier.padding(8.dp),
                        ) {
                            Text("キャンセル")
                        }
                        TextButton(
                            onClick = {
                                onDismissRequest()
                                navToStudy(deck, categoryIdList)
                            },
                            modifier = Modifier.padding(8.dp),
                        ) {
                            Text("勉強")
                        }
                    }
                }
            }
        }
    }
}