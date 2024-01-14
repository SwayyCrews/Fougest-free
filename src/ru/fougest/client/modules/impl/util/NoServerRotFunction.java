package ru.fougest.client.modules.impl.util;

import net.minecraft.network.play.server.SPlayerPositionLookPacket;
import ru.fougest.client.events.Event;
import ru.fougest.client.events.impl.packet.EventPacket;
import ru.fougest.client.modules.Function;
import ru.fougest.client.modules.FunctionAnnotation;
import ru.fougest.client.modules.Type;
import ru.fougest.client.modules.settings.imp.ModeSetting;

/**
 * @author dedinside
 * @since 07.06.2023
 */
@FunctionAnnotation(name = "No Server Rot", type = Type.Util)
public class NoServerRotFunction extends Function {
    private ModeSetting serverRotMode = new ModeSetting("Тип", "Обычный", "Обычный", "RW");

    public NoServerRotFunction() {
        addSettings(serverRotMode);
    }

    @Override
    public void onEvent(final Event event) {
        if (!serverRotMode.is("RW")) {
            if (event instanceof EventPacket packet) {
                if (packet.isReceivePacket()) {
                    if (packet.getPacket() instanceof SPlayerPositionLookPacket packet1) {
                        packet1.yaw = mc.player.rotationYaw;
                        packet1.pitch = mc.player.rotationPitch;
                    }
                }
            }
        }
    }
}
