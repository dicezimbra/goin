package br.com.goin.feature.feed.post.feelings

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import br.com.goin.component.analytics.analytics.Analytics
import br.com.goin.feature.feed.R
import br.com.goin.feature.feed.model.feelings.Feelings
import kotlinx.android.synthetic.main.activity_feeling.*

private const val PICK_FEELING = 222
private const val FEELING = "FEELING"

class FeelingsActivity : AppCompatActivity(), FeelingsView {

    private val presenter: FeelingsPresenter = FeelingsPresenterImpl(this)

    companion object {
        fun starter(activity: Activity) {
            activity.startActivityForResult(Intent(activity, FeelingsActivity::class.java), PICK_FEELING)
        }

        fun starter(context: Context) {
            val intent = Intent(context, FeelingsActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feeling)

        presenter.onCreate()

    }

    override fun onResume() {
        super.onResume()
        Analytics.instance.screenView(this, getString(R.string.post_feelings_screen_name))
    }
    override fun configureToolbar() {
        toolbar_feelings.setOnBackButtonClicked { onBackPressed() }
        toolbar_feelings.title = getString(R.string.feeling)

    }

    @SuppressLint("SetTextI18n")
    override fun settingUpFeelings() {
        emoji_text_glad.text = "\uD83D\uDE42"
        emoji_text_excited.text = "\uD83E\uDD17"
        emoji_text_happy.text = "\uD83D\uDE01"
        emoji_text_cool.text = "\uD83D\uDE0F"
        emoji_text_anxious.text = "\uD83D\uDE2B"
        emoji_text_joyful.text = "\uD83D\uDE1C"
        emoji_text_seductive.text = "\uD83D\uDE09"
        emoji_text_angry.text = "\uD83D\uDE21"
        emoji_text_sick.text = "\uD83E\uDD22"
    }

    override fun configureOnClickListeners() {
        toolbar_feelings.setNavigationOnClickListener { onBackPressed() }
        feeling_contente.setOnClickListener { presenter.onEmojiClicked(emoji_text_glad) }
        feeling_empolgado.setOnClickListener { presenter.onEmojiClicked(emoji_text_excited) }
        feeling_feliz.setOnClickListener { presenter.onEmojiClicked(emoji_text_happy) }
        feeling_descolado.setOnClickListener { presenter.onEmojiClicked(emoji_text_cool) }
        feeling_ansioso.setOnClickListener { presenter.onEmojiClicked(emoji_text_anxious) }
        feeling_alegre.setOnClickListener { presenter.onEmojiClicked(emoji_text_joyful) }
        feeling_sedutor.setOnClickListener { presenter.onEmojiClicked(emoji_text_seductive) }
        feeling_bravo.setOnClickListener { presenter.onEmojiClicked(emoji_text_angry) }
        feeling_enjoado.setOnClickListener { presenter.onEmojiClicked(emoji_text_sick) }
    }

    override fun sendIntent(feelingName: String) {
        var feeling: String? = null

        when (feelingName) {
            Feelings.FeelingTypes.glad.name -> feeling = resources.getString(R.string.emoji_glad)
            Feelings.FeelingTypes.excited.name -> feeling = resources.getString(R.string.emoji_excited)
            Feelings.FeelingTypes.happy.name -> feeling = resources.getString(R.string.emoji_happy)
            Feelings.FeelingTypes.cool.name -> feeling = resources.getString(R.string.emoji_cool)
            Feelings.FeelingTypes.anxious.name -> feeling = resources.getString(R.string.emoji_anxious)
            Feelings.FeelingTypes.joyful.name -> feeling = resources.getString(R.string.emoji_joyful)
            Feelings.FeelingTypes.seductive.name -> feeling = resources.getString(R.string.emoji_seductive)
            Feelings.FeelingTypes.angry.name -> feeling = resources.getString(R.string.emoji_angry)
            Feelings.FeelingTypes.sick.name -> feeling = resources.getString(R.string.emoji_sick)
        }
        val data = Intent()
        data.putExtra(FEELING, feeling)
        setResult(Activity.RESULT_OK, data)
        finish()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        setResult(Activity.RESULT_CANCELED)
        finish()
    }

}