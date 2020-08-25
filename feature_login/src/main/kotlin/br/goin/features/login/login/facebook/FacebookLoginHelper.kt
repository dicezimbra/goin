package br.goin.features.login.login.facebook

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.facebook.*
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import org.json.JSONObject

private const val PUBLIC_PROFILE = "public_profile"
private const val EMAIL = "email"
private const val FACEBOOK_FIELDS = "id, first_name, last_name, email,gender, birthday, location"

class FacebookLoginHelper {

    private val callback = CallbackManager.Factory.create()

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        callback.onActivityResult(requestCode, resultCode, data)
    }

    fun login(activity: Activity, listener: (accessToken: String) -> Unit) {
        val instance = LoginManager.getInstance()

        instance.registerCallback(callback, object : FacebookCallback<LoginResult> {
            override fun onCancel() {
            }

            override fun onError(error: FacebookException?) {
            }

            override fun onSuccess(result: LoginResult?) {
                result?.let {

                    val request = GraphRequest.newMeRequest(it.accessToken, object : GraphRequest.GraphJSONObjectCallback {
                        override fun onCompleted(jsonObject: JSONObject, response: GraphResponse) {
                            listener(it.accessToken.token)
                        }
                    })
                    val bundle = Bundle()
                    bundle.putString("fields", FACEBOOK_FIELDS)
                    request.parameters = bundle
                    request.executeAsync()
                }
            }
        })


        instance.logInWithReadPermissions(activity, arrayListOf(PUBLIC_PROFILE, EMAIL))
    }
}