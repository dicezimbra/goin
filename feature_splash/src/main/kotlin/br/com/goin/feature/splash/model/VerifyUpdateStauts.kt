package br.com.goin.feature.splash.model

import com.google.gson.annotations.SerializedName

class VerifyUpdateStauts {

    @SerializedName("status")
    var status: String? = null

    companion object {

        val force = "force"
        val optional = "optional"
        val ok = "ok"
    }
}
