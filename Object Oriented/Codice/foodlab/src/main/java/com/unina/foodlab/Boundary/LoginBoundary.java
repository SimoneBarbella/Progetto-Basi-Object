package com.unina.foodlab.Boundary;

import com.unina.foodlab.Controller.GestoreAutenticazione;
import com.unina.foodlab.Boundary.util.InterfacciaFx;
import com.unina.foodlab.Boundary.util.NavigatoreScene;
import com.unina.foodlab.Boundary.util.UtilEccezioni;
import com.unina.foodlab.Entity.Chef;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;


public class LoginBoundary {

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label errorLabel;

    @FXML
    private void initialize() {
        System.out.println("[LoginBoundary] initialize");
        if (errorLabel != null) {
            errorLabel.setText("");
        }
    }

    @FXML
    private void onLoginClick(ActionEvent event) {
        System.out.println("[LoginBoundary] onLoginClick");
        String email = emailField != null ? emailField.getText() : null;
        String password = passwordField != null ? passwordField.getText() : null;

        if (errorLabel != null) {
            errorLabel.setTextFill(Color.web("#334155"));
            errorLabel.setText("Verifica credenziali...");
        }

        Chef chef = null;
        try {
            chef = GestoreAutenticazione.getInstance().loginChef(email, password);
            if (chef == null) {
                if (errorLabel != null) {
                    errorLabel.setTextFill(Color.web("#d32f2f"));
                    errorLabel.setText("Credenziali non valide");
                }

                InterfacciaFx.showError("Login", "Login fallito", "Email o password errate, oppure connessione al DB non disponibile.");
                return;
            }

            if (errorLabel != null) {
                errorLabel.setTextFill(Color.web("#166534"));
                errorLabel.setText("Login effettuato!");
            }

            InterfacciaFx.showInfo("Login", "Login effettuato", "Benvenuto, " + chef.getNome() + " " + chef.getCognome() + "!");
        } catch (RuntimeException ex) {
            System.err.println("[LoginBoundary] Errore runtime durante il login: " + ex.getMessage());
            ex.printStackTrace();

            String userHint = userHintForLoginException(ex);

            if (errorLabel != null) {
                errorLabel.setTextFill(Color.web("#d32f2f"));
                errorLabel.setText(userHint);
            }

            InterfacciaFx.showError("Login", "Errore di connessione", userHint);
        }

        if (chef == null) {
            return;
        }

        final Chef loggedChef = chef;

        NavigatoreScene.switchScene(
            event,
            "Home",
            "/com/unina/foodlab/Boundary/fxml/home.fxml",
            "Risorsa home.fxml non trovata nel classpath.",
            (HomeBoundary controller) -> controller.initData(loggedChef),
            HomeBoundary.class
        );
    }

    @FXML
    private void onAnnullaClick(ActionEvent event) {
        System.out.println("[LoginBoundary] onAnnullaClick");
        if (emailField != null) {
            emailField.clear();
        }
        if (passwordField != null) {
            passwordField.clear();
        }
        if (errorLabel != null) {
            errorLabel.setTextFill(Color.web("#334155"));
            errorLabel.setText("");
        }
    }

    private static String userHintForLoginException(RuntimeException ex) {
        String msg = ex != null ? ex.getMessage() : null;
        String rootMsg = UtilEccezioni.rootCauseMessageOrNull(ex);

        String userHint = "Errore durante il login (vedi console)";
        String combined = (rootMsg != null ? rootMsg : msg);
        if (combined == null) {
            return userHint;
        }

        String lower = combined.toLowerCase();
        if (lower.contains("autenticazione con password fallita")) {
            return "Password Postgres errata: aggiorna user/password in DatabaseConnection";
        }
        if (lower.contains("connessione db non disponibile")) {
            return "Connessione DB non disponibile: controlla url/user/password";
        }

        return userHint;
    }
}
