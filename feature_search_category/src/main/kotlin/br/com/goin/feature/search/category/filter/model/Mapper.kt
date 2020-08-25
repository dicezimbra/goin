package br.com.goin.feature.search.category.filter.model

class Mapper {

    fun chipTagsMap(chipTagsList: List<ChipTag>): ChipTagResponse {

        return ChipTagResponse(chipTagsList)
    }
}