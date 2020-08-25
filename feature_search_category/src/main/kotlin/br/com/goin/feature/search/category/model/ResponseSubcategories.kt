package br.com.goin.feature.search.category.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class ResponseSubcategories(@SerializedName("subcategories") val subcategories: List<SubcategoriesModel>): Serializable