/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package StrongClassifier.util;

import java.util.Arrays;

/**
 *
 * @author Kshitiz Mandal
 */
public class SortPlusIndex {

    private Integer[] indexes;
    private int[] sortedArray;
    private int[] integerToInt;

    public SortPlusIndex(int[] strLearner_result) {
        sortedArray = new int[strLearner_result.length];
        System.arraycopy(strLearner_result, 0, sortedArray, 0, strLearner_result.length);
        ArrayIndexComparator comparator = new ArrayIndexComparator(strLearner_result);
        indexes = comparator.createIndexArray();
        Arrays.sort(indexes, comparator);
        Arrays.sort(sortedArray);
        this.integerToInt = convertIntegerToInt(indexes);
    }

    public int[] getSortedArray() {
        return this.sortedArray;
    }

    public int[] getActualIndex() {
        return this.integerToInt;
    }
    
    private int[] convertIntegerToInt(Integer[] negativeInd){
        int[] integerToInt = new int[negativeInd.length];
        for (int i = 0; i < negativeInd.length; i++){
            integerToInt[i] = negativeInd[i];
        }
        return integerToInt;
    }
}
