package org.example;

import java.util.ArrayList;
import java.util.List;

public class Level {
    private double[] inputs;
    private double[] outputs;
    private double[] biases;

    private List<double[]> weights;

    public Level(int inputCount, int outputCount) {

        this.inputs = new double[inputCount];
        this.outputs = new double[outputCount];
        this.biases = new double[outputCount];

        this.weights = new ArrayList<>();
        for (int i = 0; i < inputCount; i++) {
            this.weights.add(new double[outputCount]);
        }

        Level.randomize(this);
    }

    public double[] getInputs() {
        return inputs;
    }

    public double[] getOutputs() {
        return outputs;
    }

    public double[] getBiases() {
        return biases;
    }

    public List<double[]> getWeights() { return weights; }

    private static void randomize(Level level) {
        for (int i = 0; i < level.inputs.length; i++) {
            for (int j = 0; j < level.outputs.length; j++) {
                level.weights.get(i)[j] = Math.random() * 2 - 1;
            }
        }
        for (int i = 0; i < level.biases.length; i++) {
            level.biases[i] = Math.random() * 2 - 1;
        }
    }

    static double[] feedForward(double[] givenInputs, Level level) {
        for (int i = 0; i < level.inputs.length; i++) {
            level.inputs[i] = givenInputs[i];
        }
        for (int i = 0; i < level.outputs.length; i++) {
            double sum = 0;
            for (int j = 0; j < level.inputs.length; j++) {
                sum += level.inputs[j]*level.weights.get(j)[i];
            }

            if(sum > level.biases[i]){
                level.outputs[i] = 1;
            }else {
                level.outputs[i] = 0;
            }
        }
        return level.outputs;
    }
}
