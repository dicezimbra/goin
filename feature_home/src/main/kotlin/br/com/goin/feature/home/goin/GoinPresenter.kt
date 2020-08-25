package br.com.goin.feature.home.goin

import br.com.goin.component.session.user.location.UserLocation

interface GoinPresenter {
    fun onCreate()
    fun onDestroy()
    fun refresh()
    fun onResume()
    fun reloadData(userLocation: UserLocation)
}