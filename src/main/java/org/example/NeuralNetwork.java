package org.example;

import java.util.ArrayList;
import java.util.List;

public class NeuralNetwork {
    private List<Level> levels;

    public NeuralNetwork() {
    }

    public NeuralNetwork(int[] neuronCounts) {
        levels = new ArrayList<>();
        for (int i = 0; i < neuronCounts.length - 1; i++) {
            this.levels.add(new Level(
                    neuronCounts[i], neuronCounts[i+1]
            ));
        }
    }

    public List<Level> getLevels() {
        return levels;
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

    static void mutate(NeuralNetwork network, double amount) {
        network.levels.forEach(level -> {
            for (int i = 0; i < level.getBiases().length; i++) {
                level.getBiases()[i] = Utils.lerp(
                        level.getBiases()[i],
                        Math.random()*2 -1,
                        amount
                );
            }
            for (int i = 0; i < level.getWeights().size(); i++) {
                for (int j = 0; j < level.getWeights().get(i).length; j++) {
                    level.getWeights().get(i)[j] = Utils.lerp(
                            level.getWeights().get(i)[j],
                            Math.random()*2 -1,
                            amount
                    );
                }
            }
        });
    }


}
