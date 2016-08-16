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

    private float threshold, minErr;
    private int polarity, featInd;
    private int[] bestResult;

    public void TrainWeakLearner(int posImageNum, int negImageNumber, int totalImage, float[] weights, int[] labels, float[][] completeFeatures) {
        float tPlus = 0, tMinus = 0;
        int polar;
        minErr = 999999999;

        int[] result = new int[totalImage];
        bestResult = new int[totalImage];
        float[] oneFeat = new float[totalImage];
        float[] sortOneFeat = new float[totalImage];
        float[] cumSortedOneFeature = new float[113472];

        Integer[] sortInd = new Integer[totalImage];
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
        for (int feaCount = 0; feaCount < 113472; feaCount++) {

            //Get required Feature from all images
            for (int imageCount = 0; imageCount < totalImage; imageCount++) {
                oneFeat[imageCount] = completeFeatures[imageCount][feaCount];
            }

            //Sorting features and extract pre sorting indices 
            SortPlusIndex sortPlusIndex = new SortPlusIndex(oneFeat);
            sortOneFeat = sortPlusIndex.getSortedArray();
            sortInd = sortPlusIndex.getActualIndex();

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
             * Calculate misclassifications and errors according to cumulative
             * value such that it represents the error of that particular
             * position value was taken as the classification threshold.
             */
            for (int imageCount = 0; imageCount < totalImage; imageCount++) {
                errPlus[imageCount] = sPlus[imageCount] + (tMinus - sMinus[imageCount]);
                errMinus[imageCount] = sMinus[imageCount] + (tPlus - sPlus[imageCount]);
//                System.out.println("FeatCount "+ feaCount +"SPlus = " + sPlus[imageCount] + " SMinus" + sMinus[imageCount]);
                if (errPlus[imageCount] < errMinus[imageCount]) {
                    oneFeatErr[imageCount] = errPlus[imageCount];
                } else {
                    oneFeatErr[imageCount] = errMinus[imageCount];
                }
            }

            //Among all the error values narrow it down to a single minimum error value
            float minOneFeatErr = oneFeatErr[0];
            int minInd = 0;
            for (int imageCount = 1; imageCount < totalImage; imageCount++) {
                if (oneFeatErr[imageCount] < minOneFeatErr) {
//                    if(oneFeatErr[imageCount] != 0){
                        minOneFeatErr = oneFeatErr[imageCount];
                        minInd = imageCount;
//                    }
                }
            }

            //Choose polarity and result value for the above choosen min error value
            if (errPlus[minInd] <= errMinus[minInd]) {
                polar = -1;
                for (int i = minInd; i < totalImage; i++) {
                    result[i] = 1;
                }
                for (int j = 0; j < totalImage; j++) {
                    result[sortInd[j]] = result[j];
                }
            } else {
                polar = 1;
                for (int i = 0; i < minInd; i++) {
                    result[i] = 1;
                }
                for (int j = 0; j < totalImage; j++) {
                    result[sortInd[j]] = result[j];
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
                    threshold =  (float) 0.5*(sortOneFeat[minInd-1] + sortOneFeat[minInd]);
                }
                polarity = polar;
                featInd = feaCount;
                for (int i = 0; i < totalImage; i++) {
                    bestResult[i] = result[i];
                }
            }

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
