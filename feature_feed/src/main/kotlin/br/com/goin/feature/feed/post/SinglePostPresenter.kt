package br.com.goin.feature.feed.post

import android.os.Bundle

import androidx.appcompat.app.AppCompatActivity
import br.com.goin.feature.feed.R
import br.com.goin.feature.feed.model.Post
import java.io.Serializable

interface SinglePostPresenter {
    fun onReceivePost(serializable: Serializable)
    fun onCreate()
    fun onDestroy()
    fun onLikeClicked()
    fun deletePost()
    fun reportPost()
}