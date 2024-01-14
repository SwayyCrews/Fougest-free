package ru.fougest.client.modules.impl.render;

import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.shader.Framebuffer;
import ru.fougest.client.events.Event;
import ru.fougest.client.events.impl.player.EventModelRender;
import ru.fougest.client.events.impl.render.EventRender;
import ru.fougest.client.modules.Function;
import ru.fougest.client.modules.FunctionAnnotation;
import ru.fougest.client.modules.Type;
import ru.fougest.client.util.render.BloomHelper;
import ru.fougest.client.util.render.OutlineUtils;
import ru.fougest.client.util.render.ShaderUtil;

@FunctionAnnotation(name = "GlowESP", type = Type.Render)
public class GlowESP extends Function {

    public static Framebuffer framebuffer = new Framebuffer(1,1,true,false);

    @Override
    public void onEvent(Event event) {
        if (event instanceof EventModelRender e) {
            framebuffer.bindFramebuffer(false);
            e.render();
            framebuffer.unbindFramebuffer();
            mc.getFramebuffer().bindFramebuffer(true);
        }

        if (event instanceof EventRender e) {
            if (e.isRender2D()) {
                GlStateManager.enableBlend();

                OutlineUtils.registerRenderCall(() -> {
                    framebuffer.bindFramebufferTexture();
                    ShaderUtil.drawQuads();
                });

                BloomHelper.registerRenderCallHand(() -> {
                    framebuffer.bindFramebufferTexture();
                    ShaderUtil.drawQuads();
                });



                OutlineUtils.draw(1, -1);
                BloomHelper.drawC(10, 1, true, -1, 2);
                OutlineUtils.setupBuffer(framebuffer);

                mc.getFramebuffer().bindFramebuffer(true);
            }
        }

    }
}
