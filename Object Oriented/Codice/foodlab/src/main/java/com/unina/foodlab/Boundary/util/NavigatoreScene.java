package com.unina.foodlab.Boundary.util;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.function.Consumer;

public final class NavigatoreScene {

    private NavigatoreScene() {
        // utility
    }

    public static boolean switchScene(ActionEvent event, String dialogTitle, String fxmlResourcePath, String missingResourceMessage) {
        return switchScene(event, dialogTitle, fxmlResourcePath, missingResourceMessage, null, null);
    }

    public static boolean switchScene(Node nodeInScene, String dialogTitle, String fxmlResourcePath, String missingResourceMessage) {
        return switchScene(nodeInScene, dialogTitle, fxmlResourcePath, missingResourceMessage, null, null);
    }

    public static <T> boolean switchScene(
        ActionEvent event,
        String dialogTitle,
        String fxmlResourcePath,
        String missingResourceMessage,
        Consumer<T> controllerInitializer,
        Class<T> controllerClass
    ) {
        if (event == null || event.getSource() == null) {
            InterfacciaFx.showError(dialogTitle, "Errore navigazione", "Evento non valido.");
            return false;
        }
        if (!(event.getSource() instanceof Node node)) {
            InterfacciaFx.showError(dialogTitle, "Errore navigazione", "Impossibile recuperare la finestra corrente.");
            return false;
        }

        return switchScene(node, dialogTitle, fxmlResourcePath, missingResourceMessage, controllerInitializer, controllerClass);
    }

    public static <T> boolean switchScene(
        Node nodeInScene,
        String dialogTitle,
        String fxmlResourcePath,
        String missingResourceMessage,
        Consumer<T> controllerInitializer,
        Class<T> controllerClass
    ) {
        if (nodeInScene == null || nodeInScene.getScene() == null || nodeInScene.getScene().getWindow() == null) {
            InterfacciaFx.showError(dialogTitle, "Errore navigazione", "Impossibile recuperare la finestra corrente.");
            return false;
        }

        URL location = NavigatoreScene.class.getResource(fxmlResourcePath);
        if (location == null) {
            InterfacciaFx.showError(dialogTitle, "Schermata non trovata", missingResourceMessage);
            return false;
        }

        try {
            FXMLLoader loader = new FXMLLoader(location);
            Parent root = loader.load();

            if (controllerInitializer != null && controllerClass != null) {
                Object controller = loader.getController();
                if (controller != null) {
                    controllerInitializer.accept(controllerClass.cast(controller));
                }
            }

            Stage stage = (Stage) nodeInScene.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.centerOnScreen();
            stage.show();
            return true;
        } catch (IOException e) {
            System.err.println("[NavigatoreScene] Errore caricamento " + fxmlResourcePath + ": " + e.getMessage());
            e.printStackTrace();
            InterfacciaFx.showError(dialogTitle, "Errore apertura schermata", "Impossibile caricare la schermata: " + e.getMessage());
            return false;
        } catch (ClassCastException e) {
            System.err.println("[NavigatoreScene] Controller non compatibile per " + fxmlResourcePath + ": " + e.getMessage());
            e.printStackTrace();
            InterfacciaFx.showError(dialogTitle, "Errore apertura schermata", "Controller non compatibile per la schermata richiesta.");
            return false;
        }
    }

    public static boolean openModal(String dialogTitle, String windowTitle, String fxmlResourcePath, String missingResourceMessage) {
        return openModal(dialogTitle, windowTitle, fxmlResourcePath, missingResourceMessage, null, null);
    }

    public static <T> boolean openModal(
        String dialogTitle,
        String windowTitle,
        String fxmlResourcePath,
        String missingResourceMessage,
        Consumer<T> controllerInitializer,
        Class<T> controllerClass
    ) {
        URL location = NavigatoreScene.class.getResource(fxmlResourcePath);
        if (location == null) {
            InterfacciaFx.showError(dialogTitle, "Schermata non trovata", missingResourceMessage);
            return false;
        }

        try {
            FXMLLoader loader = new FXMLLoader(location);
            Parent root = loader.load();

            if (controllerInitializer != null && controllerClass != null) {
                Object controller = loader.getController();
                if (controller != null) {
                    controllerInitializer.accept(controllerClass.cast(controller));
                }
            }

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle(windowTitle);
            stage.setScene(new Scene(root));
            stage.centerOnScreen();
            stage.showAndWait();
            return true;
        } catch (IOException e) {
            System.err.println("[NavigatoreScene] Errore caricamento " + fxmlResourcePath + ": " + e.getMessage());
            e.printStackTrace();
            InterfacciaFx.showError(dialogTitle, "Errore apertura schermata", "Impossibile caricare la schermata: " + e.getMessage());
            return false;
        } catch (ClassCastException e) {
            System.err.println("[NavigatoreScene] Controller non compatibile per " + fxmlResourcePath + ": " + e.getMessage());
            e.printStackTrace();
            InterfacciaFx.showError(dialogTitle, "Errore apertura schermata", "Controller non compatibile per la schermata richiesta.");
            return false;
        }
    }
}
