package com.example.multiplechoicesrs.model

import androidx.compose.ui.graphics.Color
import com.patrykandpatrick.vico.core.cartesian.data.CartesianValueFormatter
import java.text.DecimalFormat

data class BarChartData(
    val title: String,
    val settings: BarChartDataSettings,
    val seriesList: List<BarChartDataSeries>,
    val labelList: List<String>
)

data class BarChartDataSeries(
    val entries: List<BarChartDataEntry>
)

data class BarChartDataEntry(
    val value: Float,
    val color: Color
)

data class BarChartDataSettings(
    val rotateXAxisLabel: Boolean,
    val labelFormat: BarChartLabelFormat,
)

enum class BarChartLabelFormat(
    val labelFormatter: CartesianValueFormatter,
    val stepSize: Double
) {
    DEFAULT(
        labelFormatter = CartesianValueFormatter.Default,
        stepSize = 1.0
    ),
    PERCENTAGE(
        labelFormatter = CartesianValueFormatter.decimal(DecimalFormat("#.#%")),
        stepSize = 0.2
    )
}
