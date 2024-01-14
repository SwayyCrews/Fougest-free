package ru.fougest.client.modules.impl.movement;

import ru.fougest.client.events.Event;
import ru.fougest.client.events.impl.player.*;
import ru.fougest.client.modules.Function;
import ru.fougest.client.modules.FunctionAnnotation;
import ru.fougest.client.modules.Type;
import ru.fougest.client.modules.settings.imp.SliderSetting;
import ru.fougest.client.util.movement.MoveUtil;

/**
 * @author dedinside
 * @since 22.07.2023
 */
@FunctionAnnotation(name = "WaterSpeed", type = Type.Movement)
public class WaterSpeed extends Function {


    public SliderSetting speed = new SliderSetting("Скорость", 0.41f, 0.1f, 0.5f, 0.01f);
    public SliderSetting motionY = new SliderSetting("Скорость по Y", 0.0f, 0.0f, 0.1f, 0.01f);

    public WaterSpeed() {
        addSettings(speed, motionY);
    }

    private float currentValue;

    @Override
    public void onEvent(Event event) {
        if (event instanceof EventTravel move) {
            if (mc.player.collidedVertically || mc.player.collidedHorizontally) {
                return;
            }
            if (mc.player.isSwimming()) {
                float speed = this.speed.getValue().floatValue() / 10F;
                if (mc.gameSettings.keyBindJump.pressed) {
                    mc.player.motion.y += motionY.getValue().floatValue();
                }
                if (mc.gameSettings.keyBindSneak.pressed) {
                    mc.player.motion.y -= motionY.getValue().floatValue();
                }

                MoveUtil.setMotion(MoveUtil.getMotion());
                move.speed += speed;
            }
        }
    }

    public float calculateNewValue(float value, float increment) {
        return value * Math.min((currentValue += increment) / 100.0f, 1.0f);
    }

}
