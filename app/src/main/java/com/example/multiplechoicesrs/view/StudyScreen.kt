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
import com.example.multiplechoicesrs.ui.theme.GreenCorrectAnswer
import com.example.multiplechoicesrs.ui.theme.RedIncorrectAnswer

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
    var indexCurrentQuestion by remember { mutableIntStateOf(0) }
    var numCorrect by remember { mutableIntStateOf(0) }
    var numIncorrect by remember { mutableIntStateOf(0) }
    val scrollState = rememberScrollState()

    Box(Modifier.zIndex(1f)) {
        Column(
            modifier = Modifier
                .padding(5.dp)
                .verticalScroll(scrollState)
        ) {
            Text(questionList[indexCurrentQuestion].question)
        }

        Column {
            Spacer(Modifier.weight(1f))
            AnswerBottomSheet(questionList[indexCurrentQuestion]) { questionId, answerGiven, isCorrect ->
                if (isCorrect) {
                    numCorrect++
                } else {
                    numIncorrect++
                }

                if (indexCurrentQuestion < questionList.size - 1) {
                    indexCurrentQuestion++
                }
            }
        }
    }
}

@Composable
fun AnswerBottomSheet(
    question: Question,
    onClickNext: (questionId: Int, answerGiven: Int, isCorrect: Boolean) -> Unit
) {
    val radioOptions = listOf(question.answer1, question.answer2, question.answer3, question.answer4)
    val (selectedOption, onOptionSelected) = remember { mutableStateOf("") }
    var selectedInt by remember { mutableIntStateOf(radioOptions.indexOf(selectedOption)) }
    var submitButtonText by remember { mutableStateOf("確認") }
    val scrollState = rememberScrollState()

    ExpandableBottomView {
        Column {
            Column(Modifier
                .verticalScroll(scrollState)
                .weight(1f, fill = false)
                .selectableGroup()
            ) {
                radioOptions.forEachIndexed { index, text ->
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .selectable(
                                selected = (text == selectedOption),
                                onClick = { onOptionSelected(text) },
                                role = Role.RadioButton
                            )
                            .modifyIf(selectedInt != -1, {
                                var color = Color.Transparent
                                val index = index + 1

                                if (question.correctAnswer == index) {
                                    color = GreenCorrectAnswer
                                } else if (selectedInt == index) {
                                    color = RedIncorrectAnswer
                                }

                                border(
                                    border = BorderStroke(2.dp, color),
                                    shape = RoundedCornerShape(10.dp)
                                )
                            })
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

            Row {
                Spacer(Modifier.weight(1f))

                Button(
                    enabled = selectedOption.isNotEmpty(),
                    onClick = {
                        if (selectedInt != -1) {
                            submitButtonText = "確認"
                            //TODO: Load next question
                            //Mutable list und mit remove?
                            //Oder index speichern?
                            //Lieber index speichern, da ja relearn status existiert
                            //In jedem Fall muss hier mit callback gearbeitet werden
                            //Was fur Infos mussen verarbeitet werden?
                            //Letztendlich soll gespeichert werden welche Antwort und ob diese korrekt
                            //also givenAnswer + isCorrect
                            //Falls correct kommt die Frage in dieser Session nicht mehr hoch
                            //Falls inkorrekt muss erneut kommen
                            //Also 2 Listen?
                            //Eine mit allen Fragen
                            //Eine andere mit den questionId die noch due sind
                            //Oder man konnte gleich die questionResults reinschmeissen
                            //Es muss ja auch die Box verandert werden
                            //Wenn das sofort passiert muss man nicht speichern, wie oft eine spezifische Frage in der Session falsch, oder?
                            //Aber fur StudySession insg. wie viele correct, wie viele inkorrekt
                            //Was muss sonst noch gespeichert werden?
                            //StudySession:
                            //numCorrect
                            //numIncorrect
                            //Answer:
                            //questionId
                            //answerGiven
                            //isCorrect
                            //QuestionResult:
                            //questionId
                            //Ob korrekt oder nicht
                            //Daraus neuer status, dateDue, box
                            //Macht das Sinn, hier nur zu speichern wie oft korrekt?
                            //Sollte nicht auch numIncorrect pro Frage gespeichert werden?
                            //Das kann aber eigentlich per answer herausgefunden werden
                            //Macht der aktuelle Aufbau Sinn?
                            //Man konnte gucken wie viele Answer Datensatze zu einer Question und dann minus numCorrect
                            //Dann mussen nicht alle Answer Datensatze durchlaufen werden
                            //TODO: Bei Import QuestionResult Datensatz anlegen?

                            onClickNext(question.questionId, selectedInt, selectedInt == question.correctAnswer)
                            selectedInt = -1
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