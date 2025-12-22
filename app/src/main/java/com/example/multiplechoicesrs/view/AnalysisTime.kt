package com.example.multiplechoicesrs.view

import android.R.id.icon
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.multiplechoicesrs.model.Deck
import com.example.multiplechoicesrs.model.LineChartData
import com.example.multiplechoicesrs.model.viewmodel.AnalysisTimeUiState
import com.example.multiplechoicesrs.model.viewmodel.AnalysisTimeViewModel
import com.example.multiplechoicesrs.view.custom.NumberPicker
import com.example.multiplechoicesrs.view.custom.charts.LineChart

@Composable
fun AnalysisTime(deck: Deck) {
    var numWeeks by remember { mutableIntStateOf(4) }
    val lazyListState = rememberLazyListState(initialFirstVisibleItemIndex = 0)
    var values = remember {
        (2..12).map { it.toString() }
    }


    Column {
        val context = LocalContext.current
        val analysisTimeViewModel: AnalysisTimeViewModel = viewModel {
            AnalysisTimeViewModel(context, deck.deckId, numWeeks)
        }

        //TODO: NumberPicker
        //Via slider?
        //Or NumberPicker (Drehrad)
        //Custom oder per Library?
        //TODO: In onChanged vieModel getData erneut aufrufen?

        NumberPicker(
            modifier = Modifier.fillMaxWidth(),
            list = values,
            fontSize = 32.sp,
            state = lazyListState,
            flingBehavior = rememberSnapFlingBehavior(lazyListState = lazyListState)
        )


        AnalysisTimeRouting(analysisTimeViewModel.analysisTimeUiState)
    }
}

@Composable
fun AnalysisTimeRouting(
    analysisTimeUiState: AnalysisTimeUiState
) {
    when(analysisTimeUiState) {
        is AnalysisTimeUiState.Loading -> LoadingScreen()
        is AnalysisTimeUiState.NoData -> NoDataScreen()
        is AnalysisTimeUiState.Success -> AnalysisTimeCharts(
            weeklyData = analysisTimeUiState.weeklyData
        )
    }
}

@Composable
fun AnalysisTimeCharts(
    weeklyData: LineChartData
) {
    LineChart(weeklyData)
//    Text("Test")
}