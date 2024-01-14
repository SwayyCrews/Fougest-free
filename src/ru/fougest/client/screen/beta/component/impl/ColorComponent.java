package ru.fougest.client.screen.beta.component.impl;

import com.mojang.blaze3d.matrix.MatrixStack;
import ru.fougest.client.modules.settings.imp.ColorSetting;
import ru.fougest.client.screen.beta.component.ColorWindow;
import ru.fougest.client.screen.theme.DWTheme.DWTheme;
import ru.fougest.client.util.font.Fonts;
import ru.fougest.client.util.render.RenderUtil;

public class ColorComponent extends Component {

    public static ColorWindow opened;
    public ColorSetting option;
    public ColorWindow setted;

    public ColorComponent(ColorSetting option) {
        this.option = option;
        setted = new ColorWindow(this);
        this.s = option;
    }

    @Override
    public void drawComponent(MatrixStack matrixStack, int mouseX, int mouseY) {
        Fonts.rubikSemiBold[14].drawString(matrixStack, option.getName(), x + 5, y + height / 2f - 1, DWTheme.textColor);
        float size = 8;
        RenderUtil.Render2D.drawRoundedCorner(x + width - 10 - size /2f, y + height / 2f - size /2f, size,size, 3, option.color);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        float size = 12;
        if (RenderUtil.isInRegion(mouseX,mouseY, x + width - 10 - size /2f, y + height / 2f - size /2f, size,size)) {
            if (setted == opened) {
                opened = null;
                return;
            }
            opened = setted;
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int mouseButton) {

    }

    @Override
    public void keyTyped(int keyCode, int scanCode, int modifiers) {

    }

    @Override
    public void charTyped(char codePoint, int modifiers) {

    }

    @Override
    public void onConfigUpdate() {
        super.onConfigUpdate();
        setted.onConfigUpdate();
    }
}
