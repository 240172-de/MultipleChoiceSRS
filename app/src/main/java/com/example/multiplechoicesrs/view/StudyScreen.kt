package com.example.multiplechoicesrs.view

import android.annotation.SuppressLint
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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.multiplechoicesrs.R
import com.example.multiplechoicesrs.db.QuestionResultTableHelper
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
import com.example.multiplechoicesrs.view.dialog.ShowMemoDialog

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
    val answerList = rememberSaveable { emptyList<Answer>().toMutableStateList() }
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
                contentDescription = stringResource(R.string.back)
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
    val questionResultTableHelper = QuestionResultTableHelper(LocalContext.current)
    val activeQuestionIds = rememberSaveable { questionList.map { it.questionId }.toMutableStateList() }
    var indexCurrentQuestion by rememberSaveable { mutableIntStateOf(0) }
    val scrollState = rememberScrollState()
    var fullSizeImage: ImageBitmap? by remember { mutableStateOf(null) }
    var showExplanationDialog by remember { mutableStateOf(false) }
    var showMemoDialog by remember { mutableStateOf(false) }

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

    if (showMemoDialog) {
        ShowMemoDialog(
            memo = questionList[indexCurrentQuestion].result.memo,
            onSubmit = { memo ->
                showMemoDialog = false

                questionList[indexCurrentQuestion].result.memo = memo
                questionResultTableHelper.saveQuestionResult(questionList[indexCurrentQuestion].result)
            },
            onDismissRequest = {
                showMemoDialog = false
            }
        )
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
                        contentDescription = stringResource(R.string.full_size_image),
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
                },
                onClickShowMemo = {
                    showMemoDialog = true
                }
            )
        }
    }
}

@SuppressLint("LocalContextGetResourceValueCall")
@Composable
fun AnswerBottomSheet(
    question: Question,
    onSubmitAnswer: (answer: Answer) -> Unit,
    onClickNext: () -> Unit,
    onClickImage: (ImageBitmap) -> Unit,
    onClickShowExplanation: () -> Unit,
    onClickShowMemo: () -> Unit
) {
    val context = LocalContext.current
    val radioOptions = listOf(question.answer1, question.answer2, question.answer3, question.answer4)
    val answerImageList = listOf(question.answer1Image, question.answer2Image, question.answer3Image, question.answer4Image)
    val (indexSelectedOption, onOptionSelected) = rememberSaveable { mutableIntStateOf(-1) }
    var submittedAnswer by rememberSaveable { mutableIntStateOf(-1) }
    var submitButtonText by rememberSaveable { mutableStateOf(context.getString(R.string.confirm)) }
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
                                var strokeWidth = 2.dp
                                val index = index + 1

                                if (question.correctAnswer == index) {
                                    color = GreenCorrectAnswer
                                    strokeWidth = 3.5.dp
                                } else if (submittedAnswer == index) {
                                    color = RedIncorrectAnswer
                                    strokeWidth = 2.dp
                                }

                                border(
                                    border = BorderStroke(strokeWidth, color),
                                    shape = RoundedCornerShape(10.dp)
                                )
                            }
                            .padding(vertical = 7.dp)
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
                                contentDescription = stringResource(R.string.full_size_image),
                                modifier = Modifier.clickable{ onClickImage(answerImageList[index]!!) }
                            )
                        }
                    }
                }
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                if (submittedAnswer != -1) {
                    Button(onClickShowMemo) {
                        Text(stringResource(R.string.show_memo))
                    }
                }

                Spacer(Modifier.weight(1f))

                if (submittedAnswer != -1 && question.explanation.isNotEmpty()) {
                    Button(onClickShowExplanation) {
                        Text(stringResource(R.string.show_explanation))
                    }
                }

                Button(
                    enabled = indexSelectedOption != -1,
                    onClick = {
                        if (submittedAnswer != -1) {
                            onOptionSelected(-1)
                            submittedAnswer = -1
                            submitButtonText = context.getString(R.string.confirm)

                            onClickNext()
                        } else {
                            submittedAnswer = indexSelectedOption + 1
                            submitButtonText = context.getString(R.string.next)

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