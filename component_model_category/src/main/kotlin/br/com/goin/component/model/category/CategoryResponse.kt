package br.com.goin.component.model.category

import com.google.gson.annotations.SerializedName

class CategoryResponse(@SerializedName("categories") var categories: List<Category> = arrayListOf())