package ru.fougest.client.modules.impl.movement;

import ru.fougest.client.events.Event;
import ru.fougest.client.events.impl.player.EventMove;
import ru.fougest.client.modules.Function;
import ru.fougest.client.modules.FunctionAnnotation;
import ru.fougest.client.modules.Type;
import ru.fougest.client.modules.settings.imp.SliderSetting;
import ru.fougest.client.util.movement.MoveUtil;

/**
 * @author dedinside
 * @since 27.06.2023
 */
@FunctionAnnotation(name = "Dragon Fly", type = Type.Movement)
public class DragonFlyFunction extends Function {
    private final SliderSetting dragonFlySpeed = new SliderSetting("Скорость флая", 1.6f, 1.0f, 10.0F, 0.01f);
    private final SliderSetting dragonFlyMotionY = new SliderSetting("Скорость флая по Y", 0.6f, 0.1f, 5, 0.01f);

    public DragonFlyFunction() {
        addSettings(dragonFlySpeed,dragonFlyMotionY);
    }

    @Override
    public void onEvent(Event event) {
        if (event instanceof EventMove move) {
            handleDragonFly(move);
        }
    }

    /**
     * Обработка движения при /fly
     *
     * @param move Обработчик EventMove
     */
    private void handleDragonFly(EventMove move) {
        if (mc.player.abilities.isFlying) {

            if (!mc.player.isSneaking() && mc.gameSettings.keyBindJump.isKeyDown()) {
                move.motion().y = dragonFlyMotionY.getValue().floatValue();
            }
            if (mc.gameSettings.keyBindSneak.isKeyDown()) {
                move.motion().y = -dragonFlyMotionY.getValue().floatValue();
            }

            MoveUtil.MoveEvent.setMoveMotion(move, dragonFlySpeed.getValue().floatValue());
        }
    }
}
