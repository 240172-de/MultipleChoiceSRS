package com.example.multiplechoicesrs.model

import androidx.compose.ui.graphics.Color

data class PieChartData(
    val centerText: String,
    val entries: List<PieChartDataEntry>
)

data class PieChartDataEntry(
    val fieldName: String,
    val value: Float,
    val color: Color
)