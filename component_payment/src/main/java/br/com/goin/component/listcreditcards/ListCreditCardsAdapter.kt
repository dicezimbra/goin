package br.com.goin.component.listcreditcards

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.goin.component.payment.CreditCardHelper
import br.com.goin.component.payment.CreditCardModel
import br.com.goin.component.payment.R
import kotlinx.android.synthetic.main.item_credit_card.view.*

class ListCreditCardsAdapter(val creditCards: List<CreditCardModel>): RecyclerView.Adapter<ListCreditCardsAdapter.CreditCardModelViewHolder>()  {

    var onItemSelected: ( (CreditCardModel) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CreditCardModelViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_credit_card, parent, false)
        return CreditCardModelViewHolder(view)
    }

    override fun getItemCount(): Int {
        return creditCards.size
    }

    override fun onBindViewHolder(holder: CreditCardModelViewHolder, position: Int) {
        holder.bind(creditCards[position])
    }

    inner class CreditCardModelViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val number = view.number
        val label1 = view.label1
        val card_image = view.card_image
        val constraint_my_tickets = view.constraint_my_tickets

        fun bind(creditCard: CreditCardModel){
            number.text = CreditCardHelper.formatForViewingXXXX(creditCard.number ?: "", creditCard.creditCardType)
            label1.text = "Cartão final"

            if(creditCard.creditCardType == null || creditCard.creditCardType == CreditCardHelper.CardType.INVALID){
                creditCard.number?.let {
                    creditCard.creditCardType = CreditCardHelper.findCardType(it)
                }
            }

            creditCard.creditCardType?.frontResource?.let {
                card_image.setImageResource(it)
            }

            constraint_my_tickets.setOnClickListener{
                onItemSelected?.invoke(creditCard)
            }
        }
    }
}