package br.com.legacy.service.retrofit;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * Created by debs_atanes on 5/jul/18.
 */

public class RetrofitExternalService<T> {

    private T apiService;
    private Retrofit retrofit;

    public T getApiService() {
        return apiService;
    }

    public RetrofitExternalService(Class<T> clss, String baseUrl) {
        retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiService = retrofit.create(clss);
    }
}
