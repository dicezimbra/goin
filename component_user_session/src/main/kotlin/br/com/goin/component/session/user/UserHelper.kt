package br.com.goin.component.session.user

import android.app.Activity
import android.content.Context
import br.com.goin.base.helpers.PreferenceHelper
import br.com.goin.component.session.user.signature.LongVersionSignature
import com.bumptech.glide.load.ImageHeaderParser
import java.util.*

object UserHelper{

    enum class ImageType {
        MyProfile
    }

    private var signatures = HashMap<ImageType, LongVersionSignature>()

    fun getSignature(): LongVersionSignature {
        val signature = signatures.get(ImageType.MyProfile)
        if (signature != null) return signature

        val timestamp = PreferenceHelper.read(ImageType.MyProfile.name) as? Long ?: 0

        val longVersionSignature: LongVersionSignature
        if (timestamp > 0) {
            longVersionSignature = LongVersionSignature(timestamp)
        } else {
            longVersionSignature = LongVersionSignature(Calendar.getInstance().timeInMillis)
        }

        signatures.put(ImageType.MyProfile, longVersionSignature)
        return longVersionSignature
    }
}