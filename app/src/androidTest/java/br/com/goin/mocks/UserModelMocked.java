package br.com.goin.mocks;

public class UserModelMocked {
    public static String mockedUser =
            "{\n"+
            "   \"data\": {\n"+
            "        \"myUser\": {\n"+
            "            \"id\": \"us-east-1:d0388fcc-a4c0-4858-8b0e-66289bd60613\",\n"+
            "            \"name\": \"Teste\",\n"+
            "            \"email\": \"j@j.com\",\n"+
            "            \"cpf\": null,\n"+
            "            \"accountProviders\": [\n"+
            "               \"Cognito\"\n"+
            "             ],\n"+
            "            \"profilePicture\": \"https://s3.amazonaws.com/jumpers-storage/Users/us-east-1:d0388fcc-a4c0-4858-8b0e-66289bd60613\",\n"+
            "            \"followingCount\": 0,\n"+
            "            \"followersCount\": 0,\n"+
            "            \"eventsCount\": 0,\n"+
            "            \"clubsCount\": 0,\n"+
            "            \"categoriesCount\": 0,\n"+
            "            \"userScore\": null,\n"+
            "            \"ticketCount\": 0,\n"+
            "            \"pushEndpoint\": null,\n"+
            "            \"bio\": null,\n"+
            "            \"postsCount\": 0\n"+
            "        }\n"+
            "    }\n"+
            "}";

    public static String mockedUpdateUser = "{\n"+"" +
            "                                   \"data\":{\n"+
            "                                       \"updateUser\":{\n"+
            "                                           \"id\": \"us-east-1:d0388fcc-a4c0-4858-8b0e-66289bd60613\"\n"+
            "                                       }"+
            "                                   }"+
            "                                }";

    public static String mockedVerifyEmail = "{\n" +
            "                               \"data\":{\n"+
            "                                   \"verifyUserEmailExists\": [\n"+
            "                                       \"Cognito\"\n"+
            "                                   ]\n"+
            "                               }"+
            "}";

    public static String mockedVerifyEmailUserDoesNotExists = "{\n" +
            "                               \"data\":{\n"+
            "                                   \"verifyUserEmailExists\": null"+
            "                               }"+
            "}";

    public static String mockedSendUserInfo = "{\n" +
            "                               \"data\":{\n"+
            "                                   \"registerUser\": {" +
            "                                       \"id\": \"us-east-1:d0388fcc-a4c0-4858-8b0e-66289bd60613\"," +
            "                                       \"email\": \"teste@teste.com\"," +
            "                                       \"profilePicture\": \"https://picsum.photos/200\"," +
            "                                       \"eventsCount\": 0," +
            "                                       \"clubsCount\": 0," +
            "                                       \"followingCount\": 0," +
            "                                       \"followersCount\": 0," +
            "                                   }" +
            "                                }" +
            "}";

    public static String mockedTempUser = "{\n" +
            "                                   \"data\":{\n"+
            "                                       \"createUserTemp\" {\n" +
            "                                           \"email\": \"teste@teste.com\"," +
            "                                           \"password\": \"123456\"," +
            "                                   }" +
            "}";

    public static String validUserName = "Teste";
    public static String validUserEmail = "j@j.com";
    public static String validUserPassword = "123456";
    public static String invalidUserEmail = "teste@teste";
    public static String invalidUserPassword = "6543";
    public static String validUserId = "us-east-1:d0388fcc-a4c0-4858-8b0e-66289bd60613";
}
