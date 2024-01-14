package ru.fougest.client.modules.impl.player;

import net.minecraft.client.gui.screen.DeathScreen;
import ru.fougest.client.events.Event;
import ru.fougest.client.events.impl.player.EventUpdate;
import ru.fougest.client.modules.Function;
import ru.fougest.client.modules.FunctionAnnotation;
import ru.fougest.client.modules.Type;
/**
 * @author dedinside
 * @since 04.06.2023
 */
@FunctionAnnotation(name = "AutoRespawn", type = Type.Player)
public class AutoRespawnFunction extends Function {

    @Override
    public void onEvent(final Event event) {
        if (event instanceof EventUpdate) {
            if (mc.currentScreen instanceof DeathScreen && mc.player.deathTime > 2) {
                mc.player.respawnPlayer();
                mc.displayGuiScreen(null);
            }
        }
    }
}
