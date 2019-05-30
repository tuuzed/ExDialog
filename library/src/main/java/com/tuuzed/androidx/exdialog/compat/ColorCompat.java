package com.tuuzed.androidx.exdialog.compat;

import androidx.annotation.ColorInt;
import androidx.annotation.FloatRange;
import androidx.annotation.IntRange;

public class ColorCompat {

    public static float alpha(@ColorInt int color) {
        return (color >> 24 & 0xFF) / 255f;
    }

    public static float red(@ColorInt int color) {
        return (color >> 16 & 0xFF) / 255f;
    }

    public static float green(@ColorInt int color) {
        return (color >> 8 & 0xFF) / 255f;
    }

    public static float blue(@ColorInt int color) {
        return (color & 0xFF) / 255f;
    }

    @ColorInt
    public static int color(
            @IntRange(from = 0, to = 255) int a,
            @IntRange(from = 0, to = 255) int r,
            @IntRange(from = 0, to = 255) int g,
            @IntRange(from = 0, to = 255) int b
    ) {
        return a << 24 | (r << 16) | (g << 8) | b;
    }

    @ColorInt
    public static int color(
            @FloatRange(from = 0.0, to = 1.0) float a,
            @FloatRange(from = 0.0, to = 1.0) float r,
            @FloatRange(from = 0.0, to = 1.0) float g,
            @FloatRange(from = 0.0, to = 1.0) float b
    ) {
        return (int) (a * 255.0f + 0.5f) << 24 |
                ((int) (r * 255.0f + 0.5f) << 16) |
                ((int) (g * 255.0f + 0.5f) << 8) |
                (int) (b * 255.0f + 0.5f);
    }
}

