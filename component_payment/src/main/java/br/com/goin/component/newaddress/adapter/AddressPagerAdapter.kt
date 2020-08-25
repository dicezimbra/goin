package br.com.goin.component.newaddress.adapter

import android.content.Context
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.com.goin.component.address.AddressModel
import com.github.rtoshiro.util.format.SimpleMaskFormatter
import com.github.rtoshiro.util.format.text.MaskTextWatcher
import com.jakewharton.rxbinding3.widget.textChanges
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.item_address_view_pager.view.*
import java.util.concurrent.TimeUnit
import androidx.viewpager.widget.ViewPager
import br.com.goin.component.payment.R

class AddressPagerAdapter(context: Context, var addressItems: AddressModel?, var addressNick: String?) : androidx.viewpager.widget.PagerAdapter() {

    private var arrayTitle: Array<String>? = null
    var onFieldIsValid: (Boolean) -> Unit = {}
    var onZipcodeTextChanges: (String) -> Unit = {}
    var onNicknameTextChanges: (String) -> Unit = {}
    var onAddonTextChanges: (String) -> Unit = {}
    var onNeighborhoodTextChanges: (String) -> Unit = {}
    var onNumberTextChanges: (String) -> Unit = {}
    var onThroughfarTextChanges: (String) -> Unit = {}
    init {
        arrayTitle = context.resources.getStringArray(R.array.address_view_pager_array)
    }

    override fun getItemPosition(`object`: Any): Int {
        return POSITION_NONE
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val context = container.context
        val itemView = LayoutInflater.from(context).inflate(R.layout.item_address_view_pager, container, false)

        itemView.input_layout_address_component.hint = arrayTitle?.get(position)



        when (position) {
            0 -> {
                configNickname(itemView, addressNick)
            }
            1 -> {
                configZipcode(itemView, addressItems)
            }
            2 -> {
                configThroughfare(itemView, addressItems)
            }
            3 -> {
                configNumber(itemView, addressItems)
            }
            4 -> {
                configNeighborhood(itemView, addressItems)
            }
            5 -> {
                configAddon(itemView, addressItems)
            }
        }

        container.addView(itemView)

        return itemView
    }

    override fun getCount(): Int {
        return arrayTitle?.size!!
    }


    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }

    override fun destroyItem(container: ViewGroup, position: Int, viewObject: Any) {
        val view = viewObject as View
        (view.tag as? Disposable)?.dispose()
        (container as ViewPager).removeView(view)

    }

    private fun configZipcode(itemView: View, addressItems: AddressModel?) {

        if (addressItems != null) {
            itemView.field_address_component.setText(addressItems.cep)
        }

        itemView.field_address_component.inputType = InputType.TYPE_CLASS_NUMBER
        itemView.field_address_component.maxLines = 9

        val smf = SimpleMaskFormatter("NNNNN-NNN")
        val mtw = MaskTextWatcher(itemView.field_address_component, smf)

        itemView.field_address_component.addTextChangedListener(mtw)

        val diposable = itemView.field_address_component
                .textChanges()
                .skipInitialValue()
                .doOnNext { }
                .debounce(500, TimeUnit.MILLISECONDS)
                .subscribe {
                    if (it.length == 9) {
                        onZipcodeTextChanges.invoke(it.toString())
                    }
                }

        itemView.tag = diposable
    }

    private fun configNickname(itemView: View, addressNick: String?) {

        if (addressNick != null) {
            itemView.field_address_component.setText(addressNick)
        }


        itemView.field_address_component.requestFocus()
        itemView.field_address_component.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD shl InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS


        val diposable = itemView.field_address_component
                .textChanges()
                .skipInitialValue()
                .doOnNext { }
                .debounce(500, TimeUnit.MILLISECONDS)
                .subscribe {
                    onNicknameTextChanges.invoke(it.toString())
                }
        itemView.tag = diposable
    }

    private fun configAddon(itemView: View, addressItems: AddressModel?) {

        itemView.field_address_component.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD shl InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS

        val diposable = itemView.field_address_component
                .textChanges()
                .skipInitialValue()
                .doOnNext { }
                .debounce(500, TimeUnit.MILLISECONDS)
                .subscribe {
                    onAddonTextChanges.invoke(it.toString())
                }
        itemView.tag = diposable
    }

    private fun configNeighborhood(itemView: View, addressItems: AddressModel?) {
        if (addressItems != null) {
            itemView.field_address_component.setText(addressItems.bairro)
        }


        itemView.field_address_component.inputType  = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD shl InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS

        val diposable = itemView.field_address_component
                .textChanges()
                .skipInitialValue()
                .doOnNext { }
                .debounce(500, TimeUnit.MILLISECONDS)
                .subscribe {
                    onNeighborhoodTextChanges.invoke(it.toString())
                }
        itemView.tag = diposable
    }

    private fun configNumber(itemView: View, addressItems: AddressModel?) {

        itemView.field_address_component.inputType = InputType.TYPE_CLASS_NUMBER

        val diposable = itemView.field_address_component
                .textChanges()
                .skipInitialValue()
                .doOnNext { }
                .debounce(500, TimeUnit.MILLISECONDS)
                .subscribe {
                    onNumberTextChanges.invoke(it.toString())
                }
        itemView.tag = diposable
    }

    private fun configThroughfare(itemView: View, addressItems: AddressModel?) {
        if (addressItems != null) {
            itemView.field_address_component.setText(addressItems.logradouro)
        }

        itemView.field_address_component.inputType  = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD shl InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS

        val diposable = itemView.field_address_component
                .textChanges()
                .skipInitialValue()
                .doOnNext { }
                .debounce(500, TimeUnit.MILLISECONDS)
                .subscribe {
                    onThroughfarTextChanges.invoke(it.toString())
                }
        itemView.tag = diposable
    }

}