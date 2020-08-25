package br.com.legacy.service;

import br.com.legacy.managers.BaseManager;
import br.com.legacy.managers.dtos.ErrorResponse;
import br.com.legacy.managers.dtos.OutsmartResponse;
import br.com.legacy.managers.dtos.QueryResponse;
import br.com.legacy.managers.Iugu.IuguApiService;
import br.com.legacy.service.retrofit.OutsmartApiCacheService;
import br.com.legacy.service.retrofit.OutsmartApiService;
import br.com.legacy.service.retrofit.RetrofitCacheService;
import br.com.legacy.service.retrofit.RetrofitCallback;
import br.com.legacy.service.retrofit.RetrofitCallbackCache;
import br.com.goin.component.rest.api.RetrofitService;
import br.com.legacy.service.retrofit.RetrofitExternalService;
import br.com.legacy.utils.Constants;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by appsimples on 9/15/17.
 */

public class BaseServiceManager {

    public static RetrofitService<OutsmartApiService> outsmartService = new RetrofitService<>(OutsmartApiService.class, Constants.GRAPHQL_BASE_URL);
    public static RetrofitCacheService<OutsmartApiCacheService> outsmartCacheService = new RetrofitCacheService<>(OutsmartApiCacheService.class, Constants.GRAPHQL_BASE_URL);
    public static RetrofitExternalService<IuguApiService> iuguService = new RetrofitExternalService<>(IuguApiService.class, Constants.IUGU_API_ENDPOINT);

    private void addRetrofitRequest(BaseManager manager, final Call<?> call) {
        manager.requests.add(new BaseManager.RequestManager() {
            @Override
            public void cancel() {
                call.cancel();
            }
        });
    }

    private Callback retrofitCallback(BaseManager manager, BaseManager.RequestResponseHandler handler, String key) {
        return new RetrofitCallback<>(manager, handler, key).getRetrofitCallback();
    }

    private Callback retrofitCallbackCache(BaseManager manager, BaseManager.RequestResponseHandler handler, String key) {
        return new RetrofitCallbackCache<>(manager, handler, key).getRetrofitCallback();
    }

    public void enqueueCall(Call call, BaseManager manager, BaseManager.RequestResponseHandler handler, String key) {
        call.enqueue(retrofitCallback(manager, handler, key));
        addRetrofitRequest(manager, call);
    }

    public void enqueueCallCache(Call<OutsmartResponse<QueryResponse>> call, BaseManager manager, BaseManager.RequestResponseHandler handler, String key) {
        call.enqueue(retrofitCallbackCache(manager, handler, key));
        //addRetrofitRequest(manager, call);
    }

    public void enqueueCall(Call call, BaseManager manager, BaseManager.RequestResponseHandler handler ){
        call.enqueue(retrofitCallback(manager, handler, null));
        addRetrofitRequest(manager, call);
    }

    public void enqueueCallExternalApi(Call call, BaseManager manager, final BaseManager.RequestResponseHandler handler) {
        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                if(response.code() == 200){
                    handler.onResponse(response.body());
                } else {
                    handler.onFailure(new ErrorResponse(response.code(), response.message()));
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {
            }
        });
        addRetrofitRequest(manager, call);
    }

}
