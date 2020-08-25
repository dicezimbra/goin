package br.com.goin.component.address

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class UserAddressInput(@SerializedName("updateAddress") var address: String) : Serializable