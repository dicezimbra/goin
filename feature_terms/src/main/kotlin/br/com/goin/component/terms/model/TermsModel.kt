package br.com.goin.component.terms.model

import com.google.gson.annotations.SerializedName

data class TermsModel(@SerializedName("title") var title: String,@SerializedName("description") var description: String)

data class TermsListModel(@SerializedName("data")var data: List<TermsModel>)