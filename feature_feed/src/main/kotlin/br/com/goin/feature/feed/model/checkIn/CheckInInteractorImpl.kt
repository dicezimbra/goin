package br.com.goin.feature.feed.model.checkIn

import io.reactivex.Observable

class CheckInInteractorImpl : CheckInInteractor {

    private val repository = CheckInRepository()
    private val mapper = CheckInMapper()

    override fun getEventsToCheckIn(myLocation: FloatArray): Observable<List<CheckIn>> {
        return repository.getAvailableEventsToCheckIn(myLocation).map { mapper.mapToModel(it.data?.checkInAvailable) }
    }

}