/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package my.NNPais;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

/**
 * class that emulates decision making
 * @author Drevlen
 * @param neuron main component
 * @param size number of neurons in network
 * @param inSize number of input values 
 * @param learnFactor for training how much weights are changing 
 */
public class NNetwork implements Serializable{
    private ArrayList<Neuron> neuron;
    private int size;
    private int inSize;
    private double learnFactor;
    /**
     * Constructing new network
     * @param size number of neurons in network
     * @param inSize number of input values 
     * @param factor for training how much weights are changing 
     */
    public void init(int size, int inSize, double factor) {
        this.size = size;
        this.inSize = inSize;
        learnFactor = factor;
        Random rand = new Random();
        neuron = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            neuron.add(new Neuron());
        }
        /*
         *  Creating input neurons
         */
        for (int i = 0; i < inSize; i++) {
            Neuron nerv = neuron.get(i);
            ArrayList<Neuron> outputs = new ArrayList<>(1);
            if (i == 0) {
                outputs.add(neuron.get(inSize));
            } else {
                outputs.add(neuron.get(inSize + inSize % i));
            } 
            nerv.set(i, null, outputs, null);
        }
        /*
         *  Creating hiden layers
         */
        for (int i = inSize; i < size - inSize; i++) {
            Neuron nerv = neuron.get(i);
            ArrayList<Neuron> outputs = new ArrayList<>(2);
            ArrayList<Neuron> inputs = new ArrayList<>(2);
            ArrayList<Double> initWeight = new ArrayList<>(2);
            if (i % inSize == 0) {
                inputs.add(neuron.get(i - inSize));
                outputs.add(neuron.get(i + inSize));
                outputs.add(neuron.get(i + 1));
                initWeight.add(rand.nextDouble() * 2);
            } else if (i % inSize  == inSize - 1) {
                inputs.add(neuron.get(i - inSize));
                inputs.add(neuron.get(i - 1));
                outputs.add(neuron.get(i + inSize));
                initWeight.add(rand.nextDouble() * 2);
                initWeight.add(rand.nextDouble() * 2);
            } else {
                inputs.add(neuron.get(i - inSize));
                inputs.add(neuron.get(i - 1));
                outputs.add(neuron.get(i + inSize));
                outputs.add(neuron.get(i + 1));
                initWeight.add(rand.nextDouble() * 2);
                initWeight.add(rand.nextDouble() * 2);               
            }
            nerv.set(i, inputs, outputs, initWeight);
        }
        
        for (int i = size - inSize; i < size; i++) {
            Neuron nerv = neuron.get(i);
            ArrayList<Neuron> inputs = new ArrayList<>(2);
            ArrayList<Double> initWeight = new ArrayList<>(2);
            if (i % inSize == 0) {
                inputs.add(neuron.get(i - inSize));
                initWeight.add(rand.nextDouble() * 2);
            } else if (i % inSize  == inSize - 1) {
                inputs.add(neuron.get(i - inSize));
                inputs.add(neuron.get(i - 1));
                initWeight.add(rand.nextDouble() * 2);
                initWeight.add(rand.nextDouble() * 2);
            } else {
                inputs.add(neuron.get(i - inSize));
                inputs.add(neuron.get(i - 1));
                initWeight.add(rand.nextDouble() * 2);
                initWeight.add(rand.nextDouble() * 2);               
            }
            nerv.set(i, inputs, null, initWeight);
        }
    }
    /**
     * one iteration of training rewarding for rightly guessed answer
     * @param input image representation
     * @param answer right answer
     */
    public void train(ArrayList<Integer> input, boolean answer) {
        double carrot = learnFactor;
        double stick = 2 - learnFactor;
        if (answer != this.clasify(input)) {
            carrot = stick;
            stick = learnFactor;
        }
        for (int i = size - 1; i >= inSize; i--) {
            Neuron nerv = neuron.get(i);
            ArrayList<Double> inputs = nerv.getInputValues();
            if (inputs.get(0) >= inputs.get(inputs.size() - 1)) {
                nerv.updateWeight(0, carrot);
                nerv.updateWeight(inputs.size() - 1, stick);
            } else {
                nerv.updateWeight(0, stick);
                nerv.updateWeight(inputs.size() - 1, carrot);
            }
        }
    }
    /**
     * Answears main question of its existence
     * @param input image representation
     * @return returns true if image is an object or false if it is not
     */
    public boolean clasify(ArrayList<Integer> input) {
        long sum = 0;
        for (int i = 0; i < inSize; i++) {
            Neuron nerv = neuron.get(i);
            nerv.value = input.get(i);
            sum += nerv.value;
            nerv.updated = true;
        }
        boolean needUpdate;
        do {
            needUpdate = false;
            for (int i = inSize; i < size; i++) {
                Neuron nerv = neuron.get(i);
                if (!nerv.update()) {
                    needUpdate = true;
                }
            }
        } while(needUpdate);
        Neuron nerv = neuron.get(size - 1);
        return nerv.value >= sum;
    }
}
