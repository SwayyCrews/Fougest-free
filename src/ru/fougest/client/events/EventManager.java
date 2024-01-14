package ru.fougest.client.events;

import net.minecraft.client.Minecraft;
import ru.fougest.main.managment.Managment;
import ru.fougest.client.modules.Function;
import ru.fougest.client.util.ClientUtil;

public class EventManager {

    /**
     * �������� ������� � �������� ��� ���� �������� ������� ��� ���������.
     *
     * @param event ������� ��� ������.
     */
    public static void call(final Event event) {
        if (Minecraft.getInstance().player == null || Minecraft.getInstance().world == null) {
            return;
        }

        if (event.isCancel()) {
            return;
        }

        // ������� ���� �������� ������� � ����� �������
        if (!ClientUtil.legitMode) {
            callEvent(event);
        }
    }

    /**
     * �������� ��������� ������� � �������� ��� ���� �������� ������� ��� ���������.
     *
     * @param event ������� ��� ������.
     */
    private static void callEvent(Event event) {
        for (final Function module : Managment.FUNCTION_MANAGER.getFunctions()) {
            if (!module.isState())
                continue;

            module.onEvent(event);
        }
    }
}