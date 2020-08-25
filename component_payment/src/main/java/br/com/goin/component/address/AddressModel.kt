package br.com.goin.component.address

import com.google.gson.annotations.SerializedName

class AddressModel {

    @SerializedName("cep")
    var cep : String? = null

    @SerializedName("logradouro")
    var logradouro : String? = null

    @SerializedName("complemento")
    var complemento : String? = null

    @SerializedName("bairro")
    var bairro : String? = null

    @SerializedName("localidade")
    var localidade : String? = null

    @SerializedName("uf")
    var uf : String? = null

    @SerializedName("unidade")
    var unidade : String? = null

    @SerializedName("ibge")
    var ibge : String? = null

    @SerializedName("gia")
    var gia : String? = null

    @SerializedName("label")
    var label : String? = null


    var number: String? = null
    var nickname: String? = null

    var selected : Boolean = false
}