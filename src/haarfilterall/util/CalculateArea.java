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
public class CalculateArea {

    public int PointsCalculation(int winWidth, int winHeight, int sliderPosCol, int sliderPosRow, int[][] integralValues) {

        int btmRgtX, btmRgtY, btmLftX, btmLftY, topRgtX, topRgtY, topLftX, topLftY, a, b, c, d;
        int featureValues, totPixel;

        /**
         * My window width value is the 'i' from features which starts from 1
         * hence, -1 for adjusting width since for window width 1, the window
         * itself should be considered or for window width 2 only one step to
         * move Another -1 since the integral values array starts form 0 - the
         * desired value so subtraction of one more one to chose the desired
         * value
         */
        btmRgtX = sliderPosCol + winWidth - 2;
        btmRgtY = sliderPosRow + winHeight - 2;

        btmLftX = sliderPosCol - 2;
        btmLftY = sliderPosRow + winHeight - 2;

        topRgtX = sliderPosCol + winWidth - 2;
        topRgtY = sliderPosRow - 2;

        topLftX = sliderPosCol - 2;
        topLftY = sliderPosRow - 2;

        a = integralValues[btmRgtY][btmRgtX];

        b = integralValues[topLftY][topLftX];

        c = integralValues[btmLftY][btmLftX];

        d = integralValues[topRgtY][topRgtX];

        totPixel = winHeight * winWidth;

        featureValues = ((a + b) - (c + d)) / totPixel;

        return featureValues;
    }

}
