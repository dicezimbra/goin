package br.com.goin.feature.full.image

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import br.com.goin.component.analytics.analytics.Analytics
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_ful_image.*

class FullImageActivity : AppCompatActivity(), FullImageView {

    private val presenter: FullImagePresenter = FullImagePresenterImpl(this)

    companion object {
        private const val KEY_URL = "URL"

        fun starter(activity: Activity, view: View, url: String) {
            val intent = Intent(activity, FullImageActivity::class.java)
            intent.putExtra(KEY_URL, url)

            val options = ActivityOptionsCompat.makeSceneTransitionAnimation(activity, view, "photo")
            activity.startActivity(intent, options.toBundle())
        }

        fun starter(context: Context, url: String) {
            val intent = Intent(context, FullImageActivity::class.java)
            intent.putExtra(KEY_URL, url)
            context.startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        Analytics.instance.screenView(this, getString(R.string.post_full_image_screen_view))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_ful_image)
        presenter.onReceiveExtras(intent.getStringExtra(KEY_URL))
    }

    override fun setImage(it: String) {
        Glide.with(this)
                .load(it)
                .into(full_image_view)
    }
}
