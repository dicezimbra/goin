package br.com.goin.viewControllers.activitites


import android.content.Intent
import android.os.Bundle
import androidx.annotation.IdRes
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import androidx.test.rule.ActivityTestRule
import br.com.goin.R
import br.com.goin.RetrofitService.TestRetrofitService
import br.com.goin.features.terms.TermsActivity
import br.com.goin.features.terms.TermsType
import br.com.goin.helpers.TestHelpers
import br.com.goin.mocks.TermsModelMocked
import com.google.api.client.http.HttpStatusCodes
import org.junit.After
import org.junit.BeforeClass
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@MediumTest
@RunWith(AndroidJUnit4::class)
class TermsActivityTest {
    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(TermsActivity::class.java, false, false)
    private val b: Bundle = Bundle()

    companion object {
        @BeforeClass
        @JvmStatic
        fun mocksSetup() {
            TestHelpers.mockTerms()
        }
    }

    @Test
    fun should_ShowHalfPriceScreenTitle_WhenUserEntersTheHalfPriceScreen() {
        // Given
        TestRetrofitService.mockedResponses.add(TermsModelMocked.termsMocked)
        TestRetrofitService.mockedResponsesHttpCodes.add(HttpStatusCodes.STATUS_CODE_OK)

        // Setup
        setupTermsTypeAndLaunchActivity(TermsType.HALF_PRICING_POLICY)

        // Should
        assertScreenTitle(R.string.entry_policy)
        assertAllItemsAreDisplayed()
        assertItemDescriptionIsDisplayed()
    }

    @Test
    fun should_ShowPrivacyPolicyScreenTitle_WhenUserEntersTheHalfPriceScreen() {
        // Given
        TestRetrofitService.mockedResponses.add(TermsModelMocked.termsMocked)
        TestRetrofitService.mockedResponsesHttpCodes.add(HttpStatusCodes.STATUS_CODE_OK)

        // Setup
        setupTermsTypeAndLaunchActivity(TermsType.PRIVACY_POLICY)

        // Should
        assertScreenTitle(R.string.privacy_policy)
        assertAllItemsAreDisplayed()
        assertItemDescriptionIsDisplayed()
    }

    @Test
    fun should_ShowTermsOfUseScreenTitle_WhenUserEntersTheHalfPriceScreen() {
        // Given
        TestRetrofitService.mockedResponses.add(TermsModelMocked.termsMocked)
        TestRetrofitService.mockedResponsesHttpCodes.add(HttpStatusCodes.STATUS_CODE_OK)

        // Setup
        setupTermsTypeAndLaunchActivity(TermsType.TERMS_OF_USE)

        // Should
        assertScreenTitle(R.string.terms_of_use)
        assertAllItemsAreDisplayed()
        assertItemDescriptionIsDisplayed()
    }

    @Test
    fun should_ShowErrorMessage_WhenTermsReturnEmpty() {
        // Given
        TestRetrofitService.mockedResponses.add(TermsModelMocked.emptyTerms)
        TestRetrofitService.mockedResponsesHttpCodes.add(HttpStatusCodes.STATUS_CODE_OK)

        // Setup
        setupTermsTypeAndLaunchActivity()

        // Should
        assertDialogDisplayed()
    }

    @Test
    fun should_ShowErrorMessage_WhenRequisitionFails() {
        // Given
        TestRetrofitService.mockedResponses.add(TermsModelMocked.termsMocked)
        TestRetrofitService.mockedResponsesHttpCodes.add(HttpStatusCodes.STATUS_CODE_NOT_FOUND)

        // Setup
        setupTermsTypeAndLaunchActivity()

        // Should
        assertDialogDisplayed()
    }

    @Test
    fun should_ToggleTerms_WhenUserChangesTheTermsSelected() {
        // Given
        TestRetrofitService.mockedResponses.add(TermsModelMocked.termsMocked)
        TestRetrofitService.mockedResponsesHttpCodes.add(HttpStatusCodes.STATUS_CODE_OK)

        // Setup
        setupTermsTypeAndLaunchActivity()

        // Should
        assertAllItemsAreDisplayed()
        clickOnItemWithText(TermsModelMocked.userTerms)
        assertItemDescriptionIsDisplayed(TermsModelMocked.description + TermsModelMocked.userTerms)
        clickOnItemWithText(TermsModelMocked.privacyPolicy)
        assertItemDescriptionIsDisplayed(TermsModelMocked.description + TermsModelMocked.privacyPolicy)
    }

    @Test
    fun should_ShowTermDescription_WhenUserSelectsItem() {
        // Given
        TestRetrofitService.mockedResponses.add(TermsModelMocked.termsMocked)
        TestRetrofitService.mockedResponsesHttpCodes.add(HttpStatusCodes.STATUS_CODE_OK)

        // Setup
        setupTermsTypeAndLaunchActivity()

        // Should
        assertAllItemsAreDisplayed()
        clickOnItemWithText(TermsModelMocked.privacyPolicy)
        assertItemDescriptionIsDisplayed(TermsModelMocked.description + TermsModelMocked.privacyPolicy)
    }

    @Test
    fun should_CloseTermDescription_WhenUserSelectsItemAgain() {
        // Given
        TestRetrofitService.mockedResponses.add(TermsModelMocked.termsMocked)
        TestRetrofitService.mockedResponsesHttpCodes.add(HttpStatusCodes.STATUS_CODE_OK)

        // Setup
        setupTermsTypeAndLaunchActivity()

        // Should
        assertAllItemsAreDisplayed()
        clickOnItemWithText(TermsModelMocked.halfPrice)
        assertItemDescriptionIsDisplayed(TermsModelMocked.description + TermsModelMocked.halfPrice)
        clickOnItemWithText(TermsModelMocked.halfPrice)
        assertItemDescriptionIsDisplayed()
    }

    private fun setupTermsTypeAndLaunchActivity(relation: TermsType = TermsType.PRIVACY_POLICY) {
        b.putString(TermsActivity.termTypeKey, relation.name)
        mActivityTestRule.launchActivity(Intent().apply { putExtras(b) })
    }

    private fun assertScreenTitle(@IdRes title: Int) {
        onView(withText(title)).perform(click())
    }

    private fun clickOnItemWithText(title: String) {
        onView(withText(title)).perform(click())
    }

    private fun assertAllItemsAreDisplayed() {
        val possibleTermsTitles = arrayListOf(
                TermsModelMocked.halfPrice,
                TermsModelMocked.privacyPolicy,
                TermsModelMocked.userTerms
        )

        for(term in possibleTermsTitles) {
            onView(withText(term)).check(matches(isDisplayed()))
        }
    }

    private fun assertItemDescriptionIsDisplayed(cellDescription: String? = null) {
        val possibleTerms = arrayListOf(
                TermsModelMocked.description + TermsModelMocked.halfPrice,
                TermsModelMocked.description + TermsModelMocked.privacyPolicy,
                TermsModelMocked.description + TermsModelMocked.userTerms
        )

        for(term in possibleTerms) {
            onView(withText(term)).check(
                if(cellDescription == term) matches(isDisplayed()) else doesNotExist()
            )
        }
    }

    private fun assertDialogDisplayed() {
        onView(withText(R.string.unkown_error)).check(matches(isDisplayed()))
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
