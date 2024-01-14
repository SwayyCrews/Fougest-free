package ru.fougest.client.events.impl.player;

import ru.fougest.client.events.Event;

public class EventTravel extends Event {

    public float speed;

    public EventTravel(float speed) {
        this.speed = speed;
    }

}
