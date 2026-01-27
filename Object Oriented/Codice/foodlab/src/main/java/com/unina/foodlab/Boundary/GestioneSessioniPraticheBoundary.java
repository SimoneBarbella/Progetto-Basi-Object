package com.unina.foodlab.Boundary;

import com.unina.foodlab.Controller.GestoreRicette;
import com.unina.foodlab.Controller.GestoreSessioni;
import com.unina.foodlab.Entity.Chef;
import com.unina.foodlab.Entity.Corso;
import com.unina.foodlab.Entity.Ingrediente;
import com.unina.foodlab.Entity.IngredienteQuantita;
import com.unina.foodlab.Entity.Ricetta;
import com.unina.foodlab.Entity.Sessione;
import com.unina.foodlab.Entity.SessionePresenza;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.geometry.Side;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.stage.Stage;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class GestioneSessioniPraticheBoundary {

    @FXML
    private Label corsoLabel;

    @FXML
    private TableView<SessionePresenza> sessioniTable;

    @FXML
    private TableColumn<SessionePresenza, String> dataCol;

    @FXML
    private TableColumn<SessionePresenza, Integer> aderentiCol;

    @FXML
    private TableColumn<SessionePresenza, String> ricetteCol;

    @FXML
    private TableColumn<SessionePresenza, String> quantitaTotaleCol;

    @FXML
    private Label sessioneSelezionataLabel;

    @FXML
    private TextField nuovaRicettaNomeField;

    @FXML
    private TextArea nuovaRicettaDescrField;

    @FXML
    private TextField nuovaRicettaTempoField;

    @FXML
    private TextField nuovoIngredienteNomeField;

    @FXML
    private TextField nuovoIngredienteUnitaField;

    @FXML
    private TextField ingredienteQuantitaField;

    @FXML
    private javafx.scene.control.Button creaRicettaButton;

    @FXML
    private javafx.scene.control.Button aggiungiIngredienteButton;

    @FXML
    private ListView<IngredienteQuantita> ingredientiListView;

    @FXML
    private ListView<Ricetta> ricetteListView;

    @FXML
    private javafx.scene.control.Button dettaglioRicettaButton;

    @FXML
    private HBox ricetteActionsBox;

    private Corso corso;
    private Chef chef;
    private SessionePresenza sessioneSelezionata;

    private final javafx.collections.ObservableList<Ricetta> ricetteDisponibiliMaster =
            javafx.collections.FXCollections.observableArrayList();
    private final javafx.collections.ObservableList<Ingrediente> ingredientiDisponibiliMaster =
            javafx.collections.FXCollections.observableArrayList();

    private final ContextMenu nuoveRicetteSuggestMenu = new ContextMenu();
    private final ContextMenu ingredientiSuggestMenu = new ContextMenu();

    private Ricetta ricettaSelezionataNuovaSuggest = null;
    private Ingrediente ingredienteSelezionatoSuggest = null;

    private final List<IngredienteQuantita> ingredientiNuovaRicetta = new ArrayList<>();
    private final Map<Integer, String> ricettePerSessione = new HashMap<>();
    private final Map<Integer, String> quantitaTotalePerSessione = new HashMap<>();

    @FXML
    private void initialize() {
        if (dataCol != null) {
            dataCol.setCellValueFactory(c -> {
                LocalDateTime d = c.getValue().getOraInizio();
                String val = d != null ? d.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")) : "";
                return new javafx.beans.property.SimpleStringProperty(val);
            });
        }
        if (aderentiCol != null) {
            aderentiCol.setCellValueFactory(c -> new javafx.beans.property.SimpleObjectProperty<>(c.getValue().getNumAderenti()));
        }
        if (ricetteCol != null) {
            ricetteCol.setCellValueFactory(c -> {
                Integer id = c.getValue().getIdSessione();
                String val = (id != null) ? ricettePerSessione.getOrDefault(id, "") : "";
                return new javafx.beans.property.SimpleStringProperty(val);
            });
        }
        if (quantitaTotaleCol != null) {
            quantitaTotaleCol.setCellValueFactory(c -> {
                Integer id = c.getValue().getIdSessione();
                String val = (id != null) ? quantitaTotalePerSessione.getOrDefault(id, "") : "";
                return new javafx.beans.property.SimpleStringProperty(val);
            });
            quantitaTotaleCol.setCellFactory(col -> new javafx.scene.control.TableCell<>() {
                private final Button btn = new Button();

                {
                    btn.getStyleClass().add("secondary-button");
                    btn.setOnAction(e -> {
                        SessionePresenza sessione = getTableView().getItems().get(getIndex());
                        if (sessione != null) {
                            openListaSpesa(sessione);
                        }
                    });
                }

                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null || getIndex() < 0) {
                        setGraphic(null);
                        return;
                    }
                    if (getTableView() == null || getTableView().getItems() == null || getIndex() >= getTableView().getItems().size()) {
                        setGraphic(null);
                        return;
                    }

                    SessionePresenza sessione = getTableView().getItems().get(getIndex());
                    btn.setText("Quantità totale");
                    btn.setDisable(sessione == null || sessione.getNumAderenti() <= 0);
                    setGraphic(btn);
                }
            });
        }

        if (sessioniTable != null) {
            sessioniTable.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
                sessioneSelezionata = newVal;
                refreshRicetteSection();
            });
            sessioniTable.setOnMouseClicked(e -> refreshRicetteSection());
        }

        setupNuovaRicettaSuggest();
        setupIngredienteSuggest();

        if (ricetteListView != null) {
            ricetteListView.setCellFactory(list -> new javafx.scene.control.ListCell<>() {
                @Override
                protected void updateItem(Ricetta item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                        setGraphic(null);
                    } else {
                        String labelText = displayRicetta(item);

                        Label lbl = new Label(labelText);

                        Region spacer = new Region();
                        HBox.setHgrow(spacer, Priority.ALWAYS);

                        Button removeBtn = new Button("x");
                        removeBtn.getStyleClass().add("secondary-button");
                        removeBtn.setFocusTraversable(false);
                        removeBtn.setOnAction(e -> {
                            if (sessioneSelezionata == null || sessioneSelezionata.getIdSessione() == null
                                    || item.getIdRicetta() == null) {
                                return;
                            }
                            try {
                                GestoreRicette.getInstance().disassociaRicettaSessione(
                                        sessioneSelezionata.getIdSessione(), item.getIdRicetta());
                                loadSessioniPratiche();
                            } catch (RuntimeException ex) {
                                showRicettaError(ex.getMessage());
                            }
                        });

                        HBox row = new HBox(10, lbl, spacer, removeBtn);
                        setText(null);
                        setGraphic(row);
                    }
                }
            });
            ricetteListView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
                updateDettagliRicetteButtonState();
            });

            ricetteListView.setOnMouseClicked(e -> {
                if (e.getClickCount() == 2 && !ricetteListView.getSelectionModel().isEmpty()) {
                    onDettaglioRicettaClick(new ActionEvent(ricetteListView, ricetteListView));
                }
            });
        }


        if (ingredienteQuantitaField != null) {
            ingredienteQuantitaField.focusedProperty().addListener((obs, wasFocused, isFocused) -> {
                if (isFocused && ingredientiSuggestMenu.isShowing()) {
                    ingredientiSuggestMenu.hide();
                }
            });
        }

        if (ingredientiListView != null) {
            ingredientiListView.setCellFactory(list -> new javafx.scene.control.ListCell<>() {
                @Override
                protected void updateItem(IngredienteQuantita item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                        setGraphic(null);
                    } else {
                        String unita = item.getUnita() != null ? item.getUnita() : "";
                        Label lbl = new Label(item.getNome() + " - " + item.getQuantita() + (unita.isEmpty() ? "" : (" " + unita)));

                        Region spacer = new Region();
                        HBox.setHgrow(spacer, Priority.ALWAYS);

                        Button removeBtn = new Button("x");
                        removeBtn.getStyleClass().add("secondary-button");
                        removeBtn.setFocusTraversable(false);
                        removeBtn.setOnAction(e -> {
                            ingredientiNuovaRicetta.remove(item);
                            if (ingredientiListView != null) {
                                ingredientiListView.setItems(javafx.collections.FXCollections.observableArrayList(ingredientiNuovaRicetta));
                                boolean hasIngredienti = !ingredientiNuovaRicetta.isEmpty();
                                ingredientiListView.setVisible(hasIngredienti);
                                ingredientiListView.setManaged(hasIngredienti);
                            }
                        });

                        HBox row = new HBox(10, lbl, spacer, removeBtn);
                        setText(null);
                        setGraphic(row);
                    }
                }
            });
        }
    }

    public void initData(Corso corso, Chef chef) {
        this.corso = corso;
        this.chef = chef;
        if (corsoLabel != null && corso != null) {
            String nome = corso.getNome();
            String id = corso.getIdCorso();
            corsoLabel.setText(nome + (id != null ? " (ID " + id + ")" : ""));
        }
        loadSessioniPratiche();
    }

    private void loadSessioniPratiche() {
        if (corso == null) return;

        Integer previousSelectedSessionId = sessioneSelezionata != null ? sessioneSelezionata.getIdSessione() : null;

        List<Sessione> sessioni = GestoreSessioni.getInstance().getSessioniByCorso(corso);
        List<SessionePresenza> pratiche = sessioni.stream()
                .filter(s -> s instanceof SessionePresenza)
                .map(s -> (SessionePresenza) s)
                .collect(Collectors.toList());
        if (sessioniTable != null) {
            sessioniTable.setItems(javafx.collections.FXCollections.observableArrayList(pratiche));
            if (!pratiche.isEmpty()) {
                boolean restored = false;
                if (previousSelectedSessionId != null) {
                    for (SessionePresenza s : pratiche) {
                        if (s != null && s.getIdSessione() != null && s.getIdSessione().equals(previousSelectedSessionId)) {
                            sessioniTable.getSelectionModel().select(s);
                            restored = true;
                            break;
                        }
                    }
                }
                if (!restored) {
                    sessioniTable.getSelectionModel().selectFirst();
                }
            }
        }
        aggiornaRicettePerSessione(pratiche);
        aggiornaQuantitaTotalePerSessione(pratiche);

        ricetteDisponibiliMaster.setAll(GestoreRicette.getInstance().getRicetteDisponibili());
        ingredientiDisponibiliMaster.setAll(GestoreRicette.getInstance().getIngredientiDisponibili());
        refreshRicetteSection();
    }

    private void refreshRicetteSection() {
        int idSessione = sessioneSelezionata != null && sessioneSelezionata.getIdSessione() != null
                ? sessioneSelezionata.getIdSessione() : -1;

        if (sessioneSelezionataLabel != null) {
            if (sessioneSelezionata == null) {
                sessioneSelezionataLabel.setText("Ricette della sessione");
            } else {
                int numeroCorso = 0;
                if (sessioniTable != null && sessioniTable.getItems() != null) {
                    int idx = sessioniTable.getItems().indexOf(sessioneSelezionata);
                    numeroCorso = idx >= 0 ? idx + 1 : 0;
                }
                String numText = numeroCorso > 0 ? String.valueOf(numeroCorso) : "";
                sessioneSelezionataLabel.setText("Ricette della sessione n. " + numText);
            }
        }

        boolean enable = idSessione > 0;
        if (creaRicettaButton != null) creaRicettaButton.setDisable(!enable);
        if (aggiungiIngredienteButton != null) aggiungiIngredienteButton.setDisable(!enable);
        if (nuovaRicettaNomeField != null) nuovaRicettaNomeField.setDisable(!enable);
        if (nuovaRicettaDescrField != null) nuovaRicettaDescrField.setDisable(!enable);
        if (nuovaRicettaTempoField != null) nuovaRicettaTempoField.setDisable(!enable);
        if (nuovoIngredienteNomeField != null) nuovoIngredienteNomeField.setDisable(!enable);
        if (nuovoIngredienteUnitaField != null) nuovoIngredienteUnitaField.setDisable(!enable);
        if (ingredienteQuantitaField != null) ingredienteQuantitaField.setDisable(!enable);

        applyRicettaMode(enable);

        if (ricetteListView != null) {
            if (enable) {
                var ricette = GestoreRicette.getInstance().getRicettePerSessione(idSessione);
                ricetteListView.setItems(javafx.collections.FXCollections.observableArrayList(ricette));

                // Se c'è almeno una ricetta, seleziona la prima per rendere subito attivo "Dettagli ricetta"
                if (!ricetteListView.getItems().isEmpty() && ricetteListView.getSelectionModel().getSelectedItem() == null) {
                    ricetteListView.getSelectionModel().selectFirst();
                }
            } else {
                ricetteListView.setItems(javafx.collections.FXCollections.observableArrayList(new ArrayList<>()));
            }
        }

        updateDettagliRicetteButtonState();

        if (ricetteListView != null) {
            boolean hasRicette = ricetteListView.getItems() != null && !ricetteListView.getItems().isEmpty();
            ricetteListView.setVisible(hasRicette);
            ricetteListView.setManaged(hasRicette);
            if (ricetteActionsBox != null) {
                ricetteActionsBox.setVisible(hasRicette);
                ricetteActionsBox.setManaged(hasRicette);
            }
        }

        if (!enable) {
            ingredientiNuovaRicetta.clear();
            if (ingredientiListView != null) {
                ingredientiListView.setItems(javafx.collections.FXCollections.observableArrayList(new ArrayList<>()));
            }
        } else if (ingredientiListView != null) {
            ingredientiListView.setItems(javafx.collections.FXCollections.observableArrayList(ingredientiNuovaRicetta));
        }

        if (ingredientiListView != null) {
            boolean hasIngredienti = ingredientiListView.getItems() != null && !ingredientiListView.getItems().isEmpty();
            ingredientiListView.setVisible(hasIngredienti);
            ingredientiListView.setManaged(hasIngredienti);
        }
    }

    private void updateDettagliRicetteButtonState() {
        if (dettaglioRicettaButton == null) return;

        boolean hasSessione = sessioneSelezionata != null && sessioneSelezionata.getIdSessione() != null;
        boolean hasRicette = ricetteListView != null && ricetteListView.getItems() != null && !ricetteListView.getItems().isEmpty();

        dettaglioRicettaButton.setDisable(!(hasSessione && hasRicette));
    }

    private void aggiornaRicettePerSessione(List<SessionePresenza> sessioni) {
        ricettePerSessione.clear();
        if (sessioni == null) return;
        for (SessionePresenza s : sessioni) {
            Integer id = s.getIdSessione();
            if (id == null) continue;
            List<Ricetta> ricette = GestoreRicette.getInstance().getRicettePerSessione(id);
            String nomi = ricette.stream()
                    .map(Ricetta::getNome)
                    .filter(n -> n != null && !n.isBlank())
                    .collect(Collectors.joining(", "));
            ricettePerSessione.put(id, nomi);
        }
    }

    private void aggiornaQuantitaTotalePerSessione(List<SessionePresenza> sessioni) {
        quantitaTotalePerSessione.clear();
        if (sessioni == null) return;
        for (SessionePresenza s : sessioni) {
            Integer id = s.getIdSessione();
            if (id == null) continue;
            double tot = GestoreSessioni.getInstance().getQuantitaTotaleBySessioneId(id);
            quantitaTotalePerSessione.put(id, String.valueOf(tot));
        }
    }

    @FXML
    private void onCreateRicettaClick(ActionEvent event) {
        if (sessioneSelezionata == null || sessioneSelezionata.getIdSessione() == null) {
            showRicettaWarning("Seleziona una sessione valida.");
            return;
        }
        String nome = nuovaRicettaNomeField != null ? nuovaRicettaNomeField.getText() : null;

        // Se l'utente ha selezionato (o digitato) una ricetta già esistente, gestiscila come "Aggiungi ricetta"
        Ricetta ricettaEsistente = ricettaSelezionataNuovaSuggest;
        if (ricettaEsistente == null && nome != null && !nome.isBlank()) {
            ricettaEsistente = resolveRicetta(nome);
        }
        if (ricettaEsistente != null && ricettaEsistente.getIdRicetta() != null) {
            try {
                GestoreRicette.getInstance().associaRicettaSessione(sessioneSelezionata.getIdSessione(), ricettaEsistente.getIdRicetta());
                if (nuovaRicettaNomeField != null) nuovaRicettaNomeField.clear();
                if (nuovaRicettaDescrField != null) nuovaRicettaDescrField.clear();
                if (nuovaRicettaTempoField != null) nuovaRicettaTempoField.clear();
                if (nuovaRicettaDescrField != null) nuovaRicettaDescrField.setDisable(false);
                if (nuovaRicettaTempoField != null) nuovaRicettaTempoField.setDisable(false);
                ricettaSelezionataNuovaSuggest = null;
                if (nuoveRicetteSuggestMenu.isShowing()) nuoveRicetteSuggestMenu.hide();
                loadSessioniPratiche();
            } catch (RuntimeException ex) {
                showRicettaError(ex.getMessage());
            }
            return;
        }

        String descr = nuovaRicettaDescrField != null ? nuovaRicettaDescrField.getText() : null;
        String tempoStr = nuovaRicettaTempoField != null ? nuovaRicettaTempoField.getText() : null;

        if (nome == null || nome.isBlank()) {
            showRicettaWarning("Inserisci il nome della ricetta.");
            return;
        }
        if (descr == null || descr.isBlank()) {
            showRicettaWarning("Inserisci la descrizione della ricetta.");
            return;
        }
        if (tempoStr == null || tempoStr.isBlank()) {
            showRicettaWarning("Inserisci il tempo in formato HH:mm:ss.");
            return;
        }

        LocalTime tempo;
        try {
            tempo = LocalTime.parse(tempoStr.trim(), DateTimeFormatter.ofPattern("HH:mm:ss"));
        } catch (DateTimeParseException ex) {
            showRicettaWarning("Formato tempo non valido. Usa HH:mm:ss (es. 00:45:00).");
            return;
        }

        if (ingredientiNuovaRicetta.isEmpty()) {
            showRicettaWarning("Aggiungi almeno un ingrediente (Richiede).");
            return;
        }

        try {
            Ricetta nuova = GestoreRicette.getInstance().creaRicettaConIngredienti(
                    nome.trim(), descr.trim(), tempo, ingredientiNuovaRicetta
            );
            if (nuova != null && nuova.getIdRicetta() != null) {
                GestoreRicette.getInstance().associaRicettaSessione(sessioneSelezionata.getIdSessione(), nuova.getIdRicetta());
            }
            if (nuovaRicettaNomeField != null) nuovaRicettaNomeField.clear();
            if (nuovaRicettaDescrField != null) nuovaRicettaDescrField.clear();
            if (nuovaRicettaTempoField != null) nuovaRicettaTempoField.clear();
            ingredientiNuovaRicetta.clear();
            loadSessioniPratiche();
        } catch (RuntimeException ex) {
            showRicettaError(ex.getMessage());
        }
    }

    @FXML
    private void onAddIngredienteClick(ActionEvent event) {
        if (sessioneSelezionata == null || sessioneSelezionata.getIdSessione() == null) {
            showRicettaWarning("Seleziona una sessione valida.");
            return;
        }
        String nome = nuovoIngredienteNomeField != null ? nuovoIngredienteNomeField.getText() : null;
        Ingrediente existing = resolveIngrediente(nome);
        boolean usedExisting = existing != null;
        String unita = usedExisting ? existing.getUnitàDiMisura() : (nuovoIngredienteUnitaField != null ? nuovoIngredienteUnitaField.getText() : null);
        boolean isNewIngredient = !usedExisting && nome != null && !nome.isBlank();
        String quantitaStr = ingredienteQuantitaField != null ? ingredienteQuantitaField.getText() : null;

        if (nome == null || nome.isBlank()) {
            showRicettaWarning("Seleziona o inserisci un ingrediente.");
            return;
        }

        // Validazione esplicita: un nuovo ingrediente richiede sempre l'unità di misura
        if (isNewIngredient && (unita == null || unita.isBlank())) {
            showRicettaWarning("Per un nuovo ingrediente devi inserire l'unità di misura.");
            return;
        }
        if (unita == null || unita.isBlank()) {
            String fallbackUnita = nuovoIngredienteUnitaField != null ? nuovoIngredienteUnitaField.getText() : null;
            if (fallbackUnita != null && !fallbackUnita.isBlank()) {
                unita = fallbackUnita;
            } else {
                showRicettaWarning("Inserisci l'unità di misura dell'ingrediente.");
                return;
            }
        }
        if (quantitaStr == null || quantitaStr.isBlank()) {
            showRicettaWarning("Inserisci la quantità necessaria.");
            return;
        }

        BigDecimal quantita;
        try {
            quantita = new BigDecimal(quantitaStr.trim());
        } catch (NumberFormatException ex) {
            showRicettaWarning("Quantità non valida.");
            return;
        }
        if (quantita.compareTo(BigDecimal.ZERO) <= 0) {
            showRicettaWarning("La quantità deve essere positiva.");
            return;
        }

        ingredientiNuovaRicetta.add(new IngredienteQuantita(nome.trim(), unita.trim(), quantita));

        // se è un nuovo ingrediente (non preso dal combo), aggiungilo subito ai suggerimenti
        if (!usedExisting) {
            Ingrediente nuovo = new Ingrediente();
            nuovo.setNome(nome.trim());
            nuovo.setUnitàDiMisura(unita.trim());
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
        if (ingredientiSuggestMenu.isShowing()) ingredientiSuggestMenu.hide();
        if (ingredientiListView != null) {
            ingredientiListView.setItems(javafx.collections.FXCollections.observableArrayList(ingredientiNuovaRicetta));
        }

        if (ingredientiListView != null) {
            boolean hasIngredienti = !ingredientiNuovaRicetta.isEmpty();
            ingredientiListView.setVisible(hasIngredienti);
            ingredientiListView.setManaged(hasIngredienti);
        }
    }

    private void setupNuovaRicettaSuggest() {
        if (nuovaRicettaNomeField == null) return;

        nuovaRicettaNomeField.textProperty().addListener((obs, oldVal, newVal) -> {
            ricettaSelezionataNuovaSuggest = null;
            updateNuovaRicettaSuggestions(newVal);
            applyRicettaMode(sessioneSelezionata != null && sessioneSelezionata.getIdSessione() != null);
        });

        nuovaRicettaNomeField.focusedProperty().addListener((obs, wasFocused, isFocused) -> {
            if (!isFocused && nuoveRicetteSuggestMenu.isShowing()) {
                nuoveRicetteSuggestMenu.hide();
            }
        });

        nuovaRicettaNomeField.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ESCAPE && nuoveRicetteSuggestMenu.isShowing()) {
                nuoveRicetteSuggestMenu.hide();
            }
        });
    }

    private void updateNuovaRicettaSuggestions(String text) {
        if (nuovaRicettaNomeField == null) return;
        String q = text != null ? text.trim() : "";
        if (q.isEmpty()) {
            nuoveRicetteSuggestMenu.hide();
            return;
        }

        java.util.Set<Integer> alreadyInSession = new java.util.HashSet<>();
        if (ricetteListView != null && ricetteListView.getItems() != null) {
            for (Ricetta r : ricetteListView.getItems()) {
                if (r != null && r.getIdRicetta() != null) {
                    alreadyInSession.add(r.getIdRicetta());
                }
            }
        }

        String qLower = q.toLowerCase();
        List<Ricetta> matches = ricetteDisponibiliMaster.stream()
                .filter(r -> r != null && r.getNome() != null && r.getNome().toLowerCase().contains(qLower))
                .filter(r -> r.getIdRicetta() == null || !alreadyInSession.contains(r.getIdRicetta()))
                .limit(8)
                .collect(Collectors.toList());

        if (matches.isEmpty()) {
            nuoveRicetteSuggestMenu.hide();
            return;
        }

        List<MenuItem> items = new ArrayList<>();
        for (Ricetta r : matches) {
            MenuItem mi = new MenuItem(displayRicetta(r));
            mi.setOnAction(ev -> {
                ricettaSelezionataNuovaSuggest = r;
                if (nuovaRicettaNomeField != null) {
                    nuovaRicettaNomeField.setText(r.getNome() != null ? r.getNome() : "");
                    nuovaRicettaNomeField.positionCaret(nuovaRicettaNomeField.getText().length());
                }
                if (nuovaRicettaDescrField != null) {
                    nuovaRicettaDescrField.setText(r.getDescrizione() != null ? r.getDescrizione() : "");
                    nuovaRicettaDescrField.setDisable(true);
                }
                if (nuovaRicettaTempoField != null) {
                    nuovaRicettaTempoField.setText(r.getTempo() != null ? r.getTempo().toString() : "");
                    nuovaRicettaTempoField.setDisable(true);
                }

                // Se scelgo una ricetta già esistente, non devo inserire ingredienti per crearla
                ingredientiNuovaRicetta.clear();
                if (ingredientiListView != null) {
                    ingredientiListView.setItems(javafx.collections.FXCollections.observableArrayList(ingredientiNuovaRicetta));
                    ingredientiListView.setVisible(false);
                    ingredientiListView.setManaged(false);
                }
                if (nuovoIngredienteNomeField != null) nuovoIngredienteNomeField.clear();
                if (nuovoIngredienteUnitaField != null) nuovoIngredienteUnitaField.clear();
                if (ingredienteQuantitaField != null) ingredienteQuantitaField.clear();

                applyRicettaMode(sessioneSelezionata != null && sessioneSelezionata.getIdSessione() != null);
                nuoveRicetteSuggestMenu.hide();
            });
            items.add(mi);
        }

        nuoveRicetteSuggestMenu.getItems().setAll(items);
        if (!nuoveRicetteSuggestMenu.isShowing() && nuovaRicettaNomeField.isFocused()) {
            nuoveRicetteSuggestMenu.show(nuovaRicettaNomeField, Side.BOTTOM, 0, 0);
        }
    }

    private void setupIngredienteSuggest() {
        if (nuovoIngredienteNomeField == null) return;

        nuovoIngredienteNomeField.textProperty().addListener((obs, oldVal, newVal) -> {
            ingredienteSelezionatoSuggest = null;
            if (nuovoIngredienteUnitaField != null) {
                nuovoIngredienteUnitaField.setDisable(false);
            }
            updateIngredienteSuggestions(newVal);
        });

        nuovoIngredienteNomeField.focusedProperty().addListener((obs, wasFocused, isFocused) -> {
            if (!isFocused && ingredientiSuggestMenu.isShowing()) {
                ingredientiSuggestMenu.hide();
            }
        });

        nuovoIngredienteNomeField.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ESCAPE && ingredientiSuggestMenu.isShowing()) {
                ingredientiSuggestMenu.hide();
            }
        });
    }

    private void updateIngredienteSuggestions(String text) {
        if (nuovoIngredienteNomeField == null) return;
        String q = text != null ? text.trim() : "";
        if (q.isEmpty()) {
            ingredientiSuggestMenu.hide();
            return;
        }

        java.util.Set<String> alreadyAdded = new java.util.HashSet<>();
        for (IngredienteQuantita iq : ingredientiNuovaRicetta) {
            if (iq != null && iq.getNome() != null) {
                alreadyAdded.add(iq.getNome().trim().toLowerCase());
            }
        }

        String qLower = q.toLowerCase();
        List<Ingrediente> matches = ingredientiDisponibiliMaster.stream()
                .filter(i -> i != null && i.getNome() != null && i.getNome().toLowerCase().contains(qLower))
            .filter(i -> !alreadyAdded.contains(i.getNome().trim().toLowerCase()))
                .limit(8)
                .collect(Collectors.toList());

        if (matches.isEmpty()) {
            ingredientiSuggestMenu.hide();
            return;
        }

        List<MenuItem> items = new ArrayList<>();
        for (Ingrediente ing : matches) {
            MenuItem mi = new MenuItem(displayIngrediente(ing));
            mi.setOnAction(ev -> {
                ingredienteSelezionatoSuggest = ing;
                if (nuovoIngredienteNomeField != null) {
                    nuovoIngredienteNomeField.setText(ing.getNome());
                    nuovoIngredienteNomeField.positionCaret(nuovoIngredienteNomeField.getText().length());
                }
                if (nuovoIngredienteUnitaField != null) {
                    nuovoIngredienteUnitaField.setText(ing.getUnitàDiMisura() != null ? ing.getUnitàDiMisura() : "");
                    nuovoIngredienteUnitaField.setDisable(true);
                }
                ingredientiSuggestMenu.hide();
            });
            items.add(mi);
        }
        ingredientiSuggestMenu.getItems().setAll(items);
        if (!ingredientiSuggestMenu.isShowing() && nuovoIngredienteNomeField.isFocused()) {
            ingredientiSuggestMenu.show(nuovoIngredienteNomeField, Side.BOTTOM, 0, 0);
        }
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

        // Come per l'ingrediente: se esiste lo selezioni e i campi "di definizione" diventano non modificabili
        if (nuovaRicettaDescrField != null) nuovaRicettaDescrField.setDisable(existing);
        if (nuovaRicettaTempoField != null) nuovaRicettaTempoField.setDisable(existing);

        // Se è ricetta esistente, disabilita l'inserimento ingredienti (servono solo per creare ricetta nuova)
        if (nuovoIngredienteNomeField != null) nuovoIngredienteNomeField.setDisable(existing);
        if (nuovoIngredienteUnitaField != null) nuovoIngredienteUnitaField.setDisable(existing);
        if (ingredienteQuantitaField != null) ingredienteQuantitaField.setDisable(existing);
        if (aggiungiIngredienteButton != null) aggiungiIngredienteButton.setDisable(existing);
    }

    private Ricetta resolveRicetta(String text) {
        if (text == null) return null;
        String s = text.trim();
        if (s.isEmpty()) return null;

        // match per nome (ignora eventuale "(tempo)")
        int parenIdx = s.indexOf('(');
        String name = parenIdx > 0 ? s.substring(0, parenIdx).trim() : s;
        for (Ricetta r : ricetteDisponibiliMaster) {
            if (r == null || r.getNome() == null) continue;
            if (r.getNome().equalsIgnoreCase(name)) return r;
        }
        return null;
    }

    private Ingrediente resolveIngrediente(String text) {
        if (text == null) return null;
        String s = text.trim();
        if (s.isEmpty()) return null;

        // match per nome (ignora eventuale "(unità)")
        int parenIdx = s.indexOf('(');
        String name = parenIdx > 0 ? s.substring(0, parenIdx).trim() : s;
        for (Ingrediente i : ingredientiDisponibiliMaster) {
            if (i == null || i.getNome() == null) continue;
            if (i.getNome().equalsIgnoreCase(name)) return i;
        }
        return null;
    }

    private void openListaSpesa(SessionePresenza sessione) {
        if (sessione == null || sessione.getIdSessione() == null) {
            showRicettaWarning("Seleziona una sessione valida.");
            return;
        }
        try {
            java.net.URL location = getClass().getResource("/com/unina/foodlab/Boundary/fxml/listaSpesa.fxml");
            if (location == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Lista della spesa");
                alert.setHeaderText("Schermata non trovata");
                alert.setContentText("Risorsa listaSpesa.fxml non trovata nel classpath.");
                alert.showAndWait();
                return;
            }

            FXMLLoader loader = new FXMLLoader(location);
            Parent root = loader.load();

            ListaSpesaBoundary controller = loader.getController();
            controller.initData(sessione, corso, chef);

            Stage stage = (Stage) sessioniTable.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.centerOnScreen();
            stage.show();
        } catch (IOException e) {
            System.err.println("[GestioneSessioniPraticheBoundary] Errore apertura lista spesa: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void onBackClick(ActionEvent event) {
        try {
            java.net.URL location = getClass().getResource("/com/unina/foodlab/Boundary/fxml/gestioneSessioni.fxml");
            if (location == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Sessioni");
                alert.setHeaderText("Schermata non trovata");
                alert.setContentText("Risorsa gestioneSessioni.fxml non trovata nel classpath.");
                alert.showAndWait();
                return;
            }

            FXMLLoader loader = new FXMLLoader(location);
            Parent root = loader.load();

            GestioneSessioniBoundary controller = loader.getController();
            controller.initData(corso, chef);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.centerOnScreen();
            stage.show();
        } catch (IOException e) {
            System.err.println("[GestioneSessioniPraticheBoundary] Errore ritorno: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void onDettaglioRicettaClick(ActionEvent event) {
        if (sessioneSelezionata == null || sessioneSelezionata.getIdSessione() == null) {
            showRicettaWarning("Seleziona una sessione valida.");
            return;
        }
        Ricetta selected = ricetteListView != null ? ricetteListView.getSelectionModel().getSelectedItem() : null;

        try {
            java.net.URL location = getClass().getResource("/com/unina/foodlab/Boundary/fxml/gestioneRicettaDettagli.fxml");
            if (location == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Ricetta");
                alert.setHeaderText("Schermata non trovata");
                alert.setContentText("Risorsa gestioneRicettaDettagli.fxml non trovata nel classpath.");
                alert.showAndWait();
                return;
            }

            FXMLLoader loader = new FXMLLoader(location);
            Parent root = loader.load();

            GestioneRicettaDettagliBoundary controller = loader.getController();
            controller.initDataSessione(sessioneSelezionata, selected, corso, chef);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.centerOnScreen();
            stage.show();
        } catch (IOException e) {
            System.err.println("[GestioneSessioniPraticheBoundary] Errore apertura dettagli ricetta: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void showRicettaWarning(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Ricette");
        alert.setHeaderText("Attenzione");
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showRicettaError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Ricette");
        alert.setHeaderText("Errore");
        alert.setContentText(message);
        alert.showAndWait();
    }
}
