package com.unina.foodlab;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainFxApp extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(MainFxApp.class.getResource("/com/unina/foodlab/Boundary/LoginView.fxml"));
        Scene scene = new Scene(loader.load(), 820, 520);
        stage.setTitle("UninaFoodLab - Login");
        stage.setScene(scene);
        stage.setMinWidth(720);
        stage.setMinHeight(480);
        stage.setResizable(true);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
