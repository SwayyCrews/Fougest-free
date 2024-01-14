package ru.fougest.client.command.impl;

import net.minecraft.client.Minecraft;
import net.minecraft.util.text.TextFormatting;
import ru.fougest.client.command.Command;
import ru.fougest.client.command.CommandInfo;
import ru.fougest.other.friend.Friend;
import ru.fougest.other.friend.FriendManager;
import ru.fougest.main.managment.Managment;

/**
 * @author dedinside
 * @since 27.06.2023
 */
@CommandInfo(name = "friend", description = "��������� �������� ������� � ������ ������")
public class FriendCommand extends Command {

    @Override
    public void run(String[] args) throws Exception {
        if (args.length > 1) {
            switch (args[1]) {
                case "add" -> {
                    final String friendName = args[2];
                    addFriend(friendName);
                }
                case "remove" -> {
                    final String friendName = args[2];
                    removeFriend(friendName);
                }
                case "list" -> friendList();
                case "clear" -> clearFriendList();
            }
        } else {
            error();
        }
    }

    /**
     * ������� ���������� ������ � ������
     *
     * @param friendName ��� ������
     */
    private void addFriend(final String friendName) {
        final FriendManager friendManager = Managment.FRIEND_MANAGER;

        if (friendName.contains(Minecraft.getInstance().player.getName().getString())) {
            sendMessage("� ��������� �� �� ������ �������� ������ ���� � ������ :(");
            return;
        }
        if (friendManager.getFriends().stream().map(Friend::getName).toList().contains(friendName)) {
            sendMessage(friendName + " ��� ���� � ������ ������");
            return;
        }

        sendMessage(friendName + " ������� �������� � ������ ������!");
        friendManager.addFriend(friendName);
    }

    /**
     * ������� �������� ������ �� ������ ������
     *
     * @param friendName ��� ������
     */
    private void removeFriend(final String friendName) {
        final FriendManager friendManager = Managment.FRIEND_MANAGER;

        if (friendManager.isFriend(friendName)) {
            friendManager.removeFriend(friendName);
            sendMessage(friendName + " ��� ������ �� ������ ������");
            return;
        }
        sendMessage(friendName + " ���� � ������ ������");
    }

    /**
     * ������� ������ ����� ���� ������
     */
    private void friendList() {
        final FriendManager friendManager = Managment.FRIEND_MANAGER;

        if (friendManager.getFriends().isEmpty()) {
            sendMessage("������ ������ ����");
            return;
        }

        sendMessage("������ ������:");
        for (Friend friend : friendManager.getFriends()) {
            sendMessage(TextFormatting.GRAY + friend.getName());
        }
    }

    /**
     * ������� ������ ������
     */
    private void clearFriendList() {
        final FriendManager friendManager = Managment.FRIEND_MANAGER;

        if (friendManager.getFriends().isEmpty()) {
            sendMessage("������ ������ ����");
            return;
        }

        friendManager.clearFriend();
        sendMessage("������ ������ ������� ������");
    }

    @Override
    public void error() {
        sendMessage(TextFormatting.GRAY + "������ � �������������" + TextFormatting.WHITE + ":");
        sendMessage(TextFormatting.WHITE + "." + "friend add " + TextFormatting.GRAY + "<"
                + TextFormatting.RED + "name" + TextFormatting.GRAY + ">");
        sendMessage(TextFormatting.WHITE + "." + "friend remove " + TextFormatting.GRAY + "<"
                + TextFormatting.RED + "name" + TextFormatting.GRAY + ">");
        sendMessage(TextFormatting.WHITE + "." + "friend list" + TextFormatting.GRAY + " - ���������� ������ ���� ��������");
        sendMessage(TextFormatting.WHITE + "." + "friend clear" + TextFormatting.GRAY + " - ������� ���� ��������");
    }
}
