package br.com.goin.component.address

import io.reactivex.Observable


interface AddressInteractor {

    companion object {
        val instance: AddressInteractor = AddressInteractorImpl()
    }

    fun getUserAdresses(it: String): Observable<List<AddressModel>>
    fun getCepAddress(cep: String): Observable<AddressModel>
    fun addAddress(addressModel: UserAddress) : Observable<String>
}