package br.com.goin.viewControllers.activitites;


import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.filters.MediumTest;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import android.view.View;

import com.google.api.client.http.HttpStatusCodes;

import org.hamcrest.Matcher;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import br.com.goin.managers.BaseManager;
import br.com.goin.R;
import br.com.goin.retrofitService.TestRetrofitService;
import br.com.goin.features.home.HomeActivity;
import br.goin.features.login.login.LoginActivity;
import br.com.goin.helpers.CognitoTestHelper;
import br.com.goin.helpers.UserTestHelper;
import br.com.goin.mocks.UserModelMocked;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.Intents.intending;
import static androidx.test.espresso.intent.Intents.times;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static br.com.goin.helpers.TestHelpers.childAtPosition;
import static org.hamcrest.Matchers.allOf;

@MediumTest
@RunWith(AndroidJUnit4.class)
public class LoginActivityTest {

    @Rule
    public IntentsTestRule<LoginActivity> mActivityTestRule = new IntentsTestRule<>(LoginActivity.class, false, false);

    @BeforeClass
    public static void mocksSetup() {
        CognitoTestHelper.mockLogin();

        UserTestHelper.mockUserPushEndpoint();
        UserTestHelper.mockUser();
    }

    @Before
    public void initialSetup() {
        BaseManager.logoutFromSession();
        mActivityTestRule.launchActivity(new Intent());
    }

    @Test
    public void should_HaveInputFields_OnLoginScreen() {
        onView(withId(R.id.input_layout_email)).check(matches(isDisplayed()));
        onView(withId(R.id.input_password)).check(matches(isDisplayed()));
    }

    @Test
    public void should_FireIntent_When_UserTriesToSignIn() {
        // Given
        TestRetrofitService.mockedResponses.add(UserModelMocked.mockedUser);
        TestRetrofitService.mockedResponsesHttpCodes.add(HttpStatusCodes.STATUS_CODE_OK);
        mockIntentHomeActivity();

        // Then
        writeEmail(UserModelMocked.validUserEmail);
        writePassword(UserModelMocked.validUserPassword);
        clickSignIn();

        //Should
        assertIntentHomeActivity(1);
    }

    @Test
    public void should_NotFireIntent_When_UserTriesToSignInWithInvalidCredentials() {
        // Then
        writeEmail(UserModelMocked.validUserEmail);
        writePassword(UserModelMocked.invalidUserPassword);
        clickSignIn();

        // Should
        assertDialogDisplayed(R.string.invalid_user);
        assertIntentHomeActivity(0);
    }

    @Test
    public void should_NotFireIntent_When_UserTriesToSignInWithEmptyEmail() {
        // Then
        writeEmail("");
        writePassword(UserModelMocked.validUserPassword);
        clickSignIn();

        // Should
        assertDialogDisplayed(R.string.fill_all_fields_to_sing_in);
        assertIntentHomeActivity(0);
    }

    @Test
    public void should_NotFireIntent_When_UserTriesToSignInWithEmptyPassword() {
        // Then
        writeEmail(UserModelMocked.validUserEmail);
        writePassword("");
        clickSignIn();

        // Should
        assertDialogDisplayed(R.string.fill_all_fields_to_sing_in);
        assertIntentHomeActivity(0);
    }

    @Test
    public void should_NotFireIntent_When_UserTriesToSignInWithInvalidEmail() {
        // Then
        writeEmail("teste");
        writePassword(UserModelMocked.validUserPassword);
        clickSignIn();

        // Should
        assertDialogDisplayed(R.string.invalid_email);
        assertIntentHomeActivity(0);
    }

    private void writeEmail(String email) {
        Matcher<View> emailViewMatcher = childAtPosition(
                childAtPosition(
                        withId(R.id.input_layout_email),
                        0),
                0);

        onView(allOf(withId(R.id.input_e), emailViewMatcher)).perform(scrollTo(), replaceText(email), closeSoftKeyboard());
    }

    private void writePassword(String password) {
        Matcher<View> passwordViewMatcher = childAtPosition(
                childAtPosition(
                        withId(R.id.input_layout_password),
                        0),
                0);

        onView(allOf(withId(R.id.input_password), passwordViewMatcher)).perform(scrollTo(), replaceText(password), closeSoftKeyboard());
    }

    private void clickSignIn() {
        onView(withId(R.id.login_btn_Login)).perform(click());
    }

    private void assertDialogDisplayed(int dialogTextResource) {
        onView(withText(dialogTextResource)).check(matches(isDisplayed()));
    }

    private void assertIntentHomeActivity(int times) {
        intended(hasComponent(HomeActivity.class.getName()), times(times));
    }

    private void mockIntentHomeActivity() {
        intending(hasComponent(HomeActivity.class.getName())).respondWith(new Instrumentation.ActivityResult(Activity.RESULT_OK, null));
    }

    @After
    public void clearResponses() {
        if (TestRetrofitService.mockedResponsesHttpCodes != null) {
            TestRetrofitService.mockedResponsesHttpCodes.clear();
        }
        if (TestRetrofitService.mockedResponses != null) {
            TestRetrofitService.mockedResponses.clear();
        }
    }
}
