/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package haarfilterall.util;

/**
 *
 * @author Aditya Acharya
 */
public class IntegralImage {

    public int[][] Integral(int[][] values) {
        int[][] intValue = new int[24][24];
        for (int i = 0; i < 24; i++) {                                          //WIDTH
            int sum = 0;
            for (int j = 0; j < 24; j++) {                                      //HEIGHT
                sum += values[i][j];                                            //Add the current location value
                if (i == 0) {
                    intValue[i][j] = sum;                                       //if the location is left corner then just the sum of values on top of it
                } else {
                    intValue[i][j] = intValue[i - 1][j] + sum;                  //else add itself, the sum accumulated and the pixel left of the current position.
                }
            }
        }
        return intValue;
    }
}
