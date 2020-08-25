package br.com.goin.component.model.event.api

object EventQueries {

    var CLAIM_VOUCHER = "mutation ClaimGift(\$ticketId: String, \$clubId: String, \$username: String) { claimGift(ticketId: \$ticketId, clubId: \$clubId, username: \$username) { userId } }"

    var GET_MY_EVENTS = "query GetMyEventsShort(\$userId: ID, \$count: Int) {\n" +
            "  myEvents(userId: \$userId, count: \$count) {\n" +
            "    id\n" +
            "    clubId\n" +
            "    name\n" +
            "    club\n" +
            "    startDate\n" +
            "    endDate\n" +
            "    interestedCount\n" +
            "    lowestPrice\n" +
            "    placeAddress\n" +
            "       originInfos {" +
            "           buttonColor\n" +
            "           bigIcon\n" +
            "           smallIconColored\n" +
            "           smallIconWhite\n" +
            "       }" +
            "    latitude\n" +
            "    longitude\n" +
            "    image\n" +
            "    isConfirmed\n" +
            "    confirmType\n" +
            "  }\n" +
            "}"

    var VALIDATE_COUPOM = "query ValidateCoupon(\$eventId: String!, \$coupon: String!) {\n" +
            "   validateCoupon (eventId: \$eventId, coupon: \$coupon) {\n" +
            "       valid\n" +
            "       message\n" +
            "   }\n" +
            "}"

    var CONFIRM_TOKEN_IS_VALID = "query ConfirmTokenIsValid(\$originType: String) {\n" +
            "   confirmTokenIsValid (originType: \$originType) {\n" +
            "       isValid\n" +
            "   }\n" +
            "}"

    var GET_BANNER_HOME = "query GetBanner {" +
            "   banners {\n" +
            "  id\n" +
            "  action\n" +
            "  actionValue\n" +
            "  url\n" +
            "  eventId\n" +
            "  clubId\n" +
            "  typeBanner\n" +
            "}\n" +
            "}"

    var GET_NEXT_EVENTS = "query GetAllEvents (\$count: Int, \$myLocation: [Float]){\n" +
            "  allEvents (count: \$count, myLocation:\$myLocation){\n" +
            "    id\n" +
            "    clubId\n" +
            "    name\n" +
            "    club\n" +
            "    startDate\n" +
            "    endDate\n" +
            "    interestedCount\n" +
            "    placeAddress\n" +
            "    latitude\n" +
            "    longitude\n" +
            "    lowestPrice\n" +
            "    highestPrice\n" +
            "    subcategories\n" +
            "    categoryType\n" +
            "    image\n" +
            "    isConfirmed\n" +
            "    confirmType\n" +
            "    information {\n" +
            "        duration\n" +
            "        releaseDate\n" +
            "        coverImage\n" +
            "        parentalRating\n" +
            "        trailer\n" +
            "        production\n" +
            "        directors\n" +
            "        musicComposition\n" +
            "      }" +
            "    originType\n" +
            "       originInfos {" +
            "           buttonColor\n" +
            "           bigIcon\n" +
            "           smallIconColored\n" +
            "           smallIconWhite\n" +
            "       }" +
            "       categoriesInfo {" +
            "           name\n" +
            "           id\n" +
            "       }" +
            "    originId\n" +
            "    imageWidth\n" +
            "    imageHeight\n" +
            "    imageAspect\n" +
            "  }\n" +
            "}"

    var GET_EVENT = "query GetEventById(\$id: String!) {\n" +
            "   getEvent(id: \$id) {\n" +
            "       id\n" +
            "       clubId\n" +
            "       name\n" +
            "       club\n" +
            "       startDate\n" +
            "       endDate\n" +
            "       description\n" +
            "       interestedCount\n" +
            "       checkInCount\n" +
            "       allConfirmedCount\n" +
            "       lowestPrice\n" +
            "       city\n" +
            "       highestPrice\n" +
            "       placeName\n" +
            "       placeAddress\n" +
            "       latitude\n" +
            "       longitude\n" +
            "       giftsGallery {\n" +
            "           title\n" +
            "           giftId\n" +
            "           subtitle\n" +
            "           dateText\n" +
            "           image\n" +
            "           detailTitle\n" +
            "           placeName\n" +
            "           rules\n" +
            "       }"+
            "       originInfos {" +
            "           buttonColor\n" +
            "           bigIcon\n" +
            "           smallIconColored\n" +
            "           smallIconWhite\n" +
            "       }" +
            "       image\n" +
            "       isConfirmed\n" +
            "       confirmType\n" +
            "       originType\n" +
            "       originAction\n" +
            "       originURL\n" +
            "       imageWidth\n" +
            "       imageHeight\n" +
            "       imageAspect\n" +
            "       categoryEventType\n" +
            "   }\n" +
            "}"

    var CONFIRM_PRESENCE = "mutation ConfirmPresence(\$eventId: ID!) {" +
            "   confirmInterest (eventId: \$eventId) " +
            "}"

    var UNCONFIRM_PRESENCE = "mutation UnconfirmPresence(\$eventId: ID!) {" +
            "   unconfirmInterest (eventId: \$eventId) " +
            "}"

    var CONFIRM_STEFANINI_TICKET = "mutation ConfirmStefaniniTicket (\$eventId: String) {" +
            "   confirmStefaniniTicket (eventId: \$eventId) " +
            "}"
    var SEARCH_EVENT = "query searchByExpression (\$expression: String!) {" +
            "   searchByExpression (expression: \$expression) {\n" +
            "   id\n" +
            "   name\n" +
            "   categorization\n" +
            "   description\n" +
            "   logoImage\n" +
            "   city\n" +
            "   state\n" +
            "   rating\n" +
            "   placeName\n" +
            "   startDate\n" +
            "   categoryType\n" +
            "}\n" +
            "}"
    val GET_AVAILABLE_EVENTS_TO_CHECK_IN = "query GetCheckinAvailable(\$myLocation: [Float]!) {\n" +
            "   checkInAvailable(myLocation: \$myLocation) {\n" +
            "       id\n" +
            "       name\n" +
            "       club\n" +
            "       placeAddress\n" +
            "   }\n" +
            "}"


    var SEARCH_BY_NAME = "query GetEventByName(\$name: String!, \$categoryId: String, \$paginate: Int, \$limit: Int) {\n" +
            "   getEventByName(name: \$name, categoryId: \$categoryId, paginate: \$paginate, limit: \$limit) {" +
            "    id\n" +
            "    name\n" +
            "    description\n" +
            "    categorization\n" +
            "    logoImage\n" +
            "    city\n" +
            "    state\n" +
            "    rating\n" +
            "    placeName\n" +
            "    categoryType\n" +
            "    startDate\n" +
            "    score\n" +
            "    }" +
            "}"

    var EVENT_CONFIRM_ATTENDANCE = "mutation (\$eventID: String!, \$userName: String!) {" +
            " nameOnList (eventId: \$eventID, userName: \$userName) {" +
            " userId " +
            "type " +
            "placeName " +
            "image " +
            "title " +
            "subtitle " +
            "expiration " +
            "dateText " +
            "regulation " +
            "receiptText " +
            "validate " +
            "detail {" +
            "   action" +
            "   actionValue " +
            "   username " +
            "       }" +
            " } " +
            "}"

}
