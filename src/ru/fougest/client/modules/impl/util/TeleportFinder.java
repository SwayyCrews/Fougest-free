package ru.fougest.client.modules.impl.util;

import net.minecraft.network.IPacket;
import ru.fougest.client.events.Event;
import ru.fougest.client.events.impl.packet.EventPacket;
import ru.fougest.client.modules.Function;
import ru.fougest.client.modules.FunctionAnnotation;
import ru.fougest.client.modules.Type;

import java.lang.reflect.Method;

@FunctionAnnotation(name = "Teleport Finder", type = Type.Util)
public class TeleportFinder extends Function {
    @Override
    public void onEvent(Event event) {

        if (event instanceof EventPacket e) {
           if (e.isReceivePacket()) {
               IPacket<?> packet = e.getPacket();

               for (Method m : packet.getClass().getMethods()) {
                   if (m.getName().toLowerCase().contains("entityid")) {
                       System.out.println(packet);
                   }
               }
           }
        }

    }
}
