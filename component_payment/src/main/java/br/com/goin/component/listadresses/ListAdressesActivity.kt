package br.com.goin.component.listadresses

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.goin.base.model.PaymentModel
import br.com.goin.component.payment.R
import br.com.goin.component.address.AddressModel
import br.com.goin.component.address.UserAddress
import br.com.goin.component.analytics.analytics.Analytics
import br.com.goin.component.navigation.NavigationController
import br.com.goin.component.newaddress.NewAddressActivity
import br.com.goin.component.ui.kit.dialog.DialogUtils
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.activity_list_adresses.*

class ListAdressesActivity : AppCompatActivity(), ListAdressesView {

    private val presenter: ListAdressesPresenter = ListAdressesPresenterImpl(this)
    private val navigationController = NavigationController.instance
    private var adapter: ListAdressesAdapter? = null

    companion object {

        val MODEL_PAYMENT = "MODEL_PAYMENT"

        fun starter(context: Context, paymentModel: PaymentModel?) {
            val it = Intent(context, ListAdressesActivity::class.java)
            it.putExtra(MODEL_PAYMENT, paymentModel)
            context.startActivity(it)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_adresses)

        presenter.onCreate()
        presenter.onReceivePaymentModel(intent?.extras?.getSerializable(MODEL_PAYMENT))

        Analytics.instance.screenViewRes(this, R.string.LISTA_ENDERECOS)
    }

    override fun onDestroy() {
        presenter.onDestroy()
        super.onDestroy()
    }

    override fun loadAddressesInfo() {
        Glide.with(this@ListAdressesActivity)
                .load(presenter.getPaymentModel()?.eventImage)
                .apply(RequestOptions().centerCrop())

                .into(toolbar_background)
    }

    override fun configureView() {
        add_adress.setOnClickListener {
            addNewAddress()
        }

        list.layoutManager = LinearLayoutManager(this)
        var dividerItemDecoration = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        dividerItemDecoration.setDrawable(this.getResources().getDrawable(R.drawable.line_divider_recyclerview))
        list.addItemDecoration(dividerItemDecoration)
    }

    override fun loadAddress(adressesModelList: List<AddressModel>) {
        adapter = ListAdressesAdapter(adressesModelList)
        adapter.apply {
            this?.onItemSelected = { addressModel ->
                presenter.loadAddressModel(addressModel)
                presenter.goToCreditCardList()

            }
        }
        list.adapter = adapter
    }

    private var progressDialog: ProgressDialog? = null
    var dialog: DialogUtils.AlertDialogFragment? = null
    override fun handleError(t: Throwable?) {
        progressDialog?.let {
            it.hide()
            progressDialog = null
        }
        t?.let {
            dialog = DialogUtils.showError(this, "Erro ao carregar endereços" , R.drawable.icon_cloud_error, R.string.error){
                presenter.loadAdresses()
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

    override fun addNewAddress() {
        navigationController?.paymentModule()?.goToNewAddressScreen(this, presenter.getPaymentModel())
    }

    override fun goToCreditCardList() {
        navigationController?.paymentModule()?.goToCreditCardList(this, presenter.getPaymentModel()!!)
    }

    override fun configureToolbar() {
        val window = window
        val winParams = window.attributes
        winParams.flags = winParams.flags and WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS.inv()
        window.attributes = winParams
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN

        back_button.setOnClickListener {
            this.finish()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(resultCode == Activity.RESULT_OK){
            if(requestCode == NewAddressActivity.REQUEST_CODE_NEW_ADDRESS){
                val addressModel = data?.extras?.getSerializable("address_model") as UserAddress
                presenter.loadUserAddresss(addressModel)
                presenter.goToCreditCardList()
            }
        }

        super.onActivityResult(requestCode, resultCode, data)
    }
}
