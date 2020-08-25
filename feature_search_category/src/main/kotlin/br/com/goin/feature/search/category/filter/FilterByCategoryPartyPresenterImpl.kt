package br.com.goin.feature.search.category.filter

import android.util.Log
import br.com.goin.base.extensions.ioThread
import br.com.goin.base.extensions.useCache
import br.com.goin.feature.search.category.filter.model.ChipTagResponse
import br.com.goin.feature.search.category.filter.model.api.FilterByCategoryPartyInteractorImpl
import br.com.goin.feature.search.category.model.ResponseSubcategories
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo

class FilterByCategoryPartyPresenterImpl(val view: FilterByCategoryPartyView) : FilterByCategoryPartyPresenter {

    private val interactor = FilterByCategoryPartyInteractorImpl.instance
    private var selectedSubcategorie: Int? = null
    private val disposible = CompositeDisposable()

    override fun onCreate() {
        view.configureToolbar()
        view.configureView()
        view.configureExtras()
        view.configureRangeBar()
        getChipsTag()
    }

    override fun onDestroy(){
        disposible.dispose()
    }

    override fun receiveSubCategories(subcategories: ResponseSubcategories) {
        view.receiveSubCategories(subcategories, selectedSubcategorie)
    }

    override fun receiveSelectedSubCategories(subCategory: Any?) {
        this.selectedSubcategorie = subCategory as? Int
    }

    fun getChipsTag(){
        interactor.getChipTags()
                .useCache("CHIP_TAGS_PARTY", ChipTagResponse::class.java)
                .ioThread()
                .subscribe({
                    view.receiveChipTags(it)
                },{
                    Log.e("FilterByCategory", it.message)
                }).addTo(disposible)
    }
}