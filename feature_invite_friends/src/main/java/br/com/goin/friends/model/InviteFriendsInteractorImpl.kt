package br.com.goin.friends.model

import io.reactivex.Observable

class InviteFriendsInteractorImpl : InviteFriendsInteractor {

    private val repository = InviteFriendsRepository()

    override fun getFriendsToInvite(eventId: String): Observable<List<FriendToInvite>> {
        return repository.getFriendsToInvite(eventId)
    }

    override fun inviteFriend(eventId: String, invitedId: String): Observable<InviteToEventResponse> {
        return repository.inviteFriend(eventId, invitedId).map { it.data }
    }
}