package br.com.goin.component.session.user

object UserQuery {

    var UPDATE_USER = "    var UPDATE_USER = \"mutation UpdateUser(\\\$input: UpdateUserInput!) {\" +\n" +
            "            \"   updateUser(input: \\\$input) {\" +\n" +
            "            \"       id\" +\n" +
            "            \"   }\" +\n" +
            "            \"}\"\n"
}