package br.com.goin.component.model.event

import br.com.goin.component.model.event.api.Interrest
import br.com.goin.component.model.event.api.response.StateResponse
import br.com.goin.component.model.event.api.response.ValidateCouponModel
import io.reactivex.Completable
import io.reactivex.Observable
import okhttp3.ResponseBody

interface EventInteractor {

    companion object {
        val instance: EventInteractor = EventInteractorImpl()
    }

    fun getDetail(eventId: String?, clubId: String?): Observable<Event>
    fun confirmEventPresence(eventId: String): Observable<*>
    fun unconfirmEventPresence(eventId: String): Observable<*>
    fun getBanners(): Observable<List<Banner>>
    fun confirmTokenValid(originType: String): Observable<Boolean>
    fun confirmStefaniniTicket(eventId: String): Completable
    fun validateDiscountcode(eventId: String, coupon: String): Observable<ValidateCouponModel>
    fun getEventsToCheckIn(myLocation: FloatArray): Observable<List<Event>>
    fun getDetail(userId: String?): Observable<List<Event>>
    fun claimVoucher(ticketId: String?, clubId: String, userName: String): Completable
    fun confirmNameOnList(eventId: String?, userName: String?): Completable
    fun getEvents(city: String, state: String, type: String): Observable<List<EventHome>>
    fun getSate(lat: Double, lng: Double): Observable<StateResponse>
    fun removeInterest(id: String): Completable
    fun putInterest(id: String, name: String, image: String): Completable
    fun getInterests(userId: String): Observable<List<Interrest>>
}