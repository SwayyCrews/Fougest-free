package ru.fougest.client.screen.beta.component.theme;

import com.mojang.blaze3d.matrix.MatrixStack;
import ru.fougest.main.managment.Managment;
import ru.fougest.client.screen.theme.DefaultTheme.Style;
import ru.fougest.client.util.render.*;
import ru.fougest.client.util.render.animation.AnimationMath;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ThemeDrawing {

    private List<ThemeObject> objects = new ArrayList<>();

    float animation;
    public ThemeDrawing() {
        Style custom = Managment.STYLE_MANAGER.styles.get(Managment.STYLE_MANAGER.styles.size() - 1);
        for (Style style : Managment.STYLE_MANAGER.styles) {
            if (style.name.equalsIgnoreCase("Свой цвет")) continue;
            objects.add(new ThemeObject(style));
        }
        float[] rgb = RenderUtil.IntColor.rgb(custom.colors[edit]);
        float[] hsb = Color.RGBtoHSB((int) (rgb[0] * 255), (int) (rgb[1] * 255), (int) (rgb[2] * 255), null);
        this.hsb = hsb[0];
        this.satur = hsb[1];
        this.brithe = hsb[2];
    }

    boolean colorOpen;
    public float openAnimation;

    public int edit;

    float x,y,width,height;

    float hsb;
    float satur;
    float brithe;

    public void draw(MatrixStack stack, int mouseX, int mouseY, float x,float y,float width ,float height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        float offsetX = 10;
        float offsetY = 10;

        openAnimation = AnimationMath.lerp(openAnimation, colorOpen ? 1 : 0, 15);


        GaussianBlur.startBlur();
        for (ThemeObject object : objects) {
            object.x = x + offsetX;
            object.y = y + offsetY;
            object.width = 154 / 2f;
            object.height = 60;
            offsetX += object.width + 1 / 0.1f;
            if (offsetX > 450 - 100 - object.width + 1 / 0.1f) {
                offsetX = 10;
                offsetY += 65;
            }
        }
        GaussianBlur.endBlur(20, 2);
        for (ThemeObject object : objects) {
            object.draw(stack,mouseX,mouseY);
        }
    }

    public void click(int mouseX, int mouseY, int button) {
        for (ThemeObject object : objects) {
            if (RenderUtil.isInRegion(mouseX,mouseY, object.x,object.y, object.width, object.height)) {
                Managment.STYLE_MANAGER.setCurrentStyle(object.style);
            }
        }
    }


}
