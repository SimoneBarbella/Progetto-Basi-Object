package com.unina.foodlab.Boundary.util;

public final class UtilParsing {

    private UtilParsing() {
    }

    public static Integer tryParseInt(String s) {
        if (s == null) return null;
        String t = s.trim();
        if (t.isEmpty()) return null;
        try {
            return Integer.parseInt(t);
        } catch (NumberFormatException ex) {
            return null;
        }
    }
}
