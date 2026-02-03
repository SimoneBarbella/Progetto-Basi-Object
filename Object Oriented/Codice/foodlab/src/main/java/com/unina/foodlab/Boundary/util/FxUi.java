package com.unina.foodlab.Boundary.util;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.Alert;

@Deprecated
public final class FxUi {

    private FxUi() {
        // utility
    }

    @Deprecated
    public static void showError(String title, String header, String content) {
        InterfacciaFx.showError(title, header, content);
    }

    @Deprecated
    public static void showWarning(String title, String header, String content) {
        InterfacciaFx.showWarning(title, header, content);
    }

    @Deprecated
    public static void showInfo(String title, String header, String content) {
        InterfacciaFx.showInfo(title, header, content);
    }

    @Deprecated
    public static void show(Alert.AlertType type, String title, String header, String content) {
        InterfacciaFx.show(type, title, header, content);
    }

    @Deprecated
    public static void setVisibleManaged(Node node, boolean visible) {
        InterfacciaFx.setVisibleManaged(node, visible);
    }

    @Deprecated
    public static void closeWindowFromEvent(ActionEvent event) {
        InterfacciaFx.closeWindowFromEvent(event);
    }

    @Deprecated
    public static void closeWindow(Node nodeInScene) {
        InterfacciaFx.closeWindow(nodeInScene);
    }
}
