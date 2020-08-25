package br.com.goin.feature.event.discountDialog

import android.app.Dialog
import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.Toast
import br.com.goin.feature.event.BrowserHelper
import br.com.goin.component.model.event.Event
import br.com.goin.component.navigation.NavigationController
import br.com.goin.feature.event.R
import kotlinx.android.synthetic.main.coupom_discount_dialog.*

class DiscountDialog(context: Context, val event: Event) : Dialog(context, R.style.ActivityDialog), DiscountView {

    private val presenter: DiscountPresenter = DiscountPresenterImpl(this)
    private val navigationController = NavigationController.instance

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window?.setBackgroundDrawable(ColorDrawable(0))
        setContentView(R.layout.coupom_discount_dialog)

        presenter.onReceivedEvent(event)
        presenter.onCreate()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        presenter.onDestroy()
    }

    override fun configureClickListener() {
        btn_yes.setOnClickListener {
            presenter.onConfirmDiscountCode(code.text.toString())
        }

        btn_no.setOnClickListener {
            presenter.onClickNo()
        }
    }

    override fun showToastFillFields() {
        Toast.makeText(context, context.getString(R.string.fill_the_discount_field), Toast.LENGTH_SHORT).show()
    }

    override fun showToastDiscountCodeInvalid() {
        Toast.makeText(context, context.getString(R.string.invalid_discount_code), Toast.LENGTH_SHORT).show()
    }

    override fun clearDiscountCodeField() {
        code.setText("")
    }

    override fun showLoading() {
        content.visibility = View.INVISIBLE
        progress_bar.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        content.visibility = View.VISIBLE
        progress_bar.visibility = View.INVISIBLE
    }

    override fun goToIngressoRapidoSite(event: Event) {
        dismiss()
        event.originURL?.let {
            BrowserHelper.openBrowser(context, it)
        }
    }

    override fun goToTickets(event: Event, code: String) {
        dismiss()
        navigationController?.legacyApp()?.goToEventTickets(context, event.id, event.originType!!, null)
    }
}