package br.com.goin.feature.search.event.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import br.com.goin.base.extensions.gone
import br.com.goin.base.extensions.ioThread
import br.com.goin.base.extensions.visible
import br.com.goin.base.utils.DateUtils
import br.com.goin.base.utils.GpsUtils
import br.com.goin.feature.search.event.model.SearchEventType
import br.com.goin.feature.search.event.model.api.SearchEvent
import br.com.goin.feature.search.event.R
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.google.gson.Gson
import kotlinx.android.synthetic.main.item_search_event.view.*
import kotlinx.android.synthetic.main.item_search_movie.view.*
import kotlinx.android.synthetic.main.item_search_place.view.*
import java.util.*

class SearchEventAdapter(val searchEventList: MutableList<SearchEvent>) : RecyclerView.Adapter<SearchEventAdapter.SearchViewHolder>() {

    private val PLACE = 0
    private val EVENT = 1
    private val MOVIE_THEATER = 2

    var lat: Double? = null
    var lng: Double? = null

    var onClickListener: (searchEvent: SearchEvent) -> Unit = {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        return when (viewType) {
            PLACE -> {
                SearchViewHolder(LayoutInflater.from(parent.context)
                        .inflate(R.layout.item_search_place, parent, false))
            }
            EVENT -> {
                SearchViewHolder(LayoutInflater.from(parent.context)
                        .inflate(R.layout.item_search_event, parent, false))
            }
            MOVIE_THEATER -> {
                SearchViewHolder(LayoutInflater.from(parent.context)
                        .inflate(R.layout.item_search_movie, parent, false))
            }
            else -> {
                SearchViewHolder(LayoutInflater.from(parent.context)
                        .inflate(R.layout.item_search_place, parent, false))
            }
        }
    }

    override fun getItemCount(): Int {
        return searchEventList.size
    }

    override fun getItemViewType(position: Int): Int {
        return when (searchEventList.get(position).categoryType) {

            SearchEventType.PLACE.name -> {
                PLACE
            }
            SearchEventType.EVENT.name -> {
                EVENT
            }
            SearchEventType.MOVIE.name, SearchEventType.THEATER.name -> {
                MOVIE_THEATER
            }
            else -> {
                PLACE
            }
        }
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {

        val searchEvent = searchEventList[position]

        when (searchEvent.categoryType) {
            SearchEventType.PLACE.name -> {

                holder.placeName.text = searchEvent.name
                holder.subCategoryName.text = searchEvent.categorization

                Glide.with(holder.itemView.context)
                        .load(searchEvent.logoImage)
                        .apply(RequestOptions.circleCropTransform())
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .apply(RequestOptions().placeholder(R.drawable.placeholder_search_item))
                        .into(holder.imagePlace)

                holder.contraintLayoutParentPlace
                        .setOnClickListener { onClickListener(searchEvent) }

                loadDistance(holder.textViewDistanceClub, searchEvent)

                if (searchEvent.rating > 0.0f) {
                    val valueRating = String.format("%.1f", searchEvent.rating)
                    holder.starRating.text = valueRating
                    holder.starRating.visible()
                    holder.star.visible()
                } else {
                    holder.starRating.gone()
                    holder.star.gone()
                }
            }

            SearchEventType.EVENT.name -> {

                val startDate = Calendar.getInstance()
                startDate.timeInMillis = searchEvent.startDate!!
                holder.eventDay.text = if (DateUtils.getDay(startDate).length == 1)
                    "0${DateUtils.getDay(startDate)}" else DateUtils.getDay(startDate)
                holder.eventMonth.text = DateUtils.getMonth(startDate)
                holder.eventName.text = searchEvent.name
                holder.textViewSubCategoryEvent.text = searchEvent.categorization

                holder.contraintLayoutParentEvent
                        .setOnClickListener { onClickListener(searchEvent) }
                loadDistance(holder.textViewDistanceEvent, searchEvent)
            }
            SearchEventType.MOVIE.name, SearchEventType.THEATER.name -> {

                holder.textViewMovieName.text = searchEvent.name
                holder.textViewSubCategoryMovie.text = searchEvent.categorization

                Glide.with(holder.itemView.context)
                        .load(searchEvent.logoImage)
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .apply(RequestOptions().placeholder(R.drawable.placeholder_search_item))
                        .into(holder.imageViewMovie)

                holder.contraintLayoutParentMovie
                        .setOnClickListener { onClickListener(searchEvent) }
            }
        }
    }


    private fun loadDistance(textView: TextView, searchEvent: SearchEvent) {
        if (lat != null && lng != null && searchEvent.latitude != null && searchEvent.longitude != null) {
            val disposable = GpsUtils.getDistance(lat!!, lng!!, searchEvent.latitude, searchEvent.longitude)
                    .ioThread()
                    .subscribe({ distance ->
                        @SuppressLint("DefaultLocale") val placeShortKM = String.format("%.2f km", distance)
                        textView.setText(placeShortKM)
                        textView.setVisibility(View.VISIBLE)
                    }, { throwable -> })

            textView.setTag(disposable)
        } else {
            textView.setVisibility(View.INVISIBLE)
        }
    }


    class SearchViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        // place
        val placeName = itemView.textViewPlaceName
        val subCategoryName = itemView.textViewSubCategory
        val imagePlace = itemView.imageViewPlace
        val textViewDistanceClub = itemView.textViewDistanceClub
        val contraintLayoutParentPlace = itemView.contraintLayoutParentPlace
        val star = itemView.rating_search_place
        val starRating = itemView.rating_value_search_place
        // event
        val eventDay = itemView.txt_day
        val eventMonth = itemView.txt_month
        val eventName = itemView.textViewEventName
        val textViewSubCategoryEvent = itemView.textViewSubCategoryEvent
        val contraintLayoutParentEvent = itemView.contraintLayoutParentEvent
        val textViewDistanceEvent = itemView.textViewDistanceEvent
        // movie/ theater
        val imageViewMovie = itemView.imageViewMovie
        val textViewMovieName = itemView.textViewMovieName
        val textViewSubCategoryMovie = itemView.textViewSubCategoryMovie
        val contraintLayoutParentMovie = itemView.contraintLayoutParentMovie
    }
}