package com.yashkumartech.drawinggraphs.presentation.graphs

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import android.graphics.Paint
import android.util.Log
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp



@Composable
fun BarGraph(
    xValues: List<Int> = emptyList(),
    shouldRecompose: Boolean,
) {
    Log.d("HERE", xValues.toString())
    val axisColor = MaterialTheme.colorScheme.onBackground
    val barColor = MaterialTheme.colorScheme.primary
    val density = LocalDensity.current
    val maxValue = xValues.max()
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
            "Bar Graph",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )
        Canvas(
            modifier = Modifier
                .padding(8.dp)
                .aspectRatio(1.0F)
                .fillMaxSize()
        ) {
            val canvasHeight = this.size.height
            val canvasWidth = this.size.width
            val gap = 32f
            val barWidth = (canvasWidth - gap) / (xValues.size + 1)
            val barMaxHeight = canvasHeight - 30f
            var stX = gap
            drawLine(
                axisColor,
                Offset(gap, 0f),
                Offset(gap, canvasHeight - 32f),
                strokeWidth = 4f
            )
            drawLine(
                axisColor,
                Offset(gap, canvasHeight - 32f),
                Offset(canvasWidth, canvasHeight - 32f),
                strokeWidth = 4f
            )
            for(index in xValues.indices) {
                drawRect(
                    barColor,
                    Offset(stX, barMaxHeight * (1 - xValues[index] * curPercent.value / maxValue)),
                    Size(barWidth, xValues[index] * barMaxHeight * curPercent.value / maxValue)
                )
                stX += barWidth + gap
                drawContext.canvas.nativeCanvas.apply {
                    drawText(
                        index.toString(),
                        (index) * (barWidth + gap) - barWidth / 2,
                        canvasHeight - 8f,
                        textPaint
                    )
                }
                drawContext.canvas.nativeCanvas.apply {
                    drawText(
                        xValues[index].toString(),
                        15F,
                        (maxValue - xValues[index]) * (barMaxHeight * 1f / maxValue) + 16f,
                        textPaint
                    )
                }
            }
        }
    }
}

