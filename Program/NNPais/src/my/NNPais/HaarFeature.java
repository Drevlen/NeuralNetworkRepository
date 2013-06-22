/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package my.NNPais;
import com.googlecode.javacv.cpp.opencv_core.CvRect;
import com.googlecode.javacv.cpp.opencv_core.IplImage;
import java.nio.ByteBuffer;
/**
 * Haar-like feature that uses diference between pixelsums of 2 areas
 * @author Drevlen
 * @param pos - rectangle of pixels with positive value
 * @param neg - rectangle of pixels with negative value
 */
public class HaarFeature {

    private CvRect pos;
    private CvRect neg;
    private int area;
    /**
     * constructor
     * @param p rectangle of pixels with positive value
     * @param n rectangle of pixels with negative value
     */
    public HaarFeature(CvRect p, CvRect n){
        pos = p;
        neg = n;
        area = pos.width() * pos.height();// + neg.width() * neg.height();
    };
    /**
     * calculate feature Value for image
     * @param im - greyscale CvImage 
     * @param dx - shift in pixels of posible object position
     * @param dy - shift in pixels of posible object position
     * @return return value as diference between sum of pixels
     */
    public int getValue(IplImage im, int dx, int dy){
        ByteBuffer pixels;
        pixels = im.getByteBuffer();
        long sumPos = 0;
        long sumNeg = 0;
        
        for(int y = pos.y(); y < pos.y() + pos.height() ; y++) {
            for(int x = pos.x(); x < pos.x() + pos.width(); x++) {
                int index = (y + dy) * im.widthStep() + (x + dx) 
                        * im.nChannels();
                if (index < 0 || index >= pixels.limit()) {
                   System.out.println("FeaturePool getValue Error. Trying to compute unexistend pixel;\n");
                }
                    else {
                    sumPos += pixels.get(index) & 0xFF;
                }
            }        
        }
        for(int y = neg.y(); y < neg.y() + neg.height() ; y++) {
            for(int x = neg.x(); x < neg.x() + neg.width(); x++) {
                int index = (y + dy) * im.widthStep() + (x + dx) 
                        * im.nChannels();
                if (index < 0 || index >= pixels.limit()) {
                   System.out.println("FeaturePool getValue Error. Trying to compute unexistend pixel;\n");
                }
                else {
                    sumNeg += pixels.get(index) & 0xFF;
                }
            }        
        }
        return (int)(sumPos - sumNeg) / area;
    }
    
    
}
