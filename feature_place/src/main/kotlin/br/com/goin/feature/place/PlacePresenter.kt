package br.com.goin.feature.place

import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import br.com.goin.component.model.event.api.response.ApiVoucher

interface PlacePresenter {
    fun onCreate()
    fun onDestroy()
    fun onReceivePlaceId(placeId: String?)
    fun onFollowClicked()
    fun onConfirmUnfollow()
    fun onMapClicked()
    fun onReceiveCategoryName(categoryName: String?)
    fun onReceiveLocation(location: Location)
    fun claimTicket(voucher: ApiVoucher)
    fun onInviteFriendClicked()
    fun onCheckinClicked()
}