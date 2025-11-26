package com.example.multiplechoicesrs.view.custom.charts

import android.text.TextUtils
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.multiplechoicesrs.ui.theme.GreenCorrectAnswer
import com.patrykandpatrick.vico.compose.cartesian.CartesianChartHost
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberBottom
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberStart
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberColumnCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.rememberCartesianChart
import com.patrykandpatrick.vico.compose.common.component.rememberLineComponent
import com.patrykandpatrick.vico.compose.common.fill
import com.patrykandpatrick.vico.core.cartesian.axis.BaseAxis
import com.patrykandpatrick.vico.core.cartesian.axis.HorizontalAxis
import com.patrykandpatrick.vico.core.cartesian.axis.VerticalAxis
import com.patrykandpatrick.vico.core.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.core.cartesian.data.CartesianValueFormatter
import com.patrykandpatrick.vico.core.cartesian.data.columnSeries
import com.patrykandpatrick.vico.core.cartesian.layer.ColumnCartesianLayer
import com.patrykandpatrick.vico.core.common.component.TextComponent
import com.patrykandpatrick.vico.core.common.data.ExtraStore


@Composable
fun BarChart(
    title: String,
    data: Map<String, Float>,
    dataLabelFormatter: CartesianValueFormatter = CartesianValueFormatter.Default,
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            title,
            fontWeight = FontWeight.Bold
        )

        ColumnChart(data, dataLabelFormatter, Modifier.sizeIn(minHeight = 360.dp))
    }
}

val labelListKey = ExtraStore.Key<List<String>>()

//TODO: RangeProvider wenn mit Prozent
//https://github.com/patrykandpatrick/vico/blob/bb10a34/sample/src/main/java/com/patrykandpatrick/vico/sample/showcase/charts/TemperatureAnomalies.kt#L58-L67

@Composable
private fun ColumnChart(
    modelProducer: CartesianChartModelProducer,
    dataLabelFormatter: CartesianValueFormatter,
    modifier: Modifier = Modifier,
) {
    CartesianChartHost(
        chart =
            rememberCartesianChart(
                rememberColumnCartesianLayer(
                    dataLabelValueFormatter = dataLabelFormatter,
                    columnProvider = ColumnCartesianLayer.ColumnProvider.series(
                        listOf(
                            rememberLineComponent(
                                fill(GreenCorrectAnswer),
                                thickness = 5.dp
                            )
                        )
                    ),
                    dataLabel = TextComponent()
                ),
                startAxis = VerticalAxis.rememberStart(
                    valueFormatter = dataLabelFormatter
                ),
                bottomAxis = HorizontalAxis.rememberBottom(
                    size = BaseAxis.Size.Auto(minDp = 120f),
                    labelRotationDegrees = -65f,
                    label = TextComponent(
                        lineCount = 2,
                        truncateAt = TextUtils.TruncateAt.MIDDLE

                    ),
                    valueFormatter = CartesianValueFormatter { context, x, _ ->
                        context.model.extraStore[labelListKey][x.toInt()]
                    }
                ),
            ),
        modelProducer = modelProducer,
        modifier = modifier,
    )
}

@Composable
private fun ColumnChart(
    data: Map<String, Float>,
    dataLabelFormatter: CartesianValueFormatter,
    modifier: Modifier = Modifier
) {
    val modelProducer = remember { CartesianChartModelProducer() }
    LaunchedEffect(Unit) {
        modelProducer.runTransaction {
            columnSeries {
                series(data.values)
            }
            extras { it[labelListKey] = data.keys.toList() }
        }
    }
    ColumnChart(modelProducer, dataLabelFormatter, modifier)
}
