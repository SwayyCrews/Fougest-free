package ru.fougest.client.modules.impl.util;

import com.google.common.collect.Lists;
import com.mojang.authlib.GameProfile;
import net.minecraft.client.network.play.NetworkPlayerInfo;
import net.minecraft.inventory.container.ChestContainer;
import net.minecraft.inventory.container.ClickType;
import net.minecraft.network.IPacket;
import net.minecraft.network.play.server.SChatPacket;
import ru.fougest.client.events.event2.Listener;
import ru.fougest.client.events.Event;
import ru.fougest.client.events.impl.packet.EventPacket;
import ru.fougest.client.events.impl.player.EventUpdate;
import ru.fougest.client.modules.Function;
import ru.fougest.client.modules.FunctionAnnotation;
import ru.fougest.client.modules.Type;
import ru.fougest.client.modules.settings.imp.BooleanOption;
import ru.fougest.client.modules.settings.imp.MultiBoxSetting;
import ru.fougest.client.util.Counter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@FunctionAnnotation(name = "Auto Duel", type = Type.Util)
public class AutoDuel extends Function {

    private static final Pattern pattern = Pattern.compile("^\\w{3,16}$");

    private final MultiBoxSetting mode = new MultiBoxSetting("Mode",
            new BooleanOption("Шары", true),
            new BooleanOption("Щит", false),
            new BooleanOption("Шипы 3", false),
            new BooleanOption("Незеритка", false),
            new BooleanOption("Читерский рай", false),
            new BooleanOption("Лук", false),
            new BooleanOption("Классик", false),
            new BooleanOption("Тотемы", false),
            new BooleanOption("Нодебафф", false)
    );
    private double lastPosX, lastPosY, lastPosZ;
    public AutoDuel() {
        addSettings(mode);
    }


    private final List<String> sent = Lists.newArrayList();

    private final Counter counter = Counter.create();
    private final Counter counter2 = Counter.create();
    private final Counter counterChoice = Counter.create();
    private final Counter counterTo = Counter.create();


    private final Listener<EventUpdate> onUpdate = event -> {
        final List<String> players = getOnlinePlayers();

        double distance = Math.sqrt(Math.pow(lastPosX - mc.player.getPosX(), 2)
                + Math.pow(lastPosY - mc.player.getPosY(), 2)
                + Math.pow(lastPosZ - mc.player.getPosZ(), 2));

        if (distance > 500) {
            toggle();
        }

        lastPosX = mc.player.getPosX();
        lastPosY = mc.player.getPosY();
        lastPosZ = mc.player.getPosZ();



        if (counter2.hasReached(800L * players.size())) {
            sent.clear();
            counter2.reset();
        }

        for (final String player : players) {
            if (!sent.contains(player) && !player.equals(mc.session.getProfile().getName())) {
                if (counter.hasReached(1000)) {
                    mc.player.sendChatMessage("/duel " + player);
                    sent.add(player);
                    counter.reset();
                }
            }
        }


        if (mc.player.openContainer instanceof ChestContainer chest) {
            if (mc.currentScreen.getTitle().getString().contains("Выбор набора (1/1)")) {
                for (int i = 0; i < chest.getLowerChestInventory().getSizeInventory(); i++) {
                    final List<Integer> slotsID = new ArrayList<>();

                    int index = 0;

                    for (BooleanOption value : mode.getValues()) {
                        if (!value.getValue()) {
                            index++;
                            continue;
                        }

                        slotsID.add(index);
                        index++;
                    }


                    Collections.shuffle(slotsID);
                    final int slotID = slotsID.get(0);

                    if (counterChoice.hasReached(150)) {
                        mc.playerController.windowClick(chest.windowId, slotID, 0, ClickType.QUICK_MOVE, mc.player);
                        counterChoice.reset();
                    }
                }
            } else if (mc.currentScreen.getTitle().getString().contains("Настройка поединка")) {
                if (counterTo.hasReached(150)) {
                    mc.playerController.windowClick(chest.windowId, 0, 0, ClickType.QUICK_MOVE, mc.player);
                    counterTo.reset();
                }
            }
        }


    };

    private final Listener<EventPacket> onPacket = event -> {
        if (event.isReceivePacket()) {
            IPacket<?> packet = event.getPacket();

            if (packet instanceof SChatPacket chat) {
                final String text = chat.getChatComponent().getString().toLowerCase();
                if ((text.contains("начало") && text.contains("через") && text.contains("секунд!")) || (text.equals("дуэли » во время поединка запрещено использовать команды"))) {
                    toggle();
                }
            }
        }
    };

    private List<String> getOnlinePlayers() {
        return mc.player.connection.getPlayerInfoMap().stream()
                .map(NetworkPlayerInfo::getGameProfile)
                .map(GameProfile::getName)
                .filter(profileName -> pattern.matcher(profileName).matches())
                .collect(Collectors.toList());
    }

    @Override
    public void onEvent(Event event) {
        if (event instanceof EventUpdate) {
            onUpdate.onEvent((EventUpdate) event);
        } else if (event instanceof EventPacket) {
            onPacket.onEvent((EventPacket) event);
        }
    }
}