package br.com.goin.feature.place

import br.com.goin.component.model.event.Event
import br.com.goin.component.model.event.api.response.ApiVoucher
import br.com.goin.component.session.user.UserModel
import br.com.goin.component.session.user.location.UserLocation
import br.com.goin.component_model_club.model.ClubModel

interface PlaceView {
    fun configureToolbar(title: String)
    fun hideLoading()
    fun showLoading()
    fun hideLoadingVoucher()
    fun showLoadingVoucher()
    fun configurePlace(club: ClubModel, categoryName: String?)
    fun configureOnClicks(club: ClubModel)
    fun showDialogUnconfirmPresence(club: ClubModel): Any
    fun configureButtonConfirmFollow(isActive: Boolean?)
    fun configureGallery(galleryItem: List<String>)
    fun configureReadMore(club: ClubModel)
    fun configureEvents(events: List<Event>)
    fun configureVouchers(vouchers: List<ApiVoucher>?)
    fun configureEmptyVouchers()
    fun configureUberPrice(price: Float)
    fun configureUberTime(time: Int)
    fun retriveCurrentPosition()
    fun initializeMap(club: ClubModel)
    fun handleError(throwable: Throwable)
    fun openMaps(it: ClubModel)
    fun claimTicketSuccess(voucher: ApiVoucher)
    fun claimTicketError()
    fun inviteFriend(user: UserModel)
    fun showCheckingDialog(club: ClubModel)
}