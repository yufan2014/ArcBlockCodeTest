package com.arcblock.codetest

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.text.TextUtils
import android.webkit.JavascriptInterface
import com.blankj.utilcode.util.ToastUtils
import com.tencent.smtt.export.external.interfaces.SslError
import com.tencent.smtt.export.external.interfaces.SslErrorHandler
import com.tencent.smtt.export.external.interfaces.WebResourceError
import com.tencent.smtt.export.external.interfaces.WebResourceRequest
import com.tencent.smtt.sdk.WebChromeClient
import com.tencent.smtt.sdk.WebSettings
import com.tencent.smtt.sdk.WebView
import com.tencent.smtt.sdk.WebViewClient
import com.tencent.smtt.utils.p
import kotlinx.android.synthetic.main.activity_web.*


/**
 * 网页专用
 */
class WebActivity : BaseActivity() {

    companion object {

        fun startWeb(activity: BaseActivity, url: String) {
            val intent = Intent(activity, WebActivity::class.java)
            intent.putExtra("url", url)
            activity.startActivity(intent)
        }
    }


    override fun getLayout(): Int {
        return R.layout.activity_web
    }


    override fun initView() {
        val url = intent?.getStringExtra("url")
        initWebViewSetting()

        mTvWebTitle.setNavigationOnClickListener {finish()}

        startLoading()
        mWeb.loadUrl(url)
    }


    private fun initWebViewSetting() {
        val settings: WebSettings? = mWeb.settings
        settings?.textZoom = 100

        settings?.useWideViewPort = true
        settings?.loadWithOverviewMode = true
        settings?.javaScriptCanOpenWindowsAutomatically = true

        settings?.javaScriptEnabled = true
        settings?.allowFileAccess = true
        settings?.setAllowFileAccessFromFileURLs(true)
        settings?.setAllowUniversalAccessFromFileURLs(true)

        settings?.setSupportMultipleWindows(true)
        settings?.loadsImagesAutomatically = true

        settings?.domStorageEnabled = true
        settings?.databaseEnabled = true
        settings?.cacheMode = WebSettings.LOAD_NO_CACHE

        setWebViewClient()
    }

    private fun setWebViewClient() {
        mWeb.webChromeClient = object : WebChromeClient() {
            override fun onReceivedTitle(view: WebView?, title: String?) {
                super.onReceivedTitle(view, title)
                runOnUiThread {
                    if (!TextUtils.isEmpty(title)) {
                        mTvWebTitle.title = title
                        return@runOnUiThread
                    }
                }
            }

            override fun onProgressChanged(p0: WebView?, p1: Int) {
                super.onProgressChanged(p0, p1)
                if(p1>=99){
                    endLoading()
                }
            }
        }

        mWeb.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(p0: WebView, p1: String): Boolean {
                p0?.loadUrl(p1)
                return true
            }

            override fun onReceivedError(
                p0: WebView?,
                p1: WebResourceRequest?,
                p2: WebResourceError?
            ) {
                super.onReceivedError(p0, p1, p2)
                endLoading()
            }

            override fun onReceivedSslError(p0: WebView?, p1: SslErrorHandler?, p2: SslError?) {
                super.onReceivedSslError(p0, p1, p2)
                endLoading()
            }
        }

        mWeb.addJavascriptInterface(WebAppInterface(), "abt")
    }


    private inner class WebAppInterface internal constructor() {
        @JavascriptInterface
        fun requestOpenUrl(action: String,url: String) {
            ToastUtils.showShort(url)
        }
    }
}