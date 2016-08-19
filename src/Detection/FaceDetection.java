/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Detection;

import CustomDataStructure.ClassifierData;
import CustomDataStructure.IndexData;
import Detection.util.DataReader;
import Detection.util.IndexReader;
import Detection.util.TestFeatures;
import HistogramEqualization.impl.HistEq;
import HistogramEqualization.util.HistoNormalize;
import haarfilterall.util.IntegralImage;
import java.awt.Color;
import java.awt.Graphics2D;
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

    int mNumberOfClassifiers = 8;
    private ArrayList<ClassifierData> mClassifierData = new ArrayList();
    private ArrayList<IndexData> mIndexData = new ArrayList();
    private String mInputFileName = "testImages/test5.png";
    private String mOutputFileName = "detect/FinalDetected2.png";
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
        for (int i = 0; i < mNumberOfClassifiers; i++) {
            DataReader dataReader = new DataReader("classifier/NormalizeWithOne/Classifier" + (i + 1) + ".txt");
            ClassifierData data = new ClassifierData(dataReader.getPolarity(),
                    dataReader.getThreshold(),
                    dataReader.getFeatureIndex(),
                    dataReader.getAlpha());
            mClassifierData.add(data);

            IndexReader indexReader = new IndexReader("indexValues/haarIndex" + (i + 1) + ".txt");
            IndexData indexData = new IndexData(indexReader.getWidth(),
                    indexReader.getHeight(),
                    indexReader.getXpos(),
                    indexReader.getYpos());
            mIndexData.add(indexData);

        }

        startDetection();
    }

    public void startDetection() {
        BufferedImage img = null;
        BufferedImage writeImg = null;
        try {
            img = ImageIO.read(new File(mInputFileName));
            writeImg = ImageIO.read(new File(mInputFileName));
            HistEq histEq = new HistEq();                                           //call histogram equilization
            img = histEq.Change(img);                                               //retreive equilized image

            HistoNormalize histoNormalize = new HistoNormalize();
            img = histoNormalize.HistoNormal(img);
        } catch (Exception e) {
        }

        int count = 0;
        Classifier[] strongClassifier = new Classifier[mNumberOfClassifiers];
        for (int i = 0; i < mNumberOfClassifiers; i++) {
            strongClassifier[i] = new Classifier(mClassifierData.get(i).getAlpha(),
                    mClassifierData.get(i).getPolarity(),
                    mClassifierData.get(i).getThreshold());
        }

        int[][] currentPos = new int[img.getHeight()][img.getWidth()];
        ArrayList<Integer[]> positions = new ArrayList();
        for (int i = 0; i <= (img.getHeight() - 24); i++) {                     //For a 24x24 Pixel image define height such that the last image to be extracted has atleast 24 Pixels to work with
            int subCount = 0;
            int posIndex = 0;
            for (int j = 0; j <= (img.getWidth() - 24); j++) {                  //*For a 24x24 Pixel image define width such that the last image to be extracted has atleast 24 Pixels to work with

                BufferedImage sub = img.getSubimage(j, i, 24, 24);              //sub image starting point defined by i and j & 24x4 is the needed size for the sub image.             

                for (int k = 0; k < 24; k++) {
                    for (int l = 0; l < 24; l++) {
                        values[k][l] = sub.getRaster().getPixel(k, l, iarray)[0];
                    }
                }

                intValues = integralImage.Integral(values);

                for (int itr = 0; itr < mNumberOfClassifiers; itr++) {
                    int featureIndexLength = mClassifierData.get(itr).getFeatureIndex().length;
                    double[] sendFeature = new double[featureIndexLength];
                    for (int k = 0; k < featureIndexLength - 1; k++) {
                        int featureIndexValue = mClassifierData.get(itr).getFeatureIndex()[k];
                        if (featureIndexValue < firstFilterRange) {

                            sendFeature[k] = features.FeatureA(intValues,
                                    mIndexData.get(itr).getWidth()[k],
                                    mIndexData.get(itr).getHeight()[k],
                                    mIndexData.get(itr).getXpos()[k],
                                    mIndexData.get(itr).getYpos()[k]);
                        } else if (featureIndexValue < secondFilterRange) {
                            sendFeature[k] = features.FeatureB(intValues,
                                    mIndexData.get(itr).getWidth()[k],
                                    mIndexData.get(itr).getHeight()[k],
                                    mIndexData.get(itr).getXpos()[k],
                                    mIndexData.get(itr).getYpos()[k]);
                        } else if (featureIndexValue < thirdFilterRange) {
                            sendFeature[k] = features.FeatureC(intValues,
                                    mIndexData.get(itr).getWidth()[k],
                                    mIndexData.get(itr).getHeight()[k],
                                    mIndexData.get(itr).getXpos()[k],
                                    mIndexData.get(itr).getYpos()[k]);
                        } else {
                            sendFeature[k] = features.FeatureE(intValues,
                                    mIndexData.get(itr).getWidth()[k],
                                    mIndexData.get(itr).getHeight()[k],
                                    mIndexData.get(itr).getXpos()[k],
                                    mIndexData.get(itr).getYpos()[k]);
                        }
                    }
                    strongClassifier[itr].setFeatures(sendFeature);
                }

                try {
                    if (strongClassifier[0].strongClassifierFunction() == 1) {
                        if (strongClassifier[1].strongClassifierFunction() == 1) {
                            if (strongClassifier[2].strongClassifierFunction() == 1) {
                                if (strongClassifier[3].strongClassifierFunction() == 1) {
                                    if (strongClassifier[4].strongClassifierFunction() == 1) {
                                        if (strongClassifier[5].strongClassifierFunction() == 1) {
                                            if (strongClassifier[6].strongClassifierFunction() == 1) {
                                                if (strongClassifier[7].strongClassifierFunction() == 1) {

                                                    Graphics2D graph = writeImg.createGraphics();
                                                    Color transparent = new Color(255, 0, 0, 100);
                                                    graph.setColor(transparent);
                                                    graph.drawLine(j, i, j + 24, i);
                                                    graph.drawLine(j, i, j, i + 24);
                                                    graph.drawLine(j + 24, i, j + 24, i + 24);
                                                    graph.drawLine(j, i + 24, j + 24, i + 24);;
                                                    graph.dispose();
                                                    System.out.println(i + " " + j);
                                                    File ouptut = new File(mOutputFileName);
                                                    ImageIO.write(writeImg, "png", ouptut);
                                                }
                                            }
                                        }
                                    }
                                }
                            }

                        }
                    }
                    // drawDetectedSurface(currentPos);
                } catch (Exception e) {
                    System.out.println("Bahira");
                }
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
    }

    public void drawDetectedSurface(int[][] allPositions) throws IOException {
        BufferedImage writeImg = null;
        writeImg = ImageIO.read(new File(mInputFileName));
        for (int yIndex = 0; yIndex < allPositions.length; yIndex++) {
            for (int xIndex = 0; xIndex < allPositions[yIndex].length; xIndex++) {
                int count = xIndex + 1;
                int sum = 0;
                for (int traverseWindow = 1; traverseWindow < 24; traverseWindow++) {
                    if ((allPositions[yIndex][xIndex] + traverseWindow) == allPositions[yIndex][count]) {
                        sum += allPositions[yIndex][count];
                        count++;
                    }
                }
                if (count >= 10) {
                    int avg = sum / count;

                    Graphics2D graph = writeImg.createGraphics();
                    Color transparent = new Color(255, 0, 0, 100);
                    graph.setColor(transparent);
                    graph.drawLine(yIndex, avg, yIndex + 24, avg);
                    graph.drawLine(yIndex, avg, yIndex, avg + 24);
                    graph.drawLine(yIndex + 24, avg, yIndex + 24, avg + 24);
                    graph.drawLine(yIndex, avg + 24, yIndex + 24, avg + 24);;
                    graph.dispose();
                    System.out.println(yIndex + " " + avg);
                    File ouptut = new File(mOutputFileName);
                    ImageIO.write(writeImg, "png", ouptut);
                }
            }

        }
        writeImg.flush();
        writeImg = null;
    }
}
