package net.minecraft.client.gui.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;

import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.BiConsumer;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AccessibilityScreen;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.client.gui.widget.button.AbstractButton;
import net.minecraft.client.gui.widget.button.ImageButton;
import net.minecraft.client.renderer.RenderSkybox;
import net.minecraft.client.renderer.RenderSkyboxCube;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.realms.RealmsBridgeScreen;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SharedConstants;
import net.minecraft.util.Util;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.optifine.reflect.Reflector;
import net.optifine.reflect.ReflectorForge;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joml.Vector4i;
import ru.fougest.client.util.misc.TimerUtil;
import ru.fougest.main.managment.Managment;
import ru.fougest.client.screen.theme.DWTheme.DWTheme;
import ru.fougest.client.util.ClientUtil;
import ru.fougest.client.util.font.Fonts;
import ru.fougest.client.util.render.*;
import ru.fougest.client.util.render.animation.AnimationMath;

import javax.annotation.Nullable;

import static ru.fougest.client.util.IMinecraft.mc;
import static ru.fougest.client.util.IMinecraft.sr;

public class MainMenuScreen extends Screen {
    private static final Logger field_238656_b_ = LogManager.getLogger();
    public static final RenderSkyboxCube PANORAMA_RESOURCES = new RenderSkyboxCube(new ResourceLocation("textures/gui/title/background/panorama"));
    private static final ResourceLocation PANORAMA_OVERLAY_TEXTURES = new ResourceLocation("textures/gui/title/background/panorama_overlay.png");
    private static final ResourceLocation ACCESSIBILITY_TEXTURES = new ResourceLocation("textures/gui/accessibility.png");
    private final boolean showTitleWronglySpelled;
    private Button buttonResetDemo;
    private static final ResourceLocation MINECRAFT_TITLE_TEXTURES = new ResourceLocation("textures/gui/title/minecraft.png");
    private static final ResourceLocation MINECRAFT_TITLE_EDITION = new ResourceLocation("textures/gui/title/edition.png");
    private int widthCopyright;
    private int widthCopyrightRest;
    private Screen realmsNotification;
    private boolean hasCheckedForRealmsNotification;

    @Nullable
    private String splashText;
    private final RenderSkybox panorama = new RenderSkybox(PANORAMA_RESOURCES);
    private final boolean showFadeInAnimation;
    private long firstRenderTime;

    public MainMenuScreen() {
        this(false);
    }

    @Override
    public void init(Minecraft minecraft, int width, int height) {
        super.init(minecraft, width, height);
        if (ClientUtil.legitMode) {
            if (this.splashText == null) {
                this.splashText = this.minecraft.getSplashes().getSplashText();
            }

            this.widthCopyright = this.font.getStringWidth("Copyright Mojang AB. Do not distribute!");
            this.widthCopyrightRest = this.width - this.widthCopyright - 2;
            int i = 24;
            int j = this.height / 4 + 48;
            net.minecraft.client.gui.widget.button.Button button = null;


            this.addSingleplayerMultiplayerButtons(j, 24);

            if (Reflector.ModListScreen_Constructor.exists()) {
                button = ReflectorForge.makeButtonMods(this, j, 24);
                this.addButton(button);
            }

            this.addButton(new ImageButton(this.width / 2 - 124, j + 72 + 12, 20, 20, 0, 106, 20, net.minecraft.client.gui.widget.button.Button.WIDGETS_LOCATION, 256, 256, (p_lambda$init$0_1_) ->
            {
                this.minecraft.displayGuiScreen(new LanguageScreen(this, this.minecraft.gameSettings, this.minecraft.getLanguageManager()));
            }, new TranslationTextComponent("narrator.button.language")));
            this.addButton(new net.minecraft.client.gui.widget.button.Button(this.width / 2 - 100, j + 72 + 12, 98, 20, new TranslationTextComponent("menu.options"), (p_lambda$init$1_1_) ->
            {
                this.minecraft.displayGuiScreen(new OptionsScreen(this, this.minecraft.gameSettings));
            }));
            this.addButton(new net.minecraft.client.gui.widget.button.Button(this.width / 2 + 2, j + 72 + 12, 98, 20, new TranslationTextComponent("menu.quit"), (p_lambda$init$2_1_) ->
            {
                this.minecraft.shutdown();
            }));
            this.addButton(new ImageButton(this.width / 2 + 104, j + 72 + 12, 20, 20, 0, 0, 20, ACCESSIBILITY_TEXTURES, 32, 64, (p_lambda$init$3_1_) ->
            {
                this.minecraft.displayGuiScreen(new AccessibilityScreen(this, this.minecraft.gameSettings));
            }, new TranslationTextComponent("narrator.button.accessibility")));
            this.minecraft.setConnectedToRealms(false);

            if (this.minecraft.gameSettings.realmsNotifications && !this.hasCheckedForRealmsNotification) {
                RealmsBridgeScreen realmsbridgescreen = new RealmsBridgeScreen();
                this.realmsNotification = realmsbridgescreen.func_239555_b_(this);
                this.hasCheckedForRealmsNotification = true;
            }

            if (this.areRealmsNotificationsEnabled()) {
                this.realmsNotification.init(this.minecraft, this.width, this.height);
            }

            return;
        }

        int buttonWidth = (int) ((int) (353 / 2f));
        int buttonHeight = (int) ((int) (68 / 2f));
        int off = (int) (buttonHeight + 5);
        this.addButton(new Button((int) (mc.getMainWindow().scaledWidth() / 2 - buttonWidth / 2f - 7), mc.getMainWindow().scaledHeight() - 45, 33, 34, new StringTextComponent("singleplayer"), p_onPress_1_ -> {
            mc.displayGuiScreen(new WorldSelectionScreen(this));
        }));
        this.addButton(new Button((int) (mc.getMainWindow().scaledWidth() / 2 - buttonWidth / 2f - 7) + off, mc.getMainWindow().scaledHeight() - 45, 33, 34, new StringTextComponent("multiplayer"), p_onPress_1_ -> {
            mc.displayGuiScreen(new MultiplayerScreen(this));
        }));
        this.addButton(new Button((int) (mc.getMainWindow().scaledWidth() / 2 - buttonWidth / 2f - 7) + off * 2, mc.getMainWindow().scaledHeight() - 45, 33, 34, new StringTextComponent("alt"), p_onPress_1_ -> {
            mc.displayGuiScreen(Managment.ALT);
        }));
        this.addButton(new Button((int) (mc.getMainWindow().scaledWidth() / 2 - buttonWidth / 2f - 7) + off * 3, mc.getMainWindow().scaledHeight() - 45, 33, 34, new StringTextComponent("options"), p_onPress_1_ -> {
            mc.displayGuiScreen(new OptionsScreen(this, this.minecraft.gameSettings));
        }));
        this.addButton(new Button((int) (mc.getMainWindow().scaledWidth() / 2 - buttonWidth / 2f - 7) + off * 4, mc.getMainWindow().scaledHeight() - 45, 33, 34, new StringTextComponent("exit"), p_onPress_1_ -> {
            mc.shutdownMinecraftApplet();
        }));
    }

    private boolean areRealmsNotificationsEnabled() {
        return this.minecraft.gameSettings.realmsNotifications && this.realmsNotification != null;
    }

    private void addSingleplayerMultiplayerButtons(int yIn, int rowHeightIn) {
        this.addButton(new net.minecraft.client.gui.widget.button.Button(this.width / 2 - 100, yIn, 200, 20, new TranslationTextComponent("menu.singleplayer"), (p_lambda$addSingleplayerMultiplayerButtons$4_1_) ->
        {
            this.minecraft.displayGuiScreen(new WorldSelectionScreen(this));
        }));
        boolean flag = this.minecraft.isMultiplayerEnabled();
        net.minecraft.client.gui.widget.button.Button.ITooltip button$itooltip = flag ? net.minecraft.client.gui.widget.button.Button.field_238486_s_ : (p_lambda$addSingleplayerMultiplayerButtons$5_1_, p_lambda$addSingleplayerMultiplayerButtons$5_2_, p_lambda$addSingleplayerMultiplayerButtons$5_3_, p_lambda$addSingleplayerMultiplayerButtons$5_4_) ->
        {
            if (!p_lambda$addSingleplayerMultiplayerButtons$5_1_.active) {
                this.renderTooltip(p_lambda$addSingleplayerMultiplayerButtons$5_2_, this.minecraft.fontRenderer.trimStringToWidth(new TranslationTextComponent("title.multiplayer.disabled"), Math.max(this.width / 2 - 43, 170)), p_lambda$addSingleplayerMultiplayerButtons$5_3_, p_lambda$addSingleplayerMultiplayerButtons$5_4_);
            }
        };
        (this.addButton(new net.minecraft.client.gui.widget.button.Button(this.width / 2 - 100, yIn + rowHeightIn * 1, 200, 20, new TranslationTextComponent("menu.multiplayer"), (p_lambda$addSingleplayerMultiplayerButtons$6_1_) ->
        {
            Screen screen = (Screen) (this.minecraft.gameSettings.skipMultiplayerWarning ? new MultiplayerScreen(this) : new MultiplayerWarningScreen(this));
            this.minecraft.displayGuiScreen(screen);
        }, button$itooltip))).active = flag;
        (this.addButton(new net.minecraft.client.gui.widget.button.Button(this.width / 2 - 100, yIn + rowHeightIn * 2, 200, 20, new TranslationTextComponent("menu.online"), (p_lambda$addSingleplayerMultiplayerButtons$7_1_) ->
        {
            this.switchToRealms();
        }, button$itooltip))).active = flag;

        if (Reflector.ModListScreen_Constructor.exists() && this.buttons.size() > 0) {
            Widget widget = this.buttons.get(this.buttons.size() - 1);
            widget.x = this.width / 2 + 2;
            widget.setWidth(98);
        }
    }

    private void switchToRealms() {
        RealmsBridgeScreen realmsbridgescreen = new RealmsBridgeScreen();
        realmsbridgescreen.func_231394_a_(this);
    }

    public MainMenuScreen(boolean fadeIn) {
        super(new TranslationTextComponent("narrator.screen.title"));
        this.showFadeInAnimation = fadeIn;
        this.showTitleWronglySpelled = (double) (new Random()).nextFloat() < 1.0E-4D;
    }

    public void tick() {
        if (this.areRealmsNotificationsEnabled()) {
            this.realmsNotification.tick();
        }
    }

    public static CompletableFuture<Void> loadAsync(TextureManager texMngr, Executor backgroundExecutor) {
        return CompletableFuture.allOf(texMngr.loadAsync(MINECRAFT_TITLE_TEXTURES, backgroundExecutor), texMngr.loadAsync(MINECRAFT_TITLE_EDITION, backgroundExecutor), texMngr.loadAsync(PANORAMA_OVERLAY_TEXTURES, backgroundExecutor), PANORAMA_RESOURCES.loadAsync(texMngr, backgroundExecutor));
    }

    public boolean isPauseScreen() {return false;}

    public boolean shouldCloseOnEsc() {
        return false;
    }

    protected void init() {

    }

    class snowflake {
        public float x;
        public float y;
        public float size;
        public float alpha;
    }

    int snowflake_count = 229;

    public List<snowflake> sfs = new ArrayList<snowflake>();

    boolean created = false;
    private final TimerUtil bolvan = new TimerUtil();

    protected void renderSnowflake(MatrixStack matrixStack, snowflake snowflake) {
        Fonts.rubikSemiBold[new Random().nextInt(15, 22)].drawString(matrixStack, "*", snowflake.x, snowflake.y, ColorUtil.rgba(255, 255, 255, snowflake.alpha));
    }

    protected void createSnowflakeList() {
        if(!created) {
            for (int i = 1; i <= snowflake_count; i++) {
                snowflake s = new snowflake();
                s.x = new Random().nextInt(140, 1900);
                s.y = new Random().nextInt(-192, 33);
                s.alpha = new Random().nextInt(60, 255);
                s.size = 1;
                sfs.add(s);
                if (i == snowflake_count) { created = true; }
            }
        }
    }

    int f = 1;

    protected void createSnowflakes(MatrixStack matrixStack) {
        if(!created) { return; }

        long l = 22;
        if(bolvan.hasTimeElapsed(l)) {
            for (int i = 1; i <= sfs.size()-1; i++) {
                sfs.get(i).x = sfs.get(i).x - 1;
                sfs.get(i).y = sfs.get(i).y + 1;
                if(i <= sfs.size()-1) { bolvan.reset(); }
            }
        }

        for(int i = 0; i <= sfs.size()-1; i++){
            renderSnowflake(matrixStack, sfs.get(i));
        }

        if (sfs.get(sfs.size()-8).y > 90*f) {
            created = false;
            f++;
        }
        if(sfs.get(sfs.size()-8).y > 1050){
            for (int i = 1; i <= snowflake_count; i++) { sfs.remove(i); f-=0.5; }
        }
    }

    public void renderSnow(MatrixStack matrixStack) {
        createSnowflakeList();
        createSnowflakes(matrixStack);
    }

    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        if (ClientUtil.legitMode) {
            if (this.firstRenderTime == 0L && this.showFadeInAnimation) {
                this.firstRenderTime = Util.milliTime();
            }

            float f = this.showFadeInAnimation ? (float) (Util.milliTime() - this.firstRenderTime) / 1000.0F : 1.0F;
            GlStateManager.disableDepthTest();
            fill(matrixStack, 0, 0, this.width, this.height, -1);
            this.panorama.render(partialTicks, MathHelper.clamp(f, 0.0F, 1.0F));
            int i = 274;
            int j = this.width / 2 - 137;
            int k = 30;
            this.minecraft.getTextureManager().bindTexture(PANORAMA_OVERLAY_TEXTURES);
            RenderSystem.enableBlend();
            RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
            RenderSystem.color4f(1.0F, 1.0F, 1.0F, this.showFadeInAnimation ? (float) MathHelper.ceil(MathHelper.clamp(f, 0.0F, 1.0F)) : 1.0F);
            blit(matrixStack, 0, 0, this.width, this.height, 0.0F, 0.0F, 16, 128, 16, 128);
            float f1 = this.showFadeInAnimation ? MathHelper.clamp(f - 1.0F, 0.0F, 1.0F) : 1.0F;
            int l = MathHelper.ceil(f1 * 255.0F) << 24;

            if ((l & -67108864) != 0) {
                this.minecraft.getTextureManager().bindTexture(MINECRAFT_TITLE_TEXTURES);
                RenderSystem.color4f(1.0F, 1.0F, 1.0F, f1);

                if (this.showTitleWronglySpelled) {
                    this.blitBlackOutline(j, 30, (p_lambda$render$10_2_, p_lambda$render$10_3_) ->
                    {
                        this.blit(matrixStack, p_lambda$render$10_2_ + 0, p_lambda$render$10_3_, 0, 0, 99, 44);
                        this.blit(matrixStack, p_lambda$render$10_2_ + 99, p_lambda$render$10_3_, 129, 0, 27, 44);
                        this.blit(matrixStack, p_lambda$render$10_2_ + 99 + 26, p_lambda$render$10_3_, 126, 0, 3, 44);
                        this.blit(matrixStack, p_lambda$render$10_2_ + 99 + 26 + 3, p_lambda$render$10_3_, 99, 0, 26, 44);
                        this.blit(matrixStack, p_lambda$render$10_2_ + 155, p_lambda$render$10_3_, 0, 45, 155, 44);
                    });
                } else {
                    this.blitBlackOutline(j, 30, (p_lambda$render$11_2_, p_lambda$render$11_3_) ->
                    {
                        this.blit(matrixStack, p_lambda$render$11_2_ + 0, p_lambda$render$11_3_, 0, 0, 155, 44);
                        this.blit(matrixStack, p_lambda$render$11_2_ + 155, p_lambda$render$11_3_, 0, 45, 155, 44);
                    });
                }

                this.minecraft.getTextureManager().bindTexture(MINECRAFT_TITLE_EDITION);
                blit(matrixStack, j + 88, 67, 0.0F, 0.0F, 98, 14, 128, 16);

                if (Reflector.ForgeHooksClient_renderMainMenu.exists()) {
                    Reflector.callVoid(Reflector.ForgeHooksClient_renderMainMenu, this, matrixStack, this.font, this.width, this.height, l);
                }

                if (this.splashText != null) {
                    RenderSystem.pushMatrix();
                    RenderSystem.translatef((float) (this.width / 2 + 90), 70.0F, 0.0F);
                    RenderSystem.rotatef(-20.0F, 0.0F, 0.0F, 1.0F);
                    float f2 = 1.8F - MathHelper.abs(MathHelper.sin((float) (Util.milliTime() % 1000L) / 1000.0F * ((float) Math.PI * 2F)) * 0.1F);
                    f2 = f2 * 100.0F / (float) (this.font.getStringWidth(this.splashText) + 32);
                    RenderSystem.scalef(f2, f2, f2);
                    drawCenteredString(matrixStack, this.font, this.splashText, 0, -8, 16776960 | l);
                    RenderSystem.popMatrix();
                }

                String s = "Minecraft " + SharedConstants.getVersion().getName();

                if (this.minecraft.isDemo()) {
                    s = s + " Demo";
                } else {
                    s = s + ("release".equalsIgnoreCase(this.minecraft.getVersionType()) ? "" : "/" + this.minecraft.getVersionType());
                }

                if (this.minecraft.isModdedClient()) {
                    s = s + I18n.format("menu.modded");
                }

                if (Reflector.BrandingControl.exists()) {
                    if (Reflector.BrandingControl_forEachLine.exists()) {
                        BiConsumer<Integer, String> biconsumer = (p_lambda$render$12_3_, p_lambda$render$12_4_) ->
                        {
                            drawString(matrixStack, this.font, p_lambda$render$12_4_, 2, this.height - (10 + p_lambda$render$12_3_ * (9 + 1)), 16777215 | l);
                        };
                        Reflector.call(Reflector.BrandingControl_forEachLine, true, true, biconsumer);
                    }

                    if (Reflector.BrandingControl_forEachAboveCopyrightLine.exists()) {
                        BiConsumer<Integer, String> biconsumer1 = (p_lambda$render$13_3_, p_lambda$render$13_4_) ->
                        {
                            drawString(matrixStack, this.font, p_lambda$render$13_4_, this.width - this.font.getStringWidth(p_lambda$render$13_4_), this.height - (10 + (p_lambda$render$13_3_ + 1) * (9 + 1)), 16777215 | l);
                        };
                        Reflector.call(Reflector.BrandingControl_forEachAboveCopyrightLine, biconsumer1);
                    }
                } else {
                    drawString(matrixStack, this.font, s, 2, this.height - 10, 16777215 | l);
                }

                drawString(matrixStack, this.font, "Copyright Mojang AB. Do not distribute!", this.widthCopyrightRest, this.height - 10, 16777215 | l);

                if (mouseX > this.widthCopyrightRest && mouseX < this.widthCopyrightRest + this.widthCopyright && mouseY > this.height - 10 && mouseY < this.height) {
                    fill(matrixStack, this.widthCopyrightRest, this.height - 1, this.widthCopyrightRest + this.widthCopyright, this.height, 16777215 | l);
                }

                for (Widget widget : this.buttons) {
                    widget.setAlpha(f1);
                }
                if (this.areRealmsNotificationsEnabled() && f1 >= 1.0F) {
                    this.realmsNotification.render(matrixStack, mouseX, mouseY, partialTicks);
                }
                super.render(matrixStack, mouseX, mouseY, partialTicks);
            }
            return;
        }

        Vec2i fixed = ScaleMath.getMouse((int) mouseX, (int) mouseY);
        mouseX = fixed.getX();
        mouseY = fixed.getY();

        Date time = new Date();
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        mc.gameRenderer.setupOverlayRendering(2);
        RenderUtil.Render2D.drawRect(0, 0, mc.getMainWindow().scaledWidth(), mc.getMainWindow().scaledHeight(), DWTheme.typeColor);
        renderSnow(matrixStack);
        RenderUtil.Render2D.drawShadow(sr.getScaledWidth() / 90f,sr.getScaledHeight() / 50f,95,40,10, Managment.STYLE_MANAGER.getCurrentStyle().getColor(150), Managment.STYLE_MANAGER.getCurrentStyle().getColor(100), Managment.STYLE_MANAGER.getCurrentStyle().getColor(50), Managment.STYLE_MANAGER.getCurrentStyle().getColor(1));
        RenderUtil.Render2D.drawRoundOutline(sr.getScaledWidth() / 90f,sr.getScaledHeight() / 50f,95,40,5,0,DWTheme.bgColor, new Vector4i(Managment.STYLE_MANAGER.getCurrentStyle().getColor(150), Managment.STYLE_MANAGER.getCurrentStyle().getColor(100), Managment.STYLE_MANAGER.getCurrentStyle().getColor(50), Managment.STYLE_MANAGER.getCurrentStyle().getColor(1)));

        Fonts.rubikSemiBold[30].drawCenteredString(new MatrixStack(), timeFormat.format(time.getTime()), sr.getScaledWidth() / 90f + 95 / 2f,sr.getScaledHeight() / 50f + 10, DWTheme.textColor);
        Fonts.rubikSemiBold[14].drawCenteredString(new MatrixStack(), "date: " + dateFormat.format(time.getTime()), sr.getScaledWidth() / 90f + 95 / 2f,sr.getScaledHeight() / 50f + 30, DWTheme.textColor);

        RenderUtil.Render2D.drawImage(new ResourceLocation("expensive/images/mainmenu/nobackground.png"), mc.getMainWindow().scaledWidth() / 2f - 130 / 2f, sr.getScaledHeight() / 2f - 120 / 2f,130,120,-1);
        Fonts.rubikSemiBold[23].drawCenteredString(matrixStack, "FOUGEST",mc.getMainWindow().scaledWidth() / 2f + 25, sr.getScaledHeight() / 2f + 65, DWTheme.textColor);
        
        Fonts.rubikSemiBold[11].drawString(matrixStack,"made with love klaus and swybwt!",mc.getMainWindow().scaledWidth() / 2f - 478, sr.getScaledHeight() / 1.01f, DWTheme.textColor);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        mc.gameRenderer.setupOverlayRendering();
    }

    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        Vec2i fixed = ScaleMath.getMouse((int) mouseX, (int) mouseY);
        mouseX = fixed.getX();
        mouseY = fixed.getY();
        return super.mouseClicked(mouseX, mouseY, button);
    }

    public void onClose() {
        if (this.realmsNotification != null) {
            this.realmsNotification.onClose();
        }
    }

    public static class Button extends AbstractButton {

        public static final net.minecraft.client.gui.widget.button.Button.ITooltip field_238486_s_ = (button, matrixStack, mouseX, mouseY) ->
        {
        };
        protected final net.minecraft.client.gui.widget.button.Button.IPressable onPress;
        protected final net.minecraft.client.gui.widget.button.Button.ITooltip onTooltip;

        public Button(int x, int y, int width, int height, ITextComponent title, net.minecraft.client.gui.widget.button.Button.IPressable pressedAction) {
            this(x, y, width, height, title, pressedAction, field_238486_s_);
        }

        public Button(int x, int y, int width, int height, ITextComponent title, net.minecraft.client.gui.widget.button.Button.IPressable pressedAction, net.minecraft.client.gui.widget.button.Button.ITooltip onTooltip) {
            super(x, y, width, height, title);
            this.onPress = pressedAction;
            this.onTooltip = onTooltip;
        }

        public float animation;

        public void renderButton(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
            animation = AnimationMath.lerp(animation, isHovered() ? 1f : 0, 15);

            if (isHovered()) {
                Fonts.rubikSemiBold[15].drawCenteredString(new MatrixStack(), getMessage().getString(), x + width / 2f * animation, y + height / 2f - Fonts.rubikSemiBold[23].getFontHeight() / 2f - 20, ColorUtil.interpolateColor(DWTheme.textColor, DWTheme.textColor, animation));
            } else {
                Fonts.rubikSemiBold[15].drawCenteredString(new MatrixStack(), getMessage().getString(), x + width / 2f, y + height / 2f - Fonts.rubikSemiBold[23].getFontHeight() / 2f - 20 * animation, ColorUtil.interpolateColor(-DWTheme.textColor, DWTheme.textColor, animation));
            }

            RenderUtil.Render2D.drawRoundOutline(x + width / 2f - 17, y, 33, height, 5f, 0f, DWTheme.typeColor, new Vector4i(
                    ColorUtil.interpolateColor(new Color(33,33,33).getRGB(), new Color(0, 128, 255,255).getRGB(), animation)
            ));
            RenderUtil.Render2D.drawImage(new ResourceLocation("expensive/images/mainmenu/icons/" + getMessage().getString() + ".png"), x + width / 2f - 13, y + height / 2f - Fonts.rubikSemiBold[23].getFontHeight() / 2f - 5, 25,25,new Color(0,0,0).getRGB());

            if (false)
                BloomHelper.registerRenderCall(() -> {
                    Fonts.rubikSemiBold[23].drawCenteredString(matrixStack, this.getMessage().getString(), x + width / 2f, y + height / 2f - Fonts.rubikSemiBold[23].getFontHeight() / 2f + 2, RenderUtil.reAlphaInt(ColorUtil.interpolateColor(ColorUtil.rgba(132, 135, 147,255), -1, animation), (int) (255 * animation)));
                });
        }


        public void onPress() {
            this.onPress.onPress(this);
        }
    }
}
