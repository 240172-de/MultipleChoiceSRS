package com.example.multiplechoicesrs.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.multiplechoicesrs.model.Deck
import com.example.multiplechoicesrs.model.PieChartData
import com.example.multiplechoicesrs.model.viewmodel.AnalysisDeckUiState
import com.example.multiplechoicesrs.model.viewmodel.AnalysisDeckViewModel
import com.example.multiplechoicesrs.view.custom.charts.BarChart
import com.example.multiplechoicesrs.view.custom.charts.PieChart
import com.patrykandpatrick.vico.core.cartesian.data.CartesianValueFormatter
import java.text.DecimalFormat

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
        is AnalysisDeckUiState.Success -> AnalysisDeckCharts(
            pieChartData = analysisDeckUiState.pieChartData,
            barChartDataTotal = analysisDeckUiState.barChartDataTotal,
            barChartDataNormed = analysisDeckUiState.barChartDataNormed
        )
    }
}

@Composable
fun AnalysisDeckCharts(
    pieChartData: PieChartData,
    barChartDataTotal: Map<String, Float>,
    barChartDataNormed: Map<String, Float>
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(25.dp)
    ) {
        PieChart(pieChartData)

        BarChart(
            title = "正解数",
            data = barChartDataTotal
        )

        BarChart(
            title = "正解率",
            data = barChartDataNormed,
            dataLabelFormatter = CartesianValueFormatter.decimal(DecimalFormat("#.#%")),
        )
    }
}