package com.unina.foodlab.Boundary;

import com.unina.foodlab.Boundary.util.FxSuggestions;
import com.unina.foodlab.Boundary.util.InterfacciaFx;
import com.unina.foodlab.Boundary.util.RicettaFormValidator;
import com.unina.foodlab.Boundary.util.cells.IngredienteQuantitaListCell;
import com.unina.foodlab.Boundary.util.cells.RicettaListCell;
import com.unina.foodlab.Controller.GestoreRicette;
import com.unina.foodlab.Entity.Ingrediente;
import com.unina.foodlab.Entity.IngredienteQuantita;
import com.unina.foodlab.Entity.Ricetta;
import com.unina.foodlab.Entity.SessionePresenza;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Supplier;

final class RicetteIngredientiSectionUI {

    private static final int SUGGESTION_LIMIT = 8;

    private final Label sessioneSelezionataLabel;

    private final TextField nuovaRicettaNomeField;
    private final TextArea nuovaRicettaDescrField;
    private final TextField nuovaRicettaTempoField;

    private final TextField nuovoIngredienteNomeField;
    private final TextField nuovoIngredienteUnitaField;
    private final TextField ingredienteQuantitaField;

    private final Button creaRicettaButton;
    private final Button aggiungiIngredienteButton;

    private final Label ingredientiTitoloLabel;
    private final ListView<IngredienteQuantita> ingredientiListView;
    private final ListView<Ricetta> ricetteListView;

    private final Button dettaglioRicettaButton;
    private final HBox ricetteActionsBox;

    private final Supplier<SessionePresenza> sessioneSelezionata;

    private final Runnable reloadAll;
    private final Consumer<ActionEvent> openDettaglioRicetta;

    private final ObservableList<Ricetta> ricetteDisponibiliMaster = FXCollections.observableArrayList();
    private final ObservableList<Ingrediente> ingredientiDisponibiliMaster = FXCollections.observableArrayList();

    private final ContextMenu nuoveRicetteSuggestMenu = new ContextMenu();
    private final ContextMenu ingredientiSuggestMenu = new ContextMenu();

    private Ricetta ricettaSelezionataNuovaSuggest = null;
    private Ingrediente ingredienteSelezionatoSuggest = null;

    private final List<IngredienteQuantita> ingredientiNuovaRicetta = new ArrayList<>();

    private final RicetteSuggestionHelper typeAhead;

    RicetteIngredientiSectionUI(
        Label sessioneSelezionataLabel,
        TextField nuovaRicettaNomeField,
        TextArea nuovaRicettaDescrField,
        TextField nuovaRicettaTempoField,
        TextField nuovoIngredienteNomeField,
        TextField nuovoIngredienteUnitaField,
        TextField ingredienteQuantitaField,
        Button creaRicettaButton,
        Button aggiungiIngredienteButton,
        Label ingredientiTitoloLabel,
        ListView<IngredienteQuantita> ingredientiListView,
        ListView<Ricetta> ricetteListView,
        Button dettaglioRicettaButton,
        HBox ricetteActionsBox,
        Supplier<SessionePresenza> sessioneSelezionata,
        Runnable reloadAll,
        Consumer<ActionEvent> openDettaglioRicetta
    ) {
        this.sessioneSelezionataLabel = sessioneSelezionataLabel;
        this.nuovaRicettaNomeField = nuovaRicettaNomeField;
        this.nuovaRicettaDescrField = nuovaRicettaDescrField;
        this.nuovaRicettaTempoField = nuovaRicettaTempoField;
        this.nuovoIngredienteNomeField = nuovoIngredienteNomeField;
        this.nuovoIngredienteUnitaField = nuovoIngredienteUnitaField;
        this.ingredienteQuantitaField = ingredienteQuantitaField;
        this.creaRicettaButton = creaRicettaButton;
        this.aggiungiIngredienteButton = aggiungiIngredienteButton;
        this.ingredientiTitoloLabel = ingredientiTitoloLabel;
        this.ingredientiListView = ingredientiListView;
        this.ricetteListView = ricetteListView;
        this.dettaglioRicettaButton = dettaglioRicettaButton;
        this.ricetteActionsBox = ricetteActionsBox;
        this.sessioneSelezionata = sessioneSelezionata;
        this.reloadAll = reloadAll;
        this.openDettaglioRicetta = openDettaglioRicetta;

        this.typeAhead = new RicetteSuggestionHelper(
            nuovaRicettaNomeField,
            nuoveRicetteSuggestMenu,
            () -> ricetteDisponibiliMaster,
            this::getRicetteIdGiaInSessione,
            this::displayRicetta,
            this::onRicettaSuggestionSelected,
            () -> {
                ricettaSelezionataNuovaSuggest = null;
                applyRicettaMode(hasValidSession());
            },
            nuovoIngredienteNomeField,
            ingredientiSuggestMenu,
            () -> ingredientiDisponibiliMaster,
            this::getNomiIngredientiGiaAggiunti,
            this::displayIngrediente,
            this::onIngredienteSuggestionSelected,
            () -> {
                ingredienteSelezionatoSuggest = null;
                if (nuovoIngredienteUnitaField != null) {
                    nuovoIngredienteUnitaField.setDisable(false);
                }
            },
            SUGGESTION_LIMIT
        );
    }

    void initialize() {
        setupRicetteListView();
        setupIngredientiListView();
        setupIngredienteQuantitaFieldListeners();
        typeAhead.bind();
        aggiornaMasters();
        aggiornaPerSelezione(null, null);
    }

    void aggiornaMasters() {
        ricetteDisponibiliMaster.setAll(GestoreRicette.getInstanza().getRicetteDisponibili());
        ingredientiDisponibiliMaster.setAll(GestoreRicette.getInstanza().getIngredientiDisponibili());
    }

    void aggiornaPerSelezione(SessionePresenza selected, Integer numeroSessioneNelCorso) {
        Integer idSessione = selected != null ? selected.getIdSessione() : null;

        if (sessioneSelezionataLabel != null) {
            if (selected == null) {
                sessioneSelezionataLabel.setText("Ricette della sessione");
            } else {
                String numText = (numeroSessioneNelCorso != null && numeroSessioneNelCorso > 0)
                    ? String.valueOf(numeroSessioneNelCorso)
                    : "";
                sessioneSelezionataLabel.setText("Ricette della sessione n. " + numText);
            }
        }

        boolean enable = idSessione != null && idSessione > 0;
        setDisable(!enable);
        applyRicettaMode(enable);

        if (ricetteListView != null) {
            if (enable) {
                var ricette = GestoreRicette.getInstanza().getRicettePerSessione(idSessione);
                ricetteListView.setItems(FXCollections.observableArrayList(ricette));
                if (!ricetteListView.getItems().isEmpty() && ricetteListView.getSelectionModel().getSelectedItem() == null) {
                    ricetteListView.getSelectionModel().selectFirst();
                }
            } else {
                ricetteListView.setItems(FXCollections.observableArrayList(new ArrayList<>()));
            }
        }

        aggiornaDettagliStatoRicetteButton();
        aggiornaVisibilitaRicetta();

        if (!enable) {
            clearIngredientiUi();
        } else {
            setIngredientiItemsFromModel();
        }
    }

    void onCreaRicettaClick(ActionEvent event, Integer idSessione) {
        if (idSessione == null) {
            showRicettaWarning("Seleziona una sessione valida.");
            return;
        }

        String nome = nuovaRicettaNomeField != null ? nuovaRicettaNomeField.getText() : null;
        Ricetta ricettaEsistente = resolveRicettaEsistenteToAdd(nome);
        if (tryAssociaRicettaEsistente(idSessione, ricettaEsistente)) return;

        String descr = nuovaRicettaDescrField != null ? nuovaRicettaDescrField.getText() : null;
        String tempoStr = nuovaRicettaTempoField != null ? nuovaRicettaTempoField.getText() : null;

        final RicettaFormValidator.RicettaCreationInput ricettaInput;
        try {
            ricettaInput = RicettaFormValidator.validateNuovaRicetta(nome, descr, tempoStr, ingredientiNuovaRicetta);
        } catch (IllegalArgumentException ex) {
            showRicettaWarning(ex.getMessage());
            return;
        }

        creaEAssociaNuovaRicetta(idSessione, ricettaInput);
    }

    void onAggiungiIngredienteClick(ActionEvent event, Integer idSessione) {
        if (idSessione == null) {
            showRicettaWarning("Seleziona una sessione valida.");
            return;
        }
        handleAddIngrediente();
    }

    private void setupRicetteListView() {
        if (ricetteListView == null) return;

        ricetteListView.setCellFactory(list -> new RicettaListCell(
            () -> {
                SessionePresenza s = sessioneSelezionata.get();
                return s != null ? s.getIdSessione() : null;
            },
            reloadAll,
            this::showRicettaError,
            this::displayRicetta
        ));

        ricetteListView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> aggiornaDettagliStatoRicetteButton());

        ricetteListView.setOnMouseClicked(e -> {
            if (e.getClickCount() == 2 && !ricetteListView.getSelectionModel().isEmpty()) {
                openDettaglioRicetta.accept(new ActionEvent(ricetteListView, ricetteListView));
            }
        });
    }

    private void setupIngredientiListView() {
        if (ingredientiListView == null) return;
        ingredientiListView.setCellFactory(list -> new IngredienteQuantitaListCell(item -> {
            ingredientiNuovaRicetta.remove(item);
            setIngredientiItemsFromModel();
        }));
    }

    private void setupIngredienteQuantitaFieldListeners() {
        if (ingredienteQuantitaField == null) return;
        ingredienteQuantitaField.focusedProperty().addListener((obs, wasFocused, isFocused) -> {
            if (isFocused) FxSuggestions.hideIfShowing(ingredientiSuggestMenu);
        });
    }

    private void setDisable(boolean disable) {
        if (creaRicettaButton != null) creaRicettaButton.setDisable(disable);
        if (aggiungiIngredienteButton != null) aggiungiIngredienteButton.setDisable(disable);
        if (nuovaRicettaNomeField != null) nuovaRicettaNomeField.setDisable(disable);
        if (nuovaRicettaDescrField != null) nuovaRicettaDescrField.setDisable(disable);
        if (nuovaRicettaTempoField != null) nuovaRicettaTempoField.setDisable(disable);
        if (nuovoIngredienteNomeField != null) nuovoIngredienteNomeField.setDisable(disable);
        if (nuovoIngredienteUnitaField != null) nuovoIngredienteUnitaField.setDisable(disable);
        if (ingredienteQuantitaField != null) ingredienteQuantitaField.setDisable(disable);
    }

    private void aggiornaDettagliStatoRicetteButton() {
        if (dettaglioRicettaButton == null) return;

        boolean hasSessione = sessioneSelezionata.get() != null && sessioneSelezionata.get().getIdSessione() != null;
        boolean hasRicette = ricetteListView != null && ricetteListView.getItems() != null && !ricetteListView.getItems().isEmpty();

        dettaglioRicettaButton.setDisable(!(hasSessione && hasRicette));
    }

    private void setRicetteVisible(boolean visible) {
        if (ricetteListView != null) {
            InterfacciaFx.setVisibleManaged(ricetteListView, visible);
        }
        if (ricetteActionsBox != null) {
            InterfacciaFx.setVisibleManaged(ricetteActionsBox, visible);
        }
    }

    private void aggiornaVisibilitaRicetta() {
        boolean hasRicette = ricetteListView != null
            && ricetteListView.getItems() != null
            && !ricetteListView.getItems().isEmpty();
        setRicetteVisible(hasRicette);
    }

    private void setIngredientiVisible(boolean visible) {
        if (ingredientiListView != null) {
            InterfacciaFx.setVisibleManaged(ingredientiListView, visible);
        }
        if (ingredientiTitoloLabel != null) {
            InterfacciaFx.setVisibleManaged(ingredientiTitoloLabel, visible);
        }
    }

    private void setIngredientiItemsFromModel() {
        if (ingredientiListView != null) {
            ingredientiListView.setItems(FXCollections.observableArrayList(ingredientiNuovaRicetta));
        }
        setIngredientiVisible(!ingredientiNuovaRicetta.isEmpty());
    }

    private void clearIngredientiUi() {
        ingredientiNuovaRicetta.clear();
        if (ingredientiListView != null) {
            ingredientiListView.setItems(FXCollections.observableArrayList(new ArrayList<>()));
        }
        setIngredientiVisible(false);
    }

    private boolean hasValidSession() {
        SessionePresenza s = sessioneSelezionata.get();
        return s != null && s.getIdSessione() != null;
    }

    private Ricetta resolveRicettaEsistenteToAdd(String nomeRicetta) {
        Ricetta ricettaEsistente = ricettaSelezionataNuovaSuggest;
        if (ricettaEsistente == null && nomeRicetta != null && !nomeRicetta.isBlank()) {
            ricettaEsistente = resolveRicetta(nomeRicetta);
        }
        return ricettaEsistente;
    }

    private boolean tryAssociaRicettaEsistente(int idSessione, Ricetta ricettaEsistente) {
        if (ricettaEsistente == null || ricettaEsistente.getIdRicetta() == null) return false;
        try {
            GestoreRicette.getInstanza().associaRicettaSessione(idSessione, ricettaEsistente.getIdRicetta());
            resetNuovaRicettaAfterAggiungiRicettaEsistente();
            reloadAll.run();
            return true;
        } catch (RuntimeException ex) {
            showRicettaError(ex.getMessage());
            return true;
        }
    }

    private void creaEAssociaNuovaRicetta(int idSessione, RicettaFormValidator.RicettaCreationInput ricettaInput) {
        try {
            Ricetta nuova = GestoreRicette.getInstanza().creaRicettaConIngredienti(
                ricettaInput.nome(),
                ricettaInput.descr(),
                ricettaInput.tempo(),
                ingredientiNuovaRicetta
            );
            if (nuova != null && nuova.getIdRicetta() != null) {
                GestoreRicette.getInstanza().associaRicettaSessione(idSessione, nuova.getIdRicetta());
            }
            clearNuovaRicettaTextFields();
            ingredientiNuovaRicetta.clear();
            reloadAll.run();
        } catch (RuntimeException ex) {
            showRicettaError(ex.getMessage());
        }
    }

    private void handleAddIngrediente() {
        String nome = nuovoIngredienteNomeField != null ? nuovoIngredienteNomeField.getText() : null;
        Ingrediente existing = ingredienteSelezionatoSuggest != null ? ingredienteSelezionatoSuggest : resolveIngrediente(nome);
        boolean usedExisting = existing != null;
        String unita = usedExisting
            ? existing.getUnitàDiMisura()
            : (nuovoIngredienteUnitaField != null ? nuovoIngredienteUnitaField.getText() : null);
        boolean isNewIngredient = !usedExisting && nome != null && !nome.isBlank();
        String fallbackUnita = nuovoIngredienteUnitaField != null ? nuovoIngredienteUnitaField.getText() : null;
        String quantitaStr = ingredienteQuantitaField != null ? ingredienteQuantitaField.getText() : null;

        final RicettaFormValidator.IngredienteAddInput ingredienteInput;
        try {
            ingredienteInput = RicettaFormValidator.validateIngredienteToAdd(
                nome,
                unita,
                isNewIngredient,
                fallbackUnita,
                quantitaStr,
                ingredientiNuovaRicetta
            );
        } catch (IllegalArgumentException ex) {
            showRicettaWarning(ex.getMessage());
            return;
        }

        ingredientiNuovaRicetta.add(new IngredienteQuantita(
            ingredienteInput.nome(),
            ingredienteInput.unita(),
            ingredienteInput.quantita()
        ));

        if (!usedExisting) {
            Ingrediente nuovo = new Ingrediente();
            nuovo.setNome(ingredienteInput.nome());
            nuovo.setUnitàDiMisura(ingredienteInput.unita());
            boolean already = ingredientiDisponibiliMaster.stream().anyMatch(i -> i != null && i.getNome() != null
                && i.getNome().equalsIgnoreCase(nuovo.getNome()));
            if (!already) {
                ingredientiDisponibiliMaster.add(nuovo);
            }
        }

        if (ingredienteQuantitaField != null) ingredienteQuantitaField.clear();
        if (nuovoIngredienteNomeField != null) nuovoIngredienteNomeField.clear();
        if (nuovoIngredienteUnitaField != null) nuovoIngredienteUnitaField.clear();
        ingredienteSelezionatoSuggest = null;
        if (nuovoIngredienteUnitaField != null) nuovoIngredienteUnitaField.setDisable(false);
        FxSuggestions.hideIfShowing(ingredientiSuggestMenu);
        setIngredientiItemsFromModel();
    }

    private void clearNuovaRicettaTextFields() {
        if (nuovaRicettaNomeField != null) nuovaRicettaNomeField.clear();
        if (nuovaRicettaDescrField != null) nuovaRicettaDescrField.clear();
        if (nuovaRicettaTempoField != null) nuovaRicettaTempoField.clear();
    }

    private void resetNuovaRicettaAfterAggiungiRicettaEsistente() {
        clearNuovaRicettaTextFields();
        if (nuovaRicettaDescrField != null) nuovaRicettaDescrField.setDisable(false);
        if (nuovaRicettaTempoField != null) nuovaRicettaTempoField.setDisable(false);
        ricettaSelezionataNuovaSuggest = null;
        if (nuoveRicetteSuggestMenu.isShowing()) nuoveRicetteSuggestMenu.hide();
    }

    private void onRicettaSuggestionSelected(Ricetta ricetta) {
        ricettaSelezionataNuovaSuggest = ricetta;
        if (ricetta == null) {
            FxSuggestions.hideIfShowing(nuoveRicetteSuggestMenu);
            return;
        }

        if (nuovaRicettaNomeField != null) {
            nuovaRicettaNomeField.setText(ricetta.getNome() != null ? ricetta.getNome() : "");
            nuovaRicettaNomeField.positionCaret(nuovaRicettaNomeField.getText().length());
        }
        if (nuovaRicettaDescrField != null) {
            nuovaRicettaDescrField.setText(ricetta.getDescrizione() != null ? ricetta.getDescrizione() : "");
            nuovaRicettaDescrField.setDisable(true);
        }
        if (nuovaRicettaTempoField != null) {
            nuovaRicettaTempoField.setText(ricetta.getTempo() != null ? ricetta.getTempo().toString() : "");
            nuovaRicettaTempoField.setDisable(true);
        }

        clearIngredientiUi();
        if (nuovoIngredienteNomeField != null) nuovoIngredienteNomeField.clear();
        if (nuovoIngredienteUnitaField != null) nuovoIngredienteUnitaField.clear();
        if (ingredienteQuantitaField != null) ingredienteQuantitaField.clear();

        applyRicettaMode(hasValidSession());
        FxSuggestions.hideIfShowing(nuoveRicetteSuggestMenu);
    }

    private void onIngredienteSuggestionSelected(Ingrediente ingrediente) {
        ingredienteSelezionatoSuggest = ingrediente;
        if (ingrediente == null) {
            FxSuggestions.hideIfShowing(ingredientiSuggestMenu);
            return;
        }

        if (nuovoIngredienteNomeField != null) {
            nuovoIngredienteNomeField.setText(ingrediente.getNome());
            nuovoIngredienteNomeField.positionCaret(nuovoIngredienteNomeField.getText().length());
        }
        if (nuovoIngredienteUnitaField != null) {
            nuovoIngredienteUnitaField.setText(ingrediente.getUnitàDiMisura() != null ? ingrediente.getUnitàDiMisura() : "");
            nuovoIngredienteUnitaField.setDisable(true);
        }
        FxSuggestions.hideIfShowing(ingredientiSuggestMenu);
    }

    private String displayRicetta(Ricetta ricetta) {
        if (ricetta == null) return "";
        String nome = ricetta.getNome() != null ? ricetta.getNome() : "";
        String tempo = ricetta.getTempo() != null ? ricetta.getTempo().toString() : "";
        return nome + (tempo.isEmpty() ? "" : " (" + tempo + ")");
    }

    private String displayIngrediente(Ingrediente ingrediente) {
        if (ingrediente == null) return "";
        String nome = ingrediente.getNome() != null ? ingrediente.getNome() : "";
        String unita = ingrediente.getUnitàDiMisura() != null ? ingrediente.getUnitàDiMisura() : "";
        return nome + (unita.isEmpty() ? "" : " (" + unita + ")");
    }

    private void applyRicettaMode(boolean enabledForSession) {
        if (!enabledForSession) return;

        boolean existing = false;
        if (ricettaSelezionataNuovaSuggest != null && ricettaSelezionataNuovaSuggest.getIdRicetta() != null) {
            existing = true;
        } else if (nuovaRicettaNomeField != null) {
            String text = nuovaRicettaNomeField.getText();
            if (text != null && !text.isBlank()) {
                Ricetta r = resolveRicetta(text);
                existing = (r != null && r.getIdRicetta() != null);
            }
        }

        if (nuovaRicettaDescrField != null) nuovaRicettaDescrField.setDisable(existing);
        if (nuovaRicettaTempoField != null) nuovaRicettaTempoField.setDisable(existing);

        if (nuovoIngredienteNomeField != null) nuovoIngredienteNomeField.setDisable(existing);
        if (nuovoIngredienteUnitaField != null) nuovoIngredienteUnitaField.setDisable(existing);
        if (ingredienteQuantitaField != null) ingredienteQuantitaField.setDisable(existing);
        if (aggiungiIngredienteButton != null) aggiungiIngredienteButton.setDisable(existing);
    }

    private Ricetta resolveRicetta(String text) {
        String name = FxSuggestions.parseNomeSenzaDettagli(text);
        if (name.isEmpty()) return null;
        for (Ricetta r : ricetteDisponibiliMaster) {
            if (r == null || r.getNome() == null) continue;
            if (r.getNome().equalsIgnoreCase(name)) return r;
        }
        return null;
    }

    private Ingrediente resolveIngrediente(String text) {
        String name = FxSuggestions.parseNomeSenzaDettagli(text);
        if (name.isEmpty()) return null;
        for (Ingrediente i : ingredientiDisponibiliMaster) {
            if (i == null || i.getNome() == null) continue;
            if (i.getNome().equalsIgnoreCase(name)) return i;
        }
        return null;
    }

    private Set<Integer> getRicetteIdGiaInSessione() {
        Set<Integer> alreadyInSession = new HashSet<>();
        if (ricetteListView != null && ricetteListView.getItems() != null) {
            for (Ricetta r : ricetteListView.getItems()) {
                if (r != null && r.getIdRicetta() != null) {
                    alreadyInSession.add(r.getIdRicetta());
                }
            }
        }
        return alreadyInSession;
    }

    private Set<String> getNomiIngredientiGiaAggiunti() {
        Set<String> alreadyAdded = new HashSet<>();
        for (IngredienteQuantita iq : ingredientiNuovaRicetta) {
            if (iq == null) continue;
            String key = FxSuggestions.normalizeKey(iq.getNome());
            if (!key.isEmpty()) {
                alreadyAdded.add(key);
            }
        }
        return alreadyAdded;
    }

    private void showRicettaWarning(String message) {
        InterfacciaFx.showWarning("Ricette", "Attenzione", message);
    }

    private void showRicettaError(String message) {
        InterfacciaFx.showError("Ricette", "Errore", message);
    }
}
