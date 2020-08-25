package br.com.legacy.service.retrofit;

import br.com.legacy.managers.BaseManager;
import br.com.legacy.managers.dtos.ErrorResponse;
import br.com.legacy.managers.dtos.OutsmartResponse;
import br.com.R;

import java.net.UnknownHostException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by rudieros on 8/18/17.
 */

public class RetrofitCallback<T> {

    BaseManager.RequestResponseHandler handler;
    BaseManager manager;
    String key;

    public RetrofitCallback(BaseManager manager, BaseManager.RequestResponseHandler handler, String key) {
        this.handler = handler;
        this.manager = manager;
        this.key = key;
    }

    private ErrorResponse retrofitError(Throwable exception) {
        ErrorResponse e = new ErrorResponse();
        if(exception instanceof UnknownHostException)
            e.message = manager.getErrorMessages().unableToConnect();
        else
            e.message = manager.getErrorMessages().unknownError();
        return e;
    }

    public Callback<OutsmartResponse<T>> getRetrofitCallback() {
        Callback<OutsmartResponse<T>> callback = new Callback<OutsmartResponse<T>>() {
            @Override
            public void onResponse(Call<OutsmartResponse<T>> call, Response<OutsmartResponse<T>> response) {
                if (response.body() == null || response.body().errors != null && response.body().errors.size() > 0) {
                    String errorMessage = "Unknown error";
                    if (response.body() != null && response.body().errors.get(0) != null)
                        errorMessage =  response.body().errors.get(0).message;
//                    String errorPath = response.body().errors.get(0).path;
                    String errorDescription = errorMessage + " Path: " + "unknown"; // TODO: Arrumar
                    manager.handleError(new ErrorResponse(response.code(), manager.activity.getString(R.string.unkown_error)), handler);
//                    Log.d("Resquest Error:", response.errorBody().toString());
                } else {

                    if(key != null) {
                        manager.handleCacheResponse(response.body(), response.code(), handler, key);
                    }else{
                        manager.handleResponse(response.body(), response.code(), handler);
                    }
                }
            }

            @Override
            public void onFailure(Call<OutsmartResponse<T>> call, Throwable t) {
                ErrorResponse errorResponse = retrofitError(t);
                manager.handleError(errorResponse, handler);
            }
        };
        return callback;
    }
}
