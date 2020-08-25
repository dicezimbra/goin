package br.com.goin.component.model.category

import java.io.Serializable

class Category(var name: String = "",
               var id: String = "",
               var image: String? = null,
               var imageIcon: String? = null,
               var selected: Boolean? = null,
               var type: Type? = null,
               var imageWhite: String? = null,
               var categoryType: CategoryType? = null,
               var bannerCategory: String? = null,
               var actionId: String? = null): Serializable {

    val isEvent: Boolean
        get() = type != null && Type.place != type

    enum class Type {
        event, place
    }

    enum class CategoryType {
        EVENT, THEATER, PLACE, MOVIE
    }
}
