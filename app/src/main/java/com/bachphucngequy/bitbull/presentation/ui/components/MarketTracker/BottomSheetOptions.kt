package com.bachphucngequy.bitbull.presentation.ui.components.MarketTracker

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AreaChart
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.CandlestickChart
import androidx.compose.material.icons.filled.InsertChart
import androidx.compose.material.icons.filled.MultilineChart
import androidx.compose.material.icons.filled.ShowChart
import androidx.compose.material.icons.filled.TableChart
import androidx.compose.material.icons.filled.WaterfallChart
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

enum class BottomSheetContent {
    TimeSpanning,
    LineType,
    ChartToolbox
}

enum class TimeSpan {
    OneMinute,
    FiveMinutes,
    FifteenMinutes,
    ThirtyMinutes,
    OneHour,
    FourHours,
    OneDay,
    OneWeek,
    OneMonth
}

enum class LineType {
    Bar,
    Candle,
    Line,
    Area,
    Renko,
    Kagi,
    PointAndFigure,
    LineBreak,
    HeikinAshi,
}

enum class ChartToolbox {
    Indicators,
    DrawingTools,
    Patterns,
    Strategies
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PartialBottomSheet(
    modifier: Modifier = Modifier,
    onTimeSpanClick: (String) -> Unit,
    onLineTypeClick: (String) -> Unit,
) {
    var showBottomSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = false,
    )
    val sheetContent = remember { mutableStateOf(BottomSheetContent.TimeSpanning) }

    HorizontalOptionsList(
        onTimeSpanClick = {
        showBottomSheet = true
        sheetContent.value = BottomSheetContent.TimeSpanning },
        onLineTypeClick = {
            showBottomSheet = true
            sheetContent.value = BottomSheetContent.LineType
        },
        onChartToolBoxClick = {
            showBottomSheet = true
            sheetContent.value = BottomSheetContent.ChartToolbox
        }, modifier = modifier)

    if (showBottomSheet) {
        ModalBottomSheet(
            modifier = Modifier.fillMaxHeight(),
            sheetState = sheetState,
            onDismissRequest = { showBottomSheet = false }
        ) {
            BottomSheetContent(
                sheetContent.value,
                onLineTypeClick = onLineTypeClick,
                onTimeSpanClick = onTimeSpanClick
            )
        }
    }
}

@Composable
fun BottomSheetContent(
    content: BottomSheetContent,
    onLineTypeClick: (String) -> Unit,
    onTimeSpanClick: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 15.dp, end = 15.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        when (content) {
            BottomSheetContent.TimeSpanning -> {
                Text("Time Span",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                ) {
                    FilledTonalButton(onClick = { onTimeSpanClick("1") },
                        shape = RoundedCornerShape(7.dp),
                        modifier = Modifier.weight(1f)) { Text("1m") }
                    FilledTonalButton(onClick = { onTimeSpanClick("3") },
                        shape = RoundedCornerShape(7.dp),
                        modifier = Modifier.weight(1f)) { Text("3m") }
                    FilledTonalButton(onClick = { onTimeSpanClick("5") },
                        shape = RoundedCornerShape(7.dp),
                        modifier = Modifier.weight(1f)) { Text("5m") }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                ) {
                    FilledTonalButton(onClick = { onTimeSpanClick("15") },
                        shape = RoundedCornerShape(7.dp),
                        modifier = Modifier.weight(1f)) { Text("15m") }
                    FilledTonalButton(onClick = { onTimeSpanClick("30") },
                        shape = RoundedCornerShape(7.dp),
                        modifier = Modifier.weight(1f)) { Text("30m") }
                    FilledTonalButton(onClick = { onTimeSpanClick("45") },
                        shape = RoundedCornerShape(7.dp),
                        modifier = Modifier.weight(1f)) { Text("45m") }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                ) {
                    FilledTonalButton(onClick = { onTimeSpanClick("60") },
                        shape = RoundedCornerShape(7.dp),
                        modifier = Modifier.weight(1f)) { Text("1h") }
                    FilledTonalButton(onClick = { onTimeSpanClick("120") },
                        shape = RoundedCornerShape(7.dp),
                        modifier = Modifier.weight(1f)) { Text("2h") }
                    FilledTonalButton(onClick = { onTimeSpanClick("180") },
                        shape = RoundedCornerShape(7.dp),
                        modifier = Modifier.weight(1f)) { Text("3h") }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                ) {
                    FilledTonalButton(onClick = { onTimeSpanClick("D") },
                        shape = RoundedCornerShape(7.dp),
                        modifier = Modifier.weight(1f)) { Text("1D") }
                    FilledTonalButton(onClick = { onTimeSpanClick("W") },
                        shape = RoundedCornerShape(7.dp),
                        modifier = Modifier.weight(1f)) { Text("1W") }
                    FilledTonalButton(onClick = { onTimeSpanClick("M") },
                        shape = RoundedCornerShape(7.dp),
                        modifier = Modifier.weight(1f)) { Text("1M") }
                }
            }
            BottomSheetContent.LineType -> {
                Text("Line Type",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                ) {
                    LineButton(lineType = LineType.Bar,
                        onClick = { onLineTypeClick("0") },
                        modifier = Modifier.weight(1f).height(80.dp))
                    LineButton(lineType = LineType.Candle,
                        onClick = { onLineTypeClick("1") },
                        modifier = Modifier.weight(1f).height(80.dp))
                    LineButton(lineType = LineType.Line,
                        onClick = { onLineTypeClick("2") },
                        modifier = Modifier.weight(1f).height(80.dp))
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                ) {
                    LineButton(lineType = LineType.Area,
                        onClick = { onLineTypeClick("3") },
                        modifier = Modifier.weight(1f).height(80.dp))
                    LineButton(lineType = LineType.Renko,
                        onClick = { onLineTypeClick("4") },
                        modifier = Modifier.weight(1f).height(80.dp))
                    LineButton(lineType = LineType.Kagi, onClick = { onLineTypeClick("5") },
                        modifier = Modifier.weight(1f).height(80.dp))
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                ) {
                    LineButton(lineType = LineType.PointAndFigure,
                        onClick = { onLineTypeClick("6") },
                        modifier = Modifier.weight(1f).height(80.dp))
                    LineButton(lineType = LineType.LineBreak,
                        onClick = { onLineTypeClick("7") },
                        modifier = Modifier.weight(1f).height(80.dp))
                    LineButton(lineType = LineType.HeikinAshi, onClick = { onLineTypeClick("8") },
                        modifier = Modifier.weight(1f).height(80.dp))
                }
            }
            BottomSheetContent.ChartToolbox -> {
                Text("Chart Toolbox:",
                        style = MaterialTheme.typography.titleLarge)
                Spacer(modifier = Modifier.height(8.dp))
                // Add toolbox buttons here...
            }
        }
    }
}

@Composable
fun LineButton(lineType: LineType, onClick: () -> Unit, modifier: Modifier) {
    FilledTonalIconButton(onClick = onClick,
        shape = RoundedCornerShape(7.dp),
        modifier = modifier) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = when (lineType) {
                    LineType.Line -> Icons.Filled.ShowChart
                    LineType.Area -> Icons.Filled.AreaChart
                    LineType.Renko -> Icons.Filled.InsertChart
                    LineType.Candle -> Icons.Filled.CandlestickChart
                    LineType.Bar -> Icons.Filled.BarChart
                    LineType.Kagi -> Icons.Filled.WaterfallChart
                    LineType.PointAndFigure -> Icons.Filled.TableChart
                    LineType.LineBreak -> Icons.Filled.MultilineChart
                    LineType.HeikinAshi -> Icons.Filled.CandlestickChart
                },
                contentDescription = when(lineType) {
                    LineType.Line -> "Line"
                    LineType.Area -> "Area"
                    LineType.Renko -> "Baseline"
                    LineType.Candle -> "Candle"
                    LineType.Bar -> "Bar"
                    LineType.Kagi -> "Kagi"
                    LineType.PointAndFigure -> "Point & Figure"
                    LineType.LineBreak -> "Line Break"
                    LineType.HeikinAshi -> "Heikin Ashi"
                },
            )
            Text(when(lineType) {
                LineType.Line -> "Line"
                LineType.Area -> "Area"
                LineType.Renko -> "Renko"
                LineType.Candle -> "Candle"
                LineType.Bar -> "Bar"
                LineType.Kagi -> "Kagi"
                LineType.PointAndFigure -> "Point & Figure"
                LineType.LineBreak -> "Line Break"
                LineType.HeikinAshi -> "Heikin Ashi"
            })
        }
    }
}