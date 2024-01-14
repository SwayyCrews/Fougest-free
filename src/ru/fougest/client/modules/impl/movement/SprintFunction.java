package ru.fougest.client.modules.impl.movement;

import ru.fougest.client.events.Event;
import ru.fougest.client.events.impl.player.EventUpdate;
import ru.fougest.client.modules.Function;
import ru.fougest.client.modules.FunctionAnnotation;
import ru.fougest.client.modules.Type;
import ru.fougest.client.modules.settings.imp.BooleanOption;
import ru.fougest.client.util.movement.MoveUtil;

/**
 * @author dedinside
 * @since 03.06.2023
 */
@FunctionAnnotation(name = "Sprint", type = Type.Movement)
public class SprintFunction extends Function {

    public BooleanOption keepSprint = new BooleanOption("Keep Sprint", true);

    public SprintFunction() {
        addSettings(keepSprint);
    }

    @Override
    public void onEvent(final Event event) {
        if (event instanceof EventUpdate) {
            // Если игрок не присев и не столкнулся с препятствием по горизонтали
            if (!mc.player.isSneaking() && !mc.player.collidedHorizontally)
                // Устанавливаем режим спринта, если игрок движется
                mc.player.setSprinting(MoveUtil.isMoving());
        }

    }
}
