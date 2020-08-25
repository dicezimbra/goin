package br.com.goin.feature.feed.post

import android.content.Context
import android.os.Bundle

import androidx.appcompat.app.AppCompatActivity
import br.com.goin.feature.feed.R
import br.com.goin.feature.feed.model.Post

interface SinglePostView {
    fun configurePost(post: Post)
    fun configureSharedPost(post: Post)
    fun configureOnClick(post: Post, currentUserId: String)
}