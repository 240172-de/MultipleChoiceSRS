package com.example.multiplechoicesrs.view.dialog

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.multiplechoicesrs.R
import com.example.multiplechoicesrs.model.Question

@Composable
fun ShowQuestionDialog(
    question: Question,
    onDismissRequest: () -> Unit
) {
    Dialog(onDismissRequest) {
        Card(
            modifier = Modifier.padding(16.dp),
            shape = RoundedCornerShape(16.dp),
        ) {
            Column(
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .padding(top = 20.dp),
            ) {
                Column(
                    modifier = Modifier
                        .verticalScroll(rememberScrollState())
                        .weight(1f, false),
                    verticalArrangement = Arrangement.spacedBy(7.dp),
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(question.question, Modifier.padding(bottom = 10.dp))

                    question.questionImage?.let {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Image(
                                bitmap = it,
                                contentDescription = stringResource(R.string.image_content_description),
                                modifier = Modifier.fillMaxWidth(),
                                contentScale = ContentScale.FillWidth,
                            )
                        }
                    }

                    Text(stringResource(R.string.answer_a, question.answer1))
                    AnswerImage(question.answer1Image)

                    Text(stringResource(R.string.answer_i, question.answer2))
                    AnswerImage(question.answer2Image)

                    Text(stringResource(R.string.answer_u, question.answer3))
                    AnswerImage(question.answer3Image)

                    Text(stringResource(R.string.answer_e, question.answer4))
                    AnswerImage(question.answer4Image)

                    val correctAnswer = when (question.correctAnswer) {
                        1 -> "ア"
                        2 -> "イ"
                        3 -> "ウ"
                        else -> "エ"
                    }
                    Text(stringResource(R.string.correct_answer, correctAnswer), Modifier.padding(top = 10.dp))
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    TextButton(
                        onClick = { onDismissRequest() },
                        modifier = Modifier.padding(8.dp),
                    ) {
                        Text(stringResource(R.string.close))
                    }
                }
            }
        }
    }
}

@Composable
private fun AnswerImage(imageBitmap: ImageBitmap?) {
    if (imageBitmap != null) {
        Image(
            bitmap = imageBitmap,
            contentDescription = stringResource(R.string.image_content_description),
            modifier = Modifier.fillMaxWidth(),
            contentScale = ContentScale.FillWidth,
        )
    }
}