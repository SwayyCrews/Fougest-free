package ru.fougest.client.modules.impl.util;

import net.minecraft.item.Items;
import net.minecraft.network.play.client.CHeldItemChangePacket;
import net.minecraft.network.play.client.CPlayerPacket;
import net.minecraft.network.play.client.CPlayerTryUseItemPacket;
import net.minecraft.util.Hand;
import ru.fougest.client.events.Event;
import ru.fougest.client.events.impl.game.EventMouseTick;
import ru.fougest.main.managment.Managment;
import ru.fougest.client.modules.Function;
import ru.fougest.client.modules.FunctionAnnotation;
import ru.fougest.client.modules.Type;
import ru.fougest.client.modules.impl.player.GappleCooldownFunction;
import ru.fougest.client.util.world.InventoryUtil;

/**
 * @author dedinside
 * @since 07.06.2023
 */
@FunctionAnnotation(name = "MiddleClickPearl", type = Type.Util)
public class MiddleClickPearlFunction extends Function {

    @Override
    public void onEvent(final Event event) {
        if (event instanceof EventMouseTick mouseTick) {
            if (mouseTick.getButton() == 2) {
                handleMouseTickEvent();
            }
        }
    }

    /**
     * ������������ ������� EventMouseTick ��� ������� �� ������ ������ ���� (������ � ����� 2).
     */
    private void handleMouseTickEvent() {
        if (!mc.player.getCooldownTracker().hasCooldown(Items.ENDER_PEARL) && InventoryUtil.getPearls() >= 0) {
            sendHeldItemChangePacket(InventoryUtil.getPearls());

            sendPlayerRotationPacket(mc.player.rotationYaw, mc.player.rotationPitch, mc.player.isOnGround());
            useItem(Hand.MAIN_HAND);

            sendHeldItemChangePacket(mc.player.inventory.currentItem);
        }
    }

    /**
     * ���������� ����� ����� ��������� ��������.
     *
     * @param itemSlot ���� ��������
     */
    private void sendHeldItemChangePacket(int itemSlot) {
        mc.player.connection.sendPacket(new CHeldItemChangePacket(itemSlot));
        GappleCooldownFunction cooldown = Managment.FUNCTION_MANAGER.gappleCooldownFunction;
        GappleCooldownFunction.ItemEnum itemEnum = GappleCooldownFunction.ItemEnum.getItemEnum(Items.ENDER_PEARL);

        if (cooldown.state && itemEnum != null && cooldown.isCurrentItem(itemEnum)) {
            cooldown.lastUseItemTime.put(itemEnum.getItem(), System.currentTimeMillis());
        }
    }

    /**
     * ���������� ����� �������� ������, ���� ������ ���� �� � ������� ������� ��������.
     *
     * @param yaw      �������� �������� �� �����������
     * @param pitch    �������� �������� �� ���������
     * @param onGround �������� ��������� �� ����� �� �����
     */
    private void sendPlayerRotationPacket(float yaw, float pitch, boolean onGround) {
        if (Managment.FUNCTION_MANAGER.auraFunction.target != null) {
            mc.player.connection.sendPacket(new CPlayerPacket.RotationPacket(yaw, pitch, onGround));
        }
    }

    /**
     * ���������� ����� ������������� �������� � ������� ����.
     *
     * @param hand �������� ����
     */
    private void useItem(Hand hand) {
        mc.player.connection.sendPacket(new CPlayerTryUseItemPacket(hand));
        mc.player.swingArm(hand);
    }
}
