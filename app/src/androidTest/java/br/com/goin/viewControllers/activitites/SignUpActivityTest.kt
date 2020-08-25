package br.com.goin.viewControllers.activitites


import android.app.Activity
import android.app.Instrumentation
import android.content.Intent
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents.*
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.MediumTest
import androidx.test.ext.junit.runners.AndroidJUnit4
import br.com.goin.managers.BaseManager
import br.com.goin.R
import br.com.goin.retrofitService.TestRetrofitService
import br.goin.features.login.signUp.SignUpActivity
import br.com.goin.helpers.CognitoTestHelper
import br.com.goin.helpers.TestHelpers.childAtPosition
import br.com.goin.helpers.UserTestHelper
import br.com.goin.mocks.UserModelMocked
import com.google.api.client.http.HttpStatusCodes
import org.hamcrest.Matchers.allOf
import org.junit.*
import org.junit.runner.RunWith

@MediumTest
@RunWith(AndroidJUnit4::class)
class SignUpActivityTest {

    @JvmField
    @Rule
    var mActivityTestRule = IntentsTestRule(SignUpActivity::class.java, false, false)

    companion object {
        @BeforeClass
        @JvmStatic
        fun mocksSetup() {
            CognitoTestHelper.mockSignUp()

            UserTestHelper.mockUserPushEndpoint()
            UserTestHelper.mockUser()
        }
    }

    @Before
    fun initialSetup() {
        BaseManager.logoutFromSession()
        mActivityTestRule.launchActivity(Intent())
    }

    @Test
    fun should_HaveInputFields_OnSignUpScreen() {
        onView(withId(R.id.signup_name_field)).check(matches(isDisplayed()))
        onView(withId(R.id.signup_email_field)).check(matches(isDisplayed()))
        onView(withId(R.id.signup_password_field)).check(matches(isDisplayed()))
        onView(withId(R.id.signup_confirm_password_field)).check(matches(isDisplayed()))
        onView(withId(R.id.consent)).check(matches(isDisplayed()))
    }

    @Test
    fun should_FireIntent_When_UserTriesToSignUp() {
        TestRetrofitService.mockedResponses.add(UserModelMocked.mockedVerifyEmailUserDoesNotExists)
        TestRetrofitService.mockedResponsesHttpCodes.add(HttpStatusCodes.STATUS_CODE_OK)
        TestRetrofitService.mockedResponses.add(UserModelMocked.mockedSendUserInfo)
        TestRetrofitService.mockedResponsesHttpCodes.add(HttpStatusCodes.STATUS_CODE_OK)
        TestRetrofitService.mockedResponses.add(UserModelMocked.mockedTempUser)
        TestRetrofitService.mockedResponsesHttpCodes.add(HttpStatusCodes.STATUS_CODE_OK)

        mockIntentChooseProfilePictureActivity()

        writeName(UserModelMocked.validUserName)
        writeEmail(UserModelMocked.validUserEmail)
        writePassword(UserModelMocked.validUserPassword)
        writePasswordConfirmation(UserModelMocked.validUserPassword)
        selectCheckBox()

        clickSignUpButton()

        assertIntentChooseProfilePictureActivity(1)
    }

    @Test
    fun should_NotFireIntent_When_UserTriesToSignUpWithInvalidEmail() {
        writeName(UserModelMocked.validUserName)
        writeEmail(UserModelMocked.invalidUserEmail)
        writePassword(UserModelMocked.validUserPassword)
        writePasswordConfirmation(UserModelMocked.validUserPassword)
        selectCheckBox()

        clickSignUpButton()

        assertDialogDisplayed(R.string.invalid_email)
        assertIntentChooseProfilePictureActivity(0)
    }

    @Test
    fun should_NotFireIntent_When_UserTriesToSignUpWithBlankName() {
        writeName("")
        writeEmail(UserModelMocked.validUserEmail)
        writePassword(UserModelMocked.validUserPassword)
        writePasswordConfirmation(UserModelMocked.validUserPassword)
        selectCheckBox()

        clickSignUpButton()

        assertDialogDisplayed(R.string.please_insert_name)
        assertIntentChooseProfilePictureActivity(0)
    }

    @Test
    fun should_NotFireIntent_When_UserTriesToSignUpWithBlankPassword() {
        writeName(UserModelMocked.validUserName)
        writeEmail(UserModelMocked.validUserEmail)
        writePassword("")
        writePasswordConfirmation(UserModelMocked.validUserPassword)
        selectCheckBox()

        clickSignUpButton()

        assertDialogDisplayed(R.string.please_insert_password)
        assertIntentChooseProfilePictureActivity(0)
    }

    @Test
    fun should_NotFireIntent_When_UserTriesToSignUpWithBlankPasswordConfirmation() {
        writeName(UserModelMocked.validUserName)
        writeEmail(UserModelMocked.validUserEmail)
        writePassword(UserModelMocked.validUserPassword)
        writePasswordConfirmation("")
        selectCheckBox()

        clickSignUpButton()

        assertDialogDisplayed(R.string.please_confirm_password)
        assertIntentChooseProfilePictureActivity(0)
    }

    @Test
    fun should_NotFireIntent_When_UserTriesToSignUpWithDifferentPasswords() {
        writeName(UserModelMocked.validUserName)
        writeEmail(UserModelMocked.validUserEmail)
        writePassword(UserModelMocked.validUserPassword)
        writePasswordConfirmation(UserModelMocked.invalidUserPassword)
        selectCheckBox()

        clickSignUpButton()

        assertDialogDisplayed(R.string.password_dont_match)
        assertIntentChooseProfilePictureActivity(0)
    }

    @Test
    fun should_NotFireIntent_When_UserTriesToSignUpWithInvalidPassword() {
        writeName(UserModelMocked.validUserName)
        writeEmail(UserModelMocked.validUserEmail)
        writePassword(UserModelMocked.invalidUserPassword)
        writePasswordConfirmation(UserModelMocked.invalidUserPassword)
        selectCheckBox()

        clickSignUpButton()

        assertDialogDisplayed(R.string.password_incomplete)
        assertIntentChooseProfilePictureActivity(0)
    }

    @Test
    fun should_NotFireIntent_When_UserTriesToSignUpWithoutConsentWithUserTerms() {
        writeName(UserModelMocked.validUserName)
        writeEmail(UserModelMocked.validUserEmail)
        writePassword(UserModelMocked.validUserPassword)
        writePasswordConfirmation(UserModelMocked.validUserPassword)

        clickSignUpButton()

        assertDialogDisplayed(R.string.invalid_checked)
        assertIntentChooseProfilePictureActivity(0)
    }

    @Test
    fun should_NotFireIntent_When_UserTriesToSignUpWithAlreadyExistingCredentials() {
        TestRetrofitService.mockedResponses.add(UserModelMocked.mockedVerifyEmail)
        TestRetrofitService.mockedResponsesHttpCodes.add(HttpStatusCodes.STATUS_CODE_OK)

        writeName(UserModelMocked.validUserName)
        writeEmail(UserModelMocked.validUserEmail)
        writePassword(UserModelMocked.validUserPassword)
        writePasswordConfirmation(UserModelMocked.validUserPassword)
        selectCheckBox()

        clickSignUpButton()

        assertDialogDisplayed(R.string.email_exists)
        assertIntentChooseProfilePictureActivity(0)
    }

    private fun writeName(name: String) {
        val nameViewMatcher = childAtPosition(
                childAtPosition(
                        withId(R.id.view2),
                        0),
                0)

        onView(allOf(withId(R.id.signup_name_field), nameViewMatcher)).perform(scrollTo(), replaceText(name), closeSoftKeyboard())
    }

    private fun writeEmail(email: String) {
        val emailViewMatcher = childAtPosition(
                childAtPosition(
                        withId(R.id.view1),
                        0),
                0)

        onView(allOf(withId(R.id.signup_email_field), emailViewMatcher)).perform(scrollTo(), replaceText(email), closeSoftKeyboard())
    }

    private fun writePassword(password: String) {
        val passwordViewMatcher = childAtPosition(
                childAtPosition(
                        withId(R.id.view5),
                        0),
                0)

        onView(allOf(withId(R.id.signup_password_field), passwordViewMatcher)).perform(scrollTo(), replaceText(password), closeSoftKeyboard())
    }

    private fun writePasswordConfirmation(password: String) {
        val passwordConfirmationViewMatcher = childAtPosition(
                childAtPosition(
                        withId(R.id.view6),
                        0),
                0)

        onView(allOf(withId(R.id.signup_confirm_password_field), passwordConfirmationViewMatcher)).perform(scrollTo(), replaceText(password), closeSoftKeyboard())
    }

    private fun clickSignUpButton() {
        val signUpButtonViewMatcher = onView(allOf(withId(R.id.signup_btn_SignUp), withText(R.string.btn_sign_up)))

        signUpButtonViewMatcher.perform(scrollTo(), click())
    }

    private fun selectCheckBox() {
        onView(withId(R.id.consent)).perform(click())
    }

    private fun assertDialogDisplayed(dialogTextResource: Int) {
        onView(withText(dialogTextResource)).check(matches(isDisplayed()))
    }

    private fun assertIntentChooseProfilePictureActivity(times: Int) {
        intended(hasComponent(UploadProfilePictureActivity::class.java.name), times(times))
    }

    private fun mockIntentChooseProfilePictureActivity() {
        intending(hasComponent(UploadProfilePictureActivity::class.java.name)).respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK, null))
    }
    @After
    fun clearResponses() {
        if (TestRetrofitService.mockedResponsesHttpCodes != null) {
            TestRetrofitService.mockedResponsesHttpCodes.clear()
        }
        if (TestRetrofitService.mockedResponses != null) {
            TestRetrofitService.mockedResponses.clear()
        }
    }
}
