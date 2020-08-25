package br.com.goin.feature.event

import android.location.Location
import br.com.goin.component.model.event.api.response.ApiVoucher
import java.io.Serializable

interface EventPresenter {
    fun onCreate()
    fun onReceivedClubId(serializableExtra: Serializable?)
    fun onReceivedEventId(serializableExtra: Serializable?)
    fun onDestroy()
    fun onShareClicked()
    fun onLikeClicked()
    fun onMapClicked()
    fun onClickBuyTicket()
    fun onUberClicked()
    fun configureBuyButton()
    fun onConfirmUnLike()
    fun onReceiveLocation(it: Location)
    fun loadUberInformation(latitude: Double, longitude: Double)
    fun logOnAnalytics(action: String)
    fun claimTicket(voucher: ApiVoucher)
    fun onAttendanceConfirm(eventId: String?, userName: String?)
}