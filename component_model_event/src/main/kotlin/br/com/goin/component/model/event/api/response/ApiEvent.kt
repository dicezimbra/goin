package br.com.goin.component.model.event.api.response

import br.com.goin.component.model.event.Event
import br.com.goin.component.model.event.api.CategoriesInfo
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class ApiEvent(@SerializedName("description") var description: String? = null,
                    @SerializedName("id") var id: String = "",
                    @SerializedName("clubId")var clubId: String = "",
                    @SerializedName("image")var image: String = "",
                    @SerializedName("name")var name: String = "",
                    @SerializedName("club")var club: String = "",
                    @SerializedName("placeName")var placeName: String? = null,
                    @SerializedName("placeAddress")var placeAddress: String? = null,
                    @SerializedName("startDate")var startDate: Long? = null,
                    @SerializedName("endDate")var endDate: Long? = null,
                    @SerializedName("categoryName")var categoryName: String? = null,
                    @SerializedName("interestedCount")var interestedCount: Int? = null,
                    @SerializedName("checkInCount")var checkInCount: Int? = null,
                    @SerializedName("allConfirmedCount")var allConfirmedCount: Int? = null,
                    @SerializedName("latitude")var latitude: Float? = null,
                    @SerializedName("longitude")var longitude: Float? = null,
                    @SerializedName("highestPrice")var highestPrice: Int? = null,
                    @SerializedName("categoryType")var categoryType: String? = null,
                    @SerializedName("isConfirmed")var isConfirmed: Boolean = false,
                    @SerializedName("city") var city: String?,
                    @SerializedName("lowestPrice")var lowestPrice: Int? = null,
                    @SerializedName("originId")var originId: String? = null,
                    @SerializedName("originType")var originType: String? = null,
                    @SerializedName("originAction")var originAction: String? = null,
                    @SerializedName("originURL")var originURL: String? = null,
                    @SerializedName("subcategoriesInfo") var subcategories: List<SubcategoriesInfo> = arrayListOf(),
                    @SerializedName("originHasDiscount")var originHasDiscount: Boolean = false,
                    @SerializedName("imageWidth")var imageWidth: String? = null,
                    @SerializedName("imageHeight")var imageHeight: String? = null,
                    @SerializedName("imageAspect")var imageAspect: String? = null,
                    @SerializedName("originInfos") var originInfos: OriginInfos? = null,
                    @SerializedName("categoryEventType") var categoryEventType: String? = null,
                    @SerializedName("categoriesInfo") var categoriesInfos: List<CategoriesInfo> = arrayListOf(),
                    @SerializedName("confirmType") var confirmType: Event.EventConfirmationType? = null,
                    @SerializedName("rating")  var rating: Float? = null,
                    @SerializedName("state")  var state: String? = null,
                    @SerializedName("giftsGallery") var giftsGallery: List<ApiVoucher> = arrayListOf()) : Serializable

