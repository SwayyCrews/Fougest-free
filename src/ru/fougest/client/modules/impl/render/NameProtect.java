package ru.fougest.client.modules.impl.render;

import net.minecraft.client.Minecraft;
import net.minecraft.util.text.ITextComponent;
import ru.fougest.client.events.Event;
import ru.fougest.client.modules.Function;
import ru.fougest.client.modules.FunctionAnnotation;
import ru.fougest.client.modules.Type;
import ru.fougest.client.modules.settings.imp.BooleanOption;
import ru.fougest.client.modules.settings.imp.TextSetting;
import ru.fougest.client.util.ClientUtil;

@FunctionAnnotation(name = "NameProtect", type = Type.Render)
public class NameProtect extends Function {

    public TextSetting name = new TextSetting("Ник", "Fougest");
    public BooleanOption friends = new BooleanOption("Друзья", false);


    public NameProtect() {
        addSettings(name, friends);
    }

    @Override
    public void onEvent(Event event) {

    }

    public String patch(String text) {
        String out = text;
        if (this.state) {
            out = text.replaceAll(Minecraft.getInstance().session.getUsername(), name.text);
        }
        return out;
    }

    public ITextComponent patchFriendTextComponent(ITextComponent text, String name) {
        ITextComponent out = text;
        if (this.friends.get() && this.state) {
            out = ClientUtil.replace(text, name, this.name.text);
        }
        return out;
    }
}
