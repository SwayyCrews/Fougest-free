package ru.fougest.client.modules.impl.util;

import ru.fougest.client.events.Event;
import ru.fougest.client.modules.Function;
import ru.fougest.client.modules.FunctionAnnotation;
import ru.fougest.client.modules.Type;
import ru.fougest.client.modules.settings.imp.BooleanOption;
import ru.fougest.client.modules.settings.imp.MultiBoxSetting;

/**
 * @author dedinside
 * @since 12.06.2023
 */
@FunctionAnnotation(name = "Optimization", type = Type.Util)
public class Optimization extends Function {

    public final MultiBoxSetting optimizeSelection = new MultiBoxSetting("Оптимизировать", new BooleanOption("Освещение",true), new BooleanOption("Партиклы",true), new BooleanOption("Подсветка клиента.", false));

     public Optimization() {
         addSettings(optimizeSelection);
     }

    @Override
    public void onEvent(Event event) {}
}
