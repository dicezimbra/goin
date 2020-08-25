package br.com.goin.component.rest.api.model

data class RefreshTokenRequest(val email: String,
                               val refreshToken: String)