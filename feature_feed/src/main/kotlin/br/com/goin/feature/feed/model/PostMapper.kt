package br.com.goin.feature.feed.model

import br.com.goin.feature.feed.post.CreatePost

class PostMapper {

    fun mapToDTO(model: CreatePost): Post {
        val dto = Post()

        dto.description = model.description
        dto.checkInEventId = model.checkInEventId
        dto.feeling = model.feeling
        dto.image = if (model.image == true) "true" else null
        dto.video = if (model.video == true) "true" else null
        return dto
    }

}

