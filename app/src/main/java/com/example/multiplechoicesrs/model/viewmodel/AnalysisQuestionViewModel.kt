package com.example.multiplechoicesrs.model.viewmodel

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.multiplechoicesrs.db.AnswerTableHelper
import com.example.multiplechoicesrs.db.CategoryTableHelper
import com.example.multiplechoicesrs.db.QuestionTableHelper
import com.example.multiplechoicesrs.model.BarChartData
import com.example.multiplechoicesrs.model.BarChartDataEntry
import com.example.multiplechoicesrs.model.BarChartDataSeries
import com.example.multiplechoicesrs.model.BarChartDataSettings
import com.example.multiplechoicesrs.model.BarChartLabelFormat
import com.example.multiplechoicesrs.model.Category
import com.example.multiplechoicesrs.model.PieChartData
import com.example.multiplechoicesrs.model.PieChartDataEntry
import com.example.multiplechoicesrs.model.Question
import com.example.multiplechoicesrs.ui.theme.GreenCorrectAnswer
import com.example.multiplechoicesrs.ui.theme.RedIncorrectAnswer
import kotlinx.coroutines.launch

data class AnalysisQuestionData(
    val question: Question,
    val pieChartData: PieChartData,
    val barChartData: BarChartData,
    val numIncorrect: Int,
    val ratioCorrect: Float
)

sealed interface AnalysisQuestionUiState {
    data class Success(
        val data: List<AnalysisQuestionData>,
        val categories: List<Category>
    ): AnalysisQuestionUiState
    object Loading: AnalysisQuestionUiState
    object NoData: AnalysisQuestionUiState
}

class AnalysisQuestionViewModel(
    context: Context,
    private val deckId: Int
): ViewModel() {
    private val categoryTableHelper = CategoryTableHelper(context)
    private val questionTableHelper = QuestionTableHelper(context)
    private val answerTableHelper = AnswerTableHelper(context)
    var analysisQuestionUiState: AnalysisQuestionUiState by mutableStateOf(AnalysisQuestionUiState.Loading)
        private set

    init {
        getData()
    }

    fun getData() {
        viewModelScope.launch {
            analysisQuestionUiState = AnalysisQuestionUiState.Loading

            val data = mutableListOf<AnalysisQuestionData>()
            val questionList = questionTableHelper.getQuestions(deckId)

            questionList.forEach { question ->
                val answers = answerTableHelper.getAnswers(question.questionId)

                if (answers.isNotEmpty()) {
                    val numTotal = answers.size
                    val numCorrect = answers.count { it.isCorrect }
                    val numIncorrect = numTotal - numCorrect

                    val percentageCorrect = numCorrect.toFloat() / numTotal
                    val percentageIncorrect = numIncorrect.toFloat() / numTotal

                    val numA = answers.count { it.answerGiven == 1 }
                    val numB = answers.count { it.answerGiven == 2 }
                    val numC = answers.count { it.answerGiven == 3 }
                    val numD = answers.count { it.answerGiven == 4 }

                    data.add(AnalysisQuestionData(
                        question = question,
                        pieChartData = PieChartData(
                            "正解率",
                            listOf(
                                PieChartDataEntry("正解", percentageCorrect, GreenCorrectAnswer),
                                PieChartDataEntry("不正解", percentageIncorrect, RedIncorrectAnswer)
                            )
                        ),
                        barChartData = BarChartData(
                            title = "答え",
                            settings = BarChartDataSettings(
                                rotateXAxisLabel = false,
                                labelFormat = BarChartLabelFormat.DEFAULT
                            ),
                            seriesList = listOf(BarChartDataSeries(listOf(
                                BarChartDataEntry(
                                    value = numA.toFloat(),
                                    color = getColor(1, question.correctAnswer)
                                ),
                                BarChartDataEntry(
                                    value = numB.toFloat(),
                                    color = getColor(2, question.correctAnswer)
                                ),
                                BarChartDataEntry(
                                    value = numC.toFloat(),
                                    color = getColor(3, question.correctAnswer)
                                ),
                                BarChartDataEntry(
                                    value = numD.toFloat(),
                                    color = getColor(4, question.correctAnswer)
                                ),
                            ))),
                            labelList = listOf("ア", "イ", "ウ", "エ")
                        ),
                        numIncorrect = numIncorrect,
                        ratioCorrect = percentageCorrect
                    ))
                }
            }

            val categoryIds = data.distinctBy { data ->
                data.question.categoryId
            }.map { data ->
                data.question.categoryId
            }

            val categoryList = categoryTableHelper.getCategories(deckId).filter { category ->
                categoryIds.contains(category.categoryId)
            }

            analysisQuestionUiState = if (data.isEmpty()) {
                AnalysisQuestionUiState.NoData
            } else {
                AnalysisQuestionUiState.Success(data, categoryList)
            }
        }
    }
}

private fun getColor(answerGiven: Int, correctAnswer: Int): Color {
    return if (answerGiven == correctAnswer) GreenCorrectAnswer else RedIncorrectAnswer
}