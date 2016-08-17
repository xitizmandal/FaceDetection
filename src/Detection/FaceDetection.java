/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Detection;

import Detection.util.TestFeatures;
import haarfilterall.features.Features;
import haarfilterall.util.IntegralImage;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 *
 * @author xitiz
 */
public class FaceDetection {

    private double[] mAlpha1;
    private double[] mAlpha2;
    private double[] mAlpha3;
    private double[] mAlpha4;
    private double[] mAlpha5;
    private double[] mAlpha6;
    private double[] mAlpha7;

    private double[] mThreshold1;
    private double[] mThreshold2;
    private double[] mThreshold3;
    private double[] mThreshold4;
    private double[] mThreshold5;
    private double[] mThreshold6;
    private double[] mThreshold7;

    private int[] mPolarity1;
    private int[] mPolarity2;
    private int[] mPolarity3;
    private int[] mPolarity4;
    private int[] mPolarity5;
    private int[] mPolarity6;
    private int[] mPolarity7;

    private int[] mFeatIndex1;
    private int[] mFeatIndex2;
    private int[] mFeatIndex3;
    private int[] mFeatIndex4;
    private int[] mFeatIndex5;
    private int[] mFeatIndex6;
    private int[] mFeatIndex7;

    private int[] mFeatureIndex;
    private double[] mFeatureValues;
    private BufferedImage mImage;
    int[][] values = new int[24][24];
    int[][] intValues = new int[24][24];
    int[] iarray = new int[3];
    float[] twoHoriFea = new float[36432];
    float[] twoVertFea = new float[36432];
    float[] threeHoriFea = new float[23184];
    float[] fourRectFea = new float[17424];
    IntegralImage integralImage = new IntegralImage();
    TestFeatures features = new TestFeatures();

    public FaceDetection() {

        mAlpha1 = new double[]{2.0406783, 1.418369, 1.2308724, 1.371354, 1.2978652, 1.3442482};
        mAlpha2 = new double[]{1.7876259, 1.4825087, 1.6518098, 1.4889871, 1.2165082, 1.1880406};
        mAlpha3 = new double[]{1.8105515, 1.7442106, 1.9821105, 1.7529082, 1.7011455};
        mAlpha4 = new double[]{2.021135, 1.8496009, 1.848422, 1.8616174, 1.8518932};
        mAlpha5 = new double[]{2.617807, 2.605634, 3.1019492};
        mAlpha6 = new double[]{3.0094762, 3.6691499};
        mAlpha7 = new double[]{4.7014728, 5.2422824};

        mThreshold1 = new double[]{-0.4834336, -0.17540586, 0.14879242, 0.030187074, 0.14558549, -0.14034668};
        mThreshold2 = new double[]{-0.52440226, -0.099887215, -0.13420328, 0.100752845, -0.15084586, 0};
        mThreshold3 = new double[]{0.1113808, 0.06998212, 0.098518625, -0.15698205, -0.009828069};
        mThreshold4 = new double[]{-0.14213565, 0.08855698, -0.024004145, 0.14911048, -0.4375257};
        mThreshold5 = new double[]{0, 0.09286874, -0.13376644};
        mThreshold6 = new double[]{0.09286874, -0.46360576};
        mThreshold7 = new double[]{0.13070607, -0.26780456};

        mPolarity1 = new int[]{1, 1, -1, -1, -1, 1};
        mPolarity2 = new int[]{1, 1, 1, -1, 1, 1};
        mPolarity3 = new int[]{-1, -1, -1, 1, 1};
        mPolarity4 = new int[]{1, -1, 1, -1, -1};
        mPolarity5 = new int[]{1, -1, -1};
        mPolarity6 = new int[]{-1, -1};
        mPolarity7 = new int[]{1, -1};

        int A = 36432;
        int B = 72864;
        int C = 96048;

        mFeatIndex1 = new int[]{86804 - B, 106750 - C, 100982 - C, 9576, 45885 - A, 43968 - A};

        mFeatIndex2 = new int[]{86434 - B, 49336 - A, 45940 - A, 11723 , 104654 - C, 473};
        mFeatIndex3 = new int[]{49179 - A, 49765 - A, 10556, 53734 - A, 43967 - A};
        mFeatIndex4 = new int[]{54068 - A, 7103, 106977 - C, 50518 - A, 83445 - B};
        mFeatIndex5 = new int[]{472, 8636, 12542};
        mFeatIndex6 = new int[]{8636, 95193 - B};
        mFeatIndex7 = new int[]{53822 - A, 98002 - C};

//        mFeatureIndex = new int[]{9243, 45139, 61128, 0, 15817, 50027};
        startDetection();
    }

    public void startDetection() {
        BufferedImage img = null;
        try {
            img = ImageIO.read(new File("testImages/test3.png"));
            System.out.println(img.getHeight() + " " + img.getWidth());
        } catch (Exception e) {
        }
        int count = 0;
        Classifier strongClassifierOne = new Classifier(mAlpha1, mPolarity1, mThreshold1);
        Classifier strongClassifierTwo = new Classifier(mAlpha2, mPolarity2, mThreshold2);
        Classifier strongClassifierThree = new Classifier(mAlpha3, mPolarity3, mThreshold3);
        Classifier strongClassifierFour = new Classifier(mAlpha4, mPolarity4, mThreshold4);
        Classifier strongClassifierFive = new Classifier(mAlpha5, mPolarity5, mThreshold5);
        Classifier strongClassifierSix = new Classifier(mAlpha6, mPolarity6, mThreshold6);
        Classifier strongClassifierSeven = new Classifier(mAlpha7, mPolarity7, mThreshold7);
        for (int i = 0; i <= (img.getHeight() - 24); i++) {                     //For a 24x24 Pixel image define height such that the last image to be extracted has atleast 24 Pixels to work with
            int subCount = 0;
            for (int j = 0; j <= (img.getWidth() - 24); j++) {                  //*For a 24x24 Pixel image define width such that the last image to be extracted has atleast 24 Pixels to work with

                BufferedImage sub = img.getSubimage(j, i, 24, 24);              //sub image starting point defined by i and j & 24x4 is the needed size for the sub image.             

                for (int k = 0; k < 24; k++) {
                    for (int l = 0; l < 24; l++) {
                        values[k][l] = sub.getRaster().getPixel(k, l, iarray)[0];
                    }
                }

                intValues = integralImage.Integral(values);
                twoHoriFea = features.FeatureA(2, 1, intValues);
                twoVertFea = features.FeatureB(1, 2, intValues);
                threeHoriFea = features.FeatureC(3, 1, intValues);
                fourRectFea = features.FeatureE(2, 2, intValues);

                double[] sendFeature1 = {threeHoriFea[mFeatIndex1[0]], fourRectFea[mFeatIndex1[1]],
                    fourRectFea[mFeatIndex1[2]], twoHoriFea[mFeatIndex1[3]], twoVertFea[mFeatIndex1[4]], twoVertFea[mFeatIndex1[5]]};

                double[] sendFeature2 = {threeHoriFea[mFeatIndex2[0]], twoVertFea[mFeatIndex2[1]], twoVertFea[mFeatIndex2[2]],
                    twoHoriFea[mFeatIndex2[3]], fourRectFea[mFeatIndex2[4]], twoHoriFea[mFeatIndex2[5]]};

                double[] sendFeature3 = {twoVertFea[mFeatIndex3[0]], twoVertFea[mFeatIndex3[1]], twoHoriFea[mFeatIndex3[2]],
                    twoVertFea[mFeatIndex3[3]], twoVertFea[mFeatIndex3[4]]};

                double[] sendFeature4 = {twoVertFea[mFeatIndex4[0]], twoHoriFea[mFeatIndex4[1]], fourRectFea[mFeatIndex4[2]],
                    twoVertFea[mFeatIndex4[3]], threeHoriFea[mFeatIndex4[4]]};

                double[] sendFeature5 = {twoHoriFea[mFeatIndex5[0]], twoHoriFea[mFeatIndex5[1]], twoHoriFea[mFeatIndex5[2]]};
                double[] sendFeature6 = {twoHoriFea[mFeatIndex6[0]], threeHoriFea[mFeatIndex6[1]]
                };
                double[] sendFeature7 = {twoVertFea[mFeatIndex7[0]], fourRectFea[mFeatIndex7[1]]
                };

                strongClassifierOne.setFeatures(sendFeature1);
                strongClassifierTwo.setFeatures(sendFeature2);
                strongClassifierThree.setFeatures(sendFeature3);
                strongClassifierFour.setFeatures(sendFeature4);
                strongClassifierFive.setFeatures(sendFeature5);
                strongClassifierSix.setFeatures(sendFeature6);
                strongClassifierSeven.setFeatures(sendFeature7);

                int retVal = strongClassifierOne.strongClassifierFunction();
//                System.out.println(count + " " + subCount +" " + + retVal);

                try {
                    if (retVal == 1) {
                        if (strongClassifierTwo.strongClassifierFunction() == 1) {
                            if (strongClassifierThree.strongClassifierFunction() == 1) {
                                if (strongClassifierFour.strongClassifierFunction() == 1) {
                                    if (strongClassifierFive.strongClassifierFunction() == 1) {
                                        if (strongClassifierSix.strongClassifierFunction() == 1) {
                                            if (strongClassifierSeven.strongClassifierFunction() == 1) {
                                                Graphics2D graph = img.createGraphics();
                                                Color transparent = new Color(255, 0, 0, 100);
                                                graph.setColor(transparent);
                                                graph.fill(new Rectangle(j, i, 24, 24));
                                                Color transparent2 = new Color(0, 255, 0, 100);
                                                graph.setColor(transparent2);
                                                graph.fill(new Rectangle(j + 1, i + 1, 22, 22));
                                                graph.dispose();
                        System.out.println(j + "_"+ i+ " Bhitra");
                                                File ouptut = new File("detect/detected" + j + "_" + i + ".png");
                                                ImageIO.write(img, "png", ouptut);
                                            }
                                        }
                                    }
                                }
                            }

                        }
                    }
                } catch (Exception e) {
//                     System.out.println("Bahira");
                }
//                System.out.println(count);
                subCount++;

            }
            count++;
        }

//        Classifier strongClassifier = new Classifier();
//        if (classifier)
    }
}
