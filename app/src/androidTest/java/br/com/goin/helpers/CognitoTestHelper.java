package br.com.goin.helpers;

import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserSession;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.tokens.CognitoAccessToken;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.tokens.CognitoIdToken;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.tokens.CognitoRefreshToken;
import com.amazonaws.services.cognitoidentityprovider.model.NotAuthorizedException;

import org.mockito.Mockito;

import br.com.goin.mocks.UserModelMocked;
import br.com.goin.model.user.cognito.CognitoInteractor;
import br.com.goin.model.user.cognito.CognitoInteractorImpl;
import io.reactivex.Completable;
import io.reactivex.Observable;

import static org.hamcrest.Matchers.not;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.hamcrest.MockitoHamcrest.argThat;

public class CognitoTestHelper {
    public static void mockLogin() {
        CognitoInteractor mockedCognitoInteractor = Mockito.mock(CognitoInteractorImpl.class);
        Mockito.when(mockedCognitoInteractor.login(UserModelMocked.validUserEmail, UserModelMocked.validUserPassword))
                .thenReturn(Observable.just(new CognitoUserSession(new CognitoIdToken(""), new CognitoAccessToken(""), new CognitoRefreshToken(""))));

        NotAuthorizedException exception = new NotAuthorizedException("");
        exception.setErrorCode("ErrorCode");
        Mockito.when(mockedCognitoInteractor.login(anyString(), argThat(not(UserModelMocked.validUserPassword))))
                .thenReturn(Observable.error(exception));

        CognitoInteractor.Companion.setInstance(mockedCognitoInteractor);
    }

    public static void mockSignUp() {
        CognitoInteractor mockedCognitoInteractor = Mockito.mock(CognitoInteractorImpl.class);
        Mockito.when(mockedCognitoInteractor.signUp(UserModelMocked.validUserEmail, UserModelMocked.validUserPassword))
                .thenReturn(Completable.complete());

        Mockito.when(mockedCognitoInteractor.firstSignIn(UserModelMocked.validUserEmail, UserModelMocked.validUserPassword))
                .thenReturn(Completable.complete());

        NotAuthorizedException exception = new NotAuthorizedException("");
        exception.setErrorCode("ErrorCode");
        Mockito.when(mockedCognitoInteractor.signUp(argThat(not(UserModelMocked.validUserEmail)), argThat(not(UserModelMocked.validUserPassword))))
                .thenReturn(Completable.error(exception));

        Mockito.when(mockedCognitoInteractor.firstSignIn(argThat(not(UserModelMocked.validUserEmail)), argThat(not(UserModelMocked.validUserPassword))))
                .thenReturn(Completable.error(exception));

        CognitoInteractor.Companion.setInstance(mockedCognitoInteractor);
    }
}
