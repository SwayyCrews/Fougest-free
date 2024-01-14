package ru.fougest.client.modules.impl.player;

import ru.fougest.client.events.Event;
import ru.fougest.client.modules.Function;
import ru.fougest.client.modules.FunctionAnnotation;
import ru.fougest.client.modules.Type;
import ru.fougest.client.modules.settings.imp.BooleanOption;
import ru.fougest.client.modules.settings.imp.MultiBoxSetting;

/**
 * @author dedinside
 * @since 04.06.2023
 */
@FunctionAnnotation(name = "NoPush", type = Type.Player)
public class NoPushFunction extends Function {

    public final MultiBoxSetting modes = new MultiBoxSetting("Тип",
            new BooleanOption("Игроки", true),
            new BooleanOption("Блоки", true),
            new BooleanOption("Вода", true));

    public NoPushFunction() {
        addSettings(modes);
    }

    @Override
    public void onEvent(final Event event) {
    }
}
