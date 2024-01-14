package ru.fougest.client.modules.impl.util;

import ru.fougest.client.events.Event;
import ru.fougest.client.events.impl.game.EventKey;
import ru.fougest.main.managment.Managment;
import ru.fougest.client.modules.Function;
import ru.fougest.client.modules.FunctionAnnotation;
import ru.fougest.client.modules.Type;
import ru.fougest.client.modules.settings.imp.BindSetting;

@FunctionAnnotation(name = "CordDroper", type = Type.Util)
public class CordDroper extends Function {
    private BindSetting dropKey = new BindSetting("Кнопка написания координат", 0);

    public CordDroper() {
        addSettings(dropKey);
    }

    @Override
    public void onEvent(Event event) {
        if (event instanceof EventKey e) {
            if (e.key == dropKey.getKey()) {
                mc.player.sendChatMessage("!" + (int) mc.player.getPosX() + ", " + (int) mc.player.getPosY() + ", " + (int) mc.player.getPosZ());
                Managment.NOTIFICATION_MANAGER.add("CordDroper", "Ваши координаты были отправлены в чат!", 2);
            }
        }
    }
}