package ru.fougest.client.screen.theme.DWTheme;

import java.awt.*;

public class DWTheme {
    public static int bgColor;
    public static int lineColor;
    public static int typeColor;
    public static int textColor;
    public DWTheme(boolean true_or_false) {
        if (true_or_false) {
            lineColor = new Color(55,55,55).getRGB();
            typeColor = new Color(15,15,15).getRGB();
            bgColor = new Color(30,30,30).getRGB();
            textColor = new Color(255,255,255).getRGB();
        } else {
            lineColor = new Color(158,158,158).getRGB();
            typeColor = new Color(230,230,230).getRGB();
            bgColor = new Color(205,205,205).getRGB();
            textColor = new Color(0,0,0).getRGB();
        }
    }
    public void setTheme(boolean n) {
        new DWTheme(n);
    }
}
