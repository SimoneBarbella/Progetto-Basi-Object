package com.unina.foodlab.Boundary;

import com.unina.foodlab.Controller.GestoreCorsi;
import com.unina.foodlab.Entity.Chef;
import com.unina.foodlab.Entity.Corso;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

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
        if (chef != null) {
            String cognome = chef.getCognome();
            if (cognome != null && !cognome.isBlank()) {
                welcomeLabel.setText("Benvenuto Chef " + cognome);
            } else {
                welcomeLabel.setText("Benvenuto Chef");
            }
        } else {
            welcomeLabel.setText("Benvenuto Chef");
        }
        // carica i corsi gestiti
        loadCorsi();
    }

    private void loadCorsi() {
        try {
            if (chef == null) {
                return;
            }
            var corsi = GestoreCorsi.getInstance().getCorsiGestiti(chef);

            idCol.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getIdCorso()));
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
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/unina/foodlab/Boundary/fxml/creazioneCorso.fxml"));
            Parent root = loader.load();

            CreazioneCorsoBoundary controller = loader.getController();
            controller.initData(chef);

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Creazione Corso");
            stage.setScene(new Scene(root));
            stage.centerOnScreen();
            stage.showAndWait();

            // dopo la chiusura della finestra ricarica i corsi
            loadCorsi();
        } catch (IOException e) {
            System.err.println("[HomeBoundary] Errore caricamento creazioneCorso.fxml: " + e.getMessage());
            e.printStackTrace();

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Creazione corso");
            alert.setHeaderText("Errore apertura schermata");
            alert.setContentText("Impossibile aprire la schermata di creazione corso.");
            alert.showAndWait();
        }
    }

    @FXML
    private void onGestisciSessioniClick(ActionEvent event) {
        Corso selected = corsiTable != null ? corsiTable.getSelectionModel().getSelectedItem() : null;
        if (selected == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Sessioni");
            alert.setHeaderText("Seleziona un corso");
            alert.setContentText("Seleziona un corso dalla tabella per gestire le sessioni.");
            alert.showAndWait();
            return;
        }

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
            controller.initData(selected, chef);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.centerOnScreen();
            stage.show();
        } catch (IOException e) {
            System.err.println("[HomeBoundary] Errore apertura gestioneSessioni.fxml: " + e.getMessage());
            e.printStackTrace();

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Sessioni");
            alert.setHeaderText("Errore apertura schermata");
            alert.setContentText("Impossibile aprire la schermata delle sessioni: " + e.getMessage());
            alert.showAndWait();
        }
    }

    @FXML
    private void onLogoutClick(ActionEvent event) {
        try {
            java.net.URL location = getClass().getResource("/com/unina/foodlab/Boundary/fxml/LoginView.fxml");
            if (location == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Logout");
                alert.setHeaderText("Errore logout");
                alert.setContentText("Risorsa LoginView.fxml non trovata nel classpath.");
                alert.showAndWait();
                return;
            }

            FXMLLoader loader = new FXMLLoader(location);
            Parent root = loader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.centerOnScreen();
            stage.show();
        } catch (IOException e) {
            System.err.println("[HomeBoundary] Errore durante il logout: " + e.getMessage());
            e.printStackTrace();

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Logout");
            alert.setHeaderText("Errore durante il logout");
            alert.setContentText("Impossibile tornare alla schermata di login: " + e.getMessage());
            alert.showAndWait();
        }
    }

    @FXML
    private void onReportClick(ActionEvent event) {
        try {
            java.net.URL location = getClass().getResource("/com/unina/foodlab/Boundary/fxml/report.fxml");
            if (location == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Report");
                alert.setHeaderText("Schermata non trovata");
                alert.setContentText("Risorsa report.fxml non trovata nel classpath.");
                alert.showAndWait();
                return;
            }

            FXMLLoader loader = new FXMLLoader(location);
            Parent root = loader.load();

            ReportBoundary controller = loader.getController();
            controller.initData(chef);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.centerOnScreen();
            stage.show();
        } catch (IOException e) {
            System.err.println("[HomeBoundary] Errore apertura report.fxml: " + e.getMessage());
            e.printStackTrace();

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Report");
            alert.setHeaderText("Errore apertura schermata");
            alert.setContentText("Impossibile aprire la schermata Report: " + e.getMessage());
            alert.showAndWait();
        }
    }

    @FXML
    private void onNotificheClick(ActionEvent event) {
        try {
            java.net.URL location = getClass().getResource("/com/unina/foodlab/Boundary/fxml/notifiche.fxml");
            if (location == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Notifiche");
                alert.setHeaderText("Schermata non trovata");
                alert.setContentText("Risorsa notifiche.fxml non trovata nel classpath.");
                alert.showAndWait();
                return;
            }

            FXMLLoader loader = new FXMLLoader(location);
            Parent root = loader.load();

            NotificheBoundary controller = loader.getController();
            controller.initData(chef);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.centerOnScreen();
            stage.show();
        } catch (IOException e) {
            System.err.println("[HomeBoundary] Errore apertura notifiche.fxml: " + e.getMessage());
            e.printStackTrace();

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Notifiche");
            alert.setHeaderText("Errore apertura schermata");
            alert.setContentText("Impossibile aprire la schermata Notifiche: " + e.getMessage());
            alert.showAndWait();
        }
    }
}
