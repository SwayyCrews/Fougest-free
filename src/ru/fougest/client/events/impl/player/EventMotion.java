package ru.fougest.client.events.impl.player;

import lombok.AllArgsConstructor;
import lombok.Data;

import lombok.EqualsAndHashCode;
import ru.fougest.client.events.Event;

@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
public class EventMotion extends Event {
    private double x, y, z;
    private float yaw, pitch;
    private boolean onGround;
}
