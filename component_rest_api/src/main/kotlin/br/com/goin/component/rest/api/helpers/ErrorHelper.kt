package br.com.goin.component.rest.api.helpers

import br.com.goin.component.rest.api.graphQL.GraphQLResponse
import com.google.gson.Gson
import retrofit2.HttpException

object ErrorHelper {

    fun getErrorName(throwable: Throwable): String? {
        when (throwable) {
            is HttpException -> {
                val ex = Gson().fromJson(throwable.response().errorBody()?.string(), GraphQLResponse::class.java)
                return ex.name
            }
        }

        return null
    }

    fun getErrorMessage(throwable: Throwable): String? {
        when (throwable) {
            is HttpException -> {
                try {
                    val ex = Gson().fromJson(throwable.response().errorBody()?.string(), GraphQLResponse::class.java)
                    ex.errors.firstOrNull()?.let { return it.message }
                }catch (e: Exception){

                }
            }
        }

        return null
    }
}