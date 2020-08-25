package br.com.goin.feature.profile.model

object ProfileQueries {

    var FOLLOW_USER = "mutation FollowUser(\$userId: String) { followUser(id: \$userId) }"

    var UNFOLLOW_USER = "mutation UnfollowUser(\$userId: String)  { unfollowUser(id: \$userId){userId,followerId} }"

    var UPDATE_PROFILE = "mutation UpdateUser(\$input: UpdateUserInput!) {" +
            "   updateUser(input: \$input) {" +
            "       id" +
            "   }" +
            "}"

    var GET_USER_BY_ID = "query GetUserById(\$id: ID!) {\n" +
            "   user (id: \$id) { \n" +
            "       id\n" +
            "       name\n" +
            "       email\n" +
            "       cpf\n" +
            "       accountProviders\n" +
            "       profilePicture\n" +
            "       followingCount\n" +
            "       followersCount\n" +
            "       eventsCount\n" +
            "       clubsCount\n" +
            "       categoriesCount\n" +
            "       userScore\n" +
            "       ticketCount\n" +
            "       followedByMe\n" +
            "       postsCount\n" +
            "}" +
            "}\n"

}