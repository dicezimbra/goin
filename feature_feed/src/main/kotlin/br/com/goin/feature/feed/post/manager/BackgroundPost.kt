package br.com.goin.feature.feed.post.manager

import android.graphics.Bitmap
import android.util.Log
import androidx.work.Worker
import br.com.goin.base.BaseApplication
import br.com.goin.base.extensions.ioThread
import br.com.goin.feature.feed.model.Post
import br.com.goin.feature.feed.model.PostInteractor
import br.com.goin.feature.feed.post.CreatePost
import br.com.goin.feature.feed.post.PostActivityHelper
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import java.lang.Exception


class BackgroundPost : Worker() {

    private var interactorPost: PostInteractor = PostInteractor.instance

    override fun doWork(): WorkerResult {

        try {

            val postData = inputData.getString("postData", "")
            val mediaImageData = inputData.getString("mediaImageData", "")
            val mediaFilePathData = inputData.getString("mediaFilePathData", "")
            val idData = inputData.getString("idData", "")
            val idClub = inputData.getString("idClub", null)

            val postFinal = Gson().fromJson(postData, CreatePost::class.java)
            val mediaImageFinal = Gson().fromJson(mediaImageData, Bitmap::class.java)

            val createPostResponse =
                    interactorPost.createPost(post = postFinal, mediaImage = mediaImageFinal, mediaFilePath = mediaFilePathData, id = idData,
                            clubId= idClub)
                    .blockingFirst()

            if (createPostResponse != null) {
                return WorkerResult.SUCCESS
            }

        } catch (ex: Exception) {
            Log.e("BackgroundPostEx", ex.localizedMessage)
        }

        return WorkerResult.FAILURE

    }


}

