package com.example.softcomputing.fuzzy.inference;

public interface InferenceEngineInterface {
    java.util.Map<String, java.util.Map<String, Double>> infer(
            java.util.Map<String, java.util.Map<String, Double>> fuzzyInputs);
}