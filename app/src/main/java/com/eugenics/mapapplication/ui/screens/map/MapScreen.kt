package com.eugenics.mapapplication.ui.screens.map

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView

@SuppressLint("SetJavaScriptEnabled")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapScreen() {
    Scaffold { paddingValues ->
        val mUrl = "file:///android_asset/map.html"

        // Adding a WebView inside AndroidView
        // with layout as full screen
        AndroidView(
            modifier = Modifier.padding(paddingValues = paddingValues),
            factory = {
                WebView(it).apply {
                    isClickable = true
                    isFocusable = true
                    settings.setGeolocationEnabled(true)
                    settings.javaScriptEnabled = true

                    layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )
                    val webClient = object : WebViewClient() {
                        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                            loadUrl(
                                "javascript:var map = new ol.Map({\n" +
                                        "        target: 'map',\n" +
                                        "        layers: [\n" +
                                        "          new ol.layer.Tile({\n" +
                                        "            source: new ol.source.OSM()\n" +
                                        "          })\n" +
                                        "        ],\n" +
                                        "        view: new ol.View({\n" +
                                        "          center: ol.proj.fromLonLat([37.41, 8.82]),\n" +
                                        "          zoom: 4\n" +
                                        "        })\n" +
                                        "      });"
                            )
                        }
                    }
                    webViewClient = webClient
                    webChromeClient = WebChromeClient()
                    loadUrl(mUrl)
                }
            }, update = {
                it.loadUrl(mUrl)
            })
    }
}