package br.com.goin.feature.theaters.plays.movies.model

object MovieQueries {

    val GET_ALL_THEATERS_PLAY = "query getPlaysTheater(\$subcategoryId: String!, \$myLocation: [Float]) {" +
            " getPlaysTheater(subcategoryId: \$subcategoryId, myLocation:\$myLocation){" +
            "        highlighted {\n" +
            "               id\n" +
            "               image\n" +
            "               name\n" +
            "               city\n" +
            "        }\n" +
            "        inTheathers {\n" +
            "               id\n" +
            "               image\n" +
            "               name\n" +
            "               city\n" +
            "        }\n" +
            "        comingSoon {\n" +
            "               id\n" +
            "               image\n" +
            "               name\n" +
            "               city\n" +
            "        }\n" +
            "   }\n" +
            "}"


    val GET_ALL_MOVIES = "query getPlaysMovie(\$subcategoryId: String!, \$myLocation: [Float]) {" +
            " getPlaysMovie(subcategoryId: \$subcategoryId, myLocation:\$myLocation){" +
            "        highlighted {\n" +
            "               id\n" +
            "               image\n" +
            "               name\n" +
            "        }\n" +
            "        inTheathers {\n" +
            "               id\n" +
            "               image\n" +
            "               name\n" +
            "        }\n" +
            "        comingSoon {\n" +
            "               id\n" +
            "               image\n" +
            "               name\n" +
            "        }\n" +
            "   }\n" +
            "}"

    var GET_SESSIONS_EVENT_BY_ID = "query sessionsByEvent(\$eventId: String!) {" +
            "sessionsByEvent(eventId: \$eventId) {" +
            "           date\n" +
            "           week\n" +
            "       sessions\n {" +
            "           addressInfo{" +
            "               placeAddress" +
            "               latitude" +
            "               longitude" +
            "           }" +
            "           eventId" +
            "           clubName" +
            "                   details {" +
            "                   room" +
            "                   labels" +
            "           info {" +
            "                   isAvailable" +
            "                   hour" +
            "                   originURL " +
            "           } " +
            "         } " +
            "     }" +
            "   }" +
            "}"
}