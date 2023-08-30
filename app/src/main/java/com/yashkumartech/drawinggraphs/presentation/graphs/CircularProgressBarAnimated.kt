package com.yashkumartech.drawinggraphs.presentation.graphs

import android.graphics.Paint
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
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.min

@Composable
fun CircularProgressBarAnimated(
    progress: Int = 50,
    shouldRecompose: Boolean
) {
    val progress = min(progress, 100) / 100f
    val graphColor = MaterialTheme.colorScheme.primary
    val inactiveColor = MaterialTheme.colorScheme.outlineVariant
    var animationPlayed by remember {
        mutableStateOf(false)
    }
    val density = LocalDensity.current
    val textPaint = remember(density) {
        Paint().apply {
            color = graphColor.toArgb()
            textAlign = Paint.Align.CENTER
            textSize = density.run { 48.sp.toPx() }
        }
    }
    val curPercent = animateFloatAsState(
        targetValue =
        if(animationPlayed) 1F
        else                0f,
        animationSpec = tween(1000, 0), label = "Graph Size Percentage"
    )

    LaunchedEffect(key1 = true) {
        animationPlayed = true
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Text(
            "Circular Progress Bar",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )
        Canvas(
            modifier = Modifier
                .padding(32.dp)
                .aspectRatio(1.0F)
                .fillMaxSize()
        ) {
            val canvasWidth = this.size.width / 2
            val canvasHeight = this.size.height / 2
            drawCircle(
                inactiveColor,
                radius = canvasHeight,
                style = Stroke(40f)
            )
            drawArc(
                graphColor,
                -90f,
                progress * 360f * curPercent.value,
                useCenter = false,
                style = Stroke(40f, cap = StrokeCap.Round)
            )
            drawContext.canvas.nativeCanvas.apply {
                drawText(
                    String.format("%.1f", (progress * curPercent.value * 100f)) + "%",
                    canvasWidth,
                    canvasHeight + 25f,
                    textPaint
                )
            }
        }
    }
}