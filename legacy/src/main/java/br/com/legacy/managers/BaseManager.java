package br.com.legacy.managers;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;

import br.com.goin.base.helpers.PreferenceHelper;
import br.com.legacy.managers.dtos.ErrorResponse;
import br.com.legacy.managers.dtos.OutsmartResponse;
import br.com.legacy.navigation.Coordinator;
import br.com.R;
import br.com.legacy.service.CacheServiceManager;
import br.com.legacy.service.retrofit.RetrofitErrorMessages;
import br.com.legacy.service.ServiceManager;
import br.com.legacy.utils.Constants;
import br.com.legacy.utils.ErrorTranslator;
import br.com.legacy.utils.SharedPreferencesControl;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rudieros on 8/18/17.
 */

public class BaseManager {

    public static ServiceManager serviceManager;
    public static CacheServiceManager cacheServiceManager;
    public RequestResponseHandler requestResponseHandler;
    protected SharedPreferencesControl sharedPreferencesControl;
    public Activity activity;

    public interface RequestResponseHandler<T> {
        void onResponse(T response);

        void onFailure(ErrorResponse error);

    }

    public interface AuthenticatedRequest {
        void makeRequest();
    }

    public interface ExternalRequest {
        void makeExternalRequest();
    }

    public interface RequestManager {
        void cancel();
    }

    public BaseManager(Activity activity) {
        this.activity = activity;

        sharedPreferencesControl = new SharedPreferencesControl(activity, Constants.SHARED_PREFERENCES);
        if (serviceManager == null) serviceManager = new ServiceManager();
        if (cacheServiceManager == null) cacheServiceManager = new CacheServiceManager();
    }

    public List<RequestManager> requests = new ArrayList<>();

    public void handleResponse(OutsmartResponse response, int responseStatus, RequestResponseHandler handler) {
        ErrorResponse error = ErrorTranslator.translateError(responseStatus);
        if (error == null) {
            handler.onResponse(response.getData());
        } else {
            handler.onFailure(error);
        }
    }

    public void handleCacheResponse(OutsmartResponse response, int responseStatus, RequestResponseHandler handler, String key) {
        ErrorResponse error = ErrorTranslator.translateError(responseStatus);
        if (error == null) {

            if(PreferenceHelper.INSTANCE.sameOfCache(key, response.getData())){
                handler.onResponse(null);
            }else{
                PreferenceHelper.INSTANCE.write(key, response.getData());
                handler.onResponse(response.getData());
            }


        } else {
            handler.onFailure(error);
        }
    }

    public void handleCacheResponse(RequestResponseHandler handler, String key) {
        handler.onResponse(PreferenceHelper.INSTANCE.read(key));
    }

    public void handleError(ErrorResponse error, RequestResponseHandler handler) {
        // TODO: implementar handler de erro
        handler.onFailure(error);
    }

    public void cancelRequests() {
        for (RequestManager request : requests) {
            request.cancel();
        }
    }

    public void makeAuthenticatedRequest(final AuthenticatedRequest authenticatedRequest) {
        makeRequest(authenticatedRequest);
    }

    private void makeRequest(final AuthenticatedRequest authenticatedRequest) {
        try {
            authenticatedRequest.makeRequest();
        } catch (Exception e) {
        }
    }

    public void makeExternalRequest(final ExternalRequest externalRequest) {
        externalRequest.makeExternalRequest();
    }


    public void logout() {
        // log user out with a feedback
        try {
            new AlertDialog.Builder(activity)
                    .setMessage(R.string.expired_session)
                    .setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Coordinator.goToStart(activity);
                        }
                    }).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void logoutFromSession() {
        // log user out without feedback
    }

    public RetrofitErrorMessages getErrorMessages() {
        RetrofitErrorMessages errorMessages = new RetrofitErrorMessages() {
            @Override
            public String unableToConnect() {
                return activity.getString(R.string.retrofit_error_unable_to_connect);
            }

            @Override
            public String unknownError() {
                return activity.getString(R.string.retrofit_error_unknown);
            }
        };
        return errorMessages;
    }

}
