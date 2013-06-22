/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package my.NNPais;

import com.googlecode.javacv.cpp.opencv_core;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Classifier that uses neural network and Haar-like features
 * @author Drevlen
 * @param net neural network
 * @param pool haar-like features for representation of image
 * @param baseSize size of object (for not stepping ouside image)
 */
public class Classifier extends Detector {
    private NNetwork net;
    private FeaturePool pool;
    /**
     * @param number number of neurons in network
     * @param inSize number of features in pool
     * @param factor learning speed of network
     * @param minWidth minimal width of Haar block (pos + neg)
     * @param minHeight minimal heigh of Haar block (pos + neg)
     * @param maxWidth maximum width of Haar block (pos + neg) equals width of positive example
     * @param maxHeight maximum height of Haar block (pos + neg) equals height of positive example
     * @param scaleStep step for changeing of HaarBlock (> 1)
     */
    public void init(int number, int inSize, double factor, int minWidth, 
            int minHeight, int maxWidth, int maxHeight, double scaleStep){ 
        net = new NNetwork();
        net.init(number, inSize, factor);
        pool = new FeaturePool();
        pool.generate(inSize, minWidth, minHeight, maxWidth, maxHeight, 
                scaleStep);
        baseSize = new opencv_core.CvSize(maxWidth, maxHeight);
        detectionStep = 5;
    }
    /**
     * Train function
     * @param positives lots of greyscale images of same format with desired object
     * @param negatives lots of greyscale images of background
     */
    public void train (ArrayList<opencv_core.IplImage> positives, 
            ArrayList<opencv_core.IplImage> negatives) {
        for (int i = 0; i < positives.size() && i < negatives.size(); i++) {
            ArrayList<Integer> inputs = pool.getValues(positives.get(i));
            net.train(inputs, true);

            inputs = pool.getValues(negatives.get(i));
            net.train(inputs, false);            
        }
    }
    /**
     * function of classification
     * @param image input grayscale image
     * @param shift dx and dy position on image or none
     * @return true if shifted part of image is an object
     * @Override to suport Haar-like features and network classificator
     */
    @Override
    public boolean classify(opencv_core.IplImage image, int ... shift) {
        ArrayList<Integer> inputs = pool.getValues(image, shift);
        return net.clasify(inputs);
    }
    /**
     * Saving classifier to a file
     * @param filename full path to saving file
     */
    public void save(String filename) {
        FileOutputStream saveFile = null;
            try {
                saveFile = new FileOutputStream(filename);
            try (ObjectOutputStream save = new ObjectOutputStream(saveFile)) {
                save.writeObject(net);
                save.writeObject(pool.number);
                save.writeObject(pool.minWidth);
                save.writeObject(pool.minHeight);
                save.writeObject(pool.maxWidth);
                save.writeObject(pool.maxHeight);
                save.writeObject(pool.scaleStep);
                save.writeObject(baseSize);
                save.writeObject(detectionStep);
                } catch (Exception exs) { 
        }       
        } catch (FileNotFoundException ex) { 
            Logger.getLogger(Classifier.class.getName())
                    .log(Level.SEVERE, null, ex);
        } finally {
            try {
                saveFile.close();
            } catch (IOException ex) {
                Logger.getLogger(Classifier.class.getName())
                        .log(Level.SEVERE, null, ex);
            }
        }
    }
    /**
     * Loading classifier from the file
     * @param filename full path to loading file
     */    
    public void load(String filename) {
        FileInputStream savedFile = null;
        try {
            savedFile = new FileInputStream(filename);
        try (ObjectInputStream restore = new ObjectInputStream(savedFile)) {
            net = (NNetwork)restore.readObject();
            pool = new FeaturePool();
            pool.number = (int)restore.readObject();
            pool.minWidth = (int)restore.readObject();
            pool.minHeight = (int)restore.readObject();
            pool.maxWidth = (int)restore.readObject();
            pool.maxHeight = (int)restore.readObject();
            pool.scaleStep = (double)restore.readObject();
            pool.generate(pool.number, pool.minWidth, pool.minHeight, 
                        pool.maxWidth, pool.maxHeight, pool.scaleStep);
            baseSize = new opencv_core.CvSize();
            baseSize = (opencv_core.CvSize)restore.readObject();
            detectionStep = (int)restore.readObject();
            } catch (Exception exs) {
        }
        } catch (FileNotFoundException ex) { 
             Logger.getLogger(Classifier.class.getName())
                     .log(Level.SEVERE, null, ex);
        } finally {
            try {
                savedFile.close();
            } catch (IOException ex) {
                Logger.getLogger(Classifier.class.getName())
                        .log(Level.SEVERE, null, ex);
            }
        }
    }
}
