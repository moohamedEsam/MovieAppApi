package com.example.movieappapi.presentation.screen.signUp

import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.runtime.Composable
import androidx.compose.ui.viewinterop.AndroidView
import com.example.movieappapi.domain.utils.Constants

@Composable
fun SignUpScreen() {
    AndroidView(factory = {
        WebView(it).apply {
            webViewClient = WebViewClient()
            loadUrl(Constants.SIGN_UP_URL)
        }
    })
}