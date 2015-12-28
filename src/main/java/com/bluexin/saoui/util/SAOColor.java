package com.bluexin.saoui.util;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public final class SAOColor {

    public static final int DEFAULT_COLOR = 0xFFFFFFFF;
    public static final int DEFAULT_FONT_COLOR = 0x888888FF;
    public static final int DEFAULT_BOX_COLOR = 0xBBBBBBFF;
    public static final int DEFAULT_BOX_FONT_COLOR = 0x555555FF;

    public static final int HOVER_COLOR = 0xE0BE62FF;
    public static final int HOVER_FONT_COLOR = 0xFFFFFFFF;

    public static final int DISABLED_MASK = 0xFFFFFF42;

    public static final int CONFIRM_COLOR = 0x4782E3FF;
    public static final int CONFIRM_COLOR_LIGHT = 0x629DFFFF;

    public static final int CANCEL_COLOR = 0xE34747FF;
    public static final int CANCEL_COLOR_LIGHT = 0xFF6262FF;

    public static final int CURSOR_COLOR = 0x8EE1E8;

    public static final int DEAD_COLOR = 0x0033ccFF;
    public static final int HARDCORE_DEAD_COLOR = 0xC94141FF;

    public static int multiplyAlpha(int rgba, float alpha) {
        final int value = (int) (((rgba) & 0xFF) * alpha);

        return (rgba & 0xFFFFFF00) | (value & 0xFF);
    }

    private SAOColor() {
    }

    public static int mediumColor(int rgba0, int rgba1) {
        return (
                (((((rgba0 >> 24) & 0xFF) + ((rgba1 >> 24) & 0xFF)) / 2) << 24) |
                        (((((rgba0 >> 16) & 0xFF) + ((rgba1 >> 16) & 0xFF)) / 2) << 16) |
                        (((((rgba0 >> 8) & 0xFF) + ((rgba1 >> 8) & 0xFF)) / 2) << 8) |
                        (((((rgba0) & 0xFF) + ((rgba1) & 0xFF)) / 2))
        );
    }

}