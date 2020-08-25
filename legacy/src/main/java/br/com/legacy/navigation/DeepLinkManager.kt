package br.com.legacy.navigation

import android.app.Activity
import android.content.Intent
import android.net.Uri

import androidx.annotation.StringDef
import br.com.goin.base.helpers.PreferenceHelper
import br.com.goin.component.navigation.NavigationController
import br.goin.features.login.login.preview.LoginPreviewActivity

import io.branch.referral.Branch
import org.json.JSONObject

private const val INVINTED_BY_WITH_BRANCH_IO = "INVITED_BY_WITH_BRANCH_IO"

private const val TheaterDetail = "TheaterDetail"

private const val ACTION = "action"
private const val ACTION_VALUE = "actionValue"

object DeepLinkManager {

    @StringDef(TheaterDetail)
    annotation class SelectedDestination

    fun selectedDestination(activity: Activity, intent: Intent) {
        when {
            intent.data != null -> {
                branchIODestionation(activity, intent.data!!, fallback = {
                    if (intent.extras != null && intent.extras!!.containsKey(ACTION)
                            && intent.extras!!.containsKey(ACTION_VALUE)) {
                        fromNotification(intent, activity)
                    }
                })
            }
            intent.extras != null && intent.extras!!.containsKey(ACTION)
                    && intent.extras!!.containsKey(ACTION_VALUE) -> {
                fromNotification(intent, activity)
            }
            else -> {
                when (PreferenceHelper.read("firstTime")) {
                    "true" -> {
                        LoginPreviewActivity.starter(activity, false)
                        activity.finish()
                    }
                    "" -> {
                        NavigationController.instance?.welcomeModule()?.goToWelcome(activity)
                        PreferenceHelper.write("firstTime", false)
                    }
                    null -> {
                        NavigationController.instance?.welcomeModule()?.goToWelcome(activity)
                        PreferenceHelper.write("firstTime", false)
                    }
                    else -> {
                        NavigationController.instance?.homeModule()?.goToHome(activity)
                        PreferenceHelper.write("firstTime", false)
                        activity.finish()
                    }
                }
            }
        }
    }

    private fun fromNotification(intent: Intent, activity: Activity) {
        val action = intent.extras!!.getString(ACTION)!!
        val actionValue = intent.extras!!.getString(ACTION_VALUE)!!
        goToDestination(action, actionValue, activity)
    }

    fun branchIODestionation(activity: Activity, data: Uri, fallback: () -> Unit) {
        val branch = Branch.getInstance()

        branch.initSession({ referringParams, error ->
            when {
                referringParams.has(ACTION) && referringParams.has(ACTION_VALUE) -> {
                    if (referringParams[ACTION] == "INVITE") {
                        saveInvitedBy(referringParams)
                    } else {
                        goToDestination(referringParams[ACTION] as String, referringParams[ACTION_VALUE] as String, activity)
                    }

                }
                error == null -> fallback()
                else -> fallback()
            }
        }, data, activity)
    }

    private fun goToDestination(action: String, actionValue: String, activity: Activity) {
        when (action) {
            TheaterDetail -> {
                NavigationController.instance!!.theatherPlayMovieModule().goToTheatersActivity(activity, actionValue, "")
                PreferenceHelper.write("firstTime", false)
            }
            else -> NavigationController.instance!!.homeModule().goToHome(activity)
        }
    }

    private fun saveInvitedBy(referringParams: JSONObject) {
        var invintedBy = ""
        if (referringParams.has(ACTION_VALUE)) {
            invintedBy = referringParams.getString(ACTION_VALUE)
        }

        PreferenceHelper.write(INVINTED_BY_WITH_BRANCH_IO, invintedBy)
    }
}
