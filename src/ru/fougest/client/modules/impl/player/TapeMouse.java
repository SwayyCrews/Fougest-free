package ru.fougest.client.modules.impl.player;

import ru.fougest.client.events.Event;
import ru.fougest.client.events.impl.player.EventUpdate;
import ru.fougest.client.modules.Function;
import ru.fougest.client.modules.FunctionAnnotation;
import ru.fougest.client.modules.Type;

@FunctionAnnotation(name = "TapeMouse", type = Type.Player)
public class  TapeMouse extends Function {


    @Override
    public void onEvent(Event event) {
        if (event instanceof EventUpdate e) {
            if (mc.player.getCooledAttackStrength(1f) >= 1) {
                mc.clickMouse();
            }
        }
    }
}
