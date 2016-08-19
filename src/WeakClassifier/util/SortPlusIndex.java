/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package WeakClassifier.util;

import java.util.Arrays;

/**
 *
 * @author xitiz
 */
public class SortPlusIndex {

    private Integer[] indexes;
    private int[] intIndexes;
    private float[] sortedArray;

    public SortPlusIndex(float[] featureValue) {
        sortedArray = new float[featureValue.length];
        System.arraycopy(featureValue, 0, sortedArray, 0, featureValue.length);
        ArrayIndexComparator comparator = new ArrayIndexComparator(featureValue);
        indexes = comparator.createIndexArray();
        Arrays.sort(indexes, comparator);
        Arrays.sort(sortedArray);
        intIndexes = ConvertIntegerToInt(indexes);
    }

    public float[] getSortedArray() {
        return this.sortedArray;
    }

    public int[] getActualIndex() {
        return this.intIndexes;
    }

    private int[] ConvertIntegerToInt(Integer[] actualIndex) {
        int[] toInt = new int[actualIndex.length];
        for (int i = 0; i < actualIndex.length; i++) {
            toInt[i] = actualIndex[i];
        }
        return toInt;
    }
}
