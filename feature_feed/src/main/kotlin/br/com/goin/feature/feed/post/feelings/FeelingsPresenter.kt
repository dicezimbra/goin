package br.com.goin.feature.feed.post.feelings

import android.view.View

interface FeelingsPresenter {
    fun onCreate()
    fun onEmojiClicked(it: View)
}
