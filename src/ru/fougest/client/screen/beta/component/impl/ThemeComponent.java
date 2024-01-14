package ru.fougest.client.screen.beta.component.impl;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.util.math.vector.Vector4f;
import org.joml.Vector4i;
import ru.fougest.main.managment.Managment;
import ru.fougest.client.screen.beta.component.ColorThemeWindow;
import ru.fougest.client.screen.theme.DWTheme.DWTheme;
import ru.fougest.client.screen.theme.DefaultTheme.Style;
import ru.fougest.client.util.font.Fonts;
import ru.fougest.client.util.render.ColorUtil;
import ru.fougest.client.util.render.RenderUtil;

import java.awt.*;

public class ThemeComponent extends Component {

    public Style config;
    public ColorThemeWindow[] colors = new ColorThemeWindow[2];
    public static ColorThemeWindow selected = null;

    public boolean opened;


    public ThemeComponent(Style config) {
        this.config = config;
        if (config.name.equalsIgnoreCase("Свой цвет")) {
            colors[0] = new ColorThemeWindow(new Color(config.colors[0]), new Vector4f(0, 0, 0, 0));
            colors[1] = new ColorThemeWindow(new Color(config.colors[1]), new Vector4f(0, 0, 0, 0));
        }
    }

    @Override
    public void onConfigUpdate() {
        super.onConfigUpdate();
        for (ColorThemeWindow colorThemeWindow : colors) {
            if (colorThemeWindow != null)
                colorThemeWindow.onConfigUpdate();
        }

        if (config.name.equalsIgnoreCase("Свой цвет")) {
            if (colors.length >= 2) {
                colors[0] = new ColorThemeWindow(new Color(config.colors[0]), new Vector4f(0, 0, 0, 0));
                colors[1] = new ColorThemeWindow(new Color(config.colors[1]), new Vector4f(0, 0, 0, 0));
            }
        }

    }

    @Override
    public void drawComponent(MatrixStack matrixStack, int mouseX, int mouseY) {
        if (opened) {
            height += 12;
        }
        if (!(colors[0] == null && colors[1] == null)) {
            config.colors[0] = colors[0].getColor();
            config.colors[1] = colors[1].getColor();
        }

        int color1 = config.name.contains("Разноцветный") ? ColorUtil.astolfo(10, 0, 0.7f, 1, 1) : config.getColor(0);
        int color2 = config.name.contains("Разноцветный") ? ColorUtil.astolfo(10, 90, 0.7f, 1, 1) : config.getColor(90);
        int color3 = config.name.contains("Разноцветный") ? ColorUtil.astolfo(10, 180, 0.7f, 1, 1) : config.getColor(180);
        int color4 = config.name.contains("Разноцветный") ? ColorUtil.astolfo(10, 270, 0.7f, 1, 1) : config.getColor(270);

        if (Managment.STYLE_MANAGER.getCurrentStyle() == config) {
            RenderUtil.Render2D.drawGradientRound(x + 3, y, width - 5, height, 6, color1, color2, color3, color4);
            RenderUtil.Render2D.drawRoundOutline(x, y - 3, width + 1, height + 1.5f + 4, 7,0, new Color(33,33,33, 0).getRGB(), new Vector4i(DWTheme.lineColor, DWTheme.lineColor, DWTheme.lineColor, DWTheme.lineColor));
        } else {
            RenderUtil.Render2D.drawGradientRound(x + 3, y, width - 5, height, 6, color1, color2, color3, color4);
        }
        RenderUtil.Render2D.drawRoundedCorner(x + 2f, y + 36, width - 3f, 15, new Vector4f(0,7.5f,0,7.5f), new Color(0,0,0,100).getRGB());
        Fonts.rubikSemiBold[16].drawCenteredString(matrixStack, "name", x + width / 2 - 1, y + 40, -1);

        if (opened) {
            RenderUtil.Render2D.drawRect(x + 10, y + 19, 8, 8, colors[0].getColor());
            RenderUtil.Render2D.drawRect(x + 10 + 12, y + 19, 8, 8, colors[1].getColor());
        }
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (isHovered(mouseX, mouseY, 20)) {
            Managment.STYLE_MANAGER.setCurrentStyle(config);
        }
        if (isHovered(mouseX, mouseY, 20) && config.name.equalsIgnoreCase("Свой цвет") && mouseButton == 1) {
            opened = !opened;
        }
        if (opened) {
            if (RenderUtil.isInRegion(mouseX, mouseY, x + 10, y + 19, 8, 8)) {
                if (selected == colors[0]) {
                    selected = null;
                    return;
                }
                selected = colors[0];
                selected.pos = new Vector4f(mouseX, mouseY, 0, 0);
            }
            if (RenderUtil.isInRegion(mouseX, mouseY, x + 10 + 12, y + 19, 8, 8)) {
                if (selected == colors[1]) {
                    selected = null;
                    return;
                }
                selected = colors[1];
                selected.pos = new Vector4f(mouseX, mouseY, 0, 0);
            }
        }
        if (selected != null)
            selected.click(mouseX, mouseY);
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
        if (selected != null)
            selected.unclick(mouseX, mouseY);
    }

    @Override
    public void keyTyped(int keyCode, int scanCode, int modifiers) {

    }

    @Override
    public void charTyped(char codePoint, int modifiers) {

    }
}
