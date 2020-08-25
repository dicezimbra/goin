package br.com.goin.component_deep_link.branchIo

import android.app.Activity
import br.com.goin.base.utils.Constants
import br.com.goin.component_deep_link.R
import br.com.goin.feature.configuration.branchIo.BranchActionType
import br.com.goin.feature.configuration.branchIo.BranchParameterObject
import br.com.goin.feature.configuration.branchIo.MyBranchIO
import br.com.goin.feature.configuration.branchIo.MyBranchUniversalObject

object BranchIOHelper{

    fun onClickInvitefriend(context: Activity, userId: String, userName: String) {
        val bpo = BranchParameterObject(userId)
        val mybuo = MyBranchUniversalObject(context.getString(R.string.download_goin_title),
                "${context.resources.getString(R.string.download_goin_description_start)} ${userName} ${context.resources.getString(R.string.download_goin_description_end)}", Constants.GOIN_LOGO_SHARE)
        MyBranchIO.newShareDeepLink().shareDeepLink(context, bpo, mybuo, "", BranchActionType.INVITE)
    }
}