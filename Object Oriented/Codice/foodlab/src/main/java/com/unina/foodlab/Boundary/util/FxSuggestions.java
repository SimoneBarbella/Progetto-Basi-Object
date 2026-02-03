package com.unina.foodlab.Boundary.util;

import javafx.geometry.Side;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public final class FxSuggestions {

    private FxSuggestions() {
        // utility
    }

    public static void hideIfShowing(ContextMenu menu) {
        if (menu != null && menu.isShowing()) {
            menu.hide();
        }
    }

    public static String normalizeKey(String s) {
        if (s == null) return "";
        return s.trim().toLowerCase();
    }

    public static String parseNomeSenzaDettagli(String text) {
        if (text == null) return "";
        String s = text.trim();
        if (s.isEmpty()) return "";

        // Rimuove eventuali dettagli visualizzati, es. "Nome (tempo)" oppure "Nome (unità)"
        int parenIdx = s.indexOf('(');
        return (parenIdx > 0 ? s.substring(0, parenIdx) : s).trim();
    }

    public static <T> List<MenuItem> buildSuggestionMenuItems(
        List<T> matches,
        Function<T, String> labeler,
        Consumer<T> onSelected
    ) {
        if (matches == null || matches.isEmpty()) return new ArrayList<>();

        List<MenuItem> items = new ArrayList<>();
        for (T m : matches) {
            MenuItem mi = new MenuItem(labeler.apply(m));
            mi.setOnAction(ev -> onSelected.accept(m));
            items.add(mi);
        }
        return items;
    }

    public static void applySuggestionMenu(ContextMenu menu, TextField anchor, List<MenuItem> items) {
        if (menu == null) return;
        if (items == null || items.isEmpty() || anchor == null) {
            hideIfShowing(menu);
            return;
        }

        menu.getItems().setAll(items);
        if (!menu.isShowing() && anchor.isFocused()) {
            menu.show(anchor, Side.BOTTOM, 0, 0);
        }
    }
}
