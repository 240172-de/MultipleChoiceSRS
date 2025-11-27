package com.example.multiplechoicesrs.view.custom.charts

import android.graphics.Typeface
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.multiplechoicesrs.model.PieChartData
import com.example.multiplechoicesrs.model.PieChartDataEntry
import com.example.multiplechoicesrs.ui.theme.MutedWhite
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter

@Composable
fun PieChart(data: PieChartData) {
    Column(
        modifier = Modifier
            .padding(18.dp)
            .size(360.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        val backgroundColor = MaterialTheme.colorScheme.background.toArgb()
        val foregroundColor = MaterialTheme.colorScheme.onBackground.toArgb()

        Crossfade(targetState = data) { pieChartData ->
            AndroidView(
                factory = { context ->
                    PieChart(context).apply {
                        layoutParams = LinearLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT,
                        )
                        centerText = pieChartData.centerText
                        setCenterTextSize(22f)
                        setCenterTextTypeface(Typeface.DEFAULT_BOLD)
                        setCenterTextColor(foregroundColor)

                        description.isEnabled = false
                        isDrawHoleEnabled = true
                        setHoleColor(backgroundColor)

                        setEntryLabelTextSize(16f)
                        setUsePercentValues(true)

                        legend.isEnabled = true
                        legend.textSize = 16F
                        legend.horizontalAlignment = Legend.LegendHorizontalAlignment.RIGHT
                        legend.verticalAlignment = Legend.LegendVerticalAlignment.TOP
                        legend.orientation = Legend.LegendOrientation.VERTICAL
                        legend.textColor = foregroundColor
                    }
                },
                modifier = Modifier
                    .wrapContentSize()
                    .padding(5.dp),
                update = {
                    updatePieChartWithData(it, pieChartData.entries)
                })
        }
    }
}

private fun updatePieChartWithData(
    chart: PieChart,
    data: List<PieChartDataEntry>
) {
    val entries = ArrayList<PieEntry>(data.size)
    val colors = ArrayList<Int>(data.size)

    data.forEach { item ->
        entries.add(PieEntry(item.value, item.fieldName))
        colors.add(item.color.toArgb())
    }

    val dataSet = PieDataSet(entries, "")

    dataSet.colors = colors

    dataSet.yValuePosition = PieDataSet.ValuePosition.INSIDE_SLICE
    dataSet.xValuePosition = PieDataSet.ValuePosition.INSIDE_SLICE

    dataSet.sliceSpace = 2f
    dataSet.valueTextSize = 18f
    dataSet.valueTypeface = Typeface.DEFAULT_BOLD

    dataSet.valueTextColor = MutedWhite.toArgb()

    val pieData = PieData(dataSet)
    pieData.setValueFormatter(PercentFormatter(chart))
    chart.data = pieData

    chart.invalidate()
}
