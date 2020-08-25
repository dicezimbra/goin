package br.com.goin.features.wallet

import br.com.goin.features.wallet.model.Ticket

interface WalletView {
    fun configureToolbar()
    fun configureView(tickets: Map<String, List<Ticket>>)
    fun showLoading()
    fun hideLoading()
    fun handleError(throwable: Throwable)
}