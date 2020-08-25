package br.com.goin.feature_theater

import br.com.goin.component.model.uber.UberInformation
import br.com.goin.component_model_club.model.ClubModel
import br.com.goin.feature_theater.model.TheaterResponse

interface TheaterDetailView {

    fun showProgress()
    fun hideProgress()
    fun onReceiveTheater(clubModel: ClubModel)
    fun configureOnClickListeners()
    fun configureToolbar()
    fun shareClub(clubModel: ClubModel)
    fun configureSessionComponent(theaterResponse: TheaterResponse)
    fun goToInvite(id: String?)
    fun openMaps(clubModel: ClubModel)
    fun configureMapPosition(clubModel: ClubModel)
    fun configureUber(uberInformation: UberInformation)
    fun configureUberButton(clubModel: ClubModel)
}