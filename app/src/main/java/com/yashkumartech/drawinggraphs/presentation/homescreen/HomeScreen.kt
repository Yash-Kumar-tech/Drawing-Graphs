package com.yashkumartech.drawinggraphs.presentation.homescreen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.yashkumartech.drawinggraphs.presentation.graphs.BarGraph
import com.yashkumartech.drawinggraphs.presentation.graphs.CircularChart
import com.yashkumartech.drawinggraphs.presentation.graphs.CircularProgressBarAnimated
import com.yashkumartech.drawinggraphs.presentation.graphs.LineChart

@Preview
@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun HomeScreen() {
//    var dropDownExpanded by remember { mutableStateOf(false) }
//    val items = listOf("Histogram", "Bar Graph", "Pie Chart")
//    val selectedGraphIndex = remember { mutableStateOf(0) }
    val pagerState = rememberPagerState(initialPage = 0)
    var shouldRecompose by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Home Screen")
                }
            )
        }
    ) { innerPadding ->
        val values = listOf(1,5,7,3,2)
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            HorizontalPager(
                state = pagerState,
                pageCount = 4,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
            ) {pageNo ->
                when (pageNo) {
                    0 -> {
                        LineChart(values, shouldRecompose)
                    }
                    1 -> {
                        CircularChart(values, shouldRecompose)
                    }
                    2 -> {
                        BarGraph(values, shouldRecompose)
                    }
                    else -> {
                        CircularProgressBarAnimated(50, shouldRecompose)
                    }
                }
            }
            Button(
                onClick = {
                    shouldRecompose = !shouldRecompose
                }
            ) {
                Text(if(shouldRecompose) "Animate" else "Collapse")
            }
        }
    }
}