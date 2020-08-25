package br.com.goin.retrofitService;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import br.com.goin.model.UserTokenInterceptor;
import br.com.goin.component.rest.api.RetrofitService;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;


// Will be used in the future to mock the access to the api services
public class TestRetrofitService<T> extends RetrofitService<T> {
    public TestRetrofitService(Class<T> clss, String baseUrl, OfflineMockInterceptor interceptor) {
        super(clss, baseUrl);

        UserTokenInterceptor interceptor2 = new UserTokenInterceptor();

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();

        logging.setLevel(HttpLoggingInterceptor.Level.HEADERS);

        HttpLoggingInterceptor logging2 = new HttpLoggingInterceptor();

        logging2.setLevel(HttpLoggingInterceptor.Level.BODY);

        authOkHttpClient = new OkHttpClient.Builder()
                .readTimeout(Constants.TIMEOUT, TimeUnit.SECONDS)
                .connectTimeout(Constants.TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(Constants.TIMEOUT, TimeUnit.SECONDS)
                .addInterceptor(logging)
                .addInterceptor(logging2)
                .addInterceptor(interceptor)
                .addInterceptor(interceptor2)
                .build();

        retrofit = retrofit.newBuilder().client(authOkHttpClient).build();
        apiService = retrofit.create(clss);
    }

    public static List<String> mockedResponses = new ArrayList<>();
    public static List<Integer> mockedResponsesHttpCodes = new ArrayList<>();

    public static class OfflineMockInterceptor implements Interceptor {

        private final MediaType MEDIA_JSON = MediaType.parse("application/json");

        @Override
        public Response intercept(@NonNull Chain chain) {
            String json = mockedResponses.remove(0);
            int httpCode = mockedResponsesHttpCodes.remove(0);

            return new Response.Builder()
                    .body(ResponseBody.create(MEDIA_JSON, json))
                    .code(httpCode)
                    .protocol(Protocol.HTTP_2)
                    .request(chain.request())
                    .message("")
                    .build();
        }
    }
}
