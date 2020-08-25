package br.com.goin.base.extensions

import android.text.TextUtils
import android.util.Patterns

fun String.toTitleCase(): String{
    val position = this[0]
    var more = ""
    val sequence: String
    var maiuscula: String
    val pos = position.toUpperCase()

    var i = 1
    while (i < this.length) {
        more += this.get(i)
        if (this.get(i) == ' ' && i + 1 < this.length) {
            val maiusculaProxima = "" + this[(i + 1)]
            maiuscula = maiusculaProxima.toUpperCase()
            more += maiuscula
            i += 1
        }
        i++
    }
    sequence = pos + more
    return sequence
}

fun String.isValidEmail(): Boolean {
    return !TextUtils.isEmpty(this) && Patterns.EMAIL_ADDRESS.matcher(this).matches()
}

fun String.toUpperCaseNoSpace(): String{
    return this.toUpperCase().replace(" ", "_")
}