package ru.fougest.client.screen.beta;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector4f;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import org.lwjgl.glfw.GLFW;
import ru.fougest.main.managment.Managment;
import ru.fougest.client.modules.Function;
import ru.fougest.client.modules.Type;
import ru.fougest.client.screen.beta.component.impl.Component;
import ru.fougest.client.screen.beta.component.impl.*;
import ru.fougest.client.screen.beta.component.theme.ThemeDrawing;
import ru.fougest.client.screen.theme.DWTheme.DWTheme;
import ru.fougest.client.screen.theme.DefaultTheme.Style;
import ru.fougest.client.util.ClientUtil;
import ru.fougest.client.util.font.Fonts;
import ru.fougest.client.util.math.KeyMappings;
import ru.fougest.client.util.math.MathUtil;
import ru.fougest.client.util.render.*;
import ru.fougest.client.util.render.animation.AnimationMath;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static ru.fougest.client.screen.beta.component.impl.ModuleComponent.binding;
import static ru.fougest.client.util.IMinecraft.mc;
import static ru.fougest.client.util.render.RenderUtil.isInRegion;

public class ClickGui extends Screen {
    public ClickGui() {
        super(new StringTextComponent("GUI"));
        for (Function function : Managment.FUNCTION_MANAGER.getFunctions()) {
            objects.add(new ModuleComponent(function));
        }
        for (Style style : Managment.STYLE_MANAGER.styles) {
            this.theme.add(new ThemeComponent(style));
        }
    }
    private ThemeDrawing themeDrawing = new ThemeDrawing();
    double xPanel, yPanel;
    Type current;

    float animation;

    public ArrayList<ModuleComponent> objects = new ArrayList<>();

    public List<ThemeComponent> theme = new ArrayList<>();

    public float scroll = 0;
    public float animateScroll = 0;

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
        scroll += delta * 15;
        ColorComponent.opened = null;
        ThemeComponent.selected = null;
        return super.mouseScrolled(mouseX, mouseY, delta);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    boolean searchOpened;
    float seacrh;

    private String searchText = "";
    public static boolean typing;

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        float scale = 2f;
        float width = 850 / scale;
        float height = 550 / scale;
        float leftPanel = 200 / scale;
        float x = MathUtil.calculateXPosition(mc.getMainWindow().scaledWidth() / 2f, width);
        float y = MathUtil.calculateXPosition(mc.getMainWindow().scaledHeight() / 2f, height);
        xPanel = x;
        yPanel = y;
        animation = AnimationMath.lerp(animation, 0, 5);

        Vec2i fixed = ScaleMath.getMouse((int) mouseX, (int) mouseY);
        mouseX = fixed.getX();
        mouseY = fixed.getY();

        int finalMouseX = mouseX;
        int finalMouseY = mouseY;

        mc.gameRenderer.setupOverlayRendering(2);
        renderBackground(matrixStack, x, y, width, height, leftPanel, finalMouseX, finalMouseY);
        renderCategories(matrixStack, x, y, width, height, leftPanel, finalMouseX, finalMouseY);
        renderComponents(matrixStack, x, y - 6.5f, width, height + 6, leftPanel, finalMouseX, finalMouseY);
        renderColorPicker(matrixStack, x, y, width, height, leftPanel, finalMouseX, finalMouseY);
        RenderUtil.SmartScissor.setFromComponentCoordinates((int) (x + 101f), (int) (y + 25), (int) (width - leftPanel) - 0.5f, (int) height);
        if (current == Type.Theme) {
            themeDrawing.draw(matrixStack, mouseX, mouseY, x + 100 + animateScroll, y + 45, width - leftPanel, height);
            BloomHelper.draw(25, 1.5f, false);
        }
        RenderUtil.SmartScissor.unset();
        mc.gameRenderer.setupOverlayRendering();
    }

    void renderColorPicker(MatrixStack matrixStack, float x, float y, float width, float height, float leftPanel, int mouseX, int mouseY) {
        if (ColorComponent.opened != null) {
            ColorComponent.opened.draw(matrixStack, mouseX, mouseY);
        }
        if (ThemeComponent.selected != null) {
            ThemeComponent.selected.draw(matrixStack, mouseX, mouseY);
        }
    }

    void renderBackground(MatrixStack matrixStack, float x, float y, float width, float height, float leftPanel, int mouseX, int mouseY) {
        RenderUtil.Render2D.drawShadow(x, y, width, height, 20, DWTheme.bgColor);
        RenderUtil.Render2D.drawRoundedRect(x, y, width, height, 6, DWTheme.bgColor);
        RenderUtil.Render2D.drawRoundedCorner(x, y, 101, height,new Vector4f(6,6,0,0), DWTheme.typeColor);

        RenderUtil.Render2D.drawRoundedRect(x + width - 22,y + 2, 20,20,6,new Color(0,0,0).getRGB());
        RenderUtil.Render2D.drawRoundedRect(x + width - 45,y + 2, 20,20,6,new Color(255,255,255).getRGB());

        RenderUtil.Render2D.drawRect(x + width - 50,y, 1,25, DWTheme.lineColor);
        RenderUtil.Render2D.drawRect(x + leftPanel, y, 1, height, DWTheme.lineColor);
        RenderUtil.Render2D.drawRect(x + leftPanel, y + 24.5f, width - 100.5f, 1, DWTheme.lineColor);
    }

    void renderCategories(MatrixStack matrixStack, float x, float y, float width, float height, float leftPanel, int mouseX, int mouseY) {
        float heightCategory = 38 / 1.5f;
        for (Type t : Type.values()) {
            if (t == current) {
                RenderUtil.Render2D.drawShadow(x + 5, y + 32.5f + t.ordinal() * heightCategory + 1, leftPanel * (1 - animation) - 10, heightCategory - 5, 10, new Color(0, 139, 255, 255).getRGB());
                RenderUtil.Render2D.drawRoundedRect(x + 5, y + 32.5f + t.ordinal() * heightCategory + 1, leftPanel * (1 - animation) - 10, heightCategory - 5, 5, new Color(0, 139, 255, 255).getRGB());
            }
            Fonts.rubikSemiBold[20].drawString(matrixStack, t.name(), x + 25.5f, y + 39 + t.ordinal() * heightCategory + 1, DWTheme.textColor);
            Fonts.icons1[22].drawString(matrixStack, t.image, x + 10.5f, y + 39 + t.ordinal() * heightCategory + 2, DWTheme.textColor);
        }
        Fonts.rubikSemiBold[30].drawString(matrixStack, "FOUGEST", x + 15, y + 11, DWTheme.textColor);

        Fonts.rubikSemiBold[20].drawString(matrixStack, "Fougest Client", x + 110, y + 9, DWTheme.textColor);

        if (ClientUtil.me != null) {
            GlStateManager.color4f(0, 0, 0, 0);
            GlStateManager.bindTexture(RenderUtil.Render2D.downloadImage(ClientUtil.me.getAvatarUrl()));
            RenderUtil.Render2D.drawTexture(x + 4, y + 2 + height - 30, 25, 25, 5, 1);
        } else {
            mc.getTextureManager().bindTexture(new ResourceLocation("expensive/images/ui/profile/image.png"));
            RenderUtil.Render2D.drawTexture(x + 4, y + 2 + height - 30, 25, 25, 5, 1);
        }
        Fonts.rubikSemiBold[14].drawString(matrixStack, "user: " + Managment.USER_PROFILE.getName(), x + 33, y + 2 + height - 26, DWTheme.textColor);
        Fonts.rubikSemiBold[14].drawString(matrixStack, "uid: " + Managment.USER_PROFILE.getUid(), x + 33, y + 2 + height - 18, DWTheme.textColor);
    }
    void renderComponents(MatrixStack matrixStack, float x, float y, float width, float height, float leftPanel, int mouseX, int mouseY) {
        SmartScissor.push();
        SmartScissor.setFromComponentCoordinates(x, y + 64 / 2f, width, height - 64 / 2f);
        drawComponents(matrixStack, mouseX, mouseY);
        SmartScissor.unset();
        SmartScissor.pop();

        RenderUtil.Render2D.drawRoundedCorner(x + leftPanel, y + 64 / 2f, width - leftPanel, height - 64 / 2f, new Vector4f(0, 0, 0, 6), new Color(15, 15, 15, ((int) (255 * animation))).getRGB());
    }

    void renderSearchBar(MatrixStack matrixStack, float x, float y, float width, float height, float leftPanel, int mouseX, int mouseY) {
        Fonts.icons[16].drawString(matrixStack, "C", x + width - ((64 / 2f) / 2f) * 2F + 15, y + (64 / 2f) / 2f - 4, -1);
    }
    private String configName = "";
    private boolean configTyping;
    public static String confign;

    void drawComponents(MatrixStack stack, int mouseX, int mouseY) {

        List<ModuleComponent> first = objects.stream()
                .filter(moduleObject -> {
                    if (typing) {
                        return true;
                    } else {
                        return moduleObject.function.category == current;
                    }
                })
                .filter(moduleObject -> objects.indexOf(moduleObject) % 2 == 0)
                .toList();

        List<ModuleComponent> second = objects.stream()
                .filter(moduleObject -> {
                    if (typing) {
                        return true;
                    } else {
                        return moduleObject.function.category == current;
                    }
                })
                .filter(moduleObject -> objects.indexOf(moduleObject) % 2 != 0)
                .toList();

        float scale = 2f;
        animateScroll = AnimationMath.lerp(animateScroll, scroll, 20);
        float width = 850 / scale;
        float height = 550 / scale;
        float leftPanel = 200 / scale;
        float x = MathUtil.calculateXPosition(mc.getMainWindow().scaledWidth() / 2f, width);
        float y = MathUtil.calculateXPosition(mc.getMainWindow().scaledHeight() / 2f, height);

        float offset = (float) (yPanel + (64 / 2f) + 12) + animateScroll - 16;
        float size1 = 0;
        for (ModuleComponent component : first) {
            component.parent = this;
            component.setPosition((float) (xPanel + (100f + 12)) - 8, offset, 314 / 2f, 37);
            component.drawComponent(stack, mouseX, mouseY);
            if (!component.components.isEmpty()) {
                for (Component settingComp : component.components) {
                    if (settingComp.s != null && settingComp.s.visible()) {
                        offset += settingComp.height;
                        size1 += settingComp.height;
                    }
                }
            }
            offset += component.height + 8;
            size1 += component.height + 8;
        }

        float offset2 = (float) (yPanel + (64 / 2f) + 12) + animateScroll - 16;
        float size2 = 0;
        for (ModuleComponent component : second) {
            component.parent = this;
            component.setPosition((float) (xPanel + (100f + 12) + 314 / 2f + 10) - 15, offset2, 314 / 2f, 37);
            component.drawComponent(stack, mouseX, mouseY);
            if (!component.components.isEmpty()) {
                for (Component settingComp : component.components) {
                    if (settingComp.s != null && settingComp.s.visible()) {
                        offset2 += settingComp.height;
                        size2 += settingComp.height;
                    }
                }
            }
            offset2 += component.height + 8;
            size2 += component.height + 8;
        }
        float max = Math.max(size1, size2);
        if (max < height) {
            if (current == Type.Theme) {
                scroll = MathHelper.clamp(scroll, -30, 0);
            } else {
                scroll = 0;
            }
        } else {
            scroll = MathHelper.clamp(scroll, -(max - height + 28), 0);
        }
    }

    @Override
    public void init(Minecraft minecraft, int width, int height) {
        super.init(minecraft, width, height);
        ColorComponent.opened = null;
        ThemeComponent.selected = null;
        typing = false;
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {

        Vec2i fixed = ScaleMath.getMouse((int) mouseX, (int) mouseY);
        mouseX = fixed.getX();
        mouseY = fixed.getY();

        for (ModuleComponent m : objects) {
            if (searchText.isEmpty()) {
                if (m.function.category != current) continue;
            } else {
                if (!m.function.name.toLowerCase().contains(searchText.toLowerCase())) continue;
            }
            m.mouseReleased((int) mouseX, (int) mouseY, button);
        }
        if (ColorComponent.opened != null) {
            ColorComponent.opened.unclick((int) mouseX, (int) mouseY);
        }
        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (typing) {
            if (!(current == Type.Theme)) {
                if (keyCode == GLFW.GLFW_KEY_BACKSPACE) {
                    if (!searchText.isEmpty())
                        searchText = searchText.substring(0, searchText.length() - 1);
                }
                if (keyCode == GLFW.GLFW_KEY_ENTER) {
                    typing = false;
                }
            }
        }

        for (ModuleComponent m : objects) {
            if (searchText.isEmpty()) {
                if (m.function.category != current) continue;
            } else {
                if (!m.function.name.toLowerCase().contains(searchText.toLowerCase())) continue;
            }
            m.keyTyped(keyCode, scanCode, modifiers);
        }

        if (binding != null) {
            if (keyCode == GLFW.GLFW_KEY_DELETE) {
                binding.function.bind = 0;
            } else {
                Managment.NOTIFICATION_MANAGER.add("������ " + TextFormatting.GRAY +  binding.function.name + TextFormatting.WHITE+ " ��� �������� �� ������ " + KeyMappings.reverseKeyMap.get(keyCode), "Module",3);

                binding.function.bind = keyCode;
            }
            binding = null;
        }

        if (configTyping) {
            if (keyCode == GLFW.GLFW_KEY_BACKSPACE) {
                if (!configName.isEmpty())
                    configName = configName.substring(0, configName.length() - 1);
            }
            if (keyCode == GLFW.GLFW_KEY_ENTER) {
                configTyping = false;
            }
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean charTyped(char codePoint, int modifiers) {
        if (typing)
            searchText += codePoint;
        if (configTyping)
            configName += codePoint;

        for (ModuleComponent m : objects) {
            if (searchText.isEmpty()) {
                if (m.function.category != current) continue;
            } else {
                if (!m.function.name.toLowerCase().contains(searchText.toLowerCase())) continue;
            }
            m.charTyped(codePoint,modifiers);
        }
        return super.charTyped(codePoint, modifiers);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {

        Vec2i fixed = ScaleMath.getMouse((int) mouseX, (int) mouseY);

        mouseX = fixed.getX();
        mouseY = fixed.getY();

        float scale = 2f;
        float width = 850 / scale;
        float height = 550 / scale;
        float leftPanel = 199 / scale;
        float x = MathUtil.calculateXPosition(mc.getMainWindow().scaledWidth() / 2f, width);
        float y = MathUtil.calculateXPosition(mc.getMainWindow().scaledHeight() / 2f, height);
        float heightCategory = 38 / 1.5f;


        if (ColorComponent.opened != null) {
            if (!ColorComponent.opened.click((int) mouseX, (int) mouseY))
                return super.mouseClicked(mouseX, mouseY, button);
        }

        for (Type t : Type.values()) {
            if (isInRegion(mouseX, mouseY, x + 2, y + 32.5f + t.ordinal() * heightCategory + 1, leftPanel - 3, heightCategory - 2)) {
                if (current == t) continue;
                current = t;
                animation = 1;
                scroll = 0;
                searchText = "";
                ColorComponent.opened = null;
                ThemeComponent.selected = null;
                typing = false;
            }
        }
        if (current == Type.Theme) {
            themeDrawing.click((int) mouseX, (int) mouseY, button);
        }
        if (RenderUtil.isInRegion(mouseX, mouseY, x + width - 22,y + 2, 25,25)) {
            Managment.DARK_WHITE_THEME.setTheme(true);
        }
        if (RenderUtil.isInRegion(mouseX, mouseY, x + width - 45,y + 2, 25,25)) {
            Managment.DARK_WHITE_THEME.setTheme(false);
        }
        if (isInRegion(mouseX, mouseY, x - 15, y + 64 / 2f - 32, width, height - 64 / 2f + 32)) {
            for (ModuleComponent m : objects) {
                if (searchText.isEmpty()) {
                    if (m.function.category != current) continue;
                } else {
                    if (!m.function.name.toLowerCase().contains(searchText.toLowerCase())) continue;
                }
                m.mouseClicked((int) mouseX, (int) mouseY, button);
            }
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }
}
