/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Detection.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 *
 * @author xitiz
 */
public class DataReader {

    private String mFileName;
    private int mNumberOfLines;
    private int[] mPolarity;
    private float[] mThreshold;
    private int[] mFeatureIndex;
    private float[] mAlpha;

    public DataReader(String fileName) {
        this.mFileName = fileName;
        this.mNumberOfLines = numberOfLines();
        this.mPolarity = new int[mNumberOfLines];
        this.mThreshold = new float[mNumberOfLines];
        this.mFeatureIndex = new int[mNumberOfLines];
        this.mAlpha = new float[mNumberOfLines];
        readData();
    }

    private void readData() {
        try {
            BufferedReader bufferReader = new BufferedReader(new FileReader(mFileName));
            String str;
            int indexCount = 0;
            while ((str = bufferReader.readLine()) != null) {
                String[] ar = str.split(",");
                mPolarity[indexCount] = (int) Double.parseDouble(ar[0]);
                mThreshold[indexCount] = (float) Double.parseDouble(ar[1]);
                mFeatureIndex[indexCount] = (int) Double.parseDouble(ar[2]);
                mAlpha[indexCount] = (float) Double.parseDouble(ar[3]);
                indexCount++;
            }
            bufferReader.close();
        } catch (IOException e) {
            System.out.println("File Read Error");
        }
    }

    public int numberOfLines() {
        int numberOfLines = 1;
        try {
            BufferedReader bufferReader = new BufferedReader(new FileReader(mFileName));
            while ((bufferReader.readLine()) != null) {
                numberOfLines++;
            }
            bufferReader.close();
        } catch (IOException e) {
            System.out.println("File Read Error");
        }
        System.out.println(numberOfLines);
        return numberOfLines;
    }

    public float[] getAlpha() {
        return mAlpha;
    }

    public int[] getFeatureIndex() {
        return mFeatureIndex;
    }

    public int[] getPolarity() {
        return mPolarity;
    }

    public float[] getThreshold() {
        return mThreshold;
    }

}
