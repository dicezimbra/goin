package br.com.goin.model.ticket

object TicketQueries {

    var FETCH_WALLET = "query FetchWallet {\n" +
            "  wallet {\n" +
            "       ticketId\n" +
            "       type\n" +
            "       title\n" +
            "       subtitle\n" +
            "       dateText\n" +
            "       receiptText\n" +
            "       image\n" +
            "       regulation \n" +
            "           validate \n" +
            "           detailTitle \n" +
            "           placeName \n" +
            "       detail {\n" +
            "           action \n" +
            "           actionValue \n" +
            "       }\n" +
            "  }\n" +
            "}"

    var GET_MY_INDIVIDUAL_TICKETS = "query GetMyTickets(\$lastId: ID, \$count: Int,\$ticketType: String) {\n" +
            "  getMyTickets(lastId: \$lastId, count: \$count,ticketType : \$ticketType) {\n" +
            "       id\n" +
            "       name\n" +
            "       price\n" +
            "       eventName\n" +
            "       eventDate\n" +
            "       qrCode\n" +
            "       clubName\n" +
            "       buyerName\n" +
            "       numTickets\n" +
            "       paymentStatus\n" +
            "       paymentDate\n" +
            "       buyerName\n" +
            "       ticketType\n" +
            "       capacity\n" +
            "       qrCode\n" +
            "       originType\n" +
            "       isValid \n" +
            "       paymentId \n" +
            "       event{ id\n" +
            "       latitude\n" +
            "       longitude\n" +
            "       image \n" +
            "       startDate\n" +
            "       endDate}\n" +
            "  }\n" +
            "}"

    var DELETE_TICKET = "mutation deleteMyTicket(\$paymentId: String!) {" +
            "deleteMyTicket(paymentId : \$paymentId)}"

    var CUT_TICKET = "mutation CutTicket(\$ticketId: String) {\n" +
            "  cutTicket(ticketId: \$ticketId) {\n" +
            "    detailTitle\n" +
            "   }\n" +
            "}"


    var GET_EVENT_TICKETS = "query GetEventTickets(\$eventId: String!) {\n" +
            "   getEventTickets(eventId: \$eventId) {\n" +
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
            "}"

}