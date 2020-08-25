package br.com.legacy.utils


import br.com.legacy.models.UserSuggestion
import com.linkedin.android.spyglass.mentions.MentionSpan
import com.linkedin.android.spyglass.ui.MentionsEditText

/**
 * Created by AppSimples on 19/04/17.
 */

const val TAG_HASH = "d9d1d"

object DeleteUtils {

    fun getPostDescriptionWithTags(mentionsEditText: MentionsEditText?): String {
        val postDescription = StringBuilder()
        var currentIndex = 0
        if (mentionsEditText != null) {
            val mentions = mentionsEditText.text.getSpans(0, mentionsEditText.text.length, MentionSpan::class.java)
            for (span in mentions) {
                val spanEnd = mentionsEditText.mentionsText.getSpanEnd(span)
                val spanStart = mentionsEditText.mentionsText.getSpanStart(span)
                postDescription.append(mentionsEditText.text.subSequence(currentIndex, spanStart).toString())
                val tagIndicator = getTagIndicator(span.mention as UserSuggestion)
                postDescription.append(tagIndicator)
                currentIndex = spanEnd

            }
        }

        if (mentionsEditText!!.text.length > currentIndex) {
            postDescription.append(mentionsEditText.text.toString().substring(currentIndex))
        }
        return postDescription.toString()
    }

    private fun getTagIndicator(userSuggestion: UserSuggestion): String {
        return "@{" + TAG_HASH + ":" + userSuggestion.name + "/" + userSuggestion.id + "}"
    }
}
