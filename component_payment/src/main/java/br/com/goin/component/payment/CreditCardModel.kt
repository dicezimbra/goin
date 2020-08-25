package br.com.goin.component.payment

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class CreditCardModel(@SerializedName("number")
                           var number: String? = null,
                           @SerializedName("name")
                           var name: String? = null,
                           @SerializedName("expireDate")
                           var expireDate: String? = null,
                           @SerializedName("cvv")
                           var cvv: String? = null,
                           @SerializedName("birthday")
                           var birthday: String? = null,
                           @SerializedName(" cpf")
                           var cpf: String? = null,
                           @SerializedName("creditCardType")
                           var creditCardType: CreditCardHelper.CardType = CreditCardHelper.CardType.INVALID,
                           var selected: Boolean = false) : Serializable
