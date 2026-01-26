package com.unina.foodlab.Boundary;

import com.unina.foodlab.Controller.GestoreSessioni;
import com.unina.foodlab.Entity.Chef;
import com.unina.foodlab.Entity.Corso;
import com.unina.foodlab.Entity.IngredienteQuantita;
import com.unina.foodlab.Entity.SessionePresenza;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ListaSpesaBoundary {

    @FXML
    private Label corsoLabel;

    @FXML
    private Label sessioneLabel;

    @FXML
    private Label totaleLabel;

    @FXML
    private ListView<IngredienteQuantita> spesaListView;

    private Corso corso;
    private Chef chef;
    private SessionePresenza sessione;

    @FXML
    private void initialize() {
        if (spesaListView != null) {
            spesaListView.setCellFactory(list -> new javafx.scene.control.ListCell<>() {
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

    public void initData(SessionePresenza sessione, Corso corso, Chef chef) {
        this.sessione = sessione;
        this.corso = corso;
        this.chef = chef;

        if (corsoLabel != null && corso != null) {
            String nome = corso.getNome();
            String id = corso.getIdCorso();
            corsoLabel.setText(nome + (id != null ? " (ID " + id + ")" : ""));
        }

        if (sessioneLabel != null && sessione != null && sessione.getOraInizio() != null) {
            String data = sessione.getOraInizio().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
            sessioneLabel.setText("Sessione: " + data);
        }

        if (sessione != null && sessione.getIdSessione() != null) {
            List<IngredienteQuantita> lista = GestoreSessioni.getInstance().getListaSpesaBySessioneId(sessione.getIdSessione());
            if (spesaListView != null) {
                spesaListView.setItems(javafx.collections.FXCollections.observableArrayList(lista));
            }
            if (totaleLabel != null) {
                double tot = GestoreSessioni.getInstance().getQuantitaTotaleBySessioneId(sessione.getIdSessione());
                totaleLabel.setText("Totale: " + tot);
            }
        } else {
            if (spesaListView != null) {
                spesaListView.setItems(javafx.collections.FXCollections.observableArrayList());
            }
            if (totaleLabel != null) {
                totaleLabel.setText("Totale: 0");
            }
        }
    }

    @FXML
    private void onBackClick(ActionEvent event) {
        try {
            java.net.URL location = getClass().getResource("/com/unina/foodlab/Boundary/fxml/gestioneSessioniPratiche.fxml");
            if (location == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Lista della spesa");
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
            System.err.println("[ListaSpesaBoundary] Errore ritorno: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
