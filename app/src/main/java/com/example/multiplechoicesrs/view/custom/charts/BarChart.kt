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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.multiplechoicesrs.ui.theme.GreenCorrectAnswer
import com.patrykandpatrick.vico.compose.cartesian.CartesianChartHost
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberBottom
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberStart
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberColumnCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.rememberCartesianChart
import com.patrykandpatrick.vico.compose.common.component.rememberLineComponent
import com.patrykandpatrick.vico.compose.common.component.rememberTextComponent
import com.patrykandpatrick.vico.compose.common.fill
import com.patrykandpatrick.vico.compose.common.vicoTheme
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

private val LegendLabelKey = ExtraStore.Key<Set<String>>()

val labelListKey = ExtraStore.Key<List<String>>()


//TODO:
//Also Umsetzen per Legende
//Was wird gebraucht?
//Eine Farbe je column
//Wo kann man die dann eigentlich setzen?
//Momentan ist ja nur Legend color gesetzt
//TODO: Recherchieren wo Farbe setzbar
//Dann Label auf null
//Datenobjekt
//Map<String, <Float, Color>>
//Man braucht ja je Eintrag alle 3
//Label
//Wert
//ColumnColor
//Zum Farbe setzen mussen separate Series genutzt werden
//Klappt das dann mit Legend?
//Im vicoTheme sind nur 3 unterschiedliche Farben fur die Columns
//Danach wiederholt es sich
//Also vllt. doch lieber eigene Farben definieren?
//Wie ware es mit gedrehten labels?
//TODO: RangeProvider wenn mit Prozent
//https://github.com/patrykandpatrick/vico/blob/bb10a34/sample/src/main/java/com/patrykandpatrick/vico/sample/showcase/charts/TemperatureAnomalies.kt#L58-L67

@Composable
private fun ColumnChart(
    modelProducer: CartesianChartModelProducer,
    dataLabelFormatter: CartesianValueFormatter,
    modifier: Modifier = Modifier,
) {
    val columnColors = listOf(Color(0xff6438a7), Color(0xff3490de), Color(0xff73e8dc))
//    val columnColors = vicoTheme.columnCartesianLayerColors

    val legendItemLabelComponent = rememberTextComponent(vicoTheme.textColor)

    CartesianChartHost(
        chart =
            rememberCartesianChart(
                rememberColumnCartesianLayer(
                    dataLabelValueFormatter = dataLabelFormatter,
                    columnProvider = ColumnCartesianLayer.ColumnProvider.series(
//                        columnColors.map {
//                            rememberLineComponent(
//                                fill = fill(it),
//                            )
//                        }
                        listOf(
                            rememberLineComponent(
                                fill(GreenCorrectAnswer),
                                thickness = 5.dp
                            )
                        )
                    ),
//                    columnProvider = ColumnCartesianLayer.ColumnProvider.series(
//                        columns = {
//                            rememberLineComponent(
//                                fill(GreenCorrectAnswer)
//                            )
//                            LineComponent(
//                                fill(GreenCorrectAnswer)
//                            )
//                        }
//
//                    ),
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
//                    label = null
                ),
//                legend = rememberHorizontalLegend(
//                    items = { extraStore ->
//                        extraStore[LegendLabelKey].forEachIndexed { index, label ->
//                            add(
//                                LegendItem(
////                                    shapeComponent(fill(columnColors[index]), CorneredShape.Pill),
//                                    shapeComponent(
//                                        fill(columnColors[index % columnColors.size]),
//                                        CorneredShape.Pill
//                                    ),
//                                    legendItemLabelComponent,
//                                    label,
//                                )
//                            )
//                        }
//                    },
//                    padding = insets(top = 16.dp),
//                ),
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
            // Learn more: https://patrykandpatrick.com/eji9zq.
            columnSeries {
//                series(5, 6, 5)
//                series(2, 11, 8)
                series(data.values)
//                series(data.values)
//                series(data.values)
//                series(data.values)
//                series(data.values)
//                series(data.values)
            }
            extras { it[LegendLabelKey] = setOf("基本理論", "アルゴリズムとプログラミング", "コンピューター構成要素")}
            extras { it[labelListKey] = data.keys.toList() }
        }
    }
    ColumnChart(modelProducer, dataLabelFormatter, modifier)
}
