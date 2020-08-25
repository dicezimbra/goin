package br.com.goin.component.session.user.location

import io.reactivex.Observable

class UserLocationInteractorImpl: UserLocationInteractor {
    override fun fetchPair(): Pair<Double, Double>? {
       return Pair(repository.fetch().lat, repository.fetch().lng)
    }

    private val repository = UserLocationRepository()

    override fun hasLocation(): Boolean {
        return repository.hasLocation()
    }

    override fun fetch(): UserLocation {
        return repository.fetch()
    }

    override fun save(userLocation: UserLocation){
        Thread { repository.save(userLocation) }.start()
    }


}