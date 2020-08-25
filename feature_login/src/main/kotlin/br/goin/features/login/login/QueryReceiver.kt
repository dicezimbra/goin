package br.goin.features.login.login

import br.com.goin.component.session.user.UserModel
import com.google.gson.annotations.SerializedName

class QueryReceiver {
    @SerializedName("user")var user: UserModel? = null
    @SerializedName("myUser")var myUser: UserModel? = null
}
