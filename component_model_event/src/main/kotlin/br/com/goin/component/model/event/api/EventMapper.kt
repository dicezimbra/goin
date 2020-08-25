package br.com.goin.component.model.event.api


import br.com.goin.component.model.event.Event
import br.com.goin.component.model.event.EventHome
import br.com.goin.component.model.event.api.response.ApiEvent
import java.util.*

class EventMapper {

    fun mapToModel(events: List<ApiEvent>?): List<Event> {
        val newList = ArrayList<Event>()
        if (events != null) {
            for (e in events) {
                val model = map(e)
                newList.add(model)
            }
        }
        return newList
    }

    fun mapToModelHome(events: List<EventHomeApi>?): List<EventHome>{
        val newList = ArrayList<EventHome>()
        if (events != null) {
            for (e in events) {
                val model = mapHome(e)
                newList.add(model)
            }
        }
        return newList
    }

    fun mapHome(e: EventHomeApi): EventHome {
        val model = EventHome()
        model.image = e.image
        model.title = e.title
        model.subtitle = e.subtitle

        if (e.date != null) {
            val calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"))
            calendar.timeZone = TimeZone.getTimeZone("GMT")
            calendar.timeInMillis = e.date!!

            val calendarToday = Calendar.getInstance(TimeZone.getTimeZone("GMT"))
            calendarToday.timeZone = TimeZone.getTimeZone("GMT")
            calendarToday.timeInMillis = System.currentTimeMillis()

            if (calendar.before(calendarToday)) {
                model.date = calendarToday
            } else {
                model.date = calendar
            }
        }

        if (e.endDate != null) {
            val calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"))
            calendar.timeZone = TimeZone.getTimeZone("GMT")
            calendar.timeInMillis = e.endDate!!
            model.endDate = calendar
        }

        model.actionValue = e.actionValue!!
        model.lat = e.lat
        model.long = e.long
        model.imageWidth = e.imageWidth
        model.imageHeight = e.imageHeight
        model.originType = e.originType
        model.partnersInfo?.mainColor = e.partnerInfo?.mainColor
        model.partnersInfo?.textColor = e.partnerInfo?.textColor
        model.partnersInfo?.logo = e.partnerInfo?.logo
        model.partnersInfo?.buttonColor = e.partnerInfo?.buttonColor
        model.partnersInfo?.bigIcon = e.partnerInfo?.bigIcon
        model.partnersInfo?.smallIconColored = e.partnerInfo?.smallIconColored
        model.partnersInfo?.smallIconWhite = e.partnerInfo?.smallIconWhite

        return model
    }

    fun map(e: ApiEvent): Event {
        val model = Event()
        model.vouchers = e.giftsGallery
        model.id = e.id
        model.clubId = e.clubId
        model.name = e.name
        model.description = if (e.description == null) "" else e.description!!
        model.categoryName = if (e.categoryName == null) "" else e.categoryName
        model.city = if (e.city == null) "" else e.city
        model.club = e.club
        model.placeName = if (e.placeName == null) "" else e.placeName!!
        model.placeAddress = if (e.placeAddress == null) "" else e.placeAddress!!
        model.lowestPrice = if (e.lowestPrice == null) null else e.lowestPrice!! / 100
        model.highestPrice = if (e.highestPrice == null) null else e.highestPrice!! / 100
        model.originType = if (e.originType == null) "" else e.originType!!
        model.originURL = if (e.originURL == null) "" else e.originURL!!
        model.originAction = if (e.originAction == null) "SITE" else e.originAction!!
        model.originId = if (e.originId == null) "" else e.originId!!
        model.categoryEventType = e.categoryEventType

        try {
            model.categoryType = Event.CategoryType.valueOf(e.categoryType!!)
        } catch (e: Exception) {
        }

        model.imageWidth = e.imageWidth
        model.imageHeight = e.imageHeight
        model.imageAspect = e.imageAspect
        model.originHasDiscount = e.originHasDiscount

        e.originInfos?.let {
            model.buttonColor = e.originInfos?.buttonColor
            model.bigIcon = e.originInfos?.bigIcon
            model.smallIconColored = e.originInfos?.smallIconColored
            model.smallIconWhite = e.originInfos?.smallIconWhite
        }

        if (e.startDate != null) {

            val calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"))
            calendar.timeZone = TimeZone.getTimeZone("GMT")
            calendar.timeInMillis = e.startDate!!

            val calendarToday = Calendar.getInstance(TimeZone.getTimeZone("GMT"))
            calendarToday.timeZone = TimeZone.getTimeZone("GMT")
            calendarToday.timeInMillis = System.currentTimeMillis()
            model.startDate = calendar
        }

        model.lat = e.latitude?.toDouble()
        model.lng = e.longitude?.toDouble()

        if (e.endDate != null) {
            val calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"))
            calendar.timeZone = TimeZone.getTimeZone("GMT")
            calendar.timeInMillis = e.endDate!!
            model.endDate = calendar
        }
        model.intentionCount = if (e.interestedCount == null) 0 else e.interestedCount
        model.checkInCount = if (e.checkInCount == null) 0 else e.checkInCount
        model.isConfirmed = e.isConfirmed
        model.confirmType = if (e.confirmType == null) Event.EventConfirmationType.Interested else e.confirmType
        model.photoUrl = e.image



        return model
    }



  /*  @SuppressLint("MissingPermission")
    fun getMyLocation(): Location? {
        val locationManager = BaseApplication.instance.context?.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val criteria = Criteria()
        criteria.powerRequirement = Criteria.ACCURACY_COARSE
        val provider = locationManager.getBestProvider(criteria, false)
        var myLocation: Location?

        if (provider != null) {
            try {
                myLocation = locationManager.getLastKnownLocation(provider)
                return myLocation
            } catch (error: SecurityException) {
                Log.e("EventManager", error.message, error)
            }
        }
        return null
    }*/
}