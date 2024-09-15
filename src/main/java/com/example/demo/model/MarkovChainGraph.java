package com.example.demo.model;

import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;

public class MarkovChainGraph {

    // Метод для построения графика
    public static void plotGraph(double[][] results, int numSteps,int numStates) {
        Stage stage = new Stage();
        stage.setTitle("Результаты цепи Маркова");

        final NumberAxis xAxis = new NumberAxis();
        final NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Шаги");

        final LineChart<Number, Number> lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.setTitle("Изменение вероятностей");

        for (int i = 0; i < numStates; i++) {
            XYChart.Series<Number, Number> series = new XYChart.Series<>();
            series.setName("Состояние " + (i + 1));

            for (int step = 0; step < numSteps; step++) {
                series.getData().add(new XYChart.Data<>(step, results[step][i]));
            }

            lineChart.getData().add(series);
        }

        Scene scene = new Scene(lineChart, 800, 600);
        stage.setScene(scene);
        stage.show();
    }
}
