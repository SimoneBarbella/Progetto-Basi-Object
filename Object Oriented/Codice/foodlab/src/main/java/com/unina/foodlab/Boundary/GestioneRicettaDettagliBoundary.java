package com.unina.foodlab.Boundary;

import com.unina.foodlab.Controller.GestoreRicette;
import com.unina.foodlab.Entity.Chef;
import com.unina.foodlab.Entity.Corso;
import com.unina.foodlab.Entity.IngredienteQuantita;
import com.unina.foodlab.Entity.Ricetta;

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
import java.util.List;

public class GestioneRicettaDettagliBoundary {

    @FXML
    private Label corsoLabel;

    @FXML
    private Label ricettaNomeLabel;

    @FXML
    private Label ricettaTempoLabel;

    @FXML
    private Label ricettaDescrLabel;

    @FXML
    private ListView<IngredienteQuantita> ingredientiListView;

    private Corso corso;
    private Chef chef;
    private Ricetta ricetta;

    @FXML
    private void initialize() {
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

    public void initData(Ricetta ricetta, Corso corso, Chef chef) {
        this.ricetta = ricetta;
        this.corso = corso;
        this.chef = chef;

        if (corsoLabel != null && corso != null) {
            String nome = corso.getNome();
            String id = corso.getIdCorso();
            corsoLabel.setText(nome + (id != null ? " (ID " + id + ")" : ""));
        }

        if (ricettaNomeLabel != null) {
            ricettaNomeLabel.setText("Nome: " + (ricetta != null ? ricetta.getNome() : ""));
        }
        if (ricettaTempoLabel != null) {
            String tempo = ricetta != null && ricetta.getTempo() != null ? ricetta.getTempo().toString() : "";
            ricettaTempoLabel.setText("Tempo: " + tempo);
        }
        if (ricettaDescrLabel != null) {
            ricettaDescrLabel.setText("Descrizione: " + (ricetta != null ? ricetta.getDescrizione() : ""));
        }

        if (ingredientiListView != null && ricetta != null && ricetta.getIdRicetta() != null) {
            List<IngredienteQuantita> ingredienti = GestoreRicette.getInstance().getIngredientiPerRicetta(ricetta.getIdRicetta());
            ingredientiListView.setItems(javafx.collections.FXCollections.observableArrayList(ingredienti));
        }
    }

    @FXML
    private void onBackClick(ActionEvent event) {
        try {
            java.net.URL location = getClass().getResource("/com/unina/foodlab/Boundary/fxml/gestioneSessioniPratiche.fxml");
            if (location == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Ricetta");
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
            System.err.println("[GestioneRicettaDettagliBoundary] Errore ritorno: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
