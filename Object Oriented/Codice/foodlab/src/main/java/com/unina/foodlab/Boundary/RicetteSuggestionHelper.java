package com.unina.foodlab.Boundary;

import com.unina.foodlab.Boundary.util.FxSuggestions;
import com.unina.foodlab.Boundary.util.FxTypeAhead;
import com.unina.foodlab.Entity.Ingrediente;
import com.unina.foodlab.Entity.Ricetta;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.TextField;

import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

final class RicetteSuggestionHelper {

    private final TextField nuovaRicettaNomeField;
    private final ContextMenu nuoveRicetteSuggestMenu;
    private final Supplier<List<Ricetta>> ricetteDisponibili;
    private final Supplier<Set<Integer>> ricetteGiaInSessione;
    private final Function<Ricetta, String> ricettaLabel;
    private final Consumer<Ricetta> onRicettaSelected;
    private final Runnable onRicettaCleared;

    private final TextField nuovoIngredienteNomeField;
    private final ContextMenu ingredientiSuggestMenu;
    private final Supplier<List<Ingrediente>> ingredientiDisponibili;
    private final Supplier<Set<String>> ingredientiGiaAggiunti;
    private final Function<Ingrediente, String> ingredienteLabel;
    private final Consumer<Ingrediente> onIngredienteSelected;
    private final Runnable onIngredienteCleared;

    private final int suggestionLimit;

    private FxTypeAhead<Ricetta> ricetteTypeAhead;
    private FxTypeAhead<Ingrediente> ingredientiTypeAhead;

    RicetteSuggestionHelper(
        TextField nuovaRicettaNomeField,
        ContextMenu nuoveRicetteSuggestMenu,
        Supplier<List<Ricetta>> ricetteDisponibili,
        Supplier<Set<Integer>> ricetteGiaInSessione,
        Function<Ricetta, String> ricettaLabel,
        Consumer<Ricetta> onRicettaSelected,
        Runnable onRicettaCleared,
        TextField nuovoIngredienteNomeField,
        ContextMenu ingredientiSuggestMenu,
        Supplier<List<Ingrediente>> ingredientiDisponibili,
        Supplier<Set<String>> ingredientiGiaAggiunti,
        Function<Ingrediente, String> ingredienteLabel,
        Consumer<Ingrediente> onIngredienteSelected,
        Runnable onIngredienteCleared,
        int suggestionLimit
    ) {
        this.nuovaRicettaNomeField = nuovaRicettaNomeField;
        this.nuoveRicetteSuggestMenu = nuoveRicetteSuggestMenu;
        this.ricetteDisponibili = ricetteDisponibili;
        this.ricetteGiaInSessione = ricetteGiaInSessione;
        this.ricettaLabel = ricettaLabel;
        this.onRicettaSelected = onRicettaSelected;
        this.onRicettaCleared = onRicettaCleared;
        this.nuovoIngredienteNomeField = nuovoIngredienteNomeField;
        this.ingredientiSuggestMenu = ingredientiSuggestMenu;
        this.ingredientiDisponibili = ingredientiDisponibili;
        this.ingredientiGiaAggiunti = ingredientiGiaAggiunti;
        this.ingredienteLabel = ingredienteLabel;
        this.onIngredienteSelected = onIngredienteSelected;
        this.onIngredienteCleared = onIngredienteCleared;
        this.suggestionLimit = suggestionLimit;
    }

    void bind() {
        bindRicette();
        bindIngredienti();
    }

    private void bindRicette() {
        if (nuovaRicettaNomeField == null) return;

        ricetteTypeAhead = new FxTypeAhead<>(
            nuovaRicettaNomeField,
            nuoveRicetteSuggestMenu,
            q -> {
                Set<Integer> alreadyInSession = ricetteGiaInSessione.get();
                String qLower = q.toLowerCase();
                return ricetteDisponibili.get().stream()
                    .filter(r -> r != null && r.getNome() != null && r.getNome().toLowerCase().contains(qLower))
                    .filter(r -> r.getIdRicetta() == null || !alreadyInSession.contains(r.getIdRicetta()))
                    .limit(suggestionLimit)
                    .collect(Collectors.toList());
            },
            ricettaLabel,
            onRicettaSelected,
            onRicettaCleared
        );

        ricetteTypeAhead.bind();
    }

    private void bindIngredienti() {
        if (nuovoIngredienteNomeField == null) return;

        ingredientiTypeAhead = new FxTypeAhead<>(
            nuovoIngredienteNomeField,
            ingredientiSuggestMenu,
            q -> {
                Set<String> alreadyAdded = ingredientiGiaAggiunti.get();
                String qLower = q.toLowerCase();
                return ingredientiDisponibili.get().stream()
                    .filter(i -> i != null && i.getNome() != null && i.getNome().toLowerCase().contains(qLower))
                    .filter(i -> !alreadyAdded.contains(normalizeKey(i.getNome())))
                    .limit(suggestionLimit)
                    .collect(Collectors.toList());
            },
            ingredienteLabel,
            onIngredienteSelected,
            onIngredienteCleared
        );

        ingredientiTypeAhead.bind();
    }

    private static String normalizeKey(String s) {
        return FxSuggestions.normalizeKey(s);
    }
}
