package com.example.demo.model;


public class MarkovChainCalculator {

    public static double[][] calculateMarkovChain(double[][] transitionMatrix, double[] initialProbabilities, double[] workload, int numSteps) {
        int numStates = initialProbabilities.length;
        double[][] results = new double[numSteps][numStates];
        double[] currentProbabilities = initialProbabilities.clone();

        for (int step = 0; step < numSteps; step++) {
            results[step] = currentProbabilities.clone();
            currentProbabilities = multiplyMatrixVector(transitionMatrix, currentProbabilities);

            // Учитываем вектор трудоемкости
            for (int i = 0; i < numStates; i++) {
                results[step][i] *= workload[i];
            }
        }
        return results;
    }

    private static double[] multiplyMatrixVector(double[][] matrix, double[] vector) {
        int length = vector.length;
        double[] result = new double[length];

        for (int i = 0; i < length; i++) {
            for (int j = 0; j < length; j++) {
                result[i] += matrix[i][j] * vector[j];
            }
        }
        return result;
    }
}

