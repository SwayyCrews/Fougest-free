package ru.fougest.client.modules.impl.render;

import net.optifine.shaders.Shaders;
import ru.fougest.client.events.Event;
import ru.fougest.client.events.impl.render.EventRender;
import ru.fougest.client.modules.Function;
import ru.fougest.client.modules.FunctionAnnotation;
import ru.fougest.client.modules.Type;
import ru.fougest.client.modules.settings.imp.BooleanOption;
import ru.fougest.client.modules.settings.imp.SliderSetting;
import ru.fougest.client.util.ClientUtil;

@FunctionAnnotation(name = "Custom Fog", type = Type.Render)
public class CustomFog extends Function {


    public SliderSetting power = new SliderSetting("Сила", 20, 5,50, 1);
    public BooleanOption confirm = new BooleanOption("я подтверждаю что у меня мощный ПК", false);

    public CustomFog() {
        addSettings(power,confirm);
    }

    public boolean firstStart;

    @Override
    protected void onEnable() {
        super.onEnable();
        if (!confirm.get()) {
            ClientUtil.sendMesage("Вы не нажали на галочку!");
            setState(false);
        } else {
            Shaders.setShaderPack(Shaders.SHADER_PACK_NAME_DEFAULT);
        }
    }

    @Override
    public void onEvent(Event event) {
        if (event instanceof EventRender e) {
            if (e.isRender2D()) {

            }
        }
    }

    public int getDepth() {
        return 6;
    }

}
