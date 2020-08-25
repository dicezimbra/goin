package br.com.goin.feature.rating

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.goin.component.navigation.NavigationController
import br.com.goin.component.session.user.UserModel
import br.com.goin.component.ui.kit.dialog.DialogUtils
import br.com.goin.component.ui.kit.features.error.ErrorViewHelper
import br.com.goin.component_model_club.model.ClubModel
import br.com.goin.component_model_club.model.ClubRatingCardModel
import br.com.goin.component_model_club.model.ClubRatingModel
import br.com.goin.feature.R
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.activity_rating.*

class RatingActivity : AppCompatActivity(), RatingView{



    private val presenter: RatingPresenter = RatingPresenterImpl(this)
    private var navigationController = NavigationController.instance
    var adapter : RatingAdapter? = null
    val list = ArrayList<ClubRatingCardModel>()
    private var progressDialog : ProgressDialog? = null

    companion object{
        private const val CLUB = "CLUB"
        fun starter(context: Context, clubid: String){
            val intent = Intent(context, RatingActivity::class.java)
            intent.putExtra(CLUB, clubid)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rating)

        presenter.onReceiveClub(intent?.extras?.getString(CLUB))
        presenter.loadUserInfos()
        presenter.loadRatings()

        toolbar.setOnBackButtonClicked {
            this.finish()
        }
    }

    override fun loadRatings(clubModel: ClubRatingModel?) {
        clubModel?.ratingsList?.let {
            list.clear()
            list.addAll(it)
            adapter = RatingAdapter(list)
            adapter!!.onClickListener = { categorieModel ->

            }
            recycler_view.adapter = adapter
            recycler_view.layoutManager = LinearLayoutManager(this@RatingActivity, RecyclerView.VERTICAL, false)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.onDestroy()
    }


    override fun hideLoading() {
        progressDialog?.let {
            it.hide()
            progressDialog = null
        }
    }


    override fun handleError(t: Throwable?) {
        progressDialog?.let {
            it.hide()
            progressDialog = null
        }
        t?.let {
           Toast.makeText(this, getString(R.string.error_rating), Toast.LENGTH_LONG).show()
        }
    }

    override fun showLoading() {
        progressDialog?.let {
            it.dismiss()
        }

        progressDialog = DialogUtils.createProgressDialog(this)
        progressDialog?.show()
    }

    override fun loadUserInfos(it : UserModel?) {
        Glide.with(this)
                .load(it?.profilePicture)
                .apply(RequestOptions.circleCropTransform().placeholder(R.drawable.user_icon_rating))
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(user_image)

        user_name.text = it?.name

        post_button.setOnClickListener {
            presenter.userRating(rating_bar.rating, txt_comment.text.toString())
        }
    }

    override fun ratingSuccess(it: ClubRatingCardModel) {
        list.add(0, it)
        adapter?.notifyItemInserted(0)
        txt_comment.setText("")
    }
}
