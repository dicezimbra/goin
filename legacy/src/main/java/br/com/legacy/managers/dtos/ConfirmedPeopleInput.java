package br.com.legacy.managers.dtos;

import br.com.goin.component.model.event.Event;

public class ConfirmedPeopleInput {

    String eventId;
    Event.EventConfirmationType type;
    String lastUserId;
    int count;

    public ConfirmedPeopleInput(String eventId, Event.EventConfirmationType type) {
        this.eventId = eventId;
        this.type = type;
    }
}
