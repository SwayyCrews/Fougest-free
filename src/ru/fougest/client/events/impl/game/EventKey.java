package ru.fougest.client.events.impl.game;

import ru.fougest.client.events.Event;

public class EventKey extends Event {

    public int key;

    public EventKey(int key) {
        this.key = key;
    }
}
