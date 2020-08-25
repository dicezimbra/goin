package br.com.goin.feature.theaters.plays.movies.playsMoviesDetail.session.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.goin.feature.theaters.R

import br.com.goin.feature.theaters.plays.movies.playsMoviesDetail.model.EventSessionDetailInfo
import br.com.goin.feature.theaters.plays.movies.playsMoviesDetail.session.Session
import br.com.goin.base.extensions.gone
import br.com.goin.base.extensions.toTitleCase
import br.com.goin.base.extensions.visible

import kotlinx.android.synthetic.main.view_event_session_component_list_item.view.*

class SessionComponentListAdapter(private val sessions: List<Session>):
        RecyclerView.Adapter<SessionComponentListAdapter.ViewHolder>() {

    var onClickUber: ((Session) -> Unit)? = null
    var onClickMap: ((Session) -> Unit)? = null
    var onClickSession: ((Session, EventSessionDetailInfo) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context)
                .inflate(R.layout.view_event_session_component_list_item, parent, false)

        return ViewHolder(parent.context, view as ViewGroup)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val session = sessions[position]

        if (session.lat != null && session.lng != null) {
            configureAddress(session, holder)
            configureUber(session, holder)
        }else{
            holder.labelHowToGet.gone()
            holder.address.gone()
            holder.labelRoutes.gone()
            holder.arrowHowtoGet.gone()
            holder.uberEstimatedTime.gone()
            holder.uberEstimatedPrice.gone()
            holder.imageViewArrowUber.gone()
            holder.arrowLocation.gone()
        }

        configureClick(holder, session)
        holder.bindItem(session)
    }

    override fun onViewRecycled(holder: ViewHolder) {
        super.onViewRecycled(holder)
        holder.labelHowToGet.visible()
        holder.address.visible()
        holder.labelRoutes.visible()
        holder.arrowHowtoGet.visible()
        holder.uberEstimatedTime.visible()
        holder.uberEstimatedPrice.visible()
        holder.imageViewArrowUber.visible()
        holder.locationIcon.visible()
        holder.address.visible()
        holder.arrowLocation.visible()
    }

    private fun configureAddress(session: Session, holder: ViewHolder) {
        if (session.address.isBlank()) {
            holder.locationIcon.gone()
            holder.address.gone()
        } else {
            holder.address.text = session.address.toTitleCase()
        }
    }

    private fun configureUber(session: Session, holder: ViewHolder) {
        val context = holder.itemView.context
        val uberInformation = session.uberInformation
        if(uberInformation == null){
            holder.uberEstimatedPrice.gone()
            holder.uberEstimatedTime.gone()
            holder.imageViewArrowUber.gone()
            holder.imageViewUber.gone()
            holder.textViewGoToUber.gone()
        }

        session.uberInformation?.let {
            holder.imageViewArrowUber.visible()
            holder.imageViewUber.visible()
            holder.textViewGoToUber.visible()

            it.price?.let { price ->
                holder.uberEstimatedPrice.visible()
                holder.uberEstimatedPrice.text = context.getString(R.string.uber_price, price)
            }
            it.time?.let { time ->
                holder.uberEstimatedTime.visible()
                holder.uberEstimatedTime.text = context.getString(R.string.uber_wait_time, time)
            }
        }
    }

    private fun configureClick(holder: ViewHolder, session: Session) {
        holder.uberEstimatedTime.setOnClickListener {
            onClickUber?.invoke(session)
        }

        holder.imageViewArrowUber.setOnClickListener {
            onClickUber?.invoke(session)
        }

        holder.uberEstimatedPrice.setOnClickListener {
            onClickUber?.invoke(session)
        }

        holder.imageViewArrowAvailableRoutes.setOnClickListener {
            onClickMap?.invoke(session)
        }

        holder.textViewAvailableRoutes.setOnClickListener {
            onClickMap?.invoke(session)
        }
    }

    override fun getItemCount(): Int {
        return sessions.size
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    inner class ViewHolder(var context: Context, val view: ViewGroup) :
            androidx.recyclerview.widget.RecyclerView.ViewHolder(view) {

        val constraintLayout = view.contraint_layout
        val locationIcon = view.location_icon
        val address = view.full_address_text
        val arrowLocation = view.imageViewDirectionEstablishment
        val labelHowToGet = view.textViewHowToGet
        val labelRoutes = view.textViewAvailableRoutes
        val arrowHowtoGet = view.imageViewArrowAvailableRoutes

        val textViewAvailableRoutes = view.textViewAvailableRoutes
        val imageViewArrowAvailableRoutes = view.imageViewArrowAvailableRoutes

        val uberEstimatedTime = view.uber_estimated_time
        val uberEstimatedPrice = view.uber_estimated_price
        val imageViewArrowUber =  view.imageViewArrowUber
        val imageViewUber = view.imageViewUber
        val textViewGoToUber = view.textViewGoToUber

        fun bindItem(session: Session) {
            view.textViewMovieName.text = session.clubName
            createSessionsView(session)
        }

        private fun createSessionsView(session: Session) {
            session.details?.forEach { detail ->
                view.room.text = detail.room
                if (detail.room.isNullOrBlank()) {
                    view.room.visibility = View.GONE
                }

                detail.eventSessionDetailInfo?.let { info ->
                    val sessionComponentListTimes = SessionComponentListTimes(info)
                    sessionComponentListTimes.onClickListener = {
                        onClickSession?.invoke(session, it)
                    }
                    view.recyclerViewTimeSession.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                    view.recyclerViewTimeSession.adapter = sessionComponentListTimes
                }
            }
        }
    }
}