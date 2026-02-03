package com.unina.foodlab.Boundary;

import com.unina.foodlab.Controller.GestoreCorsi;
import com.unina.foodlab.Boundary.util.InterfacciaFx;
import com.unina.foodlab.Boundary.util.NavigatoreScene;
import com.unina.foodlab.Entity.Chef;
import com.unina.foodlab.Entity.Corso;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class HomeBoundary {

    @FXML
    private Label welcomeLabel;

    @FXML
    private javafx.scene.control.TableView<Corso> corsiTable;

    @FXML
    private javafx.scene.control.TableColumn<Corso, String> idCol;

    @FXML
    private javafx.scene.control.TableColumn<Corso, String> nomeCol;

    @FXML
    private javafx.scene.control.TableColumn<Corso, java.time.LocalDate> dataCol;

    @FXML
    private javafx.scene.control.TableColumn<Corso, String> frequenzaCol;

    @FXML
    private javafx.scene.control.TableColumn<Corso, Integer> sessioniCol;

    @FXML
    private javafx.scene.control.TableColumn<Corso, Integer> partecipantiCol;

    @FXML
    private javafx.scene.control.TableColumn<Corso, String> categorieCol;

    @FXML
    private Button gestisciSessioniButton;

    @FXML
    private Button gestioneSessioniActionButton;

    private Chef chef;

    @FXML
    private void initialize() {
        if (corsiTable != null) {
            corsiTable.getSelectionModel().setSelectionMode(javafx.scene.control.SelectionMode.SINGLE);
            updateGestioneSessioniButtons(true);
            corsiTable.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
                updateGestioneSessioniButtons(newVal == null);
            });
            corsiTable.setOnMouseClicked(e -> {
                updateGestioneSessioniButtons(corsiTable.getSelectionModel().getSelectedItem() == null);
            });
        }
    }

    private void updateGestioneSessioniButtons(boolean disable) {
        if (gestisciSessioniButton != null) {
            gestisciSessioniButton.setDisable(disable);
        }
        if (gestioneSessioniActionButton != null) {
            gestioneSessioniActionButton.setDisable(disable);
        }
    }

    public void initData(Chef chef) {
        this.chef = chef;
        if (welcomeLabel != null) {
            welcomeLabel.setText(welcomeTextFor(chef));
        }
        // carica i corsi gestiti
        loadCorsi();
    }

    private static String welcomeTextFor(Chef chef) {
        if (chef == null) {
            return "Benvenuto Chef";
        }
        String cognome = chef.getCognome();
        return (cognome != null && !cognome.isBlank()) ? ("Benvenuto Chef " + cognome) : "Benvenuto Chef";
    }

    private void loadCorsi() {
        try {
            if (chef == null) {
                return;
            }
            var corsi = GestoreCorsi.getInstance().getCorsiGestiti(chef);

            idCol.setCellValueFactory(c -> {
                Integer id = c.getValue() != null ? c.getValue().getIdCorso() : null;
                return new javafx.beans.property.SimpleStringProperty(id != null ? String.valueOf(id) : "");
            });
            nomeCol.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getNome()));
            dataCol.setCellValueFactory(c -> new javafx.beans.property.SimpleObjectProperty<>(c.getValue().getDataInizio()));
            frequenzaCol.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getFrequenza()));
            sessioniCol.setCellValueFactory(c -> new javafx.beans.property.SimpleIntegerProperty(c.getValue().getNumSessioni()).asObject());
            partecipantiCol.setCellValueFactory(c -> new javafx.beans.property.SimpleIntegerProperty(c.getValue().getNumPartecipanti()).asObject());
            categorieCol.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(String.join(", ", c.getValue().getCategorie())));

            corsiTable.setItems(javafx.collections.FXCollections.observableArrayList(corsi));
            updateGestioneSessioniButtons(corsiTable.getSelectionModel().getSelectedItem() == null);
        } catch (Exception e) {
            System.err.println("[HomeBoundary] Errore caricamento corsi: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void onCreaCorsoClick(ActionEvent event) {
        final Chef loggedChef = chef;
        boolean opened = NavigatoreScene.openModal(
            "Creazione corso",
            "Creazione Corso",
            "/com/unina/foodlab/Boundary/fxml/creazioneCorso.fxml",
            "Risorsa creazioneCorso.fxml non trovata nel classpath.",
            (CreazioneCorsoBoundary controller) -> controller.initData(loggedChef),
            CreazioneCorsoBoundary.class
        );
        if (opened) {
            loadCorsi();
        }
    }

    @FXML
    private void onGestisciSessioniClick(ActionEvent event) {
        Corso selected = corsiTable != null ? corsiTable.getSelectionModel().getSelectedItem() : null;
        if (selected == null) {
            InterfacciaFx.showWarning("Sessioni", "Seleziona un corso", "Seleziona un corso dalla tabella per gestire le sessioni.");
            return;
        }

        final Corso selectedCorso = selected;
        final Chef loggedChef = chef;
        NavigatoreScene.switchScene(
            event,
            "Sessioni",
            "/com/unina/foodlab/Boundary/fxml/gestioneSessioni.fxml",
            "Risorsa gestioneSessioni.fxml non trovata nel classpath.",
            (GestioneSessioniBoundary controller) -> controller.initData(selectedCorso, loggedChef),
            GestioneSessioniBoundary.class
        );
    }

    @FXML
    private void onLogoutClick(ActionEvent event) {
        NavigatoreScene.switchScene(
            event,
            "Logout",
            "/com/unina/foodlab/Boundary/fxml/LoginView.fxml",
            "Risorsa LoginView.fxml non trovata nel classpath."
        );
    }

    @FXML
    private void onReportClick(ActionEvent event) {
        final Chef loggedChef = chef;
        NavigatoreScene.switchScene(
            event,
            "Report",
            "/com/unina/foodlab/Boundary/fxml/report.fxml",
            "Risorsa report.fxml non trovata nel classpath.",
            (ReportBoundary controller) -> controller.initData(loggedChef),
            ReportBoundary.class
        );
    }

    @FXML
    private void onNotificheClick(ActionEvent event) {
        final Chef loggedChef = chef;
        NavigatoreScene.switchScene(
            event,
            "Notifiche",
            "/com/unina/foodlab/Boundary/fxml/notifiche.fxml",
            "Risorsa notifiche.fxml non trovata nel classpath.",
            (NotificheBoundary controller) -> controller.initData(loggedChef),
            NotificheBoundary.class
        );
    }
}
