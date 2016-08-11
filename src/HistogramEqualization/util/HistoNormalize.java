/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package HistogramEqualization.util;

import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 *
 * @author Aditya Acharya
 */
public class HistoNormalize {

    public BufferedImage HistoNormal(BufferedImage bi) throws IOException {

        int numBands = bi.getRaster().getNumBands();
        int[] iarray = new int[numBands];
        float[] farray = new float[numBands];
//        String content = "";
        int count = 0;
        int height = bi.getHeight();
        int width = bi.getWidth();
        for (int i = 1; i < height; i++) {
            for (int j = 1; j < width; j++) {

                float value = bi.getRaster().getPixel(j, i, iarray)[0];
                value = value / 255;
                bi.getRaster().setPixel(j, i, iarray);
                count++;
            }
        }
        return bi;
    }
}
