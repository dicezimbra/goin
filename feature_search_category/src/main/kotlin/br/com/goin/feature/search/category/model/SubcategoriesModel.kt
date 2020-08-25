package br.com.goin.feature.search.category.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class SubcategoriesModel(@SerializedName("name")val name: String,
                         @SerializedName("subcategoryId")val subcategoryId: String,
                         @SerializedName("categoryId")val categoryId: String?,
                         @SerializedName("image")val image: String): Serializable

