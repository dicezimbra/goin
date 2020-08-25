package br.com.goin.feature.configuration.branchIo

import android.app.Activity
import br.com.goin.component_deep_link.R
import io.branch.indexing.BranchUniversalObject
import io.branch.referral.Branch
import io.branch.referral.BranchError
import io.branch.referral.util.LinkProperties
import io.branch.referral.util.ShareSheetStyle

class MyBranchIO {

    companion object {
        fun newShareDeepLink() = MyBranchIO()
    }

    fun shareDeepLink(
            activity: Activity,
            branchParameterObject: BranchParameterObject,
            myBranchUniversalObject: MyBranchUniversalObject,
            indentifier: String,
            type: BranchActionType
    ) {

        val branchUniversalObject = BranchUniversalObject()
                .setCanonicalIdentifier(indentifier)
                .setTitle(myBranchUniversalObject.title)
                .setContentDescription(myBranchUniversalObject.contentDescription)
                .setContentImageUrl(myBranchUniversalObject.imageUrl)

        val linkProperties = LinkProperties()
        linkProperties.addControlParameter("action", type.name)
        linkProperties.addControlParameter("actionValue", branchParameterObject.actionValue)

        val shareSheet = ShareSheetStyle(activity, "", "")
                .setAsFullWidthStyle(true)
                .setSharingTitle(activity.getString(R.string.share))

        branchUniversalObject.showShareSheet(activity, linkProperties, shareSheet, object : Branch.BranchLinkShareListener {
            override fun onShareLinkDialogLaunched() {}
            override fun onShareLinkDialogDismissed() {}
            override fun onLinkShareResponse(sharedLink: String, sharedChannel: String, error: BranchError) {}
            override fun onChannelSelected(channelName: String) {}
        })
    }
}
