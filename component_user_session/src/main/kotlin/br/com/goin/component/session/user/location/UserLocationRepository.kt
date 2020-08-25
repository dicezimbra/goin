package br.com.goin.component.session.user.location

import br.com.goin.base.BuildConfig
import br.com.goin.base.helpers.PreferenceHelper
import com.google.gson.Gson

private const val USER_LOCATION = "USER_LOCATION"

class UserLocationRepository {


    private val defaultLocation by lazy { UserLocation(lat = -23.5489, lng = -46.6388, cityName = "São Paulo", city = "São Paulo", uf = "SP") }

    fun hasLocation() = PreferenceHelper.read(USER_LOCATION) != null

    fun fetch(): UserLocation {
        val json = PreferenceHelper.read(USER_LOCATION) as? String

        return when (json) {
            null -> { defaultLocation }
            else -> { Gson().fromJson<UserLocation>(json, UserLocation::class.java) }
        }
    }

    fun save(userLocation: UserLocation) {
        PreferenceHelper.write(USER_LOCATION, userLocation)
    }


}