package br.com.goin.feature.feed.model

object PostQueries {

    var DELETE_POST = "mutation DeletePost(\$postId: ID!) {" +
            "   deletePost(postId: \$postId)" +
            "}"

    var REPORT_POST = "mutation ReportPost(\$postId: ID!) {" +
            "   reportPost(postId: \$postId)" +
            "}"

    var LIKE_POST = "mutation LikePost(\$postId: ID!) {" +
            "   likePost(postId: \$postId)" +
            "}"

    var DISLIKE_POST = "mutation DisikePost(\$postId: ID!) {" +
            "   dislikePost(postId: \$postId)" +
            "}"


    var GET_MY_FEED = "query GetMyPosts(\$count: Int, \$clubId: ID, \$userId: ID, \$lastPostId: String) {" +
            "  posts(count: \$count, clubId: \$clubId, userId: \$userId, lastPostId: \$lastPostId) {\n" +
            "    name\n" +
            "    avatar\n" +
            "    commentsCount\n" +
            "    likesCount\n" +
            "    timeStamp\n" +
            "    description\n" +
            "    postId\n" +
            "    likedByMe\n" +
            "    location\n" +
            "    clubName\n" +
            "    eventName\n" +
            "    checkInEventId\n" +
            "    image\n" +
            "    feeling\n" +
            "    posterId\n" +
            "    comment\n" +
            "    commentAuthor\n" +
            "    video\n" +
            "    sharedBy {\n" +
            "       id\n" +
            "       name\n" +
            "       avatar\n" +
            "    }\n" +
            "  }" +
            "}"

    var GET_FEED = "query GetFeed(\$lastPostId: String, \$lastMasterPostId: String) {" +
            "  feed(lastPostId: \$lastPostId, lastMasterPostId: \$lastMasterPostId) {\n" +
            "    post {\n" +
            "       name\n" +
            "       avatar\n" +
            "       commentsCount\n" +
            "       likesCount\n" +
            "    clubName\n" +
            "       timeStamp\n" +
            "       description\n" +
            "       postId\n" +
            "       likedByMe\n" +
            "       location\n" +
            "       eventName\n" +
            "       checkInEventId\n" +
            "       image\n" +
            "       video\n" +
            "       feeling\n" +
            "       posterId\n" +
            "       comment\n" +
            "       commentAuthor\n" +
            "       sharedBy {\n" +
            "           id\n" +
            "           name\n" +
            "           avatar\n" +
            "       }\n" +
            "    }\n" +
            "    lastPostId\n" +
            "    lastMasterPostId\n" +
            "    morePostsToGet\n" +
            "  }\n" +
            "}"


    var CREATE_POST = "mutation CreatePost(\$input: CreatePostInput) {" +
            " createPost(input: \$input){" +
            "    postId\n" +
            "    posterId\n" +
            "    description\n" +
            "    location\n" +
            "    eventName\n" +
            "    checkInEventId\n" +
            "    feeling\n" +
            "    name\n" +
            "    avatar\n" +
            "    timeStamp\n" +
            "    image\n" +
            "    video\n" +
            "   }" +
            "}"

    var CREATE_POST_WITH_EVENT = "mutation CreatePost(\$input: CreatePostInput, \$eventId: ID!, \$clubId: ID) {" +
            " createPost(input: \$input, eventId: \$eventId, clubId: \$clubId){" +
            "    postId\n" +
            "    posterId\n" +
            "    description\n" +
            "    location\n" +
            "    eventName\n" +
            "    checkInEventId\n" +
            "    feeling\n" +
            "    name\n" +
            "    avatar\n" +
            "    timeStamp\n" +
            "    image\n" +
            "    video\n" +
            "   }" +
            "}"


    var GET_COMMENTS = "query GetComments(\$postId: ID!) {" +
            "   comments(postId: \$postId) {" +
            "       commentId" +
            "       name" +
            "       description" +
            "       timeStamp" +
            "       postId" +
            "       userId" +
            "   }" +
            "}"

    var CREATE_COMMENT = "mutation CreateComment(\$input: CreateCommentInput) {" +
            "   createComment(input: \$input) {" +
            "       commentId" +
            "   }" +
            "}"
    var DELETE_COMMENT = "mutation DeleteComment(\$postId: String!, \$commentId: String!) {" +
            "   deleteComment(postId: \$postId, commentId: \$commentId)" +
            "}"


}