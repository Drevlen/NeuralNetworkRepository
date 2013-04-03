/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package my.NNPais;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Base class of Neural Network
 * Emulates work of human neurons
 * @author Drevlen
 * @param input conected to input Neurons
 * @param output conected to output Neurons
 * @param weight weights of input Neurons
 * @param updated where all inputs recieved or not 
 * @param value result of weighting of input Neurons
 * @param id identification of Neurons (instead of memory addres)
 *
 */
public class Neuron  implements Serializable {
    private ArrayList<Neuron> input;
    private ArrayList<Neuron> output;
    private ArrayList<Double> weight;
    public boolean updated;
    public int value;
    public int id;
    /**
     * Set is used for constructing a network
     * @param id unique describer of Neuron int Network
     * @param in conected input neurons
     * @param out conected output neurons
     */
    public void set(int id, ArrayList<Neuron> in, ArrayList<Neuron> out, 
            ArrayList<Double> initWeight){
        this.id = id;
        input = in;
        output = out;
        weight = initWeight;
        updated = false;
        value = 0;
    }
    /**
     * Used for detection
     * @return if true than all conected outputs need updating
     */
    public boolean update() {
        value = 0;
        updated = true;
        for (Neuron nerv : input) {
            if (nerv.updated) {
                value = (int) Math.max(value, 
                        (int)nerv.value * weight.get(input.indexOf(nerv)));
            } else {
                updated = false;
                break;
            }
        }
        return updated;
    }
    /**
     * Used for training. 
     * Increasing or decreasing weight with conected neuron.
     * Depends on sign value;
     * @param conection one of input neurons that need updating
     * @param value learn factor
     */
    public void updateWeight(Neuron conection, double value) {
        int num = 0;
        for(Neuron n : input) {
                    if (n.id == conection.id) {
                        weight.set(num, weight.get(num) * value);
                        break;
                    }
                    num++;
                }
    }
        /**
     * Used for training. 
     * Increasing or decreasing weight with indexed neuron.
     * Depends on sign value;
     * @param conection one of input neurons that need updating
     * @param value learn factor
     */
    public void updateWeight(int index, double value) {
        if (index < weight.size()) {
            weight.set(index, weight.get(index) * value);
        }
    }
    /**
     * gives information about neurons weighted input
     * @return returns weighted input values
     */
    public ArrayList<Double> getInputValues(){
        ArrayList<Double> inValues = new ArrayList<>();
        int index = 0;
        for (Neuron nerv : input) {
            inValues.add(nerv.value * weight.get(index));
            index++;
        }
        return inValues;
    }
    
}
