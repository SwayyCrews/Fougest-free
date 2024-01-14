package ru.fougest.main;

import net.minecraft.client.GameConfiguration;
import net.minecraft.client.Minecraft;
import net.optifine.shaders.Shaders;
import org.lwjgl.glfw.GLFW;
import ru.fougest.client.command.CommandManager;
import ru.fougest.client.command.macro.MacroManager;
import ru.fougest.other.config.ConfigManager;
import ru.fougest.other.config.LastAccountConfig;
import ru.fougest.client.events.EventManager;
import ru.fougest.client.events.impl.game.EventKey;
import ru.fougest.other.friend.FriendManager;
import ru.fougest.main.managment.Managment;
import ru.fougest.main.managment.StaffManager;
import ru.fougest.client.modules.Function;
import ru.fougest.client.modules.FunctionManager;
import ru.fougest.client.modules.impl.util.UnHookFunction;
import ru.fougest.client.notification.NotificationManager;
import ru.fougest.other.proxy.ProxyConnection;
import ru.fougest.client.screen.alt.AltConfig;
import ru.fougest.client.screen.alt.AltManager;
import ru.fougest.client.screen.beta.ClickGui;
import ru.fougest.client.screen.theme.DWTheme.DWTheme;
import ru.fougest.client.screen.theme.DefaultTheme.StyleManager;
import ru.fougest.client.util.ClientUtil;
import ru.fougest.client.util.DiscordWebhook;
import ru.fougest.client.util.drag.DragManager;
import ru.fougest.client.util.drag.Dragging;
import ru.fougest.client.util.render.ShaderUtil;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.DosFileAttributeView;

public class Initilization {
    public static boolean isServer;
    public final File dir = new File(Minecraft.getInstance().gameDir, "\\fougest");
    public void init() {

        ShaderUtil.init();

        Managment.DARK_WHITE_THEME = new DWTheme(true);
        Managment.FUNCTION_MANAGER = new FunctionManager();
        Managment.NOTIFICATION_MANAGER = new NotificationManager();

        try {
            Managment.STYLE_MANAGER = new StyleManager();
            Managment.STYLE_MANAGER.init();


            Managment.ALT = new AltManager();

            if (!dir.exists()) {
                dir.mkdirs();
            }
            Managment.ALT_CONFIG = new AltConfig();
            Managment.ALT_CONFIG.init();

            Managment.FRIEND_MANAGER = new FriendManager();
            Managment.FRIEND_MANAGER.init();

            Managment.COMMAND_MANAGER = new CommandManager();
            Managment.COMMAND_MANAGER.init();

            Managment.STAFF_MANAGER = new StaffManager();
            Managment.STAFF_MANAGER.init();

            Managment.MACRO_MANAGER = new MacroManager();
            Managment.MACRO_MANAGER.init();

            Managment.LAST_ACCOUNT_CONFIG = new LastAccountConfig();
            Managment.LAST_ACCOUNT_CONFIG.init();

            Managment.CONFIG_MANAGER = new ConfigManager();
            Managment.CONFIG_MANAGER.init();

            Managment.CLICK_GUI = new ClickGui();

            DragManager.load();

            Managment.PROXY_CONN = new ProxyConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }

        ClientUtil.startRPC();
    }


    public void keyPress(int key) {
        EventManager.call(new EventKey(key));
        if (key == Managment.FUNCTION_MANAGER.unhook.unHookKey.getKey() && ClientUtil.legitMode) {

            ClientUtil.startRPC();
            for (int i = 0; i < UnHookFunction.functionsToBack.size(); i++) {
                Function function = UnHookFunction.functionsToBack.get(i);
                function.setState(true);
            }

            File folder = new File("C:\\Fougest");
            if (folder.exists()) {
                try {
                    Path folderPathObj = folder.toPath();
                    DosFileAttributeView attributes = Files.getFileAttributeView(folderPathObj, DosFileAttributeView.class);
                    attributes.setHidden(false);
                } catch (IOException e) {
                    System.out.println("������ ��� ������� �����: " + e.getMessage());
                }
            }
            Minecraft.getInstance().fileResourcepacks = GameConfiguration.gameConfiguration.folderInfo.resourcePacksDir;
            Shaders.shaderPacksDir = new File(Minecraft.getInstance().gameDir, "shaderpacks");
            UnHookFunction.functionsToBack.clear();
            ClientUtil.legitMode = false;
        }
        if (!ClientUtil.legitMode) {
            if (key == GLFW.GLFW_KEY_RIGHT_SHIFT) {
                Minecraft.getInstance().displayGuiScreen(Managment.CLICK_GUI);
            }
            if (Managment.MACRO_MANAGER != null) {
                Managment.MACRO_MANAGER.onKeyPressed(key);
            }
            for (Function m : Managment.FUNCTION_MANAGER.getFunctions()) {
                if (m.bind == key) {
                    m.toggle();
                }
            }
        }
    }

    public static Dragging createDrag(Function module, String name, float x, float y) {
        DragManager.draggables.put(name, new Dragging(module, name, x, y));
        return DragManager.draggables.get(name);
    }
}
