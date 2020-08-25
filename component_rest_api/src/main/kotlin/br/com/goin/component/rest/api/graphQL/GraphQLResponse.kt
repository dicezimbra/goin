package br.com.goin.component.rest.api.graphQL

import com.google.gson.annotations.SerializedName
import org.json.JSONObject
import java.io.Serializable

import java.util.ArrayList

class GraphQLResponse<T>: Serializable {

    @SerializedName("status")
    var status: String? = null
        internal set
    @SerializedName("message")
    var message: String? = null
        internal set
    @SerializedName("name")
    var name: String? = null
        internal set
    @SerializedName("data")
    var data: T? = null

    @SerializedName("errors")
    var errors: List<GraphqlErrors> = ArrayList()

    inner class GraphqlErrors : Serializable {

        @SerializedName("message")
        var message: String? = null
        @SerializedName("name")
        var name: String? = null
        @SerializedName("locations")
        var locations: List<JSONObject>? = null
        @SerializedName("path")
        var path: List<String>? = null
    }

}
