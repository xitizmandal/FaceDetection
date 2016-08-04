/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package haarfilterall.util;

/**
 *
 * @author user
 */
public class FeatureNormalization {

    public float[] FeaNormal(int[] featureValues, int count, int max) {
        float[] temp = new float[count];
        for (int norCntr = 0; norCntr < count; norCntr++) {
//            System.out.println("Values " + featureValues[norCntr]);
//            featureValues[norCntr] = featureValues[norCntr]/max;
            temp[norCntr] = ((float) featureValues[norCntr]) / max;
//            System.out.println("Feature[" + norCntr + "] = " + temp[norCntr]);
        }
        return temp;
    }

}
