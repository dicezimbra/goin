package br.com.goin.feature_theater.session.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.com.goin.component.navigation.NavigationController
import br.com.goin.feature_theater.R
import br.com.goin.feature_theater.model.MovieTheaterEvent
import br.com.goin.feature_theater.model.Info
import kotlinx.android.synthetic.main.view_theater_session_component_list_item.view.*
import kotlinx.android.synthetic.main.view_theater_session_component_list_item_detail.view.*

class SessionTheaterComponentListAdapter(private val sessions: List<MovieTheaterEvent>)
    : androidx.recyclerview.widget.RecyclerView.Adapter<androidx.recyclerview.widget.RecyclerView.ViewHolder>() {

    var onClickListener: ((Info) -> Unit)? = null
    val navigationControler = NavigationController.instance

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): androidx.recyclerview.widget.RecyclerView.ViewHolder {
        val view: View = LayoutInflater.from(parent.context)
                .inflate(R.layout.view_theater_session_component_list_item, parent, false)

        return ViewHolder(parent.context, view as ViewGroup)
    }

    override fun onBindViewHolder(holder: androidx.recyclerview.widget.RecyclerView.ViewHolder, position: Int) {
        val session = sessions[position]
        (holder as ViewHolder).bindItem(session)
    }

    override fun getItemCount(): Int {
        return sessions.size
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    inner class ViewHolder(var context: Context, val view: ViewGroup) :
            androidx.recyclerview.widget.RecyclerView.ViewHolder(view) {


        fun bindItem(session: MovieTheaterEvent) {
            view.textViewMovieName.text = session.name
            view.textViewAboutMovie.visibility = View.VISIBLE
            view.arrow.visibility = View.VISIBLE

            view.textViewAboutMovie.setOnClickListener {

                navigationControler?.legacyApp()?.goToMovieAboutActivity(view.context, session.id!!,
                        session.name!!, "Filme", "14",
                        "100min", session.description!!, session.image)
            }

            session?.sessions.details?.forEach { detail ->
                val view = LayoutInflater.from(context)
                        .inflate(R.layout.view_theater_session_component_list_item_detail,
                                view, false)
                itemView.detail_container.addView(view)

                view.room.text = detail.room
                if (detail.room.isNullOrBlank()) {
                    view.room.visibility = View.GONE
                }

                val tagTheaterComponentListAdapter = TagTheaterComponentListAdapter(detail.labels!!)

                view.recyclerViewTag.layoutManager = androidx.recyclerview.widget
                        .LinearLayoutManager(context, androidx.recyclerview.widget
                                .LinearLayoutManager.HORIZONTAL, false)
                view.recyclerViewTag.adapter = tagTheaterComponentListAdapter

                detail.info?.let { info ->
                    val sessionComponentListTimes = SessionTheaterComponentListTimes(info)
                    sessionComponentListTimes.onClickListener = {
                        onClickListener?.invoke(it)
                    }
                    view.recyclerViewTimeSession.layoutManager = androidx.recyclerview.widget
                            .LinearLayoutManager(context, androidx.recyclerview.widget.LinearLayoutManager.HORIZONTAL, false)
                    view.recyclerViewTimeSession.adapter = sessionComponentListTimes
                }
            }
        }
    }
}