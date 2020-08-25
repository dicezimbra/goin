package br.com.goin.feature.place

import android.content.Context
import android.view.View
import androidx.appcompat.app.AlertDialog
import br.com.goin.component_model_club.model.ClubModel
import br.com.goin.feature.R

object PlaceHelper {

    fun showDialogUnconfirmPresence(like_view: View, club: ClubModel, onConfirm: () -> Unit) {
        val alert = AlertDialog.Builder(like_view.context)
        alert.setMessage(R.string.unfollow_place_question)
        alert.setPositiveButton(R.string.yes) { dialog, _ ->
            club.isFollowing
            like_view.isSelected = false
            dialog.dismiss()
            onConfirm()
        }

        alert.setNegativeButton(R.string.no) { dialog, _ -> dialog.dismiss() }
        alert.show()

    }
}