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
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.multiplechoicesrs.logic.StudyHelper
import com.example.multiplechoicesrs.model.Deck
import com.example.multiplechoicesrs.model.viewmodel.SelectNumDialogViewModel

@Composable
fun SelectNumToStudyDialog(
    deck: Deck,
    categoryIdList: List<Int>,
    onDismissRequest: () -> Unit,
    navToStudy: (deck: Deck, categoryIdList: List<Int>, numToStudy: Int) -> Unit,
    dialogViewModel: SelectNumDialogViewModel = viewModel()
) {
    val studyHelper = StudyHelper(LocalContext.current)

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
                    Text("現在、復習すべき問題はありません。先取り学習をしますか？")
                }

                OutlinedTextField(
                    value = dialogViewModel.numToStudy,
                    label = { Text("出題数") },
                    onValueChange = { dialogViewModel.updateNumToStudy(it) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )

                Row {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                    ) {
                        TextButton(
                            onClick = {
                                onDismissRequest()
                                dialogViewModel.updateDataStore()
                            },
                            modifier = Modifier.padding(8.dp),
                        ) {
                            Text("キャンセル")
                        }
                        TextButton(
                            onClick = {
                                onDismissRequest()
                                dialogViewModel.updateDataStore()
                                navToStudy(deck, categoryIdList, dialogViewModel.numToStudy.toInt())
                            },
                            enabled = dialogViewModel.numToStudy.isNotEmpty(),
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