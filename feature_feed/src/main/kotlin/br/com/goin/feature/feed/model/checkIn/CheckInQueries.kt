package br.com.goin.feature.feed.model.checkIn

object CheckInQueries {

    val GET_AVAILABLE_EVENTS_TO_CHECK_IN = "query GetCheckinAvailable(\$myLocation: [Float]!) {\n" +
            "   checkInAvailable(myLocation: \$myLocation) {\n" +
            "       id\n" +
            "       name\n" +
            "       type\n" +
            "   }\n" +
            "}"

}