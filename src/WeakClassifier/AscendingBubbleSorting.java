/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package WeakClassifier;

/**
 *
 * @author user
 */
public class AscendingBubbleSorting {

    public float[] Sort(float[] toSortFeature) {
        
        int lastSwap = toSortFeature.length - 1;
        int[] index = new int[toSortFeature.length];
        for (int i = 1; i < toSortFeature.length; i++) {
            boolean is_sorted = true;
            int currentSwap = -1;

            for (int j = 0; j < lastSwap; j++) {
                if (toSortFeature[j] > toSortFeature[j + 1]) {
                    float temp = toSortFeature[j];
                    toSortFeature[j] = toSortFeature[j + 1];
                    toSortFeature[j + 1] = temp;
                    
                    index[j] = j+1;
                    index[j+1] = j;
                    is_sorted = false;
                    currentSwap = j;
                }
            }

            if (is_sorted) {
                return toSortFeature;
            }

            lastSwap = currentSwap;
        }
        return toSortFeature;
    }

}
