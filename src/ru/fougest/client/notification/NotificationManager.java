package ru.fougest.client.notification;

import com.mojang.blaze3d.matrix.MatrixStack;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector4f;
import org.joml.Vector4i;
import ru.fougest.main.managment.Managment;
import ru.fougest.client.screen.theme.DWTheme.DWTheme;
import ru.fougest.client.util.animations.Animation;
import ru.fougest.client.util.animations.Direction;
import ru.fougest.client.util.animations.impl.DecelerateAnimation;
import ru.fougest.client.util.font.Fonts;
import ru.fougest.client.util.render.BloomHelper;
import ru.fougest.client.util.render.ColorUtil;
import ru.fougest.client.util.render.RenderUtil;
import ru.fougest.client.util.render.animation.AnimationMath;

import java.util.Comparator;
import java.util.concurrent.CopyOnWriteArrayList;

import static ru.fougest.client.util.IMinecraft.mc;
import static ru.fougest.client.util.render.ColorUtil.rgba;

public class NotificationManager {

    private final CopyOnWriteArrayList<Notification> notifications = new CopyOnWriteArrayList<>();

    public void add(String text, String content, int time) {
        notifications.add(new Notification(text, content, time));
    }


    public void draw(MatrixStack stack) {
        int yOffset = 0;
        for (Notification notification : notifications) {

            if (System.currentTimeMillis() - notification.getTime() > (notification.time2 * 1000L) - 300) {
                notification.animation.setDirection(Direction.BACKWARDS);
            } else {
                notification.yAnimation.setDirection(Direction.FORWARDS);
                notification.animation.setDirection(Direction.FORWARDS);
            }
            notification.alpha = (float) notification.animation.getOutput();
            if (System.currentTimeMillis() - notification.getTime() > notification.time2 * 1000L) {
                notification.yAnimation.setDirection(Direction.BACKWARDS);
            }
            if (notification.yAnimation.finished(Direction.BACKWARDS)) {
                notifications.remove(notification);
                continue;
            }
            float x = mc.getMainWindow().scaledWidth() - (Fonts.rubikSemiBold[14].getWidth(notification.getText()) + 8) - 10;
            float y = mc.getMainWindow().scaledHeight() - 30 -  (mc.player.getActivePotionEffects().stream().sorted(Comparator.comparing(EffectInstance::getDuration)).toList().size() * 16);
            notification.yAnimation.setEndPoint(yOffset);
            notification.yAnimation.setDuration(300);
            y -= (float) (notification.draw(stack) * notification.yAnimation.getOutput());
            notification.setX(x);
            notification.setY(AnimationMath.fast(notification.getY(), y, 15));
            yOffset++;
        }
    }


    private class Notification {
        @Getter
        @Setter
        private float x, y = mc.getMainWindow().scaledHeight() + 24;

        @Getter
        private String text;
        @Getter
        private String content;

        @Getter
        private long time = System.currentTimeMillis();

        public Animation animation = new DecelerateAnimation(500, 1, Direction.FORWARDS);
        public Animation yAnimation = new DecelerateAnimation(500, 1, Direction.FORWARDS);
        float alpha;
        int time2 = 3;

        public Notification(String text, String content, int time) {
            this.text = text;
            this.content = content;
            time2 = time;
        }

        public float draw(MatrixStack stack) {
            float width = Fonts.rubikSemiBold[14].getWidth(text) + 5;

            int firstColor = RenderUtil.reAlphaInt(Managment.STYLE_MANAGER.getCurrentStyle().getColor(0), (int) (255 * alpha));
            int secondColor = RenderUtil.reAlphaInt(Managment.STYLE_MANAGER.getCurrentStyle().getColor(100), (int) (255 * alpha));
            int thirdColor = RenderUtil.reAlphaInt(Managment.STYLE_MANAGER.getCurrentStyle().getColor(0), (int) (255 * alpha));
            int fourthColor = RenderUtil.reAlphaInt(Managment.STYLE_MANAGER.getCurrentStyle().getColor(100), (int) (255 * alpha));

            if (false)
                BloomHelper.registerRenderCall(() -> RenderUtil.Render2D.drawRoundedCorner(x + width, y, 4, 20, new Vector4f(0, 0, 3, 3), new Vector4i(firstColor, secondColor, thirdColor, fourthColor)));

            RenderUtil.Render2D.drawShadow(x - 10, y, width + 16, 23, 15, ColorUtil.rgba(33, 33, 33, 255 * alpha));
            RenderUtil.Render2D.drawRoundOutline(x - 10, y, width + 18, 23, 5,0, RenderUtil.reAlphaInt(DWTheme.typeColor, (int) (255 * alpha)), new Vector4i(RenderUtil.reAlphaInt(ColorUtil.getColorStyle(1), (int) (255 * alpha)), RenderUtil.reAlphaInt(ColorUtil.getColorStyle(50), (int) (255 * alpha)), RenderUtil.reAlphaInt(ColorUtil.getColorStyle(100), (int) (255 * alpha)), RenderUtil.reAlphaInt(ColorUtil.getColorStyle(150), (int) (255 * alpha))));
            RenderUtil.Render2D.drawRoundedCorner(x + 8.5f + 4, y + 1.5f, 1, 21, new Vector4f(0,0,0,0),new Vector4i(RenderUtil.reAlphaInt(ColorUtil.getColorStyle(1), (int) (255 * alpha)), RenderUtil.reAlphaInt(ColorUtil.getColorStyle(1), (int) (255 * alpha)), RenderUtil.reAlphaInt(ColorUtil.getColorStyle(1), (int) (255 * alpha)), RenderUtil.reAlphaInt(ColorUtil.getColorStyle(1), (int) (255 * alpha))));

            Fonts.rubikSemiBold[14].drawString(stack, content, x + 12 + 4, y + 5, RenderUtil.reAlphaInt(DWTheme.textColor, (int) (255 * alpha)));
            Fonts.rubikSemiBold[12].drawString(stack, text, x + 12 + 4, y + 16, RenderUtil.reAlphaInt(DWTheme.textColor, (int) (255 * alpha)));
            RenderUtil.Render2D.drawImage(new ResourceLocation("expensive/images/success.png"),x - 6, y + 4f, 16, 16, RenderUtil.reAlphaInt(DWTheme.textColor, (int) (255 * alpha)));

            return 25;
        }
    }
}
