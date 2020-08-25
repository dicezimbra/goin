package br.com.goin.feature_theater.model

object TheaterQueries {

    val GET_THEATER = "query(\$clubId: String!) {\n" +
            "        sessionsByClub(clubId: \$clubId) {\n" +
            "            date\n" +
            "            week\n" +
            "            events {\n" +
            "                id\n" +
            "                name\n" +
            "                description\n" +
            "                image\n" +
            "                sessions {\n" +
            "                    clubId\n" +
            "                    clubName\n" +
            "                    details {\n" +
            "                        room\n" +
            "                        labels\n" +
            "                        info {\n" +
            "                            hour\n" +
            "                            originURL\n" +
            "                        }\n" +
            "                    }\n" +
            "                }\n" +
            "            }\n" +
            "        }\n" +
            "    }"

    val GET_CLUB = "query GetClub(\$clubId: ID!) {\n" +
            "  club(id: \$clubId) {\n" +
            "    id\n" +
            "    name\n" +
            "    description\n" +
            "    address\n" +
            "    latitude\n" +
            "    longitude\n" +
            "    logoImage\n" +
            "  }\n" +
            "}"
}