package br.com.goin.feature_theater.model

import io.reactivex.Observable

interface TheaterInteractor{

    companion object {

        val instance: TheaterInteractor = TheaterInteractorImpl()
    }

    fun sessionsByClub(clubId: String?): Observable<TheaterResponse>
   // fun getClub(clubId: String?): Observable<Club>
}