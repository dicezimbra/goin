package br.com.legacy.helper

import android.app.Activity
import android.net.Uri
import android.util.Log
import br.com.goin.base.helpers.PreferenceHelper
import io.branch.referral.Branch
import org.json.JSONException
import org.json.JSONObject

private const val INVINTED_BY_WITH_BRANCH_IO = "INVINTED_BY_WITH_BRANCH_IO"

class BranchIOHelper{

    fun initSession(activity: Activity, data: Uri){
        val branch = Branch.getInstance()

        branch.initSession({ referringParams, error ->
            if (error == null) {
                try {
                    fetchInvitedBy(referringParams)
                } catch (e: JSONException) {
                    Log.i("BRANCH SDK", referringParams.toString())
                }

                Log.i("BRANCH SDK", referringParams.toString())
            } else {
                Log.i("BRANCH SDK", error.message)
            }
        }, data, activity)
    }

    private fun fetchInvitedBy(referringParams: JSONObject) {
        var invintedBy = ""
        if (referringParams.has("actionValue")) {
            invintedBy = referringParams.getString("actionValue")
        }

        PreferenceHelper.write(INVINTED_BY_WITH_BRANCH_IO, invintedBy)
    }

    fun getInvitedByCode(): String? {
        return PreferenceHelper.read(INVINTED_BY_WITH_BRANCH_IO) as? String
    }
}