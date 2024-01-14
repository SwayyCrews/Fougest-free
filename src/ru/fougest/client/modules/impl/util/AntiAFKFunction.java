package ru.fougest.client.modules.impl.util;

import ru.fougest.client.events.Event;
import ru.fougest.client.events.impl.player.EventUpdate;
import ru.fougest.client.modules.Function;
import ru.fougest.client.modules.FunctionAnnotation;
import ru.fougest.client.modules.Type;
import ru.fougest.client.util.misc.TimerUtil;
import ru.fougest.client.util.movement.MoveUtil;

/**
 * @author dedinside
 * @since 12.06.2023
 */
@FunctionAnnotation(name = "AntiAFK", type = Type.Util)
public class AntiAFKFunction extends Function {

    private final TimerUtil timerUtil = new TimerUtil();

    @Override
    public void onEvent(Event event) {
        if (event instanceof EventUpdate) {

            if (!MoveUtil.isMoving()) {
                if (timerUtil.hasTimeElapsed(15000)) {
                    mc.player.sendChatMessage("/BEFF LOX");
                    timerUtil.reset();
                }
            } else {
                timerUtil.reset();
            }
        }
    }
}
