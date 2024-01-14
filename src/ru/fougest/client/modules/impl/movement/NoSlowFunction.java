package ru.fougest.client.modules.impl.movement;

import net.minecraft.item.UseAction;
import net.minecraft.network.play.client.*;
import net.minecraft.util.Hand;
import ru.fougest.client.events.Event;
import ru.fougest.client.events.impl.packet.EventPacket;
import ru.fougest.client.events.impl.player.EventDamage;
import ru.fougest.client.events.impl.player.EventNoSlow;
import ru.fougest.client.modules.Function;
import ru.fougest.client.modules.FunctionAnnotation;
import ru.fougest.client.modules.Type;
import ru.fougest.client.modules.settings.imp.ModeSetting;
import ru.fougest.client.util.misc.DamageUtil;
import ru.fougest.client.util.movement.MoveUtil;

/**
 * @author dedinside
 * @since 04.06.2023
 */
@FunctionAnnotation(name = "NoSlow", type = Type.Movement)
public class NoSlowFunction extends Function {
    public ModeSetting mode = new ModeSetting("���", "Matrix", "Vanilla", "Matrix", "Really World", "GrimAC");
    private DamageUtil damageUtil = new DamageUtil();

    public NoSlowFunction() {
        addSettings(mode);
    }

    @Override
    public void onEvent(final Event event) {
        if (mc.player.isElytraFlying()) return;

        if (event instanceof EventNoSlow eventNoSlow) {
            handleEventUpdate(eventNoSlow);
        } else if (event instanceof EventDamage damage) {
            damageUtil.processDamage(damage);
        } else if (event instanceof EventPacket eventPacket) {
            if (eventPacket.isReceivePacket())
                damageUtil.onPacketEvent(eventPacket);
        }
    }


    /**
     * ������������ ������� ���� EventUpdate.
     */
    private void handleEventUpdate(EventNoSlow eventNoSlow) {
        if (mc.player.isHandActive()) {
            switch (mode.get()) {
                case "Vanilla" -> eventNoSlow.setCancel(true);
                case "GrimAC" -> handleGrimACMode(eventNoSlow);
                case "Matrix" -> handleMatrixMode(eventNoSlow);
                case "Really World" -> handleGrimNewMode(eventNoSlow);
            }
        }
    }

    /**
     * ������������ ��� "Matrix".
     */
    private void handleMatrixMode(EventNoSlow eventNoSlow) {
        boolean isFalling = (double) mc.player.fallDistance > 0.725;
        float speedMultiplier;
        eventNoSlow.setCancel(true);
        if (mc.player.isOnGround() && !mc.player.movementInput.jump) {
            if (mc.player.ticksExisted % 2 == 0) {
                boolean isNotStrafing = mc.player.moveStrafing == 0.0F;
                speedMultiplier = isNotStrafing ? 0.5F : 0.4F;
                mc.player.motion.x *= speedMultiplier;
                mc.player.motion.z *= speedMultiplier;
            }
        } else if (isFalling) {
            boolean isVeryFastFalling = (double) mc.player.fallDistance > 1.4;
            speedMultiplier = isVeryFastFalling ? 0.95F : 0.97F;
            mc.player.motion.x *= speedMultiplier;
            mc.player.motion.z *= speedMultiplier;
        }
    }

    private void handleGrimNewMode(EventNoSlow e) {
        this.damageUtil.time(1500);

        if ((mc.player.getHeldItemOffhand().getUseAction() == UseAction.BLOCK
                || mc.player.getHeldItemOffhand().getUseAction() == UseAction.EAT) && mc.player.getActiveHand() == Hand.MAIN_HAND) {
            return;
        }

        if (mc.player.isOnGround() && !mc.player.movementInput.jump && !damageUtil.isNormalDamage()) {
            return;
        }

        if (mc.player.getActiveHand() == Hand.MAIN_HAND) {
            mc.player.connection.sendPacket(new CPlayerTryUseItemPacket(Hand.OFF_HAND));
            e.setCancel(true);
            return;
        }
        e.setCancel(true);
        sendItemChangePacket();
    }


    /**
     * ������������ ��� "GrimAC".
     */
    private void handleGrimACMode(EventNoSlow noSlow) {
        if (mc.player.getHeldItemOffhand().getUseAction() == UseAction.BLOCK && mc.player.getActiveHand() == Hand.MAIN_HAND || mc.player.getHeldItemOffhand().getUseAction() == UseAction.EAT && mc.player.getActiveHand() == Hand.MAIN_HAND) {
            return;
        }

        if (mc.player.getActiveHand() == Hand.MAIN_HAND) {
            mc.player.connection.sendPacket(new CPlayerTryUseItemPacket(Hand.OFF_HAND));
            noSlow.setCancel(true);
            return;
        }

        noSlow.setCancel(true);
        sendItemChangePacket();
    }

    /**
     * ���������� ������ ����� ��������� ��������, ���� ����� ��������.
     */
    private void sendItemChangePacket() {
        if (MoveUtil.isMoving()) {
            mc.player.connection.sendPacket(new CHeldItemChangePacket((mc.player.inventory.currentItem % 8 + 1)));
            mc.player.connection.sendPacket(new CHeldItemChangePacket(mc.player.inventory.currentItem));
        }
    }

    @Override
    protected void onEnable() {
        super.onEnable();
    }
}
