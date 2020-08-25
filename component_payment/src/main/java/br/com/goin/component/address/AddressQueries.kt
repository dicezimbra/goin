package br.com.goin.component.address

object AddressQueries {

    var SEND_ADDRESS = "mutation userAddress(\$address: UserAddressInput!) {\n" +
            "  updateAddress(address: \$address)\n" +
            "}"


    var GET_USER_ADRESSES = "query GetUserAdresses(\$userId: String!) {\n" +
            "  userAddress(userId: \$userId) {\n" +
            "    alias\n" +
            "    street\n" +
            "    number\n" +
            "    complement\n" +
            "    district\n" +
            "    zipcode\n" +
            "    city\n" +
            "    state\n" +
            "  }\n" +
            "}"
}