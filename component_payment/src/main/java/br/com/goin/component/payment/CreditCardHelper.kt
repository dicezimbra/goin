package br.com.goin.component.payment


import com.github.rtoshiro.util.format.MaskFormatter
import com.github.rtoshiro.util.format.SimpleMaskFormatter
import java.io.Serializable
import java.util.regex.Pattern
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


class CreditCardHelper {

    companion object {

        private val PATTERN_GENERIC = Pattern.compile("[0-9]{3}\\.?[0-9]{3}\\.?[0-9]{3}\\-?[0-9]{2}")
        private val PATTERN_NUMBERS = Pattern.compile("(?=^((?!((([0]{11})|([1]{11})|([2]{11})|([3]{11})|([4]{11})|([5]{11})|([6]{11})|([7]{11})|([8]{11})|([9]{11})))).)*$)([0-9]{11})")

        private val simpleDateFormat = SimpleDateFormat("MM/yy")


        fun findCardType(number: String): CardType {
            val cleaned = cleanNumber(number)
            for (type in CardType.values()) {
                if (type.fullRegex != null) {
                    val pattern = Pattern.compile(type.fullRegex)
                    val matcher = pattern.matcher(cleaned)

                    if (matcher.matches()) {
                        return type
                    }
                }
            }

            return CardType.INVALID
        }

        fun isValidNumber(number: String): Boolean {
            val cleaned = cleanNumber(number)
            val cardType = findCardType(cleaned)
            if (cardType == null || cardType.fullRegex == null) return false

            val pattern = Pattern.compile(cardType.fullRegex)
            val matcher = pattern.matcher(cleaned)

            return matcher.matches() && validateCardNumber(cleaned)
        }

        private fun validateCardNumber(ccNumber: String): Boolean {
            var sum = 0
            var alternate = false
            for (i in ccNumber.length - 1 downTo 0) {
                var n = Integer.parseInt(ccNumber.substring(i, i + 1))
                if (alternate) {
                    n *= 2
                    if (n > 9) {
                        n = n % 10 + 1
                    }
                }
                sum += n
                alternate = !alternate
            }
            return sum % 10 == 0
        }

        fun formatForViewing(enteredNumber: String, type: CardType?): String {
            val cleaned = cleanNumber(enteredNumber)
            val len = cleaned.length


            val gaps = ArrayList<String>()

            val segmentLengths = intArrayOf(0, 0, 0, 0, 0)

            when (type) {
                CardType.VISA, CardType.MASTERCARD, CardType.JCB, CardType.DISCOVER // { 4-4-4-4}
                -> {
                    gaps.add(" ")
                    segmentLengths[0] = 4
                    gaps.add(" ")
                    segmentLengths[1] = 4
                    gaps.add(" ")
                    segmentLengths[2] = 4
                    gaps.add(" ")
                }
                CardType.AMEX // {4-6-5}
                -> {
                    gaps.add(" ")
                    segmentLengths[0] = 6
                    gaps.add(" ")
                    segmentLengths[1] = 5
                    gaps.add("")
                    segmentLengths[2] = 0
                    gaps.add(" ")
                }
                CardType.DINERS// {4-6-4}
                -> {
                    gaps.add(" ")
                    segmentLengths[0] = 6
                    gaps.add(" ")
                    segmentLengths[1] = 4
                    gaps.add("")
                    segmentLengths[2] = 0
                    gaps.add(" ")
                }
                CardType.VERVE // { 4-4-4-4-3}
                -> {
                    gaps.add(" ")
                    segmentLengths[0] = 4
                    gaps.add(" ")
                    segmentLengths[1] = 4
                    gaps.add(" ")
                    segmentLengths[2] = 4
                    gaps.add(" ")
                    segmentLengths[3] = 4
                }
                else -> return enteredNumber
            }

            var end = 4
            var start: Int
            val segment1 = cleaned.substring(0, end)
            start = end
            end = if (segmentLengths[0] + end > len) len else segmentLengths[0] + end
            val segment2 = cleaned.substring(start, end)
            start = end
            end = if (segmentLengths[1] + end > len) len else segmentLengths[1] + end
            val segment3 = cleaned.substring(start, end)
            start = end
            end = if (segmentLengths[2] + end > len) len else segmentLengths[2] + end
            val segment4 = cleaned.substring(start, end)

            start = end
            end = if (segmentLengths[3] + end > len) len else segmentLengths[3] + end
            val segment5 = cleaned.substring(start, end)

            val ret = String.format("%s%s%s%s%s%s%s%s%s", segment1, gaps.get(0),
                    segment2, gaps.get(1), segment3, gaps.get(2), segment4,
                    gaps.get(3), segment5)

            return ret.trim { it <= ' ' }
        }

        fun formatForViewingXXXX(enteredNumber: String, type: CardType): String {
            val cleaned = cleanNumber(enteredNumber)
            val len = cleaned.length


            val gaps = ArrayList<String>()

            val segmentLengths = intArrayOf(0, 0, 0, 0, 0)

            when (type) {
                CardType.VISA, CardType.MASTERCARD, CardType.JCB, CardType.DISCOVER // { 4-4-4-4}
                -> {
                    gaps.add(" ")
                    segmentLengths[0] = 4
                    gaps.add(" ")
                    segmentLengths[1] = 4
                    gaps.add(" ")
                    segmentLengths[2] = 4
                    gaps.add(" ")
                }
                CardType.AMEX // {4-6-5}
                -> {
                    gaps.add(" ")
                    segmentLengths[0] = 6
                    gaps.add(" ")
                    segmentLengths[1] = 5
                    gaps.add("")
                    segmentLengths[2] = 0
                    gaps.add(" ")
                }
                CardType.DINERS// {4-6-4}
                -> {
                    gaps.add(" ")
                    segmentLengths[0] = 6
                    gaps.add(" ")
                    segmentLengths[1] = 4
                    gaps.add("")
                    segmentLengths[2] = 0
                    gaps.add(" ")
                }
                CardType.VERVE // { 4-4-4-4-3}
                -> {
                    gaps.add(" ")
                    segmentLengths[0] = 4
                    gaps.add(" ")
                    segmentLengths[1] = 4
                    gaps.add(" ")
                    segmentLengths[2] = 4
                    gaps.add(" ")
                    segmentLengths[3] = 4
                }
                else -> return enteredNumber
            }

            var end = 4
            var start: Int
            var segment1 = cleaned.substring(0, end)
            start = end
            end = if (segmentLengths[0] + end > len) len else segmentLengths[0] + end
            var segment2 = cleaned.substring(start, end)
            start = end
            end = if (segmentLengths[1] + end > len) len else segmentLengths[1] + end
            var segment3 = cleaned.substring(start, end)
            start = end
            end = if (segmentLengths[2] + end > len) len else segmentLengths[2] + end
            var segment4 = cleaned.substring(start, end)

            start = end
            end = if (segmentLengths[3] + end > len) len else segmentLengths[3] + end
            var segment5 = cleaned.substring(start, end)

            if (!segment5.isNullOrEmpty()) {
                segment4 = "*".repeat(segment4.length)
            }

            if (!segment4.isNullOrEmpty()) {
                segment3 = "*".repeat(segment3.length)
            }

            if (!segment3.isNullOrEmpty()) {
                segment2 = "*".repeat(segment2.length)
            }

            if (!segment2.isNullOrEmpty()) {
                segment1 = "*".repeat(segment1.length)
            }


            val ret = String.format("%s%s%s%s%s%s%s%s%s", segment1, gaps.get(0),
                    segment2, gaps.get(1), segment3, gaps.get(2), segment4,
                    gaps.get(3), segment5)

            return ret.trim { it <= ' ' }
        }

        private fun getCorrectMask(enteredNumber: String, type: CardType): String {
            val cleaned = cleanNumber(enteredNumber)
            val len = cleaned.length


            val gaps = ArrayList<String>()

            val segmentLengths = intArrayOf(0, 0, 0, 0, 0)

            when (type) {
                CardType.VISA, CardType.MASTERCARD, CardType.JCB, CardType.DISCOVER // { 4-4-4-4}
                -> {
                    gaps.add(" ")
                    segmentLengths[0] = 4
                    gaps.add(" ")
                    segmentLengths[1] = 4
                    gaps.add(" ")
                    segmentLengths[2] = 4
                    gaps.add(" ")
                }
                CardType.AMEX // {4-6-5}
                -> {
                    gaps.add(" ")
                    segmentLengths[0] = 6
                    gaps.add(" ")
                    segmentLengths[1] = 5
                    gaps.add("")
                    segmentLengths[2] = 0
                    gaps.add(" ")
                }
                CardType.DINERS// {4-6-4}
                -> {
                    gaps.add(" ")
                    segmentLengths[0] = 6
                    gaps.add(" ")
                    segmentLengths[1] = 4
                    gaps.add("")
                    segmentLengths[2] = 0
                    gaps.add(" ")
                }
                CardType.VERVE // { 4-4-4-4-3}
                -> {
                    gaps.add(" ")
                    segmentLengths[0] = 4
                    gaps.add(" ")
                    segmentLengths[1] = 4
                    gaps.add(" ")
                    segmentLengths[2] = 4
                    gaps.add(" ")
                    segmentLengths[3] = 4
                }
                else -> return "NNNN NNNN NNNN NNNN"
            }

            var end = 4
            var start: Int
            var segment1 = cleaned.substring(0, end)
            start = end
            end = if (segmentLengths[0] + end > len) len else segmentLengths[0] + end
            var segment2 = cleaned.substring(start, end)
            start = end
            end = if (segmentLengths[1] + end > len) len else segmentLengths[1] + end
            var segment3 = cleaned.substring(start, end)
            start = end
            end = if (segmentLengths[2] + end > len) len else segmentLengths[2] + end
            var segment4 = cleaned.substring(start, end)

            start = end
            end = if (segmentLengths[3] + end > len) len else segmentLengths[3] + end
            var segment5 = cleaned.substring(start, end)

            segment5 = "N".repeat(segment5.length)
            segment4 = "N".repeat(segment4.length)
            segment3 = "N".repeat(segment3.length)
            segment2 = "N".repeat(segment2.length)
            segment1 = "N".repeat(segment1.length)

            val ret = String.format("%s%s%s%s%s%s%s%s%s", segment1, gaps.get(0),
                    segment2, gaps.get(1), segment3, gaps.get(2), segment4,
                    gaps.get(3), segment5)

            return ret.trim { it <= ' ' }
        }

        fun securityCodeValid(type: CardType?): Int {
            if (type == null) return 3
            when (type) {
                CardType.AMEX -> return 4
                CardType.DISCOVER, CardType.INVALID, CardType.MASTERCARD, CardType.VISA, CardType.DINERS, CardType.JCB, CardType.VERVE -> return 3
                else -> return 3
            }
        }

        fun cleanNumber(number: String): String {
            return number.replace("\\s".toRegex(), "")
        }

        fun cleanNumberLong(number: String?): Long {
            number?.let {
                return it.replace("\\s".toRegex(), "").toLong()
            }
        }

        fun getMask(number: String, cardType: CardType): MaskFormatter? {
            return SimpleMaskFormatter(getCorrectMask(number, cardType))
        }

        fun formatExpirationDate(text: String): String {
            var text = text
            try {
                when (text.length) {
                    1 -> {
                        val digit = Integer.parseInt(text)

                        return if (digit < 2) {
                            text
                        } else {
                            "0$text/"
                        }
                    }
                    2 -> {
                        val month = Integer.parseInt(text)
                        return if (month > 12 || month < 1) {
                            // Invalid digit
                            //text.substring(0, 1)
                            "Inválido"
                        } else {
                            "$text/"
                        }
                    }
                    3 -> {
                        if (text.substring(2, 3).equals("/", ignoreCase = true)) {
                            return text
                        } else {
                            text = text.substring(0, 2) + "/" + text.substring(2, 3)
                        }
                        val now = getCurrentExpDate()
                        val currentYearStr = "${now.get(Calendar.YEAR)}"

                        val yearDigit = Integer.parseInt(text.substring(3, 4))
                        val currentYearDigit = Integer.parseInt(currentYearStr.substring(2, 3))
                        return if (yearDigit < currentYearDigit) {
                            // Less than current year invalid
                            //text.substring(0, 3)
                            "Inválido"
                        } else {
                            text
                        }
                    }
                    4 -> {
                        val now = getCurrentExpDate()
                        val currentYearStr = "${now.get(Calendar.YEAR)}"
                        val yearDigit = Integer.parseInt(text.substring(3, 4))
                        val currentYearDigit = Integer.parseInt(currentYearStr.substring(2, 3))
                        return if (yearDigit < currentYearDigit) {
                            text.substring(0, 3)
                        } else {
                            text
                        }
                    }
                    5 -> {
                        // always make the year in the current century
                        val now2 = getCurrentExpDate()
                        val currentYearStr2 = "${now2.get(Calendar.YEAR)}"
                        val yearStr = text.substring(0, 3) + currentYearStr2.substring(0, 2) + text.substring(3, 5)
                        val expiry = simpleDateFormat.parse(yearStr)
                        return if (expiry.before(now2.time)) {
                            // Invalid exp date
                            //text.substring(0, 4)
                            "Inválido"
                        } else {
                            text
                        }
                    }
                    else -> return if (text.length > 5) {
                        text.substring(0, 5)
                    } else {
                        text
                    }
                }

            } catch (e: ParseException) {
                // TODO Auto-generated catch block
                e.printStackTrace()
            }

            // If an exception is thrown we clear out the text
            return ""
        }

        fun formatDate(text: String): Boolean {
            var text = text
            try {
                when (text.length) {
                    1 -> {
                        val digit = Integer.parseInt(text)

                        return digit >= 2
                    }
                    2 -> {
                        val month = Integer.parseInt(text)
                        return month > 12 || month < 1
                    }
                    3 -> {
                        if (text.substring(2, 3).equals("/", ignoreCase = true)) {
                            return false
                        } else {
                            text = text.substring(0, 2) + "/" + text.substring(2, 3)
                        }
                        val now = getCurrentExpDate()
                        val currentYearStr = "${now.get(Calendar.YEAR)}"

                        val yearDigit = Integer.parseInt(text.substring(3, 4))
                        val currentYearDigit = Integer.parseInt(currentYearStr.substring(2, 3))
                        return yearDigit >= currentYearDigit
                    }
                    4 -> {
                        val now = getCurrentExpDate()
                        val currentYearStr = "${now.get(Calendar.YEAR)}"
                        val yearDigit = Integer.parseInt(text.substring(3, 4))
                        val currentYearDigit = Integer.parseInt(currentYearStr.substring(2, 3))
                        return (yearDigit < currentYearDigit)
                    }
                    5 -> {
                        // always make the year in the current century
                        val now2 = getCurrentExpDate()
                        val currentYearStr2 = "${now2.get(Calendar.YEAR)}"
                        val yearStr = text.substring(0, 3) + currentYearStr2.substring(0, 2) + text.substring(3, 5)
                        val expiry = simpleDateFormat.parse(yearStr)
                        return !expiry.before(now2.time)
                    }
                    else -> return (text.length > 5)
                }

            } catch (e: ParseException) {
                // TODO Auto-generated catch block
                e.printStackTrace()
            }

            // If an exception is thrown we clear out the text
            return false
        }


        fun checkformatExpirationDateisValid(text: String): Boolean {

            if (text.isNullOrEmpty()) return false
            if (text.length < 5) return false
            // always make the year in the current century
            val now2 = getCurrentExpDate()
            val currentYearStr2 = "${now2.get(Calendar.YEAR)}"
            val yearStr = text.substring(0, 3) + currentYearStr2.substring(0, 2) + text.substring(3, 5)
            val expiry = simpleDateFormat.parse(yearStr)
            return !expiry.before(now2.time)
        }

        private fun getCurrentExpDate(): Calendar {
            val now = Calendar.getInstance()
            now.set(now.get(Calendar.YEAR), now.get(Calendar.MONTH), 0, 0, 0)
            return now
        }

        fun changeHexOrder(s: String): String {
            val result = StringBuilder()
            var i = 0
            while (i <= s.length - 2) {
                result.append(StringBuilder(s.substring(i, i + 2)).reverse())
                i += 2
            }
            return result.reverse().toString()
        }

        fun isValid(cpf: String?): Boolean {
            var cpf = cpf
            if (cpf != null && PATTERN_GENERIC.matcher(cpf).matches()) {
                cpf = cpf.replace("-|\\.".toRegex(), "")
                if (cpf != null && PATTERN_NUMBERS.matcher(cpf).matches()) {
                    val numbers = IntArray(11)
                    for (i in 0..10) numbers[i] = Character.getNumericValue(cpf[i])
                    var i: Int
                    var sum = 0
                    var factor = 100
                    i = 0
                    while (i < 9) {
                        sum += numbers[i] * factor
                        factor -= 10
                        i++
                    }
                    var leftover = sum % 11
                    leftover = if (leftover == 10) 0 else leftover
                    if (leftover == numbers[9]) {
                        sum = 0
                        factor = 110
                        i = 0
                        while (i < 10) {
                            sum += numbers[i] * factor
                            factor -= 10
                            i++
                        }
                        leftover = sum % 11
                        leftover = if (leftover == 10) 0 else leftover
                        return leftover == numbers[10]
                    }
                }
            }
            return false
        }

    }


    internal object CardRegex {
        //val REGX_VISA = "^4[0-9]{3}?" // VISA 16
        val REGX_VISA = "^4[0-9]{6,}"
        val REGX_MC = "^5[1-5][0-9]{5,}|222[1-9][0-9]{3,}|22[3-9][0-9]{4,}|2[3-6][0-9]{5,}|27[01][0-9]{4,}|2720[0-9]{3,}$"
        val REGX_AMEX = "^3[47][0-9]{5,}$"
        val REGX_DISCOVER = "^6(?:011|5[0-9]{2})[0-9]{12}$" // Discover 16
        val REGX_DINERS_CLUB = "^3(?:0[0-5]|[68][0-9])[0-9]{4,}" // DinersClub 14
        val REGX_JCB = "^35[0-9]{14}$" // JCB 16
        val REGX_VERVE = "^(506099|5061[0-8][0-9]|50619[0-8])[0-9]{13}$" // Interswitch Verve [Nigeria]
        val REGX_ELO = "^(40117[8-9]|431274|438935|451416|457393|45763[1-2]|506(699|7[0-6][0-9]|77[0-8])|509\\d{3}|504175|627780|636297|636368|65003[1-3]|6500(3[5-9]|4[0-9]|5[0-1])|6504(0[5-9]|[1-3][0-9])|650(4[8-9][0-9]|5[0-2][0-9]|53[0-8])|6505(4[1-9]|[5-8][0-9]|9[0-8])|6507(0[0-9]|1[0-8])|65072[0-7]|6509(0[1-9]|1[0-9]|20)|6516(5[2-9]|[6-7][0-9])|6550([0-1][0-9]|2[1-9]|[3-4][0-9]|5[0-8]))"
    }


    enum class CardType constructor(val friendlyName: String, val frontResource: Int, val fullRegex: String) : Serializable {


        VISA("VISA", R.drawable.icon_goin_visa, CardRegex.REGX_VISA),
        MASTERCARD("MasterCard", R.drawable.icon_goin_mastercard, CardRegex.REGX_MC),
        AMEX("American Express", R.drawable.icon_goin_amex, CardRegex.REGX_AMEX),
        DISCOVER("Discover", R.drawable.discover, CardRegex.REGX_DISCOVER),
        DINERS("DinersClub", R.drawable.icon_goin_diners, CardRegex.REGX_DINERS_CLUB),
        JCB("JCB", R.drawable.jcb_payment_ico, CardRegex.REGX_JCB),
        VERVE("Verve", R.drawable.payment_ic_verve, CardRegex.REGX_VERVE),
        ELO("Elo", R.drawable.icon_goin_elo, CardRegex.REGX_ELO),
        INVALID("Unknown", R.drawable.unknown_cc, "");

        override fun toString(): String {
            return friendlyName
        }
    }


}