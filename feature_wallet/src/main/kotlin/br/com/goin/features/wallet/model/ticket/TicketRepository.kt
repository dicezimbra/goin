package br.com.goin.features.wallet.model.ticket

import br.com.goin.base.BuildConfig
import br.com.goin.base.dtos.GraphqlQuery
import br.com.goin.base.dtos.OutsmartResponse
import br.com.goin.base.dtos.TicketDetails
import br.com.goin.component.rest.api.RetrofitService
import br.com.goin.component.rest.api.graphQL.GraphQLResponse
import br.com.goin.features.wallet.model.EventTicket
import br.com.goin.features.wallet.model.Ticket
import br.com.goin.model.ticket.TicketDeleteResponse
import br.com.goin.model.ticket.TicketQueries
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single

class TicketRepository {

    private val service = RetrofitService(TicketApi::class.java, BuildConfig.BASE_URL)

    fun getMyIndividualTickets(ticketType: String): Observable<HashMap<String, List<TicketDetails>>> {
        val graphqlQuery = GraphqlQuery.builder(TicketQueries.GET_MY_INDIVIDUAL_TICKETS)
                .`var`("lastId", null)
                .`var`("count", null)
                .`var`("ticketType", ticketType)
                .build()

        return service.apiService.getTickets(graphqlQuery).map { it.data }
    }

    fun validate(ticketId: String): Completable {
        val graphqlQuery = GraphqlQuery.builder(TicketQueries.CUT_TICKET)
                .`var`("ticketId", ticketId)
                .build()

        return service.apiService.validate(graphqlQuery)
    }

    fun fetchWallet(userId: String): Observable<List<Ticket>> {
        val graphqlQuery = GraphqlQuery.builder(TicketQueries.FETCH_WALLET)
                .build()

        return service.apiService.fetchWallet(graphqlQuery).map { it.data?.wallet }
    }

    fun deleteTicket(paymentId: String): Single<GraphQLResponse<TicketDeleteResponse>> {
        val graphqlQuery = GraphqlQuery.builder(TicketQueries.DELETE_TICKET)
                .`var`("paymentId", paymentId)
                .build()

        return service.apiService.deleteTicket(graphqlQuery).map { it }
    }


    fun getEventTickets(eventId: String): Observable<List<EventTicket>> {
        val graphqlQuery = GraphqlQuery.builder(TicketQueries.GET_EVENT_TICKETS)
                .`var`("eventId", eventId)
                .build()

        return service.apiService.fetchEventTickets(graphqlQuery).map { it.data?.eventTickets }
    }
}
