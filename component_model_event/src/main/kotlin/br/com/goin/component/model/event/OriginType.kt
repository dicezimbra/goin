package br.com.goin.component.model.event

enum class OriginType(val type: String) {
    GOIN("GOIN"),
    INGRESSE("INGRESSE"),
    ALOINGRESSOS("ALOINGRESSOS"),
    INGRESSORAPIDO("INGRESSORAPIDO"),
    EVENTBRITE("EVENTBRITE"),
    SITE("SITE"),
    PAGAMENTO("PAGAMENTO"),
    PAGAMENTO_AUTENTICADO("PAGAMENTO_AUTENTICADO"),
    CORPORATIVO("corporation")
}