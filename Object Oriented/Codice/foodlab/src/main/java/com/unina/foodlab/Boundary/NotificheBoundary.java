package com.unina.foodlab.Boundary;

import com.unina.foodlab.Controller.GestoreNotifiche;
import com.unina.foodlab.Controller.GestoreCorsi;
import com.unina.foodlab.Boundary.util.FormatiDataOra;
import com.unina.foodlab.Boundary.util.InterfacciaFx;
import com.unina.foodlab.Boundary.util.NavigatoreScene;
import com.unina.foodlab.Entity.Chef;
import com.unina.foodlab.Entity.Corso;
import com.unina.foodlab.Entity.Notifica;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NotificheBoundary {

    @FXML
    private Label chefLabel;

    @FXML
    private TableView<Notifica> notificheTable;

    @FXML
    private TableColumn<Notifica, String> dataCol;

    @FXML
    private TableColumn<Notifica, String> corsoCol;

    @FXML
    private Button eliminaButton;

    @FXML
    private Button dettagliButton;

    private Chef chef;

    private final Map<Integer, String> corsoNameById = new HashMap<>();

    private final DateTimeFormatter dtf = FormatiDataOra.YYYY_MM_DD_HH_MM;

    @FXML
    private void initialize() {
        if (dataCol != null) {
            dataCol.setCellValueFactory(c -> {
                LocalDateTime dt = c.getValue() != null ? c.getValue().getDataInvio() : null;
                String val = dt != null ? dt.format(dtf) : "";
                return new javafx.beans.property.SimpleStringProperty(val);
            });
        }

        if (corsoCol != null) {
            corsoCol.setCellValueFactory(c -> {
                Integer id = c.getValue() != null ? c.getValue().getIdCorso() : null;
                return new javafx.beans.property.SimpleStringProperty(formatCorsoLabel(id));
            });
        }

        if (notificheTable != null) {
            notificheTable.getSelectionModel().selectedItemProperty().addListener((obs, o, n) -> {
                if (eliminaButton != null) {
                    eliminaButton.setDisable(n == null);
                }
                if (dettagliButton != null) {
                    dettagliButton.setDisable(n == null);
                }
            });
        }

    }

    public void initData(Chef chef) {
        this.chef = chef;
        if (chefLabel != null && chef != null) {
            String nome = chef.getNome();
            chefLabel.setText(nome != null && !nome.isBlank() ? nome : chef.getEmail());
        }

        caricaScelteCorsi();
        aggiornaNotifiche();
    }

    @FXML
    private void onNuovaNotificaClick(ActionEvent event) {
        NavigatoreScene.switchScene(
            event,
            "Notifiche",
            "/com/unina/foodlab/Boundary/fxml/scriviNotifica.fxml",
            "Risorsa scriviNotifica.fxml non trovata nel classpath.",
            (ScriviNotificaBoundary controller) -> controller.initData(chef),
            ScriviNotificaBoundary.class
        );
    }

    @FXML
    private void onAggiornaClick(ActionEvent event) {
        aggiornaNotifiche();
    }

    private void aggiornaNotifiche() {
        if (chef == null) {
            showWarning("Chef non valido.");
            return;
        }

        try {
            List<Notifica> list = GestoreNotifiche.getInstanza().getNotifiche(chef);
            if (notificheTable != null) {
                notificheTable.setItems(javafx.collections.FXCollections.observableArrayList(list));
            }
            if (eliminaButton != null) {
                eliminaButton.setDisable(true);
            }
            if (dettagliButton != null) {
                dettagliButton.setDisable(true);
            }
        } catch (RuntimeException ex) {
            showError(ex.getMessage());
        }
    }

    @FXML
    private void onDettagliClick(ActionEvent event) {
        if (notificheTable == null) return;
        Notifica selected = notificheTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showWarning("Seleziona una notifica.");
            return;
        }

        String data = selected.getDataInvio() != null ? selected.getDataInvio().format(dtf) : "";

        String corso = formatCorsoLabel(selected.getIdCorso());

        String msg = selected.getMessaggio() != null ? selected.getMessaggio() : "";

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Dettagli notifica");
        alert.setHeaderText("Data: " + data + " | Corso: " + corso);

        TextArea area = new TextArea(msg);
        area.setWrapText(true);
        area.setEditable(false);
        area.setPrefRowCount(10);
        area.setPrefWidth(700);

        alert.getDialogPane().setContent(area);
        alert.getDialogPane().setPrefWidth(760);
        alert.showAndWait();
    }

    private String formatCorsoLabel(Integer idCorso) {
        if (idCorso == null) {
            return "Tutti i corsi";
        }
        String nome = corsoNameById.get(idCorso);
        return (nome != null && !nome.isBlank()) ? (idCorso + " - " + nome) : String.valueOf(idCorso);
    }

    private void caricaScelteCorsi() {
        corsoNameById.clear();

        if (chef == null) {
            return;
        }

        try {
            List<Corso> corsi = GestoreCorsi.getInstanza().getCorsiGestiti(chef);
            for (Corso c : corsi) {
				Integer id = c != null ? c.getIdCorso() : null;
                String nome = c != null ? c.getNome() : null;
                if (id != null) {
                    corsoNameById.put(id, nome != null ? nome : "");
                }
            }
        } catch (Exception e) {
            System.err.println("[NotificheBoundary] Errore caricamento corsi: " + e.getMessage());
        }
    }

    @FXML
    private void onEliminaClick(ActionEvent event) {
        if (notificheTable == null) return;
        Notifica selected = notificheTable.getSelectionModel().getSelectedItem();
        if (selected == null || selected.getIdNotifica() == null) {
            showWarning("Seleziona una notifica valida.");
            return;
        }

        try {
            GestoreNotifiche.getInstanza().eliminaNotifica(selected.getIdNotifica());
            aggiornaNotifiche();
        } catch (RuntimeException ex) {
            showError(ex.getMessage());
        }
    }

    @FXML
    private void onBackClick(ActionEvent event) {
        NavigatoreScene.switchScene(
            event,
            "Notifiche",
            "/com/unina/foodlab/Boundary/fxml/home.fxml",
            "Risorsa home.fxml non trovata nel classpath.",
            (HomeBoundary controller) -> controller.initData(chef),
            HomeBoundary.class
        );
    }

    private void showWarning(String message) {
        InterfacciaFx.showWarning("Notifiche", "Attenzione", message);
    }

    private void showError(String message) {
        InterfacciaFx.showError("Notifiche", "Errore", message);
    }

}
