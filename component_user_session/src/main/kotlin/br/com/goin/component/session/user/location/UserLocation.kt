package br.com.goin.component.session.user.location

data class UserLocation(val lat: Double,
                        val lng: Double,
                        val cityName: String? = "",
                        val city: String? = null,
                        val state: String? = null,
                        val uf: String? = null)