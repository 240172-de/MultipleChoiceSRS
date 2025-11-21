package com.example.multiplechoicesrs.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.multiplechoicesrs.R
import com.example.multiplechoicesrs.model.Deck
import com.example.multiplechoicesrs.model.Question
import com.example.multiplechoicesrs.model.viewmodel.AnalysisQuestionData
import com.example.multiplechoicesrs.model.viewmodel.AnalysisQuestionUiState
import com.example.multiplechoicesrs.model.viewmodel.AnalysisQuestionViewModel
import com.example.multiplechoicesrs.view.custom.ExpandableView
import com.example.multiplechoicesrs.view.custom.charts.PieChart
import com.example.multiplechoicesrs.view.dialog.ShowQuestionDialog

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
    var filterText by remember { mutableStateOf("") }

    var selectedQuestion: Question? by remember { mutableStateOf(null) }
    var showQuestionDialog by remember { mutableStateOf(false) }

    if (showQuestionDialog && selectedQuestion != null) {
        ShowQuestionDialog(selectedQuestion!!) {
            showQuestionDialog = false
        }
    }

    Column(
        verticalArrangement = Arrangement.spacedBy(15.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            OutlinedTextField(
                value = filterText,
                onValueChange = { filterText = it },
                leadingIcon = {
                    Icon(
                        painter = painterResource(R.drawable.baseline_search_24),
                        contentDescription = "フィルター"
                    )
                },
                modifier = Modifier.weight(1f)
            )

            OutlinedButton(
                onClick = {
                    //TODO: Filter categories
                },
                modifier = Modifier.size(46.dp),
                contentPadding = PaddingValues(0.dp),
                shape = RoundedCornerShape(15)
            ) {
                Icon(
                    painter = painterResource(R.drawable.baseline_filter_alt_24),
                    contentDescription = "分野フィルター",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .size(36.dp)
                )
            }
        }

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(list.filter { item ->
                item.question.source.contains(filterText)
            }) { item ->
                AnalysisQuestionItem(item) {
                    selectedQuestion = item.question
                    showQuestionDialog = true
                }
            }
        }
    }
}

@Composable
fun AnalysisQuestionItem(
    data: AnalysisQuestionData,
    onClickShowQuestion: () -> Unit
) {
    ExpandableView(
        title = data.question.source,
        initialExpandedState = false
    ) {
        Column {
            Button(onClick = {
                onClickShowQuestion()
            }) {
                Text("問題表示")
            }

            PieChart(data.pieChartData)
        }
    }
}