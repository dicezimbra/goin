package br.com.goin.feature.notifications.retrofitService

import br.com.goin.base.utils.Constants
import java.util.ArrayList
import java.util.concurrent.TimeUnit
import br.com.goin.component.rest.api.RetrofitService
import br.com.goin.component.rest.api.interceptors.UserTokenInterceptor
import okhttp3.Interceptor
import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.Protocol
import okhttp3.Response
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor


class TestRetrofitService<T>(clss: Class<T>, baseUrl: String, interceptor: OfflineMockInterceptor) : RetrofitService<T>(clss, baseUrl) {
    init {

        val interceptor2 = UserTokenInterceptor()
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.HEADERS

        val logging2 = HttpLoggingInterceptor()

        logging2.level = HttpLoggingInterceptor.Level.BODY

        authOkHttpClient = OkHttpClient.Builder()
                .readTimeout(Constants.TIMEOUT.toLong(), TimeUnit.SECONDS)
                .connectTimeout(Constants.TIMEOUT.toLong(), TimeUnit.SECONDS)
                .writeTimeout(Constants.TIMEOUT.toLong(), TimeUnit.SECONDS)
                .addInterceptor(logging)
                .addInterceptor(logging2)
                .addInterceptor(interceptor)
                .addInterceptor(interceptor2)
                .build()

        retrofit = retrofit.newBuilder().client(authOkHttpClient).build()
        apiService = retrofit.create(clss)
    }

    class OfflineMockInterceptor : Interceptor {

        private val MEDIA_JSON = MediaType.parse("application/json")

        override fun intercept(chain: Interceptor.Chain): Response {
            val json = mockedResponses.removeAt(0)
            val httpCode = mockedResponsesHttpCodes.removeAt(0)

            return Response.Builder()
                    .body(ResponseBody.create(MEDIA_JSON, json))
                    .code(httpCode)
                    .protocol(Protocol.HTTP_2)
                    .request(chain.request())
                    .message("")
                    .build()
        }
    }

    companion object {
        var mockedResponses: MutableList<String> = ArrayList()
        var mockedResponsesHttpCodes: MutableList<Int> = ArrayList()
    }
}
