package ru.fougest.client.events.impl.player;

import ru.fougest.client.events.Event;

public class EventStep extends Event {

    public float stepHeight;

    public EventStep(float stepHeight) {
        this.stepHeight = stepHeight;
    }

}
