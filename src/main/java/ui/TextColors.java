package ui;


import javax.swing.*;
import java.awt.*;

public enum TextColors {
    DEFAULT(UIManager.getColor("Label.foreground")),
    HOVER(new Color(160, 160, 160)),
    PLACEHOLDER(Color.GRAY),
    ERROR(Color.RED);

    private final Color darkColor;
    private final Color lightColor;

    TextColors(Color darkColor) {
        this.darkColor = darkColor;
        this.lightColor = new Color(255 - darkColor.getRed(), 255 - darkColor.getGreen(), 255 - darkColor.getBlue());
    }

    public Color color() {
        return darkColor;
    }
}
