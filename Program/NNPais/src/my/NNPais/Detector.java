/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package my.NNPais;

import static com.googlecode.javacv.cpp.opencv_imgproc.cvCvtColor;
import static com.googlecode.javacv.cpp.opencv_imgproc.CV_BGR2GRAY;
import com.googlecode.javacv.cpp.opencv_core;
import java.util.ArrayList;

/**
 * general functionality of detectors
 * @author Drevlen
 */
public class Detector {
    protected opencv_core.IplImage image;
    public int detectionStep;
    public opencv_core.CvSize baseSize;
    /**
     * initing image to fit requirements
     * @param inImage 
     */
    public void setImage(opencv_core.IplImage inImage) {
        cvCvtColor(inImage, image, CV_BGR2GRAY);
//        image = opencv_core.cvCloneImage(inImage);
    }
    /**
    * Finds object on all positions
    * @return returns all found objects
    */
    public ArrayList<opencv_core.CvRect> detectAll(){
        ArrayList<opencv_core.CvRect> objects = new ArrayList<>();
        for (int y = 0; y < image.cvSize().height() - baseSize.height(); 
                y+=detectionStep) {
                    for (int x = 0; x < image.cvSize().width() 
                            - baseSize.width(); x+=detectionStep) {
                        if (classify(image, x, y)) {
                            opencv_core.CvRect rect = new opencv_core.CvRect();
                            rect.x(x);
                            rect.y(y);
                            rect.width(baseSize.width());
                            rect.height(baseSize.height());  
                            objects.add(rect);
                        }
                    }   
        }
        return objects;
    }
     /**
     * function of classification
     * @param image input grayscale image
     * @param shift dx and dy position on image or none
     * @return true if shifted part of image is an object
     */
    public boolean classify(opencv_core.IplImage image, int ... shift){
        return false;
    }
}
