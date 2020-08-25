package br.com.goin.features.wallet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.ViewPager
import br.com.goin.base.extensions.gone
import br.com.goin.base.extensions.visible
import br.com.goin.component.analytics.analytics.Analytics
import br.com.goin.component.ui.kit.features.error.ErrorViewHelper
import br.com.goin.feature.wallet.R
import br.com.goin.features.wallet.model.Ticket
import kotlinx.android.synthetic.main.fragment_wallet.*

class WalletFragment : androidx.fragment.app.Fragment(), WalletView {

    companion object {
        fun newInstance() = WalletFragment()
    }

    private val presenter: WalletPresenter = WalletPresenterImpl(this)
    private var adapter: WalletPagerAdapter? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_wallet, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.onCreate()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.onDestroy()
    }

    override fun configureToolbar(){
        toolbar.hideBackButton()
        toolbar.title = getString(R.string.my_tickets_title)
    }

    override fun configureView(tickets: Map<String, List<Ticket>>){
        if(adapter == null) {
            adapter = WalletPagerAdapter(context!!, tickets, childFragmentManager)
            viewPager.adapter = adapter
            tabLayout.setupWithViewPager(viewPager)

            viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
                override fun onPageScrollStateChanged(state: Int) {
                }

                override fun onPageSelected(position: Int) {
                    when (position) {
                        0 -> {
                            Analytics.instance.screenView(activity!!, getString(R.string.wallet_all_screen_name))
                        }
                        1 -> {
                            Analytics.instance.screenView(activity!!, getString(R.string.wallet_voucher_screen_name))
                        }
                        2 -> {
                            Analytics.instance.screenView(activity!!, getString(R.string.wallet_tickets_screen_name))
                        }
                    }
                }

                override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                }
            })
        }else{
            adapter?.tickets = tickets
            adapter?.notifyDataSetChanged()
        }
    }

    override fun showLoading() {
        progress_bar.visible()
        viewPager.gone()
        tabLayout.gone()
    }

    override fun hideLoading() {
        progress_bar.gone()
        viewPager.visible()
        tabLayout.visible()
    }

    override fun handleError(throwable: Throwable){
        ErrorViewHelper.handleErrorView(error_container, throwable){
            presenter.onCreate()
        }
    }

    override fun onResume() {
        super.onResume()
        presenter.onResume()
        Analytics.instance.screenView(activity!!, getString(R.string.wallet_all_screen_name))
    }
}
