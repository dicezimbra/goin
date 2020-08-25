package br.com.goin.feature.feed.model.checkIn

class CheckInMapper {

    fun mapToModel(events: List<CheckInDetails>?): List<CheckIn> {
        val newList = ArrayList<CheckIn>()
        if (events != null) {
            for (e in events) {
                val model = map(e)
                newList.add(model)
            }
        }
        return newList
    }

    fun map(e: CheckInDetails): CheckIn {
        val model = CheckIn()
        when(e.type){
            "CLUB" -> {
                model.club = if (e.name == null) "" else e.name
                model.clubId = e.id
            }
            else -> {
                model.name = if (e.name == null) "" else e.name
                model.id = e.id
            }
        }

        return model
    }
}