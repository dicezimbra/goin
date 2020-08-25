package br.goin.features.login.login

import com.google.gson.annotations.SerializedName
import java.util.ArrayList

class VerifyEmail {
    @SerializedName("verifyUserEmailExists")var verifyUserEmailExists: ArrayList<String>? = null
}
