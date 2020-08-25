package br.com.legacy.managers.Iugu;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface IuguApiService {

    @POST("/v1/payment_token")
    Call<IuguPaymentTokenResponse> getIuguPaymentToken( @Header("Authorization") String authToken,
                                                        @Body IuguParameterModel body);

}