package br.com.goin.component.payment

import com.google.gson.annotations.SerializedName

class CreditCardModelList {

    @SerializedName("creditCards")
    var list: ArrayList<CreditCardModel> = ArrayList()

}