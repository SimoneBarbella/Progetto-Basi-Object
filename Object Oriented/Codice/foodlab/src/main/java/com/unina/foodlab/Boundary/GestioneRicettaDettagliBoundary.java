package com.unina.foodlab.Boundary;

import com.unina.foodlab.Controller.GestoreRicette;
import com.unina.foodlab.Boundary.util.NavigatoreScene;
import com.unina.foodlab.Entity.Chef;
import com.unina.foodlab.Entity.Corso;
import com.unina.foodlab.Entity.IngredienteQuantita;
import com.unina.foodlab.Entity.Ricetta;
import com.unina.foodlab.Entity.SessionePresenza;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import java.util.List;

public class GestioneRicettaDettagliBoundary {

    @FXML
    private Label corsoLabel;

    @FXML
    private Label ricettaNomeLabel;

    @FXML
    private Label ricettaTempoLabel;

    @FXML
    private TextArea ricettaDescrArea;

    @FXML
    private Label ricetteTitoloLabel;

    @FXML
    private ListView<Ricetta> ricetteListView;

    @FXML
    private ListView<IngredienteQuantita> ingredientiListView;

    private Corso corso;
    private Chef chef;

    @FXML
    private void initialize() {
        if (ricetteListView != null) {
            ricetteListView.setCellFactory(list -> new javafx.scene.control.ListCell<>() {
                @Override
                protected void updateItem(Ricetta item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                    } else {
                        String tempo = item.getTempo() != null ? item.getTempo().toString() : "";
                        setText(item.getNome() + (tempo.isEmpty() ? "" : " (" + tempo + ")"));
                    }
                }
            });
            ricetteListView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> showRicetta(newVal));
        }
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
        // compatibilità: dettagli per una singola ricetta
        initDataSessione(null, ricetta, corso, chef);
    }

    public void initDataSessione(SessionePresenza sessione, Ricetta preselect, Corso corso, Chef chef) {
        this.corso = corso;
        this.chef = chef;

        setRicetteTitoloLabel(sessione, preselect);
        setCorsoLabel(corso);

        List<Ricetta> ricette;
        if (sessione != null && sessione.getIdSessione() != null) {
            ricette = GestoreRicette.getInstance().getRicettePerSessione(sessione.getIdSessione());
        } else if (preselect != null) {
            ricette = java.util.List.of(preselect);
        } else {
            ricette = java.util.List.of();
        }

        if (ricetteListView == null) {
            // se manca la lista, mostra comunque la ricetta preselezionata
            showRicetta(preselect);
            return;
        }

        ricetteListView.setItems(javafx.collections.FXCollections.observableArrayList(ricette));
        selectRicettaIfPresent(ricette, preselect);

        if (!ricetteListView.getItems().isEmpty() && ricetteListView.getSelectionModel().getSelectedItem() == null) {
            ricetteListView.getSelectionModel().selectFirst();
        }
    }

    private void showRicetta(Ricetta ricetta) {
        if (ricettaNomeLabel != null) {
            ricettaNomeLabel.setText("Nome: " + (ricetta != null ? ricetta.getNome() : ""));
        }
        if (ricettaTempoLabel != null) {
            String tempo = ricetta != null && ricetta.getTempo() != null ? ricetta.getTempo().toString() : "";
            ricettaTempoLabel.setText("Tempo: " + tempo);
        }
        if (ricettaDescrArea != null) {
            ricettaDescrArea.setText(ricetta != null && ricetta.getDescrizione() != null ? ricetta.getDescrizione() : "");
        }

        if (ingredientiListView != null) {
            loadIngredienti(ricetta);
        }
    }

    private void setRicetteTitoloLabel(SessionePresenza sessione, Ricetta preselect) {
        if (ricetteTitoloLabel == null) {
            return;
        }
        if (sessione != null && sessione.getIdSessione() != null) {
            ricetteTitoloLabel.setText("Ricette della sessione");
            return;
        }
        if (preselect != null) {
            ricetteTitoloLabel.setText("Ricetta");
            return;
        }
        ricetteTitoloLabel.setText("Ricette");
    }

    private void setCorsoLabel(Corso corso) {
        if (corsoLabel == null || corso == null) {
            return;
        }
        String nome = corso.getNome();
		Integer id = corso.getIdCorso();
        corsoLabel.setText(nome + (id != null ? " (ID " + id + ")" : ""));
    }

    private void selectRicettaIfPresent(List<Ricetta> ricette, Ricetta preselect) {
        if (preselect == null || ricetteListView == null) {
            return;
        }
        for (Ricetta r : ricette) {
            if (r != null && r.getIdRicetta() != null && r.getIdRicetta().equals(preselect.getIdRicetta())) {
                ricetteListView.getSelectionModel().select(r);
                break;
            }
        }
    }

    private void loadIngredienti(Ricetta ricetta) {
        if (ingredientiListView == null) {
            return;
        }
        if (ricetta == null || ricetta.getIdRicetta() == null) {
            ingredientiListView.setItems(javafx.collections.FXCollections.observableArrayList());
            return;
        }
        List<IngredienteQuantita> ingredienti = GestoreRicette.getInstance().getIngredientiPerRicetta(ricetta.getIdRicetta());
        ingredientiListView.setItems(javafx.collections.FXCollections.observableArrayList(ingredienti));
    }

    @FXML
    private void onBackClick(ActionEvent event) {
        final Corso selectedCorso = corso;
        final Chef loggedChef = chef;
        NavigatoreScene.switchScene(
            event,
            "Ricetta",
            "/com/unina/foodlab/Boundary/fxml/gestioneSessioniPratiche.fxml",
            "Risorsa gestioneSessioniPratiche.fxml non trovata nel classpath.",
            (GestioneSessioniPraticheBoundary controller) -> controller.initData(selectedCorso, loggedChef),
            GestioneSessioniPraticheBoundary.class
        );
    }
}
