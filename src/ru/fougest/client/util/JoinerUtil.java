package ru.fougest.client.util;

import net.minecraft.item.Items;
import net.minecraft.network.play.client.CHeldItemChangePacket;
import ru.fougest.client.util.world.InventoryUtil;

/**
 * @author dedinside
 * @since 02.07.2023
 */
public class JoinerUtil implements IMinecraft {

    public static void selectCompass() {
        int slot = InventoryUtil.getHotBarSlot(Items.COMPASS);

        if (slot == -1) {
            return;
        }

        mc.player.inventory.currentItem = slot;
        mc.player.connection.sendPacket(new CHeldItemChangePacket(slot));
    }
}
