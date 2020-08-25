package br.com.goin.component.session.user

data class Token(val token: String,val refreshToken: String, val identityProvider: String)