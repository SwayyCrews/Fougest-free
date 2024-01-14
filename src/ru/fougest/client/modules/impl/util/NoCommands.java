package ru.fougest.client.modules.impl.util;

import ru.fougest.client.events.Event;
import ru.fougest.client.modules.Function;
import ru.fougest.client.modules.FunctionAnnotation;
import ru.fougest.client.modules.Type;

/**
 * @author dedinside
 * @since 12.07.2023
 */
@FunctionAnnotation(name = "NoCommands", type = Type.Util)
public class NoCommands extends Function {
    @Override
    public void onEvent(Event event) {

    }
}
