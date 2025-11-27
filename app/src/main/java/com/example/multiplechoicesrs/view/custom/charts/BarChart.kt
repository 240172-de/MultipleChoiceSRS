package com.example.multiplechoicesrs.view.custom.charts

import android.text.TextUtils
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import com.example.multiplechoicesrs.model.BarChartData
import com.example.multiplechoicesrs.model.BarChartDataSettings
import com.patrykandpatrick.vico.compose.cartesian.CartesianChartHost
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberBottom
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberStart
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberColumnCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.rememberCartesianChart
import com.patrykandpatrick.vico.compose.common.fill
import com.patrykandpatrick.vico.core.cartesian.axis.BaseAxis
import com.patrykandpatrick.vico.core.cartesian.axis.HorizontalAxis
import com.patrykandpatrick.vico.core.cartesian.axis.VerticalAxis
import com.patrykandpatrick.vico.core.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.core.cartesian.data.CartesianValueFormatter
import com.patrykandpatrick.vico.core.cartesian.data.ColumnCartesianLayerModel
import com.patrykandpatrick.vico.core.cartesian.data.columnSeries
import com.patrykandpatrick.vico.core.cartesian.layer.ColumnCartesianLayer
import com.patrykandpatrick.vico.core.common.component.LineComponent
import com.patrykandpatrick.vico.core.common.component.TextComponent
import com.patrykandpatrick.vico.core.common.data.ExtraStore

private val labelListKey = ExtraStore.Key<List<String>>()
private val colorListKey = ExtraStore.Key<List<List<Color>>>()

@Composable
fun BarChart(
    data: BarChartData
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (data.title.isNotEmpty()) {
            Text(
                data.title,
                fontWeight = FontWeight.Bold
            )
        }

        ColumnChart(data)
    }
}

//TODO: RangeProvider wenn mit Prozent
//https://github.com/patrykandpatrick/vico/blob/bb10a34/sample/src/main/java/com/patrykandpatrick/vico/sample/showcase/charts/TemperatureAnomalies.kt#L58-L67

@Composable
private fun ColumnChart(
    data: BarChartData,
    modifier: Modifier = Modifier
) {
    val modelProducer = remember { CartesianChartModelProducer() }
    LaunchedEffect(Unit) {
        modelProducer.runTransaction {
            columnSeries {
                data.seriesList.forEach {
                    series(it.entries.map { entry ->
                        entry.value
                    })
                }
            }
            extras { it[labelListKey] = data.labelList }
            extras { it[colorListKey] =
                data.seriesList.map { series ->
                    series.entries.map { entry ->
                        entry.color }
                }
            }
        }
    }

    ColumnChart(
        modelProducer = modelProducer,
        settings = data.settings,
        modifier = modifier
    )
}

@Composable
private fun ColumnChart(
    modelProducer: CartesianChartModelProducer,
    settings: BarChartDataSettings,
    modifier: Modifier = Modifier,
) {
    CartesianChartHost(
        chart =
            rememberCartesianChart(
                rememberColumnCartesianLayer(
                    dataLabelValueFormatter = settings.labelFormat.labelFormatter,
                    dataLabel = TextComponent(),
                    columnProvider = object : ColumnCartesianLayer.ColumnProvider {
                        override fun getColumn(
                            entry: ColumnCartesianLayerModel.Entry,
                            seriesIndex: Int,
                            extraStore: ExtraStore
                        ): LineComponent {
                            val color = extraStore[colorListKey][seriesIndex][entry.x.toInt()]

                            return LineComponent(
                                fill = fill(color),
                                thicknessDp = 5f
                            )
                        }

                        override fun getWidestSeriesColumn(
                            seriesIndex: Int,
                            extraStore: ExtraStore
                        ): LineComponent {
                            val color = extraStore[colorListKey][seriesIndex].first()

                            return LineComponent(
                                fill = fill(color),
                                thicknessDp = 10f
                            )
                        }
                    }
                ),
                startAxis = VerticalAxis.rememberStart(
                    valueFormatter = settings.labelFormat.labelFormatter
                ),
                bottomAxis = HorizontalAxis.rememberBottom(
                    size = BaseAxis.Size.Auto(minDp = if (settings.rotateXAxisLabel) 120f else 10f),
                    labelRotationDegrees = if (settings.rotateXAxisLabel) -65f else 0f,
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
