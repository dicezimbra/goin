package br.com.goin.features

enum class TicketPaymentType(val type: String) {

    GOIN("GOIN"),
    INGRESSE("INGRESSE"),
    ALOINGRESSOS("ALOINGRESSOS"),
    INGRESSORAPIDO("INGRESSORAPIDO"),
    SITE("SITE"),
    PAGAMENTO("PAGAMENTO"),
    PAGAMENTO_AUTENTICADO("PAGAMENTO_AUTENTICADO"),
    CORPORATIVO("corporation")
}