package br.com.goin.feature.notifications

import android.content.Intent
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import br.com.goin.base.utils.Constants
import br.com.goin.component.navigation.NavigationController
import br.com.goin.feature.notifications.helpers.MockControllersHelper
import br.com.goin.feature.notifications.mocks.NotificationsMocked
import br.com.goin.feature.notifications.model.NotificationApi
import br.com.goin.feature.notifications.model.NotificationInteractor
import br.com.goin.feature.notifications.model.NotificationInteractorImpl
import br.com.goin.feature.notifications.retrofitService.TestRetrofitService
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito

@RunWith(AndroidJUnit4::class)
class NotificationsActivityTest {

    @Rule
    @JvmField
    var activityTestRule = ActivityTestRule(NotificationsActivity::class.java, false, false)

    @Test
    fun should_Have_Appeared_Loading() {

        //Given
        setupMock()

        //Then
        startActivityLaunch()

        //Should
        Espresso.onView(ViewMatchers.withId(R.id.progress_bar)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun should_Have_Go_To_User_Profile() {
        val ricardoUserId = "us-east-1:e2b3fc08-bb18-408f-9f0d-4b9619ad24ca"
        val currentUserId: String? = null

        //Given
        setupMock()
        MockControllersHelper.legacyController()

        val intent = Intent()
        intent.putExtra("targetId", ricardoUserId)
        intent.putExtra("currentUserId", currentUserId)

        //Then
        startActivityLaunchIntent(intent)
        onRecyclerVIewClicked(0)

        //Should
        mockVerifyProfile(ricardoUserId, currentUserId)

    }

    @Test
    fun should_Have_Go_To_Event() {
        val eventId = "EVENTBRITE/54659339556"
        val clubId: String? = null

        //Given
        setupMock()
        MockControllersHelper.eventController()

        val intent = Intent()
        intent.putExtra("eventId", eventId)
        intent.putExtra("clubId", clubId)

        //Then
        startActivityLaunchIntent(intent)
        onRecyclerVIewClicked(1)

        //Should
        mockVerifyEvent(eventId, clubId)
    }

    @Test
    fun should_Have_Go_To_Club() {
        val eventId: String? = null
        val clubId: String? = "f62e52bc-9f35-4988-8a9f-61761c99eb4c"

        //Given
        setupMock()
        MockControllersHelper.eventController()

        val intent = Intent()
        intent.putExtra("eventId", eventId)
        intent.putExtra("clubId", clubId)

        //Then
        startActivityLaunchIntent(intent)
        onRecyclerVIewClicked(1)

        //Should
        mockVerifyClub(eventId, clubId)
    }

    @Test
    fun should_Have_Go_To_Commentary() {
        val postId: String? = "1541907379468/us-east-1:a9e2ff6a-06a0-47b1-bbd9-f49f891bae8f"

        //Given
        setupMock()
        MockControllersHelper.legacyController()

        val intent = Intent()
        intent.putExtra("postId", postId)

        //Then
        startActivityLaunchIntent(intent)
        onRecyclerVIewClicked(3)

        //Should
        mockVerifyComment(postId)
    }

    @Test
    fun should_Have_Go_To_Post_Liked() {
        val postId = "1542389238928/us-east-1:a9e2ff6a-06a0-47b1-bbd9-f49f891bae8f"

        //Given
        setupMock()
        MockControllersHelper.legacyController()

        val intent = Intent()
        intent.putExtra("postId", postId)

        //Then
        startActivityLaunchIntent(intent)
        onRecyclerVIewClicked(5)

        //Should
        mockVerifyLikes(postId)
    }

    private fun onRecyclerVIewClicked(position: Int) {
        onView(withId(R.id.recycler_view)).perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(position, ViewActions.click()))
    }

    private fun mockVerifyProfile(userId: String, currentUserId: String?) {
        Mockito.verify(NavigationController.instance?.legacyApp())?.goToProfile(activityTestRule.activity,
                userId,
                currentUserId)

    }

    private fun mockVerifyEvent(eventId: String?, clubId: String?) {
        Mockito.verify(NavigationController.instance?.eventModule())?.goToEvent(activityTestRule.activity,
                eventId,
                clubId)
    }

    private fun mockVerifyClub(eventId: String?, clubId: String?) {
        Mockito.verify(NavigationController.instance?.eventModule())?.goToEvent(activityTestRule.activity,
                eventId,
                clubId)
    }

    private fun mockVerifyComment(postId: String?) {
        Mockito.verify(NavigationController.instance?.legacyApp()?.goToComments(activityTestRule.activity,
                postId))
    }

    private fun mockVerifyLikes(postId: String) {
        Mockito.verify(NavigationController.instance?.legacyApp()?.goToPostLikes(activityTestRule.activity,
                postId))
    }

    private fun startActivityLaunchIntent(intent: Intent) {
        activityTestRule.launchActivity(intent)
    }

    private fun setupMock() {
        val retrofitServiceTest = TestRetrofitService(NotificationApi::class.java, BuildConfig.GRAPHQL_BASE_URL, TestRetrofitService.OfflineMockInterceptor())
        (NotificationInteractor.instance as NotificationInteractorImpl).repository.service = retrofitServiceTest
        TestRetrofitService.mockedResponsesHttpCodes.add(200)
        TestRetrofitService.mockedResponsesHttpCodes.add(500)
        TestRetrofitService.mockedResponses.add(NotificationsMocked.usersQuery)
        TestRetrofitService.mockedResponses.add(NotificationsMocked.eventsQuery)
        TestRetrofitService.mockedResponses.add(NotificationsMocked.notifcationsQuery)
        TestRetrofitService.mockedResponses.add(NotificationsMocked.commentariesQuery)
        TestRetrofitService.mockedResponses.add(NotificationsMocked.postLikers)
    }

    private fun startActivityLaunch() {
        activityTestRule.launchActivity(Intent())
    }

}