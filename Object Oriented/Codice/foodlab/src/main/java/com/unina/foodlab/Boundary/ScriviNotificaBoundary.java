package com.unina.foodlab.Boundary;

import com.unina.foodlab.Controller.GestoreCorsi;
import com.unina.foodlab.Controller.GestoreNotifiche;
import com.unina.foodlab.Boundary.util.InterfacciaFx;
import com.unina.foodlab.Boundary.util.NavigatoreScene;
import com.unina.foodlab.Entity.Chef;
import com.unina.foodlab.Entity.Corso;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

import java.util.List;

public class ScriviNotificaBoundary {

    private record NotificaInput(String messaggio, Integer idCorso) {}

    @FXML
    private Label chefLabel;

    @FXML
    private ComboBox<CorsoChoice> corsoCombo;

    @FXML
    private TextArea messaggioArea;

    private Chef chef;

    @FXML
    private void initialize() {
        // no-op
    }

    public void initData(Chef chef) {
        this.chef = chef;

        if (chefLabel != null && chef != null) {
            String nome = chef.getNome();
            chefLabel.setText(nome != null && !nome.isBlank() ? nome : chef.getEmail());
        }

        caricaScelteCorsi();
    }

    private void caricaScelteCorsi() {
        if (corsoCombo == null) return;

        corsoCombo.getItems().clear();
        corsoCombo.getItems().add(new CorsoChoice(null, "Tutti i corsi"));

        if (chef == null) {
            corsoCombo.getSelectionModel().selectFirst();
            return;
        }

        try {
            List<Corso> corsi = GestoreCorsi.getInstanza().getCorsiGestiti(chef);
            for (Corso c : corsi) {
				Integer id = c != null ? c.getIdCorso() : null;
                String nome = c != null ? c.getNome() : null;
                if (id != null) {
                    corsoCombo.getItems().add(new CorsoChoice(id, id + " - " + (nome != null ? nome : "")));
                }
            }
        } catch (Exception e) {
            System.err.println("[ScriviNotificaBoundary] Errore caricamento corsi: " + e.getMessage());
        }

        corsoCombo.getSelectionModel().selectFirst();
    }

    @FXML
    private void onInviaClick(ActionEvent event) {
        NotificaInput input = validateInput();
        if (input == null) {
            return;
        }

        try {
            GestoreNotifiche.getInstanza().inviaNotifica(chef, input.messaggio(), input.idCorso());
            showInfo("Notifica inviata.");
            goToNotifiche(event);
        } catch (RuntimeException ex) {
            showError(ex.getMessage());
        }
    }

    private NotificaInput validateInput() {
        if (chef == null) {
            showWarning("Chef non valido.");
            return null;
        }

        String messaggio = messaggioArea != null ? messaggioArea.getText() : null;
        if (messaggio == null || messaggio.isBlank()) {
            showWarning("Inserisci un messaggio.");
            return null;
        }

        Integer idCorso = null;
        if (corsoCombo != null) {
            CorsoChoice selected = corsoCombo.getSelectionModel().getSelectedItem();
            idCorso = selected != null ? selected.idCorso : null;
        }

        return new NotificaInput(messaggio, idCorso);
    }

    @FXML
    private void onBackClick(ActionEvent event) {
        goToNotifiche(event);
    }

    private void goToNotifiche(ActionEvent event) {
        NavigatoreScene.switchScene(
            event,
            "Notifiche",
            "/com/unina/foodlab/Boundary/fxml/notifiche.fxml",
            "Risorsa notifiche.fxml non trovata nel classpath.",
            (NotificheBoundary controller) -> controller.initData(chef),
            NotificheBoundary.class
        );
    }

    private void showWarning(String message) {
        InterfacciaFx.showWarning("Notifiche", "Attenzione", message);
    }

    private void showError(String message) {
        InterfacciaFx.showError("Notifiche", "Errore", message);
    }

    private void showInfo(String message) {
        InterfacciaFx.showInfo("Notifiche", null, message);
    }

    private static class CorsoChoice {
        private final Integer idCorso;
        private final String label;

        private CorsoChoice(Integer idCorso, String label) {
            this.idCorso = idCorso;
            this.label = label;
        }

        @Override
        public String toString() {
            return label;
        }
    }
}
