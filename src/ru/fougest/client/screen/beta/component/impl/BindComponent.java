package ru.fougest.client.screen.beta.component.impl;

import com.mojang.blaze3d.matrix.MatrixStack;
import ru.fougest.client.modules.settings.imp.BindSetting;
import ru.fougest.client.screen.theme.DWTheme.DWTheme;
import ru.fougest.client.util.ClientUtil;
import ru.fougest.client.util.font.Fonts;
import ru.fougest.client.util.render.RenderUtil;

public class BindComponent extends Component {

    public BindSetting option;
    boolean bind;


    public BindComponent(BindSetting option) {
        this.option = option;
        this.s = option;
    }

    @Override
    public void drawComponent(MatrixStack matrixStack, int mouseX, int mouseY) {

        height -= 3;

        String bindString = option.getKey() == 0 ? "NONE" : ClientUtil.getKey(option.getKey());

        if (bindString == null) {
            bindString = "";
        }

        float width = Fonts.rubikSemiBold[14].getWidth(bindString) + 4;
        RenderUtil.Render2D.drawRoundedCorner(x + 5, y + 2, width, 10, 2, DWTheme.bgColor);
        Fonts.rubikSemiBold[14].drawCenteredString(matrixStack, bindString, x + 5 + (width / 2), y + 6, DWTheme.textColor);
        Fonts.rubikSemiBold[14].drawString(matrixStack, option.getName(), x + 5 + width + 3, y + 6, DWTheme.textColor);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (bind && mouseButton > 1) {
            option.setKey(-100 + mouseButton);
            bind = false;
        }
        if (isHovered(mouseX, mouseY) && mouseButton == 0) {
            bind = true;
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int mouseButton) {

    }

    @Override
    public void keyTyped(int keyCode, int scanCode, int modifiers) {
        if (bind) {
            if (keyCode == 261) {
                option.setKey(0);
                bind = false;
                return;
            }
            option.setKey(keyCode);
            bind = false;
        }
    }

    @Override
    public void charTyped(char codePoint, int modifiers) {

    }
}
