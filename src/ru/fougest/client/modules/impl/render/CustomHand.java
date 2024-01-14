package ru.fougest.client.modules.impl.render;

import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.settings.PointOfView;
import net.minecraft.client.shader.Framebuffer;
import ru.fougest.client.events.Event;
import ru.fougest.client.events.impl.render.EventRender;
import ru.fougest.client.modules.Function;
import ru.fougest.client.modules.FunctionAnnotation;
import ru.fougest.client.modules.Type;
import ru.fougest.client.util.render.*;

@FunctionAnnotation(name = "Custom Hand", type = Type.Render)
public class CustomHand extends Function {

    public static Framebuffer handBuffer = new Framebuffer(1,1, true,false);

    @Override
    public void onEvent(Event event) {
        if (event instanceof EventRender e) {
            if (e.isRender2D() && mc.gameSettings.getPointOfView() == PointOfView.FIRST_PERSON) {
                GlStateManager.pushMatrix();
                GlStateManager.enableBlend();
                OutlineUtils.registerRenderCall(() -> {
                    handBuffer.bindFramebufferTexture();
                    ShaderUtil.drawQuads();
                });
                BloomHelper.registerRenderCall(() -> {
                    handBuffer.bindFramebufferTexture();
                    ShaderUtil.drawQuads();
                });
                BloomHelper.drawC(10, 1f, true, ColorUtil.getColorStyle(0), 3);
                OutlineUtils.draw(1, ColorUtil.getColorStyle(0));

                GaussianBlur.startBlur();
                handBuffer.bindFramebufferTexture();
                ShaderUtil.drawQuads();
                GaussianBlur.endBlur(10,3);


                GlStateManager.popMatrix();
            }
        }
    }
}
