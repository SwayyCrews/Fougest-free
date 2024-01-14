package ru.fougest.client.modules.impl.player;

import ru.fougest.client.events.Event;
import ru.fougest.client.events.impl.player.EventMotion;
import ru.fougest.client.modules.Function;
import ru.fougest.client.modules.FunctionAnnotation;
import ru.fougest.client.modules.Type;

@FunctionAnnotation(name = "NoFall", type = Type.Player)
public class NoFall extends Function {


    @Override
    public void onEvent(Event event) {
        if (event instanceof EventMotion e) {
            if (mc.player.ticksExisted % 3 == 0 && mc.player.fallDistance > 3) {
                e.setY(e.getY() + 0.2f);
            }
        }
    }
}
