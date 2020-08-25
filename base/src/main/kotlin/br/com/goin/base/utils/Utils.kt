package br.com.goin.base.utils

import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.StateListDrawable
import android.net.ConnectivityManager
import android.os.Bundle

import androidx.appcompat.app.AlertDialog
import android.text.InputType
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.TextPaint
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.view.animation.AnimationSet
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.core.content.ContextCompat
import br.com.goin.base.BaseApplication
import br.com.goin.base.R
import java.text.DecimalFormat
import java.text.Normalizer
import java.text.NumberFormat
import java.util.InputMismatchException
import java.util.Locale
import java.util.regex.Pattern

object Utils {
    val TAG_HASH = "d9d1d"
    val dateFormat: String
        get() {
            val defaultDateFormat = "MM/dd/yyyy"
            val locale = Locale.getDefault()
            val brazil = Locale("pt", "BR")
            return if (locale == brazil)
                "dd/MM/yyyy"
            else
                defaultDateFormat
        }


    // Esconde o teclado
    fun hideKeyboard(view: View?, activity: Activity) {
        if (view != null) {
            val `in` = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            `in`.hideSoftInputFromWindow(view.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
        }
    }


    fun hideKeyboardOnTouch(view: View, activity: Activity) {
        // Set up touch listener for non-text box views to hide keyboard.
        if (!view.isClickable) {
            view.setOnTouchListener { _, _ ->
                hideKeyboard(view, activity)
                false
            }
        }
        //If a layout container, iterate over children and seed recursion.
        if (view is ViewGroup) {
            for (i in 0 until view.childCount) {
                val innerView = view.getChildAt(i)
                hideKeyboardOnTouch(innerView, activity)
            }
        }
    }

    fun showKeyboard(activity: Activity, editText: EditText) {
        val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT)
    }

    // Diferentes pinturas da view para diferentes estados

    /**
     * Não usar em Buttons, usar em TextViews, por exemplo.
     */
    fun makeSelector(color: Int): StateListDrawable {
        val res = StateListDrawable()
        val pressedColor = ColorDrawable(color)
        pressedColor.alpha = 50
        res.addState(intArrayOf(android.R.attr.state_pressed), pressedColor)
        res.addState(intArrayOf(), ColorDrawable(color))
        return res
    }


    // Normaliza as strings de pesquisa para minuscula, sem acentos ou diacriticos
    fun getSearchString(str: String): String {
        return Normalizer.normalize(str, Normalizer.Form.NFD).replace("[^\\p{ASCII}]".toRegex(), "").toLowerCase()
    }

    fun isAppInstalled(packageName: String, appContext: Context): Boolean {
        val pm = appContext.packageManager
        try {
            pm.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES)
            return true
        } catch (e: PackageManager.NameNotFoundException) {
        }

        return false
    }

    // Animação para text button
    fun setClickEffect(): AnimationSet {
        val `in` = AlphaAnimation(0.6f, 1.0f)
        `in`.duration = 100

        val out = AlphaAnimation(1.0f, 0.6f)
        out.duration = 100

        val `as` = AnimationSet(true)
        `as`.addAnimation(out)
        `in`.startOffset = 100
        `as`.addAnimation(`in`)
        return `as`
    }

    @JvmOverloads
    fun sendBroadcast(context: Context, code: String, bundle: Bundle? = null) {
        val intent = Intent(code)
        if (bundle != null) intent.putExtras(bundle)
        context.sendBroadcast(intent)
    }

    fun getPostWithTags(description: String?): SpannableStringBuilder {

        val builder = SpannableStringBuilder()
        if (description == null) return builder
        var currentIndex = 0
        val tagIndicator = "@{" + TAG_HASH
        val bold = StyleSpan(Typeface.BOLD)


        while (currentIndex <= description.length - 1) {
            val tagStartIndex = description.indexOf(tagIndicator, currentIndex)
            val tagEndIndex = description.indexOf("}", tagStartIndex)
            if (tagStartIndex == -1 || tagEndIndex == -1) {
                return builder.append(description.substring(currentIndex))
            }
            val normalText = description.substring(currentIndex, tagStartIndex)
            val tagInformation = description.substring(tagStartIndex + tagIndicator.length + 1, tagEndIndex).split("/".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            val tag = tagInformation[0]
            if (tagInformation.size == 1) {
                return builder.append(description.substring(currentIndex))
            }
            val auxSpan = SpannableString(normalText + tag)
            currentIndex = tagEndIndex + 1
            auxSpan.setSpan(bold, normalText.length, auxSpan.length, 0)
            val clickableSpan = object : ClickableSpan() {
                override fun onClick(widget: View) {
                    //TODO DESCOMENTAR QUANDO TIVER O MODULO DO USER PROFILE
                    //Coordinator.goToUserProfile(activity, id, SessionManager.getInstance().getCurrentUser(activity).getId())
                }

                override fun updateDrawState(ds: TextPaint) {
                    ds.isUnderlineText = false
                    ds.color = ds.linkColor
                }
            }
            auxSpan.setSpan(clickableSpan, normalText.length, auxSpan.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

            builder.append(auxSpan)
        }
        return builder
    }

    fun getPriceText(price: Double): String {
        val formatter = NumberFormat.getCurrencyInstance(Locale("pt", "BR"))
        return formatter.format(price)
    }

    fun isValidCPF(cpf: String): Boolean {
        var CPF = cpf

        CPF = removeCaracteresEspeciais(CPF)

        // considera-se erro CPF's formados por uma sequencia de numeros iguais
        if (CPF == "00000000000" || CPF == "11111111111" || CPF == "22222222222" ||
                CPF == "33333333333" || CPF == "44444444444" || CPF == "55555555555"
                || CPF == "66666666666" || CPF == "77777777777" || CPF == "88888888888"
                || CPF == "99999999999" || CPF.length != 11)

            return false

        val dig10: Char
        val dig11: Char
        var sm: Int
        var i: Int
        var r: Int
        var num: Int
        var peso: Int

        // "try" - protege o codigo para eventuais erros de conversao de tipo (int)
        try {
            // Calculo do 1o. Digito Verificador
            sm = 0
            peso = 10
            i = 0
            while (i < 9) {
                // converte o i-esimo caractere do CPF em um numero:
                // por exemplo, transforma o caractere '0' no inteiro 0
                // (48 eh a posicao de '0' na tabela ASCII)
                num = CPF[i].toInt() - 48
                sm = sm + num * peso
                peso = peso - 1
                i++
            }

            r = 11 - sm % 11
            if (r == 10 || r == 11)
                dig10 = '0'
            else
                dig10 = (r + 48).toChar() // converte no respectivo caractere numerico

            // Calculo do 2o. Digito Verificador
            sm = 0
            peso = 11
            i = 0
            while (i < 10) {
                num = CPF[i].toInt() - 48
                sm = sm + num * peso
                peso = peso - 1
                i++
            }

            r = 11 - sm % 11
            if (r == 10 || r == 11)
                dig11 = '0'
            else
                dig11 = (r + 48).toChar()

            // Verifica se os digitos calculados conferem com os digitos informados.
            return dig10 == CPF[9] && dig11 == CPF[10]
        } catch (erro: InputMismatchException) {
            return false
        }

    }

    private fun removeCaracteresEspeciais(document: String): String {
        var doc = document
        if (doc.contains(".")) {
            doc = doc.replace(".", "")
        }
        if (doc.contains("-")) {
            doc = doc.replace("-", "")
        }
        if (doc.contains("/")) {
            doc = doc.replace("/", "")
        }
        return doc
    }

    fun initCapsWord(word: String): String {

        var more = ""
        var sequence: String?
        var maiuscula: String
        var position = "" + word[0]
        val pos = position.toUpperCase()

        var i = 1
        while (i < word.length) {
            more = more + word[i]
            if (word[i] == ' ' && i + 1 < word.length) {
                val maiusculaProxima = "" + word[i + 1]
                maiuscula = maiusculaProxima.toUpperCase()
                more = more + maiuscula
                i = i + 1
            }
            i++
        }
        sequence = pos + more
        return sequence
    }

    fun showToastShort(context: Context, messagge: String) {

        Toast.makeText(context, messagge, Toast.LENGTH_SHORT).show()
    }

    fun showAlertWithCallBack(context: Context, title: String, message: String, positiveButton: String,
                              positiveListener: (Any, Any) -> Unit) {

        val builder = AlertDialog.Builder(context)

        builder.setMessage(message)
                .setTitle(title)

        builder.setPositiveButton(positiveButton, positiveListener)
        val dialog = builder.create()
        dialog.show()
    }

    /**
     * 1- a number digit must occur at least once
     * 2- a upper case letter must occur at least once
     * 3- at least eight characters
     */
    fun isValidPassword(password: String): Boolean {

        return Pattern.matches("^(?=.*[0-9])(?=.*[A-Z]).{8,}$", password)
    }

    /**
     *
     * if > 1 return eg.: 100 km
     * else eg.: 0,100 km
     *
     */

    fun formatKm(dist: Float?): String? {
        var distance = dist

        distance = distance?.div(1000)
        var km: String? = null
        val df = DecimalFormat("#0.000")

        if (distance != null) {
            if (distance > 1)
                km = Math.round(distance).toString()
            else
                km = df.format(distance)
        }

        return km
    }
}
