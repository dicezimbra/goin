package br.com.goin.component.listadresses

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.goin.component.address.AddressModel
import br.com.goin.component.payment.R
import kotlinx.android.synthetic.main.item_address_list.view.*

class ListAdressesAdapter(val adresses: List<AddressModel>): RecyclerView.Adapter<ListAdressesAdapter.ListAdressesViewHolder>()  {

    var onItemSelected: ( (AddressModel) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListAdressesViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_address_list, parent, false)
        return ListAdressesViewHolder(view)
    }

    override fun getItemCount(): Int {
        return adresses.size
    }

    override fun onBindViewHolder(holder: ListAdressesViewHolder, position: Int) {
        holder.bind(adresses[position])
    }

    inner class ListAdressesViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val address = view.address
        val label = view.label
        val fullItem = view.constraint_my_tickets

        fun bind(addressModel: AddressModel){
            val stringBuilder = StringBuilder()

            addressModel.logradouro?.let {
                stringBuilder.append(it)
            }
            addressModel.complemento?.let {
                stringBuilder.append(", ")
                stringBuilder.append(it)
            }
            addressModel.bairro?.let {
                stringBuilder.append(" - ")
                stringBuilder.append(it)
            }
            addressModel.localidade?.let {
                stringBuilder.append("\n")
                stringBuilder.append(it)
            }
            addressModel.uf?.let {
                stringBuilder.append(" - ")
                stringBuilder.append(it)
            }
            addressModel.cep?.let {
                stringBuilder.append(", ")
                stringBuilder.append(it)
            }

            address.text = stringBuilder.toString()
            label.text = addressModel.label

            fullItem.setOnClickListener{
                onItemSelected?.invoke(addressModel)
            }

        }
    }
}