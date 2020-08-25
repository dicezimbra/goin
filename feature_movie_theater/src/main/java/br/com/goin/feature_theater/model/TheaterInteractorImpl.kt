package br.com.goin.feature_theater.model

import br.com.goin.component_model_club.ClubMapper
import io.reactivex.Observable

class TheaterInteractorImpl : TheaterInteractor {

    private val repository = TheaterRepository()
    private val mapper = ClubMapper()

    override fun sessionsByClub(clubId: String?): Observable<TheaterResponse> {
        return repository.sessionsByClub(clubId).map { it.data }
    }

//    override fun getClub(clubId: String?): Observable<Club> {
//        return repository.getClub(clubId).map { mapper.mapToModel(it.data.club) }
//    }
}