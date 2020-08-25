package br.com.goin.features.fullticket

import android.animation.AnimatorInflater
import android.animation.AnimatorSet
import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import br.com.goin.component.navigation.NavigationController
import br.com.goin.feature.wallet.R
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.activity_full_ticket.*
import kotlinx.android.synthetic.main.item_wallet_fullticket.*
import kotlinx.android.synthetic.main.item_wallet_fullticket_back.*
import android.content.DialogInterface
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableString
import android.text.style.TextAppearanceSpan
import androidx.core.content.ContextCompat
import br.com.goin.base.extensions.timer
import br.com.goin.component.analytics.analytics.Analytics
import br.com.goin.features.wallet.model.ACTION
import br.com.goin.features.wallet.model.Ticket
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.item_wallet_fullticket.view.*
import java.util.concurrent.TimeUnit

class FullticketActivity : AppCompatActivity(), FullTicketView {

    private val navigationController = NavigationController.instance!!
    private val presenter: FullTicketPresenter = FullTicketPresenterImpl(this)

    private var rightOut: AnimatorSet? = null
    private var leftIn: AnimatorSet? = null

    private var dialog: AlertDialog? = null
    private var disposable = CompositeDisposable()

    companion object {
        private const val TICKET = "TICKET"

        fun starter(context: Context, ticket: Ticket){
            val intent = Intent(context, FullticketActivity::class.java)
            intent.putExtra(TICKET, ticket)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_full_ticket)

        val distance = 8000
        val scale = resources.displayMetrics.density * distance
        container_back.cameraDistance = scale
        container_front.cameraDistance = scale

        presenter.onReceiveTicket(intent?.extras?.getSerializable(TICKET))
        presenter.onCreate()
    }

    public override fun onResume() {
        super.onResume()
        Analytics.instance.screenView(this, getString(R.string.ticket_screen_name))
    }

    override fun initActions(ticketModel: Ticket) {
        toolbar.setOnBackButtonClicked { onBackPressed() }
    }

    override fun configureOnClickListeners(ticket: Ticket) {
        information_button.setOnClickListener { flipCard(ticket) }
    }

    override fun configureAnimations() {
        rightOut = AnimatorInflater.loadAnimator(this, R.animator.out_animation) as AnimatorSet
        leftIn = AnimatorInflater.loadAnimator(this, R.animator.in_animation) as AnimatorSet
    }

    private fun flipCard(ticket: Ticket) {
        information_button.setOnClickListener {  }
        voucher_back_button.setOnClickListener { flipCardReverse(ticket) }
        location.setOnClickListener {  }

        rightOut?.setTarget(container_front)
        leftIn?.setTarget(container_back)
        rightOut?.start()
        leftIn?.start()
    }

    private fun flipCardReverse(ticket: Ticket) {
        information_button.setOnClickListener { flipCard(ticket) }
        voucher_back_button.setOnClickListener {  }
        location.setOnClickListener {
            ticket.detail?.let {
                when(it.action){
                    ACTION.CLUB -> {
                        navigationController.placeModule().goToPlace(this, ticket.detail.actionValue ?: "", "")
                    }
                    ACTION.EVENT -> {
                        navigationController.eventModule().goToEvent(this, ticket.detail.actionValue ?: "", "")
                    }
                }
            }
        }

        rightOut?.setTarget(container_back)
        leftIn?.setTarget(container_front)
        rightOut?.start()
        leftIn?.start()
    }

    override fun configureTicket(ticketModel: Ticket) {
        ticket_view.onTicketCutoffListener = {
            showConfirmationDialog()
        }
        ticket_view.afterAnimationListener = {
            disposable.add(timer(500, TimeUnit.MILLISECONDS){
                showSuccessDialog()
            })
        }

        configureFrontTicket(ticketModel)
        configureBackTicket(ticketModel)
    }

    override fun cutOffTicket(){
        dialog?.dismiss()

        disposable.add(timer(500, TimeUnit.MILLISECONDS) {
            ticket_view.cutOffTicket()
        })
    }

    private fun showConfirmationDialog() {
        val view = View.inflate(this, R.layout.dialog_confirm, null)
        val builder = AlertDialog.Builder(this).setView(view)
        builder.setPositiveButton(getString(R.string.confirm)) { dialog, which ->
            dialog.dismiss()
            showLoadingDialog()
            presenter.validateTicket()
        }
        builder.setNegativeButton(getString(R.string.cancel_ticket)) { dialog, which ->
            dialog.dismiss()
            ticket_view.reset()
        }
        val dialog = builder.create()
        dialog.show()

        val buttonPositive = dialog.getButton(DialogInterface.BUTTON_POSITIVE)
        buttonPositive.setTextColor(ContextCompat.getColor(this, R.color.style_orange))

        val buttonNegative = dialog.getButton(DialogInterface.BUTTON_NEGATIVE)
        buttonNegative.setTextColor(ContextCompat.getColor(this, R.color.black))
    }

    override fun showErrorDialog() {
        dialog?.dismiss()

        val view = View.inflate(this, R.layout.dialog_ticket_error, null)
        val builder = AlertDialog.Builder(this).setView(view)
        builder.setNegativeButton(getString(R.string.close)) { dialog, which ->
            dialog.dismiss()
        }
        val dialog = builder.create()
        dialog.show()

        val buttonNegative = dialog.getButton(DialogInterface.BUTTON_NEGATIVE)
        buttonNegative.setTextColor(ContextCompat.getColor(this, R.color.black))
    }

    override fun showSuccessDialog() {
        dialog?.dismiss()

        val view = View.inflate(this, R.layout.dialog_success, null)
        val builder = AlertDialog.Builder(this).setView(view)
        builder.setNegativeButton(getString(R.string.close)) { dialog, which ->
            dialog.dismiss()
        }
        val dialog = builder.create()
        dialog.show()

        val buttonNegative = dialog.getButton(DialogInterface.BUTTON_NEGATIVE)
        buttonNegative.setTextColor(ContextCompat.getColor(this, R.color.black))
    }

    override fun showLoadingDialog() {
        val view = View.inflate(this, R.layout.dialog_loading, null)
        val builder = AlertDialog.Builder(this).setView(view).setCancelable(false)
        dialog = builder.create()
        dialog?.show()
    }

    private fun configureBackTicket(ticket: Ticket) {
        container_back.findViewById<TextView>(R.id.regulament_description).text = ticket.regulation ?: ""

        val title = "${ticket.title} ${ticket.subtitle}"
        val subtitle = ticket.subtitle ?: ""
        val titleSpannable = SpannableString(title)

        val redColor = ColorStateList(arrayOf(intArrayOf()), intArrayOf(ContextCompat.getColor(this, R.color.gray_ticket)))
        val highlightSpan = TextAppearanceSpan(null, Typeface.NORMAL, -1, redColor, null)
        titleSpannable.setSpan(highlightSpan, title.indexOf(subtitle), title.indexOf(subtitle) + subtitle.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

        container_back.voucher_description.text = titleSpannable
    }

    private fun configureFrontTicket(ticketModel: Ticket) {
        ticketModel.image?.let {
            Glide.with(this)
                    .load(it)
                    .apply(RequestOptions.placeholderOf(R.drawable.branco))
                    .into(container_front.voucher_image)
        }

        ticketModel.detailTitle?.let {
            container_front.ticket_type.text = it
        }

        val title = "${ticketModel.title} ${ticketModel.subtitle}"
        val subtitle = ticketModel.subtitle ?: ""
        val titleSpannable = SpannableString(title)

        val redColor = ColorStateList(arrayOf(intArrayOf()), intArrayOf(ContextCompat.getColor(this, R.color.gray_ticket)))
        val highlightSpan = TextAppearanceSpan(null, Typeface.NORMAL, -1, redColor, null)
        titleSpannable.setSpan(highlightSpan, title.indexOf(subtitle), title.indexOf(subtitle) + subtitle.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

        container_front.voucher_description.text = titleSpannable
        container_front.date.text = ticketModel.dateText
        container_front.location.text = ticketModel.placeName ?: ""
    }
}