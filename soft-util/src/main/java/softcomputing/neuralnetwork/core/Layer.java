package softcomputing.neuralnetwork.core;

import java.util.Random;

public class Layer {
    Neuron[] neurons;
    
    public Layer(int neuronCount, int inputCount, Random rand, Neuron.ActivationType activationType) {
        neurons = new Neuron[neuronCount];
        for (int i = 0; i < neuronCount; i++) {
            neurons[i] = new Neuron(inputCount, rand, activationType);
        }
    }
    
    public double[] forward(double[] inputs) {
        double[] outputs = new double[neurons.length];
        for (int i = 0; i < neurons.length; i++) {
            outputs[i] = neurons[i].forward(inputs);
        }
        return outputs;
    }
}