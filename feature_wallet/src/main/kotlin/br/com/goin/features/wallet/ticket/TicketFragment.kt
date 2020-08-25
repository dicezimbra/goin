package br.com.goin.features.wallet.ticket

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import br.com.goin.base.extensions.dpToPx
import br.com.goin.base.utils.Constants
import br.com.goin.component.ui.kit.features.error.ErrorViewHelper
import br.com.goin.component.ui.kit.views.SpaceItemDecoration
import br.com.goin.feature.wallet.R
import br.com.goin.features.fullticket.FullticketActivity
import br.com.goin.features.wallet.model.Ticket
import br.com.goin.features.wallet.model.Validate
import br.com.goin.features.wallet.model.Validate.*
import kotlinx.android.synthetic.main.fragment_ticket.*

class TicketFragment : androidx.fragment.app.Fragment(), TicketsView {

    private var presenter: TicketsPresenter = TicketPresenterImpl(this)
    private var adapter: TicketsAdapter? = null

    companion object {
        private const val TICKETS = "TICKETS"

        fun newInstance(tickets: ArrayList<Ticket>): TicketFragment {
            val extras = Bundle()
            extras.putSerializable(TICKETS, tickets)

            val ticketFragment = TicketFragment()
            ticketFragment.arguments = extras
            return ticketFragment
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter.onDestroy()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_ticket, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        presenter.onReceiveTickets(arguments?.getSerializable(TICKETS))
        presenter.onCreate()
    }

    override fun configureRecyclerView() {
        adapter = TicketsAdapter()
        adapter?.onClickListener = { ticket ->
            when(ticket.validate){
                EXPIRED -> {
                    showPastTicketDialog()
                }
                USED -> {
                    showUsedTicketDialog()
                }
                else -> {
                    FullticketActivity.starter(context!!, ticket)
                }
            }
        }

        val layoutManager = androidx.recyclerview.widget.LinearLayoutManager(context)
        newRecyclerViewWallet?.layoutManager = layoutManager
        newRecyclerViewWallet.addItemDecoration(SpaceItemDecoration(10.dpToPx()))
        newRecyclerViewWallet?.adapter = adapter
    }

    override fun onTicketLoaded(tickets: List<Ticket>) {
        adapter?.apply {
            this.tickets = tickets
            this.notifyDataSetChanged()
        }

        empty_list?.visibility = View.GONE
        newRecyclerViewWallet?.visibility = View.VISIBLE
    }

    override fun configureEmptyState() {
        empty_list?.visibility = View.VISIBLE
        newRecyclerViewWallet?.visibility = View.GONE
    }

    override fun hideLoading() {
        progressBar.visibility = View.GONE
    }

    override fun showLoading() {
        progressBar.visibility = View.GONE
    }

    override fun handleError(throwable: Throwable){
        ErrorViewHelper.handleErrorView(container, throwable){
            presenter.onCreate()
        }
    }

    private fun showUsedTicketDialog() {
        val view = View.inflate(context!!, R.layout.dialog_used_ticket, null)
        val builder = AlertDialog.Builder(context!!).setView(view)
        builder.setNegativeButton(getString(R.string.close)) { dialog, which ->
            dialog.dismiss()
        }
        val dialog = builder.create()
        dialog.show()

        val buttonNegative = dialog.getButton(DialogInterface.BUTTON_NEGATIVE)
        buttonNegative.setTextColor(ContextCompat.getColor(context!!, R.color.black))
    }

    private fun showPastTicketDialog() {
        val view = View.inflate(context!!, R.layout.dialog_past_ticket, null)
        val builder = AlertDialog.Builder(context!!).setView(view)
        builder.setNegativeButton(getString(R.string.close)) { dialog, which ->
            dialog.dismiss()
        }
        val dialog = builder.create()
        dialog.show()

        val buttonNegative = dialog.getButton(DialogInterface.BUTTON_NEGATIVE)
        buttonNegative.setTextColor(ContextCompat.getColor(context!!, R.color.black))
    }
}