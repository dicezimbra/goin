package br.com.goin.component.newaddress

import android.annotation.SuppressLint
import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.viewpager.widget.ViewPager
import br.com.goin.base.extensions.gone
import br.com.goin.base.extensions.visible
import br.com.goin.base.model.PaymentModel
import br.com.goin.component.address.AddressModel
import br.com.goin.component.address.UserAddress
import br.com.goin.component.analytics.analytics.Analytics
import br.com.goin.component.listadresses.ListAdressesActivity
import br.com.goin.component.newaddress.adapter.AddressPagerAdapter
import br.com.goin.component.payment.R
import br.com.goin.component.ui.kit.dialog.DialogUtils
import br.com.goin.component.ui.kit.features.error.ErrorDialog
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.activity_new_address.*

class NewAddressActivity : AppCompatActivity(), NewAddressView {

    private val presenter: NewAddressPresenter = NewAddressPresenterImpl(this)
    private var viewPagerAdapter: AddressPagerAdapter? = null
    private var addressItems: AddressModel? = null
    private var userAddress: UserAddress? = null
    private var addressNickname: String? = null
    private var addressNumber: String? = null
    private var errorDialog: ErrorDialog? = null

    companion object {
        const val MODEL_PAYMENT = "MODEL_PAYMENT"
        const val REQUEST_CODE_NEW_ADDRESS = 5000
        fun starter(context: Activity, paymentModel: PaymentModel?) {
            val it = Intent(context, NewAddressActivity::class.java)
            it.putExtra(MODEL_PAYMENT, paymentModel)
            context.startActivityForResult(it, REQUEST_CODE_NEW_ADDRESS)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_address)

        presenter.onCreate()
        presenter.onReceivePaymentModel(intent?.extras?.getSerializable(MODEL_PAYMENT))

        errorDialog = ErrorDialog(this@NewAddressActivity)

        ViewCompat.setOnApplyWindowInsetsListener(container) { view, insets ->
            ViewCompat.onApplyWindowInsets(container,
                    insets.replaceSystemWindowInsets(insets.getSystemWindowInsetLeft(), 0,
                            insets.getSystemWindowInsetRight(), insets.getSystemWindowInsetBottom()))
        }

        Analytics.instance.screenViewRes(this, R.string.ADICIONAR_ENDERECO)
    }

    override fun onPaymentReceived(paymentModel: PaymentModel?) {
        paymentModel?.let {
            Glide.with(this@NewAddressActivity)
                    .load(it.eventImage)
                    .apply(RequestOptions().centerCrop())
                    .into(toolbar_background)
        }
    }

    override fun configureToolbar() {
        toolbar_title.text = getString(R.string.new_address)
        val window = window
        val winParams = window.attributes
        winParams.flags = winParams.flags and WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS.inv()
        window.attributes = winParams
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN

        back_button.setOnClickListener {
            this.finish()
        }
        toolbar_title.setOnClickListener { onBackPressed() }

    }

    override fun configureOnClickListeners() {

        next_button_address.setOnClickListener {
            view_pager_address.currentItem = view_pager_address.currentItem + 1

            when (view_pager_address.currentItem)
            {
                0 -> {
                    disableButton()
                }
                1-> {
                    disableButton()
                }
                2-> {
                    enableButton()
                }
                3-> {
                    disableButton()
                }
                4-> {
                    enableButton()
                }
                5-> {
                    enableButton()
                }
            }
        }

        view_pager_address.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }

            override fun onPageSelected(position: Int) {
                if (viewPagerAdapter?.count?.minus(1) == view_pager_address.currentItem) {
                    next_button_address.gone()
                    save_button_address.visible()
                } else {
                    next_button_address.visible()
                    save_button_address.gone()
                }
            }
        })

        save_button_address.setOnClickListener {
            addressItems?.let { addr ->
                userAddress = UserAddress()
                userAddress?.district = addr.bairro
                if (addr.complemento == "") {
                    userAddress?.complement = null
                } else {
                    userAddress?.complement = addr.complemento
                }
                userAddress?.alias = addressNickname
                userAddress?.city = addr.localidade
                userAddress?.number = addr.number
                userAddress?.state = addr.uf
                userAddress?.street = addr.logradouro
                userAddress?.zipcode = addr.cep
                presenter.setAddressItens(userAddress!!)
                presenter.addAddress()
            }

        }

        back_button_address.setOnClickListener {
            view_pager_address.currentItem = view_pager_address.currentItem - 1
        }
    }

    @SuppressLint("SetTextI18n")
    override fun configureViewPager() {
        viewPagerAdapter = AddressPagerAdapter(this, addressItems, addressNickname)
        view_pager_address.adapter = viewPagerAdapter
        view_pager_address.clipToPadding = false
        view_pager_address.setPagingEnabled(false)
        view_pager_address.offscreenPageLimit = 5
        view_pager_address.setPadding(40, 0, 40, 0)

        viewPagerAdapter?.onZipcodeTextChanges = {
            if (!it.isBlank()) {
                runOnUiThread {

                    presenter.loadAddress(it.replace("-", ""))
                }
            } else {
                disableButton()
            }
        }

        viewPagerAdapter?.onNicknameTextChanges = {
            if (!it.isBlank()) {
                runOnUiThread {
                    nickname_address.text = it
                    addressNickname = it
                    addressItems?.nickname = addressNickname

                    enableButton()
                }
            } else {
                disableButton()
            }
        }

        viewPagerAdapter?.onAddonTextChanges = {
            if (!it.isBlank()) {
                runOnUiThread {
                    addressItems?.complemento = it
                    if (!addressItems?.complemento.isNullOrEmpty()) {
                        full_address.text = addressItems?.logradouro + ", " + addressNumber + " - " +
                                addressItems?.bairro + ", " + addressItems?.localidade + " - " +
                                addressItems?.uf + ", " + addressItems?.cep + " - " + addressItems?.complemento
                    } else {
                        full_address.text = addressItems?.logradouro + ", " + addressNumber + " - " +
                                addressItems?.bairro + ", " + addressItems?.localidade + " - " +
                                addressItems?.uf + ", " + addressItems?.cep
                    }
                    if (addressItems?.complemento != null && addressNumber.isNullOrEmpty()) {
                        full_address.text = addressItems?.logradouro + ", " +
                                addressItems?.bairro + ", " + addressItems?.localidade + " - " +
                                addressItems?.uf + ", " + addressItems?.cep + " - " + addressItems?.complemento
                    }
                    enableButton()
                }
            } else {
                disableButton()
            }
        }

        viewPagerAdapter?.onNeighborhoodTextChanges = {
            if (!it.isBlank()) {

                runOnUiThread {
                    addressItems?.bairro = it
                    if (addressNumber != null) {
                        if (addressItems?.complemento != null) {
                            full_address.text = addressItems?.logradouro + ", " + addressNumber + " - " +
                                    addressItems?.bairro + ", " + addressItems?.localidade + " - " +
                                    addressItems?.uf + ", " + addressItems?.cep + " - " + addressItems?.complemento
                        }
                    } else {
                        full_address.text = addressItems?.logradouro + ", " + addressNumber + " - " +
                                addressItems?.bairro + ", " + addressItems?.localidade + " - " +
                                addressItems?.uf + ", " + addressItems?.cep + " - " + addressItems?.complemento
                    }
                    enableButton()
                }
            } else {
                disableButton()
            }
        }

        viewPagerAdapter?.onNumberTextChanges = {
            if (!it.isBlank()) {
                runOnUiThread {
                    addressNumber = it
                    if (addressNumber!!.isEmpty()) {
                        full_address.text = addressItems?.logradouro + " - " +
                                addressItems?.bairro + ", " + addressItems?.localidade + " - " +
                                addressItems?.uf + ", " + addressItems?.cep
                    } else {
                        full_address.text = addressItems?.logradouro + ", " + addressNumber + " - " +
                                addressItems?.bairro + ", " + addressItems?.localidade + " - " +
                                addressItems?.uf + ", " + addressItems?.cep
                    }

                    addressItems?.number = addressNumber
                    enableButton()
                }
            } else {
                disableButton()
            }
        }

        viewPagerAdapter?.onThroughfarTextChanges = {
            if (!it.isBlank()) {
                runOnUiThread {
                    addressItems?.logradouro = it
                    full_address.text = addressItems?.logradouro + " - " +
                            addressItems?.bairro + ", " + addressItems?.localidade + " - " +
                            addressItems?.uf + ", " + addressItems?.cep

                    enableButton()
                }
            } else {
                disableButton()
            }
        }

        viewPagerAdapter?.onFieldIsValid = {
            if(it){
                enableButton()
            }else{
                disableButton()
            }
        }


    }

    @SuppressLint("SetTextI18n")
    override fun onLoadAddress(address: AddressModel?) {
        addressItems = address

        viewPagerAdapter?.addressItems = address
        viewPagerAdapter?.addressNick = addressNickname
        viewPagerAdapter?.notifyDataSetChanged()
        addressItems?.let {
            if (it.logradouro != null) {
                full_address.text = it.logradouro + " - " + it.bairro + ", " + it.localidade + " - " + it.uf + ", " + it.cep
                enableButton()
            } else {
                disableButton()
                full_address.text = getString(R.string.address_not_found)
            }
        }
    }

    private fun enableButton() {
        next_button_address.alpha = 1f
        next_button_address.isEnabled = true
    }

    override fun onAddressSuccess(address: UserAddress) {

        intent?.putExtra("address_model", address)
        setResult(Activity.RESULT_OK, intent)
        finish()

        setResult(Activity.RESULT_OK)
        this.finish()
    }

    override fun onAddressError() {
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

    private var progressDialog: ProgressDialog? = null
    var dialog: DialogUtils.AlertDialogFragment? = null
    override fun showException(message: String?) {
        progressDialog?.let {
            it.hide()
            progressDialog = null
        }
        message?.let {
            dialog = DialogUtils.showError(this, "Erro ao adicionar endereço" , R.drawable.icon_cloud_error, R.string.error){
                presenter.addAddress()
                dialog?.dismiss()
            }
        }
    }

    override fun disableButtonNext() {
        disableButton()
    }

    private fun disableButton() {
        next_button_address.alpha = 0.4f
        next_button_address.isEnabled = false
    }

}
