package br.com.goin.base.helpers

import android.content.Context
import android.content.Intent

object ShareHelper {

    fun shareEvent(context: Context, eventName: String, eventId: String) {
        val shareIntent = Intent()
        shareIntent.action = Intent.ACTION_SEND
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Venha curtir com a gente! $eventName")
        shareIntent.putExtra(Intent.EXTRA_TEXT, "Veja o evento: https://www.goin.com.br/EventDetail?eventId=$eventId")
        shareIntent.type = "text/plain"
        context.startActivity(Intent.createChooser(shareIntent, "Convide seus amigos usando:"))
    }

    fun sharePlace(context: Context, name: String, clubId: String) {
        val shareIntent = Intent()
        shareIntent.action = Intent.ACTION_SEND
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Venha curtir com a gente! $name")
        shareIntent.putExtra(Intent.EXTRA_TEXT, "Veja o estabelecimento: https://www.goin.com.br/ClubDetail?clubId=" + clubId)
        shareIntent.type = "text/plain"
        context.startActivity(Intent.createChooser(shareIntent, "Convide seus amigos usando:"))
    }

    fun shareClub(context: Context, clubName: String, clubId: String) {
        val shareIntent = Intent()
        shareIntent.action = Intent.ACTION_SEND
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Venha curtir com a gente! $clubName")
        shareIntent.putExtra(Intent.EXTRA_TEXT, "Veja o club: https://www.goin.com.br/TheaterSessionDetail?clubId=$clubId")
        shareIntent.type = "text/plain"
        context.startActivity(Intent.createChooser(shareIntent, "Convide seus amigos usando:"))
    }
}