package ru.fougest.client.command.impl;

import net.minecraft.util.text.TextFormatting;
import ru.fougest.client.command.Command;
import ru.fougest.client.command.CommandInfo;
import ru.fougest.main.managment.Managment;
import ru.fougest.client.modules.Function;
import ru.fougest.client.util.ClientUtil;

@CommandInfo(name = "t", description = "Включить/выключить модуль.")
public class ToggleCommand extends Command {
    @Override
    public void run(String[] args) throws Exception {
        if (args.length == 2) {
            Function func = Managment.FUNCTION_MANAGER.get(args[1]);
            func.setState(!func.isState());

            if (func.isState()) ClientUtil.sendMesage(TextFormatting.GREEN + "Модуль " + func.name + " включен.");
            else ClientUtil.sendMesage(TextFormatting.RED + "Модуль " + func.name + " выключен.");
        } else {
            ClientUtil.sendMesage(TextFormatting.RED + "Вы указали неверное название модуля!");
        }
    }

    @Override
    public void error() {

    }
}
