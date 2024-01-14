package ru.fougest.client.command.impl;

import ru.fougest.client.command.Command;
import ru.fougest.client.command.CommandInfo;
import ru.fougest.main.managment.Managment;
import ru.fougest.client.util.ClientUtil;

@CommandInfo(name = "panic", description = "Выключает все функции чита")

public class PanicCommand extends Command {
    @Override
    public void run(String[] args) throws Exception {
        if (args.length == 1) {
            Managment.FUNCTION_MANAGER.getFunctions().stream().filter(function -> function.state).forEach(function -> function.setState(false));
            ClientUtil.sendMesage("Выключил все модули!");
        } else error();
    }

    @Override
    public void error() {

    }
}
