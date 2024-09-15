// package com.example.demo.ui;
package com.example.demo;

import com.example.demo.model.MarkovChainCalculator;

import com.example.demo.model.MarkovChainData;
import com.example.demo.model.MarkovChainGraph;
import com.example.demo.model.MarkovChainValidator;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

import java.io.File;
import java.io.IOException;

public class MarkovChainApp extends Application {

    private TextField[][] transitionMatrixFields;
    private TextField[] initialProbabilitiesFields;
    private TextField[] workloadFields;  // Поля для ввода трудоемкости
    private Spinner<Integer> stateCountSpinner;
    private VBox matrixBox;
    private HBox probBox;
    private HBox workloadBox;
    private int numStates = 3;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        Label stateCountLabel = new Label("Количество состояний:");
        stateCountSpinner = new Spinner<>(2, 10, numStates);
        stateCountSpinner.setEditable(true);
        stateCountSpinner.valueProperty().addListener((obs, oldValue, newValue) -> {
            numStates = newValue;
            updateMatrixAndFields();
        });

        Label transitionMatrixLabel = new Label("Матрица переходов:");
        matrixBox = new VBox(5);
        updateMatrixAndFields();

        Button calculateButton = new Button("Вычислить");
        calculateButton.setOnAction(e -> calculateAndValidate());

        Button saveButton = new Button("Сохранить");
        saveButton.setOnAction(e -> saveToFile(primaryStage));

        Button loadButton = new Button("Загрузить");
        loadButton.setOnAction(e -> loadFromFile(primaryStage));

        VBox layout = new VBox(10, stateCountLabel, stateCountSpinner, transitionMatrixLabel, matrixBox, calculateButton, saveButton, loadButton);
        layout.setPadding(new Insets(10));
        Scene scene = new Scene(layout, 400, 500);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Markov Chain Calculator");
        primaryStage.show();
    }
    private void updateMatrixAndFields() {
        matrixBox.getChildren().clear();
        transitionMatrixFields = new TextField[numStates][numStates];
        initialProbabilitiesFields = new TextField[numStates];
        workloadFields = new TextField[numStates];

        for (int i = 0; i < numStates; i++) {
            HBox row = new HBox(5);
            for (int j = 0; j < numStates; j++) {
                transitionMatrixFields[i][j] = new TextField();
                transitionMatrixFields[i][j].setPrefWidth(50);
                row.getChildren().add(transitionMatrixFields[i][j]);
            }
            matrixBox.getChildren().add(row);
        }

        Label initialProbabilitiesLabel = new Label("Начальные вероятности:");
        probBox = new HBox(5);
        for (int i = 0; i < numStates; i++) {
            initialProbabilitiesFields[i] = new TextField();
            initialProbabilitiesFields[i].setPrefWidth(50);
            probBox.getChildren().add(initialProbabilitiesFields[i]);
        }
        matrixBox.getChildren().addAll(initialProbabilitiesLabel, probBox);

        Label workloadLabel = new Label("Трудоемкость этапов:");
        workloadBox = new HBox(5);
        for (int i = 0; i < numStates; i++) {
            workloadFields[i] = new TextField();
            workloadFields[i].setPrefWidth(50);
            workloadBox.getChildren().add(workloadFields[i]);
        }
        matrixBox.getChildren().addAll(workloadLabel, workloadBox);
    }

    private void calculateAndValidate() {
        double[][] transitionMatrix = new double[numStates][numStates];
        double[] initialProbabilities = new double[numStates];
        double[] workload = new double[numStates];

        try {
            for (int i = 0; i < numStates; i++) {
                for (int j = 0; j < numStates; j++) {
                    transitionMatrix[i][j] = Double.parseDouble(transitionMatrixFields[i][j].getText());
                }
            }

            for (int i = 0; i < numStates; i++) {
                initialProbabilities[i] = Double.parseDouble(initialProbabilitiesFields[i].getText());
                workload[i] = Double.parseDouble(workloadFields[i].getText());
            }

            if (!MarkovChainValidator.validateTransitionMatrix(transitionMatrix)) {
                showAlert("Ошибка", "Сумма каждой строки матрицы должна равняться 1");
                return;
            }

            double[][] results = MarkovChainCalculator.calculateMarkovChain(transitionMatrix, initialProbabilities, workload, 10);
            MarkovChainGraph.plotGraph(results, 10,numStates); // Построение графика после расчёта

        } catch (NumberFormatException ex) {
            showAlert("Ошибка", "Пожалуйста, введите корректные числовые значения.");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
    // Сохранение в файл JSON
    private void saveToFile(Stage primaryStage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Сохранить матрицу");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JSON файлы", "*.json"));
        File file = fileChooser.showSaveDialog(primaryStage);

        if (file != null) {
            try {
                MarkovChainData data = new MarkovChainData(numStates, transitionMatrixFields, initialProbabilitiesFields, workloadFields);
                ObjectMapper mapper = new ObjectMapper();
                mapper.writeValue(file, data);
            } catch (IOException e) {
                showAlert("Ошибка", "Не удалось сохранить данные: " + e.getMessage());
            }
        }
    }

    // Загрузка из файла JSON
    private void loadFromFile(Stage primaryStage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Загрузить матрицу");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JSON файлы", "*.json"));
        File file = fileChooser.showOpenDialog(primaryStage);

        if (file != null) {
            try {
                ObjectMapper mapper = new ObjectMapper();
                MarkovChainData data = mapper.readValue(file, MarkovChainData.class);
                numStates = data.getNumStates();
                stateCountSpinner.getValueFactory().setValue(numStates);
                updateMatrixAndFields();

                for (int i = 0; i < numStates; i++) {
                    for (int j = 0; j < numStates; j++) {
                        transitionMatrixFields[i][j].setText(String.valueOf(data.getTransitionMatrix()[i][j]));
                    }
                    initialProbabilitiesFields[i].setText(String.valueOf(data.getInitialProbabilities()[i]));
                    workloadFields[i].setText(String.valueOf(data.getWorkload()[i]));
                }

            } catch (IOException e) {
                showAlert("Ошибка", "Не удалось загрузить данные: " + e.getMessage());
            }
        }
    }

}