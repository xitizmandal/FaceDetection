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
public class Classifier {

    private float[] mAlpha;
    private int[] mPolarity;
    private float[] mThreshold;
    private double[] mFeatures;

    public Classifier(float[] alpha, int[] polarity, float[] threshold) {
        this.mAlpha = alpha;
        this.mPolarity = polarity;
        this.mThreshold = threshold;
    }

    public void setFeatures(double[] features) {
        this.mFeatures = features;
    }

    private int weakClassifierFunction(double featureValue, int polarity, double threshold) {
        if ((featureValue * polarity) < (threshold * polarity)) {
            return 1;
        } else {
            return 0;
        }
    }

    public int strongClassifierFunction() {
        double functionSum = 0;
        double alphaSum = 0;
        for (int i = 0; i < mAlpha.length; i++) {
            functionSum += (mAlpha[i] * weakClassifierFunction(mFeatures[i], mPolarity[i], mThreshold[i]));
            alphaSum += mAlpha[i];
        }

        if (functionSum >= (0.5 * alphaSum)) {
            return 1;
        } else {
            return 0;
        }
    }

}
