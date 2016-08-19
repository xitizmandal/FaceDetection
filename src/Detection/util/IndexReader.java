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
public class IndexReader {

    private String mFileName;
    private int[] mWidth;
    private int[] mHeight;
    private int[] mXpos;
    private int[] mYpos;
    private int[] mAlpha;
    private int mNumberOfLines;

    public IndexReader(String fileName) {
        this.mFileName = fileName;
        this.mNumberOfLines = numberOfLines();
        this.mWidth = new int[mNumberOfLines];
        this.mHeight = new int[mNumberOfLines];
        this.mXpos = new int[mNumberOfLines];
        this.mYpos = new int[mNumberOfLines];
        readData();
    }

    private void readData() {
        try {
            BufferedReader bufferReader = new BufferedReader(new FileReader(mFileName));
            String str;
            int indexCount = 0;
//            bufferReader.readLine();
            while ((str = bufferReader.readLine()) != null) {
                String[] ar = str.split(",");
                mWidth[indexCount] = ((int) Double.parseDouble(ar[0]));
                mHeight[indexCount] = ((int) Double.parseDouble(ar[1]));
                mXpos[indexCount] = ((int) Double.parseDouble(ar[2]));
                mYpos[indexCount] = ((int) Double.parseDouble(ar[3]));
                indexCount++;
//                bufferReader.readLine();
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
        return numberOfLines;
    }

    public int[] getHeight() {
        return mHeight;
    }

    public int[] getYpos() {
        return mYpos;
    }

    public int[] getXpos() {
        return mXpos;
    }

    public int[] getWidth() {
        return mWidth;
    }

}
