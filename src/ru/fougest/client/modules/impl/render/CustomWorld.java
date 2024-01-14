package ru.fougest.client.modules.impl.render;

import net.minecraft.network.play.server.SUpdateTimePacket;
import ru.fougest.client.events.Event;
import ru.fougest.client.events.impl.packet.EventPacket;
import ru.fougest.client.events.impl.player.EventUpdate;
import ru.fougest.client.modules.Function;
import ru.fougest.client.modules.FunctionAnnotation;
import ru.fougest.client.modules.Type;
import ru.fougest.client.modules.settings.imp.*;

/**
 * @author dedinside
 * @since 26.06.2023
 */
@FunctionAnnotation(name = "Custom World", type = Type.Render)
public class CustomWorld extends Function {

    public MultiBoxSetting modes = new MultiBoxSetting("��������",
            new BooleanOption("�����", false),
            new BooleanOption("�����", false));

    private ModeSetting timeOfDay = new ModeSetting("����� �����", "����", "����", "�����", "�������", "����", "�������", "�������").setVisible(() -> modes.get(0));

    public ColorSetting colorFog = new ColorSetting("���� ������", -1).setVisible(() -> modes.get(1));
    public SliderSetting distanceFog = new SliderSetting("��������� ������", 4.0F, 1.1f, 30.0F, 0.01f).setVisible(() -> modes.get(1));

    public CustomWorld() {
        addSettings(modes, timeOfDay, colorFog, distanceFog);
    }

    float time;

    @Override
    public void onEvent(Event event) {
        if (event instanceof EventPacket eventPacket && ((EventPacket) event).isReceivePacket()) {
            if (eventPacket.getPacket() instanceof SUpdateTimePacket) {
                if (modes.get(0)) {
                    eventPacket.setCancel(true);
                }
            }
        }
        if (event instanceof EventUpdate) {
            if (modes.get(0)) {
                float time = 0;
                switch (timeOfDay.get()) {
                    case "����" -> time = 1000;
                    case "�����" -> time = 12000;
                    case "�������" -> time = 23000;
                    case "����" -> time = 13000;
                    case "�������" -> time = 18000;
                    case "�������" -> time = 6000;
                }
                mc.world.setDayTime((long) time);
            }
        }
    }
}
