package br.com.goin.component.rest.api

import android.content.Context
import br.com.goin.component.rest.api.interceptors.DeviceInfoInterceptor
import br.com.goin.component.rest.api.interceptors.UserTokenInterceptor

import java.io.File
import java.util.concurrent.TimeUnit

import br.com.goin.base.BaseApplication
import okhttp3.Cache
import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

open class RetrofitService<T>(clss: Class<T>, baseUrl: String) {

    var apiService: T
    var authOkHttpClient: OkHttpClient
    var retrofit: Retrofit

    init {

        val logging = HttpLoggingInterceptor()
        val logging2 = HttpLoggingInterceptor()
        if(BuildConfig.DEBUG) {
            logging.level = HttpLoggingInterceptor.Level.HEADERS
            logging2.level = HttpLoggingInterceptor.Level.BODY
        }else{
            logging.level = HttpLoggingInterceptor.Level.NONE
            logging2.level = HttpLoggingInterceptor.Level.NONE
        }

        authOkHttpClient = OkHttpClient.Builder()
                .readTimeout(30, TimeUnit.SECONDS)
                .connectTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .addInterceptor(logging)
                .addInterceptor(logging2)
                .addInterceptor(UserTokenInterceptor())
                .addInterceptor(DeviceInfoInterceptor())
                .addNetworkInterceptor(provideCacheInterceptor())
                .cache(provideCache())
                .build()

        retrofit = Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(authOkHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()

        apiService = retrofit.create(clss)
    }

    private fun provideCache(): Cache? {
        var cache: Cache? = null

        try {
            cache = Cache(File(BaseApplication.instance.context!!.cacheDir, "http-cache"),
                    (50 * 1024 * 1024).toLong()) // 50 MB
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return cache
    }

    private fun provideCacheInterceptor(): Interceptor {
        return Interceptor { chain ->
            val response = chain.proceed(chain.request())

            val cacheControl = if (isConnected()) {
                CacheControl.Builder()
                        .maxAge(2, TimeUnit.MINUTES)
                        .build()
            } else {
                CacheControl.Builder()
                        .maxStale(7, TimeUnit.DAYS)
                        .build()
            }
            val sCacheControl = cacheControl.toString()

            response.newBuilder()
                    .removeHeader(HEADER_PRAGMA)
                    .removeHeader(HEADER_CACHE_CONTROL)
                    .header(HEADER_CACHE_CONTROL, sCacheControl)
                    .build()

        }
    }

    private fun provideOfflineCacheInterceptor(): Interceptor {
        return Interceptor { chain ->
            var request = chain.request()
            if (!isConnected()) {
                val cacheControl = CacheControl.Builder()
                        .maxStale(7, TimeUnit.DAYS)
                        .build()

                request = request.newBuilder()
                        .removeHeader(HEADER_PRAGMA)
                        .removeHeader(HEADER_CACHE_CONTROL)
                        .cacheControl(cacheControl)
                        .build()
            }
            chain.proceed(request)
        }
    }


    private fun isConnected(): Boolean{
        try {
            val e = BaseApplication.instance.context!!.getSystemService(
                    Context.CONNECTIVITY_SERVICE) as android.net.ConnectivityManager
            val activeNetwork = e.activeNetworkInfo
            return activeNetwork != null && activeNetwork.isConnected
        } catch (e: Exception) {

        }

        return false
    }

    companion object {
        val HEADER_PRAGMA = "Pragma"
        val HEADER_CACHE_CONTROL = "Cache-Control"
    }
}
