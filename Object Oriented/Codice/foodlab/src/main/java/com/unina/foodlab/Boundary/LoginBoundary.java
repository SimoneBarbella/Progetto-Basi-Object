package com.unina.foodlab.Boundary;

import com.unina.foodlab.Control.GestoreAutenticazione;
import com.unina.foodlab.Entity.Chef;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
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

        try {
            Chef chef = GestoreAutenticazione.getInstance().loginChef(email, password);
            if (chef == null) {
                if (errorLabel != null) {
                    errorLabel.setTextFill(Color.web("#d32f2f"));
                    errorLabel.setText("Credenziali non valide");
                }

                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Login");
                alert.setHeaderText("Login fallito");
                alert.setContentText("Email o password errate, oppure connessione al DB non disponibile.");
                alert.showAndWait();
                return;
            }

            if (errorLabel != null) {
                errorLabel.setTextFill(Color.web("#166534"));
                errorLabel.setText("Login effettuato!");
            }

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Login");
            alert.setHeaderText("Login effettuato");
            alert.setContentText("Benvenuto, " + chef.getNome() + " " + chef.getCognome() + "!");
            alert.showAndWait();
        } catch (RuntimeException ex) {
            System.err.println("[LoginBoundary] Errore runtime durante il login: " + ex.getMessage());
            ex.printStackTrace();

            String msg = ex.getMessage();
            Throwable cause = ex;
            while (cause.getCause() != null && cause.getCause() != cause) {
                cause = cause.getCause();
            }
            String rootMsg = cause.getMessage();

            String userHint = "Errore durante il login (vedi console)";
            String combined = (rootMsg != null ? rootMsg : msg);
            if (combined != null && combined.toLowerCase().contains("autenticazione con password fallita")) {
                userHint = "Password Postgres errata: aggiorna user/password in DatabaseConnection";
            } else if (combined != null && combined.toLowerCase().contains("connessione db non disponibile")) {
                userHint = "Connessione DB non disponibile: controlla url/user/password";
            }

            if (errorLabel != null) {
                errorLabel.setTextFill(Color.web("#d32f2f"));
                errorLabel.setText(userHint);
            }

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Login");
            alert.setHeaderText("Errore di connessione");
            alert.setContentText(userHint);
            alert.showAndWait();
        }

        // TODO: qui puoi cambiare scena (Scene/Stage) verso la schermata successiva.
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
}
