package br.com.goin.component_model_club.model

import android.app.Activity
import android.graphics.drawable.Drawable
import android.widget.ImageView

import java.io.Serializable
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols

import br.com.goin.component.model.event.Event
import br.com.goin.component.model.event.api.response.ApiEvent

/**
 * Created by cezimbra
 */

class ClubRatingModel (var ratingsList: List<ClubRatingCardModel>? = null,
                       var globalRating: Float = 0.toFloat(),
                       var globalRatingCount: Int? = null,
                       var ratedByMe: Boolean = false): Serializable
