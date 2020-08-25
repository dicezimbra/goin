package br.com.goin.component.model.category

import br.com.goin.base.BuildConfig
import br.com.goin.component.rest.api.RetrofitService
import br.com.goin.component.rest.api.graphQL.GraphqlBody
import io.reactivex.Observable

class CategoriesRepository {

    private val service = RetrofitService(CategoriesApi::class.java, BuildConfig.BASE_URL)

    fun getCategories(): Observable<List<Category>> {
        val graphqlQuery = GraphqlBody.Builder(CategoriesQueries.GET_CATEGORIES)
                .build()

        return service.apiService.getCategories(graphqlQuery).map { (it.data?.categories) }
    }
}