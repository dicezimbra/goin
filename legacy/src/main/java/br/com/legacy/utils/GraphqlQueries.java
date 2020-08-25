package br.com.legacy.utils;

/**
 * Created by appsimples on 9/15/17.
 */

public class GraphqlQueries {

    public static String VERIFY_EMAIL = "query VerifyEmail($email: String!) {\n" +
            "  verifyUserEmailExists(email: $email)\n" +
            "}";

    public static String CREATE_USER_TEMP = "mutation createUserTemp($input: UserTempInput) {\n" +
            "  createUserTemp(input: $input) {\n" +
            "       email" +
            "       password" +
            "   }" +
            "}";

    public static String FACEBOOK_SIGN_IN = "mutation FacebookSignIn($input: FacebookSigninInput) {\n" +
            "  facebookSignIn(input: $input) {\n" +
            "       isSignup" +
            "       user {" +
            "           id" +
            "           name" +
            "           email" +
            "           phone" +
            "           cpf" +
            "           dateOfBirth" +
            "           accountProviders" +
            "           profilePicture" +
            "           followingCount" +
            "           followersCount" +
            "           eventsCount" +
            "           clubsCount" +
            "           categoriesCount" +
            "           userScore" +
            "           ticketCount" +
            "           pushEndpoint" +
            "       }" +
            "   }" +
            "}";

    public static String REGISTER_CUSTOMER = "mutation RegisterUser($input: RegisterUserInput!) {" +
            "   registerUser(input: $input) {" +
            "       id" +
            "       email" +
            "       profilePicture" +
            "       eventsCount" +
            "       clubsCount" +
            "       followingCount" +
            "       followersCount" +
            "   }" +
            "}";

    public static String SEARCH_USER = "query SearchUsers($query: String) {" +
            "   searchUsers(query: $query) {" +
            "       name" +
            "       id" +
            "       followedByMe" +
            "       profilePicture" +
            "   }" +
            "}";

    public static String SEARCH_FB_USERS = "query SearchFacebookUsers {" +
            "   searchFacebookFriends {" +
            "       name" +
            "       id" +
            "       followedByMe" +
            "       profilePicture" +
            "   }" +
            "}";

    public static String GET_CATEGORIES = "query GetCategories {\n" +
            "  categories {\n" +
            "    id\n" +
            "    name\n" +
            "    image\n" +
            "    type\n" +
            "    imageWhite\n" +
            "  }\n" +
            "}";

    public static String GET_NEXT_EVENTS = "query GetAllEvents ($count: Int){\n" +
            "  allEvents (count: $count){\n" +
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
            "    image\n" +
            "    isConfirmed\n" +
            "    confirmType\n" +
            "    originType\n" +
            "    originId\n" +
            "    imageWidth\n" +
            "    imageHeight\n" +
            "    imageAspect\n" +
            "  }\n" +
            "}";

    public static String GET_SUBCATEGORIES = "query subcategories($categoryId: ID!) {\n" +
            "  subcategories (categoryId: $categoryId){\n" +
            "    subcategoryId\n" +
            "    categoryId\n" +
            "    name\n" +
            "    image\n" +
            "  }\n" +
            "}";

    public static String GET_MY_CATEGORIES = "query GetMyCategories {\n" +
            "  categories (selectedOnly: true) {\n" +
            "    id\n" +
            "    description\n" +
            "    selected\n" +
            "  }\n" +
            "}";

    public static String GET_BANNER_HOME = "query GetBanner {" +
            "   banners {\n" +
            "  id\n" +
            "  eventId\n" +
            "  clubId\n" +
            "  typeBanner\n" +
            "  url\n" +
            "}\n" +
            "}";

    public static String UPDATE_USER = "mutation UpdateUser($input: UpdateUserInput!) {" +
            "   updateUser(input: $input) {" +
            "       id" +
            "   }" +
            "}";
    public static String UNSELECT_CATEGORY = "mutation UnselectCategory($categoryId: String!) {\n" +
            "   unselectCategory(id: $categoryId)\n" +
            "}";

    public static String GET_MY_USER = "query GetMyUser {" +
            "   myUser {" +
            "       id" +
            "       name" +
            "       email" +
            "       cpf" +
            "       accountProviders" +
            "       profilePicture" +
            "       followingCount" +
            "       followersCount" +
            "       eventsCount" +
            "       clubsCount" +
            "       categoriesCount" +
            "       userScore" +
            "       ticketCount" +
            "       pushEndpoint" +
            "       bio" +
            "       postsCount" +
            "   }" +
            "}";

    public static String GET_USER_BY_ID = "query GetUserById($id: ID!) {\n" +
            "   user (id: $id) { \n" +
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
            "}\n";


    public static String CREATE_POST = "mutation CreatePost($input: CreatePostInput) { createPost(input: $input){" +
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
            "}";

    public static String CREATE_EVENT_POST = "mutation CreatePost($input: CreatePostInput, $eventId: ID) { createPost(input: $input, eventId: $eventId){" +
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
            "}";

    public static String GET_POSTS = "query GetMyPosts($count: Int, $clubId: ID, $userId: ID, $lastPostId: String) {" +
            "  posts(count: $count, clubId: $clubId, userId: $userId, lastPostId: $lastPostId) {\n" +
            "    name\n" +
            "    avatar\n" +
            "    commentsCount\n" +
            "    likesCount\n" +
            "    timeStamp\n" +
            "    description\n" +
            "    postId\n" +
            "    likedByMe\n" +
            "    location\n" +
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
            "}";

    public static String GET_POST = "query GetPost($postId: ID!) {" +
            "  getPost(postId: $postId) {\n" +
            "    name\n" +
            "    avatar\n" +
            "    commentsCount\n" +
            "    likesCount\n" +
            "    timeStamp\n" +
            "    description\n" +
            "    postId\n" +
            "    likedByMe\n" +
            "    location\n" +
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
            "}";


    public static String CONFIRM_TOKEN_IS_VALID = "query ConfirmTokenIsValid($originType: String) {\n" +
            "   confirmTokenIsValid (originType: $originType) {\n" +
            "       isValid\n" +
            "   }\n" +
            "}";

    public static String GET_USER_RAFFLES = "query GetMyRaffles($userId: String, $count: Int) {" +
            "   getMyRaffles(userId: $userId, count: $count) {" +
            "       raffleId" +
            "       endTime\n" +
            "       maxPeople\n" +
            "       name\n" +
            "       description\n" +
            "       image\n" +
            "   }" +
            "}";

    public static String GET_MY_RAFFLES = "query GetMyRaffles($count: Int) {" +
            "   getMyRaffles(count: $count) {" +
            "       raffleId" +
            "       endTime\n" +
            "       maxPeople\n" +
            "       name\n" +
            "       description\n" +
            "       image\n" +
            "   }" +
            "}";

    public static String JOIN_RAFFLE = "mutation joinRaffle($raffleId: String) {" +
            "   joinRaffle(raffleId: $raffleId)" +
            "}";

    public static String LEAVE_RAFFLE = "mutation leaveRaffle($raffleId: String) {" +
            "   leaveRaffle(raffleId: $raffleId)" +
            "}";

    public static String GET_NOTIFICATIONS = "query GetNotifications($lastId: ID, $count: Int) {" +
            "   notifications(lastId: $lastId, count: $count) {" +
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
            "}";

    public static String GET_COMMENTS = "query GetComments($postId: ID!) {" +
            "   comments(postId: $postId) {" +
            "       commentId" +
            "       name" +
            "       description" +
            "       timeStamp" +
            "       postId" +
            "       userId" +
            "   }" +
            "}";

    public static String CREATE_COMMENT = "mutation CreateComment($input: CreateCommentInput) {" +
            "   createComment(input: $input) {" +
            "       commentId" +
            "   }" +
            "}";

    public static String DELETE_COMMENT = "mutation DeleteComment($postId: String!, $commentId: String!) {" +
            "   deleteComment(postId: $postId, commentId: $commentId)" +
            "}";

    public static String LIKE_POST = "mutation LikePost($postId: ID!) {" +
            "   likePost(postId: $postId)" +
            "}";

    public static String DISLIKE_POST = "mutation DisikePost($postId: ID!) {" +
            "   dislikePost(postId: $postId)" +
            "}";

    public static String DELETE_POST = "mutation DeletePost($postId: ID!) {" +
            "   deletePost(postId: $postId)" +
            "}";

    public static String SHARE_POST = "mutation SharePost($postId: ID!) {" +
            "   sharePost(postId: $postId)" +
            "}";

    public static String GET_FEED = "query GetFeed($lastPostId: String, $lastMasterPostId: String) {" +
            "  feed(lastPostId: $lastPostId, lastMasterPostId: $lastMasterPostId) {\n" +
            "    post {\n" +
            "       name\n" +
            "       avatar\n" +
            "       commentsCount\n" +
            "       likesCount\n" +
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
            "       raffle {\n" +
            "           raffleId\n" +
            "           endTime\n" +
            "           maxPeople\n" +
            "           name\n" +
            "           joined\n" +
            "       }\n" +
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
            "}";

    public static String GET_EVENT_FEED = "query GetFeed($lastPostId: String, $eventId: String) {" +
            "  feed(lastPostId: $lastPostId, eventId: $eventId) {\n" +
            "    post {\n" +
            "       name\n" +
            "       avatar\n" +
            "       commentsCount\n" +
            "       likesCount\n" +
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
            "       raffle {\n" +
            "           raffleId\n" +
            "           endTime\n" +
            "           maxPeople\n" +
            "           name\n" +
            "           joined\n" +
            "       }\n" +
            "       sharedBy {\n" +
            "           id\n" +
            "           name\n" +
            "           avatar\n" +
            "       }\n" +
            "    }\n" +
            "    lastPostId\n" +
            "    morePostsToGet\n" +
            "  }\n" +
            "}";

    public static String GET_USER_FOLLOWERS = "query GetUserFollowers($userId: ID!) {" +
            "getUserFollowers(userId: $userId) {\n" +
            "    id\n" +
            "    followerName\n" +
            "    followerAvatar\n" +
            "    followedByMe\n" +
            "  }" +
            "}";

    public static String GET_CURRENT_USER_FOLLOWERS = "query GetUserFollowers{" +
            "getUserFollowers{\n" +
            "    id\n" +
            "    followerName\n" +
            "    followerAvatar\n" +
            "    followedByMe\n" +
            "  }" +
            "}";

    public static String GET_USER_FOLLOWING = "query GetUserFollowing($userId: ID!) {" +
            "getUserFollowing(userId: $userId) {\n" +
            "    id\n" +
            "    userName\n" +
            "    userAvatar\n" +
            "    followedByMe\n" +
            "  }" +
            "}";

    public static String GET_CURRENT_USER_FOLLOWING = "query GetUserFollowers{" +
            "getUserFollowing{\n" +
            "    id\n" +
            "    userName\n" +
            "    userAvatar\n" +
            "    followedByMe\n" +
            "  }" +
            "}";

    public static String GET_EVENTS = "query GetEvents($clubId: ID, $count: Int, $locationBlocked: Boolean, $query: String, $myLocation: [Float], $categories: [ID], $filter: String, $priceRange: [Float]) {\n" +
            "  events(clubId: $clubId, count: $count, locationBlocked: $locationBlocked, query: $query, myLocation: $myLocation, categories: $categories, filter: $filter, priceRange: $priceRange) {\n" +
            "    id\n" +
            "    clubId\n" +
            "    name\n" +
            "    club\n" +
            "    startDate\n" +
            "    description\n" +
            "    intentionCount\n" +
            "    lowestPrice\n" +
            "    highestPrice\n" +
            "    subcategories\n" +
            "    categoryEventType\n" +
            "    placeAddress\n" +
            "    latitude\n" +
            "    longitude\n" +
            "    image\n" +
            "    isConfirmed\n" +
            "    confirmType\n" +
            "  }\n" +
            "}";

    public static String GET_SEARCH_EVENTS = "query GetSearchEvents($query: String!, $filter: EventFilterType!, $pagination: Int, $myLocation: [Float]!, $categories: [ID], $priceRange: [Float]) {\n" +
            "  searchEvents(query: $query, filter: $filter, pagination: $pagination, myLocation: $myLocation, categories: $categories, priceRange: $priceRange) {\n" +
            "    id\n" +
            "    clubId\n" +
            "    name\n" +
            "    club\n" +
            "    startDate\n" +
            "    interestedCount\n" +
            "    lowestPrice\n" +
            "    placeAddress\n" +
            "    latitude\n" +
            "    longitude\n" +
            "    image\n" +
            "    isConfirmed\n" +
            "    confirmType\n" +
            "  }\n" +
            "}";

    public static String GET_SEARCH_EVENTS_NEW = "query searchEvents($query: String!, $pagination: Int, $categoryId: ID,$subCategoryId: ID, $location: [Float]!) {\n" +
            "  searchEvents(query: $query,  pagination: $pagination, categoryId: $categoryId, subcategoryId: $subCategoryId, myLocation: $location) {\n" +
            "    id\n" +
            "    clubId\n" +
            "    name\n" +
            "    club\n" +
            "    image\n" +
            "  }\n" +
            "}";

    public static String SEARCH_EVENTS = "query GetSearchEvents($query: String!, $filter: EventFilterType, $pagination: Int, $myLocation: [Float]!, $categoryId: ID, $priceRange: [Float],$subCategoryId: ID) {\n" +
            "  searchEvents(query: $query, filter: $filter, pagination: $pagination, myLocation: $myLocation, categoryId: $categoryId, priceRange: $priceRange, subcategoryId: $subCategoryId) {\n" +
            "    id\n" +
            "    clubId\n" +
            "    name\n" +
            "    club\n" +
            "    startDate\n" +
            "    interestedCount\n" +
            "    lowestPrice\n" +
            "    placeAddress\n" +
            "    latitude\n" +
            "    longitude\n" +
            "    image\n" +
            "    isConfirmed\n" +
            "    confirmType\n" +
            "  }\n" +
            "}";

    public static String GET_EVENTS_SUGGESTIONS = "query GetEventSuggestions($pagination: Int, $myLocation: [Float]!, $count: Int) {\n" +
            "  eventSuggestions(pagination: $pagination, myLocation: $myLocation, count: $count) {\n" +
            "    id\n" +
            "    clubId\n" +
            "    name\n" +
            "    club\n" +
            "    startDate\n" +
            "    interestedCount\n" +
            "    lowestPrice\n" +
            "    placeAddress\n" +
            "    latitude\n" +
            "    longitude\n" +
            "       CategoriesInfo {" +
            "           name\n" +
            "           id\n" +
            "       }" +
            "    image\n" +
            "    isConfirmed\n" +
            "    confirmType\n" +
            "  }\n" +
            "}";


    public static String GET_MY_EVENTS = "query GetMyEventsShort($userId: ID, $count: Int) {\n" +
            "  myEvents(userId: $userId, count: $count) {\n" +
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
            "}";

    public static String GET_EVENT = "query GetEventById($id: String!) {\n" +
            "   getEvent(id: $id) {\n" +
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
            "       highestPrice\n" +
            "       placeName\n" +
            "       placeAddress\n" +
            "       latitude\n" +
            "       longitude\n" +
            "       image\n" +
            "       isConfirmed\n" +
            "       originHasDiscount\n" +
            "       originInfos {" +
            "           buttonColor\n" +
            "           bigIcon\n" +
            "           smallIconColored\n" +
            "           smallIconWhite\n" +
            "       }" +
            "       confirmType\n" +
            "       originType\n" +
            "       originURL\n" +
            "   }\n" +
            "}";

    public static String GET_EVENT_TICKETS = "query GetEventTickets($eventId: String!) {\n" +
            "   getEventTickets(eventId: $eventId) {\n" +
            "       id\n" +
            "       name\n" +
            "       price\n" +
            "       description\n" +
            "       quantity\n" +
            "       ticketType\n" +
            "       isHalfPrice\n" +
            "       remaining\n" +
            "       eventDate\n" +
            "       eventName\n" +
            "       eventAddress" +
            "       clubName\n" +
            "       available\n" +
            "       capacity\n" +

            "       discount\n" +
            "       discountPercent\n" +
            "       discountValue\n" +
            "       finalValue\n" +
            "       originUrl\n" +
            "   }\n" +
            "}";

    public static String GET_CHECKIN_AVAILABLE = "query GetCheckinAvailable($myLocation: [Float]!) {\n" +
            "   checkInAvailable(myLocation: $myLocation) {\n" +
            "       id\n" +
            "       name\n" +
            "       club\n" +
            "       placeAddress\n" +
            "   }\n" +
            "}";

    public static String GET_SEARCH_MAP_EVENTS = "query GetSearchMapEvents($query: String!, $filter: EventFilterType!, $myLocation: [Float]!, $topLeftLatLon: [Float]!, $bottomRightLatLon: [Float]!, $categories: [ID], $priceRange: [Float]) {" +
            "  searchMapEvents(query: $query, filter: $filter, myLocation: $myLocation, topLeftLatLon: $topLeftLatLon, bottomRightLatLon: $bottomRightLatLon, categories: $categories, priceRange: $priceRange) {\n" +
            "       id\n" +
            "       clubId\n" +
            "       name\n" +
            "       club\n" +
            "       startDate\n" +
            "       interestedCount\n" +
            "       lowestPrice\n" +
            "       placeAddress\n" +
            "       latitude\n" +
            "       longitude\n" +
            "       image\n" +
            "       isConfirmed\n" +
            "       confirmType\n" +

            "   }\n" +
            "}";

    public static String GET_SEARCH_MAP_SUGGESTIONS = "query GetSearchMapSuggestions($filter: EventFilterType!, $myLocation: [Float]!, $topLeftLatLon: [Float]!, $bottomRightLatLon: [Float]!) {" +
            "  searchMapSuggestions(filter: $filter, myLocation: $myLocation, topLeftLatLon: $topLeftLatLon, bottomRightLatLon: $bottomRightLatLon) {\n" +
            "       id\n" +
            "       clubId\n" +
            "       name\n" +
            "       club\n" +
            "       startDate\n" +
            "       interestedCount\n" +
            "       lowestPrice\n" +
            "       placeAddress\n" +
            "       latitude\n" +
            "       longitude\n" +
            "       image\n" +
            "       isConfirmed\n" +
            "       confirmType\n" +

            "   }\n" +
            "}";

    public static String SEND_CHAT_TEXT_MESSAGE = "mutation SendMessage($receiverId: String!, $message: String){" +
            "   sendMessage(input: {receiverId: $receiverId, message: $message}){" +
            "       chatId" +
            "   }" +
            "}";

    public static String SEND_CHAT_IMAGE_MESSAGE = "mutation SendMessage($receiverId: String!, $photoUrl: String){" +
            "   sendMessage(input: {receiverId: $receiverId, photoUrl: $photoUrl}){" +
            "       chatId" +
            "   }" +
            "}";

    public static String SEND_GROUP_CHAT_TEXT_MESSAGE = "mutation SendGroupMessage($groupId: String!, $message: String){" +
            "   sendGroupMessage(input: {groupId: $groupId, message: $message})}";

    public static String SEND_GROUP_CHAT_IMAGE_MESSAGE = "mutation SendGroupMessage($groupId: String!, $photoUrl: String){" +
            "   sendGroupMessage(input: {groupId: $groupId, photoUrl: $photoUrl})}";

    public static String CREATE_GROUP = "mutation CreateGroup($name: String!, $users: [String]) { \n" +
            "   createGroup(input:{ name: $name, users: $users}){ \n" +
            "       groupId\n" +
            "   }\n" +
            "}";

    public static String LEAVE_GROUP = "mutation LeaveGroup($groupId: String) { \n" +
            "   leaveGroup(groupId: $groupId)} \n";

    public static String EDIT_GROUP = "mutation EditGroup($name: String!, $groupId: String!) { \n" +
            "   editGroupName(input: {name: $name, groupId: $groupId})} \n";

    public static String GET_FRIENDS_TO_INVITE = "query GetFriendsToInvite($eventId: ID!) {\n" +
            "   friendsToInvite (eventId: $eventId) {\n" +
            "       userId\n" +
            "       name\n" +
            "       avatar\n" +
            "       invitedByMe\n" +
            "   }\n" +
            "}";


    public static String SEARCH_FRIENDS = "query SearchFriends($query: String) {\n" +
            "   searchFriends (query: $query) {\n" +
            "       id\n" +
            "       name\n" +
            "       avatar\n" +
            "       chatId\n" +
            "   }\n" +
            "}";

    public static String INVITE_FRIEND = "mutation InviteFriend($eventId: ID!, $invitedId: ID!) {\n" +
            "   inviteToEvent (eventId: $eventId, invitedId: $invitedId)\n" +
            "}\n";

    public static String CONFIRM_PRESENCE = "mutation ConfirmPresence($eventId: ID!) {" +
            "   confirmInterest (eventId: $eventId) " +
            "}";

    public static String UNCONFIRM_PRESENCE = "mutation UnconfirmPresence($eventId: ID!) {" +
            "   unconfirmInterest (eventId: $eventId) " +
            "}";

    public static String GET_SEARCH_CLUBS = "query GetSearchClubs($query: String) {\n" +
            "  searchClubs (query: $query) {\n" +
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
            "}";

    public static String SEARCH_CLUBS = "query searchClubs($query: String!, $pagination: Int, $categoryId: ID,$subcategoryId: ID) {\n" +
            "  searchClubs(query: $query, pagination: $pagination, categoryId: $categoryId, subcategoryId: $subcategoryId) {\n" +
            "    id\n" +
            "    name\n" +
            "    website\n" +
            "    followersCount\n" +
            "    logoImage\n" +
            "    rating \n" +
            "    ratingCount\n" +
            "    followedByMe\n" +
            "  }\n" +
            "}";


    public static String FILTER_RESULTS = "query searchFilter($input: SearchFilterRequest!, $paginate: Int!, $limit: Int!) {\n" +
            "   searchFilter(input: $input, paginate: $paginate, limit: $limit) {\n" +
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
            "   }";


    public static String GET_STATES = "query getStates{" +
            "       searchStates {\n" +
            "            state\n" +
            "       }" +
            "}";


    public static String GET_CITIES_BY_STATE = "query getCities($state: String!){" +
            "   searchCities(state: $state) {\n" +
            "       city\n" +
            "    }\n" +
            "}";


    public static String GET_FOLLOWED_CLUBS = "query GetFollowedClubs($userId: ID) {\n" +
            "  followedClubs (userId: $userId) {\n" +
            "    id\n" +
            "    name\n" +
            "    website\n" +
            "    followersCount\n" +
            "    logoImage\n" +
            "    rating \n" +
            "    ratingCount\n" +
            "    followedByMe\n" +
            "  }\n" +
            "}";

    public static String FOLLOW_CLUB = "mutation FollowClub($clubId: ID) { follow(clubId: $clubId) }";

    public static String UNFOLLOW_CLUB = "mutation UnfollowClub($clubId: ID)  { unfollow(clubId: $clubId) }";

    public static String GET_CLUB = "query GetClub($clubId: ID!, $eventCount: Int, $postCount: Int) {\n" +
            "  club(id: $clubId) {\n" +
            "    id\n" +
            "    name\n" +
            "    coverImage\n" +
            "    description\n" +
            "    phone\n" +
            "    website\n" +
            "    followedByMe\n" +
            "    followersCount\n" +
            "    address\n" +
            "    latitude\n" +
            "    longitude\n" +
            "    photoGallery {\n" +
            "      url\n" +
            "    }\n" +
            "    posts(count: $postCount) {\n" +
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
            "    events(count: $eventCount) {\n" +
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
            "    \n" +
            "  }\n" +
            "}";

    public static String GET_CLUB_FOLLOWERS = "query GetClubFollowers($clubId: String) {\n" +
            "  getClubFollowers(clubId: $clubId) {\n" +
            "    id\n" +
            "    name\n" +
            "    profilePicture\n" +
            "    followedByMe\n" +
            "  }" +
            "}";

    public static String GET_CLUB_EVENTS = "query GetClub($clubId: ID!, $eventCount: Int) {\n" +
            "  club(id: $clubId) {\n" +
            "    events(count: $eventCount) {\n" +
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
            "}";

    public static String GET_POST_LIKERS = "query GetLikers($postId: String!) { " +
            "     postLikers(postId: $postId) {\n" +
            "       name\n" +
            "       avatar\n" +
            "       userId\n" +
            "  }" +
            " }";

    public static String GET_EVENT_CONFIRMED_PEOPLE = "query GetConfirmedPeople($input: ConfirmedPeopleInput) { " +
            "     confirmedPeople(input: $input) {\n" +
            "       name\n" +
            "       avatar\n" +
            "       userId\n" +
            "       followedByMe\n" +
            "  }" +
            " }";

    public static String FOLLOW_USER = "mutation FollowUser($userId: String) { followUser(id: $userId) }";

    public static String UNFOLLOW_USER = "mutation UnfollowUser($userId: String)  { unfollowUser(id: $userId){userId,followerId} }";

    public static String GET_MY_INDIVIDUAL_TICKETS = "query GetMyTickets($lastId: ID, $count: Int) {\n" +
            "  getMyTickets(lastId: $lastId, count: $count) {\n" +
            "       id\n" +
            "       name\n" +
            "       price\n" +
            "       eventName\n" +
            "       eventDate\n" +
            "       clubName\n" +
            "       buyerName\n" +
            "       numTickets\n" +
            "       paymentStatus\n" +
            "       paymentDate\n" +
            "       buyerName\n" +
            "       ticketType\n" +
            "       capacity\n" +
            "       event{ id\n" +
            "       latitude\n" +
            "       longitude\n" +
            "       image \n" +
            "       startDate\n" +
            "       endDate}\n" +
            "  }\n" +
            "}";

    public static String GET_MY_VIP_BOX_TICKETS = "query GetMyVipTickets($lastId: ID, $count: Int) {\n" +
            "  getMyVipTickets(lastId: $lastId, count: $count) {\n" +
            "       id\n" +
            "       name\n" +
            "       price\n" +
            "       eventName\n" +
            "       eventDate\n" +
            "       clubName\n" +
            "       buyerName\n" +
            "       numTickets\n" +
            "       paymentStatus\n" +
            "       paymentDate\n" +
            "       buyerName\n" +
            "       ticketType\n" +
            "       capacity\n" +
            "  }\n" +
            "}";

    public static String GET_MY_TICKET_DETAIL = "query GetMyTicketDetail($id: String!) {" +
            "   getMyTicketDetail(id: $id) {\n" +
            "       name\n" +
            "       description\n" +
            "       price\n" +
            "       eventDate\n" +
            "       eventName\n" +
            "       eventId\n" +
            "       eventAddress\n" +
            "       buyerName\n" +
            "       paymentDate\n" +
            "       paymentStatus\n" +
            "       capacity\n" +
            "       ticketType\n" +
            "       vipBoxId\n" +
            "       paymentInfo {\n" +
            "              type\n" +
            "              months\n" +
            "              cardHolder\n" +
            "              CPF\n" +
            "              displayNumber\n" +
            "              postalCode\n" +
            "              street\n" +
            "              streetNumber\n" +
            "              complement\n" +
            "              neighborhood\n" +
            "              city\n" +
            "              state\n" +
            "              buyerEmail\n" +
            "              buyerPhone\n" +
            "              buyerName\n" +
            "              buyerPhone\n" +
            "              pdf\n" +
            "              purchaseTotal\n" +
            "       }\n" +
            "       ticketsInfo {\n" +
            "              userId\n" +
            "              name\n" +
            "              RG\n" +
            "              halfPriceId\n" +
            "       }\n" +
            "   }\n" +
            "}";

    public static String BUY_TICKET = "mutation BuyTicket($eventId: ID!, $clubId: ID!, $ticketId: ID!, $quantity: Int!) {" +
            " buyTicket(eventId: $eventId, clubId: $clubId, ticketId: $ticketId, quantity: $quantity)" +
            "}";

    public static String TRANSFER_TICKET = "mutation TransferTicket($input: TransferTicketInput!){\n" +
            "   transferTicket(input: $input)\n" +
            "}";

    public static String CONFIRM_TICKET = "mutation ConfirmBuyTicket($ticketId: ID!, $paypalData: String) {" +
            " confirmBuyTicket(ticketId: $ticketId, paypalData: $paypalData)" +
            "}";

    public static String BUY_TICKET_CREDIT_CARD = "mutation BuyTicketCreditCard($input: BuyTicketInput!) {" +
            " buyTicketCreditCard(input: $input){" +
            "       name\n" +
            "       description\n" +
            "       price\n" +
            "       eventDate\n" +
            "       ticketId\n" +
            "       eventName\n" +
            "       eventId\n" +
            "       eventAddress\n" +
            "       buyerName\n" +
            "       paymentDate\n" +
            "       paymentStatus\n" +
            "       capacity\n" +
            "       originType\n" +
            "       paymentInfo {\n" +
            "              type\n" +
            "              cvv\n" +
            "              cardHolder\n" +
            "              CPF\n" +
            "              displayNumber\n" +
            "              postalCode\n" +
            "              street\n" +
            "              streetNumber\n" +
            "              complement\n" +
            "              neighborhood\n" +
            "              city\n" +
            "              state\n" +
            "              buyerEmail\n" +
            "              buyerPhone\n" +
            "              buyerName\n" +
            "              buyerPhone\n" +
            "              pdf\n" +
            "       }\n" +
            "       ticketsInfo {\n" +
            "              userId\n" +
            "              name\n" +
            "              RG\n" +
            "              halfPriceId\n" +
            "       }\n" +
            "   }\n" +
            "}";

    public static String BUY_TICKET_BANK_SLIP = "mutation buyTicketBankSlip($input: BuyTicketInput!) {" +
            " buyTicketBankSlip(input: $input){" +
            "       name\n" +
            "       description\n" +
            "       price\n" +
            "       eventDate\n" +
            "       eventName\n" +
            "       ticketId\n" +
            "       eventId\n" +
            "       eventAddress\n" +
            "       buyerName\n" +
            "       paymentDate\n" +
            "       paymentStatus\n" +
            "       capacity\n" +
            "       paymentInfo {\n" +
            "              type\n" +
            "              installments\n" +
            "              cardHolder\n" +
            "              CPF\n" +
            "              displayNumber\n" +
            "              postalCode\n" +
            "              street\n" +
            "              streetNumber\n" +
            "              complement\n" +
            "              neighborhood\n" +
            "              city\n" +
            "              state\n" +
            "              buyerEmail\n" +
            "              buyerPhone\n" +
            "              buyerName\n" +
            "              buyerPhone\n" +
            "              pdf\n" +
            "       }\n" +
            "       ticketsInfo {\n" +
            "              userId\n" +
            "              name\n" +
            "              RG\n" +
            "              halfPriceId\n" +
            "       }\n" +
            "   }\n" +
            "}";


    public static String CANCEL_TICKET = "mutation CancelBuyTicket($ticketId: ID!, $eventId: ID!) {" +
            " cancelBuyTicket(ticketId: $ticketId, eventId: $eventId)" +
            "}";

    public static String UPDATE_PARTNER_INFO = "mutation UpdatePartnerInfo($originType: String, $token: String, $userId: String) {" +
            " updatePartnerInfo(originType: $originType, token: $token, userId: $userId)" +
            "}";

    public static String VALIDATE_COUPOM = "query ValidateCoupon($eventId: String!, $coupon: String!) {\n" +
            "   validateCoupon (eventId: $eventId, coupon: $coupon) {\n" +
            "       valid\n" +
            "       message\n" +
            "   }\n" +
            "}";

    public static String HAS_UPDATES = "query GetVersionStatus($os: VersionType, $version: String) {" +
            "  getVersionStatus(os: $os, version: $version) {\n" +
            "    status\n" +
            "  }" +
            "}";
}


