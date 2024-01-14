package ru.fougest.main.managment;

import ru.fougest.client.command.CommandManager;
import ru.fougest.client.command.macro.MacroManager;
import ru.fougest.other.config.ConfigManager;
import ru.fougest.other.config.LastAccountConfig;
import ru.fougest.other.friend.FriendManager;
import ru.fougest.client.modules.FunctionManager;
import ru.fougest.client.notification.NotificationManager;
import ru.fougest.other.proxy.ProxyConnection;
import ru.fougest.client.screen.alt.AltConfig;
import ru.fougest.client.screen.alt.AltManager;
import ru.fougest.client.screen.beta.ClickGui;
import ru.fougest.client.screen.theme.DWTheme.DWTheme;
import ru.fougest.client.screen.theme.DefaultTheme.StyleManager;
import ru.fougest.client.util.UserProfile;

public class Managment {

    public static FunctionManager FUNCTION_MANAGER;
    public static CommandManager COMMAND_MANAGER;
    public static FriendManager FRIEND_MANAGER;
    public static MacroManager MACRO_MANAGER;
    public static LastAccountConfig LAST_ACCOUNT_CONFIG;

    public static StaffManager STAFF_MANAGER;
    public static ClickGui CLICK_GUI;
    public static ConfigManager CONFIG_MANAGER;
    public static StyleManager STYLE_MANAGER;
    public static UserProfile USER_PROFILE;
    public static NotificationManager NOTIFICATION_MANAGER;
    public static DWTheme DARK_WHITE_THEME;
    public static AltManager ALT;
    public static AltConfig ALT_CONFIG;

    public static ProxyConnection PROXY_CONN;
}
