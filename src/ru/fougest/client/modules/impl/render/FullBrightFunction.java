package ru.fougest.client.modules.impl.render;

import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import ru.fougest.client.events.Event;
import ru.fougest.client.events.impl.player.EventUpdate;
import ru.fougest.client.modules.Function;
import ru.fougest.client.modules.FunctionAnnotation;
import ru.fougest.client.modules.Type;
import ru.fougest.client.modules.settings.imp.ModeSetting;

/**
 * @author dedinside
 * @since 04.06.2023
 */
@FunctionAnnotation(name = "FullBright", type = Type.Render)
public class FullBrightFunction extends Function {
    public ModeSetting fbType = new ModeSetting("Type", "Gamma", "Gamma", "Potion");

    public FullBrightFunction() {
        addSettings(fbType);
    }

    @Override
    public void onEvent(Event event) {
        if (event instanceof EventUpdate && fbType.is("Potion")) {
            mc.player.addPotionEffect(new EffectInstance(Effects.NIGHT_VISION, 1337, 1));
        }
    }

    @Override
    protected void onEnable() {
        super.onEnable();
        if (fbType.is("Gamma")) {
            mc.gameSettings.gamma = 1_000_000;
        }
    }

    @Override
    protected void onDisable() {
        if (fbType.is("Gamma")) {
            mc.gameSettings.gamma = 1;
        } else {
            mc.player.removePotionEffect(Effects.NIGHT_VISION);
        }
        super.onDisable();
    }
}
