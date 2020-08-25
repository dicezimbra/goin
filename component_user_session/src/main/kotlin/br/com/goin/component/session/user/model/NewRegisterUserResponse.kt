package br.com.goin.component.session.user.model

data class NewRegisterUserResponse(val token: String,
                                   val id:String,
                                   val email: String,
                                   val name: String,
                                   val accountProviders: List<String> = arrayListOf())
