package br.com.goin.model.ticket

import br.com.goin.component.rest.api.graphQL.GraphQLResponse
import br.com.goin.features.wallet.model.EventTicket
import br.com.goin.features.wallet.model.Ticket
import br.com.goin.features.wallet.model.TicketModel
import br.com.goin.features.wallet.model.ticket.TicketRepository
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single

class TicketInteractorImpl : TicketInteractor {


    private val mapper = TicketMapper()
    private val repository = TicketRepository()

    override fun getMyIndividualTickets(ticketType: String): Observable<List<TicketModel>> {
        return repository.getMyIndividualTickets(ticketType)
                .map {
                    val list = it["getMyTickets"] ?: listOf()
                    mapper.map(list, TicketModel.CardType.MY_TICKET)
                }
    }

    override fun validate(ticketId: String): Completable {
        return repository.validate(ticketId)
    }

    override fun fetchWallet(userId: String): Observable<List<Ticket>> {
        return repository.fetchWallet(userId)
    }

    override fun deleteTicket(paymentId: String): Single<GraphQLResponse<TicketDeleteResponse>> {
        return repository.deleteTicket(paymentId)
    }

    override fun fetchEventTickets(eventId: String): Observable<List<EventTicket>> {
        return repository.getEventTickets(eventId)
    }
}
