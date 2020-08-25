package br.com.goin.feature.notifications.model

object NotificationsQueries {

    var GET_NOTIFICATIONS = "query GetNotifications(\$lastId: ID, \$count: Int) {" +
            "   notifications(lastId: \$lastId, count: \$count) {" +
            "       id" +
            "       timeStamp" +
            "       firstName" +
            "       secondName" +
            "       categoryType" +
            "       type" +
            "       creatorId" +
            "       destinationId" +
            "       avatar" +
            "       followedByMe" +
            "   }" +
            "}"

    var FOLLOW_USER = "mutation FollowUser(\$userId: String) { followUser(id: \$userId) }"

    var UNFOLLOW_USER = "mutation UnfollowUser(\$userId: String)  { unfollowUser(id: \$userId){userId,followerId} }"
}