package com.unina.foodlab.Boundary;

import com.unina.foodlab.Controller.GestoreSessioni;
import com.unina.foodlab.Boundary.util.FormatiDataOra;
import com.unina.foodlab.Boundary.util.NavigatoreScene;
import com.unina.foodlab.Entity.Chef;
import com.unina.foodlab.Entity.Corso;
import com.unina.foodlab.Entity.IngredienteQuantita;
import com.unina.foodlab.Entity.SessionePresenza;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
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
        this.corso = corso;
        this.chef = chef;

        if (corsoLabel != null && corso != null) {
            String nome = corso.getNome();
			Integer id = corso.getIdCorso();
            corsoLabel.setText(nome + (id != null ? " (ID " + id + ")" : ""));
        }

        if (sessioneLabel != null && sessione != null && sessione.getOraInizio() != null) {
            String data = sessione.getOraInizio().format(FormatiDataOra.YYYY_MM_DD_HH_MM);
            sessioneLabel.setText("Sessione: " + data);
        }

        if (sessione != null && sessione.getIdSessione() != null) {
            List<IngredienteQuantita> lista = GestoreSessioni.getInstanza().getListaSpesaBySessioneId(sessione.getIdSessione());
            if (spesaListView != null) {
                spesaListView.setItems(javafx.collections.FXCollections.observableArrayList(lista));
            }
            if (totaleLabel != null) {
                java.math.BigDecimal tot = GestoreSessioni.getInstanza().getQuantitaTotaleBySessioneId(sessione.getIdSessione());
                String totStr = tot != null ? tot.stripTrailingZeros().toPlainString() : "0";
                totaleLabel.setText("Totale: " + totStr);
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
        final Corso selectedCorso = corso;
        final Chef loggedChef = chef;
        NavigatoreScene.switchScene(
            event,
            "Lista della spesa",
            "/com/unina/foodlab/Boundary/fxml/gestioneSessioniPratiche.fxml",
            "Risorsa gestioneSessioniPratiche.fxml non trovata nel classpath.",
            (GestioneSessioniPraticheBoundary controller) -> controller.initData(selectedCorso, loggedChef),
            GestioneSessioniPraticheBoundary.class
        );
    }
}
