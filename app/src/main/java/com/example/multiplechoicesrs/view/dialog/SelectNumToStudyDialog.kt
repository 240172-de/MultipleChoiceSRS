package com.example.multiplechoicesrs.view.dialog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.core.text.isDigitsOnly
import com.example.multiplechoicesrs.logic.StudyHelper
import com.example.multiplechoicesrs.model.Deck

@Composable
fun SelectNumToStudyDialog(
    deck: Deck,
    categoryIdList: List<Int>,
    onDismissRequest: () -> Unit,
    navToStudy: (deck: Deck, categoryIdList: List<Int>, numToStudy: Int) -> Unit
) {
    val studyHelper = StudyHelper(LocalContext.current)
    var numToStudy by remember { mutableStateOf("5") }

    Dialog(onDismissRequest = onDismissRequest) {
        Card(
            modifier = Modifier.padding(16.dp),
            shape = RoundedCornerShape(16.dp),
        ) {
            Column(
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .padding(top = 10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(deck.name, fontWeight = FontWeight.Bold)

                if (!studyHelper.hasDueCards(deck.deckId, categoryIdList)) {
                    //TODO: Japanese
                    Text("No questions due. Study ahead?")
                }

                //TODO: Letzten Wert speichern?
                OutlinedTextField(
                    value = numToStudy,
                    label = { Text("To study") },
                    onValueChange = { if (it.isDigitsOnly()) numToStudy = it },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )

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
                                navToStudy(deck, categoryIdList, numToStudy.toInt())
                            },
                            enabled = numToStudy.isNotEmpty(),
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