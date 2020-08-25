package br.com.goin.component_model_club.model

import br.com.goin.component.model.event.api.response.ApiEvent
import br.com.goin.component.model.event.api.response.ApiVoucher
import br.com.goin.component.session.user.UserCardDetails
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Club(@SerializedName("id")
                var id: String? = null,
                @SerializedName("name")
                var name: String? = null,
                @SerializedName("followersCount")
                var followersCount: Int? = null,
                @SerializedName("logoImage")
                var logoImage: String? = null,
                @SerializedName("subcategoriesInfo")
                var subcategories: List<SubcategoriesInfo> = arrayListOf(),
                @SerializedName("coverImage")
                var coverImage: String? = null,
                @SerializedName("description")
                var description: String? = null,
                @SerializedName("tags")
                var tags: List<Tag>? = null,
                @SerializedName("address")
                var address: String? = null,
                @SerializedName("rating")
                var rating: Float = 0.toFloat(),
                @SerializedName("ratingCount")
                var ratingCount: Int? = null,
                @SerializedName("followedByMe")
                var followedByMe: Boolean? = null,
                @SerializedName("events")
                var events: List<ApiEvent>? = null,
                @SerializedName("photos")
                var photoGallery: List<String>? = null,
                @SerializedName("website")
                var website: String? = null,
                @SerializedName("followers")
                var followers: List<UserCardDetails>? = null,
                @SerializedName("latitude")
                var latitude: Float? = null,
                @SerializedName("city")
                var city: String? = null,
                @SerializedName("longitude")
                var longitude: Float? = null,
                @SerializedName("club")
                var club: Club? = null,
                @SerializedName("priceRating")
                var priceRating: Int? = null,
                @SerializedName("state")
                var state: String? = null,
                var giftsGallery: List<ApiVoucher>? = null) : Serializable

data class PhotoGalleryItem(@SerializedName("url") var url: String? = null)
data class Tag(@SerializedName("name") val name: String? = null)