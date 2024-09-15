package com.example.demo.model;

import javafx.scene.control.TextField;

public class MarkovChainData {

    private int numStates;
    private double[][] transitionMatrix;
    private double[] initialProbabilities;
    private double[] workload;

    public MarkovChainData() {
    }

    public MarkovChainData(int numStates, TextField[][] transitionMatrixFields, TextField[] initialProbabilitiesFields, TextField[] workloadFields) {
        this.numStates = numStates;
        this.transitionMatrix = new double[numStates][numStates];
        this.initialProbabilities = new double[numStates];
        this.workload = new double[numStates];

        for (int i = 0; i < numStates; i++) {
            for (int j = 0; j < numStates; j++) {
                this.transitionMatrix[i][j] = Double.parseDouble(transitionMatrixFields[i][j].getText());
            }
            this.initialProbabilities[i] = Double.parseDouble(initialProbabilitiesFields[i].getText());
            this.workload[i] = Double.parseDouble(workloadFields[i].getText());
        }
    }

    public int getNumStates() {
        return numStates;
    }

    public double[][] getTransitionMatrix() {
        return transitionMatrix;
    }

    public double[] getInitialProbabilities() {
        return initialProbabilities;
    }

    public double[] getWorkload() {
        return workload;
    }
}

