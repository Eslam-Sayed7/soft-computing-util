// package com.example.softcomputing.neuralnetwork.core;
// public class TestNeuralNetwork {
    
//     public static void main(String[] args) {
//         System.out.println("=== Neural Network Test Suite ===\n");
        
//         testBasicForward();
//         testFlattenAndSetWeights();
//         testCopyConstructor();
//         testCarControlScenario();
//         testActivationFunctions();
        
//         System.out.println("\n=== All Tests Completed Successfully ===");
//     }
    
//     // Test 1: Basic forward pass
//     static void testBasicForward() {
//         System.out.println("Test 1: Basic Forward Pass");
//         System.out.println("---------------------------");
        
//         // Create a simple network: 3 inputs -> 4 hidden -> 2 outputs
//         NeuralNetwork nn = new NeuralNetwork(3, 4, 2);
        
//         // Test input (e.g., 3 sensor readings)
//         double[] input = {0.5, -0.3, 0.8};
        
//         // Forward pass
//         double[] output = nn.forward(input);
        
//         System.out.println("Input: [" + input[0] + ", " + input[1] + ", " + input[2] + "]");
//         System.out.println("Output: [" + output[0] + ", " + output[1] + "]");
//         System.out.println("Output in valid range [-1,1]: " + 
//                            (output[0] >= -1 && output[0] <= 1 && output[1] >= -1 && output[1] <= 1));
//         System.out.println("PASS\n");
//     }
    
//     // Test 2: Flatten and SetWeights (critical for GA)
//     static void testFlattenAndSetWeights() {
//         System.out.println("Test 2: Flatten and SetWeights");
//         System.out.println("-------------------------------");
        
//         NeuralNetwork nn1 = new NeuralNetwork(2, 3, 1);
//         double[] input = {0.5, 0.5};
        
//         // Get output before modification
//         double[] output1 = nn1.forward(input);
        
//         // Extract chromosome
//         double[] genome = nn1.flatten();
//         System.out.println("Genome length: " + genome.length + " parameters");
//         System.out.println("Expected: " + nn1.getParameterCount() + " parameters");
        
//         // Modify genome (simulate mutation)
//         for (int i = 0; i < genome.length; i++) {
//             genome[i] = genome[i] * 0.9;
//         }
        
//         // Load modified genome back
//         nn1.setWeights(genome);
//         double[] output2 = nn1.forward(input);
        
//         System.out.println("Output before: " + output1[0]);
//         System.out.println("Output after: " + output2[0]);
//         System.out.println("Outputs changed: " + (output1[0] != output2[0]));
//         System.out.println("PASS\n");
//     }
    
//     // Test 3: Copy constructor
//     static void testCopyConstructor() {
//         System.out.println("Test 3: Copy Constructor");
//         System.out.println("------------------------");
        
//         NeuralNetwork original = new NeuralNetwork(3, 5, 2);
//         double[] input = {0.2, 0.4, 0.6};
        
//         // Get output from original
//         double[] output1 = original.forward(input);
        
//         // Create copy
//         NeuralNetwork copy = new NeuralNetwork(original);
//         double[] output2 = copy.forward(input);
        
//         System.out.println("Original output: [" + output1[0] + ", " + output1[1] + "]");
//         System.out.println("Copy output: [" + output2[0] + ", " + output2[1] + "]");
        
//         boolean match = (Math.abs(output1[0] - output2[0]) < 0.0001 && 
//                         Math.abs(output1[1] - output2[1]) < 0.0001);
//         System.out.println("Outputs match: " + match);
        
//         // Modify copy
//         double[] genome = copy.flatten();
//         genome[0] = genome[0] + 1.0;
//         copy.setWeights(genome);
        
//         double[] output3 = copy.forward(input);
//         double[] output4 = original.forward(input);
        
//         boolean originalUnchanged = (Math.abs(output1[0] - output4[0]) < 0.0001);
//         System.out.println("Original unchanged after copy modification: " + originalUnchanged);
//         System.out.println("PASS\n");
//     }
    
//     // Test 4: Car control scenario
//     static void testCarControlScenario() {
//         System.out.println("Test 4: Car Control Scenario");
//         System.out.println("-----------------------------");
        
//         // 5 distance sensors -> 6 hidden neurons -> 2 outputs (steering, throttle)
//         NeuralNetwork carBrain = new NeuralNetwork(5, 6, 2);
        
//         System.out.println("Network structure: 5 sensors -> 6 hidden -> 2 outputs");
//         System.out.println("Total parameters: " + carBrain.getParameterCount());
        
//         // Simulate sensor readings (distances to walls/obstacles)
//         double[] sensors = {0.8, 0.6, 0.9, 0.3, 0.7};
        
//         double[] control = carBrain.forward(sensors);
        
//         System.out.println("\nSensor readings: [0.8, 0.6, 0.9, 0.3, 0.7]");
//         System.out.println("Steering: " + String.format("%.3f", control[0]) + " (left -1, right +1)");
//         System.out.println("Throttle: " + String.format("%.3f", control[1]) + " (reverse -1, forward +1)");
//         System.out.println("PASS\n");
//     }
    
//     // Test 5: Different activation functions
//     static void testActivationFunctions() {
//         System.out.println("Test 5: Activation Function Comparison");
//         System.out.println("---------------------------------------");
        
//         double[] input = {0.5, 0.5};
        
//         // Test with TANH output (default)
//         NeuralNetwork nnTanh = new NeuralNetwork(2, 3, 2);
//         double[] outTanh = nnTanh.forward(input);
        
//         // Test with SIGMOID output
//         NeuralNetwork nnSigmoid = new NeuralNetwork(
//             new java.util.Random(), 
//             Neuron.ActivationType.SIGMOID, 
//             2, 3, 2
//         );
//         double[] outSigmoid = nnSigmoid.forward(input);
        
//         System.out.println("TANH output: [" + outTanh[0] + ", " + outTanh[1] + "]");
//         System.out.println("SIGMOID output: [" + outSigmoid[0] + ", " + outSigmoid[1] + "]");
//         System.out.println("TANH in range [-1,1]: " + (outTanh[0] >= -1 && outTanh[0] <= 1));
//         System.out.println("SIGMOID in range [0,1]: " + (outSigmoid[0] >= 0 && outSigmoid[0] <= 1));
//         System.out.println("PASS\n");
//     }
// }