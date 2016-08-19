/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CustomDataStructure;

/**
 *
 * @author xitiz
 */
public class ClassifierData {

    private int[] mPolarity;
    private float[] mThreshold;
    private int[] mFeatureIndex;
    private float[] mAlpha;

    public ClassifierData(int[] polarity, float[] threshold, int[] featureIndex, float[] alpha) {
        this.mPolarity = polarity;
        this.mThreshold = threshold;
        this.mFeatureIndex = featureIndex;
        this.mAlpha = alpha;
    }

    public int[] getPolarity() {
        return mPolarity;
    }

    public void setPolarity(int[] mPolarity) {
        this.mPolarity = mPolarity;
    }

    public float[] getThreshold() {
        return mThreshold;
    }

    public void setThreshold(float[] mThreshold) {
        this.mThreshold = mThreshold;
    }

    public int[] getFeatureIndex() {
        return mFeatureIndex;
    }

    public void setFeatureIndex(int[] mFeatureIndex) {
        this.mFeatureIndex = mFeatureIndex;
    }

    public float[] getAlpha() {
        return mAlpha;
    }

    public void setAlpha(float[] mAlpha) {
        this.mAlpha = mAlpha;
    }

}
