package ru.fougest.client.modules.impl.util;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.text.TextFormatting;
import ru.fougest.client.events.Event;
import ru.fougest.client.events.impl.game.EventKey;
import ru.fougest.client.modules.Function;
import ru.fougest.client.modules.FunctionAnnotation;
import ru.fougest.client.modules.Type;
import ru.fougest.client.modules.settings.imp.BindSetting;
import ru.fougest.client.modules.settings.imp.BooleanOption;
import ru.fougest.client.util.ClientUtil;
import ru.fougest.client.util.world.InventoryUtil;

@FunctionAnnotation(name = "AutoSwap", type = Type.Util)
public class AutoSwap extends Function {

    private BindSetting swapToShieldKey = new BindSetting("замена на щит", 0);
    private BindSetting swapToGoldenAppleKey = new BindSetting("замена на золотое €блоко", 0);
    private BindSetting swapToEnchGoldenAppleKey = new BindSetting("замена на чареное €блоко", 0);
    private ItemStack oldStack = null;
    private BooleanOption notification = new BooleanOption("ќповещение", true);

    public AutoSwap() {
        addSettings(swapToShieldKey, swapToGoldenAppleKey, swapToEnchGoldenAppleKey, notification);
    }

    @Override
    public void onEvent(Event event) {
        if (event instanceof EventKey e) {
            if (e.key == swapToShieldKey.getKey()) {
                swapItem(Items.SHIELD, "ўит");
            }

            if (e.key == swapToGoldenAppleKey.getKey()) {
                swapItem(Items.GOLDEN_APPLE, "«олотое €блоко");
            }

            if (e.key == swapToEnchGoldenAppleKey.getKey()) {
                swapItem(Items.ENCHANTED_GOLDEN_APPLE, "«ачарованное золотое €блоко");
            }
        }
    }

    private void swapItem(Item desiredItem, String itemName) {
        int itemSlot = InventoryUtil.getItemSlot(desiredItem);

        if (itemSlot == -1) {
            ClientUtil.sendMesage(TextFormatting.RED + itemName + " не найден в инвентаре!");
            return;
        }

        if (mc.player.getHeldItemOffhand().getItem() != desiredItem) {
            oldStack = mc.player.getHeldItemOffhand().copy();
            InventoryUtil.moveItem(itemSlot, 45, true);

            if (notification.get()) {
                ClientUtil.sendMesage(TextFormatting.RED + "—вапнул на " + itemName + " в левую руку!");
            }
        } else if (oldStack != null) {
            int oldStackSlot = InventoryUtil.getItemSlot(oldStack.getItem());
            InventoryUtil.moveItem(oldStackSlot, 45, true);

            if (notification.get()) {
                ClientUtil.sendMesage(TextFormatting.RED + "—вапнул обратно в левую руку!");
            }
            oldStack = null;
        }
    }
}