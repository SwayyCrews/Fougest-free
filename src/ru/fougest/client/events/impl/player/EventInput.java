package ru.fougest.client.events.impl.player;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import ru.fougest.client.events.Event;
@Getter
@Setter
@AllArgsConstructor
public class EventInput extends Event {
    private float forward, strafe;
    private boolean jump, sneak;
    private double sneakSlowDownMultiplier;
}
