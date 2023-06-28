package de.ambertation.wunderlib.ui;

import net.minecraft.ChatFormatting;
import net.minecraft.util.FastColor;

public class ColorHelper {
    public static final int BLACK = ChatFormatting.BLACK.getColor() | 0xFF000000;
    public static final int DARK_BLUE = ChatFormatting.DARK_BLUE.getColor() | 0xFF000000;
    public static final int DARK_GREEN = ChatFormatting.DARK_GREEN.getColor() | 0xFF000000;
    public static final int DARK_AQUA = ChatFormatting.DARK_AQUA.getColor() | 0xFF000000;
    public static final int DARK_RED = ChatFormatting.DARK_RED.getColor() | 0xFF000000;
    public static final int DARK_PURPLE = ChatFormatting.DARK_PURPLE.getColor() | 0xFF000000;
    public static final int GOLD = ChatFormatting.GOLD.getColor() | 0xFF000000;
    public static final int GRAY = ChatFormatting.GRAY.getColor() | 0xFF000000;
    public static final int DARK_GRAY = ChatFormatting.DARK_GRAY.getColor() | 0xFF000000;
    public static final int BLUE = ChatFormatting.BLUE.getColor() | 0xFF000000;
    public static final int GREEN = ChatFormatting.GREEN.getColor() | 0xFF000000;
    public static final int AQUA = ChatFormatting.AQUA.getColor() | 0xFF000000;
    public static final int RED = ChatFormatting.RED.getColor() | 0xFF000000;
    public static final int LIGHT_PURPLE = ChatFormatting.LIGHT_PURPLE.getColor() | 0xFF000000;
    public static final int YELLOW = ChatFormatting.YELLOW.getColor() | 0xFF000000;
    public static final int WHITE = ChatFormatting.WHITE.getColor() | 0xFF000000;
    public static final int DEFAULT_TEXT = WHITE;

    public static final int SCREEN_BACKGROUND = 0xFF343444;
    public static final int CONTAINER_BACKGROUND = 0x77000000;

    public static int color(int r, int g, int b) {
        return FastColor.ARGB32.color(0xff, r, g, b);
    }

    public static int color(String hex) {
        int r = Integer.parseInt(hex.substring(0, 2), 16);
        int g = Integer.parseInt(hex.substring(2, 4), 16);
        int b = Integer.parseInt(hex.substring(4, 6), 16);
        return color(r, g, b);
    }

    public static int[] toIntARGB(int color) {
        return new int[]{
                FastColor.ARGB32.alpha(color),
                FastColor.ARGB32.red(color),
                FastColor.ARGB32.green(color),
                FastColor.ARGB32.blue(color)
        };
    }

    public static float[] toFloatArray(int color) {
        return new float[]{
                ((float) FastColor.ARGB32.alpha(color) / 0xFF),
                ((float) FastColor.ARGB32.red(color) / 0xFF),
                ((float) FastColor.ARGB32.green(color) / 0xFF),
                ((float) FastColor.ARGB32.blue(color) / 0xFF)
        };
    }

    public static String toRGBHex(int color) {
        return "#"
                + Integer.toHexString(FastColor.ARGB32.red(color))
                + Integer.toHexString(FastColor.ARGB32.green(color))
                + Integer.toHexString(FastColor.ARGB32.blue(color));
    }

    public static boolean validHexColor(String hexColor) {
        if (hexColor == null || hexColor.isBlank()) return false;
        try {
            parseHex(hexColor);
        } catch (NumberFormatException ex) {
            return false;
        }

        return true;
    }

    public static int parseHex(String hexColor) {
        if (hexColor.startsWith("#")) hexColor = hexColor.substring(1);
        if (hexColor.startsWith("0x")) hexColor = hexColor.substring(2);
        int len = hexColor.length();
        if (len != 6 && len != 8 && len != 3 && len != 4) {
            return -1;
        }

        int color, shift;
        if (len == 3) {
            hexColor = ""
                    + hexColor.charAt(0) + hexColor.charAt(0)
                    + hexColor.charAt(1) + hexColor.charAt(1)
                    + hexColor.charAt(2) + hexColor.charAt(2);
            len = 6;
        } else if (len == 4) {
            hexColor = ""
                    + hexColor.charAt(0) + hexColor.charAt(0)
                    + hexColor.charAt(1) + hexColor.charAt(1)
                    + hexColor.charAt(2) + hexColor.charAt(2)
                    + hexColor.charAt(3) + hexColor.charAt(3);
            len = 8;
        }

        if (len == 6) {
            color = 0xFF000000;
            shift = 16;
        } else {
            color = 0;
            shift = 24;
        }

        try {
            String[] splited = hexColor.split("(?<=\\G.{2})");
            for (String digit : splited) {
                color |= Integer.valueOf(digit, 16) << shift;
                shift -= 8;
            }
        } catch (NumberFormatException ex) {
            return -1;
        }

        return color;
    }

    public static int blendColors(float t, int c1, int c2) {
        int r = (int) (t * FastColor.ARGB32.red(c2) + (1 - t) * FastColor.ARGB32.red(c1));
        int g = (int) (t * FastColor.ARGB32.green(c2) + (1 - t) * FastColor.ARGB32.green(c1));
        int b = (int) (t * FastColor.ARGB32.blue(c2) + (1 - t) * FastColor.ARGB32.blue(c1));
        int a = (int) (t * FastColor.ARGB32.alpha(c2) + (1 - t) * FastColor.ARGB32.alpha(c1));
        return FastColor.ARGB32.color(a, r, g, b);
    }
}