package br.com.goin.component.address

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class UserAddressList(@SerializedName ("userAddress") var addresses: List<UserAddress>) : Serializable