package br.com.goin.findfriends.model

object FindFriendsQuery {

    var SEARCH_USER = "query SearchUsers(\$query: String) {" +
            "searchUsers(query: \$query) {" +
            "       name" +
            "       id" +
            "       followedByMe" +
            "       profilePicture" +
            "   }" +
            "}"

    var FOLLOW_USER = "mutation FollowUser(\$currentUserId: String) { followUser(id: \$currentUserId) }"

    var UNFOLLOW_USER = "mutation UnfollowUser(\$currentUserId: String)  { unfollowUser(id: \$currentUserId){currentUserId,followerId} }"
}