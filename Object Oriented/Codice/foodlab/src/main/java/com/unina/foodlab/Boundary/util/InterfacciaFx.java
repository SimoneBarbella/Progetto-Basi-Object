package com.unina.foodlab.Boundary.util;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

public final class InterfacciaFx {

    private InterfacciaFx() {
        // utility
    }

    public static void showError(String title, String header, String content) {
        show(Alert.AlertType.ERROR, title, header, content);
    }

    public static void showWarning(String title, String header, String content) {
        show(Alert.AlertType.WARNING, title, header, content);
    }

    public static void showInfo(String title, String header, String content) {
        show(Alert.AlertType.INFORMATION, title, header, content);
    }

    public static void show(Alert.AlertType type, String title, String header, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public static void setVisibleManaged(Node node, boolean visible) {
        if (node == null) return;
        node.setVisible(visible);
        node.setManaged(visible);
    }

    public static void closeWindowFromEvent(ActionEvent event) {
        if (event == null || event.getSource() == null) return;
        if (!(event.getSource() instanceof Node node)) return;
        closeWindow(node);
    }

    public static void closeWindow(Node nodeInScene) {
        if (nodeInScene == null || nodeInScene.getScene() == null) return;
        if (!(nodeInScene.getScene().getWindow() instanceof Stage stage)) return;
        stage.close();
    }
}
