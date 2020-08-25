package br.com.goin.features.password.newpassword

import java.io.Serializable

interface NewPasswordPresenter {
    fun onCreate()
    fun onValidatePasswordEquals(confirm: String, newpass: String)
    fun onReceiveEmailExtras(extra: Serializable)
    fun validatePassword(email: String, pin: String, pass: String, confirmPass: String)
     fun onValidateCapitalLetter(newPass: String, pin: String)

}
