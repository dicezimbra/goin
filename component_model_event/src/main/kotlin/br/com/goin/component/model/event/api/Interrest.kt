package br.com.goin.component.model.event.api

import com.google.gson.annotations.SerializedName

data class Interrest(@SerializedName("action")val action: String,
                     @SerializedName("actionValue")val actionValue: String,
                     @SerializedName("name")val name: String? = null,
                     @SerializedName("image")val image: String? = null)