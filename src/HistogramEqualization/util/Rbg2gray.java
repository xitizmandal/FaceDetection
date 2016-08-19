/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package HistogramEqualization.util;

import java.awt.Color;

/**
 *
 * @author user
 */
public class Rbg2gray {

    public Color SettingColor(Color c) {

        int red = (int) (c.getRed() * 0.299);
        int green = (int) (c.getGreen() * 0.587);
        int blue = (int) (c.getBlue() * 0.114);

        Color newColor = new Color(red + green + blue, red + green + blue, red + green + blue);

        return newColor;
    }
}
