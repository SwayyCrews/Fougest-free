package ru.fougest.client.modules.impl.render;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.minecart.MinecartEntity;
import net.minecraft.tileentity.*;
import ru.fougest.client.events.Event;
import ru.fougest.client.events.impl.render.EventRender;
import ru.fougest.client.modules.Function;
import ru.fougest.client.modules.FunctionAnnotation;
import ru.fougest.client.modules.Type;
import ru.fougest.client.modules.settings.imp.BooleanOption;
import ru.fougest.client.modules.settings.imp.MultiBoxSetting;
import ru.fougest.client.util.render.RenderUtil;

/**
 * @author dedinside
 * @since 28.06.2023
 */

@FunctionAnnotation(name = "BlockESP", type = Type.Render)
public class BlockESP extends Function {

    private final MultiBoxSetting blocksSelection = new MultiBoxSetting("Выбрать блоки",
            new BooleanOption("Сундуки", true),
            new BooleanOption("Эндер Сундуки", true),
            new BooleanOption("Спавнера", true),
            new BooleanOption("Шалкера", true),
            new BooleanOption("Кровати", true),
            new BooleanOption("Вагонетка", true));

    public BlockESP() {
        addSettings(blocksSelection);
    }

    @Override
    public void onEvent(Event event) {

        if (event instanceof EventRender e) {
            if (e.isRender3D()) {
                for (TileEntity t : mc.world.loadedTileEntityList) {
                    if (t instanceof ChestTileEntity) {
                        if (blocksSelection.get(0))
                            RenderUtil.Render3D.drawBlockBox(t.getPos(), -1);
                    }
                    if (t instanceof EnderChestTileEntity) {
                        if (blocksSelection.get(1))
                            RenderUtil.Render3D.drawBlockBox(t.getPos(), -1);
                    }
                    if (t instanceof MobSpawnerTileEntity) {
                        if (blocksSelection.get(2))
                            RenderUtil.Render3D.drawBlockBox(t.getPos(), -1);
                    }
                    if (t instanceof ShulkerBoxTileEntity) {
                        if (blocksSelection.get(3))
                            RenderUtil.Render3D.drawBlockBox(t.getPos(), -1);
                    }
                    if (t instanceof BedTileEntity) {
                        if (blocksSelection.get(4))
                            RenderUtil.Render3D.drawBlockBox(t.getPos(), -1);
                    }
                }
                for (Entity entity : mc.world.getAllEntities()) {
                    if (!(entity instanceof MinecartEntity) || !blocksSelection.get(5)) continue;


                    RenderUtil.Render3D.drawBlockBox(entity.getPosition(), -1);

                }
            }
        }

    }
}
