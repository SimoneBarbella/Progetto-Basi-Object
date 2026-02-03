package com.unina.foodlab.Boundary;

import com.unina.foodlab.Entity.Chef;
import com.unina.foodlab.Entity.Corso;
import com.unina.foodlab.Entity.IngredienteQuantita;
import com.unina.foodlab.Entity.Ricetta;
import com.unina.foodlab.Entity.SessionePresenza;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

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
    private TextField nuovaRicettaNomeField;

    @FXML
    private TextArea nuovaRicettaDescrField;

    @FXML
    private TextField nuovaRicettaTempoField;

    @FXML
    private TextField nuovoIngredienteNomeField;

    @FXML
    private TextField nuovoIngredienteUnitaField;

    @FXML
    private TextField ingredienteQuantitaField;

    @FXML
    private javafx.scene.control.Button creaRicettaButton;

    @FXML
    private javafx.scene.control.Button aggiungiIngredienteButton;

    @FXML
    private Label ingredientiTitoloLabel;

    @FXML
    private ListView<IngredienteQuantita> ingredientiListView;

    @FXML
    private ListView<Ricetta> ricetteListView;

    @FXML
    private javafx.scene.control.Button dettaglioRicettaButton;

    @FXML
    private HBox ricetteActionsBox;

    private GestioneSessioniPraticheCoordinator coordinator;

    @FXML
    private void initialize() {
        coordinator = new GestioneSessioniPraticheCoordinator(
            corsoLabel,
            sessioniTable,
            dataCol,
            aderentiCol,
            ricetteCol,
            quantitaTotaleCol,
            sessioneSelezionataLabel,
            nuovaRicettaNomeField,
            nuovaRicettaDescrField,
            nuovaRicettaTempoField,
            nuovoIngredienteNomeField,
            nuovoIngredienteUnitaField,
            ingredienteQuantitaField,
            creaRicettaButton,
            aggiungiIngredienteButton,
            ingredientiTitoloLabel,
            ingredientiListView,
            ricetteListView,
            dettaglioRicettaButton,
            ricetteActionsBox
        );
        coordinator.initialize();
    }

    public void initData(Corso corso, Chef chef) {
        if (coordinator != null) {
            coordinator.initData(corso, chef);
        }
    }

    @FXML
    private void onCreateRicettaClick(ActionEvent event) {
        if (coordinator != null) {
            coordinator.onCreateRicettaClick(event);
        }
    }

    @FXML
    private void onAddIngredienteClick(ActionEvent event) {
        if (coordinator != null) {
            coordinator.onAddIngredienteClick(event);
        }
    }

    @FXML
    private void onBackClick(ActionEvent event) {
        if (coordinator != null) {
            coordinator.onBackClick(event);
        }
    }

    @FXML
    private void onDettaglioRicettaClick(ActionEvent event) {
        if (coordinator != null) {
            coordinator.onDettaglioRicettaClick(event);
        }
    }
}
