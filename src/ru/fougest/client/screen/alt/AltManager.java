package ru.fougest.client.screen.alt;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Session;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.StringTextComponent;
import org.apache.commons.lang3.RandomStringUtils;
import org.joml.Vector4i;
import org.lwjgl.glfw.GLFW;
import ru.fougest.client.screen.theme.DWTheme.DWTheme;
import ru.fougest.client.util.font.Fonts;
import ru.fougest.client.util.render.*;
import ru.fougest.client.util.render.animation.AnimationMath;

import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import static baritone.api.utils.Helper.mc;

public class AltManager extends Screen {

    public AltManager() {
        super(new StringTextComponent(""));

    }

    public ArrayList<Account> accounts = new ArrayList<>();


    @Override
    protected void init() {
        super.init();
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        return super.mouseReleased(mouseX, mouseY, button);
    }


    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {

        if (keyCode == GLFW.GLFW_KEY_BACKSPACE) {
            if (!altName.isEmpty())
                altName = altName.substring(0, altName.length() - 1);
        }

        if (keyCode == GLFW.GLFW_KEY_ENTER) {
            if (!altName.isEmpty())
                accounts.add(new Account(altName));
            typing = false;
        }

        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean charTyped(char codePoint, int modifiers) {
        altName += Character.toString(codePoint);
        return super.charTyped(codePoint, modifiers);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        Vec2i fixed = ScaleMath.getMouse((int) mouseX, (int) mouseY);
        mouseX = fixed.getX();
        mouseY = fixed.getY();
        float x = width / 2f - 150, y = (327 / 2f);

        if (RenderUtil.isInRegion(mouseX, mouseY, x + 63, 664 / 2f, 249 / 2f, 46 / 2f)) {
            AltConfig.updateFile();
            accounts.add(new Account(RandomStringUtils.randomAlphabetic(8)));
        }
        if (RenderUtil.isInRegion(mouseX, mouseY, x + 63, 723 / 2f, 249 / 2f, 46 / 2f)) {
            if (!altName.isEmpty() && altName.length() < 20) {
                accounts.add(new Account(altName));
                AltConfig.updateFile();
            }

            typing = false;
        }
        if (RenderUtil.isInRegion(mouseX, mouseY, x + 21, y + 25 + 30, 419 / 2f, 23)) {

            typing = !typing;
        }

        float altX = 778 / 2f, altY = 298 / 2f;

        float iter = scrollAn;
        Iterator<Account> iterator = accounts.iterator();
        while (iterator.hasNext()) {
            Account account = iterator.next();
            float panWidth = 177;

            float acX = altX + 388;
            float acY = 442 / 2f + (iter * (37)) - 68;
            float acY1 = 629 / 2f + (iter * (37));

            if (RenderUtil.isInRegion(mouseX, mouseY, acX, acY, panWidth, 35)) {
                if (button == 0) {
                    mc.session = new Session(account.accountName, "", "", "mojang");
                } else {
                    iterator.remove(); // Безопасное удаление элемента
                    AltConfig.updateFile();
                }
            }

            iter++;
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public void init(Minecraft minecraft, int width, int height) {
        super.init(minecraft, width, height);
    }

    @Override
    public void tick() {
        super.tick();
    }

    public float scroll;
    public float scrollAn;

    public boolean hoveredFirst;
    public boolean hoveredSecond;

    public float hoveredFirstAn;
    public float hoveredSecondAn;

    private String altName = "";
    private boolean typing;

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
        scroll += delta * 1;
        return super.mouseScrolled(mouseX, mouseY, delta);
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        scrollAn = AnimationMath.lerp(scrollAn, scroll, 5);
        hoveredFirst = RenderUtil.isInRegion(mouseX, mouseY, 345 / 2f, 664 / 2f, 249 / 2f, 46 / 2f);
        hoveredSecond = RenderUtil.isInRegion(mouseX, mouseY, 345 / 2f, 723 / 2f, 249 / 2f, 46 / 2f);
        hoveredFirstAn = AnimationMath.lerp(hoveredFirstAn, hoveredFirst ? 1 : 0, 10);
        hoveredSecondAn = AnimationMath.lerp(hoveredSecondAn, hoveredSecond ? 1 : 0, 10);

        mc.gameRenderer.setupOverlayRendering(2);


        float width = mc.getMainWindow().scaledWidth();
        float height = mc.getMainWindow().scaledHeight();

        float x = width / 2f - 150, y = (327 / 2f);

        RenderUtil.Render2D.drawRect(0, 0, width, height, DWTheme.typeColor);
        RenderUtil.Render2D.drawImage(new ResourceLocation("expensive/images/mainmenu/nobackground.png"), 30,10,110,100,-1);
        RenderUtil.Render2D.drawRect(0, 0, width, height, RenderUtil.reAlphaInt(DWTheme.typeColor, 200));
        Fonts.rubikSemiBold[23].drawString(matrixStack, "FOUGEST", x + -179, y + -100, DWTheme.textColor);
        Fonts.rubikSemiBold[23].drawString(matrixStack, "PREMIUM", x + -179, y + -85, DWTheme.textColor);
        Fonts.rubikSemiBold[23].drawString(matrixStack, "1.16.5", x + -179, y + -70, DWTheme.textColor);


        RenderUtil.Render2D.drawRoundOutline(x, y, 502 / 2f, 481 / 2f, 5, 0f, DWTheme.bgColor, new Vector4i(
                DWTheme.lineColor, DWTheme.lineColor, DWTheme.lineColor, DWTheme.lineColor
        ));

        Fonts.rubikSemiBold[14].drawCenteredString(matrixStack, "Вы успешно зашли под никнеймом " + mc.getSession().getUsername(), x + ((502 / 2f) / 2f) - 10, y + 481 / 2f + 10, DWTheme.textColor);

        Fonts.rubikSemiBold[20].drawString(matrixStack, "Менеджер аккаунтов", x + 21, y + 25, DWTheme.textColor);
        Fonts.rubikSemiBold[14].drawString(matrixStack, "Никнейм", x + 21, y + 25 + 22, DWTheme.textColor);
        RenderUtil.Render2D.drawRoundedRect(x + 21, y + 25 + 30, 419 / 2f, 23, 5, DWTheme.typeColor);
        Fonts.rubikSemiBold[14].drawString(matrixStack, "Пароль", x + 21, y + 20 + 66, DWTheme.textColor);
        Fonts.rubikSemiBold[14].drawString(matrixStack, "(если требуется)", x + 23 + Fonts.rubikSemiBold[14].getWidth("Пароль"), y + 20 + 66, DWTheme.textColor);
        RenderUtil.Render2D.drawRoundedRect(x + 21, y + 20 + 66 + 7, 419 / 2f, 23, 5, DWTheme.typeColor);

        SmartScissor.push();
        SmartScissor.setFromComponentCoordinates(x + 21, y + 25 + 30, 419 / 2f - 5, 23);
        Fonts.rubikSemiBold[15].drawString(matrixStack, altName + (typing ? (System.currentTimeMillis() % 1000 > 500 ? "_" : "") : ""), x + 28, y + 25 + 38, DWTheme.textColor);
        SmartScissor.unset();
        SmartScissor.pop();

        RenderUtil.Render2D.drawRoundedRect(x + 63, 664 / 2f, 249 / 2f, 46 / 2f, 5, DWTheme.typeColor);
        RenderUtil.Render2D.drawRoundedRect(x + 63, 723 / 2f, 249 / 2f, 46 / 2f, 5, DWTheme.typeColor);

        BloomHelper.registerRenderCall(() -> {
            Fonts.rubikSemiBold[18].drawCenteredString(matrixStack, "Рандомный ник", 300 / 2f + (249 / 2f) / 2f, 364 / 2f + 8, DWTheme.textColor);
            Fonts.rubikSemiBold[18].drawCenteredString(matrixStack, "Войти", 345 / 2f + (249 / 2f) / 2f, 722 / 2f + 8, DWTheme.textColor);
        });


        Fonts.rubikSemiBold[18].drawCenteredString(matrixStack, "Рандомный ник", x + 123, 681 / 2f, DWTheme.textColor);
        Fonts.rubikSemiBold[18].drawCenteredString(matrixStack, "Войти", x + 123, 742 / 2f, DWTheme.textColor);

        float altX = 778 / 2f, altY = 298 / 2f;


        RenderUtil.Render2D.drawRoundOutline(altX + 384, 298 / 2f, 185, 539 / 2f, 5, 0f, DWTheme.bgColor, new Vector4i(
                DWTheme.lineColor, DWTheme.lineColor, DWTheme.lineColor, DWTheme.lineColor
        ));

        float iter = scrollAn;
        float size = 0;
        SmartScissor.push();
        SmartScissor.setFromComponentCoordinates(altX + 384, 298 / 2f, 923 / 2f, 539 / 2f, 1);

        for (Account account : accounts) {
            float panWidth = 177;

            float acX = altX + 388;
            float acY = 442 / 2f + (iter * (37)) - 68;
            float acY1 = 629 / 2f + (iter * (37));

            if (account.accountName.equalsIgnoreCase(mc.session.getUsername())) {
                RenderUtil.Render2D.drawRoundedRect(acX, acY, panWidth, 35, 5, ColorUtil.rgba(0, 0, 0, 255 * 0.22));
            }
            RenderUtil.Render2D.drawRoundedRect(acX, acY, panWidth, 35, 5, ColorUtil.rgba(0, 0, 0, 255 * 0.22));

            SmartScissor.push();
            SmartScissor.setFromComponentCoordinates(altX + 384, 298 / 2f, 923 / 2f, 539 / 2f);

            BloomHelper.registerRenderCall(() -> {
                SmartScissor.push();
                SmartScissor.setFromComponentCoordinates(acX + 5, 442 / 2f, panWidth - 8, 261 / 2f);
                Fonts.rubikSemiBold[14].drawCenteredString(matrixStack, account.accountName, acX + panWidth / 2f, acY1, ColorUtil.rgba(161, 164, 177, account.accountName.equalsIgnoreCase(mc.session.getUsername()) ? 255 : 0));
                SmartScissor.unset();
                SmartScissor.pop();
            });

            Fonts.rubikSemiBold[14].drawString(matrixStack, account.accountName, acX + 36, acY + 11, DWTheme.textColor);

            Date dateAdded = new Date(account.dateAdded);
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String formattedDate = dateFormat.format(dateAdded);

            Fonts.rubikSemiBold[14].drawString(matrixStack, "Дата создания: " + formattedDate, acX + 36, acY + 21, DWTheme.textColor);

            SmartScissor.unset();
            SmartScissor.pop();

            StencilUtil.initStencilToWrite();
            RenderUtil.Render2D.drawTexture(acX + 8.5F, acY + 8.5F, 36 / 2f, 36 / 2f, 5, 1);
            StencilUtil.readStencilBuffer(1);
            mc.getTextureManager().bindTexture(account.skin);
            AbstractGui.drawScaledCustomSizeModalRect(acX + 8.5F, acY + 8.5F, 8F, 8F, 8F, 8F, 36 / 2f, 36 / 2f, 64, 64);
            StencilUtil.uninitStencilBuffer();

            iter++;
            size++;
        }
        scroll = MathHelper.clamp(scroll, size > 7 ? -size + 7f : 0, 0);

        SmartScissor.unset();
        SmartScissor.pop();

        mc.gameRenderer.setupOverlayRendering();
    }


}
