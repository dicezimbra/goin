package br.com.goin.component_model_club.model

import java.io.Serializable
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols

import br.com.goin.component.model.event.Event
import br.com.goin.component.model.event.api.response.ApiVoucher

data class ClubModel(var clubId: String = "",
                     var clubSite: String? = null,
                     var followers: Int? = null,
                     var clubName: String? = null,
                     var clubLocation: String? = null,
                     var clubDescription: String? = null,
                     var clubShortDescription: String? = null,
                     var address: String? = null,
                     var events: List<Event>? = null,
                     var coverImage: String? = null,
                     var clubLogoUrl: String? = null,
                     var rating: Float? = null,
                     var ratingCount: Int? = null,
                     var galleryUrls: MutableList<String>? = null,
                     var distance: Float? = null,
                     var hasLogo: Boolean = false,
                     var latitude: Float? = null,
                     var longitude: Float? = null,
                     var subcategory: String? = null,
                     var priceRating: Int? = null,
                     var isFollowing: Boolean = false,
                     var tags: List<Tag>? = null,
                     var giftsGallery : List<ApiVoucher>? = null) : Serializable


