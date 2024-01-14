package ru.fougest.client.modules.impl.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import lombok.Getter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.network.play.NetworkPlayerInfo;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.EffectUtils;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Team;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector4f;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.GameType;
import org.joml.Vector4i;
import ru.fougest.main.Initilization;
import ru.fougest.client.events.Event;
import ru.fougest.client.events.impl.player.EventUpdate;
import ru.fougest.client.events.impl.render.EventRender;
import ru.fougest.main.managment.Managment;
import ru.fougest.client.modules.Function;
import ru.fougest.client.modules.FunctionAnnotation;
import ru.fougest.client.modules.Type;
import ru.fougest.client.modules.settings.imp.BooleanOption;
import ru.fougest.client.modules.settings.imp.MultiBoxSetting;
import ru.fougest.client.modules.settings.imp.SliderSetting;
import ru.fougest.client.screen.theme.DWTheme.DWTheme;
import ru.fougest.client.screen.theme.DefaultTheme.StyleManager;
import ru.fougest.client.util.ClientUtil;
import ru.fougest.client.util.animations.Animation;
import ru.fougest.client.util.animations.Direction;
import ru.fougest.client.util.animations.impl.EaseBackIn;
import ru.fougest.client.util.drag.Dragging;
import ru.fougest.client.util.font.Fonts;
import ru.fougest.client.util.font.styled.StyledFont;
import ru.fougest.client.util.math.MathUtil;
import ru.fougest.client.util.misc.HudUtil;
import ru.fougest.client.util.misc.TimerUtil;
import ru.fougest.client.util.render.ColorUtil;
import ru.fougest.client.util.render.RenderUtil;
import ru.fougest.client.util.render.SmartScissor;
import ru.fougest.client.util.render.animation.AnimationMath;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Pattern;

@FunctionAnnotation(name = "Hud", type = Type.Render)
public class HUDSELFCODE extends Function {
    public MultiBoxSetting elements = new MultiBoxSetting("Элементы",
            new BooleanOption("Логотип", false),
            new BooleanOption("Активные бинды", false),
            new BooleanOption("Координаты", false),
            new BooleanOption("Список Модулей", false),
            new BooleanOption("Уведомления", false),
            new BooleanOption("Стафф лист", false),
            new BooleanOption("Таргет", false),
            new BooleanOption("Эффекты", false),
            new BooleanOption("Время", false));
    public MultiBoxSetting limitations = new MultiBoxSetting("Ограничения",
            new BooleanOption("Скрывать визуалы", true),
            new BooleanOption("Только бинды", false)).setVisible(() -> elements.get(3));
    public SliderSetting fontSize = new SliderSetting("Размер шрифта", 14, 12, 18, 1);

    public HUDSELFCODE() {
        addSettings(elements, limitations, fontSize);
    }
    private int activeModules = 0;
    private float heightDynamic = 0;
    List<Function> sortedFunctions = new ArrayList<>();
    TimerUtil delay = new TimerUtil();
    public Dragging keyBinds = Initilization.createDrag(this, "KeyBinds", 10, 100);
    private final Pattern namePattern = Pattern.compile("^\\w{3,16}$");
    private final Pattern prefixMatches = Pattern.compile(".*(mod|der|adm|help|wne|мод|хелп|помо|адм|владе|отри|таф|taf|curat|курато|dev|раз|supp|сапп|yt|ютуб).*");


    @Override
    public void onEvent(Event event) {
        if (event instanceof EventRender e) {
            if (elements.get(0)) onWaterMark(e);
            if (elements.get(1)) onKeyBinds(e);
            if (elements.get(2)) onCoordInfo(e);
            if (elements.get(3)) onArrayList(e);
            if (elements.get(5)) onStaffListRender(e);
            if (elements.get(6)) onRenderTargetHUD(e);
            if (elements.get(7)) onPotionElementsRender(e);
            if (elements.get(8)) onTimeInfo(e);
        }
        if (event instanceof EventUpdate) {
            staffPlayers.clear();

            for (ScorePlayerTeam team : mc.world.getScoreboard().getTeams().stream().sorted(Comparator.comparing(Team::getName)).toList()) {

                String name = team.getMembershipCollection().toString();
                name = name.substring(1, name.length() - 1);

                if (namePattern.matcher(name).matches()) {
                    if (prefixMatches.matcher(repairString(team.getPrefix().getString().toLowerCase(Locale.ROOT))).matches() || Managment.STAFF_MANAGER.isStaff(name)) {
                        staffPlayers.add(new StaffPlayer(name, team.getPrefix()));
                    }
                }
            }
        }
    }
    private String repairString(String input) {
        StringBuilder sb = new StringBuilder(input.length());
        for (char c : input.toCharArray()) {
            if (c >= 65281 && c <= 65374) {
                sb.append((char) (c - 65248));
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    public void onWaterMark(EventRender e) {
        StyledFont font = Fonts.rubikSemiBold[14];
        StyledFont font1 = Fonts.rubikSemiBold[14];
        float x = 5;
        float y = 5;
        String text = "FOUGEST";
        String info = "user: " + Managment.USER_PROFILE.getName() + "  " + "uid: " + Managment.USER_PROFILE.getUid() + "  " + Minecraft.debugFPS + "fps" + "  " + HudUtil.calculatePing() + "ms";

        float width = font.getWidth(text) + font1.getWidth(info) + 10;
        float height = font.getFontHeight() + 5;

        RenderUtil.Render2D.drawShadow(x,y,width + 1,height, 10, Managment.STYLE_MANAGER.getCurrentStyle().getColor(1), Managment.STYLE_MANAGER.getCurrentStyle().getColor(50), Managment.STYLE_MANAGER.getCurrentStyle().getColor(100), Managment.STYLE_MANAGER.getCurrentStyle().getColor(150));
        RenderUtil.Render2D.drawRoundOutline(x,y,width + 1,height,5,0, DWTheme.typeColor,new Vector4i(Managment.STYLE_MANAGER.getCurrentStyle().getColor(150), Managment.STYLE_MANAGER.getCurrentStyle().getColor(100), Managment.STYLE_MANAGER.getCurrentStyle().getColor(50), Managment.STYLE_MANAGER.getCurrentStyle().getColor(1)));
        font.drawString(e.matrixStack,ClientUtil.gradient(text, Managment.STYLE_MANAGER.getCurrentStyle().getColor(90), Managment.STYLE_MANAGER.getCurrentStyle().getColor(1)),x + 5, y + 5, -1);
        font1.drawString(e.matrixStack,info,x + font.getWidth(text) + 7, y + 5, DWTheme.textColor);
    }
    public void onTimeInfo(EventRender e) {
        StyledFont font = Fonts.rubikSemiBold[14];
        Date time = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
        float x = sr.scaledWidth() / 2.0F;
        float y = 5;
        float width2 = font.getWidth(dateFormat.format(time.getTime())) + 10;
        float height = font.getFontHeight() + 5;
        RenderUtil.Render2D.drawShadow(x - 20,y,width2,height,10,Managment.STYLE_MANAGER.getCurrentStyle().getColor(150), Managment.STYLE_MANAGER.getCurrentStyle().getColor(100), Managment.STYLE_MANAGER.getCurrentStyle().getColor(50), Managment.STYLE_MANAGER.getCurrentStyle().getColor(1));
        RenderUtil.Render2D.drawRoundOutline(x - 20,y,width2,height,5,0,DWTheme.typeColor,new Vector4i(Managment.STYLE_MANAGER.getCurrentStyle().getColor(150), Managment.STYLE_MANAGER.getCurrentStyle().getColor(100), Managment.STYLE_MANAGER.getCurrentStyle().getColor(50), Managment.STYLE_MANAGER.getCurrentStyle().getColor(1)));
        font.drawString(e.matrixStack,dateFormat.format(time.getTime()),x - 15,y + 5, DWTheme.textColor);
    }
    public Dragging potionStatus = Initilization.createDrag(this, "PotionStatus", 200, 50);
    private float hDynamic = 0;
    private int activePotions = 0;
    private void onPotionElementsRender(EventRender renderEvent) {
        float posX = potionStatus.getX();
        float posY = potionStatus.getY();
        MatrixStack matrixStack = new MatrixStack();
        int roundDegree = 4;
        int headerHeight = 14;
        int width = (int) potionStatus.getWidth();
        int padding = 5;
        int offset = 11;

        int firstColor = ColorUtil.getColorStyle(0);
        int secondColor = ColorUtil.getColorStyle(90);

        float height = activePotions * offset;

        this.hDynamic = AnimationMath.fast(this.hDynamic, height, 10);

        RenderUtil.Render2D.drawShadow(posX, posY, width, headerHeight + hDynamic + padding / 2f, 10, firstColor, secondColor, ColorUtil.getColorStyle(180), ColorUtil.getColorStyle(270));

        RenderUtil.Render2D.drawRoundOutline(posX, posY, width, hDynamic + headerHeight + 2.5f, 5,0, DWTheme.typeColor, new Vector4i(ColorUtil.getColorStyle(1), ColorUtil.getColorStyle(50), ColorUtil.getColorStyle(100), ColorUtil.getColorStyle(150)));

        Fonts.rubikSemiBold[14].drawString(matrixStack, "potions:", potionStatus.getX() + 15, posY + 6f, DWTheme.textColor);
        Fonts.icons[20].drawString(matrixStack, "q", potionStatus.getX() + 4, posY + 6f, DWTheme.textColor);

        SmartScissor.push();
        SmartScissor.setFromComponentCoordinates(posX, posY, width, headerHeight + hDynamic + padding / 2f);
        int index = 0;
        if (mc.player.getActivePotionEffects().isEmpty()) {
            RenderUtil.Render2D.drawRoundedCorner(posX + 1, posY + headerHeight, width - 2, 1, new Vector4f(0, 0, 0, 0), new Vector4i(ColorUtil.getColorStyle(1), ColorUtil.getColorStyle(50), ColorUtil.getColorStyle(100), ColorUtil.getColorStyle(150)));
            Fonts.rubikSemiBold[12].drawString(matrixStack, "is empty", posX + padding, posY + headerHeight + padding + (index * offset) + 1, DWTheme.textColor);
            index++;
        } else {
            for (EffectInstance p : mc.player.getActivePotionEffects()) {
                String durationText = EffectUtils.getPotionDurationString(p, 1);
                float durationWidth = Fonts.rubikSemiBold[12].getWidth(durationText);

                RenderUtil.Render2D.drawRoundedCorner(posX + 1, posY + headerHeight, width - 2, 1, new Vector4f(0, 0, 0, 0), new Vector4i(ColorUtil.getColorStyle(1), ColorUtil.getColorStyle(50), ColorUtil.getColorStyle(100), ColorUtil.getColorStyle(150)));

                Fonts.rubikSemiBold[12].drawString(matrixStack, I18n.format(p.getEffectName()) + " " + durationText, posX + padding, posY + headerHeight + padding + (index * offset) + 1, DWTheme.textColor);

                index++;
            }
        }
        SmartScissor.unset();
        SmartScissor.pop();
        activePotions = index;
        potionStatus.setWidth(78);
        potionStatus.setHeight(activePotions * offset + headerHeight);
    }
    public void onCoordInfo(EventRender e) {
        StyledFont font = Fonts.rubikSemiBold[14];
        float x = 5;
        float y = 515;
        String pos = (int) mc.player.getPosX() + ", " + (int) mc.player.getPosY() + ", " + (int) mc.player.getPosZ();
        font.drawStringWithOutline(e.matrixStack,"Coords: " + pos, x + 3,y, -1);
    }
    public void onKeyBinds(EventRender e) {
        float posX = keyBinds.getX();
        float posY = keyBinds.getY();

        int headerHeight = 14;
        int width = (int) Fonts.rubikSemiBold[14].getWidth(elements.options.get(1).getName()) + 15;
        int padding = 5;
        int offset = 11;

        float height = activeModules * offset;

        this.heightDynamic = AnimationMath.fast(this.heightDynamic, height, 10);

        RenderUtil.Render2D.drawShadow(posX, posY, width, headerHeight + heightDynamic + padding / 2f, 10, ColorUtil.getColorStyle(1), ColorUtil.getColorStyle(50), ColorUtil.getColorStyle(100), ColorUtil.getColorStyle(150));

        RenderUtil.Render2D.drawRoundOutline(posX, posY, width, heightDynamic + headerHeight + 2f, 5, 0, DWTheme.typeColor, new Vector4i(ColorUtil.getColorStyle(1), ColorUtil.getColorStyle(50), ColorUtil.getColorStyle(100), ColorUtil.getColorStyle(150)));

        Fonts.rubikSemiBold[14].drawString(e.matrixStack, "hot keys:", keyBinds.getX() + 18, posY + 5.5f + 1, DWTheme.textColor);
        Fonts.icons[25].drawString(e.matrixStack, "n", keyBinds.getX() + 3, posY + 4.5f, DWTheme.textColor);
        SmartScissor.push();
        SmartScissor.setFromComponentCoordinates(posX, posY, width, headerHeight + heightDynamic + padding / 2f);
        int index = 0;
        for (Function f : Managment.FUNCTION_MANAGER.getFunctions()) {
            if (f.bind != 0 && f.state) {
                String text = ClientUtil.getKey(f.bind);

                String bindText = text.toUpperCase();
                float bindWidth = Fonts.rubikSemiBold[12].getWidth(bindText);
                RenderUtil.Render2D.drawRoundedCorner(posX + 1, posY + 14, width - 2, 1, new Vector4f(0, 0, 0, 0), new Vector4i((ColorUtil.getColorStyle(1)), ColorUtil.getColorStyle(50), ColorUtil.getColorStyle(100), ColorUtil.getColorStyle(150)));

                Fonts.rubikSemiBold[12].drawString(e.matrixStack, f.name, posX + padding, posY + headerHeight + padding + (index * offset), DWTheme.textColor);
                Fonts.rubikSemiBold[12].drawString(e.matrixStack, bindText, posX + width - bindWidth - padding, posY + headerHeight + padding + (index * offset), DWTheme.textColor);
                index++;
            }
        }
        SmartScissor.unset();
        SmartScissor.pop();
        activeModules = index;

        keyBinds.setWidth(width);
        keyBinds.setHeight(activeModules * offset + headerHeight);
    }
    public void onArrayList(EventRender e) {
        float x = 5;
        float y = 25;
        float height = 10;
        float yOffset = 0;

        final StyledFont font = Fonts.rubikSemiBold[14];
        StyleManager styleManager = Managment.STYLE_MANAGER;

        if (delay.hasTimeElapsed(10000)) {
            sortedFunctions = HudUtil.getSorted(font);
            delay.reset();
        }

        float gradientForce = 1;
        int fontOffset = (14 > 14 ? -14 + 14
                - (14 > 14 ? 1 : 0) : 0);
        int firstColor;
        int secondColor;
        for (Function function : sortedFunctions) {
            if ((limitations.get(0) && function.category == Type.Render) || (limitations.get(1) && function.bind == 0)) {
                continue;
            }

            function.animation = AnimationMath.lerp(function.animation, function.state ? 1 : 0, 15);

            if (function.animation >= 0.01) {
                float width = font.getWidth(function.name) + 5;
                firstColor = styleManager.getCurrentStyle()
                        .getColor((int) ((yOffset + height * function.animation) * gradientForce));
                secondColor = styleManager.getCurrentStyle()
                        .getColor((int) (yOffset * gradientForce));

                RenderSystem.pushMatrix();
                RenderSystem.translatef(x + width / 2F, y + yOffset, 0);
                RenderSystem.scalef(1, function.animation, 1);
                RenderSystem.translatef(-(x + width / 2F), -(y + yOffset), 0);
                RenderUtil.Render2D.drawShadow(x, y + yOffset, width, height, 10, firstColor, secondColor);
                RenderSystem.popMatrix();

                yOffset += height * function.animation;
            }
        }
        yOffset = 0;
        for (Function function : sortedFunctions) {
            if ((limitations.get(0) && function.category == Type.Render) || (limitations.get(1) && function.bind == 0)) {
                continue;
            }

            function.animation = AnimationMath.lerp(function.animation, function.state ? 1 : 0, 15);

            if (function.animation >= 0.01) {
                float width = font.getWidth(function.name) + 5;
                secondColor = styleManager.getCurrentStyle()
                        .getColor((int) (yOffset * gradientForce));

                RenderSystem.pushMatrix();
                RenderSystem.translatef(x + width / 2F, y + yOffset, 0);
                RenderSystem.scalef(1, function.animation, 1);
                RenderSystem.translatef(-(x + width / 2F), -(y + yOffset), 0);
                RenderUtil.Render2D.drawRect(x, y + yOffset, width, height, DWTheme.typeColor);
                font.drawString(e.matrixStack, function.name, x + 3,
                        y + yOffset + font.getFontHeight() / 2f - fontOffset - 1, secondColor);
                RenderSystem.popMatrix();

                yOffset += height * function.animation;
            }
        }


        yOffset = 0;
        for (Function function : sortedFunctions) {
            if ((limitations.get(0) && function.category == Type.Render) || (limitations.get(1) && function.bind == 0)) {
                continue;
            }
            function.animation = AnimationMath.lerp(function.animation, function.state ? 1 : 0, 15);
            if (function.animation >= 0.01) {
                float width = font.getWidth(function.name) + 4;
                firstColor = styleManager.getCurrentStyle()
                        .getColor((int) ((yOffset + height * function.animation) * gradientForce));
                secondColor = styleManager.getCurrentStyle()
                        .getColor((int) (yOffset * gradientForce));

                RenderSystem.pushMatrix();
                RenderSystem.translatef(x + width / 2F, y + yOffset, 0);
                RenderSystem.scalef(1, function.animation, 1);
                RenderSystem.translatef(-(x + width / 2F), -(y + yOffset), 0);
                RenderUtil.Render2D.drawShadow(x, y + yOffset, 1, height, 8, firstColor, secondColor);
                RenderUtil.Render2D.drawVertical(x, y + yOffset, 1, height, firstColor, secondColor);
                RenderSystem.popMatrix();

                yOffset += height * function.animation;
            }
        }
    }

    float health = 0;
    public final Dragging targetHUD = Initilization.createDrag(this, "TargetHUD", 500, 50);
    private final Animation targetHudAnimation = new EaseBackIn(200, 1, 1.5f);
    private PlayerEntity target = null;
    private double scale = 0.0D;

    private void onRenderTargetHUD(EventRender eventRender) {
        this.target = getTarget(this.target);
        this.targetHudAnimation.setDuration(300);
        this.scale = targetHudAnimation.getOutput();
        MatrixStack stack = new MatrixStack();
        if (scale == 0.0F) {
            target = null;
        }

        if (target == null) {
            return;
        }

        final String targetName = this.target.getName().getString();

        String substring = targetName.substring(0, Math.min(targetName.length(), 18));
        final float nameWidth = Fonts.rubikSemiBold[18].getWidth(substring);
        final float xPosition = this.targetHUD.getX();
        final float yPosition = this.targetHUD.getY();
        final float maxWidth = 112;
        final float maxHeight = 37;

        // Обновление значения здоровья с анимацией
        this.health = AnimationMath.fast(health, target.getHealth() / target.getMaxHealth(), 5);
        this.health = MathHelper.clamp(this.health, 0, 1);

        GlStateManager.pushMatrix();
        AnimationMath.sizeAnimation(xPosition + (maxWidth / 2), yPosition + (maxHeight / 2), scale * animation);

        RenderUtil.Render2D.drawShadow(xPosition, yPosition, maxWidth, maxHeight, 15, ColorUtil.getColorStyle(1), ColorUtil.getColorStyle(50), ColorUtil.getColorStyle(100), ColorUtil.getColorStyle(150));

        RenderUtil.Render2D.drawGradientRound(xPosition, yPosition, maxWidth, maxHeight, 6, ColorUtil.getColorStyle(1), ColorUtil.getColorStyle(50), ColorUtil.getColorStyle(100), ColorUtil.getColorStyle(150));
        RenderUtil.Render2D.drawRoundedRect(xPosition, yPosition, maxWidth, maxHeight, 5, DWTheme.typeColor);

        RenderUtil.Render2D.drawRoundedRect(xPosition + 36, yPosition + 24, 73, 10f, 4, DWTheme.bgColor);
        RenderUtil.Render2D.drawGradientRound(xPosition + 37, yPosition + 25, health * 70 + 1, 8, 3, ColorUtil.getColorStyle(1), ColorUtil.getColorStyle(50), ColorUtil.getColorStyle(100), ColorUtil.getColorStyle(150));

        // Отрисовка головы
        RenderUtil.Render2D.drawFace(xPosition + 5, yPosition + 5, 8F, 8F, 8F, 8F, 28, 28, 64F, 64F, (AbstractClientPlayerEntity) target);

        Fonts.rubikSemiBold[14].drawString(stack, substring, xPosition + 37, yPosition + 6, DWTheme.textColor);
        String healthValue = (int) MathUtil.round(this.health * 20 + target.getAbsorptionAmount(), 0.5f) + "";
        Fonts.rubikSemiBold[14].drawString(stack, "HP: " + healthValue, xPosition + 37, yPosition + maxHeight / 2 - 2.2f, DWTheme.textColor);
        RenderUtil.Render2D.drawRoundedCorner(xPosition + 88.5f, yPosition + 4, 20, Fonts.rubikSemiBold[14].getFontHeight(), new Vector4f(0,0,0,0), new Vector4i(RenderUtil.reAlphaInt(DWTheme.typeColor, 0), RenderUtil.reAlphaInt(DWTheme.typeColor, 0),DWTheme.typeColor,DWTheme.typeColor));

        GlStateManager.popMatrix();
        this.targetHUD.setWidth(maxWidth);
        this.targetHUD.setHeight(maxHeight);
    }

    private void drawItemStack(float x, float y, float offset) {
        List<ItemStack> stackList = new ArrayList<>(Arrays.asList(target.getHeldItemMainhand(), target.getHeldItemOffhand()));
        stackList.addAll((Collection<? extends ItemStack>) target.getArmorInventoryList());

        final AtomicReference<Float> posX = new AtomicReference<>(x);

        stackList.stream()
                .filter(stack -> !stack.isEmpty())
                .forEach(stack -> HudUtil.drawItemStack(stack,
                        posX.getAndAccumulate(offset, Float::sum),
                        y,
                        true,
                        true, 0.6f));
    }


    private PlayerEntity getTarget(PlayerEntity nullTarget) {
        PlayerEntity target = nullTarget;

        if (Managment.FUNCTION_MANAGER.auraFunction.getTarget() instanceof PlayerEntity) {
            target = (PlayerEntity) Managment.FUNCTION_MANAGER.auraFunction.getTarget();
            targetHudAnimation.setDirection(Direction.FORWARDS);
        } else if (mc.currentScreen instanceof ChatScreen) {
            target = mc.player;
            targetHudAnimation.setDirection(Direction.FORWARDS);
        } else {
            targetHudAnimation.setDirection(Direction.BACKWARDS);
        }

        return target;
    }

    public CopyOnWriteArrayList<net.minecraft.util.text.TextComponent> components = new CopyOnWriteArrayList<>();
    public Dragging staffList = Initilization.createDrag(this, "StaffList", 350, 50);

    private int activeStaff = 0;
    private float hDynam = 0;
    private float widthDynamic = 0;
    private float nameWidth = 0;
    List<StaffPlayer> staffPlayers = new ArrayList<>();

    private void onStaffListRender(EventRender render) {
        float posX = staffList.getX();
        float posY = staffList.getY();
        MatrixStack matrixStack = new MatrixStack();

        int headerHeight = 14;
        float width = 90;
        int padding = 5;
        int offset = 11;

        float height = activeStaff * offset;

        this.hDynam = AnimationMath.fast(this.hDynam, height, 10);
        this.widthDynamic = AnimationMath.fast(this.widthDynamic, width, 10);
        RenderUtil.Render2D.drawShadow(posX, posY, widthDynamic, headerHeight + hDynam + padding / 2f, 10, ColorUtil.getColorStyle(1), ColorUtil.getColorStyle(50), ColorUtil.getColorStyle(100), ColorUtil.getColorStyle(150));

        RenderUtil.Render2D.drawRoundOutline(posX, posY, widthDynamic, hDynam + headerHeight + 2.5f, 5, 0, DWTheme.typeColor, new Vector4i(ColorUtil.getColorStyle(1), ColorUtil.getColorStyle(50), ColorUtil.getColorStyle(100), ColorUtil.getColorStyle(150)));

        Fonts.rubikSemiBold[14].drawString(matrixStack, "staff list:", posX + 15, posY + 6.5f, DWTheme.textColor);
        Fonts.icons[20].drawString(matrixStack, "d", posX + 3, posY + 6f, DWTheme.textColor);
        int index = 0;

        SmartScissor.push();
        SmartScissor.setFromComponentCoordinates(posX, posY, widthDynamic, headerHeight + hDynam + padding / 2f);
        if (!staffPlayers.isEmpty()) {
            for (StaffPlayer staff : staffPlayers) {
                String name = staff.getName();
                ITextComponent prefix = staff.getPrefix();
                String status = staff.getStatus().getString();
                RenderUtil.Render2D.drawRoundedCorner(posX + 1, posY + 14, width - 2, 1, new Vector4f(0,0,0,0), new Vector4i((ColorUtil.getColorStyle(1)), ColorUtil.getColorStyle(50), ColorUtil.getColorStyle(100), ColorUtil.getColorStyle(150)));

                Fonts.rubikSemiBold[12].drawString(matrixStack, prefix, posX + padding, posY + headerHeight + padding + (index * offset), DWTheme.textColor);
                Fonts.rubikSemiBold[12].drawString(matrixStack, name + status, posX + padding + Fonts.rubikSemiBold[12].getWidth(prefix.getString()), posY + headerHeight + padding + (index * offset), DWTheme.textColor);
                nameWidth = Fonts.rubikSemiBold[12].getWidth(prefix.getString() + name + status);
                index++;
            }
        } else {
            nameWidth = 0;
        }
        SmartScissor.unset();
        SmartScissor.pop();

        activeStaff = index;
        staffList.setWidth(widthDynamic);
        staffList.setHeight(hDynam + headerHeight);
    }

    private class StaffPlayer {

        @Getter
        String name;
        @Getter
        ITextComponent prefix;
        @Getter
        Status status;

        private StaffPlayer(String name, ITextComponent prefix) {
            this.name = name;
            this.prefix = prefix;

            updateStatus();
        }

        private void updateStatus() {
            for (AbstractClientPlayerEntity player : mc.world.getPlayers()) {
                if (player.getNameClear().equals(name)) {
                    status = Status.NEAR;
                    return;
                }
            }

            for (NetworkPlayerInfo info : mc.getConnection().getPlayerInfoMap()) {
                if (info.getGameProfile().getName().equals(name)) {
                    if (info.getGameType() == GameType.SPECTATOR) {
                        status = Status.SPEC;
                        return;
                    }

                    status = Status.NONE;
                    return;
                }
            }

            status = Status.VANISHED;
        }
    }

    public enum Status {
        NONE(""),
        NEAR(" §e[N]"),
        SPEC(" §c[S]"),
        VANISHED(" §6[V]");

        @Getter
        final String string;

        Status(String string) {
            this.string = string;
        }
    }
}
