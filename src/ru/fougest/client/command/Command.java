package ru.fougest.client.command;


import ru.fougest.client.util.ClientUtil;
import ru.fougest.client.util.IMinecraft;

public abstract class Command implements IMinecraft {
    public final String command, description;
    public Command() {
        command = this.getClass().getAnnotation(CommandInfo.class).name();
        description = this.getClass().getAnnotation(CommandInfo.class).description();
    }

    public abstract void run(String[] args) throws Exception;
    public abstract void error();
    public void sendMessage(String message) {
        ClientUtil.sendMesage(message);
    }

}
