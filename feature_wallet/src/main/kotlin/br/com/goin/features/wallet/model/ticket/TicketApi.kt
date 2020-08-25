package br.com.goin.features.wallet.model.ticket

import br.com.goin.base.dtos.GraphqlQuery
import br.com.goin.base.dtos.OutsmartResponse
import br.com.goin.base.dtos.TicketDetails
import br.com.goin.component.rest.api.graphQL.GraphQLResponse
import br.com.goin.features.wallet.model.EventTicket
import br.com.goin.features.wallet.model.Ticket
import br.com.goin.features.wallet.model.WalletResponse
import br.com.goin.model.ticket.TicketDeleteResponse
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.POST
import java.util.HashMap

interface TicketApi {

    @POST("graphql")
    fun fetchWallet(@Body body: GraphqlQuery): Observable<GraphQLResponse<WalletResponse>>

    @POST("graphql")
    fun getTickets(@Body body: GraphqlQuery): Observable<GraphQLResponse<HashMap<String, List<TicketDetails>>>>

    @POST("graphql")
    fun validate(@Body body: GraphqlQuery): Completable

    @POST("graphql")
    fun deleteTicket(@Body body: GraphqlQuery): Single<GraphQLResponse<TicketDeleteResponse>>

    @POST("graphql")
    fun fetchEventTickets(@Body body: GraphqlQuery?):  Observable<GraphQLResponse<TicketResponse>>

}