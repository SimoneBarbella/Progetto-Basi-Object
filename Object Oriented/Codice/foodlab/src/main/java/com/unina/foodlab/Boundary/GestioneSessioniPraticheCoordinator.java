package com.unina.foodlab.Boundary;


import com.unina.foodlab.Boundary.util.InterfacciaFx;
import com.unina.foodlab.Boundary.util.NavigatoreScene;
import com.unina.foodlab.Entity.Chef;
import com.unina.foodlab.Entity.Corso;
import com.unina.foodlab.Entity.IngredienteQuantita;
import com.unina.foodlab.Entity.Ricetta;
import com.unina.foodlab.Entity.SessionePresenza;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

final class GestioneSessioniPraticheCoordinator {

    private final Label corsoLabel;
    private final TableView<SessionePresenza> sessioniTable;
    private final ListView<Ricetta> ricetteListView;

    private Corso corso;
    private Chef chef;
    private SessionePresenza sessioneSelezionata;

    private final SessioniPraticheTableController tableController;
    private final RicetteIngredientiSectionController sectionController;

    GestioneSessioniPraticheCoordinator(
        Label corsoLabel,
        TableView<SessionePresenza> sessioniTable,
        TableColumn<SessionePresenza, String> dataCol,
        TableColumn<SessionePresenza, Integer> aderentiCol,
        TableColumn<SessionePresenza, String> ricetteCol,
        TableColumn<SessionePresenza, String> quantitaTotaleCol,
        Label sessioneSelezionataLabel,
        TextField nuovaRicettaNomeField,
        TextArea nuovaRicettaDescrField,
        TextField nuovaRicettaTempoField,
        TextField nuovoIngredienteNomeField,
        TextField nuovoIngredienteUnitaField,
        TextField ingredienteQuantitaField,
        Button creaRicettaButton,
        Button aggiungiIngredienteButton,
        Label ingredientiTitoloLabel,
        ListView<IngredienteQuantita> ingredientiListView,
        ListView<Ricetta> ricetteListView,
        Button dettaglioRicettaButton,
        HBox ricetteActionsBox
    ) {
        this.corsoLabel = corsoLabel;
        this.sessioniTable = sessioniTable;
        this.ricetteListView = ricetteListView;

        this.tableController = new SessioniPraticheTableController(
            sessioniTable,
            dataCol,
            aderentiCol,
            ricetteCol,
            quantitaTotaleCol,
            this::openListaSpesa
        );

        this.sectionController = new RicetteIngredientiSectionController(
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
            ricetteActionsBox,
            () -> sessioneSelezionata,
            this::loadSessioniPratiche,
            this::onDettaglioRicettaClick
        );
    }

    void initialize() {
        tableController.initialize(this::onSessionSelected);
        sectionController.initialize();
    }

    void initData(Corso corso, Chef chef) {
        this.corso = corso;
        this.chef = chef;
        if (corsoLabel != null && corso != null) {
            String nome = corso.getNome();
			Integer id = corso.getIdCorso();
            corsoLabel.setText(nome + (id != null ? " (ID " + id + ")" : ""));
        }
        loadSessioniPratiche();
    }

    void onCreateRicettaClick(ActionEvent event) {
        sectionController.onCreateRicettaClick(event, requireIdSessioneSelezionataOrWarn());
    }

    void onAddIngredienteClick(ActionEvent event) {
        sectionController.onAddIngredienteClick(event, requireIdSessioneSelezionataOrWarn());
    }

    void onBackClick(ActionEvent event) {
        final Corso selectedCorso = corso;
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

    void onDettaglioRicettaClick(ActionEvent event) {
        if (requireIdSessioneSelezionataOrWarn() == null) return;
        Ricetta selected = ricetteListView != null ? ricetteListView.getSelectionModel().getSelectedItem() : null;

        final SessionePresenza selectedSessione = sessioneSelezionata;
        final Ricetta selectedRicetta = selected;
        final Corso selectedCorso = corso;
        final Chef loggedChef = chef;
        NavigatoreScene.switchScene(
            event,
            "Ricetta",
            "/com/unina/foodlab/Boundary/fxml/gestioneRicettaDettagli.fxml",
            "Risorsa gestioneRicettaDettagli.fxml non trovata nel classpath.",
            (GestioneRicettaDettagliBoundary controller) -> controller.initDataSessione(selectedSessione, selectedRicetta, selectedCorso, loggedChef),
            GestioneRicettaDettagliBoundary.class
        );
    }

    private void loadSessioniPratiche() {
        sessioneSelezionata = tableController.loadAndApply(corso, sessioneSelezionata);
        sectionController.refreshMasters();
        refreshSectionForSelectedSession();
    }
    private Integer requireIdSessioneSelezionataOrWarn() {
        Integer idSessione = sessioneSelezionata != null ? sessioneSelezionata.getIdSessione() : null;
        if (idSessione == null) {
            showRicettaWarning("Seleziona una sessione valida.");
        }
        return idSessione;
    }

    private void onSessionSelected(SessionePresenza selected) {
        sessioneSelezionata = selected;
        refreshSectionForSelectedSession();
    }

    private void refreshSectionForSelectedSession() {
        Integer numeroCorso = null;
        if (sessioniTable != null && sessioniTable.getItems() != null && sessioneSelezionata != null) {
            int idx = sessioniTable.getItems().indexOf(sessioneSelezionata);
            numeroCorso = idx >= 0 ? idx + 1 : null;
        }
        sectionController.refreshForSelection(sessioneSelezionata, numeroCorso);
    }

    private void openListaSpesa(SessionePresenza sessione) {
        if (sessione == null || sessione.getIdSessione() == null) {
            showRicettaWarning("Seleziona una sessione valida.");
            return;
        }
        final SessionePresenza selectedSessione = sessione;
        final Corso selectedCorso = corso;
        final Chef loggedChef = chef;
        NavigatoreScene.switchScene(
            sessioniTable,
            "Lista della spesa",
            "/com/unina/foodlab/Boundary/fxml/listaSpesa.fxml",
            "Risorsa listaSpesa.fxml non trovata nel classpath.",
            (ListaSpesaBoundary controller) -> controller.initData(selectedSessione, selectedCorso, loggedChef),
            ListaSpesaBoundary.class
        );
    }

    private void showRicettaWarning(String message) {
        InterfacciaFx.showWarning("Ricette", "Attenzione", message);
    }
}
