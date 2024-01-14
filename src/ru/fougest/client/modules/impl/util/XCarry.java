package ru.fougest.client.modules.impl.util;

import net.minecraft.network.play.client.CCloseWindowPacket;
import ru.fougest.client.events.Event;
import ru.fougest.client.events.impl.packet.EventPacket;
import ru.fougest.client.modules.Function;
import ru.fougest.client.modules.FunctionAnnotation;
import ru.fougest.client.modules.Type;

@FunctionAnnotation(name = "XCarry", type = Type.Util)
public class XCarry extends Function {


    @Override
    public void onEvent(Event event) {
        if (event instanceof EventPacket) {
            if (((EventPacket) event).getPacket() instanceof CCloseWindowPacket) {
                event.setCancel(true);
            }
        }
    }
}
