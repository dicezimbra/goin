package br.com.goin.component.model.event.api.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName



class StateResponse {
    @SerializedName("codigo_ibge")
    @Expose
    public val codigoIbge: Int? = null
    @SerializedName("nome_municipio")
    @Expose
    public val nomeMunicipio: String? = null
    @SerializedName("codigo_uf")
    @Expose
    public val codigoUf: Int? = null
    @SerializedName("uf")
    @Expose
    public val uf: String? = null
    @SerializedName("estado")
    @Expose
    public val estado: String? = null
    @SerializedName("capital")
    @Expose
    public val capital: Boolean? = null
    @SerializedName("latitude")
    @Expose
    public val latitude: Double? = null
    @SerializedName("longitude")
    @Expose
    public val longitude: Double? = null
    @SerializedName("distance")
    @Expose
    public val distance: Double? = null
}