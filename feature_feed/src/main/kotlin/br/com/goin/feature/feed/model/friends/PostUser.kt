package br.com.goin.feature.feed.model.friends

import java.io.Serializable

data class PostUser(
        val id: String,
        val avatar: String,
        val name: String,
        val chatId: String) : Serializable
