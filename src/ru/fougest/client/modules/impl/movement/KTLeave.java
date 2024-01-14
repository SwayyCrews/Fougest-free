package ru.fougest.client.modules.impl.movement;

import net.minecraft.network.play.client.CPlayerPacket;
import net.minecraft.network.play.server.SEntityVelocityPacket;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.gen.Heightmap;
import ru.fougest.client.events.Event;
import ru.fougest.client.events.impl.packet.EventPacket;
import ru.fougest.client.modules.Function;
import ru.fougest.client.modules.FunctionAnnotation;
import ru.fougest.client.modules.Type;
import ru.fougest.client.util.ClientUtil;
import ru.fougest.client.util.misc.ServerUtil;

import java.util.concurrent.ThreadLocalRandom;

@FunctionAnnotation(name = "KTLeave", type = Type.Movement)
public class KTLeave extends Function {

    @Override
    public void onEvent(Event event) {
        if (event instanceof EventPacket e) {
            if (ServerUtil.isHW()) {
                if (e.getPacket() instanceof SEntityVelocityPacket p) {
                    if (p.getEntityID() == mc.player.getEntityId()) {
                        leaveHoly();
                    }
                }
            } else {
                ClientUtil.sendMesage(TextFormatting.RED + "������ ������� �������� ������ �� Holyworld!");
                toggle();
            }
        }

    }


    private void leaveHoly() {
        float x = (float) mc.player.getPosX() + ThreadLocalRandom.current().nextFloat(-50, 50);
        float z = (float) mc.player.getPosZ() + ThreadLocalRandom.current().nextFloat(-50, 50);

        float y = mc.world.getHeight(Heightmap.Type.WORLD_SURFACE, (int) x, (int) z) - 1;
        ClientUtil.sendMesage("������������...");

        for (int i = 0; i <= 10; i++) {
            mc.player.connection.sendPacket(new CPlayerPacket.PositionPacket(x, y, z, true));
        }
        mc.player.connection.sendPacket(new CPlayerPacket.PositionRotationPacket(mc.player.getPosX(), mc.player.getPosY(), mc.player.getPosZ(), -180, 0, false));
        for (int i = 0; i <= 10; i++) {
            mc.player.connection.sendPacket(new CPlayerPacket.PositionPacket(x, y, z, true));
        }
        mc.player.connection.sendPacket(new CPlayerPacket.PositionRotationPacket(mc.player.getPosX(), mc.player.getPosY(), mc.player.getPosZ(), -180, 0, false));

        for (int i = 0; i <= 10; i++) {
            mc.player.connection.sendPacket(new CPlayerPacket.PositionPacket(x, y, z, true));
        }
        toggle();
    }
}
