package ru.fougest.client.modules.impl.util;

import net.minecraft.inventory.container.ChestContainer;
import net.minecraft.inventory.container.ClickType;
import net.minecraft.item.Item;
import ru.fougest.client.events.Event;
import ru.fougest.client.events.impl.player.EventUpdate;
import ru.fougest.client.modules.Function;
import ru.fougest.client.modules.FunctionAnnotation;
import ru.fougest.client.modules.Type;
import ru.fougest.client.modules.settings.imp.SliderSetting;
import ru.fougest.client.util.misc.TimerUtil;

/**
 * @author dedinside
 * @since 27.07.2023
 */

@FunctionAnnotation(name = "ChestStealer", type = Type.Player)
public class ChestStealer extends Function {

    private final SliderSetting stealDelay = new SliderSetting("Steal Delay", 100, 0, 1000, 1);
    private final TimerUtil timerUtil = new TimerUtil();

    public ChestStealer() {
        addSettings(stealDelay);
    }

    @Override
    public void onEvent(final Event event) {
        if (event instanceof EventUpdate) {
            if (mc.player.openContainer instanceof ChestContainer) {
                ChestContainer container = (ChestContainer) mc.player.openContainer;
                for (int index = 0; index < container.inventorySlots.size(); ++index) {
                    if (container.getLowerChestInventory().getStackInSlot(index).getItem() != Item.getItemById(0)
                            && timerUtil.hasTimeElapsed(stealDelay.getValue().longValue())) {
                        mc.playerController.windowClick(container.windowId, index, 0, ClickType.QUICK_MOVE, mc.player);
                        timerUtil.reset();
                        continue;
                    }

                    if (container.getLowerChestInventory().isEmpty()) {
                        mc.player.closeScreen();
                    }
                }
            }
        }
    }
}
