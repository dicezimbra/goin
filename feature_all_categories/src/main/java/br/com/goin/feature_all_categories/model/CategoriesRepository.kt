package br.com.goin.model.categories

import br.com.goin.base.BuildConfig
import br.com.goin.base.GraphqlQuery
import br.com.goin.component.rest.api.RetrofitService
import io.reactivex.Observable
import br.com.goin.feature_all_categories.model.CategoryDetails
class CategoriesRepository {

    private val service = RetrofitService(CategoriesApi::class.java, BuildConfig.BASE_URL)

    fun getCategories(): Observable<List<CategoryDetails>> {
        val graphqlQuery = GraphqlQuery.builder(CategoriesQueries.GET_CATEGORIES)
                .build()

        return service.apiService.getCategories(graphqlQuery).map { (it.data?.categories) }
    }
}