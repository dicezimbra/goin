package br.com.goin.component.terms

interface TermsPresenter {
    fun fetchTerms(termsType: TermsType)
    fun onDestroy()
    fun onCreate()
}