package softcomputing.neuralnetwork.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class NeuralNetwork {
    List<Layer> layers = new ArrayList<>();
    Random rand;

    public NeuralNetwork(int... layerSizes) {
        this(new Random(), layerSizes);
    }

    public NeuralNetwork(Random rand, int... layerSizes) {
        this.rand = rand;

        for (int i = 1; i < layerSizes.length - 1; i++) {
            layers.add(new Layer(layerSizes[i], layerSizes[i - 1], rand, Neuron.ActivationType.RELU));
        }

        if (layerSizes.length > 1) {
            layers.add(new Layer(
                    layerSizes[layerSizes.length - 1],
                    layerSizes[layerSizes.length - 2],
                    rand,
                    Neuron.ActivationType.TANH));
        }
    }

    public NeuralNetwork(Random rand, Neuron.ActivationType outputActivation, int... layerSizes) {
        this.rand = rand;

        for (int i = 1; i < layerSizes.length - 1; i++) {
            layers.add(new Layer(layerSizes[i], layerSizes[i - 1], rand, Neuron.ActivationType.RELU));
        }

        if (layerSizes.length > 1) {
            layers.add(new Layer(
                    layerSizes[layerSizes.length - 1],
                    layerSizes[layerSizes.length - 2],
                    rand,
                    outputActivation));
        }
    }

    public NeuralNetwork(NeuralNetwork other) {
        this.rand = new Random();
        for (Layer layer : other.layers) {
            Neuron.ActivationType type = layer.neurons[0].activationType;
            Layer newLayer = new Layer(
                    layer.neurons.length,
                    layer.neurons[0].weights.length,
                    rand,
                    type);
            layers.add(newLayer);
        }
        this.setWeights(other.flatten());
    }

    public double[] forward(double[] input) {
        double[] output = input;
        for (Layer layer : layers) {
            output = layer.forward(output);
        }
        return output;
    }

    public double[] flatten() {
        List<Double> genes = new ArrayList<>();
        for (Layer layer : layers) {
            for (Neuron neuron : layer.neurons) {
                for (double w : neuron.weights) {
                    genes.add(w);
                }
                genes.add(neuron.bias);
            }
        }
        return genes.stream().mapToDouble(Double::doubleValue).toArray();
    }

    public void setWeights(double[] genome) {
        int index = 0;
        for (Layer layer : layers) {
            for (Neuron neuron : layer.neurons) {
                for (int i = 0; i < neuron.weights.length; i++) {
                    neuron.weights[i] = genome[index++];
                }
                neuron.bias = genome[index++];
            }
        }
    }

    public int getParameterCount() {
        return flatten().length;
    }
}