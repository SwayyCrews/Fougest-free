package ru.fougest.client.screen.beta.component.theme;

import com.mojang.blaze3d.matrix.MatrixStack;
import org.joml.Vector4i;
import ru.fougest.main.managment.Managment;
import ru.fougest.client.screen.theme.DWTheme.DWTheme;
import ru.fougest.client.screen.theme.DefaultTheme.Style;
import ru.fougest.client.util.font.Fonts;
import ru.fougest.client.util.render.RenderUtil;
import ru.fougest.client.util.render.animation.AnimationMath;

public class ThemeObject {

    public float x, y, width, height;

    public Style style;
    public float anim;



    public ThemeObject(Style style) {
        this.style = style;
    }



    public void draw(MatrixStack stack, int mouseX, int mouseY) {
        Vector4i colors = new Vector4i(
                style.colors[0],
                style.colors[0],
                style.colors[1],
                style.colors[1]
        );

        if (style.name.equalsIgnoreCase("Разно цветный")) {
            colors = new Vector4i(
                    style.getColor(0),
                    style.getColor(0),
                    style.getColor(90),
                    style.getColor(90)
            );
        }
        Vector4i finalColors = colors;
        anim = AnimationMath.lerp(anim,  Managment.STYLE_MANAGER.getCurrentStyle() == style ? 1 : RenderUtil.isInRegion(mouseX,mouseY, x,y,width,height) ? 0.7f : 0, 5);


        RenderUtil.Render2D.drawRoundOutline(x, y, width, height,5,0,DWTheme.typeColor, new Vector4i(
                Managment.STYLE_MANAGER.getCurrentStyle() == style ? RenderUtil.reAlphaInt(DWTheme.lineColor, (int) (255 * anim)) : RenderUtil.reAlphaInt(DWTheme.typeColor, (int) (255 * anim)),
                Managment.STYLE_MANAGER.getCurrentStyle() == style ? RenderUtil.reAlphaInt(DWTheme.lineColor, (int) (255 * anim)) : RenderUtil.reAlphaInt(DWTheme.typeColor, (int) (255 * anim)),
                Managment.STYLE_MANAGER.getCurrentStyle() == style ? RenderUtil.reAlphaInt(DWTheme.lineColor, (int) (255 * anim)) : RenderUtil.reAlphaInt(DWTheme.typeColor, (int) (255 * anim)),
                Managment.STYLE_MANAGER.getCurrentStyle() == style ? RenderUtil.reAlphaInt(DWTheme.lineColor, (int) (255 * anim)) : RenderUtil.reAlphaInt(DWTheme.typeColor, (int) (255 * anim))
        ));
        RenderUtil.Render2D.drawRoundedRect(x + 59, y + 47, 15, 10,3,finalColors.w);
        RenderUtil.Render2D.drawRoundedRect(x + 42, y + 47, 15, 10,3,finalColors.x);

        float off = 0;
        for (String ss : style.name.split(" ")) {
            Fonts.rubikSemiBold[16].drawString(stack, ss, x + 5, y + 7 + off, DWTheme.textColor);
            off += 10;
        }
    }
}
