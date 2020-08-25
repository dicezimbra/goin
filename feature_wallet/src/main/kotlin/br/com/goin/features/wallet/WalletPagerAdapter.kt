package br.com.goin.features.wallet

import android.content.Context
import androidx.fragment.app.FragmentStatePagerAdapter
import br.com.goin.feature.wallet.R
import br.com.goin.features.wallet.model.Ticket
import br.com.goin.features.wallet.ticket.TicketFragment

private const val TICKET = "TICKET"
private const val VOUCHER = "VOUCHER"
private const val ALL = "ALL"

class WalletPagerAdapter(private val context: Context,
                         var tickets: Map<String, List<Ticket>>,
                         fm: androidx.fragment.app.FragmentManager?) : FragmentStatePagerAdapter(fm) {

    override fun getItem(position: Int): androidx.fragment.app.Fragment {
        val tickets = getListByPosition(position)
        return TicketFragment.newInstance(ArrayList(tickets))
    }

    override fun getCount(): Int {
        return 3
    }

    override fun getPageTitle(position: Int): String {
        return when(position){
            0 -> context.getString(R.string.text_tab_wallet_all)
            1 -> context.getString(R.string.text_tab_wallet_voucher)
            2 -> context.getString(R.string.text_tab_wallet_ticket)
            else -> ""
        }
    }

    private fun getListByPosition(position: Int): List<Ticket>{
        return when(position){
            0 -> tickets.getValue(ALL)
            1 -> tickets.getValue(VOUCHER)
            2 -> tickets.getValue(TICKET)
            else -> arrayListOf()
        }
    }

    override fun getItemPosition(`object`: Any): Int {
        return POSITION_NONE
    }
}