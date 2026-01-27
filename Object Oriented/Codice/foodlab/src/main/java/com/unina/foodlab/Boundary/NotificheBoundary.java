package com.unina.foodlab.Boundary;

import com.unina.foodlab.Controller.GestoreNotifiche;
import com.unina.foodlab.Controller.GestoreCorsi;
import com.unina.foodlab.Entity.Chef;
import com.unina.foodlab.Entity.Corso;
import com.unina.foodlab.Entity.Notifica;

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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.io.IOException;
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

    @FXML
    private ComboBox<CorsoChoice> corsoCombo;

    @FXML
    private TextArea messaggioArea;

    private Chef chef;

    private final Map<Integer, String> corsoNameById = new HashMap<>();

    private final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

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
                String val;
                if (id == null) {
                    val = "Tutti";
                } else {
                    String nome = corsoNameById.get(id);
                    val = (nome != null && !nome.isBlank()) ? (id + " - " + nome) : String.valueOf(id);
                }
                return new javafx.beans.property.SimpleStringProperty(val);
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

        loadCorsiChoices();
        refreshNotifiche();
    }

    @FXML
    private void onAggiornaClick(ActionEvent event) {
        refreshNotifiche();
    }

    private void refreshNotifiche() {
        if (chef == null) {
            showWarning("Chef non valido.");
            return;
        }

        try {
            List<Notifica> list = GestoreNotifiche.getInstance().getNotifiche(chef);
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

        String corso;
        if (selected.getIdCorso() == null) {
            corso = "Tutti";
        } else {
            String nome = corsoNameById.get(selected.getIdCorso());
            corso = (nome != null && !nome.isBlank()) ? (selected.getIdCorso() + " - " + nome) : String.valueOf(selected.getIdCorso());
        }

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

    @FXML
    private void onInviaClick(ActionEvent event) {
        if (chef == null) {
            showWarning("Chef non valido.");
            return;
        }

        String messaggio = messaggioArea != null ? messaggioArea.getText() : null;
        if (messaggio == null || messaggio.isBlank()) {
            showWarning("Inserisci un messaggio.");
            return;
        }

        Integer idCorso = null;
        if (corsoCombo != null) {
            CorsoChoice selected = corsoCombo.getSelectionModel().getSelectedItem();
            if (selected != null) {
                idCorso = selected.idCorso;
            }
        }

        try {
            GestoreNotifiche.getInstance().inviaNotifica(chef, messaggio, idCorso);
            if (messaggioArea != null) {
                messaggioArea.clear();
            }
            refreshNotifiche();
            showInfo("Notifica inviata.");
        } catch (RuntimeException ex) {
            showError(ex.getMessage());
        }
    }

    private void loadCorsiChoices() {
        if (corsoCombo == null) {
            return;
        }
        corsoNameById.clear();

        corsoCombo.getItems().clear();
        corsoCombo.getItems().add(new CorsoChoice(null, "Tutti i corsi"));

        if (chef == null) {
            corsoCombo.getSelectionModel().selectFirst();
            return;
        }

        try {
            List<Corso> corsi = GestoreCorsi.getInstance().getCorsiGestiti(chef);
            for (Corso c : corsi) {
                Integer id = safeParseInt(c != null ? c.getIdCorso() : null);
                String nome = c != null ? c.getNome() : null;
                if (id != null) {
                    corsoNameById.put(id, nome != null ? nome : "");
                    corsoCombo.getItems().add(new CorsoChoice(id, id + " - " + (nome != null ? nome : "")));
                }
            }
        } catch (Exception e) {
            System.err.println("[NotificheBoundary] Errore caricamento corsi: " + e.getMessage());
        }

        corsoCombo.getSelectionModel().selectFirst();
    }

    private Integer safeParseInt(String s) {
        if (s == null) return null;
        String t = s.trim();
        if (t.isEmpty()) return null;
        try {
            return Integer.parseInt(t);
        } catch (NumberFormatException ex) {
            return null;
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
            GestoreNotifiche.getInstance().eliminaNotifica(selected.getIdNotifica());
            refreshNotifiche();
        } catch (RuntimeException ex) {
            showError(ex.getMessage());
        }
    }

    @FXML
    private void onBackClick(ActionEvent event) {
        try {
            java.net.URL location = getClass().getResource("/com/unina/foodlab/Boundary/fxml/home.fxml");
            if (location == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Notifiche");
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
            System.err.println("[NotificheBoundary] Errore ritorno Home: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void showWarning(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Notifiche");
        alert.setHeaderText("Attenzione");
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Notifiche");
        alert.setHeaderText("Errore");
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showInfo(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Notifiche");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private static class CorsoChoice {
        private final Integer idCorso;
        private final String label;

        private CorsoChoice(Integer idCorso, String label) {
            this.idCorso = idCorso;
            this.label = label;
        }

        @Override
        public String toString() {
            return label;
        }
    }
}
