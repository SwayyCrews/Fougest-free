package ru.fougest.client.screen.beta.component.impl;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.util.text.TextFormatting;
import org.lwjgl.glfw.GLFW;
import ru.fougest.main.managment.Managment;
import ru.fougest.client.modules.Function;
import ru.fougest.client.modules.settings.Setting;
import ru.fougest.client.modules.settings.imp.*;
import ru.fougest.client.screen.beta.ClickGui;
import ru.fougest.client.screen.theme.DWTheme.DWTheme;
import ru.fougest.client.util.ClientUtil;
import ru.fougest.client.util.font.Fonts;
import ru.fougest.client.util.render.ColorUtil;
import ru.fougest.client.util.render.RenderUtil;
import ru.fougest.client.util.render.SmartScissor;
import ru.fougest.client.util.render.animation.AnimationMath;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static ru.fougest.client.util.render.RenderUtil.reAlphaInt;

public class ModuleComponent extends Component {

    public Function function;

    public List<Component> components = new ArrayList<>();

    public ModuleComponent(Function function) {
        this.function = function;
        for (Setting setting : function.getSettingList()) {
            switch (setting.getType()) {
                case BOOLEAN_OPTION -> components.add(new BooleanComponent((BooleanOption) setting));
                case SLIDER_SETTING -> components.add(new SliderComponent((SliderSetting) setting));
                case MODE_SETTING -> components.add(new ModeComponent((ModeSetting) setting));
                case COLOR_SETTING -> components.add(new ColorComponent((ColorSetting) setting));
                case MULTI_BOX_SETTING -> components.add(new ListComponent((MultiBoxSetting) setting));
                case BIND_SETTING -> components.add(new BindComponent((BindSetting) setting));
                case TEXT_SETTING -> components.add(new TextComponent((TextSetting) setting));
            }
        }
    }

    public float animationToggle;
    public static ModuleComponent binding;

    @Override
    public void drawComponent(MatrixStack matrixStack, int mouseX, int mouseY) {

        float totalHeight = 2;
        for (Component component : components) {
            if (component.s != null && component.s.visible()) {
                totalHeight += component.height;
            }
        }

        float off = 2f;

        components.forEach(c -> {
            c.function = function;
            c.parent = parent;
        });

        animationToggle = AnimationMath.lerp(animationToggle, function.state ? 1 : 0, 10);
        RenderUtil.Render2D.drawRoundedRect(x, y, width, height + totalHeight, 5f, DWTheme.typeColor);

        RenderUtil.Render2D.drawRect(x + 5, y + 18, width - 10, 1f, DWTheme.lineColor);
        RenderUtil.Render2D.drawShadow(x + 5, y + 18, width - 10, 1f,3, DWTheme.lineColor);
        Fonts.rubikSemiBold[14].drawString(matrixStack, function.name, x + 7.5f, y + 9f, DWTheme.textColor);

        if (function.bind == 0) {
            Fonts.rubikSemiBold[14].drawString(matrixStack, "[ " + "]", x + width - 20 - Fonts.rubikSemiBold[14].getWidth("[ " + "]") + 15, y + 9, new Color(33,33,33).getRGB());
        } else {
            Fonts.rubikSemiBold[14].drawString(matrixStack, "[" + GLFW.glfwGetKeyName(function.bind, 0) + "]", x + width - 20 - Fonts.rubikSemiBold[14].getWidth("[" + GLFW.glfwGetKeyName(function.bind, 0) + "]") + 15, y + 9, new Color(33,33,33).getRGB());
        }

        int color = ColorUtil.interpolateColor(DWTheme.bgColor, RenderUtil.IntColor.rgba(74, 166, 218, 255), animationToggle);
        RenderUtil.Render2D.drawShadow(x + 7 + Fonts.rubikSemiBold[14].getWidth("Включен"), y + 23 + off, 10, 10, 8, reAlphaInt(color, 50));
        RenderUtil.Render2D.drawRoundedRect(x + 7 + Fonts.rubikSemiBold[14].getWidth("Включен"), y + 23 + off, 10, 10, 2f, color);
        SmartScissor.push();

        SmartScissor.setFromComponentCoordinates(x + 7 + Fonts.rubikSemiBold[14].getWidth("Включен"), y + 23 + off, 10 * animationToggle, 10);
        Fonts.icons[12].drawString(matrixStack, "A", x + 9 + Fonts.rubikSemiBold[14].getWidth("Включен"), y + 28 + off, -1);
        SmartScissor.unset();
        SmartScissor.pop();

        Fonts.rubikSemiBold[14].drawString(matrixStack, "Включен", x + 5f, y + 27f + off, DWTheme.textColor);

        float offsetY = 0;
        for (Component component : components) {
            if (component.s != null && component.s.visible()) {
                component.setPosition(x, y + height + offsetY, width, 20);
                component.drawComponent(matrixStack, mouseX, mouseY);
                offsetY += component.height;
            }
        }

    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (RenderUtil.isInRegion(mouseX, mouseY, x + 5, y + 20, width - 10, 17) && mouseButton <= 1) {
            function.toggle();
        }

        if (binding == this && mouseButton > 2) {
            function.bind = -100 + mouseButton;
            Managment.NOTIFICATION_MANAGER.add("Модуль " + TextFormatting.GRAY + binding.function.name + TextFormatting.WHITE + " был забинжен на кнопку " + ClientUtil.getKey(-100 + mouseButton), "Module", 5);
            binding = null;
        }

        if (RenderUtil.isInRegion(mouseX, mouseY, x + 5, y, width - 10, 20)) {
            if (mouseButton == 2) {
                ClickGui.typing = false;
                binding = this;
            }
        }
        components.forEach(component -> component.mouseClicked(mouseX, mouseY, mouseButton));
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
        components.forEach(component -> component.mouseReleased(mouseX, mouseY, mouseButton));
    }

    @Override
    public void keyTyped(int keyCode, int scanCode, int modifiers) {
        components.forEach(component -> component.keyTyped(keyCode, scanCode, modifiers));
    }

    @Override
    public void charTyped(char codePoint, int modifiers) {
        components.forEach(component -> component.charTyped(codePoint, modifiers));
    }
}
