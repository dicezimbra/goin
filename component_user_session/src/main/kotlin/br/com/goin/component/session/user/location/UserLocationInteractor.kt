package br.com.goin.component.session.user.location



interface UserLocationInteractor{

    companion object {
        val instance: UserLocationInteractor = UserLocationInteractorImpl()
    }

    fun save(userLocation: UserLocation)
    fun fetch(): UserLocation
    fun fetchPair(): Pair<Double, Double>?
    fun hasLocation(): Boolean
}