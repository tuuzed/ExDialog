package com.tuuzed.androidx.exdialog.compat;

import android.content.res.ColorStateList;
import android.widget.Button;
import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.button.MaterialButton;

public class DialogButtonCompat {

    public static void setRippleColor(@Nullable Button button, @ColorInt int color) {
        setRippleColor(button, defaultRippleColor(color));
    }

    public static void setRippleColor(@Nullable Button button, @NonNull ColorStateList colorStateList) {
        if (button == null) return;
        if (button instanceof MaterialButton) {
            ((MaterialButton) button).setRippleColor(colorStateList);
        }
    }

    public static void setTextColor(@Nullable Button button, @ColorInt int color) {
        setTextColor(button, defaultTextColor(color));
    }

    public static void setTextColor(@Nullable Button button, @NonNull ColorStateList colorStateList) {
        if (button == null) return;
        button.setTextColor(colorStateList);
    }

    @NonNull
    private static ColorStateList defaultRippleColor(@ColorInt int color) {
        int[][] states = new int[][]{
                {android.R.attr.state_pressed},
                {android.R.attr.state_focused, android.R.attr.state_hovered},
                {android.R.attr.state_focused},
                {android.R.attr.state_hovered},
                {}
        };
        float r = ColorCompat.red(color);
        float g = ColorCompat.green(color);
        float b = ColorCompat.blue(color);
        int[] colors = new int[]{
                ColorCompat.color(0.16f, r, g, b),
                ColorCompat.color(0.12f, r, g, b),
                ColorCompat.color(0.12f, r, g, b),
                ColorCompat.color(0.04f, r, g, b),
                ColorCompat.color(0.00f, r, g, b),
        };
        return new ColorStateList(states, colors);
    }

    @NonNull
    private static ColorStateList defaultTextColor(@ColorInt int color) {
        int[][] states = new int[][]{
                {android.R.attr.state_enabled},
                {}
        };
        float r = ColorCompat.red(color);
        float g = ColorCompat.green(color);
        float b = ColorCompat.blue(color);
        int[] colors = new int[]{
                ColorCompat.color(1.00f, r, g, b),
                ColorCompat.color(0.50f, r, g, b),
        };
        return new ColorStateList(states, colors);
    }

}
