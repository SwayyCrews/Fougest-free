package ru.fougest.client.modules.impl.util;

import ru.fougest.client.events.Event;
import ru.fougest.client.modules.Function;
import ru.fougest.client.modules.FunctionAnnotation;
import ru.fougest.client.modules.Type;

@FunctionAnnotation(name = "DiscordRPC", type = Type.Util)
public class DiscordRPC extends Function {

    @Override
    protected void onDisable() {
        super.onDisable();

    }

    @Override
    public void onEvent(Event event) {

    }
}
