package br.com.goin.component.model.event.api.response

import com.google.gson.annotations.SerializedName

data class AttendanceDetail (@SerializedName("action") var action: String = "",
                             @SerializedName("actionValue") var actionValue: String = "",
                             @SerializedName("username") var username: String = "")


