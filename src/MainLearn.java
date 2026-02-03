import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class MainLearn {


    public static void main(String[] args) {

        int maxTimeGeneration = 700;

        //learn
        int timeValue = 1;
        int distanceVelue = 2;
        int colisionValue = 2000;

        int LEARNTYPECOUNT = 3;  // neparni ///////////////////////////

//        double myltipliers[] = {2, 0.2 , 0.02, 0.002, 0.0002,0.00002, 20, 200,2000, };
//        double myltipliers[] = {2, 0.2 , 0.02,  0.002,20 };
        //    double myltipliers[] = { 0.02, 0.002, 0.0002, 0.000002,0.00000002,0.000000002};
    double myltipliers[] = {0.2 ,2};


        //neuro
        int NOFMODEL = 1;
        int COUNTOFRAYS = 10;
        int wightModel = 4;
        int heightOfModel = 5;

        int CountOfOutput = 5;

        int mapIndex = 4;
        int maxGeneration = 3;
        ///car setting
        int radiusVIEW = 700;
        int carFov = 240;

        int distanceToWin = 100;


        NeuroModel nModel = new NeuroModel(COUNTOFRAYS,wightModel,heightOfModel,CountOfOutput);
        nModel.readFromFile("N_" + NOFMODEL + "_" + nModel.countOfInpunts + "_" + nModel.wightOfModel + "_" +nModel.heightOfModel + "_" + nModel.countofOutput);



        Map map = new Map();
        map.readFromFile("map" +mapIndex+ ".txt");

        int countOfAllWeght = nModel.countOfInpunts * nModel.heightOfModel + (nModel.wightOfModel - 1) * nModel.heightOfModel * nModel.heightOfModel + nModel.heightOfModel * nModel.countofOutput;
        int countOfALLNeurons = nModel.wightOfModel * nModel.heightOfModel + nModel.countofOutput;


        System.out.println("countOfAllWeght: " + countOfAllWeght + " countOfALLNeurons: " + countOfALLNeurons);
        int curentGeneration =0;
        int curentChangeIndex = 0;

//        Car car = new Car(new Vector2(map.startStop.p1),  0 ,100, 50, COUNTOFRAYS, radiusVIEW, carFov);
        Car car;


        while(curentGeneration < maxGeneration) {
            curentGeneration++;
            System.out.println("current Generation: " + curentGeneration);

            for (int myltIndex = 0; myltIndex < myltipliers.length; myltIndex++) {
                double currenMylt = myltipliers[myltIndex];

                for (curentChangeIndex = 0; curentChangeIndex < countOfALLNeurons + countOfAllWeght; curentChangeIndex++) {
                    double[] results = new double[LEARNTYPECOUNT];

                    if (curentChangeIndex < countOfAllWeght) {

                        for (int i = 0; i < LEARNTYPECOUNT; i ++){
                            NeuroModel neurochanged = new NeuroModel(nModel);
                            neurochanged.setWeith(curentChangeIndex, ((i - ((LEARNTYPECOUNT - 1.0)  / 2)) * currenMylt)  + nModel.getWeith(curentChangeIndex));
                            car = new Car(new Vector2(map.startStop.p1),  0 ,100, 50, COUNTOFRAYS, radiusVIEW, carFov);
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
                        System.out.println("best result " + minV);
                        nModel.setWeith(curentChangeIndex, ((minI - ((LEARNTYPECOUNT - 1.0)  / 2)) * currenMylt)  + nModel.getWeith(curentChangeIndex));

                    }else {
                        for (int i = 0; i < LEARNTYPECOUNT; i ++){
                            NeuroModel neurochanged = new NeuroModel(nModel);
                            neurochanged.setB(curentChangeIndex - countOfAllWeght, ((i - ((LEARNTYPECOUNT - 1.0)  / 2)) * currenMylt)  + nModel.getB(curentChangeIndex - countOfAllWeght));
                            car = new Car(new Vector2(map.startStop.p1),  0 ,100, 50, COUNTOFRAYS, radiusVIEW, carFov);

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
                        System.out.println("best result " + minV);
                        nModel.setB(curentChangeIndex - countOfAllWeght, ((minI - ((LEARNTYPECOUNT - 1.0)  / 2)) * currenMylt)  + nModel.getB(curentChangeIndex - countOfAllWeght));

                    }



                }


            }
            nModel.safeToFile("N_" + NOFMODEL + "_" + nModel.countOfInpunts + "_" + nModel.wightOfModel + "_" +nModel.heightOfModel + "_" + nModel.countofOutput);
            System.out.println("neoro safe");



        }

        nModel.safeToFile("N_" + NOFMODEL + "_" + nModel.countOfInpunts + "_" + nModel.wightOfModel + "_" +nModel.heightOfModel + "_" + nModel.countofOutput);


    }


}