/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package my.NNPais;

import static com.googlecode.javacv.cpp.opencv_core.*;
import com.googlecode.javacv.cpp.opencv_core.CvRect;
import com.googlecode.javacv.cpp.opencv_core.IplImage;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Used to describe image as set of Haar-like features
 * @author Drevlen
 * @param features array of Haar-like features
 * @param number number of values that represent image
 * @param minWidth minimal width of Haar block (pos + neg)
 * @param minHeight minimal heigh of Haar block (pos + neg)
 * @param maxWidth maximum width of Haar block (pos + neg) equals width of positive example
 * @param maxHeight maximum height of Haar block (pos + neg) equals height of positive example
 * @param scaleStep step for changeing of HaarBlock (> 1)
 */
public class FeaturePool {
    private ArrayList<HaarFeature> features;
    public int number;
    public int minWidth;
    public int minHeight;
    public int maxWidth;
    public int maxHeight;
    public double scaleStep;
    /**
     * inital method for creating of features
     * @param number number of values that represent image
     * @param minWidth minimal width of Haar block (pos + neg)
     * @param minHeight minimal heigh of Haar block (pos + neg)
     * @param maxWidth maximum width of Haar block (pos + neg) equals width of positive example
     * @param maxHeight maximum height of Haar block (pos + neg) equals height of positive example
     * @param scaleStep step for changeing of HaarBlock (> 1)
     */
    public void generate(int number, int minWidth, int minHeight, 
            int maxWidth, int maxHeight, double scaleStep) {
        this.number = number;
        this.minWidth = minWidth;
        this.minHeight = minHeight;
        this.maxWidth = maxWidth;
        this.maxHeight = maxHeight;
        this.scaleStep = scaleStep;
        
        int height = maxHeight;
        int width = maxWidth;
        features = new ArrayList<>(number);
            while(height > minHeight && width > minWidth){
                for (int y = 0; y <= maxHeight - height; y++) {
                    for (int x = 0; x <= maxWidth - width; x++)
                    {
                        CvRect pos = cvRect(x, y, x + width, y + height / 2);
                        CvRect neg = cvRect(x, y + height / 2, x + width, y + height / 2);
                        features.add(new HaarFeature(pos, neg));
                        if (features.size() == number) {
                            return;
                        }
                    }
                }
                height /= scaleStep;
                width /= scaleStep;
            }
            if (features.size() < number){
                CvRect pos = cvRect(0,0,0,0), neg = cvRect(0,0,0,0);
                HaarFeature zero = new HaarFeature(pos, neg);
                for (int i = features.size(); i < number; i++) {
                    features.add(zero);
                }  
            }
                
    }
    /**
     * transform image into set of values from haar-like features
     * @param image freyscale image to transform
     * @param shift dx and dy shifts inside bigger image or none
     * @return ArrayList of integer values
     */
    public ArrayList<Integer> getValues(IplImage image, int ... shift) {
        ArrayList<Integer> values = new ArrayList<>();
        int dx = 0, dy = 0;
        if (shift.length == 2) {
            dx = shift[0];
            dy = shift[1];
        }
        for (int i = 0; i < features.size(); i++)
        {
            values.add(features.get(i).getValue(image, dx, dy));
        }
        return values;
    }
    
}
