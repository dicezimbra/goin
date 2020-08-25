package br.com.goin.feature.profile.model

import br.com.goin.component.session.user.UserModel
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class ProfileResponse: Serializable {
    @SerializedName("user")
    var user: UserModel? = null
}
