package com.yashkumartech.drawinggraphs.presentation.homescreen

data class HomeScreenState(
    var selectedGraph: AvailableGraphs = AvailableGraphs.BAR_GRAPH,
    var xValues: List<Int> = emptyList()
)

enum class AvailableGraphs {
    BAR_GRAPH,
    HISTOGRAM,
    PIE_CHART,
    LINE_CHART,
    SMOOTH_CURVE
}