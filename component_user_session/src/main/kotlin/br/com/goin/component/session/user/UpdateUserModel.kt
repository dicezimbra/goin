package br.com.goin.component.session.user

import com.google.gson.annotations.SerializedName
import java.io.Serializable

open class UpdateUserModel(@SerializedName("email") var email: String = "",
                         @SerializedName("name")var name: String = "",
                         @SerializedName("profilePicture")var  profilePicture: String = "",
                         @SerializedName("accountProvider")var accountProvider: String? = null,
                           @SerializedName("pushEndpoint")var pushEndpoint: String? = null,
                         @SerializedName("categories")var categories: List<String>? = null,
                         @SerializedName("cpf")var cpf: String? = null): Serializable {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as UserModel

        if (email != other.email) return false
        if (name != other.name) return false
        if (profilePicture != other.profilePicture) return false
        if (accountProvider != other.accountProvider) return false
        if (categories != other.categories) return false

        return true
    }

    override fun hashCode(): Int {
        var result = email.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + profilePicture.hashCode()
        result = 31 * result + (accountProvider?.hashCode() ?: 0)
        result = 31 * result + (categories?.hashCode() ?: 0)
        return result
    }
}
