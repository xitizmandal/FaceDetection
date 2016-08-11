/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package WeakClassifier.util;

import java.util.Arrays;

/**
 *
 * @author Aditya Acharya
 */
public class CummulativeSummation {
    private float[] sPlus;
    private float[] sMinus;
    private float[] overallSum;

    public CummulativeSummation(float[] sortWeights, int [] sortLabels, int totalImage) {
        sPlus = new float[totalImage];
        sMinus = new float[totalImage];
        overallSum = new float [totalImage];
        
        sPlus[0] = sortWeights[0] * sortLabels[0];
        overallSum[0] = sortWeights[0];
        
        for(int i= 1 ; i < totalImage; i++){
            sPlus[i] = sPlus[i-1] + (sortWeights[i]*sortLabels[i]);
            overallSum[i] = overallSum[i-1]+ sortWeights[i]; 
        }
        for(int j = 0; j< totalImage; j++){
            sMinus[j] = overallSum[j] - sPlus[j];
        }
    }

    public float[] getSPlus() {
        return this.sPlus;
    }

    public float[] getSMinus() {
        return this.sMinus;
    }
    
//    public float[] CumSum (float[] toSum, int imageCount){
//        float[] cumSum = new float [imageCount];
//        cumSum[0] = toSum[0];
//        for(int i = 1 ; i< imageCount ; i++){
//            cumSum[i] = cumSum[i-1] + toSum[i];  
//        }
//        
//        return cumSum;
//    }
}