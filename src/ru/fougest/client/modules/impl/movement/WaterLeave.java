package ru.fougest.client.modules.impl.movement;

import net.minecraft.network.play.client.CConfirmTeleportPacket;
import net.minecraft.network.play.server.SPlayerPositionLookPacket;
import ru.fougest.client.events.Event;
import ru.fougest.client.events.impl.packet.EventPacket;
import ru.fougest.client.events.impl.player.EventUpdate;
import ru.fougest.client.modules.Function;
import ru.fougest.client.modules.FunctionAnnotation;
import ru.fougest.client.modules.Type;

@FunctionAnnotation(name = "Water Leave", type = Type.Movement)
public class WaterLeave extends Function {

    private boolean inWater;
    private int ticks;

    @Override
    public void onEvent(Event event) {
        if (event instanceof EventPacket e) {
            if (e.getPacket() instanceof SPlayerPositionLookPacket p) {
                if (mc.player == null) toggle();
                mc.player.setPosition(p.getX(), p.getY(), p.getZ());
                mc.player.connection.sendPacket(new CConfirmTeleportPacket(p.getTeleportId()));
                e.setCancel(true);
                mc.player.motion.y += 0.5;
                toggle();
            }
        }
        if (event instanceof EventUpdate e) {
            if (mc.player.isInWater() && mc.player.fallDistance > 0) {
                mc.player.motion.y = 3;
            }
        }
    }

    @Override
    public void onDisable() {
        inWater = false;
        mc.timer.timerSpeed = 1f;
        ticks = 0;
        super.onDisable();
    }

}
