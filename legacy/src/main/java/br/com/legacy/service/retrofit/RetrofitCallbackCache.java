package br.com.legacy.service.retrofit;

import br.com.legacy.managers.BaseManager;
import br.com.legacy.managers.dtos.OutsmartResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by rudieros on 8/18/17.
 */

public class RetrofitCallbackCache<T> {

    BaseManager.RequestResponseHandler handler;
    BaseManager manager;
    String key;

    public RetrofitCallbackCache(BaseManager manager, BaseManager.RequestResponseHandler handler, String key) {
        this.handler = handler;
        this.manager = manager;
        this.key = key;
    }

    public Callback<OutsmartResponse<T>> getRetrofitCallback() {
        Callback<OutsmartResponse<T>> callback = new Callback<OutsmartResponse<T>>() {
            @Override
            public void onResponse(Call<OutsmartResponse<T>> call, Response<OutsmartResponse<T>> response) {
                manager.handleCacheResponse(handler, key);
            }

            @Override
            public void onFailure(Call<OutsmartResponse<T>> call, Throwable t) {
                manager.handleCacheResponse(handler, key);
            }
        };
        return callback;
    }
}
