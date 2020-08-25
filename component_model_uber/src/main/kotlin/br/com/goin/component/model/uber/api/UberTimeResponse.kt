package br.com.goin.component.model.uber.api

class UberTimeResponse<T> {
    var status: String? = null
    var times: Collection<T>? = null
}
