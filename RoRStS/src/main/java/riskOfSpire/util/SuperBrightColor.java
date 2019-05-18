package riskOfSpire.util;

import com.badlogic.gdx.graphics.Color;

public class SuperBrightColor extends Color {
    public SuperBrightColor(float r, float g, float b, float a) {
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
    }

    @Override
    public Color clamp() {
        return this;
    }
}
