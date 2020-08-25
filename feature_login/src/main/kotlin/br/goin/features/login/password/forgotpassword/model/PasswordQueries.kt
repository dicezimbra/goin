package br.goin.features.login.password.forgotpassword.model

object PasswordQueries {

    var FORGOT_PASSWORD = "mutation forgotPassword(\$email: String!) {forgotPassword(input: {email : \$email})}"
    
    var RESET_PASSWORD = "mutation confirmPassword(\$email: String!, " +
            "\$verificationCode: String!, " +
            "\$newPassword: String!, " +
            "\$passwordConfirmation: String!) {" +
            "confirmPassword(input: {email : \$email, verificationCode : \$verificationCode, newPassword : \$newPassword, passwordConfirmation : \$passwordConfirmation})}"

    var CHANGE_PASSWORD = "mutation changeUserPassword(\$email: String!, " +
            "\$oldPassword: String!, " +
            "\$newPassword: String!, " +
            "\$passwordConfirmation: String!) {" +
            "changeUserPassword(input: {email : \$email, oldPassword : \$oldPassword, newPassword : \$newPassword, passwordConfirmation : \$passwordConfirmation})}"


}