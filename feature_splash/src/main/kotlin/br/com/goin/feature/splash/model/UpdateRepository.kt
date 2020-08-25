package br.com.goin.feature.splash.model

import br.com.goin.base.BuildConfig
import br.com.goin.base.dtos.GraphqlQuery
import br.com.goin.component.rest.api.RetrofitService
import br.com.goin.component.rest.api.graphQL.GraphQLResponse
import io.reactivex.Observable

class UpdateRepository{

    companion object {
        var service = RetrofitService(UpdateApi::class.java, BuildConfig.BASE_URL)
    }

    fun hasUpdates(version: String): Observable<GraphQLResponse<QueryResponse>> {
        val graphqlQuery = GraphqlQuery.builder(UpdateQueries.HAS_UPDATES)
                .`var`("version", version)
                .`var`("os", "ANDROID")
                .build()

        return service.apiService.hasUpdates(graphqlQuery)
    }
}