/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package WeakClassifier.util;

/**
 *
 * @author user
 */
public class CummulativeSummation {
    
    public float[] CumSum (float[] toSum, int imageCount){
        float[] cumSum = new float [imageCount];
        cumSum[0] = toSum[0];
        for(int i = 1 ; i< imageCount ; i++){
            cumSum[i] = cumSum[i-1] + toSum[i];  
        }
        
        return cumSum;
    }
    
    public float[] PosCumSum (float[] toSum, int imageCount , float [] labels){
        float[] cumSum = new float [imageCount];
        cumSum[0] = toSum[0];
        for(int i = 1 ; i< imageCount ; i++){
            cumSum[i] = cumSum[i-1] + toSum[i];  
        }
        
        return cumSum;
    }
    
    public float[] NegCumSum (float[] toSum, int imageCount){
        float[] cumSum = new float [imageCount];
        cumSum[0] = toSum[0];
        for(int i = 1 ; i< imageCount ; i++){
            cumSum[i] = cumSum[i-1] + toSum[i];  
        }
        
        return cumSum;
    }
}
