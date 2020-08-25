package br.com.goin.component.newcreditcard

import android.animation.AnimatorInflater
import android.animation.AnimatorSet
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
import br.com.goin.base.extensions.dpToPx
import br.com.goin.base.extensions.gone
import br.com.goin.base.extensions.invisible
import br.com.goin.base.extensions.visible
import br.com.goin.base.model.PaymentModel
import br.com.goin.component.analytics.analytics.Analytics
import br.com.goin.component.newaddress.NewAddressActivity
import br.com.goin.component.payment.CreditCardHelper
import br.com.goin.component.payment.CreditCardModel
import br.com.goin.component.payment.R
import br.com.goin.component.ui.kit.dialog.DialogUtils
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.activity_new_credit_card.*
import kotlinx.android.synthetic.main.credit_card_back.*
import kotlinx.android.synthetic.main.credit_card_front.*
import java.text.SimpleDateFormat



class NewCreditCardActivity : AppCompatActivity(), NewCreditCardView {

    companion object {
        const val MODEL_PAYMENT = "MODEL_PAYMENT"
        fun starter(context: Activity, paymentInfo: PaymentModel) {
            val intent = Intent(context, NewCreditCardActivity::class.java)
            intent.putExtra(MODEL_PAYMENT, paymentInfo)
            context.startActivityForResult(intent, 5001)
        }
    }

    override fun onPaymentReceived(paymentModel: PaymentModel?) {
        paymentModel?.let {
            Glide.with(this@NewCreditCardActivity)
                    .load(it.eventImage)
                    .apply(RequestOptions().centerCrop())
                    .into(toolbar_background)
        }
    }

    private val presenter: NewCreditCardPresenter = NewCreditCardPresenterImpl(this)
    private var adapter: NewCreditCardAdapter? = null
    private var rightOut: AnimatorSet? = null
    private var leftIn: AnimatorSet? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_credit_card)

        presenter.onReceivePaymentModel(intent?.extras?.getSerializable(NewAddressActivity.MODEL_PAYMENT))
        presenter.onCreate()

        ViewCompat.setOnApplyWindowInsetsListener(container) { view, insets ->
            ViewCompat.onApplyWindowInsets(container,
                    insets.replaceSystemWindowInsets(insets.systemWindowInsetLeft, 0,
                            insets.systemWindowInsetRight, insets.systemWindowInsetBottom))
        }

        Analytics.instance.screenViewRes(this, R.string.ADICIONAR_CARTOES)
    }


    override fun configureToolbar() {
        toolbar_title.text = getString(R.string.new_credit_card)
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

    @SuppressLint("SimpleDateFormat")
    override fun configureViewPager(model: CreditCardModel) {
        val arrayTitle = resources.getStringArray(R.array.card_view_pager_array)
        adapter = NewCreditCardAdapter(arrayTitle, model)
        view_pager.adapter = adapter
        view_pager.setPadding(30.dpToPx(), 0, 30.dpToPx(), 0)
        view_pager.setPagingEnabled(false)

        adapter?.onNameTextChange = {
            model.name = it
            credit_card_name.text = it

            if (it.length >= 10) {
                enableNextButton()
            } else {
                disableNextButton()
            }
        }

        adapter?.onNumberTextChange = { s: String, cardType: CreditCardHelper.CardType ->
            credit_card_number.text = s
            if (s.isNotBlank()) {
                model.number = s
            }

            credit_card_logo.setImageResource(cardType.frontResource)
            number_cc_reverse.text = s
            number_cc_reverse.gone()
        }

        adapter?.onFieldIsValid = {
            if (it) {
                enableNextButton()
            } else {
                disableNextButton()
            }
        }

        adapter?.onExpirationTextChange = {
            credit_card_valid.text = CreditCardHelper.formatExpirationDate(it)

            if (CreditCardHelper.checkformatExpirationDateisValid(it)) {
                val dateFormat = SimpleDateFormat("MM/yy")
                model.expireDate = it

                enableNextButton()
            } else {
                disableNextButton()
            }
        }

        adapter?.onCVVTextChange = {
            a_credit_card_cvv.text = it
            model.cvv = it

            if (it.length == 3) {
                enableNextButton()
            } else {
                disableNextButton()
            }
        }

        adapter?.onBirthdayTextChange = {
            if (it.isNotEmpty()) {
                if (CreditCardHelper.formatDate(it)){
                    model.birthday = it
                    enableNextButton()
                }
            }else{
                disableNextButton()
            }
        }

        adapter?.onCPFTextChange = {
            if (it.isNotEmpty()) {
                if (CreditCardHelper.isValid(it)){
                    model.cpf = it

                    enableNextButton()
                }else{
                    disableNextButton()
                }
            }
        }

    }

    override fun configureOnClickListeners() {


        view_pager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }

            override fun onPageSelected(position: Int) {
                if (adapter?.count?.minus(1) == view_pager.currentItem) {
                    next_button.gone()
                    save_button.visible()
                } else {
                    next_button.visible()
                    save_button.gone()
                }

                if (position > 0) {
                    back_button_address.visible()
                } else {
                    back_button_address.invisible()
                }
            }
        })

        back_button_address.setOnClickListener {
            view_pager.currentItem = view_pager.currentItem - 1

            if (view_pager.currentItem == 2) {
                flipCardReverse()
            }
            if (view_pager.currentItem == 3) {
                flipCard()
            }

            enableNextButton()
           
        }

        next_button.setOnClickListener {
            view_pager.currentItem = view_pager.currentItem + 1

            if (view_pager.currentItem == 3) {
                flipCard()
            }

            if (view_pager.currentItem == 4) {
                flipCardReverse()
            }

            disableNextButton()
        }

        save_button.setOnClickListener {
            presenter.saveCreditCard()
        }
    }

    override fun onDestroy() {
        presenter.onDestroy()
        super.onDestroy()
    }

    private fun enableNextButton() {
        next_button.alpha = 1f
        next_button.isEnabled = true
    }

    private fun disableNextButton() {
        next_button.alpha = 0.4f
        next_button.isEnabled = false
    }

    override fun configureAnimations() {
        rightOut = AnimatorInflater.loadAnimator(this, R.animator.out_animation) as AnimatorSet
        leftIn = AnimatorInflater.loadAnimator(this, R.animator.in_animation) as AnimatorSet
    }

    private fun flipCard() {
        rightOut?.setTarget(credit_card_front)
        leftIn?.setTarget(a_credit_card_back)
        rightOut?.start()
        leftIn?.start()

    }

    private fun flipCardReverse() {
        rightOut?.setTarget(a_credit_card_back)
        leftIn?.setTarget(credit_card_front)
        rightOut?.start()
        leftIn?.start()
    }

    private var progressDialog : ProgressDialog? = null

    override fun hideLoading() {
        progressDialog?.let {
            it.hide()
            progressDialog = null
        }
    }
    var dialog: DialogUtils.AlertDialogFragment? = null
    override fun handleError(throwable: Throwable) {
        progressDialog?.let {
            it.hide()
            progressDialog = null
        }
        throwable?.let {
            dialog=  DialogUtils.showError(this, "Erro ao salvar o cartão" , R.drawable.icon_cloud_error, R.string.error){
                presenter.saveCreditCard()
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

    override fun onSuccess(it: CreditCardModel) {
        intent?.putExtra("credit_card", it)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }
}
