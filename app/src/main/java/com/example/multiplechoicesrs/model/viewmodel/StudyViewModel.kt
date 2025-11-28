package com.example.multiplechoicesrs.model.viewmodel

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.multiplechoicesrs.logic.StudyHelper
import com.example.multiplechoicesrs.model.Question
import kotlinx.coroutines.launch

sealed interface StudyUiState {
    data class Success(val questionList: List<Question>): StudyUiState
    object NoData: StudyUiState
    object Loading: StudyUiState
}

class StudyViewModel(
    context: Context,
    val deckId: Int,
    val categoryIdList: List<Int>,
    val numToStudy: Int
): ViewModel() {
    private val studyHelper = StudyHelper(context)
    var studyUiState: StudyUiState by mutableStateOf(StudyUiState.Loading)
        private set

    init {
        getQuestions()
    }

    fun getQuestions() {
        viewModelScope.launch {
            studyUiState = StudyUiState.Loading
            val questionList = studyHelper.getQuestions(deckId, categoryIdList, numToStudy)

            studyUiState = if (questionList.isEmpty()) {
                StudyUiState.NoData
            } else {
                StudyUiState.Success(questionList)
            }
        }
    }
}