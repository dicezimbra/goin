package br.goin.features.login.register

import com.google.gson.annotations.SerializedName

data class NewRegisterUserResponse(@SerializedName("token")val token: String,
                                   @SerializedName("id")val id:String,
                                   @SerializedName("refreshToken")val refreshToken: String,
                                   @SerializedName("email")val email: String,
                                   @SerializedName("name")val name: String,
                                   @SerializedName("accountProviders")val accountProviders: List<String> = arrayListOf())
