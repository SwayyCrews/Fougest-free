package ru.fougest.client.command.impl;

import net.minecraft.client.Minecraft;
import ru.fougest.client.command.Command;
import ru.fougest.client.command.CommandInfo;

@CommandInfo(name = "vclip", description = "Телепортирует вас вверх.")
public class VClipCommand extends Command {
    @Override
    public void run(String[] args) throws Exception {
        Minecraft.getInstance().player.setPosition(mc.player.getPosX(), mc.player.getPosY() + Double.parseDouble(args[1]), mc.player.getPosZ());
    }

    @Override
    public void error() {

    }
}
