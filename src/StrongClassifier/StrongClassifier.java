/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package StrongClassifier;
import WeakClassifier.WeakLearner;
import java.lang.Math;

/**
 *
 * @author xitiz
 */
public class StrongClassifier {
    private int mPositiveTrainSize = 178;
    private int mNegativeTrainSize;
    private int mNoOfFeatures = 113472;         //For Our image set
    private int mTotalTrainSize;//noTrain
    private int mCascadeIteration;
    private int mStage;
    private int[] mIndicesAll;
    private int[] mNextIndicesAll;
    private float[] imageWeight;
    private int[][] weakClassifier;
    private float[][] ht;
    private float[] alpha;                      //weight factors for weak classifiers
    private float[][] ht_result;                //Classification Result for t th weak classifier; 1 for +ve; 0 for -ve
    private int[] strLearner_result;
    private float[] truePositiveRate;
    private float[] falsePositiveRate;
    private float[][] completeFeatures;
    private int[] labels;
    
    public StrongClassifier(int CascadeIteration, float[][] completeFeatures,  int[] indicesAll, int stage){   
        this.mCascadeIteration = CascadeIteration;
        this.completeFeatures = completeFeatures;
        this.mIndicesAll = indicesAll;
        this.mStage = stage;
        this.mNegativeTrainSize = indicesAll.length - mPositiveTrainSize;
        this.mTotalTrainSize = mPositiveTrainSize + mNegativeTrainSize;
        initializeFeatures();
        initializeWeights(mTotalTrainSize);
    }
    
    public int[] nextIndexAll(){
        return mNextIndicesAll;       
    }
    
    private void initializeFeatures(){
        
    }
    
    private void initializeWeights(int totalTrainSize){
        for(int i = 0; i < totalTrainSize;i++){
            if(i<mPositiveTrainSize){
                imageWeight[i] = (float) (0.5/mPositiveTrainSize);
            } else {
                imageWeight[i] = (float) (0.5/mNegativeTrainSize);
            }
        }
    }
    
    public float[] normalizeWeights(float[] weights){
        float totalWeightSum = weightSum(weights);
        float[] normalizedWeight = new float[weights.length];
        
        for (int i = 0; i < weights.length;i++){
            normalizedWeight[i] = weights[i]/totalWeightSum;
        }
        return normalizedWeight;
    }
    
    public float weightSum(float[] weights){
        float totalSum = 0;
        for (int i = 0; i < (weights.length); i++){
            totalSum += weights[i];
        }
        return totalSum;
    }
    
    public float[] updateWeights(float beta, float[] weights){
        float temp;
        for(int i = 0; i< weights.length;i++){
            temp = (float) Math.pow(beta,1);                //TODO change value 1
            weights[i] = weights[i] * temp;
        }
        return weights;
    }
    
    public void setHTResult(int t, int[] classification){
        for(int i = 0; i < classification.length;i++){
            ht_result[i][t] = classification[i];
        }
    }
    
    //TODO function is wrong correct it.
    public float[] strongLearner(int t){
        float[] strLearner = new float[ht_result.length];
        float sum = 0;
        for (int i = 0;i < ht_result.length; i++){
            for(int j = 0; j < t; j++){
                sum += ht_result[i][j] * alpha[j];
            }
            strLearner[i] = sum;
            sum = 0;
        }
        return strLearner;
    }
    
    public float[] getThresholdRange(float[] values){
        float[] thresholdRange = new float[mPositiveTrainSize];
        System.arraycopy(values, 0, thresholdRange, 0, mPositiveTrainSize);
        return thresholdRange;
    }
    
    public float getMinimumValue(float[] values){
        float minimumValue = 1;                 //max value of threshold is 1
        for (int i = 0; i < values.length; i++){
            if (values[i] < minimumValue){
                minimumValue = values[i];
            }
        }
        return minimumValue;
    }
    
    public float getSum(int startIndex, int endIndex){
        float sum = 0;
        for(int i = startIndex; i < endIndex; i++){
            sum += strLearner_result[i];
        }
        return sum;
    }
    
    //For sorting negative test images only
    public int[] sortStrongListener(){
        int tempIndex = strLearner_result.length - mPositiveTrainSize;
        int[] temp = new int[tempIndex];
        
        System.arraycopy(strLearner_result, mPositiveTrainSize, temp, 0, tempIndex);
        
        int tempValue;              //Our comparision value is either 0 or 1
        for(int i=1;i < temp.length; i++){
            for(int j = 0; j < temp.length - i ; j++){
                if(temp[j] < temp[j+1]){
                    tempValue = temp[j];
                    temp[j] = temp[j+1];
                    temp[j+1] = tempValue;
                }
            }
        }
        return temp;
    }
    
    public void trainStrongClassifier(){
        for(int t = 0 ;t < mCascadeIteration; t++){
            float minError, threshold, beta;
            int polarity, featureIndex;
            int[] classifyResult = new int[mPositiveTrainSize];
            //Normalize the Weights
            imageWeight = normalizeWeights(imageWeight);
            
            //Select the weak classifier
            WeakLearner weakLearner = new WeakLearner();
            weakLearner.TrainWeakLearner(mPositiveTrainSize, mNegativeTrainSize,
                    mTotalTrainSize, imageWeight, labels, completeFeatures);
            //Get this values from the weak classifier;
            //Return values of getWeakClassifier;
            minError = weakClassifier[0][t];
            polarity = weakClassifier[1][t];
            threshold = weakClassifier[2][t];
            featureIndex = weakClassifier[3][t];
//            classifyResult = weakClassifier[4][t];
            setHTResult(t,classifyResult);
            //Set beta Value
            beta = minError/(1 - minError);
            
            //Update Weights
            imageWeight = updateWeights(beta, imageWeight);
            
            //Set alpha for weak Classifier t
            alpha[t] = (float) (Math.log(1/beta));
            
            //strongLearner
            float[] strLearner = strongLearner(t);
            
            //Strong classifier threshold
            float thresholdStrong = getMinimumValue(getThresholdRange(strLearner));
            
            for (int i = 0; i<mTotalTrainSize;i++){
                if(strLearner[i] >= thresholdStrong){
                    strLearner_result[i] = 1;
                } else {
                    strLearner_result[i] = 0;
                }
            }
            
            truePositiveRate[t] = (getSum(0,mPositiveTrainSize))/mPositiveTrainSize;
            falsePositiveRate[t] = (getSum(mPositiveTrainSize,mTotalTrainSize))/mNegativeTrainSize;
            
            if (truePositiveRate[t] == 1 && falsePositiveRate[t] <= 0.5){
                break;
            }
            
            int [] tempNextInd = sortStrongListener();
            
            
        }
        
        
    }    
}
