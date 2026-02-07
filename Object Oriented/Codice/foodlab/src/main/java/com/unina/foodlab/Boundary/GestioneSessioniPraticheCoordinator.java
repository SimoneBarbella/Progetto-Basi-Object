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

    private final SessioniTableHandler tableController;
    private final RicetteIngredientiSectionUI sectionController;

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

        this.tableController = new SessioniTableHandler(
            sessioniTable,
            dataCol,
            aderentiCol,
            ricetteCol,
            quantitaTotaleCol,
            this::apriListaSpesa
        );

        this.sectionController = new RicetteIngredientiSectionUI(
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
            this::caricaSessioniPratiche,
            this::onDettaglioRicettaClick
        );
    }

    void initialize() {
        tableController.initialize(this::onSessioneSelezionata);
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
        caricaSessioniPratiche();
    }

    void onCreaRicettaClick(ActionEvent event) {
        sectionController.onCreaRicettaClick(event, richiediIdSessioneSelezionataOAvvisa());
    }

    void onAggiungiIngredienteClick(ActionEvent event) {
        sectionController.onAggiungiIngredienteClick(event, richiediIdSessioneSelezionataOAvvisa());
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
        if (richiediIdSessioneSelezionataOAvvisa() == null) return;
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

    private void caricaSessioniPratiche() {
        sessioneSelezionata = tableController.caricaEApplica(corso, sessioneSelezionata);
        sectionController.aggiornaMasters();
        aggiornaSezionePerSessioneSelezionata();
    }
    private Integer richiediIdSessioneSelezionataOAvvisa() {
        Integer idSessione = sessioneSelezionata != null ? sessioneSelezionata.getIdSessione() : null;
        if (idSessione == null) {
            InterfacciaFx.showWarning("Ricette", "Attenzione", "Seleziona una sessione valida.");
        }
        return idSessione;
    }

    private void onSessioneSelezionata(SessionePresenza selected) {
        sessioneSelezionata = selected;
        aggiornaSezionePerSessioneSelezionata();
    }

    private void aggiornaSezionePerSessioneSelezionata() {
        Integer numeroCorso = null;
        if (sessioniTable != null && sessioniTable.getItems() != null && sessioneSelezionata != null) {
            int idx = sessioniTable.getItems().indexOf(sessioneSelezionata);
            numeroCorso = idx >= 0 ? idx + 1 : null;
        }
        sectionController.aggiornaPerSelezione(sessioneSelezionata, numeroCorso);
    }

    private void apriListaSpesa(SessionePresenza sessione) {
        if (sessione == null || sessione.getIdSessione() == null) {
            InterfacciaFx.showWarning("Ricette", "Attenzione", "Seleziona una sessione valida.");
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

    
}
