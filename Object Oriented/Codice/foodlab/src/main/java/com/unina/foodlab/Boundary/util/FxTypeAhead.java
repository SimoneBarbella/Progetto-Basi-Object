package com.unina.foodlab.Boundary.util;

import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public final class FxTypeAhead<T> {

    private final TextField field;
    private final ContextMenu menu;
    private final Function<String, List<T>> search;
    private final Function<T, String> labeler;
    private final Consumer<T> onSelected;
    private final Runnable onQueryChanged;

    public FxTypeAhead(
        TextField field,
        ContextMenu menu,
        Function<String, List<T>> search,
        Function<T, String> labeler,
        Consumer<T> onSelected,
        Runnable onQueryChanged
    ) {
        this.field = field;
        this.menu = menu;
        this.search = search;
        this.labeler = labeler;
        this.onSelected = onSelected;
        this.onQueryChanged = onQueryChanged;
    }

    public void bind() {
        if (field == null) return;

        field.textProperty().addListener((obs, oldVal, newVal) -> {
            if (onQueryChanged != null) {
                onQueryChanged.run();
            }
            refresh(newVal);
        });

        field.focusedProperty().addListener((obs, wasFocused, isFocused) -> {
            if (!isFocused) {
                FxSuggestions.hideIfShowing(menu);
            }
        });

        field.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ESCAPE) {
                FxSuggestions.hideIfShowing(menu);
            }
        });
    }

    public void refresh(String text) {
        if (field == null) return;
        String q = text != null ? text.trim() : "";
        if (q.isEmpty()) {
            FxSuggestions.hideIfShowing(menu);
            return;
        }

        List<T> matches = search.apply(q);
        if (matches == null || matches.isEmpty()) {
            FxSuggestions.hideIfShowing(menu);
            return;
        }

        List<MenuItem> items = FxSuggestions.buildSuggestionMenuItems(matches, labeler, onSelected);
        FxSuggestions.applySuggestionMenu(menu, field, items);
    }
}
