package br.com.legacy.helper

import android.content.Context
import android.content.Intent
import br.com.goin.component.model.event.Event

object ShareHelper {

    fun shareEvent(context: Context, event: Event) {
        val shareIntent = Intent()
        shareIntent.action = Intent.ACTION_SEND
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Venha curtir com a gente! " + event.club)
        shareIntent.putExtra(Intent.EXTRA_TEXT, "Veja o evento: https://www.goin.com.br/EventDetail?eventId=" + event.id)
        shareIntent.type = "text/plain"
        context.startActivity(Intent.createChooser(shareIntent, "Convide seus amigos usando:"))
    }
}