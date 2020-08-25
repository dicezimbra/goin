package br.com.goin.component.confirmation

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import br.com.goin.base.model.PaymentAddress
import br.com.goin.base.model.PaymentModel
import br.com.goin.base.utils.DateUtils
import br.com.goin.component.analytics.analytics.Analytics
import br.com.goin.component.payment.CreditCardHelper
import br.com.goin.component.payment.R
import br.com.goin.component.ui.kit.dialog.DialogUtils
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.activity_confirmation.*
import okhttp3.ResponseBody
import java.util.*

class ConfirmationActivity : AppCompatActivity(), ConfirmationView {


    private val presenter: ConfirmationPresenter = ConfirmationPresenterImpl(this)
    var dialogFragment: DialogUtils.AlertInputDialogFragment? = null

    companion object {
        val MODEL_PAYMENT = "MODEL_PAYMENT"
        fun starter(context: Context, paymentInfo: PaymentModel) {
            val it = Intent(context, ConfirmationActivity::class.java)
            it.putExtra(MODEL_PAYMENT, paymentInfo)
            context.startActivity(it)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_confirmation)
        presenter.onReceivePaymentModel(intent?.getSerializableExtra(MODEL_PAYMENT))
        presenter.onCreate()
        presenter.loadToolbar()

        Analytics.instance.screenViewRes(this, R.string.RESUMO_COMPRA)
    }

    override fun loadPaymentInfos(paymentModel: PaymentModel) {
        ticket_name.text = paymentModel.tickets[0].info.name
        ticket_date.text = DateUtils.convertCalendarToDateString(paymentModel.tickets[0].info.eventDate)
        ticket_value.text = String.format(Locale.getDefault(), "R$ %.2f", paymentModel.tickets[0].info.price)


        val creditCardType = CreditCardHelper.findCardType("${paymentModel.creditcard.number}")
        credit_card__number.text = CreditCardHelper.formatForViewingXXXX("${paymentModel.creditcard.number}", creditCardType)
        creditCardType?.frontResource?.let {
            card_image.setImageResource(it)
        }

        address_label.text = paymentModel.address.label
        address.text = getFullAddress(paymentModel.address)
    }

    private fun getFullAddress(address: PaymentAddress): CharSequence? {
        val stringBuilder = StringBuilder()

        address.street?.let {
            stringBuilder.append(it)
        }
        address.complement?.let {
            stringBuilder.append(", ")
            stringBuilder.append(it)
        }
        address.district?.let {
            stringBuilder.append(" - ")
            stringBuilder.append(it)
        }
        address.city?.let {
            stringBuilder.append("\n")
            stringBuilder.append(it)
        }
        address.state?.let {
            stringBuilder.append(" - ")
            stringBuilder.append(it)
        }
        address.zipcode?.let {
            stringBuilder.append(", ")
            stringBuilder.append(it)
        }

        return stringBuilder.toString()
    }

    override fun configureClick() {
        buy_button.setOnClickListener{
            presenter.getCvv()
        }
    }

    private var progressDialog: ProgressDialog? = null
    var dialog: DialogUtils.AlertDialogFragment? = null
    override fun handleError(t: Throwable?) {
        Analytics.instance.screenViewRes(this, R.string.RESUMO_COMPRA_ERRO)

        progressDialog?.let {
            it.hide()
            progressDialog = null
        }
        t?.let {
            dialog = DialogUtils.showError(this, "Erro ao processar pagamento" , R.drawable.icon_cloud_error, R.string.error){
                presenter.finishPurchase()
                dialog?.dismiss()
            }

        }
    }

    override fun hideLoading() {
        progressDialog?.let {
            it.hide()
            progressDialog = null
        }
    }

    override fun showLoading() {
        progressDialog?.let {
            it.dismiss()
        }

        progressDialog = DialogUtils.createProgressDialog(this)
        progressDialog?.show()
    }

    override fun showSuccess(it: ResponseBody?) {
        Analytics.instance.screenViewRes(this, R.string.RESUMO_COMPRA_SUCESSO)
        DialogUtils.showSuccess(this, R.string.success_payment, R.drawable.icon_invoice_success, R.string.success_payment_title){
            this@ConfirmationActivity.finish()
        }
    }

    override fun getCVV() {

        var description = getString(R.string.input_payment, presenter.getCCLasat4Numbers())

        dialogFragment = DialogUtils.showInput(this, description, R.drawable.icon_mask_cvv, R.string.input_payment_title) {
            cvv ->
            presenter.isSecurityCodeValid(cvv)
        }
    }

    override fun cvvValid() {
        dialogFragment?.dismiss()
        presenter.finishPurchase()
    }

    override fun loadToolbar(paymentModel: PaymentModel?) {
            paymentModel?.let {
                Glide.with(this@ConfirmationActivity)
                        .load(it.eventImage)
                        .apply(RequestOptions().centerCrop())
                        .into(toolbar_background)
            }
    }
}
