package com.example.multiplechoicesrs.view.dialog

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.multiplechoicesrs.ui.theme.GreenCorrectAnswer
import com.example.multiplechoicesrs.ui.theme.RedIncorrectAnswer

@Composable
fun ResultDialog(
    numCorrect: Int,
    numIncorrect: Int,
    onDismissRequest: () -> Unit
) {
    Dialog(onDismissRequest) {
        Card(
            modifier = Modifier.padding(16.dp),
            shape = RoundedCornerShape(16.dp),
        ) {
            Column(
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .padding(top = 20.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                SegmentedRing(numCorrect, numIncorrect)

                TextButton(
                    onClick = { onDismissRequest() },
                    modifier = Modifier.padding(8.dp),
                ) {
                    Text("OK")
                }
            }
        }
    }
}

@Composable
fun SegmentedRing(
    numCorrect: Int,
    numIncorrect: Int,
    modifier: Modifier = Modifier
) {
    val numTotal = numCorrect + numIncorrect

    val percentageCorrect = numCorrect.toFloat() / numTotal
    val sweepAngleCorrect = 360f * percentageCorrect
    val sweepAngleIncorrect = 360f - sweepAngleCorrect

    val toDraw: List<Pair<Color, Float>> = listOf(
        Pair(GreenCorrectAnswer, sweepAngleCorrect),
        Pair(RedIncorrectAnswer, sweepAngleIncorrect)
    )

    val textMeasurer = rememberTextMeasurer()
    val textToDraw = "$numCorrect / $numTotal"

    val style = TextStyle(
        fontSize = 28.sp,
        color = MaterialTheme.colorScheme.onBackground
    )

    val textLayoutResult = remember(textToDraw) {
        textMeasurer.measure(textToDraw, style)
    }

    Canvas(modifier = modifier.size(200.dp)) {
        var startAngle = -90f
        val strokeWidth = 50f

        toDraw.forEach { pair ->
            drawArc(
                color = pair.first,
                startAngle = startAngle,
                sweepAngle = pair.second,
                useCenter = false,
                style = Stroke(width = strokeWidth, cap = StrokeCap.Butt)
            )
            startAngle += pair.second
        }

        drawText(
            textMeasurer = textMeasurer,
            text = textToDraw,
            style = style,
            topLeft = Offset(
                x = center.x - textLayoutResult.size.width / 2,
                y = center.y - textLayoutResult.size.height / 2,
            )
        )
    }
}