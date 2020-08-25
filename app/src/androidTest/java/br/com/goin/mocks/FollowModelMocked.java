package br.com.goin.mocks;

public class FollowModelMocked {
    public static String emptyUserFollowers = "{\n" +
                                              "   \"data\":{\n" +
                                              "       \"getUserFollowers\":[]\n" +
                                              "   }\n" +
                                              "}";

    public static String followerUserName = "Teste 2";

    public static String oneUserFollower = "{\n" +
                                           "   \"data\":{\n" +
                                           "       \"getUserFollowers\":[\n" +
                                           "           {\n" +
                                           "               \"id\":\"us-east-1:f80ee0eb-2928-44cc-a35c-4d62c0288a58\",\n" +
                                           "               \"followerName\":\"" + followerUserName + "\",\n" +
                                           "               \"followerAvatar\":\"https://s3.amazonaws.com/goin-public-dev/Users/us-east-1:f80ee0eb-2928-44cc-a35c-4d62c0288a58\",\n" +
                                           "               \"followedByMe\":false\n" +
                                           "           }\n" +
                                           "         ]\n" +
                                           "   }\n" +
                                           "}";

    public static String userFollowers = "{\n" +
                                         "   \"data\":{\n" +
                                         "       \"getUserFollowers\":[\n" +
                                         "           {\n" +
                                         "               \"id\": \"us-east-1:54546e0e-7cfa-4071-80d4-416376cbe394\",\n" +
                                         "               \"followerName\":\"Marco Dev. Android \",\n" +
                                         "               \"followerAvatar\":\"https://s3.amazonaws.com/goin-public-dev/Users/us-east-1:54546e0e-7cfa-4071-80d4-416376cbe394\",\n" +
                                         "               \"followedByMe\":true\n" +
                                         "           },\n" +
                                         "           {\n" +
                                         "               \"id\":\"us-east-1:f80ee0eb-2928-44cc-a35c-4d62c0288a58\",\n" +
                                         "               \"followerName\":\"Teste 2\",\n" +
                                         "               \"followerAvatar\":\"https://s3.amazonaws.com/goin-public-dev/Users/us-east-1:f80ee0eb-2928-44cc-a35c-4d62c0288a58\",\n" +
                                         "               \"followedByMe\":false\n" +
                                         "           }\n" +
                                         "         ]\n" +
                                         "   }\n" +
                                         "}";

    public static String userFollowing = "{\n" +
                                         "   \"data\":{\n" +
                                         "       \"getUserFollowing\":[\n" +
                                         "            {\n" +
                                         "               \"id\":\"us-east-1:54546e0e-7cfa-4071-80d4-416376cbe394\",\n" +
                                         "               \"userName\":\"Marco Dev. Android \",\n" +
                                         "               \"userAvatar\":\"https://s3.amazonaws.com/goin-public-dev/Users/us-east-1:54546e0e-7cfa-4071-80d4-416376cbe394\",\n" +
                                         "               \"followedByMe\":true\n" +
                                         "            },\n" +
                                         "           {" +
                                         "               \"id\":\"us-east-1:61434e03-854a-4bed-b83b-50040a06e98b\",\n" +
                                         "               \"userName\":\"Joao Teste\",\n" +
                                         "               \"userAvatar\":\"https://s3.amazonaws.com/goin-public-dev/Users/us-east-1:61434e03-854a-4bed-b83b-50040a06e98b\",\n" +
                                         "               \"followedByMe\":false\n" +
                                         "           },\n" +
                                         "           {\n" +
                                         "               \"id\":\"us-east-1:f80ee0eb-2928-44cc-a35c-4d62c0288a58\",\n" +
                                         "               \"userName\":\"Teste 2\",\n" +
                                         "               \"userAvatar\":\"https://s3.amazonaws.com/goin-public-dev/Users/us-east-1:f80ee0eb-2928-44cc-a35c-4d62c0288a58\",\n" +
                                         "               \"followedByMe\":false\n" +
                                         "           }\n" +
                                         "       ]\n" +
                                         "   }\n" +
                                         "}";

    public static String followingUserName = "Teste";

    public static String oneUserFollowing = "{\n" +
                                        "   \"data\":{\n" +
                                        "       \"getUserFollowing\":[\n" +
                                        "           {\n" +
                                        "               \"id\":\"us-east-1:f80ee0eb-2928-44cc-a35c-4d62c0288a58\",\n" +
                                        "               \"userName\":\"" + followingUserName + "\",\n" +
                                        "               \"userAvatar\":\"https://s3.amazonaws.com/goin-public-dev/Users/us-east-1:f80ee0eb-2928-44cc-a35c-4d62c0288a58\",\n" +
                                        "               \"followedByMe\":true\n" +
                                        "           }\n" +
                                        "       ]\n" +
                                        "   }\n" +
                                        "}";

    public static String emptyUserFollowing = "{\n" +
                                              "   \"data\":{\n" +
                                              "       \"getUserFollowing\":[]\n" +
                                              "   }\n" +
                                              "}";

    public static String successfulFollow = "{\n" +
                                            "    \"data\":{\n" +
                                            "        \"followUserListener\":\"User successfully followed\"\n" +
                                            "    }\n" +
                                            "}";

    public static String successfulUnfollow = "{\n" +
                                              "  \"data\":{\n" +
                                              "      \"unfollowUserListener\":{\n" +
                                              "          \"currentUserId\":null,\n" +
                                              "          \"followerId\":null\n" +
                                              "      }\n" +
                                              "  }\n" +
                                              "}";
}
