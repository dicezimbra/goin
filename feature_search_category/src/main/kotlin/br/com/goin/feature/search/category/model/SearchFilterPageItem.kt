package br.com.goin.feature.search.category.model

import br.com.goin.base.utils.GpsUtils
import java.io.Serializable
import java.util.*

 data class SearchFilterPageItem(val name: String,
                                val type: Type,
                                val image: String?,
                                val city: String?,
                                val lat: Double?,
                                val lng: Double?,
                                val subcategory: String? = "",
                                val id: String,
                                val rating: Float? ,
                                val address: String? = "",
                                 val logo: String? = "",
                                val state: String? = "",
                                val startDate: Calendar?,
                                val price: Int?,
                                var distance: Float ) : Serializable {

     override fun equals(other: Any?): Boolean {
         if (this === other) return true
         if (javaClass != other?.javaClass) return false

         other as SearchFilterPageItem

         if (name != other.name) return false
         if (type != other.type) return false
         if (image != other.image) return false
         if (city != other.city) return false
         if (lat != other.lat) return false
         if (lng != other.lng) return false
         if (subcategory != other.subcategory) return false
         if (id != other.id) return false
         if (rating != other.rating) return false
         if (address != other.address) return false
         if (state != other.state) return false
         if (price != other.price) return false

         return true
     }

     override fun hashCode(): Int {
         var result = name.hashCode()
         result = 31 * result + type.hashCode()
         result = 31 * result + (image?.hashCode() ?: 0)
         result = 31 * result + (city?.hashCode() ?: 0)
         result = 31 * result + (lat?.hashCode() ?: 0)
         result = 31 * result + (lng?.hashCode() ?: 0)
         result = 31 * result + (subcategory?.hashCode() ?: 0)
         result = 31 * result + id.hashCode()
         result = 31 * result + (rating?.hashCode() ?: 0)
         result = 31 * result + (address?.hashCode() ?: 0)
         result = 31 * result + (state?.hashCode() ?: 0)
         result = 31 * result + (price ?: 0)
         return result
     }

     fun getFormattedDistance( location : Pair<Double, Double>?): String{
         if(distance == null  || distance == 0F) distance = GpsUtils.calculate(location?.first, location?.second, lat, lng)
         return GpsUtils.formatt(distance)
     }

     fun fetchDistance( location : Pair<Double, Double>?): Float {
         if(distance == null || distance == 0F) distance = GpsUtils.calculate(location?.first, location?.second, lat, lng)
         return distance
     }
 }

enum class Type {
    EVENT, CLUB
}
