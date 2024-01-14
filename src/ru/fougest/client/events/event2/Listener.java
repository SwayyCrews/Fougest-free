package ru.fougest.client.events.event2;

import ru.fougest.client.events.Event;

public interface Listener<T extends Event> {
    void onEvent(T event);
}