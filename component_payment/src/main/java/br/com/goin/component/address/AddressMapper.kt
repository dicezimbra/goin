package br.com.goin.component.address

class AddressMapper {


    fun map(data: List<UserAddress>?): List<AddressModel> {
        var list = ArrayList<AddressModel>()

        data?.let {
            for (userInputAddress: UserAddress in it) {
                list.add(map(userInputAddress))
            }
        }

        return list
    }

    private fun map(data: UserAddress): AddressModel {
        var addressModel = AddressModel()
        addressModel.cep = data.zipcode
        addressModel.label = data.alias
        addressModel.logradouro = data.street
        addressModel.number = data.number
        addressModel.complemento = data.complement
        addressModel.bairro = data.district
        addressModel.localidade = data.city
        addressModel.uf = data.state

        return addressModel
    }

}