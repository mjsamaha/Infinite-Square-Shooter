package com.lobsterchops.infinitesquareshooter.utils;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;

public class VerticalLayout {

    private final Graphics2D g2;
    private int x;
    private int y;
    private int spacing;

    public VerticalLayout(Graphics2D g2, int startX, int startY, int spacing) {
        this.g2 = g2;
        this.x = startX;
        this.y = startY;
        this.spacing = spacing;
    }

    public void text(String text, Font font, Color color) {
        g2.setFont(font);
        g2.setColor(color);

        FontMetrics metrics = g2.getFontMetrics();
        int textWidth = metrics.stringWidth(text);

        g2.drawString(text, x - textWidth / 2, y);

        y += spacing;
    }

    public void space(int extra) {
        y += extra;
    }
}