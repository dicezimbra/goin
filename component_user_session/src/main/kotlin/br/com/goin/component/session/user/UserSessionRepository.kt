package br.com.goin.component.session.user

import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import br.com.goin.base.BaseApplication
import br.com.goin.base.helpers.PreferenceHelper
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import io.reactivex.Observable
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream

private const val USER_PREFERENCE_KEY = "USER"
private const val TOKEN_PREFERENCE_KEY = "TOKEN"

class UserSessionRepository {

    fun saveToken(token: String, refreshToken: String?, identityProvider: String) {
        PreferenceHelper.write(TOKEN_PREFERENCE_KEY, Token(token, refreshToken?:"", identityProvider))
    }

    fun fetchToken(): Token? {
        val tokenJson = PreferenceHelper.read(TOKEN_PREFERENCE_KEY) as? String
        return Gson().fromJson<Token>(tokenJson, Token::class.java)
    }

    fun clearToken() {
        PreferenceHelper.clear(TOKEN_PREFERENCE_KEY)
    }

    fun saveUser(user: UserModel) {
        val gson = Gson()
        val json = gson.toJson(user)
        PreferenceHelper.write(USER_PREFERENCE_KEY, json)
    }

    fun fetchUser(): UserModel? {
        val json = PreferenceHelper.read(USER_PREFERENCE_KEY) as? String
        val gson = GsonBuilder().create()
        return gson.fromJson(json, UserModel::class.java)
    }


}
