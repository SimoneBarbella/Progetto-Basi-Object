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
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
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
    private ComboBox<Ricetta> ricetteCombo;

    @FXML
    private TextField nuovaRicettaNomeField;

    @FXML
    private TextField nuovaRicettaDescrField;

    @FXML
    private TextField nuovaRicettaTempoField;

    @FXML
    private ComboBox<Ingrediente> ingredienteCombo;

    @FXML
    private TextField nuovoIngredienteNomeField;

    @FXML
    private TextField nuovoIngredienteUnitaField;

    @FXML
    private TextField ingredienteQuantitaField;

    @FXML
    private javafx.scene.control.Button aggiungiRicettaButton;

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

        if (ricetteCombo != null) {
            ricetteCombo.setConverter(new javafx.util.StringConverter<>() {
                @Override
                public String toString(Ricetta ricetta) {
                    if (ricetta == null) return "";
                    String tempo = ricetta.getTempo() != null ? ricetta.getTempo().toString() : "";
                    return ricetta.getNome() + (tempo.isEmpty() ? "" : " (" + tempo + ")");
                }

                @Override
                public Ricetta fromString(String string) {
                    return null;
                }
            });
        }

        if (ricetteListView != null) {
            ricetteListView.setCellFactory(list -> new javafx.scene.control.ListCell<>() {
                @Override
                protected void updateItem(Ricetta item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                    } else {
                        String tempo = item.getTempo() != null ? item.getTempo().toString() : "";
                        setText(item.getNome() + (tempo.isEmpty() ? "" : " (" + tempo + ")"));
                    }
                }
            });
            ricetteListView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
                if (dettaglioRicettaButton != null) {
                    dettaglioRicettaButton.setDisable(newVal == null);
                }
            });
        }

        if (ingredienteCombo != null) {
            ingredienteCombo.setConverter(new javafx.util.StringConverter<>() {
                @Override
                public String toString(Ingrediente ingrediente) {
                    if (ingrediente == null) return "";
                    String unita = ingrediente.getUnitàDiMisura() != null ? ingrediente.getUnitàDiMisura() : "";
                    return ingrediente.getNome() + (unita.isEmpty() ? "" : " (" + unita + ")");
                }

                @Override
                public Ingrediente fromString(String string) {
                    return null;
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
                    } else {
                        String unita = item.getUnita() != null ? item.getUnita() : "";
                        setText(item.getNome() + " - " + item.getQuantita() + (unita.isEmpty() ? "" : (" " + unita)));
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
        List<Sessione> sessioni = GestoreSessioni.getInstance().getSessioniByCorso(corso);
        List<SessionePresenza> pratiche = sessioni.stream()
                .filter(s -> s instanceof SessionePresenza)
                .map(s -> (SessionePresenza) s)
                .collect(Collectors.toList());
        if (sessioniTable != null) {
            sessioniTable.setItems(javafx.collections.FXCollections.observableArrayList(pratiche));
            if (!pratiche.isEmpty()) {
                sessioniTable.getSelectionModel().selectFirst();
            }
        }
        aggiornaRicettePerSessione(pratiche);
        aggiornaQuantitaTotalePerSessione(pratiche);
        if (ricetteCombo != null) {
            ricetteCombo.getItems().setAll(GestoreRicette.getInstance().getRicetteDisponibili());
        }
        if (ingredienteCombo != null) {
            ingredienteCombo.getItems().setAll(GestoreRicette.getInstance().getIngredientiDisponibili());
        }
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
        if (aggiungiRicettaButton != null) aggiungiRicettaButton.setDisable(!enable);
        if (creaRicettaButton != null) creaRicettaButton.setDisable(!enable);
        if (aggiungiIngredienteButton != null) aggiungiIngredienteButton.setDisable(!enable);
        if (nuovaRicettaNomeField != null) nuovaRicettaNomeField.setDisable(!enable);
        if (nuovaRicettaDescrField != null) nuovaRicettaDescrField.setDisable(!enable);
        if (nuovaRicettaTempoField != null) nuovaRicettaTempoField.setDisable(!enable);
        if (nuovoIngredienteNomeField != null) nuovoIngredienteNomeField.setDisable(!enable);
        if (nuovoIngredienteUnitaField != null) nuovoIngredienteUnitaField.setDisable(!enable);
        if (ingredienteQuantitaField != null) ingredienteQuantitaField.setDisable(!enable);
        if (ricetteCombo != null) ricetteCombo.setDisable(!enable);
        if (ingredienteCombo != null) ingredienteCombo.setDisable(!enable);

        if (ricetteListView != null) {
            if (enable) {
                var ricette = GestoreRicette.getInstance().getRicettePerSessione(idSessione);
                ricetteListView.setItems(javafx.collections.FXCollections.observableArrayList(ricette));
            } else {
                ricetteListView.setItems(javafx.collections.FXCollections.observableArrayList(new ArrayList<>()));
            }
        }

        if (dettaglioRicettaButton != null) {
            Ricetta selected = ricetteListView != null ? ricetteListView.getSelectionModel().getSelectedItem() : null;
            dettaglioRicettaButton.setDisable(selected == null);
        }

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
    private void onAddRicettaClick(ActionEvent event) {
        if (sessioneSelezionata == null || sessioneSelezionata.getIdSessione() == null) {
            showRicettaWarning("Seleziona una sessione valida.");
            return;
        }
        Ricetta selected = ricetteCombo != null ? ricetteCombo.getValue() : null;
        if (selected == null || selected.getIdRicetta() == null) {
            showRicettaWarning("Seleziona una ricetta esistente.");
            return;
        }

        try {
            GestoreRicette.getInstance().associaRicettaSessione(sessioneSelezionata.getIdSessione(), selected.getIdRicetta());
            loadSessioniPratiche();
        } catch (RuntimeException ex) {
            showRicettaError(ex.getMessage());
        }
    }

    @FXML
    private void onCreateRicettaClick(ActionEvent event) {
        if (sessioneSelezionata == null || sessioneSelezionata.getIdSessione() == null) {
            showRicettaWarning("Seleziona una sessione valida.");
            return;
        }
        String nome = nuovaRicettaNomeField != null ? nuovaRicettaNomeField.getText() : null;
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
        String nome = null;
        String unita = null;
        if (ingredienteCombo != null && ingredienteCombo.getValue() != null) {
            nome = ingredienteCombo.getValue().getNome();
            unita = ingredienteCombo.getValue().getUnitàDiMisura();
        }
        if ((nome == null || nome.isBlank()) && nuovoIngredienteNomeField != null) {
            nome = nuovoIngredienteNomeField.getText();
            unita = nuovoIngredienteUnitaField != null ? nuovoIngredienteUnitaField.getText() : null;
        }
        String quantitaStr = ingredienteQuantitaField != null ? ingredienteQuantitaField.getText() : null;

        if (nome == null || nome.isBlank()) {
            showRicettaWarning("Seleziona o inserisci un ingrediente.");
            return;
        }
        if (unita == null || unita.isBlank()) {
            showRicettaWarning("Inserisci l'unità di misura dell'ingrediente.");
            return;
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
        boolean usedExisting = ingredienteCombo != null && ingredienteCombo.getValue() != null;
        if (ingredienteQuantitaField != null) ingredienteQuantitaField.clear();
        if (nuovoIngredienteNomeField != null) nuovoIngredienteNomeField.clear();
        if (nuovoIngredienteUnitaField != null) nuovoIngredienteUnitaField.clear();
        if (!usedExisting && ingredienteCombo != null) ingredienteCombo.setValue(null);
        if (ingredientiListView != null) {
            ingredientiListView.setItems(javafx.collections.FXCollections.observableArrayList(ingredientiNuovaRicetta));
        }

        if (ingredientiListView != null) {
            boolean hasIngredienti = !ingredientiNuovaRicetta.isEmpty();
            ingredientiListView.setVisible(hasIngredienti);
            ingredientiListView.setManaged(hasIngredienti);
        }
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
        Ricetta selected = ricetteListView != null ? ricetteListView.getSelectionModel().getSelectedItem() : null;
        if (selected == null) {
            showRicettaWarning("Seleziona una ricetta dalla lista.");
            return;
        }

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
            controller.initData(selected, corso, chef);

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
