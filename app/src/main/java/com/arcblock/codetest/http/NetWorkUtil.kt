package com.arcblock.codetest.http

import com.arcblock.codetest.Config.Companion.BASE_URL
import com.blankj.utilcode.BuildConfig
import com.bumptech.glide.load.model.stream.HttpGlideUrlLoader.TIMEOUT
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.apache.http.conn.ssl.AllowAllHostnameVerifier
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.net.Proxy
import java.net.ProxySelector
import java.net.SocketAddress
import java.net.URI
import java.util.concurrent.TimeUnit

class NetWorkUtil {

    private val TIMEOUT: Long = 30


    private fun HttpClient() {}


    companion object {

        var client = NetWorkUtil()

        fun getInstance(): NetWorkUtil {
            return client
        }
    }

    private fun buildOkHttpClient(): OkHttpClient? {
        val mOkHttpClientBuilder = OkHttpClient.Builder()
        // 添加日志打印
        if (BuildConfig.DEBUG) {
            mOkHttpClientBuilder.addInterceptor(
                HttpLoggingInterceptor()
                    .setLevel(HttpLoggingInterceptor.Level.BODY)
            )
        }
        mOkHttpClientBuilder.connectTimeout(TIMEOUT, TimeUnit.SECONDS)
        mOkHttpClientBuilder.readTimeout(TIMEOUT, TimeUnit.SECONDS)
        mOkHttpClientBuilder.writeTimeout(TIMEOUT, TimeUnit.SECONDS)

        // 忽略证书校验
        mOkHttpClientBuilder.hostnameVerifier(AllowAllHostnameVerifier())

        // 绕过代理
        return if (!BuildConfig.DEBUG) {
            mOkHttpClientBuilder.proxySelector(object : ProxySelector() {
                override fun select(uri: URI): List<Proxy> {
                    return listOf(Proxy.NO_PROXY)
                }

                override fun connectFailed(uri: URI, sa: SocketAddress, ioe: IOException) {}
            }).build()
        } else {
            mOkHttpClientBuilder.build()
        }
    }


    private fun buildRetrofit(): Retrofit {
        val mRetrofitBuilder = Retrofit.Builder()
        mRetrofitBuilder.baseUrl(BASE_URL)
        mRetrofitBuilder.addConverterFactory(GsonConverterFactory.create())
        mRetrofitBuilder.addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        mRetrofitBuilder.client(buildOkHttpClient())
        return mRetrofitBuilder.build()
    }

    fun getService(): Api {
        return buildRetrofit().create(Api::class.java)
    }
}