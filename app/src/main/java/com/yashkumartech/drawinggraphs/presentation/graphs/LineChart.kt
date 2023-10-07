package com.yashkumartech.drawinggraphs.presentation.graphs

import android.graphics.Paint
import android.util.Log
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PointMode
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.asAndroidPath
import androidx.compose.ui.graphics.asComposePath
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.lang.Float.min

@Composable
fun LineChart(
    xValues: List<Int> = emptyList(),
    shouldRecompose: Boolean
) {
    val maxValue = xValues.max()
    val graphColor = MaterialTheme.colorScheme.primary
    val pointColor = MaterialTheme.colorScheme.error
    val axisColor = MaterialTheme.colorScheme.onBackground

    val density = LocalDensity.current
    val textPaint = remember(density) {
        Paint().apply {
            color = android.graphics.Color.WHITE
            textAlign = Paint.Align.CENTER
            textSize = density.run { 12.sp.toPx() }
        }
    }

    var animationPlayed by remember {
        mutableStateOf(false)
    }
    val curPercent = animateFloatAsState(
        targetValue =
        if(animationPlayed) 1F
        else                0f,
        animationSpec = tween(1000, 0), label = "Graph Size Percentage"
    )



    LaunchedEffect(key1 = shouldRecompose) {
        animationPlayed = !shouldRecompose
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Text(
            "Line Chart",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )
        Canvas(
            modifier = Modifier
                .padding(16.dp)
                .aspectRatio(1.0f)
                .fillMaxSize()
        ) {
            val canvasHeight = this.size.height
            val canvasWidth = this.size.width
            val gap = 100f
            val barWidth = (canvasWidth - 5f) / xValues.size
            val barMaxHeight = canvasHeight - 20f
            val offsets: MutableList<Offset> = mutableListOf()
            (xValues.indices).forEach { i ->
                drawContext.canvas.nativeCanvas.apply {
                    drawText(
                        i.toString(),
                        gap + i * barWidth,
                        canvasHeight,
                        textPaint
                    )
                }
                drawContext.canvas.nativeCanvas.apply {
                    drawText(
                        xValues[i].toString(),
                        gap - 32F,
                        (maxValue - xValues[i]) * (barMaxHeight * 1f / maxValue) + 16f,
                        textPaint
                    )
                }
            }
            var lastX = 0f
            val path = Path().apply {
                for(i in xValues.indices) {
                    val y = xValues[i]
                    val nextY = xValues.getOrNull(i + 1) ?: xValues.last()

                    val x1 = gap + i * barWidth
                    val y1 = min(canvasHeight - (y * 1f / maxValue) * canvasHeight * curPercent.value, canvasHeight - 32f)

                    val x2 = gap + (i + 1) * barWidth
                    val y2 = min(canvasHeight - (nextY * 1f / maxValue) * canvasHeight * curPercent.value, canvasHeight - 32f)

                    if(i == 0) {
                        moveTo(x1, y1)
                    }
                    lastX = (x1 + x2) / 2f
                    quadraticBezierTo(
                        x1,
                        y1,
                        x2,
                        y2
                    )
                    offsets.add(Offset(x1, y1))
                }
            }

            val fillPath = android.graphics.Path(path.asAndroidPath())
                .asComposePath()
                .apply {
                    lineTo(gap + (xValues.size - 1) * barWidth, canvasHeight - gap)
                    lineTo(gap, canvasHeight - gap)
                    close()
                }


            drawLine(
                axisColor,
                Offset(gap, 0f),
                Offset(gap, this.size.height - 32f),
                strokeWidth = 4f
            )
            drawLine(
                axisColor,
                Offset(gap, this.size.height - 32f),
                Offset(this.size.width, this.size.height - 32f),
                strokeWidth = 4f
            )
            drawPath(
                fillPath,
                brush = Brush.verticalGradient(
                    colors = listOf(
                        graphColor.copy(alpha = 0.5f),
                        Color.Transparent
                    ),
                    endY = canvasHeight - gap
                )
            )
            drawPath(
                path,
                color = graphColor,
                style = Stroke(
                    width = 3.dp.toPx(),
                    cap = StrokeCap.Round
                )
            )
            drawPoints(
                offsets,
                PointMode.Points,
                pointColor,
                strokeWidth = 16f
            )
        }
    }
}