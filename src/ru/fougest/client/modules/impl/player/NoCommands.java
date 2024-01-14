package ru.fougest.client.modules.impl.player;

import ru.fougest.client.events.Event;
import ru.fougest.client.modules.Function;
import ru.fougest.client.modules.FunctionAnnotation;
import ru.fougest.client.modules.Type;

@FunctionAnnotation(name = "NoCommands", type = Type.Player)
public class NoCommands extends Function {
    @Override
    public void onEvent(Event event) {

    }
}
