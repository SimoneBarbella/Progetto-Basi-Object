package com.unina.foodlab.Boundary;

import com.unina.foodlab.Controller.GestoreAutenticazione;
import com.unina.foodlab.Entity.Chef;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;

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

        if (chef == null) {
            return;
        }

        try {
            java.net.URL location = getClass().getResource("/com/unina/foodlab/Boundary/fxml/home.fxml");
            if (location == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Home");
                alert.setHeaderText("Errore apertura Home");
                alert.setContentText("Risorsa home.fxml non trovata nel classpath.");
                alert.showAndWait();
                return;
            }

            FXMLLoader loader = new FXMLLoader(location);
            Parent root = loader.load();

            HomeBoundary controller = loader.getController();
            controller.initData(chef);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.centerOnScreen();
            stage.show();
        } catch (IOException e) {
            System.err.println("[LoginBoundary] Errore caricamento home.fxml: " + e.getMessage());
            e.printStackTrace();

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Home");
            alert.setHeaderText("Errore apertura Home");
            alert.setContentText("Impossibile caricare la schermata Home: " + e.getMessage());
            alert.showAndWait();
        }
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
