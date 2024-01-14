package ru.fougest.client.modules.impl.render;

import ru.fougest.client.events.Event;
import ru.fougest.client.events.impl.render.EventRender;
import ru.fougest.client.modules.Function;

public class RegionRender extends Function {
    @Override
    public void onEvent(Event event) {
        if (event instanceof EventRender e) {
            if (e.isRender3D()) {

            }
        }
    }
}
