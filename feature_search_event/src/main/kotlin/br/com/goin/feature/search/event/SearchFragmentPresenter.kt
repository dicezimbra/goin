package br.com.goin.feature.search.event

import java.io.Serializable

interface SearchFragmentPresenter {

    fun onCreate()
    fun onReceiveSearchResult(serializable: Serializable?)
}