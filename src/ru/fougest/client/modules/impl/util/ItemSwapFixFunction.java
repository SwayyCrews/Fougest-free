package ru.fougest.client.modules.impl.util;

import net.minecraft.network.play.client.CHeldItemChangePacket;
import net.minecraft.network.play.server.SHeldItemChangePacket;
import ru.fougest.client.events.Event;
import ru.fougest.client.events.impl.packet.EventPacket;
import ru.fougest.client.modules.Function;
import ru.fougest.client.modules.FunctionAnnotation;
import ru.fougest.client.modules.Type;

/**
 * @author dedinside
 * @since 12.06.2023
 */
@FunctionAnnotation(name = "ItemSwapFix", type = Type.Util)
public class ItemSwapFixFunction extends Function {
    @Override
    public void onEvent(Event event) {
        if (event instanceof EventPacket packetEvent) {
            if (packetEvent.isReceivePacket()) {
                if (packetEvent.getPacket() instanceof SHeldItemChangePacket packetHeldItemChange) {
                    mc.player.connection.sendPacket(new CHeldItemChangePacket(mc.player.inventory.currentItem));
                    event.setCancel(true);
                }
            }
        }
    }
}
