package com.yashkumartech.drawinggraphs.presentation.graphs

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Preview
@Composable
fun CircularChart(
    xValues: List<Int> = emptyList(),
    shouldRecompose: Boolean
) {
    val graphColors = listOf(
        Color(255, 170, 0),
        Color(0, 209, 101),
        Color(0, 175, 249),
    )
    val sum = xValues.sum()
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
            "Circular Graph",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )
        Canvas(
            modifier = Modifier
                .padding(32.dp)
                .aspectRatio(1.0f)
                .fillMaxSize()
        ) {
            var stAngle = 0f
            for(index in xValues.indices) {
                val sweepAngle = xValues[index] * 360f * curPercent.value / sum
                drawArc(
                    graphColors[index % graphColors.size],
                    stAngle,
                    sweepAngle,
                    useCenter = false,
                    style = Stroke(20f, cap = StrokeCap.Round)
                )
                stAngle += sweepAngle
            }
        }
    }
}