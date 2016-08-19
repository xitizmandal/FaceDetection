/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Detection;

import CustomDataStructure.ClassifierData;
import Detection.util.DataReader;
import Detection.util.TestFeatures;
import haarfilterall.features.Features;
import haarfilterall.util.IntegralImage;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;

/**
 *
 * @author xitiz
 */
public class FaceDetection {

    private int[] mFeatIndex1;
    private int[] mFeatIndex2;
    private int[] mFeatIndex3;
    private int[] mFeatIndex4;
    private int[] mFeatIndex5;
    private int[] mFeatIndex6;
    private int[] mFeatIndex7;

    int mNumberOfClassifiers = 8;
//    private ArrayList<ClassifierData>[] mClassifierData = new ArrayList[mNumberOfClassifiers];
    private ArrayList<ClassifierData> mClassifierData = new ArrayList();
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
    int firstFilterRange = 36432;
    int secondFilterRange = 72864;
    int thirdFilterRange = 96048;

    public FaceDetection() {
//
//        mFeatIndex1 = new int[]{86804 - B, 106750 - C, 100982 - C, 9576, 45885 - A, 43968 - A};
//
//        mFeatIndex2 = new int[]{86434 - B, 49336 - A, 45940 - A, 11723, 104654 - C, 473};
//        mFeatIndex3 = new int[]{49179 - A, 49765 - A, 10556, 53734 - A, 43967 - A};
//        mFeatIndex4 = new int[]{54068 - A, 7103, 106977 - C, 50518 - A, 83445 - B};
//        mFeatIndex5 = new int[]{472, 8636, 12542};
//        mFeatIndex6 = new int[]{8636, 95193 - B};
//        mFeatIndex7 = new int[]{53822 - A, 98002 - C};

        //Must initialize the arrayList
        for (int i = 0; i < mNumberOfClassifiers; i++) {
//            mClassifierData[i] = new ArrayList();
            DataReader dataReader = new DataReader("classifier/Classifier" + (i + 1) + ".txt");
            ClassifierData data = new ClassifierData(dataReader.getPolarity(),
                    dataReader.getThreshold(),
                    dataReader.getFeatureIndex(),
                    dataReader.getAlpha());
            mClassifierData.add(data);
//            System.out.println(mClassifierData.get(i).getFeatureIndex()[1]);
        }

//        mFeatureIndex = new int[]{9243, 45139, 61128, 0, 15817, 50027};
        startDetection();
    }

    public void startDetection() {
        BufferedImage img = null;
        BufferedImage writeImg = null;
        try {
            img = ImageIO.read(new File("testImages/test.png"));
            writeImg = ImageIO.read(new File("testImages/test.png"));
            System.out.println(img.getHeight() + " " + img.getWidth());
        } catch (Exception e) {
        }
        int count = 0;
        Classifier[] strongClassifier = new Classifier[mNumberOfClassifiers];
        for (int i = 0; i < mNumberOfClassifiers; i++) {
            strongClassifier[i] = new Classifier(mClassifierData.get(i).getAlpha(),
                    mClassifierData.get(i).getPolarity(),
                    mClassifierData.get(i).getThreshold());
        }

//        Classifier strongClassifierOne = new Classifier(mClassifierData.get(0).getAlpha(),
//                mClassifierData.get(0).getPolarity(),
//                mClassifierData.get(0).getThreshold());
//        Classifier strongClassifierTwo = new Classifier(mClassifierData.get(1).getAlpha(),
//                mClassifierData.get(1).getPolarity(),
//                mClassifierData.get(1).getThreshold());
//        Classifier strongClassifierThree = new Classifier(mClassifierData.get(2).getAlpha(),
//                mClassifierData.get(2).getPolarity(),
//                mClassifierData.get(2).getThreshold());
//        Classifier strongClassifierFour = new Classifier(mClassifierData.get(3).getAlpha(),
//                mClassifierData.get(3).getPolarity(),
//                mClassifierData.get(3).getThreshold());
//        Classifier strongClassifierFive = new Classifier(mClassifierData.get(4).getAlpha(),
//                mClassifierData.get(4).getPolarity(),
//                mClassifierData.get(4).getThreshold());
//        Classifier strongClassifierSix = new Classifier(mClassifierData.get(5).getAlpha(),
//                mClassifierData.get(5).getPolarity(),
//                mClassifierData.get(5).getThreshold());
//        Classifier strongClassifierSeven = new Classifier(mClassifierData.get(6).getAlpha(),
//                mClassifierData.get(6).getPolarity(),
//                mClassifierData.get(6).getThreshold());
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

                for (int itr = 0; itr < mNumberOfClassifiers; itr++) {
                    int featureIndexLength = mClassifierData.get(itr).getFeatureIndex().length;
                    double[] sendFeature = new double[featureIndexLength];
                    for (int k = 0; k < featureIndexLength; k++) {
                        int featureIndexValue = mClassifierData.get(itr).getFeatureIndex()[k];
                        if (featureIndexValue < firstFilterRange) {
                            sendFeature[k] = twoHoriFea[featureIndexValue];
                        } else if (featureIndexValue < secondFilterRange) {
                            sendFeature[k] = twoVertFea[featureIndexValue - firstFilterRange];
                        } else if (featureIndexValue < thirdFilterRange) {
                            sendFeature[k] = threeHoriFea[featureIndexValue - secondFilterRange];
                        } else {
                            sendFeature[k] = twoHoriFea[featureIndexValue - thirdFilterRange];
                        }
                    }
                    strongClassifier[itr].setFeatures(sendFeature);
                }

//                double[] sendFeature1 = {threeHoriFea[mFeatIndex1[0]], fourRectFea[mFeatIndex1[1]],
//                    fourRectFea[mFeatIndex1[2]], twoHoriFea[mFeatIndex1[3]], twoVertFea[mFeatIndex1[4]], twoVertFea[mFeatIndex1[5]]};
//
//                double[] sendFeature2 = {threeHoriFea[mFeatIndex2[0]], twoVertFea[mFeatIndex2[1]], twoVertFea[mFeatIndex2[2]],
//                    twoHoriFea[mFeatIndex2[3]], fourRectFea[mFeatIndex2[4]], twoHoriFea[mFeatIndex2[5]]};
//
//                double[] sendFeature3 = {twoVertFea[mFeatIndex3[0]], twoVertFea[mFeatIndex3[1]], twoHoriFea[mFeatIndex3[2]],
//                    twoVertFea[mFeatIndex3[3]], twoVertFea[mFeatIndex3[4]]};
//
//                double[] sendFeature4 = {twoVertFea[mFeatIndex4[0]], twoHoriFea[mFeatIndex4[1]], fourRectFea[mFeatIndex4[2]],
//                    twoVertFea[mFeatIndex4[3]], threeHoriFea[mFeatIndex4[4]]};
//
//                double[] sendFeature5 = {twoHoriFea[mFeatIndex5[0]], twoHoriFea[mFeatIndex5[1]], twoHoriFea[mFeatIndex5[2]]};
//                double[] sendFeature6 = {twoHoriFea[mFeatIndex6[0]], threeHoriFea[mFeatIndex6[1]]
//                };
//                double[] sendFeature7 = {twoVertFea[mFeatIndex7[0]], fourRectFea[mFeatIndex7[1]]
//                };
//
//                strongClassifierOne.setFeatures(sendFeature);
//                strongClassifierTwo.setFeatures(sendFeature2);
//                strongClassifierThree.setFeatures(sendFeature3);
//                strongClassifierFour.setFeatures(sendFeature4);
//                strongClassifierFive.setFeatures(sendFeature5);
//                strongClassifierSix.setFeatures(sendFeature6);
//                strongClassifierSeven.setFeatures(sendFeature7);
//                int retVal = strongClassifierOne.strongClassifierFunction();
//                System.out.println(count + " " + subCount +" " + + retVal);
                try {
                    if (strongClassifier[0].strongClassifierFunction() == 1) {
                        if (strongClassifier[1].strongClassifierFunction() == 1) {
                            if (strongClassifier[2].strongClassifierFunction() == 1) {
                                if (strongClassifier[3].strongClassifierFunction() == 1) {
                                    if (strongClassifier[4].strongClassifierFunction() == 1) {
                                        if (strongClassifier[5].strongClassifierFunction() == 1) {
                                            if (strongClassifier[6].strongClassifierFunction() == 1) {
                                                if (strongClassifier[7].strongClassifierFunction() == 1) {
                                                    Graphics2D graph = img.createGraphics();
                                                    Color transparent = new Color(255, 0, 0, 100);
                                                    graph.setColor(transparent);
                                                    graph.fill(new Rectangle(j, i, 24, 24));
                                                    Color transparent2 = new Color(0, 255, 0, 100);
                                                    graph.setColor(transparent2);
                                                    graph.fill(new Rectangle(j + 1, i + 1, 22, 22));
                                                    graph.dispose();
                                                    System.out.println(i + " " + j);
                                                    File ouptut = new File("detect/detected.png");
                                                    ImageIO.write(img, "png", ouptut);
                                                }
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
                sub.flush();
                sub = null;

            }
            count++;
        }
        img.flush();
        img = null;
        writeImg.flush();
        writeImg = null;

//        Classifier strongClassifier = new Classifier();
//        if (classifier)
    }
}
