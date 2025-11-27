package com.example.multiplechoicesrs.model.viewmodel

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.multiplechoicesrs.db.AnswerTableHelper
import com.example.multiplechoicesrs.db.CategoryTableHelper
import com.example.multiplechoicesrs.db.QuestionTableHelper
import com.example.multiplechoicesrs.model.Answer
import com.example.multiplechoicesrs.model.BarChartData
import com.example.multiplechoicesrs.model.BarChartDataEntry
import com.example.multiplechoicesrs.model.BarChartDataSeries
import com.example.multiplechoicesrs.model.BarChartDataSettings
import com.example.multiplechoicesrs.model.BarChartLabelFormat
import com.example.multiplechoicesrs.model.PieChartData
import com.example.multiplechoicesrs.model.PieChartDataEntry
import com.example.multiplechoicesrs.ui.theme.GreenCorrectAnswer
import com.example.multiplechoicesrs.ui.theme.RedIncorrectAnswer
import kotlinx.coroutines.launch

sealed interface AnalysisDeckUiState {
    data class Success(
        val pieChartData: PieChartData,
        val barChartDataTotal: BarChartData,
        val barChartDataNormed: BarChartData
    ): AnalysisDeckUiState
    object Loading: AnalysisDeckUiState
    object NoData: AnalysisDeckUiState
}

class AnalysisDeckViewModel(
    context: Context,
    private val deckId: Int
): ViewModel() {
    val categoryTableHelper = CategoryTableHelper(context)
    val questionTableHelper = QuestionTableHelper(context)
    val answerTableHelper = AnswerTableHelper(context)
    var analysisDeckUiState: AnalysisDeckUiState by mutableStateOf(AnalysisDeckUiState.Loading)
        private set

    init {
        getData()
    }

    fun getData() {
        viewModelScope.launch {
            analysisDeckUiState = AnalysisDeckUiState.Loading

            val questionList = questionTableHelper.getQuestions(deckId)
            val answers = mutableListOf<Answer>()

            questionList.forEach { question ->
                answers.addAll(answerTableHelper.getAnswers(question.questionId))
            }

            if (answers.isEmpty()) {
                analysisDeckUiState = AnalysisDeckUiState.NoData
            } else {
                val numTotal = answers.size
                val numCorrect = answers.count { it.isCorrect }
                val numIncorrect = numTotal - numCorrect

                val percentageCorrect = numCorrect.toFloat() / numTotal
                val percentageIncorrect = numIncorrect.toFloat() / numTotal

                val pieChartData = PieChartData(
                    "全体",
                    listOf(
                        PieChartDataEntry("正解", percentageCorrect, GreenCorrectAnswer),
                        PieChartDataEntry("不正解", percentageIncorrect, RedIncorrectAnswer)
                    )
                )

                val categoryIds = questionList.distinctBy { question ->
                    question.categoryId
                }.map { question ->
                    question.categoryId
                }

                val categoryList = categoryTableHelper.getCategories(deckId).filter { category ->
                    categoryIds.contains(category.categoryId)
                }

                val labelList = mutableListOf<String>()
                val entriesCorrect = mutableListOf<BarChartDataEntry>()
                val entriesIncorrect = mutableListOf<BarChartDataEntry>()
                val entriesNormed = mutableListOf<BarChartDataEntry>()

                questionList.groupBy { question ->
                    question.categoryId
                }.forEach { categoryId, questions ->
                    var sum = 0
                    var total = 0

                    questions.forEach {
                        answers.filter { answer ->
                            answer.questionId == it.questionId
                        }.forEach { answer ->
                            if (answer.isCorrect) {
                                sum++
                            }

                            total++
                        }
                    }

                    val categoryName = categoryList.first { category ->
                        category.categoryId == categoryId
                    }.name

                    if (total > 0) {
                        labelList.add(categoryName)

                        entriesCorrect.add(BarChartDataEntry(
                            value = sum.toFloat(),
                            color = GreenCorrectAnswer
                        ))

                        entriesIncorrect.add(BarChartDataEntry(
                            value = (total - sum).toFloat(),
                            color = RedIncorrectAnswer
                        ))

                        entriesNormed.add(BarChartDataEntry(
                            value = sum.toFloat() / total,
                            color = GreenCorrectAnswer
                        ))
                    }
                }

                val dataTotal = BarChartData(
                    title = "正解数",
                    settings = BarChartDataSettings(
                        rotateXAxisLabel = true,
                        labelFormat = BarChartLabelFormat.DEFAULT
                    ),
                    seriesList = listOf(
                        BarChartDataSeries(entriesCorrect),
                        BarChartDataSeries(entriesIncorrect)
                    ),
                    labelList = labelList
                )

                val dataNormed = BarChartData(
                    title = "正解率",
                    settings = BarChartDataSettings(
                        rotateXAxisLabel = true,
                        labelFormat = BarChartLabelFormat.PERCENTAGE
                    ),
                    seriesList = listOf(BarChartDataSeries(entriesNormed)),
                    labelList = labelList
                )

                analysisDeckUiState = AnalysisDeckUiState.Success(
                    pieChartData = pieChartData,
                    barChartDataTotal = dataTotal,
                    barChartDataNormed = dataNormed
                )
            }
        }
    }
}