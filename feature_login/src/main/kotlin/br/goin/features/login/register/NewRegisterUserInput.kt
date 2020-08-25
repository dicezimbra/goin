package br.goin.features.login.register

data class NewRegisterUserInput(val email: String,
                                val password:String,
                                val name: String,
                                val invitedBy: String?,
                                val accountProvider: String = "Cognito")
