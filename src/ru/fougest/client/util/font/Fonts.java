package ru.fougest.client.util.font;

import lombok.SneakyThrows;
import ru.fougest.client.util.font.common.Lang;
import ru.fougest.client.util.font.styled.StyledFont;


public class Fonts {
    public static final String FONT_DIR = "/assets/minecraft/expensive/font/";

    public static volatile StyledFont[] minecraft = new StyledFont[24];
    public static volatile StyledFont[] rubikSemiBold = new StyledFont[80];
    public static volatile StyledFont[] icons = new StyledFont[130];
    public static volatile StyledFont[] icons1 = new StyledFont[131];

    @SneakyThrows
    public static void init() {
        long time = System.currentTimeMillis();

        minecraft[8] = new StyledFont("mc.ttf", 8, 0.0f, 0.0f, 0.0f, false, Lang.ENG_RU);
        for (int i = 8; i < 131; i++) {
            icons1[i] = new StyledFont("icons.ttf", i, 0.0f, 0.0f, 0.0f, true, Lang.ENG_RU);
        }
        for (int i = 8; i < 130; i++) {
            icons[i] = new StyledFont("icons1.ttf", i, 0.0f, 0.0f, 0.0f, true, Lang.ENG_RU);
        }
        for (int i = 1; i < 80; i++) {
            rubikSemiBold[i] = new StyledFont("rubik-bold.ttf", i, 0.0f, 0.0f, 0.0f, true, Lang.ENG_RU);
        }
    }
}