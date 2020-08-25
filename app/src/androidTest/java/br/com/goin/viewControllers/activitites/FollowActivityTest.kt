package br.com.goin.viewControllers.activitites


import android.content.Intent
import android.os.Bundle
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.MediumTest
import androidx.test.rule.ActivityTestRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import br.com.goin.R
import br.com.goin.RetrofitService.TestRetrofitService
import br.com.goin.feature_social.FollowActivity
import br.com.goin.features.social.FollowActivity
import br.com.goin.features.social.FollowRelation
import br.com.goin.helpers.RecyclerViewItemCountAssertion.withItemCount
import br.com.goin.helpers.TestHelpers
import br.com.goin.mocks.FollowModelMocked
import br.com.goin.mocks.UserModelMocked
import com.google.api.client.http.HttpStatusCodes
import org.junit.*
import org.junit.runner.RunWith

@MediumTest
@RunWith(AndroidJUnit4::class)
class FollowActivityTest {
    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(FollowActivity::class.java, false, false)
    private val b: Bundle = Bundle().apply {
        putString(FollowActivity.userIdKey, UserModelMocked.validUserId)
        putString(FollowActivity.currentUserIdKey, UserModelMocked.validUserId)
    }

    companion object {
        @BeforeClass
        @JvmStatic
        fun mocksSetup() {
            TestHelpers.mockFollow()
        }
    }

    @Test
    fun should_BeEmpty_WhenUserHasNoFollowers() {
        // Given
        TestRetrofitService.mockedResponses.add(FollowModelMocked.emptyUserFollowers)
        TestRetrofitService.mockedResponsesHttpCodes.add(HttpStatusCodes.STATUS_CODE_OK)

        // Setup
        setupFollowTypeAndLaunchActivity(FollowRelation.FOLLOWERS)

        // Should
        expectListToHaveUsers(0)
        assertEmptyMessageIsDisplayed(true)
    }

    @Test
    fun should_BeEmpty_WhenUserFollowsNobody() {
        // Given
        TestRetrofitService.mockedResponses.add(FollowModelMocked.emptyUserFollowing)
        TestRetrofitService.mockedResponsesHttpCodes.add(HttpStatusCodes.STATUS_CODE_OK)

        // Setup
        setupFollowTypeAndLaunchActivity(FollowRelation.FOLLOWING)

        // Should
        expectListToHaveUsers(0)
        assertEmptyMessageIsDisplayed(false)
    }

    @Test
    fun should_HaveListFilled_WhenUserHasFollowers() {
        // Given
        TestRetrofitService.mockedResponses.add(FollowModelMocked.userFollowers)
        TestRetrofitService.mockedResponsesHttpCodes.add(HttpStatusCodes.STATUS_CODE_OK)

        // Setup
        setupFollowTypeAndLaunchActivity(FollowRelation.FOLLOWERS)

        // Should
        expectListToHaveUsers(2)
    }

    @Test
    fun should_HaveListFilled_WhenUserFollowsSomePeople() {
        // Given
        TestRetrofitService.mockedResponses.add(FollowModelMocked.userFollowing)
        TestRetrofitService.mockedResponsesHttpCodes.add(HttpStatusCodes.STATUS_CODE_OK)

        // Setup
        setupFollowTypeAndLaunchActivity(FollowRelation.FOLLOWING)

        // Should
        expectListToHaveUsers(3)
    }

    @Test
    fun should_FollowUser_WhenUserTriesToFollowSomeone() {
        // Given
        TestRetrofitService.mockedResponses.add(FollowModelMocked.oneUserFollower)
        TestRetrofitService.mockedResponses.add(FollowModelMocked.successfulFollow)
        TestRetrofitService.mockedResponsesHttpCodes.add(HttpStatusCodes.STATUS_CODE_OK)
        TestRetrofitService.mockedResponsesHttpCodes.add(HttpStatusCodes.STATUS_CODE_OK)

        // Setup
        setupFollowTypeAndLaunchActivity(FollowRelation.FOLLOWERS)

        // Should
        expectListToHaveUsers(1)
        expectUserNameToBe(FollowModelMocked.followerUserName)

        checkIfUserIsFollowed(false)

        clickOnActionButton()

        checkIfUserIsFollowed(true)
    }

    @Test
    fun should_UnfollowUser_WhenUserTriesToUnfollowSomeone() {

        // Given
        TestRetrofitService.mockedResponses.add(FollowModelMocked.oneUserFollowing)
        TestRetrofitService.mockedResponses.add(FollowModelMocked.successfulUnfollow)
        TestRetrofitService.mockedResponsesHttpCodes.add(HttpStatusCodes.STATUS_CODE_OK)
        TestRetrofitService.mockedResponsesHttpCodes.add(HttpStatusCodes.STATUS_CODE_OK)

        // Setup
        setupFollowTypeAndLaunchActivity(FollowRelation.FOLLOWING)

        // Should
        expectListToHaveUsers(1)
        expectUserNameToBe(FollowModelMocked.followingUserName)

        checkIfUserIsFollowed(true)

        clickOnActionButton()

        assertDialogDisplayed(FollowModelMocked.followingUserName)
        confirmDialogButton()

        checkIfUserIsFollowed(false)
    }

    @Test
    fun should_NotFollowUser_WhenUserTriesToFollowSomeoneButFails() {
        // Given
        TestRetrofitService.mockedResponses.add(FollowModelMocked.oneUserFollower)
        TestRetrofitService.mockedResponses.add(FollowModelMocked.successfulFollow)
        TestRetrofitService.mockedResponsesHttpCodes.add(HttpStatusCodes.STATUS_CODE_OK)
        TestRetrofitService.mockedResponsesHttpCodes.add(HttpStatusCodes.STATUS_CODE_SERVER_ERROR)

        // Setup
        setupFollowTypeAndLaunchActivity(FollowRelation.FOLLOWERS)

        // Should
        expectListToHaveUsers(1)
        expectUserNameToBe(FollowModelMocked.followerUserName)

        checkIfUserIsFollowed(false)

        clickOnActionButton()

        checkIfUserIsFollowed(false)
    }

    @Test
    fun should_NotUnfollowUser_WhenUserTriesToUnfollowSomeoneButFails() {
        // Given
        TestRetrofitService.mockedResponses.add(FollowModelMocked.oneUserFollowing)
        TestRetrofitService.mockedResponses.add(FollowModelMocked.successfulUnfollow)
        TestRetrofitService.mockedResponsesHttpCodes.add(HttpStatusCodes.STATUS_CODE_OK)
        TestRetrofitService.mockedResponsesHttpCodes.add(HttpStatusCodes.STATUS_CODE_SERVER_ERROR)

        // Setup
        setupFollowTypeAndLaunchActivity(FollowRelation.FOLLOWING)

        // Should
        expectListToHaveUsers(1)
        expectUserNameToBe(FollowModelMocked.followingUserName)

        checkIfUserIsFollowed(true)

        clickOnActionButton()

        assertDialogDisplayed(FollowModelMocked.followingUserName)
        confirmDialogButton()

        checkIfUserIsFollowed(true)
    }

    private fun setupFollowTypeAndLaunchActivity(relation: FollowRelation) {
        b.putString(FollowActivity.followRelationKey, relation.name)
        mActivityTestRule.launchActivity(Intent().apply { putExtras(b) })
    }

    private fun expectListToHaveUsers(numberUsers: Int) {
        onView(withId(R.id.friends_list)).check(withItemCount(numberUsers))
    }

    private fun assertEmptyMessageIsDisplayed(isFollowers: Boolean) {
        val stringRes = if(isFollowers) R.string.you_have_no_followers else R.string.you_dont_follow_anyone
        onView(withText(stringRes)).check(matches(isDisplayed()))
    }

    private fun expectUserNameToBe(userName: String) {
        onView(withId(R.id.user_card_name)).check(matches(withText(userName)))
    }

    private fun checkIfUserIsFollowed(follows: Boolean) {
        val stringRes: Int = if(follows) R.string.following else R.string.follow
        onView(withId(R.id.invite_button)).check(matches(withText(stringRes)))
    }

    private fun clickOnActionButton() {
        onView(withId(R.id.invite_button)).perform(click())
    }

    private fun assertDialogDisplayed(name: String) {
        val unfollowAlert = String.format(mActivityTestRule.activity.getString(R.string.unfollow_alert) ?: "", name)
        onView(withText(unfollowAlert)).check(matches(isDisplayed()))
    }

    private fun confirmDialogButton() {
        onView(withId(android.R.id.button1)).perform(click())
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
