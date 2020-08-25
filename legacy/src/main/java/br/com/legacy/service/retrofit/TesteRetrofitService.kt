package br.com.legacy.service.retrofit

import android.content.Context
import br.com.goin.base.BaseApplication
import br.com.goin.component.rest.api.interceptors.UserTokenInterceptor

import java.io.File
import java.util.concurrent.TimeUnit

import br.com.legacy.utils.Constants

import okhttp3.Cache
import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory


/**
 * Created by appsimples on 8/9/17.
 */

class TesteRetrofitService<T>(clss: Class<T>, baseUrl: String) {

    val apiService: T

    private val retrofit: Retrofit

    private val isConnected: Boolean
        get() {
            try {
                val e = BaseApplication.instance.context!!.getSystemService(
                        Context.CONNECTIVITY_SERVICE) as android.net.ConnectivityManager
                val activeNetwork = e.activeNetworkInfo
                return activeNetwork != null && activeNetwork.isConnected
            } catch (e: Exception) {

            }

            return false
        }

    init {

        val interceptor = UserTokenInterceptor()
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.HEADERS

        val logging2 = HttpLoggingInterceptor()
        logging2.level = HttpLoggingInterceptor.Level.BODY

        retrofit = Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(createClient(logging, logging2, interceptor))
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()

        apiService = retrofit.create(clss)
    }

    fun createClient(logging: HttpLoggingInterceptor,
                             logging2: HttpLoggingInterceptor,
                             interceptor: UserTokenInterceptor): OkHttpClient = OkHttpClient.Builder()
            .readTimeout(Constants.TIMEOUT.toLong(), TimeUnit.SECONDS)
            .connectTimeout(Constants.TIMEOUT.toLong(), TimeUnit.SECONDS)
            .writeTimeout(Constants.TIMEOUT.toLong(), TimeUnit.SECONDS)
            .addInterceptor(logging)
            .addInterceptor(logging2)
            .addInterceptor(interceptor)
            .addInterceptor(provideOfflineCacheInterceptor())
            .addNetworkInterceptor(provideCacheInterceptor())
            .cache(provideCache())
            .build()

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
        return Interceptor{ chain ->
            val response = chain.proceed(chain.request())
            val cacheControl: CacheControl
            if (isConnected) {
                cacheControl = CacheControl.Builder()
                        .maxAge(2, TimeUnit.MINUTES)
                        .build()
            } else {
                cacheControl = CacheControl.Builder()
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
        return Interceptor{ chain ->
            var request = chain.request()

            if (!isConnected) {
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

    companion object {
        val HEADER_PRAGMA = "Pragma"
        val HEADER_CACHE_CONTROL = "Cache-Control"
    }
}
