/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Parallel;

import haarfilterall.features.Features;

/**
 *
 * @author user
 */
public class ParallelRun implements Runnable {

    private Thread t;
    private String threadName;
    private int tasknumber;
    private int[][] intValues = new int[24][24];
    private int[] twoHoriFea = new int[43200];
    private int[] twoVertFea = new int[43200];
    private int[] threeHoriFea = new int[43200];
    private int[] fourRectFea = new int[43200];
    Features features = new Features();

    public ParallelRun(String name, int task, int[][] integralValues) {
        threadName = name;
        tasknumber = task;
        intValues = integralValues;
//        System.out.println("Creating " + threadName);
    }

    public void run() {

        switch (tasknumber) {
            case 1:
//                twoHoriFea = features.FeatureA(2, 1, intValues);
                break;
            case 2:
//                twoVertFea = features.FeatureB(1, 2, intValues);
                break;
            case 3:
//                threeHoriFea = features.FeatureC(3, 1, intValues);
                break;
            case 4:
//                fourRectFea = features.FeatureE(2, 2, intValues);
                break;
            default:
                break;
        }
//        if (tasknumber == 1) {
//            twoHoriFea = features.FeatureA(2, 1, intValues);
////            System.out.println("F 1");
////            Thread.sleep(50);
//        } else if (tasknumber == 2) {
//            twoVertFea = features.FeatureB(1, 2, intValues);
////            Thread.sleep(50);
////            System.out.println("F 2");
//        } else if (tasknumber == 3) {
//            threeHoriFea = features.FeatureC(3, 1, intValues);
//        } else if (tasknumber == 4) {
//            fourRectFea = features.FeatureE(2, 2, intValues);
//        }
    }

    public void start() {
//        System.out.println("Starting " + threadName);
        if (t == null) {
            t = new Thread(this, threadName);
            t.start();

        }
    }
}
