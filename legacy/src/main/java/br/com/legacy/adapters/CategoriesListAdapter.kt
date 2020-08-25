package br.com.legacy.adapters

import android.app.Activity

import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

import java.util.ArrayList

import br.com.legacy.adapters.viewHolders.SubCategoriesViewHolder
import br.com.legacy.models.ClubModel
import br.com.legacy.models.SearchFilterModel
import br.com.legacy.models.SearchModel
import br.com.R
import br.com.goin.component.model.event.Event
import br.com.goin.component.navigation.NavigationController

class CategoriesListAdapter(var activity: Activity, var mySearchResultClubModel: SearchFilterModel, var categoryName: String) : RecyclerView.Adapter<SubCategoriesViewHolder>() {

    var mySearchResult: MutableList<SearchModel> = ArrayList()
    val navigationControler = NavigationController.instance

    init {
        addSearch(mySearchResultClubModel)
    }

    fun addSearch(searchFilterModel: SearchFilterModel) {
        mySearchResult = convertEvents(searchFilterModel.events)
        mySearchResult.addAll(convertClubs(searchFilterModel.clubModels))
    }

    fun addSearchRefresh(searchFilterModel: SearchFilterModel) {
        mySearchResult.addAll(convertEvents(searchFilterModel.events))
        mySearchResult.addAll(convertClubs(searchFilterModel.clubModels))
    }

    private fun convertClubs(clubModels: List<ClubModel>): List<SearchModel> {

        val o = ArrayList<SearchModel>()

        for (i in clubModels) {
            o.add(convertClub(i))
        }
        return o

    }

    private fun convertClub(i: ClubModel): SearchModel {
        return SearchModel(i.clubName, i.clubId, TYPE_CLUB, i.clubLogoUrl, i.rating)
    }

    private fun convertEvents(events: List<Event>): MutableList<SearchModel> {
        val o = ArrayList<SearchModel>()

        for (i in events) {
            o.add(convertEvent(i))
        }
        return o
    }

    private fun convertEvent(i: Event): SearchModel {
        return SearchModel(i.name, i.id, TYPE_EVENT, i.photoUrl)
    }

    @NonNull
    override fun onCreateViewHolder(@NonNull parent: ViewGroup, viewType: Int): SubCategoriesViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.new_item_category_establishment, parent, false)
        return SubCategoriesViewHolder(view)
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun onBindViewHolder(@NonNull holder: SubCategoriesViewHolder, position: Int) {
        val clubModel = mySearchResult[position]
        //val clubModelExtras = if (mySearchResultClubModel.clubModels.size > 0)
        //    mySearchResultClubModel.clubModels[position] else null

        if (holder.constraintEstablishmentInfo != null)
            holder.constraintEstablishmentInfo.setOnClickListener {
                if (categoryName == CINEMA_CATEGORY) {

                    navigationControler?.legacyApp()?.goToTheatersDetailActivity(
                            activity, clubModel.id, "", categoryName)
                    return@setOnClickListener
                } else if (clubModel.type == TYPE_CLUB) {
                    navigationControler?.placeModule()?.goToPlace(activity, clubModel.id, categoryName)
                } else if (clubModel.type == TYPE_EVENT) {
                    navigationControler?.eventModule()?.goToEvent(activity, clubModel.id, "")
                }
            }

        if (clubModel.name == null || clubModel.name.length == 0) {
            holder.txtEstablishmentNameCategory.visibility = View.GONE
        }
        holder.txtEstablishmentNameCategory.text = clubModel.name


        Glide.with(activity)
                .load(clubModel.image)
                .apply(RequestOptions.placeholderOf(R.drawable.profile_placerholder))
                .apply(RequestOptions.errorOf(R.drawable.profile_placerholder))
                .into(holder.imgEstablishmentCategory)


        holder.txtCategorySubCategory.text = categoryName
        if (clubModel.rating != null)
            holder.ratingBarCategories.text = clubModel.rating.toString()
        else
            holder.starBarCategories.visibility = View.GONE
    }

    override fun getItemCount(): Int {
        return mySearchResult.size
    }

    companion object {

        var TYPE_CLUB = "CLUB"
        var TYPE_EVENT = "EVENT"
        val CINEMA_CATEGORY = "Cinema"
    }


}
