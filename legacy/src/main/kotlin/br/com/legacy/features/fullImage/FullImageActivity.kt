package br.com.legacy.features.fullImage

import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import br.com.R
import br.com.legacy.managers.SessionManager
import br.com.legacy.utils.Constants
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import kotlinx.android.synthetic.main.activity_full_image.*

class FullImageActivity : AppCompatActivity(), FullImageView {

    private val presenter: FullImagePresenter = FullImagePresenterImpl(this)

    companion object {
        fun starter(context: Context, anyType: String) {
            val intent = Intent(context, FullImageActivity::class.java)
            intent.putExtra(Constants.UPDATED_USER, anyType)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_full_image)

        presenter.onReceiveExtras(intent.getStringExtra(Constants.UPDATED_USER))
    }



    override fun setImage(it: String) {
        Glide.with(this)
                .load(it)
                .apply(RequestOptions().signature(SessionManager.getInstance().getSignature(this@FullImageActivity, SessionManager.ImageType.MyProfile)))
                .apply(RequestOptions().placeholder(R.drawable.profile_placerholder))
                .apply(RequestOptions.errorOf(R.drawable.profile_placerholder))
                .listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                        return true
                    }

                    override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                        return false
                    }

                })
                .into(full_image_view)
    }
}
