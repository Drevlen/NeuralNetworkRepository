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
            ArrayList<Neuron> outputs = new ArrayList<>(3);
            if (i == 0) {
                outputs.add(neuron.get(inSize));
                outputs.add(neuron.get(inSize + 1));
            } else if (i == inSize - 1) {
                outputs.add(neuron.get(inSize + i - 1));
                outputs.add(neuron.get(inSize + i));                
            } else {
                outputs.add(neuron.get(inSize + i - 1));
                outputs.add(neuron.get(inSize + i));
                outputs.add(neuron.get(inSize + i + 1));
            } 
            nerv.set(i, null, outputs, null);
        }
        /*
         *  Creating hiden layers
         */
        for (int i = inSize; i < size - inSize; i++) {
            Neuron nerv = neuron.get(i);
            ArrayList<Neuron> outputs = new ArrayList<>(4);
            ArrayList<Neuron> inputs = new ArrayList<>(4);
            ArrayList<Double> initWeight = new ArrayList<>(4);
            if (i % inSize == 0) {
                inputs.add(neuron.get(i - inSize));
                inputs.add(neuron.get(i - inSize + 1));
                outputs.add(neuron.get(i + 1));
                outputs.add(neuron.get(i + inSize));
                outputs.add(neuron.get(i + inSize + 1));
                initWeight.add(rand.nextDouble() * 2 - 1);
                initWeight.add(rand.nextDouble() * 2 - 1);
            } else if (i % inSize  == inSize - 1) {
                inputs.add(neuron.get(i - inSize - 1));
                inputs.add(neuron.get(i - inSize));
                inputs.add(neuron.get(i - 1));
                outputs.add(neuron.get(i + inSize - 1));
                outputs.add(neuron.get(i + inSize));
                initWeight.add(rand.nextDouble() * 2 - 1);
                initWeight.add(rand.nextDouble() * 2 - 1);
                initWeight.add(rand.nextDouble() * 2 - 1);
            } else {
                inputs.add(neuron.get(i - inSize - 1));
                inputs.add(neuron.get(i - inSize));
                inputs.add(neuron.get(i - inSize + 1));
                inputs.add(neuron.get(i - 1));
                outputs.add(neuron.get(i + 1));
                outputs.add(neuron.get(i + inSize - 1));
                outputs.add(neuron.get(i + inSize));
                outputs.add(neuron.get(i + inSize + 1));
                initWeight.add(rand.nextDouble() * 2 - 1);
                initWeight.add(rand.nextDouble() * 2 - 1);
                initWeight.add(rand.nextDouble() * 2 - 1);
                initWeight.add(rand.nextDouble() * 2 - 1);
            }
            nerv.set(i, inputs, outputs, initWeight);
        }
        
        for (int i = size - inSize; i < size; i++) {
            Neuron nerv = neuron.get(i);
            ArrayList<Neuron> inputs = new ArrayList<>(4);
            ArrayList<Neuron> outputs = new ArrayList<>(1);
            ArrayList<Double> initWeight = new ArrayList<>(4);
            if (i % inSize == 0) {
                inputs.add(neuron.get(i - inSize));
                inputs.add(neuron.get(i - inSize + 1));
                outputs.add(neuron.get(i + 1));
                initWeight.add(rand.nextDouble() * 2 - 1);
                initWeight.add(rand.nextDouble() * 2 - 1);
            } else if (i % inSize  == inSize - 1) {
                inputs.add(neuron.get(i - inSize));
                inputs.add(neuron.get(i - inSize - 1));
                inputs.add(neuron.get(i - 1));
                outputs = null;
                initWeight.add(rand.nextDouble() * 2 - 1);
                initWeight.add(rand.nextDouble() * 2 - 1);
                initWeight.add(rand.nextDouble() * 2 - 1);
            } else {
                inputs.add(neuron.get(i - inSize - 1));
                inputs.add(neuron.get(i - inSize));
                inputs.add(neuron.get(i - inSize + 1));
                inputs.add(neuron.get(i - 1));
                outputs.add(neuron.get(i + 1));
                initWeight.add(rand.nextDouble() * 2 - 1);
                initWeight.add(rand.nextDouble() * 2 - 1);
                initWeight.add(rand.nextDouble() * 2 - 1);
                initWeight.add(rand.nextDouble() * 2 - 1);

            }
            nerv.set(i, inputs, outputs, initWeight);
        }
    }
    /**
     * one iteration of training rewarding for rightly guessed answer
     * @param input image representation
     * @param answer right answer
     */
    public void train(ArrayList<Integer> input, boolean answer) {      
        double corection = (answer == clasify(input))? learnFactor : - learnFactor;
        for (int i = size - 1; i >= inSize; i--) {
            Neuron nerv = neuron.get(i);
            for (Neuron nervIn : nerv.getInputs()) {
                double delta = (double)Math.abs(nerv.value - nervIn.value) / 256;
                if ((nervIn.value >= 0) == (nerv.value >= 0)) {
                    nerv.updateWeight(nervIn, corection * (delta));
                    //increase if tryAnswer == answer
                }
                else {
                    //decrease if tryAnswer == answer
                    nerv.updateWeight(nervIn, - corection * (delta));
                }
//                System.out.println(delta);
            }

        }
    }
    /**
     * Answears main question of its existence
     * @param input image representation
     * @return returns true if image is an object or false if it is not
     */
    public boolean clasify(ArrayList<Integer> input) {
        //set values
        for (int i = 0; i < inSize; i++) {
            Neuron nerv = neuron.get(i);
            nerv.value = input.get(i);
            nerv.updated = true;
        }
        boolean needUpdate;
        //compute network
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
        return nerv.value >= 0;
    }
}
