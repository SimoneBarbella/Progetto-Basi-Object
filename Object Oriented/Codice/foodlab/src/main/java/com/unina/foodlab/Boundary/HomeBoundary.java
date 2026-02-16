package com.unina.foodlab.Boundary;

import com.unina.foodlab.Boundary.util.InterfacciaFx;
import com.unina.foodlab.Boundary.util.NavigatoreScene;
import com.unina.foodlab.Controller.GestoreCorsi;
import com.unina.foodlab.Entity.Chef;
import com.unina.foodlab.Entity.Corso;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;

import java.time.LocalDate;
import java.util.List;

public class HomeBoundary {

    @FXML
    private Label welcomeLabel;

    @FXML
    private TableView<Corso> corsiTable;

    @FXML
    private TableColumn<Corso, String> idCol;

    @FXML
    private TableColumn<Corso, String> nomeCol;

    @FXML
    private TableColumn<Corso, LocalDate> dataCol;

    @FXML
    private TableColumn<Corso, String> frequenzaCol;

    @FXML
    private TableColumn<Corso, Integer> sessioniCol;

    @FXML
    private TableColumn<Corso, Integer> partecipantiCol;

    @FXML
    private TableColumn<Corso, String> categorieCol;

    @FXML
    private Button gestisciSessioniButton;

    @FXML
    private Button gestioneSessioniActionButton;

    private Chef chef;

    @FXML
    private void initialize() {
        if (corsiTable != null) {
            corsiTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
            aggiornaGestioneSessioniButtons(true);
            corsiTable.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
                aggiornaGestioneSessioniButtons(newVal == null);
            });
            corsiTable.setOnMouseClicked(e -> {
                aggiornaGestioneSessioniButtons(corsiTable.getSelectionModel().getSelectedItem() == null);
            });
        }
    }

    private void aggiornaGestioneSessioniButtons(boolean disable) {
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
            welcomeLabel.setText(messaggioDiBenvenuto(chef));
        }
        // carica i corsi gestiti
		caricaCorsi();
    }

    private static String messaggioDiBenvenuto(Chef chef) {
        if (chef == null) {
            return "Benvenuto Chef";
        }
        String cognome = chef.getCognome();
        return (cognome != null && !cognome.isBlank()) ? ("Benvenuto Chef " + cognome) : "Benvenuto Chef";
    }

    private void caricaCorsi() {
        try {
            if (chef == null) {
                return;
            }
            List<Corso> corsi = GestoreCorsi.getInstanza().getCorsiGestiti(chef);

            idCol.setCellValueFactory(c -> {
                Integer id = c.getValue() != null ? c.getValue().getIdCorso() : null;
                return new SimpleStringProperty(id != null ? String.valueOf(id) : "");
            });
            nomeCol.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getNome()));
            dataCol.setCellValueFactory(c -> new SimpleObjectProperty<>(c.getValue().getDataInizio()));
            frequenzaCol.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getFrequenza()));
            sessioniCol.setCellValueFactory(c -> new SimpleIntegerProperty(c.getValue().getNumSessioni()).asObject());
            partecipantiCol.setCellValueFactory(c -> new SimpleIntegerProperty(c.getValue().getNumPartecipanti()).asObject());
            categorieCol.setCellValueFactory(c -> new SimpleStringProperty(String.join(", ", c.getValue().getCategorie())));

            corsiTable.setItems(FXCollections.observableArrayList(corsi));
            aggiornaGestioneSessioniButtons(corsiTable.getSelectionModel().getSelectedItem() == null);
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
			caricaCorsi();
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
