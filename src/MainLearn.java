import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class MainLearn {


    public static void main(String[] args) {

        int maxTimeGeneration = 2500;

        //learn
        int timeValue = 1;
        int distanceVelue = 5;
        int colisionValue = 200;

        int LEARNTYPECOUNT = 7;  // neparni ///////////////////////////

//        double myltipliers[] = {2, 0.2 , 0.02 };
        double myltipliers[] = {2, 0.2 , 0.02,  0.002 };
//          double myltipliers[] = {0.0002, 0.000002,0.00000002,0.000000002 ,0.02, 0.002};
//    double myltipliers[] = {0.000002,0.2 ,2,20, 0.02 , 0.0002};


        //neuro
        int NOFMODEL = 1;
        int countOfInput = 21;
        int wightModel = 4;
        int heightOfModel = 15;

        int CountOfOutput = 5;

        int mapIndex = 4;
        int maxGeneration = 30;
        ///car setting
        int radiusVIEW = 700;
        int carFov = 240;

        int distanceToWin = 100;


        NeuroModel nModel = new NeuroModel(countOfInput,wightModel,heightOfModel,CountOfOutput);
        nModel.readFromFile("N_" + NOFMODEL + "_" + nModel.countOfInpunts + "_" + nModel.wightOfModel + "_" +nModel.heightOfModel + "_" + nModel.countofOutput);



        Map map = new Map();
        map.readFromFile("map" +mapIndex+ ".txt");

        int countOfAllWeght = nModel.countOfInpunts * nModel.heightOfModel + (nModel.wightOfModel - 1) * nModel.heightOfModel * nModel.heightOfModel + nModel.heightOfModel * nModel.countofOutput;
        int countOfALLNeurons = nModel.wightOfModel * nModel.heightOfModel + nModel.countofOutput;


        System.out.println("countOfAllWeght: " + countOfAllWeght + " countOfALLNeurons: " + countOfALLNeurons);
        int curentGeneration =0;
        int curentChangeIndex = 0;

        double bestResultView = Double.MAX_VALUE;

//        Car car = new Car(new Vector2(map.startStop.p1),  0 ,100, 50, COUNTOFRAYS, radiusVIEW, carFov);
        Car car;


        while(curentGeneration < maxGeneration) {
            curentGeneration++;
            System.out.println("current Generation: " + curentGeneration);

            for (int myltIndex = 0; myltIndex < myltipliers.length; myltIndex++) {
                double currenMylt = myltipliers[myltIndex];

                for (curentChangeIndex = 0; curentChangeIndex < countOfALLNeurons + countOfAllWeght; curentChangeIndex++) {
                    double[] results = new double[LEARNTYPECOUNT];

//                    System.out.println(curentChangeIndex * 100 / (countOfALLNeurons + countOfAllWeght));

                    if (curentChangeIndex < countOfAllWeght) {

                        for (int i = 0; i < LEARNTYPECOUNT; i ++){
                            NeuroModel neurochanged = new NeuroModel(nModel);
                            neurochanged.setWeith(curentChangeIndex, ((i - ((LEARNTYPECOUNT - 1.0)  / 2)) * currenMylt)  + nModel.getWeith(curentChangeIndex));
                            car = new Car(new Vector2(map.startStop.p1),  0 ,100, 50, countOfInput - 1, radiusVIEW, carFov);
                            results[i] = car.curkCarResult(map, neurochanged,distanceToWin,maxTimeGeneration,timeValue,distanceVelue,colisionValue);


                        }
                        double minV = results[0];
                        int minI= 0;
                        for(int i = 0; i < results.length; i++){
                            if(results[i] < minV){
                                minV = results[i];
                                minI = i;
                            }
                        }
                        if (minV < bestResultView ) {
                            bestResultView = minV;
                            System.out.println("best result " + minV + " current mylt " + currenMylt);
                        }
                        nModel.setWeith(curentChangeIndex, ((minI - ((LEARNTYPECOUNT - 1.0)  / 2)) * currenMylt)  + nModel.getWeith(curentChangeIndex));

                    }else {
                        for (int i = 0; i < LEARNTYPECOUNT; i ++){
                            NeuroModel neurochanged = new NeuroModel(nModel);
                            neurochanged.setB(curentChangeIndex - countOfAllWeght, ((i - ((LEARNTYPECOUNT - 1.0)  / 2)) * currenMylt)  + nModel.getB(curentChangeIndex - countOfAllWeght));
                            car = new Car(new Vector2(map.startStop.p1),  0 ,100, 50, countOfInput - 1, radiusVIEW, carFov);

                            results[i] = car.curkCarResult(map, neurochanged,distanceToWin,maxTimeGeneration,timeValue,distanceVelue,colisionValue);


                        }
                        double minV = results[0];
                        int minI= 0;
                        for(int i = 0; i < results.length; i++){
                            if(results[i] < minV){
                                minV = results[i];
                                minI = i;
                            }
                        }
                        if(minV < bestResultView ) {
                            bestResultView = minV;
                            System.out.println("best result " + minV + " current mylt " + currenMylt);
                        }
                        nModel.setB(curentChangeIndex - countOfAllWeght, ((minI - ((LEARNTYPECOUNT - 1.0)  / 2)) * currenMylt)  + nModel.getB(curentChangeIndex - countOfAllWeght));

                    }



                }
                nModel.safeToFile("N_" + NOFMODEL + "_" + nModel.countOfInpunts + "_" + nModel.wightOfModel + "_" +nModel.heightOfModel + "_" + nModel.countofOutput);
                System.out.println("neoro safe");


            }
            nModel.safeToFile("N_" + NOFMODEL + "_" + nModel.countOfInpunts + "_" + nModel.wightOfModel + "_" +nModel.heightOfModel + "_" + nModel.countofOutput);
            System.out.println("neoro safe");



        }

        nModel.safeToFile("N_" + NOFMODEL + "_" + nModel.countOfInpunts + "_" + nModel.wightOfModel + "_" +nModel.heightOfModel + "_" + nModel.countofOutput);


    }


}