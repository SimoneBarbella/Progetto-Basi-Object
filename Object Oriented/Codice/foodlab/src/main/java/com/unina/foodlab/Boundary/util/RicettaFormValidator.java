package com.unina.foodlab.Boundary.util;

import com.unina.foodlab.Entity.IngredienteQuantita;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.List;

public final class RicettaFormValidator {

    private RicettaFormValidator() {
        // utility
    }

    public record RicettaCreationInput(String nome, String descr, LocalTime tempo) {}

    public record IngredienteAddInput(String nome, String unita, BigDecimal quantita) {}

    public static RicettaCreationInput validateNuovaRicetta(
        String nome,
        String descr,
        String tempoStr,
        List<IngredienteQuantita> ingredientiNuovaRicetta
    ) {
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("Inserisci il nome della ricetta.");
        }
        if (descr == null || descr.isBlank()) {
            throw new IllegalArgumentException("Inserisci la descrizione della ricetta.");
        }
        if (tempoStr == null || tempoStr.isBlank()) {
            throw new IllegalArgumentException("Inserisci il tempo in formato HH:mm:ss.");
        }

        final LocalTime tempo;
        try {
            tempo = LocalTime.parse(tempoStr.trim(), FormatiDataOra.HH_MM_SS);
        } catch (DateTimeParseException ex) {
            throw new IllegalArgumentException("Formato tempo non valido. Usa HH:mm:ss (es. 00:45:00).");
        }

        if (ingredientiNuovaRicetta == null || ingredientiNuovaRicetta.isEmpty()) {
            throw new IllegalArgumentException("Aggiungi almeno un ingrediente.");
        }

        return new RicettaCreationInput(nome.trim(), descr.trim(), tempo);
    }

    public static IngredienteAddInput validateIngredienteToAdd(
        String nome,
        String unita,
        boolean isNewIngredient,
        String fallbackUnita,
        String quantitaStr,
        List<IngredienteQuantita> ingredientiNuovaRicetta
    ) {
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("Seleziona o inserisci un ingrediente.");
        }

        String nomeNormalized = FxSuggestions.normalizeKey(FxSuggestions.parseNomeSenzaDettagli(nome));
        if (!nomeNormalized.isEmpty() && ingredientiNuovaRicetta != null) {
            boolean alreadyAdded = ingredientiNuovaRicetta.stream()
                .filter(iq -> iq != null && iq.getNome() != null)
                .map(iq -> FxSuggestions.normalizeKey(FxSuggestions.parseNomeSenzaDettagli(iq.getNome())))
                .anyMatch(nomeNormalized::equals);
            if (alreadyAdded) {
                throw new IllegalArgumentException("Ingrediente già aggiunto alla ricetta.");
            }
        }

        if (isNewIngredient && (unita == null || unita.isBlank())) {
            throw new IllegalArgumentException("Per un nuovo ingrediente devi inserire l'unità di misura.");
        }
        if (unita == null || unita.isBlank()) {
            if (fallbackUnita != null && !fallbackUnita.isBlank()) {
                unita = fallbackUnita;
            } else {
                throw new IllegalArgumentException("Inserisci l'unità di misura dell'ingrediente.");
            }
        }
        if (quantitaStr == null || quantitaStr.isBlank()) {
            throw new IllegalArgumentException("Inserisci la quantità necessaria.");
        }

        final BigDecimal quantita;
        try {
            quantita = new BigDecimal(quantitaStr.trim());
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("Quantità non valida.");
        }
        if (quantita.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("La quantità deve essere positiva.");
        }

        return new IngredienteAddInput(nome.trim(), unita.trim(), quantita);
    }
}
