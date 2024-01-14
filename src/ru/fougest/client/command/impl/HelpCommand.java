package ru.fougest.client.command.impl;

import ru.fougest.client.command.Command;
import ru.fougest.client.command.CommandInfo;
import ru.fougest.main.managment.Managment;
import ru.fougest.client.util.ClientUtil;

@CommandInfo(name = "help", description = "Телепортирует вас вперед.")
public class HelpCommand extends Command {
    @Override
    public void run(String[] args) throws Exception {
        for (Command cmd : Managment.COMMAND_MANAGER.getCommands()) {
            if (cmd instanceof HelpCommand) continue;
            ClientUtil.sendMesage(cmd.command + " | " + cmd.description);
        }
    }

    @Override
    public void error() {

    }
}
