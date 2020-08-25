package br.com.goin.base.helpers

import android.content.Context
import android.content.Intent
import android.net.Uri

object GDriveHelper{

    fun openFile(fileUrl: String, context: Context){
        val url = "https://drive.google.com/viewerng/viewer?embedded=true&url=${fileUrl}"
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        context.startActivity(browserIntent)
    }

}