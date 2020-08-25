package br.com.goin.feature_all_categories

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import br.com.goin.base.utils.Constants
import br.com.goin.component.navigation.NavigationController
import br.com.goin.component.session.user.location.UserLocation
import kotlinx.android.synthetic.main.activity_categories_list.*
import br.com.goin.feature_all_categories.model.CategoryDetails

private const val CATEGORIES = "CATEGORIES"

class CategoriesActivity : AppCompatActivity(), CategoriesView {

    private val presenter: CategoriesPresenter = CategoriesPresenterImpl(this)
    private var mUserLocation: UserLocation? = null
    private val navigationControler = NavigationController.instance

    companion object {
        fun starter(categories: ArrayList<CategoryDetails>, context: Context) {
            val intent = Intent(context, CategoriesActivity::class.java)
            intent.putExtra(CATEGORIES, categories)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_categories_list)

        presenter.onReceivedCategories(intent.getSerializableExtra(CATEGORIES))
        presenter.onCreate()
    }

    override fun configureToolbar() {
        toolbar.setTitle(getString(R.string.categories_title))
        toolbar.setOnBackButtonClicked { onBackPressed() }
    }

    override fun configureCurrentLocation(userLocation: UserLocation) {
        mUserLocation = userLocation
    }

    override fun configureCategories(categories: ArrayList<CategoryDetails>) {
        val adapter = CategoriesAdapter(categories)

        adapter.onClickListener = { categorieModel ->
            when (categorieModel.categoryType) {
                CategoryDetails.CategoryType.MOVIE -> {
                    navigationControler?.theatherPlayMovieModule()?.goToMovieActivity(this, categorieModel.id, categorieModel.name)
                }
                CategoryDetails.CategoryType.THEATER -> {
                    navigationControler?.theatherPlayMovieModule()?.goToTheatersActivity(this, categorieModel.id, categorieModel.name)
                }
                else -> onCategorieClicked(categorieModel)
            }
        }

        recycler_view.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this)
        recycler_view.adapter = adapter
    }

    private fun onCategorieClicked(categorie: CategoryDetails) {
        val data = Bundle()
        data.putString(Constants.QUERY, "")
        data.putString(Constants.CATEGORY_NAME, categorie.name)
        data.putString(Constants.CATEGORY_ID, categorie.id)
        data.putString(Constants.CATEGORY_TYPE, categorie.type?.name)
        data.putSerializable(Constants.CATEGORY, categorie)
    }
}