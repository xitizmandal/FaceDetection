/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package WeakClassifier;

import WeakClassifier.util.CummulativeSummation;

/**
 *
 * @author user
 */
public class WeakLearner {

    public void TrainWeakLearner(int posImageNum, int negImageNumber, int totalImage, float[] weights, int[] labels, float[][] completeFeatures) {
        float tPlus = 0, tMinus = 0;
        float[] oneFeature = new float[113472];
        float[] sortedOneFeature = new float[113472];
        float[] cumSortedOneFeature = new float[113472];
        float[] sPlus = new float[totalImage];
        float[] sMinus = new float[totalImage];
        float[] errPlus = new float[totalImage];
        float[] errMinus = new float[totalImage];

        AscendingBubbleSorting ascendingBubbleSorting = new AscendingBubbleSorting();
        CummulativeSummation cummulativeSummation = new CummulativeSummation();

        for (int i = 0; i < posImageNum; i++) {
            tPlus += weights[i];
        }
        for (int i = posImageNum; i < totalImage; i++) {
            tMinus += weights[i];
        }

        for (int feaCount = 0; feaCount < 113472; feaCount++) {

            for (int imageCount = 0; imageCount < totalImage; imageCount++) {
                oneFeature[imageCount] = completeFeatures[imageCount][feaCount];
                //sort index needed
            }
            sortedOneFeature = ascendingBubbleSorting.Sort(oneFeature);
            cumSortedOneFeature = cummulativeSummation.CumSum(oneFeature, totalImage);
        }
    }
}
