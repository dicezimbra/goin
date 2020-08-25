package br.com.goin.component.listcreditcards

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.goin.base.model.PaymentCreditCard
import br.com.goin.base.model.PaymentModel
import br.com.goin.component.analytics.analytics.Analytics
import br.com.goin.component.navigation.NavigationController
import br.com.goin.component.payment.CreditCardHelper
import br.com.goin.component.payment.CreditCardModel
import br.com.goin.component.payment.R
import br.com.goin.component.ui.kit.dialog.DialogUtils
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.activity_list_credit_card.*

class ListCreditCardActivity : AppCompatActivity(), ListCreditCardView {


    private val presenter: ListCreditCardPresenter = ListCreditCardPresenterImpl(this)
    private val navigationController = NavigationController.instance
    private var adapter: ListCreditCardsAdapter? = null


    companion object {
        val MODEL_PAYMENT = "MODEL_PAYMENT"
        fun starter(context: Context, paymentInfo: PaymentModel) {
            val it = Intent(context, ListCreditCardActivity::class.java)
            it.putExtra(MODEL_PAYMENT, paymentInfo)
            context.startActivity(it)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_credit_card)

        presenter.onReceivePaymentModel(intent?.extras?.getSerializable(ListCreditCardActivity.MODEL_PAYMENT))
        presenter.onCreate()

        Analytics.instance.screenViewRes(this, R.string.LISTA_CARTOES)
    }

    override fun onDestroy() {
        presenter.onDestroy()
        super.onDestroy()
    }

    override fun configureView() {

        add.setOnClickListener {
            addNewCreditCard()
        }

        list.layoutManager = LinearLayoutManager(this)
        var dividerItemDecoration = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        dividerItemDecoration.setDrawable(this.getResources().getDrawable(R.drawable.line_divider_recyclerview))
        list.addItemDecoration(dividerItemDecoration)
    }

    override fun loadCreditCards(creditCardList: List<CreditCardModel>) {
        adapter = ListCreditCardsAdapter(creditCardList)
        adapter.apply {
            this?.onItemSelected = { creditCardModel ->

                val paymentModel = presenter.getPaymentModel()
                paymentModel.creditcard = mapToCreditCard(creditCardModel)
                NavigationController.instance!!.paymentModule().goToConfirmationScreen(this@ListCreditCardActivity, paymentModel)
            }
        }
        list.adapter = adapter
    }

    private fun mapToCreditCard(creditCardModel: CreditCardModel): PaymentCreditCard {
        val paymentCreditCard = PaymentCreditCard ()

        paymentCreditCard.brand = creditCardModel.creditCardType.friendlyName
        paymentCreditCard.installments
        paymentCreditCard.number = CreditCardHelper.cleanNumberLong(creditCardModel.number)
        paymentCreditCard.holderName = creditCardModel.name
        paymentCreditCard.expiracyMonth = extractMonth(creditCardModel.expireDate)
        paymentCreditCard.expiracyYear = extractYear(creditCardModel.expireDate)
        paymentCreditCard.cvv = CreditCardHelper.cleanNumberLong(creditCardModel.cvv)
        paymentCreditCard.birthday = creditCardModel.birthday
        paymentCreditCard.cpf = creditCardModel.cpf

        return paymentCreditCard
    }

    private fun extractYear(expireDate: String?): Int? {
        return expireDate?.substring(3,5)?.toInt()
    }

    private fun extractMonth(expireDate: String?): Int? {
        return expireDate?.substring(0,2)?.toInt()
    }


    private var progressDialog: ProgressDialog? = null

    override fun hideLoading() {
        progressDialog?.let {
            it.hide()
            progressDialog = null
        }
    }
    var dialog: DialogUtils.AlertDialogFragment? = null

    override fun handleError(t: Throwable?) {
        progressDialog?.let {
            it.hide()
            progressDialog = null
        }
        t?.let {

            dialog = DialogUtils.showError(this, "Erro ao carregar os cartões" , R.drawable.icon_cloud_error, R.string.error){
                presenter.onCreate()
                dialog?.dismiss()
            }
        }
    }


    override fun showLoading() {
        progressDialog?.let {
            it.dismiss()
        }

        progressDialog = DialogUtils.createProgressDialog(this)
        progressDialog?.show()
    }

    override fun addNewCreditCard() {
        NavigationController.instance!!.paymentModule().goToNewCreditCradScreen(this, presenter.getPaymentModel())
    }

    override fun onPaymentReceived(paymentModel: PaymentModel?) {
        paymentModel?.let {
            Glide.with(this@ListCreditCardActivity)
                    .load(it.eventImage)
                    .apply(RequestOptions().centerCrop())
                    .into(toolbar_background)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if (resultCode == Activity.RESULT_OK && requestCode == 5001) {
            val creditCardModel = data?.extras?.getSerializable("credit_card") as CreditCardModel
            val paymentModel = presenter.getPaymentModel()
            paymentModel.creditcard = mapToCreditCard(creditCardModel)
            NavigationController.instance!!.paymentModule().goToConfirmationScreen(this@ListCreditCardActivity, paymentModel)

        }

        super.onActivityResult(requestCode, resultCode, data)
    }

}
