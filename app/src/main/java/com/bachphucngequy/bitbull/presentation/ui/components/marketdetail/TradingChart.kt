package com.example.pitbulltradingapp.ui.components.marketdetail

import android.annotation.SuppressLint
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun TradingChart(
    symbol: String,
    selectedTimeFrame: String,
    selectedChartType: String
) {
    var webView by remember { mutableStateOf<WebView?>(null) }

    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_DESTROY) {
                webView?.destroy()
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    // WebView for TradingView chart
    AndroidView(
        factory = { context ->
            WebView(context).apply {
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
                webViewClient = WebViewClient()
                settings.javaScriptEnabled = true
                settings.domStorageEnabled = true
                settings.loadWithOverviewMode = true
//                settings.useWideViewPort = true
                settings.builtInZoomControls = true
                settings.displayZoomControls = false
                settings.setSupportZoom(true)

                // Optionally, set initial scale for the WebView
                setInitialScale(200)
            }
        },
        update = { webView ->
            val html = """
                    <html>
                        <body style="margin:0;padding:0;">
                            <div class="tradingview-widget-container" style="height:100vh;">
                                <div id="tradingview_chart" style="height:100%;"></div>
                                <script type="text/javascript" src="https://s3.tradingview.com/tv.js"></script>
                                <script type="text/javascript">
                                    new TradingView.widget({
                                        "width": "100%",
                                        "height": "100%",
                                        "symbol": "BINANCE:${symbol}",
                                        "interval": "$selectedTimeFrame",
                                        "timezone": "Etc/UTC",
                                        "theme": "light",
                                        "style": "$selectedChartType",
                                        "locale": "en",
                                        "hide_top_toolbar": true,
                                        "toolbar_bg": "#f1f3f6",
                                        "enable_publishing": false,
                                        "allow_symbol_change": true,
                                        "container_id": "tradingview_chart"
                                
                                    });
                                </script>
                            </div>
                        </body>
                    </html>
                """.trimIndent()
            webView.loadDataWithBaseURL(null, html, "text/html", "UTF-8", null)
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(400.dp)
    )
}