package ru.fougest.client.modules.impl.render;

import net.minecraft.client.MainWindow;
import net.minecraft.client.settings.PointOfView;
import net.minecraft.util.math.MathHelper;
import ru.fougest.client.events.Event;
import ru.fougest.client.events.impl.render.EventRender;
import ru.fougest.main.managment.Managment;
import ru.fougest.client.modules.Function;
import ru.fougest.client.modules.FunctionAnnotation;
import ru.fougest.client.modules.Type;
import ru.fougest.client.screen.theme.DefaultTheme.Style;
import ru.fougest.client.util.render.animation.AnimationMath;

import static ru.fougest.client.util.render.ColorUtil.*;
import static ru.fougest.client.util.render.RenderUtil.Render2D.*;

/**
 * @author dedinside
 * @since 29.06.2023
 */
@FunctionAnnotation(name = "Crosshair", type = Type.Render)
public class Crosshair extends Function {
    private float circleAnimation = 0.0F;

    @Override
    public void onEvent(Event event) {
        if (event instanceof EventRender e) {
            handleCrosshairRender();
        }
    }

    private void handleCrosshairRender() {
        if (mc.gameSettings.getPointOfView() != PointOfView.FIRST_PERSON) {
            return;
        }

        final MainWindow mainWindow = mc.getMainWindow();

        final float x = (float) mainWindow.scaledWidth() / 2.0F;
        final float y = (float) mainWindow.scaledHeight() / 2.0F;

        final float calculateCooldown = mc.player.getCooledAttackStrength(1.0F);
        final float endRadius = MathHelper.clamp(calculateCooldown * 360, 0, 360);

        this.circleAnimation = AnimationMath.lerp(this.circleAnimation, -endRadius, 4);

        final int mainColor = rgba(30, 30, 30, 255);
        Style style = Managment.STYLE_MANAGER.getCurrentStyle();

        drawCircle(x, y, 0, 360, 3.5f, 3, false, mainColor);
        drawCircle(x, y, 0, circleAnimation, 3.5f, 3, false, style);
    }
}
