package br.com.legacy.managers.Iugu;

import android.app.Activity;

import br.com.legacy.managers.BaseManager;
import br.com.legacy.managers.dtos.ErrorResponse;

/**
 * Created by debs_atanes on 10/jul/18.
 */

public class IuguManager extends BaseManager {

    public IuguManager.IuguServiceHandler iuguServiceHandler;

    public interface IuguServiceHandler {
        void onSuccessToGetToken(IuguPaymentTokenResponse response);
        void onError(ErrorResponse error);
    }

    public IuguManager (Activity activity, IuguManager.IuguServiceHandler handler) {
        super(activity);
        iuguServiceHandler = handler;
    }

    public void requestIuguPaymentToken(final IuguParameterModel bodyParams){
        final RequestResponseHandler handler = new RequestResponseHandler<IuguPaymentTokenResponse>() {
            @Override
            public void onResponse(IuguPaymentTokenResponse response) {
                if (iuguServiceHandler != null) {
                    iuguServiceHandler.onSuccessToGetToken(response);
                }
            }
            @Override
            public void onFailure(ErrorResponse error) {
                if (iuguServiceHandler != null) {
                    iuguServiceHandler.onError(error);
                }
            }
        };

        final IuguServiceManager iuguServiceManager = new IuguServiceManager();
        makeExternalRequest(new ExternalRequest() {
            @Override
            public void makeExternalRequest() {
                iuguServiceManager.getIuguPaymentToken(IuguManager.this, handler, bodyParams);
            }
        });
    }
}
