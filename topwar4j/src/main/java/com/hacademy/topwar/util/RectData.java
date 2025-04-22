package com.hacademy.topwar.util;

import java.awt.Rectangle;

public class RectData {
    public int x;
    public int y;
    public int width;
    public int height;

    public RectData() {}

    public RectData(Rectangle r) {
        this.x = r.x;
        this.y = r.y;
        this.width = r.width;
        this.height = r.height;
    }

    public Rectangle toRectangle() {
        return new Rectangle(x, y, width, height);
    }
}
