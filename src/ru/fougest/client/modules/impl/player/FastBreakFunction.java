package ru.fougest.client.modules.impl.player;

import ru.fougest.client.events.Event;
import ru.fougest.client.events.impl.player.EventUpdate;
import ru.fougest.client.modules.Function;
import ru.fougest.client.modules.FunctionAnnotation;
import ru.fougest.client.modules.Type;
/**
 * @author dedinside
 * @since 04.06.2023
 */
@FunctionAnnotation(name = "FastBreak", type = Type.Player)
public class FastBreakFunction extends Function {

    @Override
    public void onEvent(Event event) {
        if (event instanceof EventUpdate) {
            // —брасываем задержку удара блока дл€ игрока
            mc.playerController.blockHitDelay = 0;

            // ѕровер€ем, превышает ли текущий урон блока значение 1.0F
            if (mc.playerController.curBlockDamageMP > 1.0F) {
                // ≈сли превышает, устанавливаем значение урона блока равным 1.0F
                mc.playerController.curBlockDamageMP = 1.0F;
            }
        }
    }
}
