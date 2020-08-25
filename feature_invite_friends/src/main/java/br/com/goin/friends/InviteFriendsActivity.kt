package br.com.goin.friends

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.goin.base.extensions.dpToPx
import br.com.goin.base.extensions.gone
import br.com.goin.base.extensions.visible
import br.com.goin.component.analytics.analytics.Analytics
import br.com.goin.friends.model.FriendToInvite
import com.jakewharton.rxbinding3.appcompat.queryTextChanges
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import kotlinx.android.synthetic.main.activity_invite_friends.*
import java.util.concurrent.TimeUnit

class InviteFriendsActivity : AppCompatActivity(), InviteFriendsView{

    private val presenter: InviteFriendsPresenter = InviteFriendsPresenterImpl(this)
    private var adapter: InviteFriendAdapter? = null
    private var disposable = CompositeDisposable()

    companion object {
        private const val EVENT_ID = "EVENT_ID"

        fun starter(context: Context, eventId: String){
            val intent = Intent(context, InviteFriendsActivity::class.java)
            val b = Bundle()
            b.putString(EVENT_ID, eventId)
            intent.putExtras(b)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_invite_friends)

        presenter.onReceivedEventId(intent?.getStringExtra(EVENT_ID))
        presenter.onCreate()
    }

    override fun onDestroy(){
        super.onDestroy()
        disposable.dispose()
        presenter.onDestroy()
    }

    override fun onResume() {
        super.onResume()
        Analytics.instance.screenView(this, getString(R.string.invite_friends_screen_name))
    }

    override fun configureToolbar() {
        toolbar.setTitle(R.string.invite_button)
        toolbar.setNavigationOnClickListener { finish() }
    }

    override fun configureSearchView(){
        search_view.queryTextChanges()
                .skipInitialValue()
                .debounce(500, TimeUnit.MILLISECONDS)
                .subscribe {
                    runOnUiThread {
                        adapter?.filter?.filter(it)
                    }
                }.addTo(disposable)
    }

    override fun configureRecyclerView(){
        recyclerViewFriends.layoutManager = LinearLayoutManager(this)
        recyclerViewFriends.addItemDecoration(SpaceItemDecoration(10.dpToPx()))

        val animation = AnimationUtils.loadLayoutAnimation(this, br.com.goin.component.ui.kit.R.anim.layout_animation)
        recyclerViewFriends.layoutAnimation = animation
    }

    override fun updateInviteFriendItem(){
        adapter?.notifyDataSetChanged()
    }

    override fun receiveFriendsToInvite(friends: MutableList<FriendToInvite>) {
        adapter = InviteFriendAdapter(friends)
        adapter?.onClickInvite = {
            presenter.inviteFriend(it)
        }

        recyclerViewFriends.adapter = adapter
        eventNoFriendsToInvite.gone()
        recyclerViewFriends.visible()
    }

    override fun configureEmptyView() {
        eventNoFriendsToInvite.visible()
        recyclerViewFriends.gone()
    }

    override fun showLoading() {
        progressBar.visible()
    }

    override fun hideLoading() {
        progressBar.gone()
    }

    override fun inviteFrendsSuccess() {
        presenter.onRefresh()
    }
}