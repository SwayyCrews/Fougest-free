package ru.fougest.client.command.impl;

import net.minecraft.util.text.TextFormatting;
import ru.fougest.client.command.Command;
import ru.fougest.client.command.CommandInfo;
import ru.fougest.main.managment.Managment;
import ru.fougest.client.modules.Function;
import ru.fougest.client.util.ClientUtil;

@CommandInfo(name = "t", description = "��������/��������� ������.")
public class ToggleCommand extends Command {
    @Override
    public void run(String[] args) throws Exception {
        if (args.length == 2) {
            Function func = Managment.FUNCTION_MANAGER.get(args[1]);
            func.setState(!func.isState());

            if (func.isState()) ClientUtil.sendMesage(TextFormatting.GREEN + "������ " + func.name + " �������.");
            else ClientUtil.sendMesage(TextFormatting.RED + "������ " + func.name + " ��������.");
        } else {
            ClientUtil.sendMesage(TextFormatting.RED + "�� ������� �������� �������� ������!");
        }
    }

    @Override
    public void error() {

    }
}
