package com.example.multiplechoicesrs.view

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.multiplechoicesrs.model.Deck
import com.example.multiplechoicesrs.model.viewmodel.AnalysisDeckUiState
import com.example.multiplechoicesrs.model.viewmodel.AnalysisQuestionData
import com.example.multiplechoicesrs.model.viewmodel.AnalysisQuestionUiState
import com.example.multiplechoicesrs.model.viewmodel.AnalysisQuestionViewModel
import com.example.multiplechoicesrs.view.custom.ExpandableView
import com.example.multiplechoicesrs.view.custom.charts.PieChart

@Composable
fun AnalysisQuestion(deck: Deck) {
    Column {
        val context = LocalContext.current
        val analysisQuestionViewModel: AnalysisQuestionViewModel = viewModel {
            AnalysisQuestionViewModel(context, deck.deckId)
        }

        AnalysisQuestionRouting(analysisQuestionViewModel.analysisQuestionUiState)
    }
}

@Composable
fun AnalysisQuestionRouting(
    analysisQuestionUiState: AnalysisQuestionUiState
) {
    when(analysisQuestionUiState) {
        is AnalysisQuestionUiState.Loading -> LoadingScreen()
        is AnalysisQuestionUiState.NoData -> NoDataScreen()
        is AnalysisQuestionUiState.Success -> AnalysisQuestionList(analysisQuestionUiState.data)
    }
}

@Composable
fun AnalysisQuestionList(list: List<AnalysisQuestionData>) {
    Column {
        //TODO: Filtering
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(list) { item ->
                AnalysisQuestionItem(item)
            }
        }
    }
}

@Composable
fun AnalysisQuestionItem(data: AnalysisQuestionData) {
    ExpandableView(
        title = data.question.source,
        initialExpandedState = false
    ) {
        Column {
            Button(onClick = {
                //TODO: Open Dialog
                Log.d("TEST", "click ${data.question.source}")
            }) {
                Text("問題表示")
            }

            PieChart(data.pieChartData)
        }
    }
}