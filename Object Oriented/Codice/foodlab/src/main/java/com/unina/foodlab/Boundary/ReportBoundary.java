package com.unina.foodlab.Boundary;

import com.unina.foodlab.Controller.GestoreReport;
import com.unina.foodlab.Entity.Chef;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Map;

public class ReportBoundary {

    @FXML
    private Label chefLabel;

    @FXML
    private PieChart sessioniPieChart;

    @FXML
    private BarChart<String, Number> ricetteBarChart;

    private Chef chef;

    public void initData(Chef chef) {
        this.chef = chef;
        if (chefLabel != null && chef != null) {
            chefLabel.setText(chef.getNome() != null ? chef.getNome() : chef.getEmail());
        }
        refreshCharts();
    }

    @FXML
    private void onAggiornaClick(ActionEvent event) {
        refreshCharts();
    }

    private void refreshCharts() {
        if (chef == null) {
            showWarning("Chef non valido.");
            return;
        }

        try {
            Map<String, Integer> sessioni = GestoreReport.getInstance().getSessioniStats(chef);
            double[] ricetteStats = GestoreReport.getInstance().getRicetteStats(chef);

            refreshPie(sessioni);
            refreshBar(ricetteStats);
        } catch (RuntimeException ex) {
            showError(ex.getMessage());
        }
    }

    private void refreshPie(Map<String, Integer> stats) {
        if (sessioniPieChart == null) return;

        int online = 0;
        int presenza = 0;
        if (stats != null) {
            online = stats.getOrDefault("ONLINE", 0);
            presenza = stats.getOrDefault("PRESENZA", 0);
        }

        sessioniPieChart.getData().setAll(
                new PieChart.Data("Online", online),
                new PieChart.Data("Presenza", presenza)
        );
        sessioniPieChart.setLegendVisible(true);
        sessioniPieChart.setLabelsVisible(true);
    }

    private void refreshBar(double[] stats) {
        if (ricetteBarChart == null) return;

        double min = (stats != null && stats.length > 0) ? stats[0] : 0;
        double max = (stats != null && stats.length > 1) ? stats[1] : 0;
        double media = (stats != null && stats.length > 2) ? stats[2] : 0;

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.getData().add(new XYChart.Data<>("Min", min));
        series.getData().add(new XYChart.Data<>("Media", media));
        series.getData().add(new XYChart.Data<>("Max", max));

        ricetteBarChart.getData().clear();
        ricetteBarChart.getData().add(series);
    }

    @FXML
    private void onBackClick(ActionEvent event) {
        try {
            java.net.URL location = getClass().getResource("/com/unina/foodlab/Boundary/fxml/home.fxml");
            if (location == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Report");
                alert.setHeaderText("Schermata non trovata");
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
            System.err.println("[ReportBoundary] Errore ritorno Home: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void showWarning(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Report");
        alert.setHeaderText("Attenzione");
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Report");
        alert.setHeaderText("Errore");
        alert.setContentText(message);
        alert.showAndWait();
    }
}
