package br.com.goin.friends.model

import io.reactivex.Observable

interface InviteFriendsInteractor {

    companion object {

        val instance: InviteFriendsInteractor = InviteFriendsInteractorImpl()
    }

    fun getFriendsToInvite(eventId: String): Observable<List<FriendToInvite>>
    fun inviteFriend(eventId: String, invitedId: String): Observable<InviteToEventResponse>

}