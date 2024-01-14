package ru.fougest.client.modules.impl.player;

import ru.fougest.client.events.Event;
import ru.fougest.client.modules.Function;
import ru.fougest.client.modules.FunctionAnnotation;
import ru.fougest.client.modules.Type;
import ru.fougest.client.modules.settings.imp.SliderSetting;

/**
 * @author dedinside
 * @since 25.06.2023
 */

@FunctionAnnotation(name = "ItemScroller", type = Type.Player)
public class ItemScroller extends Function {

    public SliderSetting delay = new SliderSetting("Задержка", 80, 0, 1000, 1);


    public ItemScroller() {
        addSettings(delay);
    }

    @Override
    public void onEvent(Event event) {

    }
}
