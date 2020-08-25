package br.com.goin.feature_social

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import br.com.goin.base.extensions.gone
import br.com.goin.base.extensions.visible
import br.com.goin.component.analytics.analytics.Analytics
import br.com.goin.component.session.user.ActionType
import br.com.goin.component.session.user.UserModel
import br.com.goin.feature_social.adapter.FriendsInviteListAdapter
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_follow.*

class FollowActivity : AppCompatActivity(), FollowView {

    private lateinit var identifier: FollowRelation
    private val presenter: FollowPresenterImpl = FollowPresenterImpl(this)
    var userId: String? = null
    var currentUserId: String? = null
    var adapter: FriendsInviteListAdapter? = null

    companion object {
        fun starter(activity: Activity, userId: String, relation: FollowRelation, requestCode: Int) {
            Intent(activity, FollowActivity::class.java).apply {
                putExtra(userIdKey, userId)
                putExtra(followRelationKey, relation.name)
                if (requestCode > 0) {
                    activity.startActivityForResult(this, requestCode)
                } else {
                    activity.startActivity(this)
                }
            }
        }

        const val userIdKey = "targetId"
        const val currentUserIdKey = "currentUserId"
        const val followRelationKey = "followRelation"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_follow)
        intent.extras?.let {
            userId = it.getString(userIdKey)
            currentUserId = it.getString(currentUserIdKey)
            identifier = FollowRelation.valueOf(it.getString(followRelationKey)
                    ?: FollowRelation.FOLLOWERS.name)
        }
        presenter.onCreate()
    }

    override fun onResume() {
        super.onResume()
        when(identifier){
            FollowRelation.FOLLOWERS -> {
                Analytics.instance.screenView(this, getString(R.string.followers_screen_name))
            }
            FollowRelation.FOLLOWING -> {
                Analytics.instance.screenView(this, getString(R.string.following_screen_name))
            }
        }
    }

    override fun onStart() {
        super.onStart()
        presenter.loadUserModels(userId, identifier)
    }

    override fun setupToolbar() {
        val toolbarText: String = when (identifier) {
            FollowRelation.FOLLOWERS -> getString(R.string.followers)
            FollowRelation.FOLLOWING -> getString(R.string.following)
        }
        toolbar.title = toolbarText
        toolbar.setOnBackButtonClicked { onBackPressed() }
    }

    override fun configureRecyclerView() {
        friends_list.isVerticalScrollBarEnabled = false
        friends_list.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this)
    }

    override fun setupEmptyListMessage() {
        empty_message.text = when (identifier) {
            FollowRelation.FOLLOWERS -> getString(R.string.you_have_no_followers)
            FollowRelation.FOLLOWING -> getString(R.string.you_dont_follow_anyone)
        }
    }

    override fun configureUserList(list: List<UserModel>) {
        if (adapter == null) setupAdapter(list)

        if (list.isEmpty()) {
            showEmptyMessage()
        } else {
            showList()
            bindListToAdapter(list)
        }
    }

    private fun setupAdapter(list: List<UserModel>) {
        adapter = FriendsInviteListAdapter(list.toMutableList(), ActionType.Follow, currentUserId)
        adapter?.followUser = {
            when (identifier) {
                FollowRelation.FOLLOWERS -> {
                    presenter.logOnAnalytics(
                            getString(R.string.analytics_followers_screen_name),
                            getString(R.string.analytics_followers_category),
                            getString(R.string.analytics_followers_follow_action)
                    )
                }
                FollowRelation.FOLLOWING -> {
                    presenter.logOnAnalytics(
                            getString(R.string.analytics_following_screen_name),
                            getString(R.string.analytics_following_category),
                            getString(R.string.analytics_following_follow_action)
                    )
                }
            }
            presenter.followUser(it)
        }
        adapter?.unfollowUser = { presenter.unfollowUser(it) }

        friends_list.adapter = adapter
    }

    override fun changeUserStatus(userId: String?, followStatus: Boolean) {
        val index = adapter?.users?.indexOfFirst { it.id == userId }
        if (index != null && index != -1) {
            adapter?.users?.get(index)?.followedByMe = followStatus
            adapter?.notifyItemChanged(index)
        }
    }

    override fun showLoading() {
        friends_list.gone()
        following_followers_progress_bar.visible()
    }

    override fun hideLoading() {
        following_followers_progress_bar.gone()
        friends_list.visible()
    }

    override fun showSnackBarOnError() {
        Snackbar.make(following_followers_container, getString(R.string.service_error), Snackbar.LENGTH_LONG)
    }

    private fun showEmptyMessage() {
        empty_message.visible()
        friends_list.gone()
    }

    private fun showList() {
        empty_message.gone()
        friends_list.visible()
    }

    private fun bindListToAdapter(list: List<UserModel>) {
        val userList = list.toMutableList()
        for (userModel in userList) {
            userModel.actionType = ActionType.Follow
        }

        adapter?.setUserList(userList)
    }
}
