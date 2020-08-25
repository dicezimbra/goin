package br.com.legacy.managers.Iugu;

import br.com.legacy.managers.BaseManager;
import br.com.legacy.service.BaseServiceManager;
import br.com.legacy.utils.Constants;

import retrofit2.Call;

/**
 * Created by debs_atanes on 10/jul/18.
 */

public class IuguServiceManager  extends BaseServiceManager {
    String iuguAuth;
    private String getIuguAuth(){
        if (this.iuguAuth == null){
            this.iuguAuth = "Basic " + Constants.IUGU_AUTH_TOKEN;
        }
        return this.iuguAuth;
    }

    public void getIuguPaymentToken(BaseManager manager, BaseManager.RequestResponseHandler handler, IuguParameterModel bodyParams) {
        Call<IuguPaymentTokenResponse> call = iuguService
                                            .getApiService()
                                            .getIuguPaymentToken(getIuguAuth(), bodyParams);
        enqueueCallExternalApi(call, manager, handler);
    }
}
