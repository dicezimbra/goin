package br.com.goin.component_model_club

object ClubQueries {



    var GET_SEARCH_CLUBS = "query GetSearchClubs(\$query: String) {\n" +
            "  searchClubs (query: \$query) {\n" +
            "    id\n" +
            "    name\n" +
            "    website\n" +
            "    address\n" +
            "    followersCount\n" +
            "    logoImage\n" +
            "    rating \n" +
            "    categories \n" +
            "    latitude \n" +
            "    longitude \n" +
            "    ratingCount\n" +
            "    followedByMe\n" +
            "  }\n" +
            "}"

    var SEARCH_CLUBS = "query searchClubs(\$query: String!, \$pagination: Int, \$categoryId: ID,\$subcategoryId: ID) {\n" +
            "  searchClubs(query: \$query, pagination: \$pagination, categoryId: \$categoryId, subcategoryId: \$subcategoryId) {\n" +
            "    id\n" +
            "    name\n" +
            "    website\n" +
            "    followersCount\n" +
            "    logoImage\n" +
            "    rating \n" +
            "    ratingCount\n" +
            "    followedByMe\n" +
            "  }\n" +
            "}"

    var GET_FOLLOWED_CLUBS = "query GetFollowedClubs(\$userId: ID) {\n" +
            "  followedClubs (userId: \$userId) {\n" +
            "    id\n" +
            "    name\n" +
            "    website\n" +
            "    followersCount\n" +
            "    logoImage\n" +
            "    rating \n" +
            "    ratingCount\n" +
            "    followedByMe\n" +
            "  }\n" +
            "}"

    var FOLLOW_CLUB = "mutation FollowClub(\$clubId: ID) { follow(clubId: \$clubId) }"

    var UNFOLLOW_CLUB = "mutation UnfollowClub(\$clubId: ID)  { unfollow(clubId: \$clubId) }"

    var GET_CLUB = "query GetClub(\$clubId: ID!, \$eventCount: Int, \$postCount: Int) {\n" +
            "  club(id: \$clubId) {\n" +
            "    id\n" +
            "    name\n" +
            "    coverImage\n" +
            "    description\n" +
            "    phone\n" +
            "    website\n" +
            "    followedByMe\n" +
            "    followersCount\n" +
            "    address\n" +
            "    logoImage\n" +
            "    latitude\n" +
            "    longitude\n" +
            "    priceRating\n" +
            "    photos \n" +
            "    tags {\n" +
            "    name \n" +
            "    }\n" +
            "    posts(count: \$postCount) {\n" +
            "      postId\n" +
            "      posterId\n" +
            "      description\n" +
            "      location\n" +
            "      eventName\n" +
            "      checkInEventId\n" +
            "      feeling\n" +
            "      timeStamp\n" +
            "      likesCount\n" +
            "      commentsCount\n" +
            "      image\n" +
            "      avatar\n" +
            "      name\n" +
            "      comment\n" +
            "      commentAuthor\n" +
            "      likedByMe\n" +
            "    }\n" +
            "    events(count: \$eventCount) {\n" +
            "      id\n" +
            "      clubId\n" +
            "      name\n" +
            "      club\n" +
            "      startDate\n" +
            "      interestedCount\n" +
            "      lowestPrice\n" +
            "      highestPrice\n" +
            "      subcategories\n" +
            "      categoryEventType\n" +
            "      placeAddress\n" +
            "      image\n" +
            "      isConfirmed\n" +
            "      latitude\n" +
            "      longitude\n" +
            "      confirmType\n" +
            "    }\n" +
            "    rating\n" +
            "    ratingCount\n" +
            "       giftsGallery {\n" +
            "           giftId\n" +
            "           title\n" +
            "           subtitle\n" +
            "           dateText\n" +
            "           image\n" +
            "           detailTitle\n" +
            "           placeName\n" +
            "           rules\n" +
            "    }"+
            "    \n" +
            "  }\n" +
            "}"

    val GET_CLUB_MOVIE_THEATER = "query GetClub(\$clubId: ID!) {\n" +
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

    val FILTER_RESULTS = "query searchFilter(\$input: SearchFilterInput!, \$paginate: Int!, \$limit: Int!) {\n" +
            "   searchFilter(input: \$input, paginate: \$paginate, limit: \$limit) {\n" +
            "       currentPage\n" +
            "       totalPages\n" +
            "       totalItems\n" +
            "       events {\n" +
            "           id\n" +
            "           categoryType\n" +
            "           name\n" +
            "           subcategories\n" +
            "           image\n" +
            "           placeAddress\n" +
            "           latitude\n" +
            "           longitude\n" +
            "           lowestPrice\n" +
            "           rating\n" +
            "       }\n" +
            "       clubs {\n" +
            "           id\n" +
            "           name\n" +
            "           logoImage\n" +
            "           address\n" +
            "           latitude\n" +
            "           longitude\n" +
            "       }\n" +
            "      }\n" +
            "   }"

    var GET_CLUB_FOLLOWERS = "query GetClubFollowers(\$clubId: String) {\n" +
            "  getClubFollowers(clubId: \$clubId) {\n" +
            "    id\n" +
            "    name\n" +
            "    profilePicture\n" +
            "    followedByMe\n" +
            "  }" +
            "}"

    var GET_CLUB_EVENTS = "query GetClub(\$clubId: ID!, \$eventCount: Int) {\n" +
            "  club(id: \$clubId) {\n" +
            "    events(count: \$eventCount) {\n" +
            "      id\n" +
            "      clubId\n" +
            "      name\n" +
            "      club\n" +
            "      startDate\n" +
            "      endDate\n" +
            "      interestedCount\n" +
            "      lowestPrice\n" +
            "      placeAddress\n" +
            "      image\n" +
            "      isConfirmed\n" +
            "      latitude\n" +
            "      longitude\n" +
            "    }\n" +
            "  }\n" +
            "}"


    var RATE_CLUB = "mutation RateClub(\$rating: Float, \$comment: String, \$clubId: String!){\n" +
            "   rateClub( input:{\n" +
            "       rating: \$rating\n" +
            "       comment: \$comment\n" +
            "       clubId: \$clubId\n" +
            "   }) {\n" +
            "       rating\n" +
            "       comment\n" +
            "       userName\n" +
            "       avatar\n" +
            "   }\n" +
            "}"



    var UPDATE_RATING = "mutation UpdateRating(\$rating: Float, \$comment: String, \$clubId: String!){\n" +
            "   updateRating( input:{\n" +
            "       rating: \$rating\n" +
            "       comment: \$comment\n" +
            "       clubId: \$clubId\n" +
            "   }) {\n" +
            "       rating\n" +
            "       comment\n" +
            "       userName\n" +
            "       avatar\n" +
            "   }\n" +
            "}"

    var DELETE_RATING = "mutation DeleteRating(\$clubId: String!) {" +
            "   deleteRating(clubId: \$clubId)" +
            "}"

    var GET_CLUB_RATINGS = "query GetClubRatings(\$clubId: ID!){\n" +
            "   ratings(clubId: \$clubId) {\n" +
            "       ratingsList {\n" +
            "           rating\n" +
            "           comment\n" +
            "           userName\n" +
            "           avatar\n" +
            "           userId\n" +
            "       }" +
            "       globalRating\n" +
            "       globalRatingCount\n" +
            "       ratedByMe\n" +
            "   }\n" +
            "}"
}