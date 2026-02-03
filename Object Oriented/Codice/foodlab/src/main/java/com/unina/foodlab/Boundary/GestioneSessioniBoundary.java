package com.unina.foodlab.Boundary;

import com.unina.foodlab.Controller.GestoreSessioni;
import com.unina.foodlab.Boundary.util.InterfacciaFx;
import com.unina.foodlab.Boundary.util.FormatiDataOra;
import com.unina.foodlab.Boundary.util.NavigatoreScene;
import com.unina.foodlab.Entity.Chef;
import com.unina.foodlab.Entity.Corso;
import com.unina.foodlab.Entity.Sessione;
import com.unina.foodlab.Entity.SessioneOnline;
import com.unina.foodlab.Entity.SessionePresenza;
import com.unina.foodlab.Enum.TipoSessione;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
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
                String val = d != null ? d.format(FormatiDataOra.YYYY_MM_DD_HH_MM) : "";
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
			Integer id = corso.getIdCorso();
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
            InterfacciaFx.showError("Sessioni", "Errore caricamento sessioni", "Impossibile caricare le sessioni del corso.");
        }
    }

    @FXML
    private void onAddSessioneClick(ActionEvent event) {
        if (corso == null) {
            InterfacciaFx.showWarning("Sessioni", "Corso non selezionato", "Impossibile creare una sessione senza un corso.");
            return;
        }

        LocalDate data = dataSessionePicker != null ? dataSessionePicker.getValue() : null;
        String oraText = oraSessioneField != null ? oraSessioneField.getText() : null;
        TipoSessione tipo = tipoSessioneCombo != null ? tipoSessioneCombo.getValue() : null;
        String teoria = teoriaField != null ? teoriaField.getText() : null;

        LocalTime ora = validateAndParseOraSessione(data, tipo, oraText, teoria);
        if (ora == null) {
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
            InterfacciaFx.showError("Sessioni", "Errore creazione sessione", ex.getMessage());
        }
    }

    private static LocalTime validateAndParseOraSessione(LocalDate data, TipoSessione tipo, String oraText, String teoria) {
        if (data == null) {
            InterfacciaFx.showWarning("Sessioni", "Data mancante", "Seleziona la data della sessione.");
            return null;
        }
        if (tipo == null) {
            InterfacciaFx.showWarning("Sessioni", "Tipo mancante", "Seleziona il tipo di sessione.");
            return null;
        }
        if (oraText == null || oraText.isBlank()) {
            InterfacciaFx.showWarning("Sessioni", "Orario mancante", "Inserisci l'orario in formato HH:mm.");
            return null;
        }

        final LocalTime ora;
        try {
            ora = LocalTime.parse(oraText.trim(), FormatiDataOra.HH_MM);
        } catch (DateTimeParseException ex) {
            InterfacciaFx.showWarning("Sessioni", "Formato orario non valido", "Usa il formato HH:mm (es. 18:30).");
            return null;
        }

        if (tipo == TipoSessione.online && (teoria == null || teoria.isBlank())) {
            InterfacciaFx.showWarning("Sessioni", "Teoria mancante", "Inserisci la teoria per le sessioni online.");
            return null;
        }

        return ora;
    }

    @FXML
    private void onBackClick(ActionEvent event) {
        final Chef loggedChef = chef;
        NavigatoreScene.switchScene(
            event,
            "Home",
            "/com/unina/foodlab/Boundary/fxml/home.fxml",
            "Risorsa home.fxml non trovata nel classpath.",
            (HomeBoundary controller) -> controller.initData(loggedChef),
            HomeBoundary.class
        );
    }

    @FXML
    private void onOpenPraticheClick(ActionEvent event) {
        final Corso selectedCorso = corso;
        final Chef loggedChef = chef;
        NavigatoreScene.switchScene(
            event,
            "Sessioni pratiche",
            "/com/unina/foodlab/Boundary/fxml/gestioneSessioniPratiche.fxml",
            "Risorsa gestioneSessioniPratiche.fxml non trovata nel classpath.",
            (GestioneSessioniPraticheBoundary controller) -> controller.initData(selectedCorso, loggedChef),
            GestioneSessioniPraticheBoundary.class
        );
    }
}
