package br.goin.features.login.model

object UserQueries {

    var REGISTER = "mutation NewRegisterUser(\$input: NewRegisterUserInput!){" +
            " newRegisterUser(input: \$input) {\n" +
            "    token\n" +
            "    id\n" +
            "    email\n" +
            "    name\n" +
            "    accountProviders\n" +
            "  }\n" +
            "}\n"

    var GET_MY_USER = "query GetMyUser {" +
            "   myUser {" +
            "       id" +
            "       name" +
            "       email" +
            "       cpf" +
            "       accountProviders" +
            "       profilePicture" +
            "       followingCount" +
            "       followersCount" +
            "       eventsCount" +
            "       clubsCount" +
            "       categoriesCount" +
            "       userScore" +
            "       ticketCount" +
            "       pushEndpoint" +
            "       bio" +
            "       postsCount" +
            "   }" +
            "}"

    var UPDATE_USER = "mutation UpdateUser(\$input: UpdateUserInput!) {" +
            "   updateUser(input: \$input) {" +
            "       id" +
            "   }" +
            "}"

    var VERIFY_EMAIL = "query VerifyEmail(\$email: String!) {\n" +
            "  verifyUserEmailExists(email: \$email)\n" +
            "}"

    var CREATE_USER_TEMP = "mutation createUserTemp(\$input: UserTempInput) {\n" +
            "  createUserTemp(input: \$input) {\n" +
            "       email" +
            "       password" +
            "   }" +
            "}"

    var REGISTER_CUSTOMER = "mutation RegisterUser(\$input: RegisterUserInput!) {" +
            "   registerUser(input: \$input) {" +
            "       id" +
            "       email" +
            "       profilePicture" +
            "       eventsCount" +
            "       clubsCount" +
            "       followingCount" +
            "       followersCount" +
            "   }" +
            "}"

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