package br.com.goin.component.rest.api.graphQL

import com.google.gson.annotations.SerializedName
import java.util.HashMap

class GraphqlBody(@SerializedName("query") var query: String,
                  @SerializedName("variables") var variables: MutableMap<String, Any?>) {

    class Builder(private val query: String) {

        @SerializedName("variables")
        private var variables: MutableMap<String, Any?> = HashMap()

        fun `var`(key: String, value: Any?): Builder {
            variables[key] = value
            return this
        }

        fun build(): GraphqlBody {
            return GraphqlBody(this.query, this.variables)
        }
    }

    fun builder(query: String): Builder {
        return Builder(query)
    }
}

