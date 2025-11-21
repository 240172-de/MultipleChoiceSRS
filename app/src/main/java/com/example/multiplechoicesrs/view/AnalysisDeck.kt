package com.example.multiplechoicesrs.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.multiplechoicesrs.model.Deck
import com.example.multiplechoicesrs.model.PieChartData
import com.example.multiplechoicesrs.model.viewmodel.AnalysisDeckUiState
import com.example.multiplechoicesrs.model.viewmodel.AnalysisDeckViewModel
import com.example.multiplechoicesrs.view.custom.charts.PieChart

@Composable
fun AnalysisDeck(deck: Deck) {
    Column {
        val context = LocalContext.current
        val analysisDeckViewModel: AnalysisDeckViewModel = viewModel {
            AnalysisDeckViewModel(context, deck.deckId)
        }

        AnalysisDeckRouting(analysisDeckViewModel.analysisDeckUiState)
    }
}

@Composable
fun AnalysisDeckRouting(
    analysisDeckUiState: AnalysisDeckUiState
) {
    when(analysisDeckUiState) {
        is AnalysisDeckUiState.Loading -> LoadingScreen()
        is AnalysisDeckUiState.NoData -> NoDataScreen()
        is AnalysisDeckUiState.Success -> AnalysisDeckCharts(analysisDeckUiState.pieChartData)
    }
}

@Composable
fun AnalysisDeckCharts(pieChartData: PieChartData) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        PieChart(pieChartData)
    }
}