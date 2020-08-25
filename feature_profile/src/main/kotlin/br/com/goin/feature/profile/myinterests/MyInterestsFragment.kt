package br.com.goin.feature.profile.myinterests

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.goin.base.extensions.gone
import br.com.goin.base.extensions.visible
import br.com.goin.component.model.event.Event
import br.com.goin.component.model.event.api.Interrest
import br.com.goin.component.navigation.NavigationController
import br.com.goin.feature.profile.R
import kotlinx.android.synthetic.main.fragment_my_interests.*

class MyInterestsFragment : Fragment(), MyInterestView {

    private val presenter: MyInterestPresenter = MyInterestPresenterImpl(this)

    companion object {
        private const val PROFILE_ID = "PROFILE_ID"
        fun newInstance(profileId: String): Fragment {
            val bundle = Bundle()
            bundle.putString(PROFILE_ID, profileId)
            val fragment = MyInterestsFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_my_interests, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.onReceivedProfileId(arguments?.getString(PROFILE_ID))
        presenter.onCreate()
    }

    override fun configureMyInterests(interrest: List<Interrest>) {
        val adapter = MyInterestAdapter(interrest)
        event_no_my_events.gone()
        event_my_events_view.visible()

        event_my_events_view.layoutManager = LinearLayoutManager(context)
        event_my_events_view.adapter = adapter

        adapter.onClickListener = {
            when(it.action){
                "CLUB" -> {
                    NavigationController.instance?.placeModule()?.goToPlace(activity!!, it.actionValue, "")
                }
                "EVENT" -> {
                    NavigationController.instance?.eventModule()?.goToEvent(activity!!, it.actionValue)
                }
            }
        }
    }

    override fun showEmptyView() {
        event_no_my_events.visible()
        event_my_events_view.gone()
    }

    override fun showLoading() {
        progress.visible()
        event_no_my_events.gone()
        event_my_events_view.gone()
    }

    override fun hideLoading() {
        if (progress != null) {
            progress.gone()
        }
    }
}