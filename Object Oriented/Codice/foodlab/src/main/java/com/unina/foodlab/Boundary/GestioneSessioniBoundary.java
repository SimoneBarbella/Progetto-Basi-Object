package com.unina.foodlab.Boundary;

import com.unina.foodlab.Controller.GestoreSessioni;
import com.unina.foodlab.Entity.Chef;
import com.unina.foodlab.Entity.Corso;
import com.unina.foodlab.Entity.Sessione;
import com.unina.foodlab.Entity.SessioneOnline;
import com.unina.foodlab.Entity.SessionePresenza;
import com.unina.foodlab.Enum.TipoSessione;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.text.Text;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class GestioneSessioniBoundary {

    @FXML
    private Label corsoLabel;

    @FXML
    private TableView<Sessione> sessioniTable;

    @FXML
    private TableColumn<Sessione, String> dataCol;

    @FXML
    private TableColumn<Sessione, String> tipoCol;

    @FXML
    private TableColumn<Sessione, String> teoriaCol;

    @FXML
    private TableColumn<Sessione, Integer> aderentiCol;

    @FXML
    private DatePicker dataSessionePicker;

    @FXML
    private TextField oraSessioneField;

    @FXML
    private javafx.scene.control.ComboBox<TipoSessione> tipoSessioneCombo;

    @FXML
    private TextArea teoriaField;

    private Corso corso;
    private Chef chef;

    @FXML
    private void initialize() {
        if (tipoSessioneCombo != null) {
            tipoSessioneCombo.getItems().setAll(TipoSessione.values());
            tipoSessioneCombo.valueProperty().addListener((obs, oldVal, newVal) -> {
                if (teoriaField != null) {
                    boolean enable = newVal == TipoSessione.online;
                    teoriaField.setDisable(!enable);
                    if (!enable) {
                        teoriaField.clear();
                    }
                }
            });
        }
        if (teoriaField != null) {
            teoriaField.setDisable(true);
        }
        if (dataCol != null) {
            dataCol.setCellValueFactory(c -> {
                LocalDateTime d = c.getValue().getOraInizio();
                String val = d != null ? d.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")) : "";
                return new javafx.beans.property.SimpleStringProperty(val);
            });
        }
        if (tipoCol != null) {
            tipoCol.setCellValueFactory(c -> {
                var t = c.getValue().getTipoSessione();
                String val = t != null ? t.name() : "";
                return new javafx.beans.property.SimpleStringProperty(val);
            });
        }
        if (teoriaCol != null) {
            teoriaCol.setCellValueFactory(c -> {
                Sessione s = c.getValue();
                String val = "";
                if (s instanceof SessioneOnline) {
                    val = ((SessioneOnline) s).getTeoria();
                }
                return new javafx.beans.property.SimpleStringProperty(val == null ? "" : val);
            });

            teoriaCol.setCellFactory(col -> {
                TableCell<Sessione, String> cell = new TableCell<>() {
                    private final Text text = new Text();

                    {
                        text.wrappingWidthProperty().bind(col.widthProperty().subtract(12));
                        setGraphic(text);
                    }

                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty || item == null) {
                            text.setText("");
                            setText(null);
                        } else {
                            text.setText(item);
                            setText(null);
                        }
                    }
                };
                cell.setPrefHeight(javafx.scene.control.Control.USE_COMPUTED_SIZE);
                return cell;
            });
        }
        if (aderentiCol != null) {
            aderentiCol.setCellValueFactory(c -> {
                Sessione s = c.getValue();
                Integer val = null;
                if (s instanceof SessionePresenza) {
                    val = ((SessionePresenza) s).getNumAderenti();
                }
                return new javafx.beans.property.SimpleObjectProperty<>(val);
            });
        }
    }

    public void initData(Corso corso, Chef chef) {
        this.corso = corso;
        this.chef = chef;
        if (corsoLabel != null && corso != null) {
            String nome = corso.getNome();
            String id = corso.getIdCorso();
            if (nome != null && !nome.isBlank()) {
                corsoLabel.setText(nome + (id != null ? " (ID " + id + ")" : ""));
            } else if (id != null) {
                corsoLabel.setText("ID " + id);
            }
        }
        loadSessioni();
    }

    public void initData(Corso corso) {
        initData(corso, null);
    }

    private void loadSessioni() {
        if (corso == null) {
            return;
        }
        try {
            java.util.List<Sessione> sessioni = GestoreSessioni.getInstance().getSessioniByCorso(corso);
            if (sessioniTable != null) {
                sessioniTable.setItems(javafx.collections.FXCollections.observableArrayList(sessioni));
                if (!sessioni.isEmpty()) {
                    sessioniTable.getSelectionModel().selectFirst();
                }
            }
        } catch (Exception e) {
            System.err.println("[GestioneSessioniBoundary] Errore caricamento sessioni: " + e.getMessage());
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Sessioni");
            alert.setHeaderText("Errore caricamento sessioni");
            alert.setContentText("Impossibile caricare le sessioni del corso.");
            alert.showAndWait();
        }
    }

    @FXML
    private void onAddSessioneClick(ActionEvent event) {
        if (corso == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Sessioni");
            alert.setHeaderText("Corso non selezionato");
            alert.setContentText("Impossibile creare una sessione senza un corso.");
            alert.showAndWait();
            return;
        }

        LocalDate data = dataSessionePicker != null ? dataSessionePicker.getValue() : null;
        String oraText = oraSessioneField != null ? oraSessioneField.getText() : null;
        TipoSessione tipo = tipoSessioneCombo != null ? tipoSessioneCombo.getValue() : null;
        String teoria = teoriaField != null ? teoriaField.getText() : null;

        if (data == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Sessioni");
            alert.setHeaderText("Data mancante");
            alert.setContentText("Seleziona la data della sessione.");
            alert.showAndWait();
            return;
        }
        if (tipo == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Sessioni");
            alert.setHeaderText("Tipo mancante");
            alert.setContentText("Seleziona il tipo di sessione.");
            alert.showAndWait();
            return;
        }
        if (oraText == null || oraText.isBlank()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Sessioni");
            alert.setHeaderText("Orario mancante");
            alert.setContentText("Inserisci l'orario in formato HH:mm.");
            alert.showAndWait();
            return;
        }

        LocalTime ora;
        try {
            ora = LocalTime.parse(oraText.trim(), DateTimeFormatter.ofPattern("HH:mm"));
        } catch (DateTimeParseException ex) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Sessioni");
            alert.setHeaderText("Formato orario non valido");
            alert.setContentText("Usa il formato HH:mm (es. 18:30).");
            alert.showAndWait();
            return;
        }

        if (tipo == TipoSessione.online && (teoria == null || teoria.isBlank())) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Sessioni");
            alert.setHeaderText("Teoria mancante");
            alert.setContentText("Inserisci la teoria per le sessioni online.");
            alert.showAndWait();
            return;
        }

        try {
            LocalDateTime dataOra = LocalDateTime.of(data, ora);
            GestoreSessioni.getInstance().creaSessione(corso, dataOra, tipo, teoria);
            if (dataSessionePicker != null) dataSessionePicker.setValue(null);
            if (oraSessioneField != null) oraSessioneField.clear();
            if (teoriaField != null) teoriaField.clear();
            loadSessioni();
        } catch (RuntimeException ex) {
            System.err.println("[GestioneSessioniBoundary] Errore creazione sessione: " + ex.getMessage());
            ex.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Sessioni");
            alert.setHeaderText("Errore creazione sessione");
            alert.setContentText(ex.getMessage());
            alert.showAndWait();
        }
    }

    @FXML
    private void onBackClick(ActionEvent event) {
        try {
            java.net.URL location = getClass().getResource("/com/unina/foodlab/Boundary/fxml/home.fxml");
            if (location == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Home");
                alert.setHeaderText("Schermata non trovata");
                alert.setContentText("Risorsa home.fxml non trovata nel classpath.");
                alert.showAndWait();
                return;
            }

            FXMLLoader loader = new FXMLLoader(location);
            Parent root = loader.load();

            HomeBoundary controller = loader.getController();
            controller.initData(chef);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.centerOnScreen();
            stage.show();
        } catch (IOException e) {
            System.err.println("[GestioneSessioniBoundary] Errore ritorno home: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void onOpenPraticheClick(ActionEvent event) {
        try {
            java.net.URL location = getClass().getResource("/com/unina/foodlab/Boundary/fxml/gestioneSessioniPratiche.fxml");
            if (location == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Sessioni pratiche");
                alert.setHeaderText("Schermata non trovata");
                alert.setContentText("Risorsa gestioneSessioniPratiche.fxml non trovata nel classpath.");
                alert.showAndWait();
                return;
            }

            FXMLLoader loader = new FXMLLoader(location);
            Parent root = loader.load();

            GestioneSessioniPraticheBoundary controller = loader.getController();
            controller.initData(corso, chef);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.centerOnScreen();
            stage.show();
        } catch (IOException e) {
            System.err.println("[GestioneSessioniBoundary] Errore apertura gestioneSessioniPratiche.fxml: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
