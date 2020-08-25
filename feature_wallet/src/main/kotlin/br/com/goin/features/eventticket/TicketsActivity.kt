package br.com.goin.eventticket

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.goin.base.utils.DateUtils
import br.com.goin.component.model.event.Event
import br.com.goin.component.navigation.NavigationController
import br.com.goin.component.session.user.UserHelper
import br.com.goin.component.ui.kit.dialog.DialogUtils
import br.com.goin.feature.wallet.R
import br.com.goin.features.eventticket.TicketsAdapter
import br.com.goin.features.wallet.model.EventTicket
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.activity_event_tickets.*
import android.view.WindowManager
import android.view.View
import br.com.goin.base.model.PaymentModel
import br.com.goin.base.model.PaymentTicket
import br.com.goin.component.analytics.analytics.Analytics


class TicketsActivity : AppCompatActivity() , TicketView{


    val navigationControler = NavigationController.instance

    private val presenter: TicketPresenter = TicketPresenterImpl(this)
    private var adapter : TicketsAdapter? = null

    companion object {
        val EVENT_ID = "EVENT_ID"
        val INGRESSE_TOKEN = "INGRESSE_TOKEN"
        val ORIGIN_TYPE = "ORIGIN_TYPE"

        fun starter(context: Context, eventId: String) {
            val intent = Intent(context, TicketsActivity::class.java)
            intent.putExtra(EVENT_ID, eventId)
            context.startActivity(intent)
        }

        fun starter(context: Context, eventId: String, originType: String, ingresseToken : String?) {

            val intent = Intent(context, TicketsActivity::class.java)
            intent.putExtra(EVENT_ID, eventId)
            intent.putExtra(ORIGIN_TYPE, originType)
            intent.putExtra(INGRESSE_TOKEN, ingresseToken)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event_tickets)

        presenter.onReceiveEventId(intent.getStringExtra(TicketsActivity.EVENT_ID))
        presenter.onReceiveIngresseToken(intent.getStringExtra(INGRESSE_TOKEN))
        presenter.onReceiveOriginType( intent.getStringExtra(ORIGIN_TYPE))
        presenter.fillPaymentModel()
        presenter.onCreate()

        Analytics.instance.screenViewRes(this, R.string.LISTA_INGRESSO_EVENTO)
    }

    override fun onDestroy() {
        presenter.onDestroy()
        super.onDestroy()
    }

    override fun configureRecyclerView(){
        tickets_cards.layoutManager = LinearLayoutManager(this)
        var dividerItemDecoration = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        dividerItemDecoration.setDrawable(this.getResources().getDrawable(R.drawable.line_divider_recyclerview))
        tickets_cards.addItemDecoration(dividerItemDecoration)
    }

    override fun configurePlusClicks() {
        go.setBackgroundResource(R.drawable.gradient_background_default_goin_button)
        go.setOnClickListener{
            val ticket = adapter?.getTicketSelected()
            if(ticket != null){
                presenter.fillTicketModel(ticket)
                navigationControler?.paymentModule()?.goToSelectAddressScreen(this, presenter.getPaymentModel())
            }else{
                Toast.makeText(this, "Selecione um item_success_dialog", Toast.LENGTH_LONG).show()
            }
        }
    }


    override fun configureMinusClicks() {
        go.setBackgroundResource(R.drawable.gradient_background_default_goin_button_disabled)
        go.setOnClickListener{

        }
    }

    override fun configureTickets(allEvents: List<EventTicket>?) {
        allEvents?.let { allEvents ->
            adapter = TicketsAdapter(allEvents)
            tickets_cards.adapter = adapter

            adapter?.apply {
                onPlusClicked = { value: String, ticket1: EventTicket ->

                    ticket1.buyQuantity = 1
                    for(ticket : EventTicket in itens){
                        ticket.isPlusActive = false
                        ticket.isMinusActive = false
                    }

                    ticket1.isMinusActive  = true

                    total_value.text = value
                    adapter?.notifyDataSetChanged()

                    configurePlusClicks()
                }

                onMinusClicked = { value: String, ticket1: EventTicket ->

                    for(ticket : EventTicket in itens){
                        ticket.isPlusActive = true
                        ticket.isMinusActive = false
                    }

                    ticket1.buyQuantity = 0

                    total_value.text = value
                    adapter?.notifyDataSetChanged()

                    configureMinusClicks()
                }
            }
        }
    }

    override fun eventLoaded(event: Event?) {
        event?.let {
            txt_month.text = DateUtils.getMonth(it.startDate)
            txt_day.text = DateUtils.getDay(it.startDate)
            event_name.text = it.name
            if (event.club.isNullOrEmpty()) {
                event_place.text = it.placeAddress
            } else {
                event_place.text = it.club
            }
        }
    }


    private var progressDialog : ProgressDialog? = null

    override fun hideLoading() {
        progressDialog?.let {
            it.hide()
            progressDialog = null
        }
    }


    override fun handleError(t: Throwable?) {
        progressDialog?.let {
            it.hide()
            progressDialog = null
        }
        t?.let {
            Toast.makeText(this, "Erro ao carregar os tickets", Toast.LENGTH_LONG).show()
        }
    }

    override fun showLoading() {
        progressDialog?.let {
            it.dismiss()
        }

        progressDialog = DialogUtils.createProgressDialog(this)
        progressDialog?.show()
    }

    override fun loadToolbar(event: Event?) {
        Glide.with(this)
                .load(event?.photoUrl)
                .apply(RequestOptions().centerCrop())
                .into(toolbar_background)

        presenter.getPaymentModel().eventImage = event?.photoUrl

    }

    override fun configureToolbar() {
        val window = window
        val winParams = window.attributes
        winParams.flags = winParams.flags and WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS.inv()
        window.attributes = winParams
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN

        back_button.setOnClickListener{
            this.finish()
        }
    }

}
