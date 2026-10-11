package com.africaniches.business

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import android.graphics.Color
import android.view.Gravity
import android.widget.LinearLayout

class MainActivity : Activity() {
    private lateinit var webView: WebView
    private lateinit var progress: ProgressBar
    private lateinit var status: TextView
    private val homeUrl = "https://business.africaniches.com/"
    private var lastUrl = homeUrl

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = Color.rgb(9, 45, 25)
        window.navigationBarColor = Color.rgb(9, 45, 25)

        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setBackgroundColor(Color.WHITE)
        }
        val header = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER_VERTICAL
            setPadding(dp(16), dp(10), dp(16), dp(10))
            setBackgroundColor(Color.rgb(9, 45, 25))
        }
        val brand = TextView(this).apply {
            text = "AFRICA NICHES  •  BUSINESS"
            textSize = 16f
            setTextColor(Color.rgb(255, 213, 79))
            typeface = android.graphics.Typeface.DEFAULT_BOLD
        }
        header.addView(brand, LinearLayout.LayoutParams(0, dp(42), 1f))
        val refresh = TextView(this).apply {
            text = "↻  Actualiser"
            textSize = 14f
            setTextColor(Color.WHITE)
            gravity = Gravity.CENTER
            setPadding(dp(8), 0, dp(4), 0)
            setOnClickListener { webView.reload() }
        }
        header.addView(refresh)
        progress = ProgressBar(this, null, android.R.attr.progressBarStyleHorizontal).apply {
            max = 100
            progress = 0
            visibility = View.GONE
        }
        status = TextView(this).apply {
            text = "Connexion au back-office sécurisé…"
            textSize = 13f
            setTextColor(Color.DKGRAY)
            setPadding(dp(12), dp(4), dp(12), dp(4))
            visibility = View.GONE
        }
        webView = WebView(this)
        root.addView(header)
        root.addView(progress, LinearLayout.LayoutParams(-1, dp(3)))
        root.addView(status)
        root.addView(webView, LinearLayout.LayoutParams(-1, 0, 1f))
        setContentView(root)

        webView.settings.apply {
            javaScriptEnabled = true // nécessaire aux interfaces web modernes
            domStorageEnabled = true
            databaseEnabled = true
            loadsImagesAutomatically = true
            javaScriptCanOpenWindowsAutomatically = false
            setSupportMultipleWindows(false)
            mixedContentMode = WebSettings.MIXED_CONTENT_NEVER_ALLOW
            allowFileAccess = false
            allowContentAccess = false
            builtInZoomControls = false
            displayZoomControls = false
            userAgentString = "$userAgentString AfricaNichesBusinessAndroid/1.0"
        }
        webView.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                progress.progress = newProgress
                progress.visibility = if (newProgress in 1..99) View.VISIBLE else View.GONE
                if (newProgress >= 100) status.visibility = View.GONE
            }
        }
        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
                val uri = request.url
                val scheme = uri.scheme ?: return true
                if (scheme == "https" && (uri.host == "business.africaniches.com" || uri.host == "africaniches.com" || uri.host == "www.africaniches.com")) {
                    lastUrl = uri.toString()
                    return false
                }
                if (scheme == "http" || scheme == "https") {
                    openExternal(uri)
                    return true
                }
                if (scheme == "tel" || scheme == "mailto") {
                    openExternal(uri)
                    return true
                }
                Toast.makeText(this@MainActivity, "Lien non pris en charge par l’application.", Toast.LENGTH_SHORT).show()
                return true
            }

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                status.text = "Chargement du back-office…"
                status.visibility = View.VISIBLE
                if (url != null) lastUrl = url
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                status.visibility = View.GONE
                if (url != null) lastUrl = url
            }

            override fun onReceivedError(view: WebView?, request: WebResourceRequest?, error: WebResourceError?) {
                if (request?.isForMainFrame == true) {
                    status.text = "Impossible de joindre le back-office. Vérifiez la connexion Internet puis touchez Actualiser."
                    status.visibility = View.VISIBLE
                }
            }
        }
        if (savedInstanceState != null) webView.restoreState(savedInstanceState) else webView.loadUrl(homeUrl)
    }

    private fun openExternal(uri: Uri) {
        try {
            startActivity(Intent(Intent.ACTION_VIEW, uri))
        } catch (_: ActivityNotFoundException) {
            Toast.makeText(this, "Aucune application ne peut ouvrir ce lien.", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        webView.saveState(outState)
        super.onSaveInstanceState(outState)
    }

    @Deprecated("Deprecated in Android API; retained for broad device compatibility")
    override fun onBackPressed() {
        if (this::webView.isInitialized && webView.canGoBack()) webView.goBack() else super.onBackPressed()
    }

    override fun onDestroy() {
        if (this::webView.isInitialized) {
            webView.stopLoading()
            webView.destroy()
        }
        super.onDestroy()
    }

    private fun dp(value: Int): Int = (value * resources.displayMetrics.density).toInt()
}
