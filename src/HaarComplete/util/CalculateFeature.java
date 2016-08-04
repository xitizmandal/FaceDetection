/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package haarfilterall.util;

/**
 *
 * @author user
 */
public class CalculateFeature {

    /**
     * Two layer Horizontal Sliding Window Feature Calculation
     */
    public int CalculateSubtractionA(int winWidth, int winHeight, int sliderPosCol, int sliderPosRow, int count, int[][] intValues) {

        CalculateArea calculateArea = new CalculateArea();
        int blackPortion = 0, whitePortion = 0;

        int a = winWidth;
        int b = winHeight;
        int c = sliderPosCol;
        int d = sliderPosRow;
//        int counter = count;
        int featureValues;

        /**
         * Ignoring extreme left and extreme top values as they hold little
         * information when this is applied to image processing applications Get
         * the areas of the black and white portions of the filter and subtract
         * to find the features. for the white portion the window height is
         * added to the row section to shift the starting position of the window
         * to be subtracted
         */
        whitePortion = calculateArea.PointsCalculation(a, b, c, d, intValues);
        blackPortion = calculateArea.PointsCalculation(a, b, c + winWidth, d, intValues);

        featureValues = blackPortion - whitePortion;
//        System.out.println(" ");
//        System.out.println("FEATURE VALUE = " + featureValues);

        /**
         * Ultimately another portion will be chosen with respect to the above
         * window and then their values will be subtracted to get the actual
         * features of the image This is one HAAR like filter, at least two more
         * might be needed for proper recognition
         */
        return featureValues;
    }

    /**
     * Two layer Vertical Sliding Window Feature Calculation
     */
    public int CalculateSubtractionB(int winWidth, int winHeight, int sliderPosCol, int sliderPosRow, int count, int[][] intValues) {

        CalculateArea calculateArea = new CalculateArea();
        int blackPortion = 0, whitePortion = 0;

        int a = winWidth;
        int b = winHeight;
        int c = sliderPosCol;
        int d = sliderPosRow;
        int featureValues;

        blackPortion = calculateArea.PointsCalculation(a, b, c, d, intValues);
        whitePortion = calculateArea.PointsCalculation(a, b, c, d + winHeight, intValues);

        featureValues = blackPortion - whitePortion;

        return featureValues;
    }

    /**
     * Three layer Horizontal Sliding Window Feature Calculation
     */
    public int CalculateSubtractionC(int winWidth, int winHeight, int sliderPosCol, int sliderPosRow, int count, int[][] intValues) {

        CalculateArea calculateArea = new CalculateArea();
        int blackPortion = 0, whitePortion1 = 0, whitePortion2 = 0;

        int a = winWidth;
        int b = winHeight;
        int c = sliderPosCol;
        int d = sliderPosRow;
        int featureValues;

        whitePortion1 = calculateArea.PointsCalculation(a, b, c, d, intValues);
        blackPortion = calculateArea.PointsCalculation(a, b, c + winWidth, d, intValues);
        whitePortion2 = calculateArea.PointsCalculation(a, b, c + (2 * winWidth), d, intValues);
        featureValues = blackPortion - (whitePortion1 + whitePortion2);

        return featureValues;
    }

    /**
     * Four layer Rectangular Sliding Window Feature Calculation
     */
    public int CalculateSubtractionE(int winWidth, int winHeight, int sliderPosCol, int sliderPosRow, int count, int[][] intValues) {

        CalculateArea calculateArea = new CalculateArea();
        int blackPortionA = 0, whitePortionA = 0, blackPortionB = 0, whitePortionB = 0;

        int a = winWidth;
        int b = winHeight;
        int c = sliderPosCol;
        int d = sliderPosRow;
        int featureValues;

        whitePortionA = calculateArea.PointsCalculation(a, b, c, d, intValues);
        blackPortionA = calculateArea.PointsCalculation(a, b, c + winWidth, d, intValues);

        whitePortionB = calculateArea.PointsCalculation(a, b, c + winWidth, d + winHeight, intValues);
        blackPortionB = calculateArea.PointsCalculation(a, b, c, d + winHeight, intValues);

        featureValues = (blackPortionA + blackPortionB) - (whitePortionA + whitePortionB);

        return featureValues;
    }
}
