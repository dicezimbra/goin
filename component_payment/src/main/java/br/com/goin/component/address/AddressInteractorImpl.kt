package br.com.goin.component.address

import io.reactivex.Observable

class AddressInteractorImpl :AddressInteractor{

    private val repository = AddressRepository()

    override fun getUserAdresses(userId: String): Observable<List<AddressModel>> {
       return repository.getUserAdresses(userId)
    }

    override fun getCepAddress(cep: String): Observable<AddressModel> {
        return repository.getCepAddress(cep)
    }

    override fun addAddress(addressModel: UserAddress): Observable<String> {
        return repository.addAddress(addressModel).map { it.data?.address }
    }

}