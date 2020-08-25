package br.com.goin.feature.splash.model

import com.google.gson.annotations.SerializedName

class QueryResponse {

    @SerializedName("getVersionStatus")
    var getVersionStatus: VerifyUpdateStauts? = null
}
