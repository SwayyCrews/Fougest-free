package ru.fougest.client.modules.impl.player;

import net.minecraft.network.play.client.CPlayerTryUseItemPacket;
import net.minecraft.network.play.server.SPlaySoundEffectPacket;
import net.minecraft.util.Hand;
import ru.fougest.client.events.Event;
import ru.fougest.client.events.impl.packet.EventPacket;
import ru.fougest.client.events.impl.player.EventUpdate;
import ru.fougest.client.modules.Function;
import ru.fougest.client.modules.FunctionAnnotation;
import ru.fougest.client.modules.Type;
import ru.fougest.client.util.misc.TimerUtil;

@FunctionAnnotation(name = "AutoFish", type = Type.Player)
public class AutoFish extends Function {

    private final TimerUtil delay = new TimerUtil();
    private boolean isHooked = false;
    private boolean needToHook = false;

    @Override
    public void onEvent(final Event event) {
        if (mc.player == null || mc.world == null) return;

        if (event instanceof EventPacket e) {
            if (e.getPacket() instanceof SPlaySoundEffectPacket p) {
                if (p.getSound().getName().getPath().equals("entity.fishing_bobber.splash")) {
                    isHooked = true;
                    delay.reset();
                    //ClientUtil.sendMesage("–€¡¿!");
                }
            }
        }

        if (event instanceof EventUpdate e) {
            if (delay.hasTimeElapsed(600) && isHooked) {
                mc.player.connection.sendPacket(new CPlayerTryUseItemPacket(Hand.MAIN_HAND));
                isHooked = false;
                needToHook = true;
                //ClientUtil.sendMesage("«¿¡–¿À!");
                delay.reset();
            }

            if (delay.hasTimeElapsed(300) && needToHook) {
                mc.player.connection.sendPacket(new CPlayerTryUseItemPacket(Hand.MAIN_HAND));
                needToHook = false;
                //ClientUtil.sendMesage("’”…Õ”À!");
                delay.reset();
            }
        }
    }

    @Override
    protected void onDisable() {
        super.onDisable();
        delay.reset();
        isHooked = false;
        needToHook = false;
    }
}
