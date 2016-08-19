/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CustomDataStructure;

/**
 *
 * @author xitiz
 */
public class IndexData {

    private int[] mWidth;
    private int[] mHeight;
    private int[] mXpos;
    private int[] mYpos;

    public IndexData(int[] width, int[] height, int[] xPos, int[] yPos) {
        this.mWidth = width;
        this.mHeight = height;
        this.mXpos = xPos;
        this.mYpos = yPos;
    }

    public int[] getHeight() {
        return mHeight;
    }

    public int[] getWidth() {
        return mWidth;
    }

    public int[] getXpos() {
        return mXpos;
    }

    public int[] getYpos() {
        return mYpos;
    }
}
