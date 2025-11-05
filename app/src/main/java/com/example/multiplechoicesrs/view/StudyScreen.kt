package com.example.multiplechoicesrs.view

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.zIndex
import com.example.multiplechoicesrs.R
import com.example.multiplechoicesrs.ext.modifyIf
import com.example.multiplechoicesrs.logic.StudyHelper
import com.example.multiplechoicesrs.model.Deck
import com.example.multiplechoicesrs.model.Question

@Composable
fun StudyScreenLoad(
    deck: Deck,
    categoryIdList: List<Int>,
    numToStudy: Int,
    navToDeckList: () -> Unit,
    modifier: Modifier = Modifier
) {
    val studyHelper = StudyHelper(LocalContext.current)

    ProvideAppBarTitle {
        Text(deck.name)
    }

    ProvideAppBarNavigationIcon {
        IconButton(
            onClick = {
                finishStudySession(navToDeckList)
            }
        ) {
            Icon(
                painter = painterResource(R.drawable.outline_arrow_back_24),
                contentDescription = "戻る"
            )
        }
    }

    var questionList by remember { mutableStateOf(emptyList<Question>()) }

    questionList = studyHelper.getQuestions(deck.deckId, categoryIdList, numToStudy)

    if (questionList.isEmpty()) {
        Dialog(
            onDismissRequest = { },
            DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = false)
        ) {
            Box(
                contentAlignment= Alignment.Center,
                modifier = Modifier
                    .size(100.dp)
                    .background(White, shape = RoundedCornerShape(8.dp))
            ) {
                CircularProgressIndicator()
            }
        }
    } else {
        Column(modifier) {
            StudyScreen(questionList)
        }
    }
}

@Composable
fun StudyScreen(questionList: List<Question>) {
    var currentQuestion by remember { mutableStateOf(questionList.first()) }
    val scrollState = rememberScrollState()

    Box(Modifier.zIndex(1f)) {
        Column(modifier = Modifier.verticalScroll(scrollState)) {
            Text(currentQuestion.question)
        }

        Column {
            Spacer(Modifier.weight(1f))
            AnswerBottomSheet(currentQuestion)
        }

    }

}

@Composable
fun AnswerBottomSheet(question: Question) {
    val radioOptions = listOf(question.answer1, question.answer2, question.answer3, question.answer4)
    val (selectedOption, onOptionSelected) = remember { mutableStateOf("") }
    var selectedInt by remember { mutableIntStateOf(radioOptions.indexOf(selectedOption)) }
    var submitButtonText by remember { mutableStateOf("確認") }

    ExpandableBottomView {
        Column {
            Column(Modifier.selectableGroup()) {
                radioOptions.forEachIndexed { index, text ->
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                            .selectable(
                                selected = (text == selectedOption),
                                onClick = { onOptionSelected(text) },
                                role = Role.RadioButton
                            )
                            .modifyIf(selectedInt != -1, {
                                var color = Color.Transparent
                                val index = index + 1

                                if (question.correctAnswer == index) {
                                    color = Color.Green
                                } else if (selectedInt == index) {
                                    color = Color.Red
                                }

                                border(
                                    border = BorderStroke(1.dp, color),
                                    shape = RoundedCornerShape(10.dp)
                                )
                            })
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

            Row {
                Spacer(Modifier.weight(1f))

                Button(
                    enabled = selectedOption.isNotEmpty(),
                    onClick = {
                        if (selectedInt != -1) {
                            submitButtonText = "確認"
                        } else {
                            selectedInt = radioOptions.indexOf(selectedOption) + 1
                            submitButtonText = "次"
                        }
                }) {
                    Text(submitButtonText)
                }
            }
        }
    }
}

private fun finishStudySession(navToDeckList: () -> Unit) {
    //TODO: Display Results
    navToDeckList()
}