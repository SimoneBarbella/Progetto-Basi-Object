package com.unina.foodlab.Boundary.util;

import javafx.event.ActionEvent;
import javafx.scene.Node;

import java.util.function.Consumer;

@Deprecated
public final class SceneNavigator {

    private SceneNavigator() {
        // utility
    }

    @Deprecated
    public static boolean switchScene(ActionEvent event, String dialogTitle, String fxmlResourcePath, String missingResourceMessage) {
        return NavigatoreScene.switchScene(event, dialogTitle, fxmlResourcePath, missingResourceMessage);
    }

    @Deprecated
    public static boolean switchScene(Node nodeInScene, String dialogTitle, String fxmlResourcePath, String missingResourceMessage) {
        return NavigatoreScene.switchScene(nodeInScene, dialogTitle, fxmlResourcePath, missingResourceMessage);
    }

    @Deprecated
    public static <T> boolean switchScene(
        ActionEvent event,
        String dialogTitle,
        String fxmlResourcePath,
        String missingResourceMessage,
        Consumer<T> controllerInitializer,
        Class<T> controllerClass
    ) {
        return NavigatoreScene.switchScene(event, dialogTitle, fxmlResourcePath, missingResourceMessage, controllerInitializer, controllerClass);
    }

    @Deprecated
    public static <T> boolean switchScene(
        Node nodeInScene,
        String dialogTitle,
        String fxmlResourcePath,
        String missingResourceMessage,
        Consumer<T> controllerInitializer,
        Class<T> controllerClass
    ) {
        return NavigatoreScene.switchScene(nodeInScene, dialogTitle, fxmlResourcePath, missingResourceMessage, controllerInitializer, controllerClass);
    }

    @Deprecated
    public static boolean openModal(String dialogTitle, String windowTitle, String fxmlResourcePath, String missingResourceMessage) {
        return NavigatoreScene.openModal(dialogTitle, windowTitle, fxmlResourcePath, missingResourceMessage);
    }

    @Deprecated
    public static <T> boolean openModal(
        String dialogTitle,
        String windowTitle,
        String fxmlResourcePath,
        String missingResourceMessage,
        Consumer<T> controllerInitializer,
        Class<T> controllerClass
    ) {
        return NavigatoreScene.openModal(dialogTitle, windowTitle, fxmlResourcePath, missingResourceMessage, controllerInitializer, controllerClass);
    }
}
