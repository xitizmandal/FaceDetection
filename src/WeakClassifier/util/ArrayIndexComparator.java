/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package WeakClassifier.util;

import java.util.Comparator;

/**
 *
 * @author xitiz
 */
public class ArrayIndexComparator implements Comparator<Integer> {

    private final float[] array;

    public ArrayIndexComparator(float[] array) {
        this.array = array;
    }

    public Integer[] createIndexArray() {
        Integer[] indexes = new Integer[array.length];
        for (int i = 0; i < array.length; i++) {
            indexes[i] = i; // Autoboxing
        }
        return indexes;
    }

    @Override
    public int compare(Integer index1, Integer index2) {
        // Autounbox from Integer to int to use as array indexes
        if (array[index1] < array[index2]) {
            return -1;
        } else if (array[index1] == array[index2]) {
            return 0;
        } else {
            return 1;
        }
    }
}
