package br.com.goin.feature.theaters.plays.movies.model

import br.com.goin.feature.theaters.plays.movies.model.PlayModel
import br.com.goin.feature.theaters.plays.movies.model.PlaysListDTO
import br.com.goin.feature.theaters.plays.movies.model.PlaysListModel
import java.util.ArrayList

class MovieMapper {

    fun mapDtoToModel(playsList: PlaysListDTO?): PlaysListModel {
        val model = PlaysListModel()
        playsList?.getAllPlays?.let {
            model.highlighted = (mapPlayToModel(playsList.getAllPlays?.highlighted))
            model.inTheaters = (mapPlayToModel(playsList.getAllPlays?.inTheathers))
            model.comingSoon = (mapPlayToModel(playsList.getAllPlays?.comingSoon))
        }

        playsList?.getPlaysMovie?.let {
            model.highlighted = (mapPlayToModel(playsList.getPlaysMovie?.highlighted))
            model.inTheaters = (mapPlayToModel(playsList.getPlaysMovie?.inTheathers))
            model.comingSoon = (mapPlayToModel(playsList.getPlaysMovie?.comingSoon))
        }


        return model
    }

    private fun mapPlayToModel(playsList: List<PlaysListDTO.PlaysOutputSimpleView>?): List<PlayModel> {
        val newList = ArrayList<PlayModel>()

        if (playsList != null) {
            for (model in playsList) {
                val newModel = PlayModel()
                newModel.id = model.id
                newModel.name = model.name
                newModel.image = model.image
                newModel.club = model.club
                newModel.city = model.city
                newList.add(newModel)
            }
        }

        return newList
    }
}