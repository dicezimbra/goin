package br.goin.features.login.login

import com.google.gson.annotations.SerializedName

data class LoginResponse(@SerializedName("email")val email: String,
                         @SerializedName("token")val token: String,
                         @SerializedName("refreshToken")val refreshToken: String)