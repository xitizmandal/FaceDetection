/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package HistogramEqualization.impl;

import HistogramEqualization.util.Rbg2gray;
import java.awt.Color;
import java.awt.image.BufferedImage;

/**
 *
 * @author Aditya Acharya
 */
public class HistEq {

    public BufferedImage Change(BufferedImage bi) {

        //all required parameters declaration
        int width = bi.getWidth();
        int height = bi.getHeight();
        int anzpixel = width * height;
        int[] histogram = new int[255];                                         //to store intensity values from supplied image
        int i = 0;
        int sum = 0;

        /*
         *
         **RBG TO GRAY CONVERSION
         *
         */
        Rbg2gray rbg2gray = new Rbg2gray();
        for (i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                Color c = new Color(bi.getRGB(j, i));
                Color newColor = rbg2gray.SettingColor(c);                      //Get new color values from Rbg2gray.java    
                bi.setRGB(j, i, newColor.getRGB());                             //set GRAY values to the image
            }
        }

        /*
         *
         **HISTOGRAM EQUALIZATION SECTION
         *
         */
        int numBands = bi.getRaster().getNumBands();

        int[] iarray = new int[numBands];

        //read pixel intensities into histogram
        for (int x = 1; x < width; x++) {
            for (int y = 1; y < height; y++) {
                int valueBefore = bi.getRaster().getPixel(x, y, iarray)[0];
                histogram[valueBefore]++;
            }
        }

        //calculate min frequency of occurance
        int minFrequencyCount = anzpixel;
        for (int k = 0; k < 255; k++) {
            if (histogram[k] < minFrequencyCount && histogram[k] != 0) {
                minFrequencyCount = histogram[k];
            }
        }

        // build a Lookup table LUT containing scale factor
        float[] lut = new float[anzpixel];

        for (i = 0; i < 255; ++i) {
            sum += histogram[i];
            lut[i] = ((sum - minFrequencyCount) * 255) / (anzpixel - minFrequencyCount);
        }

        // transform image using sum histogram as a Lookup table
        i = 0;
        for (int x = 1; x < width; x++) {
            for (int y = 1; y < height; y++) {
                int valueBefore = bi.getRaster().getPixel(x, y, iarray)[0];
                int valueAfter = (int) lut[valueBefore];
                iarray[0] = valueAfter;
//                iarray[1] = valueAfter;
//                iarray[2] = valueAfter;
                bi.getRaster().setPixel(x, y, iarray);
                i = i + 1;
            }
        }
        return bi;                                                              //return buffered image to main
    }
}
