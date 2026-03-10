public class MainLearnWithBot {



    public static void main(String[] args) {

        int maxTimeGeneration = 2500;

        //learn
        int timeValue = 5;
        int distanceVelue = 5;
        int colisionValue = 30000;

        int LEARNTYPECOUNT = 7;  // neparni ///////////////////////////

//        double myltipliers[] = {2, 0.2 , 0.02 };
//        double myltipliers[] = {2, 0.2 , 0.02 ,20};
//          double myltipliers[] = {0.0002, 0.000002,0.00000002,0.000000002 ,0.02, 0.002};
    double myltipliers[] = {0.2 ,2, 0.02 , 0.0002 , 0.002};

        boolean ISITSTARTRANDOM = false; ///////////// !!!!!!!!!!!!!!!!!!!!!!!!
        int MaxFaileRandom = 10000;
        // spawn
        boolean ISSPAWNPOSITIONRANDOM = true;
        int radiusSpawnRanmon = 20;


        //neuro
        int NOFMODEL = 1;
        int countOfInput = 21;
        int wightModel = 3;
        int heightOfModel = 11;

        int CountOfOutput = 5;

        int mapIndex = 4;
        int maxGeneration = 30;
        ///car setting
       int countOfCar = 1;

       double myCarAcceleration = 0.6;

       int botSpeedLimeter = 27;
       double botAccereration = 0.4;

        int wightCar = 100;
        int heightCar = 50;

       Vector2 botSpawingPosCorrector = new Vector2(400, -50);
       /// bot neurons
        int NOFMODELBot = 4;
        int countOfInputBot = 11;
        int wightModelBot = 3;
        int heightOfModelBot = 11;

        int CountOfOutputBot = 5;

        Car[] cars = new Car[countOfCar];

       NeuroModel[] botsNeuro = {new NeuroModel(countOfInputBot, wightModelBot,heightOfModelBot,CountOfOutputBot)};
       botsNeuro[0].readFromFile("N_" + NOFMODELBot + "_" + botsNeuro[0].countOfInpunts + "_" + botsNeuro[0].wightOfModel + "_" +botsNeuro[0].heightOfModel + "_" + botsNeuro[0].countofOutput);

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

        Car car = new Car(new Vector2(map.startStop.p1),  0 ,wightCar, heightCar, countOfInput -1, radiusVIEW, carFov);
//        Car car;
        int curentFaileRandom =0;

        car = new Car(new Vector2(map.startStop.p1),  0 ,wightCar, heightCar, (countOfInput - 1) / 2, radiusVIEW, carFov);
//                            Car[] cars = new Car[countOfCar];
        cars[0] =  new Car(new Vector2(map.startStop.p1.addR(botSpawingPosCorrector)),  0 ,wightCar, heightCar, (countOfInputBot - 1), radiusVIEW, carFov);
//                            botcar = new Car(new Vector2(map.startStop.p1),  0 ,wightCar, heightCar, (countOfInput - 1) / 2, radiusVIEW, carFov);
        cars[0].speedLimiter = botSpeedLimeter;
//                            results[i] = car.curkCarResult(map, neurochanged,distanceToWin,maxTimeGeneration,timeValue,distanceVelue,colisionValue);
        bestResultView = car.curkCarResultWithOtherCar(map, nModel,cars,botsNeuro,distanceToWin,maxTimeGeneration,timeValue,distanceVelue,colisionValue);

//        bestResultView = car.curkCarResultWithOtherCar(map, nModel, cars,botsNeuro,distanceToWin,maxTimeGeneration,timeValue,distanceVelue,colisionValue);
        while(ISITSTARTRANDOM){

            NeuroModel nRandom = new NeuroModel(nModel);
            nRandom.setRandom(1,1);


            car = new Car(new Vector2(map.startStop.p1),  0 ,wightCar, heightCar, (countOfInput - 1) / 2, radiusVIEW, carFov);
//                            Car[] cars = new Car[countOfCar];

            car.acceleration = myCarAcceleration;
            cars[0] =  new Car(new Vector2(map.startStop.p1.addR(botSpawingPosCorrector)),  0 ,wightCar, heightCar, (countOfInputBot - 1), radiusVIEW, carFov);
//                            botcar = new Car(new Vector2(map.startStop.p1),  0 ,wightCar, heightCar, (countOfInput - 1) / 2, radiusVIEW, carFov);
            cars[0].speedLimiter = botSpeedLimeter;
            cars[0].acceleration = botAccereration;

//                            results[i] = car.curkCarResult(map, neurochanged,distanceToWin,maxTimeGeneration,timeValue,distanceVelue,colisionValue);
            double currentResult = car.curkCarResultWithOtherCar(map, nRandom,cars,botsNeuro,distanceToWin,maxTimeGeneration,timeValue,distanceVelue,colisionValue);

//                            results[i] = car.curkCarResult(map, neurochanged,distanceToWin,maxTimeGeneration,timeValue,distanceVelue,colisionValue);

//             = car.curkCarResultWithOtherCar(map, nRandom,cars,botsNeuro,distanceToWin,maxTimeGeneration,timeValue,distanceVelue,colisionValue);

            if(currentResult < bestResultView){
                bestResultView = currentResult;
                nModel = new NeuroModel(nRandom);
                curentFaileRandom = 0;
                System.out.println("new best result by Random " +bestResultView);
            }
            if(curentFaileRandom > MaxFaileRandom){
                ISITSTARTRANDOM = false;

            }
            curentFaileRandom++;
//            System.out.println(bestResultView);


        }

        System.out.println("Now Leanring");
        nModel.saveNeruonModel(NOFMODEL);

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
                            if(ISSPAWNPOSITIONRANDOM)
                            car = new Car((new Vector2(map.startStop.p1)).subR(new Vector2(Math.random() * radiusSpawnRanmon,Math.random() * radiusSpawnRanmon)),  0 ,wightCar, heightCar, (countOfInput - 1) / 2, radiusVIEW, carFov);
                            else
                            car = new Car(new Vector2(map.startStop.p1),  0 ,wightCar, heightCar, (countOfInput - 1) / 2, radiusVIEW, carFov);
//                            Car[] cars = new Car[countOfCar];
                            car.acceleration = myCarAcceleration;
                            cars[0] =  new Car(new Vector2(map.startStop.p1.addR(botSpawingPosCorrector)),  0 ,wightCar, heightCar, (countOfInputBot - 1), radiusVIEW, carFov);
//                            botcar = new Car(new Vector2(map.startStop.p1),  0 ,wightCar, heightCar, (countOfInput - 1) / 2, radiusVIEW, carFov);
                            cars[0].speedLimiter = botSpeedLimeter;
                            cars[0].acceleration = botAccereration;
//                            results[i] = car.curkCarResult(map, neurochanged,distanceToWin,maxTimeGeneration,timeValue,distanceVelue,colisionValue);
                            results[i] = car.curkCarResultWithOtherCar(map, neurochanged,cars,botsNeuro,distanceToWin,maxTimeGeneration,timeValue,distanceVelue,colisionValue);



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
                            car = new Car(new Vector2(map.startStop.p1),  0 ,wightCar, heightCar, (countOfInput - 1) / 2, radiusVIEW, carFov);
//                            Car[] cars = new Car[countOfCar];
                            car.acceleration  = myCarAcceleration;
                            cars[0] =  new Car(new Vector2(map.startStop.p1.addR(botSpawingPosCorrector)),  0 ,wightCar, heightCar, (countOfInputBot - 1), radiusVIEW, carFov);
//                            botcar = new Car(new Vector2(map.startStop.p1),  0 ,wightCar, heightCar, (countOfInput - 1) / 2, radiusVIEW, carFov);
                            cars[0].speedLimiter = botSpeedLimeter;
                            cars[0].acceleration = botAccereration;
//                            results[i] = car.curkCarResult(map, neurochanged,distanceToWin,maxTimeGeneration,timeValue,distanceVelue,colisionValue);
                            results[i] = car.curkCarResultWithOtherCar(map, neurochanged,cars,botsNeuro,distanceToWin,maxTimeGeneration,timeValue,distanceVelue,colisionValue);



                            //                            car = new Car(new Vector2(map.startStop.p1),  0 ,wightCar, heightCar, countOfInput - 1, radiusVIEW, carFov);

//                            results[i] = car.curkCarResult(map, neurochanged,distanceToWin,maxTimeGeneration,timeValue,distanceVelue,colisionValue);


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
                nModel.saveNeruonModel(NOFMODEL);


            }


            nModel.saveNeruonModel(NOFMODEL);

        }



    }


}