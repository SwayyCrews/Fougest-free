package ru.fougest.client.modules.impl.combat;

import net.minecraft.network.play.client.CPlayerDiggingPacket;
import net.minecraft.network.play.server.SConfirmTransactionPacket;
import net.minecraft.network.play.server.SEntityVelocityPacket;
import net.minecraft.network.play.server.SPlayerPositionLookPacket;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import ru.fougest.client.events.Event;
import ru.fougest.client.events.impl.packet.EventPacket;
import ru.fougest.client.events.impl.player.EventUpdate;
import ru.fougest.client.modules.Function;
import ru.fougest.client.modules.FunctionAnnotation;
import ru.fougest.client.modules.Type;
import ru.fougest.client.modules.settings.imp.ModeSetting;

@FunctionAnnotation(name = "Velocity", type = Type.Fight)
public class VelocityFunction extends Function {

    private final ModeSetting mode = new ModeSetting("Mode", "Cancel", "Cancel", "Grim", "ReallyWorld");

    public VelocityFunction() {
        addSettings(mode);
    }

    private int toSkip;
    private int await;

    BlockPos blockPos;

    boolean damaged;

    @Override
    public void onEvent(final Event event) {
        if (mc.player == null || mc.world == null) return;

        if (event instanceof EventPacket e && e.isReceivePacket()) {
            switch (mode.get()) {
                case "Cancel" -> {
                    if (e.getPacket() instanceof SEntityVelocityPacket p) {
                        if (p.getEntityID() != mc.player.getEntityId()) return;

                        e.setCancel(true);
                    }
                }

                case "Grim" -> {
                    if (e.getPacket() instanceof SEntityVelocityPacket p) {
                        if (p.getEntityID() != mc.player.getEntityId() || toSkip < 0) return;

                        toSkip = 8;
                        event.setCancel(true);
                    }

                    if (e.getPacket() instanceof SConfirmTransactionPacket) {
                        if (toSkip < 0) toSkip++;

                        else if (toSkip > 1) {
                            toSkip--;
                            event.setCancel(true);
                        }
                    }

                    if (e.getPacket() instanceof SPlayerPositionLookPacket) toSkip = -8;
                }

                case "ReallyWorld" -> {
                    if (e.getPacket() instanceof SEntityVelocityPacket p) {
                        if (p.getEntityID() != mc.player.getEntityId() || await > -5) {
                            return;
                        }

                        await = 2;
                        damaged = true;
                        event.setCancel(true);
                    }
                }
            }
        }

        if (event instanceof EventUpdate ) {
            if (mode.is("ReallyWorld")) {
                await--;

                if (damaged) {
                    blockPos = new BlockPos(mc.player.getPositionVec());
                    mc.player.connection.sendPacket(new CPlayerDiggingPacket(CPlayerDiggingPacket.Action.STOP_DESTROY_BLOCK, blockPos, Direction.UP));
                    mc.player.connection.sendPacket(new CPlayerDiggingPacket(CPlayerDiggingPacket.Action.START_DESTROY_BLOCK, blockPos, Direction.UP));
                    damaged = false;
                }
            }
        }
    }

    private void reset() {
        toSkip = 0;
        await = 0;
    }

    @Override
    protected void onEnable() {
        super.onEnable();
        reset();
    }

    @Override
    protected void onDisable() {
        super.onDisable();
        reset();
    }
}