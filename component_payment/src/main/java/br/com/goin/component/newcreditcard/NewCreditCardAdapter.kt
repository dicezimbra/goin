package br.com.goin.component.newcreditcard

import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jakewharton.rxbinding3.widget.textChanges
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.item_address_view_pager.view.*
import androidx.viewpager.widget.ViewPager
import br.com.goin.component.payment.R
import android.text.InputFilter
import android.text.method.DigitsKeyListener
import br.com.goin.component.payment.CreditCardHelper
import br.com.goin.component.payment.CreditCardModel
import com.github.rtoshiro.util.format.SimpleMaskFormatter
import com.github.rtoshiro.util.format.text.MaskTextWatcher

class NewCreditCardAdapter(private val arrayTitles: Array<String>, var model: CreditCardModel) : androidx.viewpager.widget.PagerAdapter() {

    var onFieldIsValid: (Boolean) -> Unit = {}
    var onNumberTextChange: (String, CreditCardHelper.CardType) -> Unit = { s: String, cardType: CreditCardHelper.CardType -> }
    var onNameTextChange: (String) -> Unit = {}
    var onExpirationTextChange: (String) -> Unit = {}
    var onCVVTextChange: (String) -> Unit = {}
    var onBirthdayTextChange: (String) -> Unit = {}
    var onCPFTextChange: (String) -> Unit = {}


    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val context = container.context
        val itemView = LayoutInflater.from(context).inflate(R.layout.item_card_view_pager, container, false)
        itemView.input_layout_address_component.hint = arrayTitles[position]

        onFieldIsValid.invoke(!itemView.field_address_component.text.isNullOrEmpty())


        when (position) {
            0 -> {
                configNumber(itemView, model)
            }
            1 -> {
                configName(itemView, model)
            }
            2 -> {
                configExpiration(itemView, model)
            }
            3 -> {
                configCVV(itemView, model)
            }
            4 -> {
                configCPF(itemView, model)
            }
            5 ->{
                configBirthday(itemView, model)
            }
        }

        container.addView(itemView)
        return itemView
    }

    override fun getCount(): Int {
        return arrayTitles.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }

    override fun destroyItem(container: ViewGroup, position: Int, viewObject: Any) {
        val view = viewObject as View
        (view.tag as? Disposable)?.dispose()
        (container as ViewPager).removeView(view)
    }

    private fun configCPF(itemView: View, model: CreditCardModel){
        val maxLength = 14
        val fArray = arrayOfNulls<InputFilter>(1)
        fArray[0] = InputFilter.LengthFilter(maxLength)

        itemView.field_address_component.inputType = InputType.TYPE_CLASS_NUMBER
        itemView.field_address_component.maxLines = 1
        itemView.field_address_component.filters = fArray
        itemView.field_address_component.setText(model.cpf)

        val smf = SimpleMaskFormatter("NNN.NNN.NNN-NN")
        val mtw = MaskTextWatcher(itemView.field_address_component, smf)
        itemView.field_address_component.addTextChangedListener(mtw)


        val diposable = itemView.field_address_component
                .textChanges()
                .skipInitialValue()
                .subscribe {
                    onCPFTextChange.invoke(it.toString())
                }

        itemView.tag = diposable

    }

    private fun configNumber(itemView: View, model: CreditCardModel) {

        itemView.field_address_component.setText(model.number)


        itemView.field_address_component.inputType = InputType.TYPE_CLASS_NUMBER
        itemView.field_address_component.maxLines = 1
        itemView.field_address_component.keyListener = DigitsKeyListener.getInstance("0123456789")


        val mtw = MaskTextWatcher(itemView.field_address_component, CreditCardHelper.getMask("",CreditCardHelper.CardType.INVALID))
        itemView.field_address_component.addTextChangedListener(mtw)

        val maxLength = 20
        val fArray = arrayOfNulls<InputFilter>(1)
        fArray[0] = InputFilter.LengthFilter(maxLength)
        itemView.field_address_component.filters = fArray

        val diposable = itemView.field_address_component
                .textChanges()
                .skipInitialValue()
                .subscribe {
                    var cardNumber = it.toString()
                    var type= CreditCardHelper.findCardType(cardNumber)
                    var cardNumberFormatted = CreditCardHelper.formatForViewing(cardNumber, type)

                    onFieldIsValid.invoke(CreditCardHelper.isValidNumber(cardNumberFormatted))


                    if(type == CreditCardHelper.CardType.INVALID){
                        onNumberTextChange.invoke(cardNumber, type)
                        return@subscribe
                    }else{
                        onNumberTextChange.invoke(cardNumberFormatted, type)
                    }
                }

        itemView.tag = diposable
    }

    private fun configBirthday(itemView: View, model: CreditCardModel){
        val maxLength = 10
        val fArray = arrayOfNulls<InputFilter>(1)
        fArray[0] = InputFilter.LengthFilter(maxLength)

        itemView.field_address_component.inputType = InputType.TYPE_CLASS_NUMBER
        itemView.field_address_component.maxLines = 1
        itemView.field_address_component.filters = fArray
        itemView.field_address_component.setText(model.birthday)


        val smf = SimpleMaskFormatter("NN/NN/NNNN")
        val mtw = MaskTextWatcher(itemView.field_address_component, smf)
        itemView.field_address_component.addTextChangedListener(mtw)


        val diposable = itemView.field_address_component
                .textChanges()
                .skipInitialValue()
                .subscribe {
                    onBirthdayTextChange.invoke(it.toString())
                }

        itemView.tag = diposable

    }

    private fun configName(itemView: View, model: CreditCardModel) {
        itemView.field_address_component.setText(model.name)
        itemView.field_address_component.inputType = InputType.TYPE_CLASS_TEXT
        itemView.field_address_component.maxLines = 1
        itemView.field_address_component.isAllCaps = true


        val diposable = itemView.field_address_component
                .textChanges()
                .skipInitialValue()
                .subscribe {
                    onNameTextChange.invoke(it.toString())
                }

        itemView.tag = diposable
    }

    private fun configExpiration(itemView: View, model: CreditCardModel) {
        itemView.field_address_component.setText(model.expireDate)
        itemView.field_address_component.inputType = InputType.TYPE_CLASS_NUMBER
        itemView.field_address_component.maxLines = 1
        itemView.field_address_component.keyListener = DigitsKeyListener.getInstance("0123456789")

        val smf = SimpleMaskFormatter("NN/NN")
        val mtw = MaskTextWatcher(itemView.field_address_component, smf)
        itemView.field_address_component.addTextChangedListener(mtw)

        val maxLength = 5
        val fArray = arrayOfNulls<InputFilter>(1)
        fArray[0] = InputFilter.LengthFilter(maxLength)
        itemView.field_address_component.filters = fArray


        val diposable = itemView.field_address_component
                .textChanges()
                .skipInitialValue()
                .subscribe {
                    onExpirationTextChange.invoke(it.toString())
                }

        itemView.tag = diposable
    }


    private fun configCVV(itemView: View, model: CreditCardModel) {
        itemView.field_address_component.setText(model.cvv)
        itemView.field_address_component.inputType = InputType.TYPE_CLASS_NUMBER
        itemView.field_address_component.maxLines = 1
        itemView.field_address_component.keyListener = DigitsKeyListener.getInstance("0123456789")

        val smf = SimpleMaskFormatter("NNN")
        val mtw = MaskTextWatcher(itemView.field_address_component, smf)
        itemView.field_address_component.addTextChangedListener(mtw)

        val maxLength = 3
        val fArray = arrayOfNulls<InputFilter>(1)
        fArray[0] = InputFilter.LengthFilter(maxLength)
        itemView.field_address_component.filters = fArray


        val diposable = itemView.field_address_component
                .textChanges()
                .skipInitialValue()
                .subscribe {
                    onCVVTextChange.invoke(it.toString())
                }

        itemView.tag = diposable
    }
}