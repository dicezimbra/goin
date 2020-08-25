package br.com.goin.feature_social.model

object FollowQueries {
    var GET_USER_FOLLOWERS = "query GetUserFollowers(\$userId: ID!) {" +
            "getUserFollowers(userId: \$userId) {\n" +
            "    id\n" +
            "    followerName\n" +
            "    followerAvatar\n" +
            "    followedByMe\n" +
            "  }" +
            "}"

    var GET_CURRENT_USER_FOLLOWERS = "query GetUserFollowers{" +
            "getUserFollowers{\n" +
            "    id\n" +
            "    followerName\n" +
            "    followerAvatar\n" +
            "    followedByMe\n" +
            "  }" +
            "}"

    var GET_USER_FOLLOWING = "query GetUserFollowing(\$userId: ID!) {" +
            "getUserFollowing(userId: \$userId) {\n" +
            "    id\n" +
            "    userName\n" +
            "    userAvatar\n" +
            "    followedByMe\n" +
            "  }" +
            "}"

    var GET_CURRENT_USER_FOLLOWING = "query GetUserFollowers{" +
            "getUserFollowing{\n" +
            "    id\n" +
            "    userName\n" +
            "    userAvatar\n" +
            "    followedByMe\n" +
            "  }" +
            "}"

    var FOLLOW_USER = "mutation FollowUser(\$userId: String) { followUser(id: \$userId) }"

    var UNFOLLOW_USER = "mutation UnfollowUser(\$userId: String)  { unfollowUser(id: \$userId){userId,followerId} }"
}