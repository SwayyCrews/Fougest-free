package ru.fougest.client.events.impl.player;

import ru.fougest.client.events.Event;

public class EventStrafe extends Event {

    public float yaw;

    public EventStrafe(float yaw) {
        this.yaw = yaw;
    }

}
