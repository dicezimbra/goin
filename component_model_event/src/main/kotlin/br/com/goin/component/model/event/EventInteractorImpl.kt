package br.com.goin.component.model.event

import br.com.goin.component.model.event.api.EventMapper
import br.com.goin.component.model.event.api.Interrest
import br.com.goin.component.model.event.api.response.StateResponse
import br.com.goin.component.model.event.api.response.ValidateCouponModel
import io.reactivex.Completable
import io.reactivex.Observable
import okhttp3.ResponseBody

private const val EVENT_TYPE = "EVENT"

class EventInteractorImpl : EventInteractor {

    override fun getSate(lat: Double, lng: Double): Observable<StateResponse> {
        return repository.getSate(lat, lng, 100)
    }

    private val mapper = EventMapper()
    private val repository = EventRepository()

    override fun validateDiscountcode(eventId: String, coupon: String): Observable<ValidateCouponModel> {
        return repository.validateDiscountcode(eventId, coupon)
    }

    override fun claimVoucher(ticketId: String?, clubId: String, userName: String): Completable {
        return repository.claimVoucher(ticketId, clubId, userName)
    }

    override fun confirmTokenValid(originType: String): Observable<Boolean> {
        return repository.confirmTokenValid(originType).map { it.data?.confirmTokenIsValid?.isValid }
    }

    override fun getBanners(): Observable<List<Banner>> {
        return repository.getBanners().map {
            it.data?.banners
        }
    }

    override fun getDetail(userId: String?): Observable<List<Event>> {
        return repository.getMyEvents(userId).map { mapper.mapToModel(it.data?.myEvents) }
    }

    override fun getDetail(eventId: String?, clubId: String?): Observable<Event> {
        return repository.getDetail(eventId).filter { it.data?.getEvent != null }.map { mapper.map(it.data?.getEvent!!) }
    }

    override fun confirmStefaniniTicket(eventId: String): Completable {
        return repository.confirmStefaniniTicket(eventId)
    }

    override fun confirmEventPresence(eventId: String): Observable<*> {
        return repository.confirmEventPresence(eventId)
    }

    override fun unconfirmEventPresence(eventId: String): Observable<*> {
        return repository.unconfirmEventPresence(eventId)
    }

    override fun getEventsToCheckIn(myLocation: FloatArray): Observable<List<Event>> {
        return repository.getAvailableEventsToCheckIn(myLocation).map { mapper.mapToModel(it.data?.checkInAvailable) }
    }

    override fun getEvents(city: String, state: String, type: String): Observable<List<EventHome>> {
        return repository.getEvents(city, state, type).map { mapper.mapToModelHome(it) }
   }


    override fun confirmNameOnList(eventId: String?, userName: String?): Completable {
        return repository.confirmAttendanceOnEvent(eventId, userName)
    }

    override fun removeInterest(id: String): Completable {
        return repository.removeInterest(id)
    }

    override fun putInterest(id: String,
                             name: String,
                             image: String): Completable {
        return repository.putInterest(id, EVENT_TYPE, name, image)
    }

    override fun getInterests(userId: String): Observable<List<Interrest>> {
        return repository.getInterests(userId)
    }
}