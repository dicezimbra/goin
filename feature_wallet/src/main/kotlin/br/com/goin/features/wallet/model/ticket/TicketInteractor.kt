package br.com.goin.model.ticket

import br.com.goin.base.dtos.OutsmartResponse
import br.com.goin.component.rest.api.graphQL.GraphQLResponse
import br.com.goin.features.wallet.model.EventTicket
import br.com.goin.features.wallet.model.Ticket
import br.com.goin.features.wallet.model.TicketModel
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single

interface TicketInteractor {

    companion object {
        val instance: TicketInteractor = TicketInteractorImpl()
    }

    fun getMyIndividualTickets(ticketType: String): Observable<List<TicketModel>>
    fun deleteTicket(paymentId: String): Single<GraphQLResponse<TicketDeleteResponse>>
    fun fetchWallet(userId: String): Observable<List<Ticket>>
    fun validate(ticketId: String): Completable
    fun fetchEventTickets(eventId: String): Observable<List<EventTicket>>
}
