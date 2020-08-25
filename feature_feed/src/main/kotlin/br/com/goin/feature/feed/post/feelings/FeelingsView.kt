package br.com.goin.feature.feed.post.feelings

interface FeelingsView {
    fun settingUpFeelings()
    fun sendIntent(feelingName: String)
    fun configureOnClickListeners()
    fun configureToolbar()
}
