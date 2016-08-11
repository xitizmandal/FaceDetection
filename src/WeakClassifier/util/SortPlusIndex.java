/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package WeakClassifier.util;

import java.util.Arrays;

/**
 *
 * @author Kshitiz Mandal
 */
public class SortPlusIndex {

    private Integer[] indexes;
    private float[] sortedArray;

    public SortPlusIndex(float[] featureValue) {
        sortedArray = new float[featureValue.length];
        System.arraycopy(featureValue, 0, sortedArray, 0, featureValue.length);
        ArrayIndexComparator comparator = new ArrayIndexComparator(featureValue);
        indexes = comparator.createIndexArray();
        Arrays.sort(indexes, comparator);
        Arrays.sort(sortedArray);
    }

    public float[] getSortedArray() {
        return this.sortedArray;
    }

    public Integer[] getActualIndex() {
        return this.indexes;
    }
}
