package br.com.goin.feature.event

import android.content.Context
import br.com.goin.component.navigation.EventNavigationController

class EventNavigationControllerImpl: EventNavigationController {

    override fun goToEvent(context: Context, eventId: String?, clubId: String?){
        eventId?.let {
            EventActivity.starter(eventId, clubId, context)
        }
    }
}