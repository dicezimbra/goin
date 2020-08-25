package br.com.goin.friends.model

object InviteFriendsQueries {

    val GET_FRIENDS_TO_INVITE = "query GetFriendsToInvite(\$eventId: ID!) {\n" +
            "   friendsToInvite (eventId: \$eventId) {\n" +
            "       userId\n" +
            "       name\n" +
            "       avatar\n" +
            "       invitedByMe\n" +
            "   }\n" +
            "}"

    val INVITE_FRIEND = "mutation FriendToInvite(\$eventId: ID!, \$invitedId: ID!) {\n" +
            "   inviteToEvent (eventId: \$eventId, invitedId: \$invitedId)\n" +
            "}\n"
}