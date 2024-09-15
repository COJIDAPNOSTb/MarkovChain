package com.example.demo.model;

public class MarkovChainValidator {

    public static boolean validateTransitionMatrix(double[][] matrix) {
        for (double[] row : matrix) {
            double sum = 0;
            for (double value : row) {
                sum += value;
            }
            if (Math.abs(sum - 1.0) > 1e-9) {
                return false;
            }
        }
        return true;
    }
}
