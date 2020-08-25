package br.com.goin.feature.change.password.model

object ChangePasswordQueries {

    var CHANGE_PASSWORD = "mutation changeUserPassword(\$email: String!, " +
            "\$oldPassword: String!, " +
            "\$newPassword: String!, " +
            "\$passwordConfirmation: String!) {" +
            "changeUserPassword(input: {email : \$email, oldPassword : \$oldPassword, newPassword : \$newPassword, passwordConfirmation : \$passwordConfirmation})}"

}