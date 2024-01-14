package ru.fougest.client.events.impl.player;

import ru.fougest.client.events.Event;

public class EventRotation extends Event {

    public float yaw,pitch;

    public EventRotation(float yaw, float pitch) {
        this.yaw = yaw;
        this.pitch = pitch;
    }

}
