package br.com.goin.feature.feed.post.feelings

import android.view.View
import br.com.goin.feature.feed.R.id.*
import br.com.goin.feature.feed.model.feelings.Feelings


class FeelingsPresenterImpl(val view: FeelingsView) : FeelingsPresenter {

    override fun onCreate() {
        view.settingUpFeelings()
        view.configureOnClickListeners()
        view.configureToolbar()
    }

    override fun onEmojiClicked(it: View) {
        when (it.id) {
            emoji_text_glad -> {
                view.sendIntent(Feelings.FeelingTypes.glad.name)
            }
            emoji_text_excited -> {
                view.sendIntent(Feelings.FeelingTypes.excited.name)
            }
            emoji_text_happy -> {
                view.sendIntent(Feelings.FeelingTypes.happy.name)
            }
            emoji_text_cool -> {
                view.sendIntent(Feelings.FeelingTypes.cool.name)
            }
            emoji_text_anxious -> {
                view.sendIntent(Feelings.FeelingTypes.anxious.name)
            }
            emoji_text_joyful -> {
                view.sendIntent(Feelings.FeelingTypes.joyful.name)
            }
            emoji_text_seductive -> {
                view.sendIntent(Feelings.FeelingTypes.seductive.name)
            }
            emoji_text_angry -> {
                view.sendIntent(Feelings.FeelingTypes.angry.name)
            }
            emoji_text_sick -> {
                view.sendIntent(Feelings.FeelingTypes.sick.name)
            }
        }
    }

}
