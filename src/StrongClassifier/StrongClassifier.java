/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package StrongClassifier;

import WeakClassifier.WeakLearner;
import StrongClassifier.util.SortPlusIndex;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.Math;

/**
 *
 * @author xitiz
 */
public class StrongClassifier {

    private int mPositiveTrainSize;
    private int mNegativeTrainSize;
    private int mNoOfFeatures = 113472;         //For Our image set
    private int mTotalTrainSize;//noTrain
    private int mCascadeIteration;
    private int mStage;
    private int[] mIndicesAll;
    private int[] mNextIndicesAll;
    private float[] imageWeight;
    private float[][] weakClassifier;
    private float[][] ht;
    private float[] alpha;                      //weight factors for weak classifiers
    private float[][] ht_result;                //Classification Result for t th weak classifier; 1 for +ve; 0 for -ve
    private int[] strLearner_result;
    private float[] truePositiveRate;
    private float[] falsePositiveRate;
    private float[][] completeFeatures;
    private int[] labels;
    private String writeContent;

    public StrongClassifier(int CascadeIteration, int positiveImages, float[][] completeFeatures, int[] indicesAll, int stage, int[] labels) {
        this.mCascadeIteration = CascadeIteration;
        this.mPositiveTrainSize = positiveImages;
        this.completeFeatures = completeFeatures;
        this.mIndicesAll = indicesAll;
        this.mStage = stage;
        this.mNegativeTrainSize = indicesAll.length - mPositiveTrainSize;
        this.mTotalTrainSize = mPositiveTrainSize + mNegativeTrainSize;
        System.out.println("Stage " + mStage + " " + mPositiveTrainSize + "   " + mNegativeTrainSize);
        this.labels = labels;
        this.weakClassifier = new float[4][mCascadeIteration];
        this.ht_result = new float[mTotalTrainSize][mCascadeIteration];
        this.strLearner_result = new int[mTotalTrainSize];
        this.alpha = new float[mCascadeIteration];
        this.truePositiveRate = new float[mCascadeIteration];
        this.falsePositiveRate = new float[mCascadeIteration];
        this.writeContent = "";

        //initializeFeatures();
        initializeWeights(mTotalTrainSize);
    }

    private void initializeFeatures() {

    }

    private void initializeWeights(int totalTrainSize) {
        imageWeight = new float[totalTrainSize];
        for (int i = 0; i < totalTrainSize; i++) {
            if (i < mPositiveTrainSize) {
                imageWeight[i] = (float) (0.5 / mPositiveTrainSize);
//                System.out.println(imageWeight[i]);
            } else {
                imageWeight[i] = (float) (0.5 / mNegativeTrainSize);
//                System.out.println(imageWeight[i]);
            }
        }
    }

    public float[] normalizeWeights(float[] weights) {
        float totalWeightSum = weightSum(weights);
        float[] normalizedWeight = new float[weights.length];

        for (int i = 0; i < weights.length; i++) {
            normalizedWeight[i] = weights[i] / totalWeightSum;
//            System.out.println(normalizedWeight[i]);
        }
        return normalizedWeight;
    }

    public float weightSum(float[] weights) {
        float totalSum = 0;
        for (int i = 0; i < (weights.length); i++) {
            totalSum += weights[i];
        }
        return totalSum;
    }

    public float[] updateWeights(float beta, float[] weights, int[] compareLabels) {
        float temp;

        for (int i = 0; i < weights.length; i++) {
            temp = (float) Math.pow((float) beta, 1 - compareLabels[i]);                //ei = compareLabels
            weights[i] = weights[i] * temp;
        }
        return weights;
    }

    public void setHTResult(int t, int[] classification) {
        for (int i = 0; i < classification.length; i++) {
            ht_result[i][t] = classification[i];
        }
    }

    //Matrix Multiplication.
    public float[] strongLearner(int t) {
//        System.out.println("Print gareko : " +ht_result.length);
        float[] strLearner = new float[ht_result.length];
        float sum = 0;
        for (int i = 0; i < ht_result.length; i++) {
            for (int j = 0; j < (t + 1); j++) {
                sum += ht_result[i][j] * alpha[j];
//                System.out.println( i + "_"+j+"ht_result: "+ht_result[i][j]+" alpha: "+alpha[j]+" sum: "+sum);
            }
            strLearner[i] = sum;
            sum = 0;
        }
        return strLearner;
    }

    public float[] getThresholdRange(float[] values) {
        float[] thresholdRange = new float[mPositiveTrainSize];
        System.arraycopy(values, 0, thresholdRange, 0, mPositiveTrainSize);
        return thresholdRange;
    }

    public float getMinimumValue(float[] values) {
        float minimumValue = 9999999;                 //max value of threshold cannot be greater than total number of images
        for (int i = 0; i < values.length; i++) {
            if (values[i] < minimumValue ) {
                minimumValue = values[i];
//                System.out.println(minimumValue + " " + i);
            }
        }
        return minimumValue;
    }

    public void setStrLearnerResult(float[] strLearner, float thresholdStrong) {
        for (int i = 0; i < mTotalTrainSize; i++) {
            if (strLearner[i] >= thresholdStrong) {
                strLearner_result[i] = 1;
            } else {
                strLearner_result[i] = 0;
            }
        }
    }

    public float getSum(int startIndex, int endIndex) {
        float sum = 0;
        for (int i = startIndex; i < endIndex; i++) {
            sum += strLearner_result[i];
        }
        return sum;
    }

    //For sorting negative test images only
    //Not needed
    public int[] sortStrongListener() {
        int tempIndex = strLearner_result.length - mPositiveTrainSize;
        int[] temp = new int[tempIndex];

        System.arraycopy(strLearner_result, mPositiveTrainSize, temp, 0, tempIndex);

        int tempValue;              //Our comparision value is either 0 or 1
        for (int i = 1; i < temp.length; i++) {
            for (int j = 0; j < temp.length - i; j++) {
                if (temp[j] < temp[j + 1]) {
                    tempValue = temp[j];
                    temp[j] = temp[j + 1];
                    temp[j + 1] = tempValue;
                }
            }
        }
        return temp;
    }

    public int[] comparedLabels(int[] labels, int[] classifyResult) {
        int[] compareLabels = new int[labels.length];
        for (int i = 0; i < classifyResult.length; i++) {
            if (labels[i] == classifyResult[i]) {
                compareLabels[i] = 0;
            } else {
                compareLabels[i] = 1;
            }
        }
        return compareLabels;
    }

    private void setNextIndexAll(int[] nextIndexAll) {
        this.mNextIndicesAll = nextIndexAll;
    }

    public int[] getnextIndexAll() {
        return mNextIndicesAll;
    }

    private int[] setNegativeInd(int[] negativeInd, int start) {
        int[] retInd = new int[negativeInd.length - start];
//        System.out.println(negativeInd.length);
        for (int i = 0; i < negativeInd.length - start; i++) {
            retInd[i] = negativeInd[start];
            start++;
        }
        return retInd;
    }

    private int[] setValueNextIndexAll(int[] negativeIndexAll) {
        int[] value = new int[mPositiveTrainSize + negativeIndexAll.length];
        for (int i = 0; i < mPositiveTrainSize; i++) {
            value[i] = i;
        }

        for (int i = 0; i < (negativeIndexAll.length); i++) {
            value[i + mPositiveTrainSize] = negativeIndexAll[i];
        }
        return value;
    }

    private int[] getNegativeStrLearner(int[] values) {
        int[] retNegativeStr = new int[mNegativeTrainSize];
        for (int i = 0; i < mNegativeTrainSize; i++) {
            retNegativeStr[i] = values[i + mPositiveTrainSize];
        }
        return retNegativeStr;
    }

    private int[] addIndex(int[] values) {
        for (int i = 0; i < values.length; i++) {
            values[i] = values[i] + mPositiveTrainSize;
        }
        return values;
    }

    public void trainStrongClassifier() {
        for (int t = 0; t < mCascadeIteration; t++) {
            float minError, threshold, beta;
            int polarity, featureIndex;
            int[] classifyResult = new int[mTotalTrainSize];

            //Normalize the Weights
            imageWeight = normalizeWeights(imageWeight);

            //Select the weak classifier
            WeakLearner weakLearner = new WeakLearner();
            weakLearner.TrainWeakLearner(mPositiveTrainSize, mNegativeTrainSize,
                    mTotalTrainSize, imageWeight, labels, completeFeatures);

            //Get this values from the weak classifier;
            //Return values of getWeakClassifier;
            minError = weakLearner.getMinErr();
            polarity = weakLearner.getPolarity();
            threshold = weakLearner.getThreshold();
            featureIndex = weakLearner.getFeatInd();
            classifyResult = weakLearner.getBestResult();
//            classifyResult = weakClassifier[4][t];

            weakClassifier[0][t] = minError;
            weakClassifier[1][t] = polarity;
            weakClassifier[2][t] = threshold;
            weakClassifier[3][t] = featureIndex;

            setHTResult(t, classifyResult);

            //Set beta Value
            beta = minError / (1 - minError);
//            beta = (float) 0.5;

//            System.out.println(minError + " " + beta);
            //Set ei for updatting weights
            int[] compareLabels = comparedLabels(labels, classifyResult);

            //Update Weights
            imageWeight = updateWeights(beta, imageWeight, compareLabels);

            //Set alpha for weak Classifier t
            alpha[t] = (float) (Math.log(1 / beta));

            System.out.println("Classifier " + t + " minError " + weakClassifier[0][t]
                    + " Polarity " + weakClassifier[1][t]
                    + " threshold " + weakClassifier[2][t]
                    + " featureIndex " + weakClassifier[3][t]
                    + " alpha " + alpha[t]);

            //Store value as Polarity,threshold,featureIndex,alpha
            String content = (weakClassifier[1][t]
                    + "," + weakClassifier[2][t]
                    + " ," + weakClassifier[3][t]
                    + "," + alpha[t] + "\n");

            writeContent += content;

            //strongLearner
            float[] strLearner = strongLearner(t);

            //Strong classifier threshold
            float[] thresholdVal = getThresholdRange(strLearner);

            float thresholdStrong = getMinimumValue(thresholdVal);
//            System.out.println(thresholdStrong);

            setStrLearnerResult(strLearner, thresholdStrong);

            truePositiveRate[t] = (getSum(0, mPositiveTrainSize)) / mPositiveTrainSize;
            falsePositiveRate[t] = (getSum(mPositiveTrainSize, mTotalTrainSize)) / mNegativeTrainSize;

            if (truePositiveRate[t] == 1 && falsePositiveRate[t] <= 0.5) {
                break;
            }
        }

        try {
            FileWriter writer = new FileWriter(new File("classifier/Classifier" + mStage + ".txt"));
            writer.write(writeContent);
            writer.close();
        } catch (IOException e) {
        }

        //Use SortPlusIndex of StrongClassifier.util
        int[] negStr = getNegativeStrLearner(strLearner_result);
        SortPlusIndex sortPlusIndex = new SortPlusIndex(negStr);

        int[] temp = sortPlusIndex.getSortedArray();

        int[] nextNegativeIndex = (sortPlusIndex.getActualIndex());         //setIndex function may be used.

//        int[] nextNegativeIndex = convertIntegerToInt(nextNegativeInd);
//        int[] finalNegativeIndex = new int[mNegativeTrainSize];
        int neededSize = 0;
        for (int i = 0; i < mNegativeTrainSize; i++) {
            if (temp[i] > 0) {
                //need to set nextNegIndex              
                neededSize = mNegativeTrainSize - i;
//                System.out.println(neededSize);
                nextNegativeIndex = setNegativeInd(nextNegativeIndex, i);
                break;
            }

        }

        int[] finalNegativeIndex = new int[neededSize];
        System.arraycopy(nextNegativeIndex, 0, finalNegativeIndex, 0, neededSize);

        int[] nextIndexAll = setValueNextIndexAll(finalNegativeIndex);
        System.out.println(nextIndexAll.length);
        setNextIndexAll(nextIndexAll);
    }
}
