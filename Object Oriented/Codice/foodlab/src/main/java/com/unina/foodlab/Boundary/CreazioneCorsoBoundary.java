package com.unina.foodlab.Boundary;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import com.unina.foodlab.Controller.GestoreCorsi;
import com.unina.foodlab.Entity.Chef;
import com.unina.foodlab.Entity.Corso;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.DatePicker;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class CreazioneCorsoBoundary {

    @FXML
    private DatePicker dataInizioPicker;

    @FXML
    private TextField nomeField;

    @FXML
    private TextField frequenzaField;

    @FXML
    private TextField categorieField;

    @FXML
    private Spinner<Integer> numPartecipantiSpinner;

    @FXML
    private Spinner<Integer> numSessioniSpinner;

    private Chef chef;

    private final ContextMenu categorieSuggestionsMenu = new ContextMenu();
    private List<String> categorieEsistenti = List.of();

    @FXML
    private void initialize() {
        if (numSessioniSpinner != null) {
            numSessioniSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100, 1));
            numSessioniSpinner.setEditable(true);
        }
        if (numPartecipantiSpinner != null) {
            numPartecipantiSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 0, 0));
            numPartecipantiSpinner.setEditable(false);
            numPartecipantiSpinner.setDisable(true);
        }

        if (categorieField != null) {
            categorieField.textProperty().addListener((obs, oldVal, newVal) -> updateCategorieSuggestions());
            categorieField.focusedProperty().addListener((obs, oldVal, newVal) -> {
                if (!newVal) {
                    categorieSuggestionsMenu.hide();
                }
            });
        }
    }

    public void initData(Chef chef) {
        this.chef = chef;
        try {
            categorieEsistenti = GestoreCorsi.getInstance().getCategorieEsistenti();
        } catch (RuntimeException ex) {
            // se fallisce il caricamento, i suggerimenti restano vuoti 
            categorieEsistenti = List.of();
        }
    }

    private void updateCategorieSuggestions() {
        if (categorieField == null) {
            return;
        }
        if (categorieEsistenti == null || categorieEsistenti.isEmpty()) {
            categorieSuggestionsMenu.hide();
            return;
        }

        String text = categorieField.getText();
        if (text == null) {
            categorieSuggestionsMenu.hide();
            return;
        }

        String lastTokenRaw;
        int lastComma = text.lastIndexOf(',');
        if (lastComma >= 0) {
            lastTokenRaw = text.substring(lastComma + 1);
        } else {
            lastTokenRaw = text;
        }
        String token = lastTokenRaw.trim();

        if (token.isEmpty()) {
            categorieSuggestionsMenu.hide();
            return;
        }

        String tokenLower = token.toLowerCase(Locale.ROOT);
        List<String> matches = categorieEsistenti.stream()
            .filter(c -> c != null && !c.isBlank())
            .distinct()
            .filter(c -> c.toLowerCase(Locale.ROOT).startsWith(tokenLower))
            .limit(8)
            .collect(Collectors.toList());

        if (matches.isEmpty()) {
            categorieSuggestionsMenu.hide();
            return;
        }

        categorieSuggestionsMenu.getItems().clear();
        for (String match : matches) {
            MenuItem item = new MenuItem(match);
            item.setOnAction(e -> applyCategoriaSuggestion(match));
            categorieSuggestionsMenu.getItems().add(item);
        }

        if (!categorieSuggestionsMenu.isShowing()) {
            categorieSuggestionsMenu.show(categorieField, javafx.geometry.Side.BOTTOM, 0, 0);
        }
    }

    private void applyCategoriaSuggestion(String suggestion) {
        if (categorieField == null || suggestion == null) {
            return;
        }
        String text = categorieField.getText();
        if (text == null) {
            text = "";
        }

        int lastComma = text.lastIndexOf(',');
        String prefix;
        if (lastComma >= 0) {
            // preserva eventuali spazi già presenti dopo la virgola, senza inserirli automaticamente
            int tokenStart = lastComma + 1;
            while (tokenStart < text.length() && Character.isWhitespace(text.charAt(tokenStart))) {
                tokenStart++;
            }
            prefix = text.substring(0, tokenStart);
        } else {
            prefix = "";
        }

        String newText = prefix + suggestion;
        categorieField.setText(newText);
        categorieField.positionCaret(newText.length());
        categorieSuggestionsMenu.hide();
    }

    @FXML
    private void onSalvaClick(ActionEvent event) {
        LocalDate dataInizio = dataInizioPicker != null ? dataInizioPicker.getValue() : null;
        String nome = nomeField != null ? nomeField.getText() : null;
        String frequenza = frequenzaField != null ? frequenzaField.getText() : null;
        String categorieRaw = categorieField != null ? categorieField.getText() : null;
        Integer numPartecipanti = numPartecipantiSpinner != null ? numPartecipantiSpinner.getValue() : 0;
        Integer numSessioni = numSessioniSpinner != null ? numSessioniSpinner.getValue() : null;

        if (chef == null) {
            showError("Chef non impostato", "Impossibile creare il corso senza uno chef loggato.");
            return;
        }
        if (dataInizio == null || nome == null || nome.isBlank() || frequenza == null || frequenza.isBlank()
            || numSessioni == null) {
            showError("Dati mancanti", "Compila tutti i campi richiesti.");
            return;
        }

        List<String> categorie = parseCategorie(categorieRaw);
        if (categorie.isEmpty()) {
            showError("Categorie mancanti", "Inserisci almeno una categoria.");
            return;
        }

        if (dataInizio.isBefore(LocalDate.now())) {
            showError("Data non valida", "La data di inizio non può essere nel passato.");
            return;
        }

        if (!nome.matches("^[A-Za-zÀ-ÿ '’-]+$")) {
            showError("Nome non valido", "Il nome del corso deve contenere solo lettere, spazi, apostrofi o trattini.");
            return;
        }

        for (String categoria : categorie) {
            if (categoria.length() > 50) {
                showError("Categoria non valida", "Ogni categoria deve essere lunga massimo 50 caratteri.");
                return;
            }
            if (!categoria.matches("^[A-Za-zÀ-ÿ '’-]+$")) {
                showError("Categoria non valida", "Le categorie devono contenere solo lettere, spazi, apostrofi o trattini.");
                return;
            }
        }

        Corso corso;
        try {
                corso = GestoreCorsi.getInstance().creaCorso(null, dataInizio, nome, frequenza,
                    numPartecipanti, numSessioni, categorie, chef);
        } catch (RuntimeException ex) {
            Throwable cause = ex;
            while (cause.getCause() != null && cause.getCause() != cause) {
                cause = cause.getCause();
            }
            String msg = cause.getMessage() != null ? cause.getMessage() : "Errore sconosciuto";
            showError("Errore salvataggio", msg);
            return;
        }

        if (corso == null) {
            showError("Errore salvataggio", "Impossibile salvare il corso.");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Creazione corso");
        alert.setHeaderText("Corso creato");
        alert.setContentText("Corso creato con ID: " + corso.getIdCorso());
        alert.showAndWait();

        closeCurrentWindow();
    }

    @FXML
    private void onAnnullaClick(ActionEvent event) {
        if (dataInizioPicker != null) dataInizioPicker.setValue(null);
        if (nomeField != null) nomeField.clear();
        if (frequenzaField != null) frequenzaField.clear();
        if (categorieField != null) categorieField.clear();
        if (numPartecipantiSpinner != null && numPartecipantiSpinner.getValueFactory() != null) {
            numPartecipantiSpinner.getValueFactory().setValue(0);
        }
        if (numSessioniSpinner != null && numSessioniSpinner.getValueFactory() != null) {
            numSessioniSpinner.getValueFactory().setValue(1);
        }
        closeCurrentWindow();
    }

    private void showError(String header, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Creazione corso");
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void closeCurrentWindow() {
        if (nomeField != null && nomeField.getScene() != null) {
            Stage stage = (Stage) nomeField.getScene().getWindow();
            if (stage != null) {
                stage.close();
            }
        }
    }

    private List<String> parseCategorie(String raw) {
        List<String> result = new ArrayList<>();
        if (raw == null) {
            return result;
        }
        String[] parts = raw.split(",");
        for (String p : parts) {
            String trimmed = p.trim();
            if (!trimmed.isEmpty()) {
                result.add(trimmed);
            }
        }
        return result;
    }
}
