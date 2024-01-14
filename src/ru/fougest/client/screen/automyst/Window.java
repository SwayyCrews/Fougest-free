package ru.fougest.client.screen.automyst;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.AirItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import org.joml.Vector4i;
import org.lwjgl.glfw.GLFW;
import ru.fougest.main.managment.Managment;
import ru.fougest.client.util.ClientUtil;
import ru.fougest.client.util.font.Fonts;
import ru.fougest.client.util.misc.HudUtil;
import ru.fougest.client.util.render.*;

import java.awt.*;
import java.io.*;
import java.util.*;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import static net.minecraft.util.registry.Registry.ITEM;
import static ru.fougest.client.util.render.RenderUtil.Render2D.drawRoundedRect;
import static ru.fougest.client.util.IMinecraft.mc;

public class Window extends Screen {
    public Window(ITextComponent titleIn) {
        super(titleIn);
        createFileIfNotExists();
    }
    private boolean openedAdd;
    public String name = "";
    public boolean nameTyping;
    private List<ItemStack> filteredItems = new ArrayList<>();
    private int maxVisibleItems = 7;
    private int startIndex = 0;
    private int savedItemsStartIndex = 0;
    private final int maxVisibleItemsFirst = 5;
    private String activeButton = "Свой конфиг";

    @Override
    public boolean charTyped(char codePoint, int modifiers) {
        if (nameTyping) {
            if (codePoint == '\b' && !name.isEmpty()) {
                name = name.substring(0, name.length() - 1);
                return true;
            } else if (Character.isLetterOrDigit(codePoint) || Character.isWhitespace(codePoint)) {
                name += codePoint;
                return true;
            }
        }
        return super.charTyped(codePoint, modifiers);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (nameTyping) {
            if (keyCode == GLFW.GLFW_KEY_ENTER) {
                nameTyping = false;
                return true;
            } else if (keyCode == GLFW.GLFW_KEY_BACKSPACE && !name.isEmpty()) {
                name = name.substring(0, name.length() - 1);
                return true;
            } else if (keyCode == GLFW.GLFW_KEY_A && (modifiers & GLFW.GLFW_MOD_CONTROL) != 0) {
                name = "";
                return true;
            }
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }
    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        float width = 318 / 2F;
        float heigth = 339 / 2F;
        float x = this.width / 2f - width / 2f + (openedAdd ? (width / 2f ) : 0);
        float y = this.height / 2f - heigth / 2f;
        float xA = this.width / 2f - width / 2f - (openedAdd ? (width / 2f + 10) : 0);

        GaussianBlur.startBlur();
        drawRoundedRect(x + width + 10, y , width - 90, 65, 4, ColorUtil.rgba(25, 25, 25, 150));
        GaussianBlur.endBlur(50, 3);
        drawRoundedRect(x + width + 10, y , width - 90, 65, 4, ColorUtil.rgba(25, 25, 25, 150));


        if (openedAdd) {
            GaussianBlur.startBlur();
            drawRoundedRect(xA, y, width, heigth + 50, 4, ColorUtil.rgba(25, 25, 25, 150));
            GaussianBlur.endBlur(50, 3);
            drawRoundedRect(xA, y, width, heigth + 50, 4, ColorUtil.rgba(25, 25, 25, 150));
            if (openedAdd) {
                filteredItems.clear();
                String searchText = name.toLowerCase();
                for (Item item : ITEM) {
                    ItemStack itemStack = new ItemStack(item);
                    String itemName = itemStack.getDisplayName().getString().toLowerCase();
                    if (!itemStack.isEmpty() && itemName.contains(searchText)) {
                        filteredItems.add(itemStack);
                    }
                }
                drawFilteredItemStacks(matrixStack, xA + 15, y + 50, 22);
            } else {
                drawAllItemStacks(matrixStack, xA + 15, y + 50, 22);
            }
            drawRoundedRect(xA + 10, y + 29, 140, 15, 2.5F, ColorUtil.rgba(0, 0, 0, 128));

            Fonts.rubikSemiBold[16].drawString(matrixStack,  ClientUtil.gradient("Добавить предмет", ColorUtil.getColorStyle(0), ColorUtil.getColorStyle(240)), xA + 7, y + 7, -1);
            //Безумие... Это действия... Которые снова и снова повторяются... снова... и снова...
            if (nameTyping) {
                Fonts.rubikSemiBold[13].drawString(matrixStack,  ClientUtil.gradient("Поиск...", ColorUtil.getColorStyle(0), ColorUtil.getColorStyle(240)), xA + 18, y + 23, ColorUtil.rgba(255, 255, 255, 128));
            } else if (!nameTyping && name.isEmpty()) {
                Fonts.rubikSemiBold[13].drawString(matrixStack,  ClientUtil.gradient("Поиск...", ColorUtil.getColorStyle(0), ColorUtil.getColorStyle(240)), xA + 18, y + 35, ColorUtil.rgba(255, 255, 255, 128));
            }
            if (nameTyping || (!nameTyping && !name.isEmpty())) {
                Fonts.rubikSemiBold[13].drawString(matrixStack,  ClientUtil.gradient("Поиск...", ColorUtil.getColorStyle(0), ColorUtil.getColorStyle(240)), xA + 18, y + 23, ColorUtil.rgba(255, 255, 255, 128));
            }
            Fonts.rubikSemiBold[15].drawString(matrixStack, name + (nameTyping ? System.currentTimeMillis() % 1000 > 500 ? "" : "_" : ""), xA + 18, y + 35, -1);
            drawRoundedRect(xA + width  - 45, y + 5, 39, 10, 2.5F, ColorUtil.rgba(0, 0, 0, 128));
            Fonts.rubikSemiBold[15].drawString(matrixStack,  "Закрыть", xA + width  - 43, y + 8, -1);



        }


        GaussianBlur.startBlur();
        drawRoundedRect(x + 3, y, width, heigth, 4, ColorUtil.rgba(25, 25, 25, 150));
        GaussianBlur.endBlur(50, 1);
        drawRoundedRect(x + 3, y, width, heigth, 4, ColorUtil.rgba(25, 25, 25, 150));
        Fonts.rubikSemiBold[16].drawString(matrixStack, "Очень умный Честстилер", x + 10, y + 7, -1);
        drawRoundedRect(x + 15, y + heigth - (27 / 2f) - 7, 137, 27 / 2f, 4, ColorUtil.rgba(0, 0, 0, 128));
        Fonts.rubikSemiBold[14].drawCenteredString(matrixStack,"Добавить", x + 168 / 2, y + heigth - (27 / 2f) - 2.5F, -1);
        float savedItemsX = x + width / 2.0f + (openedAdd ? 10 : 0);
        float savedItemsY = y + 50;
        drawSavedItems(matrixStack, savedItemsX, savedItemsY, 22);








        int centerX0 = (int) (x + width + 15 + 56 / 2f);
        int color0 = activeButton.equals("Свой конфиг") ? Color.RED.getRGB() : -1;
        drawRoundedRect(x + width + 15, y + 5, 56, 10, 2.5F, ColorUtil.rgba(0, 0, 0, 128));
        Fonts.rubikSemiBold[13].drawCenteredString(matrixStack, "Свой конфиг", centerX0, (int) (y + 8.5), color0);

        int centerX1 = (int) (x + width + 15 + 56 / 2f);
        int color1 = activeButton.equals("Ценное") ? Color.RED.getRGB() : -1;
        drawRoundedRect(x + width + 15, y + 5 + 15, 56, 10, 2.5F, ColorUtil.rgba(0, 0, 0, 128));
        Fonts.rubikSemiBold[13].drawCenteredString(matrixStack, "Ценное", centerX1, (int) (y + 8.5 + 15), color1);

        int centerX2 = (int) (x + width + 15 + 56 / 2f);
        int color2 = activeButton.equals("Броня") ? Color.RED.getRGB() : -1;
        drawRoundedRect(x + width + 15, y + 5 + 30, 56, 10, 2.5F, ColorUtil.rgba(0, 0, 0, 128));
        Fonts.rubikSemiBold[13].drawCenteredString(matrixStack, "Броня", centerX2, (int) (y + 8.5 + 30), color2);

        int centerX3 = (int) (x + width + 15 + 56 / 2f);
        int color3 = activeButton.equals("Инструменты") ? Color.RED.getRGB() : -1;
        drawRoundedRect(x + width + 15, y + 5 + 45, 56, 10, 2.5F, ColorUtil.rgba(0, 0, 0, 128));
        Fonts.rubikSemiBold[13].drawCenteredString(matrixStack, "Инструменты", centerX3, (int) (y + 8.5 + 45), color3);


    }

    private void drawSavedItems(MatrixStack matrixStack, float x, float y, float offset) {
        List<String> savedItems = readSavedItemsFromFile();
        float width = 318 / 2F;
        float xZ = this.width / 2f - width / 2f + (openedAdd ? (width / 2f ) : 0);
        final AtomicReference<Float> posY = new AtomicReference<>(y);
        for (int i = savedItemsStartIndex; i < savedItems.size() && i < savedItemsStartIndex + maxVisibleItemsFirst; i++) {
            String itemID = savedItems.get(i);
            Item item = ITEM.getOrDefault(new ResourceLocation(itemID));
            if (item == null) continue;
            ItemStack stack = new ItemStack(item);
            HudUtil.drawItemStack(stack, xZ + 15, posY.getAndAccumulate(offset, Float::sum) - 25, false, true, 1.3f);
            String itemName = stack.getDisplayName().getString();
            Fonts.rubikSemiBold[12].drawString(matrixStack, itemName, xZ + 37, posY.get() - 40, -1);
            Fonts.rubikSemiBold[12].drawString(matrixStack, itemID, xZ + 37, posY.get() - 34, new Color(14, 255, 96, 128).getRGB());
        }
    }

    private void drawFilteredItemStacks(MatrixStack matrixStack, float x, float y, float offset) {
        final AtomicReference<Float> posY = new AtomicReference<>(y);
        filteredItems.clear();
        String searchText = name.toLowerCase();
        for (Item item : ITEM) {
            ItemStack itemStack = new ItemStack(item);
            String itemName = itemStack.getDisplayName().getString().toLowerCase();
            String itemID = ITEM.getKey(item).getPath();
            if (!itemStack.isEmpty() && itemName.contains(searchText) && !isItemSaved(itemID)) {
                filteredItems.add(itemStack);
            }
        }
        for (int i = startIndex; i < filteredItems.size() && i < startIndex + maxVisibleItems; i++) {
            ItemStack stack = filteredItems.get(i);
            String itemID = ITEM.getKey(stack.getItem()).getPath();
            String itemName = stack.getDisplayName().getString();
            int searchStart = itemName.toLowerCase().indexOf(searchText);
            int searchEnd = searchStart + searchText.length();

            HudUtil.drawItemStack(stack, x, posY.getAndAccumulate(offset, Float::sum), false, true, 1.3f);
            Fonts.rubikSemiBold[12].drawString(matrixStack, itemName.substring(0, searchStart), x + offset, posY.get() - 16, -1);
            Fonts.rubikSemiBold[12].drawString(matrixStack, itemName.substring(searchStart, searchEnd), x + offset + Fonts.rubikSemiBold[12].getWidth(itemName.substring(0, searchStart)), posY.get() - 16, Color.RED.getRGB());
            Fonts.rubikSemiBold[12].drawString(matrixStack, itemName.substring(searchEnd), x + offset + Fonts.rubikSemiBold[12].getWidth(itemName.substring(0, searchEnd)), posY.get() - 16, -1);
            Fonts.rubikSemiBold[12].drawString(matrixStack, itemID, x + offset, posY.get() - 10, new Color(14, 255, 96, 128).getRGB());
        }
        if (filteredItems.isEmpty()) {
            Fonts.rubikSemiBold[12].drawString(matrixStack, "Предметов не обнаружено", x + offset, posY.get() + 5, Color.RED.getRGB());
        }
        if (!filteredItems.isEmpty()) {
            updateFilteredItems(name.toLowerCase());
        }
    }
    private void drawAllItemStacks(MatrixStack matrixStack, float x, float y, float offset) {
        List<ItemStack> stacks = new ArrayList<>();
        for (Item item : ITEM) {
            ItemStack itemStack = new ItemStack(item);
            if (!itemStack.isEmpty()) {
                stacks.add(itemStack);
            }
        }
        stacks.removeIf(w -> w.getItem() instanceof AirItem);
        final AtomicReference<Float> posY = new AtomicReference<>(y);
        for (int i = startIndex; i < stacks.size() && i < startIndex + maxVisibleItems; i++) {
            ItemStack stack = stacks.get(i);
            String itemID = ITEM.getKey(stack.getItem()).getPath();
            HudUtil.drawItemStack(stack, x, posY.getAndAccumulate(offset, Float::sum), false, true, 1.3f);
            Fonts.rubikSemiBold[12].drawString(matrixStack, stack.getDisplayName().getString(), x + offset, posY.get() - 16, -1);
            Fonts.rubikSemiBold[12].drawString(matrixStack, itemID, x + offset, posY.get() - 10, new Color(14, 255, 96, 128).getRGB());
        }
    }
    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
        float width = 318 / 2F;
        float height = 339 / 2F;
        float xA = this.width / 2f - width / 2f - (openedAdd ? (width / 2f + 10) : 0);
        float yA = this.height / 2f - height / 2f + 45;
        float x1 = this.width / 2f - width / 2f + (openedAdd ? (width / 2f + 10) : 0);
        float heigth = 339 / 2F;
        float y1 = this.height / 2f - heigth / 2f;
        float savedItemsX = x1 + 10;
        float savedItemsY = y1 + 20;
        int maxSavedItemsStartIndex = getMaxSavedItemsStartIndex();

        if (openedAdd && mouseX >= xA && mouseX <= xA + width && mouseY >= yA && mouseY <= yA + height) {
            int maxStartIndex = getMaxFilteredItemsStartIndex();
            startIndex = Math.max(0, Math.min(startIndex - (int) delta, maxStartIndex));
            maxVisibleItems = Math.min(filteredItems.size(), maxVisibleItems);
            return true;
        }
        if (mouseX >= savedItemsX && mouseX <= savedItemsX + 135 && mouseY >= savedItemsY && mouseY <= savedItemsY + 120) {
            savedItemsStartIndex = Math.max(0, Math.min(savedItemsStartIndex - (int) delta, maxSavedItemsStartIndex));
            return true;
        }
        if (openedAdd && mouseX >= xA + 15 && mouseX <= xA + width - 25 && mouseY >= yA + 50 && mouseY <= yA + height - 20) {
            int maxFilteredItemsStartIndex = getMaxFilteredItemsStartIndex();
            startIndex = Math.max(0, Math.min(startIndex - (int) delta, maxFilteredItemsStartIndex));
            return true;
        }
        if (!openedAdd) {
            float savedItemsX1 = x1 + 10;
            float savedItemsY1 = y1 + 20;
            if (mouseX >= savedItemsX1 && mouseX <= savedItemsX1 + 135 &&
                    mouseY >= savedItemsY1 && mouseY <= savedItemsY1 + 120) {
                savedItemsStartIndex = Math.max(0, Math.min(savedItemsStartIndex - (int) delta, maxSavedItemsStartIndex));
                return true;
            }
        }
        return super.mouseScrolled(mouseX, mouseY, delta);
    }
    private int getMaxFilteredItemsStartIndex() {
        return Math.max(0, filteredItems.size() - maxVisibleItems);
    }
    private int getMaxSavedItemsStartIndex() {
        List<String> savedItems = readSavedItemsFromFile();
        return Math.max(0, savedItems.size() - maxVisibleItemsFirst);
    }
    private void createFileIfNotExists() {
        File file = new File("expensive/AutoMist.foug");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    private void updateFilteredItems(String searchText) {
        List<ItemStack> newFilteredItems = new ArrayList<>();
        for (Item item : Registry.ITEM) {
            ItemStack itemStack = new ItemStack(item);
            String itemName = itemStack.getDisplayName().getString().toLowerCase();
            String itemID = Registry.ITEM.getKey(item).getPath();
            if (!itemStack.isEmpty() && itemName.contains(searchText) && !isItemSaved(itemID)) {
                newFilteredItems.add(itemStack);
            }
        }
        if (!newFilteredItems.isEmpty()) {
            filteredItems = newFilteredItems;
        }
    }
    private boolean isItemSaved(String itemID) {
        List<String> savedItems = readSavedItemsFromFile();
        return savedItems.contains(itemID);
    }
    private void saveItemToFile(ItemStack stack) {
        if (stack.isEmpty()) {
            return;
        }
        String itemID = Registry.ITEM.getKey(stack.getItem()).getPath();
        System.out.println("Добавил предмет: " + itemID);
        List<String> existingItems = new ArrayList<>();
        try (Scanner scanner = new Scanner(new File("expensive/AutoMist.foug"))) {
            while (scanner.hasNextLine()) {
                existingItems.add(scanner.nextLine());
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        if (!existingItems.contains(itemID)) {
            try (FileWriter writer = new FileWriter("expensive/AutoMist.foug", true)) {
                writer.write(itemID + System.lineSeparator());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        updateFilteredItems(name.toLowerCase());
    }
    private List<String> readSavedItemsFromFile() {
        List<String> savedItems = new ArrayList<>();
        try (Scanner scanner = new Scanner(new File("expensive/AutoMist.foug"))) {
            while (scanner.hasNextLine()) {
                savedItems.add(scanner.nextLine());
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return savedItems;
    }
    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        float width = 318 / 2F;
        float height = 339 / 2F;
        float x = this.width / 2f - width / 2f + (openedAdd ? (width / 2f + 10) : 0);
        float y = this.height / 2f - height / 2f;
        boolean closeButtonClicked;
        float xA = this.width / 2f - width / 2f - (openedAdd ? (width / 2f + 10) : 0);
        float savedItemsX = x + 10;
        float savedItemsY = y + 20;


        if (RenderUtil.isInRegion(mouseX, mouseY, x + width + 15, y + 5, 56, 10)) {
            activeButton = "Свой конфиг";
            return true;
        }
        if (RenderUtil.isInRegion(mouseX, mouseY, x + width + 15, y + 5 + 15, 56, 10)) {
            activeButton = "Ценное";
            Item[] UGHHHH = {
                    Items.DIAMOND_BLOCK,
                    Items.DIAMOND,
                    Items.DIAMOND_ORE,
                    Items.NETHERITE_INGOT,
                    Items.NETHERITE_SCRAP,
                    Items.PLAYER_HEAD,
                    Items.TOTEM_OF_UNDYING,
                    Items.GOLD_BLOCK,
                    Items.GOLD_INGOT,
                    Items.ENCHANTED_GOLDEN_APPLE,
                    Items.GOLDEN_APPLE,
                    Items.NETHER_STAR,
                    Items.DRAGON_HEAD,
                    Items.CREEPER_HEAD,
                    Items.ZOMBIE_HEAD,
                    Items.WITHER_SKELETON_SKULL
            };

            for (Item armorItem : UGHHHH) {
                saveItemToFile(new ItemStack(armorItem));
            }

            return true;
        }
        if (RenderUtil.isInRegion(mouseX, mouseY, x + width + 15, y + 5 + 30, 56, 10)) {
            activeButton = "Броня";
            Item[] armorItems = {
                    Items.NETHERITE_HELMET,
                    Items.NETHERITE_CHESTPLATE,
                    Items.NETHERITE_LEGGINGS,
                    Items.NETHERITE_BOOTS,
                    Items.DIAMOND_HELMET,
                    Items.DIAMOND_CHESTPLATE,
                    Items.DIAMOND_LEGGINGS,
                    Items.DIAMOND_BOOTS

            };

            for (Item armorItem : armorItems) {
                saveItemToFile(new ItemStack(armorItem));
            }

            return true;
        }

        if (RenderUtil.isInRegion(mouseX, mouseY, x + width + 15, y + 5 + 45, 56, 10)) {
            activeButton = "Инструменты";
            Item[] UGHHHH = {
                    Items.DIAMOND_SWORD,
                    Items.DIAMOND_PICKAXE,
                    Items.DIAMOND_AXE,
                    Items.NETHERITE_SWORD,
                    Items.NETHERITE_PICKAXE,
                    Items.NETHERITE_AXE

            };

            for (Item armorItem : UGHHHH) {
                saveItemToFile(new ItemStack(armorItem));
            }

            return true;
        }


        if (button == GLFW.GLFW_MOUSE_BUTTON_RIGHT &&
                mouseX >= savedItemsX && mouseX <= savedItemsX + 135 &&
                mouseY >= savedItemsY && mouseY <= savedItemsY + 120) {
            int clickedIndex = (int) ((mouseY - savedItemsY) / 22);
            int actualIndex = savedItemsStartIndex + clickedIndex;

            List<String> savedItems = readSavedItemsFromFile();

            if (actualIndex >= 0 && actualIndex < savedItems.size()) {
                String removedItem = savedItems.remove(actualIndex);
                saveItemsToFile(savedItems);
                System.out.println("Задоксил предмет: " + removedItem);
            }
            return true;
        }
        if (RenderUtil.isInRegion(mouseX, mouseY, x + 15, y + height - (27 / 2f) - 8, 136, 27 / 2f)) {
            openedAdd = true;
            return true;
        }
        if (!openedAdd) {
            return super.mouseClicked(mouseX, mouseY, button);
        }
        if (RenderUtil.isInRegion(mouseX, mouseY, xA + width - 45, y + 5, 39, 10)) {
            openedAdd = false;
            nameTyping = false;
            return true;
        }
        if (RenderUtil.isInRegion(mouseX, mouseY, xA + 15, y + 30, width - 25, 15)) {
            nameTyping = !nameTyping;
        }
        if (openedAdd) {
            if (RenderUtil.isInRegion(mouseX, mouseY, xA + 15, y + 50, width - 25, height - 20)) {
                int clickedIndex = (int) ((mouseY - (y + 50)) / 22);
                int actualIndex = startIndex + clickedIndex;
                if (actualIndex >= 0 && actualIndex < filteredItems.size()) {
                    ItemStack clickedStack = filteredItems.get(actualIndex);
                    saveItemToFile(clickedStack);
                }
                return true;
            }
        }
        filteredItems.clear();
        String searchText = name.toLowerCase();
        for (Item item : Registry.ITEM) {
            ItemStack itemStack = new ItemStack(item);
            String itemName = itemStack.getDisplayName().getString().toLowerCase();
            if (!itemStack.isEmpty() && itemName.contains(searchText)) {
                filteredItems.add(itemStack);
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }
    private void saveItemsToFile(List<String> items) {
        try (FileWriter writer = new FileWriter("expensive/AutoMist.foug")) {
            for (String item : items) {
                writer.write(item + System.lineSeparator());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}