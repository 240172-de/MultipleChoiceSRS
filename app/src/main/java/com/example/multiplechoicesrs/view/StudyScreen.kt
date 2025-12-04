package com.example.multiplechoicesrs.view

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
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
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.multiplechoicesrs.R
import com.example.multiplechoicesrs.ext.modifyIf
import com.example.multiplechoicesrs.logic.StudyHelper
import com.example.multiplechoicesrs.model.Answer
import com.example.multiplechoicesrs.model.Deck
import com.example.multiplechoicesrs.model.Question
import com.example.multiplechoicesrs.model.StudySession
import com.example.multiplechoicesrs.model.viewmodel.StudyUiState
import com.example.multiplechoicesrs.model.viewmodel.StudyViewModel
import com.example.multiplechoicesrs.ui.theme.GreenCorrectAnswer
import com.example.multiplechoicesrs.ui.theme.RedIncorrectAnswer
import com.example.multiplechoicesrs.view.custom.ExpandableBottomView
import com.example.multiplechoicesrs.view.custom.ProvideAppBarNavigationIcon
import com.example.multiplechoicesrs.view.custom.ProvideAppBarTitle
import com.example.multiplechoicesrs.view.dialog.FullSizeImageDialog
import com.example.multiplechoicesrs.view.dialog.ResultDialog
import com.example.multiplechoicesrs.view.dialog.ShowExplanationDialog

@Composable
fun StudyScreenLoad(
    deck: Deck,
    categoryIdList: List<Int>,
    numToStudy: Int,
    navToDeckList: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val studyViewModel: StudyViewModel = viewModel {
        StudyViewModel(context, deck.deckId, categoryIdList, numToStudy)
    }

    val studyHelper = StudyHelper(context)
    val answerList = remember { emptyList<Answer>().toMutableStateList() }
    var result: StudySession? by remember { mutableStateOf(null)  }

    ProvideAppBarTitle {
        Text(deck.name)
    }

    ProvideAppBarNavigationIcon {
        IconButton(
            onClick = {
                result = studyHelper.onFinishStudySession(answerList)
            }
        ) {
            Icon(
                painter = painterResource(R.drawable.outline_arrow_back_24),
                contentDescription = "戻る"
            )
        }
    }

    if (result != null) {
        val onDismiss = {
            result = null
            navToDeckList()
        }

        if (result!!.numCorrect > 0 || result!!.numIncorrect > 0) {
            ResultDialog(
                result!!.numCorrect,
                result!!.numIncorrect,
            ) {
                onDismiss()
            }
        } else {
            onDismiss()
        }
    }

    Column(modifier) {
        StudyRouting(
            studyUiState = studyViewModel.studyUiState,
            onSubmitAnswer =  { answer ->
                answerList.add(answer)
            },
            onFinish = {
                result = studyHelper.onFinishStudySession(answerList)
            }
        )
    }
}

@Composable
fun StudyRouting(
    studyUiState: StudyUiState,
    onSubmitAnswer: (Answer) -> Unit,
    onFinish: () -> Unit
) {
    when(studyUiState) {
        is StudyUiState.Loading -> LoadingScreen()
        is StudyUiState.NoData -> NoDataScreen()
        is StudyUiState.Success -> StudyScreen(studyUiState.questionList, onSubmitAnswer, onFinish)
    }
}

@Composable
fun StudyScreen(
    questionList: List<Question>,
    onSubmitAnswer: (Answer) -> Unit,
    onFinish: () -> Unit
) {
    val activeQuestionIds = remember { questionList.map { it.questionId }.toMutableStateList() }
    var indexCurrentQuestion by remember { mutableIntStateOf(0) }
    val scrollState = rememberScrollState()
    var fullSizeImage: ImageBitmap? by remember { mutableStateOf(null) }
    var showExplanationDialog by remember { mutableStateOf(false) }

    if (fullSizeImage != null) {
        FullSizeImageDialog(
            fullSizeImage!!
        ) {
            fullSizeImage = null
        }
    }

    if (showExplanationDialog) {
        ShowExplanationDialog(
            explanation = questionList[indexCurrentQuestion].explanation
        ) {
            showExplanationDialog = false
        }
    }

    Box(Modifier.zIndex(1f)) {
        Column(
            modifier = Modifier
                .padding(5.dp)
                .verticalScroll(scrollState)
        ) {
            Text(questionList[indexCurrentQuestion].question)

            questionList[indexCurrentQuestion].questionImage?.let {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Image(
                        bitmap = it,
                        contentDescription = "",
                        modifier = Modifier.clickable {
                            fullSizeImage = it
                        }
                    )
                }
            }
        }

        Column {
            Spacer(Modifier.weight(1f))
            AnswerBottomSheet(questionList[indexCurrentQuestion],
                onSubmitAnswer = { answer ->
                    activeQuestionIds.removeIf {
                        it == answer.questionId
                    }

                    //Move to end of list
                    if (!answer.isCorrect) {
                        activeQuestionIds.add(answer.questionId)
                    }

                    onSubmitAnswer(answer)
                },
                onClickNext = {
                    if (activeQuestionIds.isNotEmpty()) {
                        indexCurrentQuestion = questionList.indexOfFirst {
                            it.questionId == activeQuestionIds.first()
                        }
                    } else {
                        onFinish()
                    }
                },
                onClickImage = {
                    fullSizeImage = it
                },
                onClickShowExplanation = {
                    showExplanationDialog = true
                }
            )
        }
    }
}

//TODO: rememberSaveable
//TODO: No logging during import
//TODO: Test dark mode
@Composable
fun AnswerBottomSheet(
    question: Question,
    onSubmitAnswer: (answer: Answer) -> Unit,
    onClickNext: () -> Unit,
    onClickImage: (ImageBitmap) -> Unit,
    onClickShowExplanation: () -> Unit
) {
    val radioOptions = listOf(question.answer1, question.answer2, question.answer3, question.answer4)
    val answerImageList = listOf(question.answer1Image, question.answer2Image, question.answer3Image, question.answer4Image)
    val (indexSelectedOption, onOptionSelected) = remember { mutableIntStateOf(-1) }
    var submittedAnswer by remember { mutableIntStateOf(-1) }
    var submitButtonText by remember { mutableStateOf("確認") }
    val scrollState = rememberScrollState()

    ExpandableBottomView {
        Column {
            Column(
                Modifier
                    .verticalScroll(scrollState)
                    .weight(1f, fill = false)
                    .selectableGroup()
            ) {
                radioOptions.forEachIndexed { index, text ->
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .selectable(
                                selected = (index == indexSelectedOption),
                                onClick = { onOptionSelected(index) },
                                role = Role.RadioButton
                            )
                            .modifyIf(submittedAnswer != -1) {
                                var color = Color.Transparent
                                val index = index + 1

                                if (question.correctAnswer == index) {
                                    color = GreenCorrectAnswer
                                } else if (submittedAnswer == index) {
                                    color = RedIncorrectAnswer
                                }

                                border(
                                    border = BorderStroke(2.dp, color),
                                    shape = RoundedCornerShape(10.dp)
                                )
                            }
                            .padding(vertical = 5.dp)
                            .padding(horizontal = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = (index == indexSelectedOption),
                            onClick = null
                        )
                        Text(
                            text = text,
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.padding(start = 16.dp)
                        )

                        if (answerImageList[index] != null) {
                            Image(
                                bitmap = answerImageList[index]!!,
                                contentDescription = "",
                                modifier = Modifier.clickable{ onClickImage(answerImageList[index]!!) }
                            )
                        }
                    }
                }
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Spacer(Modifier.weight(1f))

                if (submittedAnswer != -1 && question.explanation.isNotEmpty()) {
                    Button(onClickShowExplanation) {
                        Text("解説表示")
                    }
                }

                Button(
                    enabled = indexSelectedOption != -1,
                    onClick = {
                        if (submittedAnswer != -1) {
                            onOptionSelected(-1)
                            submittedAnswer = -1
                            submitButtonText = "確認"

                            onClickNext()
                        } else {
                            submittedAnswer = indexSelectedOption + 1
                            submitButtonText = "次"

                            onSubmitAnswer(
                                Answer(
                                    questionId = question.questionId,
                                    answerGiven = submittedAnswer,
                                    isCorrect = submittedAnswer == question.correctAnswer
                                )
                            )
                        }
                    }) {
                    Text(submitButtonText)
                }
            }
        }
    }
}