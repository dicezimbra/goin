package br.com.goin.feature.feed.model.friends

object FriendsQueries {

    var SEARCH_FRIENDS = "query SearchFriendsResponse(\$query: String) {\n" +
            "   searchFriends (query: \$query) {\n" +
            "       id\n" +
            "       name\n" +
            "       avatar\n" +
            "       chatId\n" +
            "   }\n" +
            "}"


}