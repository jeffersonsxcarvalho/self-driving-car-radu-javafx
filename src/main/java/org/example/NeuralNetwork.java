package org.example;

import java.util.ArrayList;
import java.util.List;

public class NeuralNetwork {
    private List<Level> levels;

    public NeuralNetwork(int[] neuronCounts) {
        levels = new ArrayList<>();
        for (int i = 0; i < neuronCounts.length - 1; i++) {
            this.levels.add(new Level(
                    neuronCounts[i], neuronCounts[i+1]
            ));
        }
    }

    static double[] feedForward(double[] givenInputs, NeuralNetwork network) {
        double[] outputs = Level.feedForward(
                givenInputs,
                network.levels.get(0)
        );
        for (int i = 1; i < network.levels.size(); i++) {
            outputs = Level.feedForward(
                    outputs,
                    network.levels.get(i)
            );
        }
        return outputs;
    }


}
