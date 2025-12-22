package com.example.multiplechoicesrs.model.viewmodel

import android.content.Context
import android.util.Log
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
import com.example.multiplechoicesrs.model.LineChartData
import com.example.multiplechoicesrs.model.PieChartData
import com.example.multiplechoicesrs.model.PieChartDataEntry
import com.example.multiplechoicesrs.ui.theme.GreenCorrectAnswer
import com.example.multiplechoicesrs.ui.theme.RedIncorrectAnswer
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.temporal.TemporalAdjuster
import java.time.temporal.TemporalAdjusters

sealed interface AnalysisTimeUiState {
    data class Success(
        val weeklyData: LineChartData,
    ): AnalysisTimeUiState
    object Loading: AnalysisTimeUiState
    object NoData: AnalysisTimeUiState
}

class AnalysisTimeViewModel(
    context: Context,
    private val deckId: Int,
    val numWeeks: Int
): ViewModel() {
    private val questionTableHelper = QuestionTableHelper(context)
    private val answerTableHelper = AnswerTableHelper(context)
    var analysisTimeUiState: AnalysisTimeUiState by mutableStateOf(AnalysisTimeUiState.Loading)
        private set

    init {
        getData()
    }

    fun getData() {
        viewModelScope.launch {
            analysisTimeUiState = AnalysisTimeUiState.Loading

            val questionList = questionTableHelper.getQuestions(deckId)
            val answers = mutableListOf<Answer>()

            questionList.forEach { question ->
                answers.addAll(answerTableHelper.getAnswers(question.questionId))
            }

            if (answers.isEmpty()) {
                analysisTimeUiState = AnalysisTimeUiState.NoData
            } else {
                //TODO: Daten aufbereiten
                //TODO: if only single week: no data
                //TODO: numWeeks als Parameter fur getData()?
                //If no answers in the weeks, set 0?
                //TODO: Labels: x週間前
                val startWeek = ZonedDateTime.now().withZoneSameInstant(ZoneId.of("Japan"))
                    .minusWeeks(numWeeks.toLong())
                    .with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY))
                    .withHour(0)
                    .withMinute(0)
                    .withSecond(0)
                    .toString()
                Log.d("TEST", startWeek)
                val recentAnswers = answers.filter { answer ->
                    answer.timestamp > startWeek
                }


                analysisTimeUiState = AnalysisTimeUiState.Success(
                    weeklyData = LineChartData(
                        title = "Test",
                        data = emptyList()
                    )
                )
            }
        }
    }
}