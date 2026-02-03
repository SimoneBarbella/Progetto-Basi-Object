package com.unina.foodlab.Boundary;

import com.unina.foodlab.Controller.GestoreReport;
import com.unina.foodlab.Boundary.util.InterfacciaFx;
import com.unina.foodlab.Boundary.util.NavigatoreScene;
import com.unina.foodlab.Entity.Chef;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
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
        final Chef loggedChef = chef;
        NavigatoreScene.switchScene(
            event,
            "Report",
            "/com/unina/foodlab/Boundary/fxml/home.fxml",
            "Risorsa home.fxml non trovata nel classpath.",
            (HomeBoundary controller) -> controller.initData(loggedChef),
            HomeBoundary.class
        );
    }

    private void showWarning(String message) {
        InterfacciaFx.showWarning("Report", "Attenzione", message);
    }

    private void showError(String message) {
        InterfacciaFx.showError("Report", "Errore", message);
    }
}
