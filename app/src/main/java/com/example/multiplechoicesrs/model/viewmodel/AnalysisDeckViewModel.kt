package com.example.multiplechoicesrs.model.viewmodel

import com.example.multiplechoicesrs.model.PieChartData

sealed interface AnalysisDeckUiState {
    data class Success(val pieChartData: PieChartData) : AnalysisDeckUiState
    object Loading : AnalysisDeckUiState
}