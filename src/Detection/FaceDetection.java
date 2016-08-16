/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Detection;

/**
 *
 * @author xitiz
 */
public class FaceDetection {
    private double[] mAlpha;
    private double[] mThreshold;
    private int[] mPolarity;
    private int[] mFeatureIndex;
    
    public FaceDetection(){
        mAlpha = new double[] {2.4303856,3.0284867,5.944429, 11.408655, 6.1519547, 11.05554};
        mThreshold = new double[] {0.21353465,0.25338864,0.21170911,0.5, 0.21353465, 0.9946809 };
        mPolarity = new int[] {-1,-1,-1,-1,-1,-1};
        mFeatureIndex = new int[] {15817, 45139, 61128, 0, 15817, 50027};        
    }
}
