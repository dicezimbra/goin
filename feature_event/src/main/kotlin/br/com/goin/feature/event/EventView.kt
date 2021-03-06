package br.com.goin.feature.event

import br.com.goin.component.model.event.Event
import br.com.goin.component.model.event.api.response.ApiVoucher
import br.com.goin.component.session.user.UserModel

interface EventView {
    fun showLoading()
    fun hideLoading(isCorporative: Boolean)
    fun onEventLoaded(event: Event)
    fun configureOnClickListeners()
    fun configureToolbar()
    fun configureMapPosition(lat: Double, lng: Double)
    fun openMaps(event: Event)
    fun shareEvent(event: Event)
    fun showDialogUnconfirmPresence(event: Event)
    fun showCannotRemoveLikeDialog()
    fun configureActionsComponent(event: Event)
    fun openTickets(event: Event)
    fun configureGoInButton()
    fun showConfirmAttendanceButton()
    fun openAloIngressos(event: Event)
    fun requestUberInfos(event: Event)
    fun configureUberTime(time: Int)
    fun configureUberPrice(bestPrice: Float)
    fun loginInIngresse(event: Event)
    fun hideBuyTicketButton()
    fun retriveCurrentPosition()
    fun configureUberButton(event: Event)
    fun hideShareButton()
    fun showButtonProgress()
    fun hideButtonProgress()
    fun verifyGpsEnabled()
    fun showDialogConfirmedPresenceSuccess()
    fun configureButtonConfirmLike(isActive: Boolean)
    fun configureExpandableLayout(event: Event)
    fun showErrorOnConfirmAttendance(error: String)
    fun showDialogNotAuthorized()
    fun openIngresseRapidoSite(event: Event)
    fun showDiscountDialog(event: Event)
    fun configureBuyButtonByColor(event: Event)
    fun openPaymentScreen(event: Event)
    fun hideLikeButton()
    fun logOnAnalytics(action: String, eventName: String)
    fun handleError(throwable: Throwable)
    fun configureEventAttendance(eventType: String?, eventId: String?, user: UserModel?, event: Event?)
    fun onSuccessAttendance()
    fun loadingNameOnList()
    fun loadingNameOnListFinished()
    fun onErrorAttendance(errorMessage: String)
    fun configureVouchers(vouchers: List<ApiVoucher>?)
    fun configureEmptyVouchers()
    fun showLoadingVoucher()
    fun hideLoadingVoucher()
    fun claimTicketSuccess(voucher: ApiVoucher)
    fun claimTicketError()
}