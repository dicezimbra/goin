package br.com.goin.component.terms

import br.com.goin.component.terms.model.TermsModel

interface TermsView {
    fun setupToolbar()
    fun setupBackButton()
    fun configureExpandableLayout(termsModel: List<TermsModel>)
    fun showLoading()
    fun hideLoading()
    fun displayDialogOnError()
}