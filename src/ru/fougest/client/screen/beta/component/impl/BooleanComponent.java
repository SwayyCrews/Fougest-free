package ru.fougest.client.screen.beta.component.impl;

import com.mojang.blaze3d.matrix.MatrixStack;
import ru.fougest.client.modules.settings.imp.BooleanOption;
import ru.fougest.client.screen.theme.DWTheme.DWTheme;
import ru.fougest.client.util.font.Fonts;
import ru.fougest.client.util.render.ColorUtil;
import ru.fougest.client.util.render.RenderUtil;
import ru.fougest.client.util.render.SmartScissor;
import ru.fougest.client.util.render.animation.AnimationMath;

import static ru.fougest.client.util.render.RenderUtil.reAlphaInt;

public class BooleanComponent extends Component {

    public BooleanOption option;

    public BooleanComponent(BooleanOption option) {
        this.option = option;
        this.s = option;
    }

    public float animationToggle;

    @Override
    public void drawComponent(MatrixStack matrixStack, int mouseX, int mouseY) {
        height = 15;
        float off = 0.5f;
        animationToggle = AnimationMath.lerp(animationToggle, option.get() ? 1 : 0, 10);

        int color = ColorUtil.interpolateColor(DWTheme.bgColor,
                RenderUtil.IntColor.rgba(74, 166, 218, 255), animationToggle);

        RenderUtil.Render2D.drawShadow(x + 7 + Fonts.rubikSemiBold[14].getWidth(option.getName()), y + 1 + off, 10, 10, 8, reAlphaInt(color, 50));
        RenderUtil.Render2D.drawRoundedRect(x + 7 + Fonts.rubikSemiBold[14].getWidth(option.getName()), y + 1 + off, 10, 10, 2f, color);
        SmartScissor.push();

        SmartScissor.setFromComponentCoordinates(x + 7 + Fonts.rubikSemiBold[14].getWidth(option.getName()), y + 1 + off, 10 * animationToggle, 10);
        Fonts.icons[12].drawString(matrixStack, "A", x + 9 + Fonts.rubikSemiBold[14].getWidth(option.getName()), y + 6 + off, DWTheme.textColor);
        SmartScissor.unset();
        SmartScissor.pop();

        Fonts.rubikSemiBold[14].drawString(matrixStack, option.getName(), x + 5f, y + 4.5f + off, DWTheme.textColor);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (RenderUtil.isInRegion(mouseX, mouseY, x, y, width - 10, 15)) {

            option.toggle();
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
}
