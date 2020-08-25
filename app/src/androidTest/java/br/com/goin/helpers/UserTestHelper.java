package br.com.goin.helpers;

import org.mockito.Mockito;

import br.com.goin.RetrofitService.TestRetrofitService;
import br.com.goin.model.user.UserApi;
import br.com.goin.component.session.user.push.UserPushEndpointInteractor;
import br.com.goin.component.session.user.push.UserPushEndpointInteractorImpl;
import br.goin.features.login.model.UserRepository;

public class UserTestHelper {

    public static void mockUserPushEndpoint() {
        UserPushEndpointInteractor mockedUserPushInteractor = Mockito.mock(UserPushEndpointInteractorImpl.class);
        UserPushEndpointInteractor.Companion.setInstance(mockedUserPushInteractor);
    }

    public static void mockUser() {
        UserRepository.Companion.setService(new TestRetrofitService<>(UserApi.class, Constants. , new TestRetrofitService.OfflineMockInterceptor()));
    }
}
