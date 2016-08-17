/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package WeakClassifier;

import WeakClassifier.util.CummulativeSummation;
import WeakClassifier.util.SortPlusIndex;

/**
 *
 * @author Aditya Acharya
 */
public class WeakLearner {

    private float minErr, threshold;
//    private double threshold;
    private int polarity, featInd;
    private int[] bestResult;

    public void TrainWeakLearner(int posImageNum, int negImageNumber, int totalImage, float[] weights, int[] labels, float[][] completeFeatures) {
        float tPlus = 0, tMinus = 0;
        int polar;
        minErr = (float) 10000.0;
        float minOneFeatErr = (float) 19999.0;
        int minInd = 0;

        int[] result = new int[totalImage];
        int[] reorderResult = new int[totalImage];
        bestResult = new int[totalImage];
        float[] oneFeat = new float[totalImage];
        float[] sortOneFeat = new float[totalImage];
        float[] cumSortedOneFeature = new float[113472];

        int[] sortInd = new int[totalImage];
        float[] sortWeights = new float[totalImage];
        int[] sortLabels = new int[totalImage];
        float[] sPlus = new float[totalImage];
        float[] sMinus = new float[totalImage];
        float[] errPlus = new float[totalImage];
        float[] errMinus = new float[totalImage];
        float[] oneFeatErr = new float[totalImage];

        //Total image weights of postitive and negative images
        for (int i = 0; i < posImageNum; i++) {
            tPlus += weights[i];
        }
        for (int i = posImageNum; i < totalImage; i++) {
            tMinus += weights[i];
        }

        //For all features of all images
        for (int feaCount = 1; feaCount < 113472; feaCount++) {

            if (feaCount != 36432 && feaCount != 72864 && feaCount != 96048) {

                //Get required Feature from all images
                for (int imageCount = 0; imageCount < totalImage; imageCount++) {
                    oneFeat[imageCount] = completeFeatures[imageCount][feaCount];
                }

                //Sorting features and extract pre sorting indices 
                SortPlusIndex sortPlusIndex = new SortPlusIndex(oneFeat);
                sortOneFeat = sortPlusIndex.getSortedArray();
                sortInd = sortPlusIndex.getActualIndex();

//            for(int i = 0 ; i< totalImage ; i++){
//                System.out.println("Sort one fea = " + sortOneFeat[i]);
//            }
                //Sort image weights and labels according to indices extracted from above
                for (int imageCount = 0; imageCount < totalImage; imageCount++) {
                    sortWeights[imageCount] = weights[sortInd[imageCount]];
                    sortLabels[imageCount] = labels[sortInd[imageCount]];
                }

                //Cummulative sum of positive and negative image weights after sorting
                CummulativeSummation cummulativeSummation = new CummulativeSummation(sortWeights, sortLabels, totalImage);
                sPlus = cummulativeSummation.getSPlus();
                sMinus = cummulativeSummation.getSMinus();

                /**
                 * Calculate misclassifications and errors according to
                 * cumulative value such that it represents the error of that
                 * particular position value was taken as the classification
                 * threshold.
                 */
                for (int imageCount = 0; imageCount < totalImage; imageCount++) {
                    errPlus[imageCount] = sPlus[imageCount] + (tMinus - sMinus[imageCount]);
                    errMinus[imageCount] = sMinus[imageCount] + (tPlus - sPlus[imageCount]);
//                System.out.println("Err Plus = " + errPlus[imageCount]);
//                System.out.println("Err Minus = " + errMinus[imageCount]);
                    if (errPlus[imageCount] < errMinus[imageCount]) {
                        oneFeatErr[imageCount] = errPlus[imageCount];
                    } else {
                        oneFeatErr[imageCount] = errMinus[imageCount];
                    }
                }

                //Among all the error values narrow it down to a single minimum error value
//            System.out.println("0th index value = " + oneFeatErr[0]);
//            float minOneFeatErr = oneFeatErr[5];
//            int minInd = 5;
                for (int imageCount = 0; imageCount < totalImage; imageCount++) {
                    if ((oneFeatErr[imageCount] < minOneFeatErr)) {
//                    System.out.println("BHITRA Paila ko index" + minInd);
//                    if (oneFeatErr[imageCount] != 0) {
                        minOneFeatErr = oneFeatErr[imageCount];
                        minInd = imageCount;
//                    }
//                    System.out.println("BHITRA Pachi ko index" + minInd);
//                    System.out.println("");
                    }
                }

                //Choose polarity and result value for the above choosen min error value
                if (errPlus[minInd] <= errMinus[minInd]) {
                    polar = -1;
                    for (int i = minInd; i < totalImage; i++) {
                        result[i] = 1;
                    }
                    for (int i = 0; i < minInd; i++) {
                        result[i] = 0;
                    }
                    for (int j = 0; j < totalImage; j++) {
                        reorderResult[sortInd[j]] = result[j];
                    }
                } else {
                    polar = 1;
                    for (int i = 0; i < minInd; i++) {
                        result[i] = 1;
                    }
                    for (int i = minInd; i < totalImage; i++) {
                        result[i] = 0;
                    }
                    for (int j = 0; j < totalImage; j++) {
                        reorderResult[sortInd[j]] = result[j];
                    }
                }

                //Defining weak clasifier parameters
                if (minOneFeatErr < minErr) {
                    minErr = minOneFeatErr;
                    if (minInd == 0) {
                        threshold = (float) (sortOneFeat[0] - 0.5);
                    } else if (minInd == (totalImage - 1)) {
                        threshold = (float) (sortOneFeat[totalImage - 1] + 0.5);
                    } else {
//                        System.out.println("Min index"+minInd);
                        threshold = (float) (0.5 * (sortOneFeat[minInd - 1] + sortOneFeat[minInd]));
//                        System.out.println("threshold 3rd" + threshold);
//                        System.out.println("Feanumber" + feaCount);
                    }
                    polarity = polar;
                    featInd = feaCount;
                    for (int i = 0; i < totalImage; i++) {
                        bestResult[i] = reorderResult[i];

                    }
                }
//            System.out.println("threshold" + threshold);
            }
//        System.out.println("threshold" + threshold);
        }
    }

    public float getThreshold() {
        return this.threshold;
    }

    public int getPolarity() {
        return this.polarity;
    }

    public int getFeatInd() {
        return this.featInd;
    }

    public int[] getBestResult() {
        return this.bestResult;
    }

    public float getMinErr() {
        return this.minErr;
    }
}
