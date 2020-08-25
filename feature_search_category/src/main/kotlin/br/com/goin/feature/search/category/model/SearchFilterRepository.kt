package br.com.goin.feature.search.category.model

import br.com.goin.base.BuildConfig
import br.com.goin.base.GraphqlQuery
import br.com.goin.component.rest.api.RetrofitService
import br.com.goin.component.rest.api.graphQL.GraphQLResponse
import br.com.goin.component.rest.api.graphQL.GraphqlBody
import br.com.goin.feature.search.category.adapter.SearchFilterApi
import br.com.goin.feature.search.category.model.api.SearchFilterQuery
import br.com.goin.feature.search.category.model.api.SearchFilterRequest
import br.com.goin.feature.search.category.model.api.SearchFilterResponse
import io.reactivex.Observable

class SearchFilterRepository{

    companion object {
        var service = RetrofitService(SearchFilterApi::class.java, BuildConfig.BASE_URL)
    }

    fun search(request: SearchFilterRequest, limit: Int, page: Int): Observable<GraphQLResponse<SearchFilterResponse>> {
        val graphqlQuery = GraphqlBody.Builder(SearchFilterQuery.FILTER_RESULTS)
                .`var`("input", request)
                .`var`("limit", limit)
                .`var`("paginate", page)
                .build()

        return service.apiService.search(graphqlQuery)
    }

    fun getSubCategories(categoryId: String) : Observable<GraphQLResponse<ResponseSubcategories>>{
        val graphqlQuery = GraphqlQuery.Builder(SearchFilterQuery.GET_SUBCATEGORIES)
                .`var`("categoryId", categoryId)
                .build()

        return service.apiService.getSubcategories(graphqlQuery)
    }
}