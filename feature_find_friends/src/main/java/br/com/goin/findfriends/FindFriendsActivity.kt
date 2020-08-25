package br.com.goin.findfriends

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.AnimationUtils
import br.com.goin.base.extensions.dpToPx
import br.com.goin.base.extensions.gone
import br.com.goin.base.extensions.visible
import br.com.goin.component.analytics.analytics.Analytics
import br.com.goin.component.navigation.NavigationController
import br.com.goin.component.session.user.ActionType
import br.com.goin.component.session.user.UserModel
import br.com.goin.findfriends.adapter.FindFriendsClickListener
import br.com.goin.findfriends.adapter.FriendsInviteListAdapter
import br.com.goin.findfriends.adapter.SpaceItemDecoration
import com.google.android.material.snackbar.Snackbar
import com.jakewharton.rxbinding3.appcompat.queryTextChanges
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import kotlinx.android.synthetic.main.activity_find_friends.*
import kotlinx.android.synthetic.main.item_search_field.*
import java.util.concurrent.TimeUnit

class FindFriendsActivity : AppCompatActivity(), FindFriendsView, FindFriendsClickListener {

    private val presenter: FindFriendsPresenterImpl = FindFriendsPresenterImpl(this)
    private val disposables = CompositeDisposable()
    var adapter: FriendsInviteListAdapter? = null
    var currentUserId: String? = null

    private val navigationController = NavigationController.instance

    companion object {
        fun starter(context: Context) {
            Intent(context, FindFriendsActivity::class.java).apply {
                context.startActivity(this)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_find_friends)
        presenter.onCreate()
    }

    override fun onResume() {
        super.onResume()
        Analytics.instance.screenView(this, getString(R.string.find_friends_screen_name))
    }

    override fun onDestroy() {
        super.onDestroy()
        disposables.dispose()
    }

    override fun setupToolbar() {
        val toolbarText: String = getString(R.string.find_friends)
        findFriendsToolbar.title = toolbarText
        findFriendsToolbar.setOnBackButtonClicked { onBackPressed() }
    }

    override fun configureSearch() {
        searchBar.queryTextChanges()
                .skipInitialValue()
                .debounce(500, TimeUnit.MILLISECONDS)
                .subscribe { searchInput: CharSequence ->
                    runOnUiThread { onPrepareSearch(searchInput) }
                }.addTo(disposables)
    }

    private fun onPrepareSearch(searchInput: CharSequence) {
        if (searchInput.toString().isNotBlank()) {
            presenter.onSearchFriend(searchInput.toString())
        }
    }

    override fun configureRecyclerView() {
        findFriendsList.isVerticalScrollBarEnabled = false
        findFriendsList.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this)
    }

    override fun configureFriendsList(friendsList: List<UserModel>) {
        if (adapter == null) {
            setupAdapter(friendsList)
        }
        bindListToAdapter(friendsList)
    }

    private fun setupAdapter(friendsList: List<UserModel>) {
        adapter = FriendsInviteListAdapter(friendsList.toMutableList(), this, currentUserId)
        findFriendsList.adapter = adapter

        findFriendsList.addItemDecoration(SpaceItemDecoration(10.dpToPx()))

        val animation = AnimationUtils.loadLayoutAnimation(this, br.com.goin.component.ui.kit.R.anim.layout_animation)
        findFriendsList.layoutAnimation = animation
    }

    private fun bindListToAdapter(friendsList: List<UserModel>) {
        val userList = friendsList.toMutableList()
        for (userModel in userList) {
            userModel.actionType = ActionType.Follow
        }
        adapter?.setUserList(userList)
    }

    override fun showLoading() {
        findFriendsPlaceholder.gone()
        findFriendsList.gone()
        findFriendsLoading.visible()
    }

    override fun hideLoading() {
        findFriendsLoading.gone()
    }

    override fun showSnackBarOnError() {
        Snackbar.make(rootView, getString(R.string.service_error), Snackbar.LENGTH_SHORT)
    }

    override fun showEmptyMessage() {
        findFriendsPlaceholder.visible()
    }

    override fun showFriendsList() {
        findFriendsList.visible()
    }

    override fun changeUserStatus(userId: String?, followStatus: Boolean) {
        val index = adapter?.users?.indexOfFirst { it.id == userId }
        if (index != null && index != -1) {
            adapter?.users?.get(index)?.followedByMe = followStatus
            adapter?.notifyItemChanged(index)
        }
    }

    override fun onClickFollow(userId: String) {
        presenter.logOnAnalytics(
                getString(R.string.analytics_findFriends_screen_name),
                getString(R.string.analytics_findFriends_category),
                getString(R.string.analytics_findFriends_follow_action))
        presenter.followUser(userId)
    }

    override fun onClickUnfollow(userId: String) {
        presenter.unfollowUser(userId)
    }

    override fun onClickUser(userId: String, currentUserId: String?) {
        navigationController?.profileModule()?.goToProfile(this, userId)
    }
}
