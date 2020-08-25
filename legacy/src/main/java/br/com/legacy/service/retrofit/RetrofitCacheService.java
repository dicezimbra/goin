package br.com.legacy.service.retrofit;

import java.io.File;
import java.util.concurrent.TimeUnit;

;
import br.com.goin.base.BaseApplication;
import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by appsimples on 8/9/17.
 */

public class RetrofitCacheService<T> {

    private T apiService;
    private String key;
    public T getApiService(String key) {
        this.key = key;
        return apiService;
    }

    private Retrofit retrofit;
    public static final String HEADER_PRAGMA = "Pragma";
    public static final String HEADER_CACHE_CONTROL = "Cache-Control";
    public RetrofitCacheService(Class<T> clss, String baseUrl) {

        OkHttpClient authOkHttpClient = new OkHttpClient.Builder()
                .addInterceptor(provideForcedOfflineCacheInterceptor())
                .cache(provideCache())
                .build();

        retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(authOkHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        apiService = retrofit.create(clss);
    }

    private Interceptor provideForcedOfflineCacheInterceptor() {
        return chain -> {
            Request request = chain.request();

            CacheControl cacheControl = new CacheControl.Builder()
                    .maxStale(7, TimeUnit.DAYS)
                    .build();

            request = request.newBuilder()
                    .removeHeader(HEADER_PRAGMA)
                    .removeHeader(HEADER_CACHE_CONTROL)
                    .cacheControl(cacheControl)
                    .build();

            return chain.proceed(request);

        };
    }
    private Cache provideCache() {
        Cache cache = null;

        try {
            cache = new Cache(new File(BaseApplication.Companion.getInstance().getContext().getCacheDir(), "http-cache"),
                    50 * 1024 * 1024); // 50 MB
        } catch (Exception e) {
            e.printStackTrace();
        }

        return cache;
    }


}
