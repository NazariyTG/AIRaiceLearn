import java.util.stream.IntStream;

public class MainLearnWithBot {



    public static void main(String[] args) {

        int maxTimeGeneration = 3000;

        //learn
        int timeValue = 10;
        int distanceVelue = 1;
        int colisionValue = 3000;

        int distanceToWin = 100;

        int LEARNTYPECOUNT = 7;  // neparni ///////////////////////////

        double myltipliers[] = {2, 0.2 , 0.02,0.002 , 20};
//        double myltipliers[] = {2, 0.2 , 0.02 ,20};
//          double myltipliers[] = {0.0002, 0.000002,0.00000002,0.000000002 ,0.02, 0.002};
//    double myltipliers[] = {0.2 ,2, 0.02 , 0.0002 , 0.002};

        boolean ISITSTARTRANDOM = false ; ///////////// !!!!!!!!!!!!!!!!!!!!!!!!
        int MaxFaileRandom = 10000;
        // spawn
        boolean ISSPAWNPOSITIONRANDOM = true;
        int radiusSpawnRanmon = 20;

        Vector2 spawnRandom = new Vector2(0,0);
        int generationToStarntRandomPositon = 2;

        int countRandomP = 0;
        boolean isChangeRandomP= false;


        //neuro
        int NOFMODEL = 503;
        int countOfInput = 21;
        int wightModel = 3;
        int heightOfModel = 11;

        int CountOfOutput = 5;

        int mapIndex = 4;
        int maxGeneration = 30;
        ///car setting
       int countOfCar = 1;

       double myCarAcceleration = 0.6;

       int botSpeedLimeter = 20;
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




        NeuroModel nModel = new NeuroModel(countOfInput,wightModel,heightOfModel,CountOfOutput);
        nModel.readFromFile("N_" + NOFMODEL + "_" + nModel.countOfInpunts + "_" + nModel.wightOfModel + "_" +nModel.heightOfModel + "_" + nModel.countofOutput);



        Map map = new Map();
        map.readFromFile("map" +mapIndex+ ".txt");

        int countOfAllWeght = nModel.countOfInpunts * nModel.heightOfModel + (nModel.wightOfModel - 1) * nModel.heightOfModel * nModel.heightOfModel + nModel.heightOfModel * nModel.countofOutput;
        int countOfALLNeurons = nModel.wightOfModel * nModel.heightOfModel + nModel.countofOutput;


        System.out.println("countOfAllWeght: " + countOfAllWeght + " countOfALLNeurons: " + countOfALLNeurons);
        int curentGeneration =0;


        double bestResultView = Double.MAX_VALUE;

        Car car = new Car(new Vector2(map.startStop.p1),  0 ,wightCar, heightCar, countOfInput -1, radiusVIEW, carFov);
//        Car car;
        int curentFaileRandom =0;

        car = new Car(new Vector2(map.startStop.p1),  0 ,wightCar, heightCar, (countOfInput - 1) / 2, radiusVIEW, carFov);
//                            Car[] cars = new Car[countOfCar];
        cars[0] =  new Car(new Vector2(map.startStop.p1.addR(botSpawingPosCorrector)),  0 ,wightCar, heightCar, (countOfInputBot - 1), radiusVIEW, carFov);
//                            botcar = new Car(new Vector2(map.startStop.p1),  0 ,wightCar, heightCar, (countOfInput - 1) / 2, radiusVIEW, carFov);
        cars[0].speedLimiter = botSpeedLimeter;
        cars[0].acceleration = 0.4;
//                            results[i] = car.curkCarResult(map, neurochanged,distanceToWin,maxTimeGeneration,timeValue,distanceVelue,colisionValue);
        //bestResultView = car.curkCarResultWithOtherCar(map, nModel,cars,botsNeuro,distanceToWin,maxTimeGeneration,timeValue,distanceVelue,colisionValue);




        Car[] myCars = new Car[LEARNTYPECOUNT];
        for(int i = 0; i < myCars.length; i++){
            myCars[i] = new Car(new Vector2(map.startStop.p1),  0 ,wightCar, heightCar, (countOfInput - 1) / 2, radiusVIEW, carFov);
        }
        NeuroModel[] myNeuros = new NeuroModel[LEARNTYPECOUNT];
        for(int i = 0; i < myNeuros.length; i++){
            myNeuros[i] = new NeuroModel(nModel);
        }

//        double[] results = new double[LEARNTYPECOUNT];

        System.out.println(car.curkCarResultWithOtherCarWithRespawn(map, nModel, cars,botsNeuro,distanceToWin,maxTimeGeneration,timeValue,distanceVelue,colisionValue,spawnRandom, botSpawingPosCorrector));

        while(ISITSTARTRANDOM){

            NeuroModel nRandom = new NeuroModel(nModel);
            nRandom.setRandom(1,1);

            car.respawnPosition(map);
            cars[0].respawnPositionOffset(map, botSpawingPosCorrector);


//                            results[i] = car.curkCarResult(map, neurochanged,distanceToWin,maxTimeGeneration,timeValue,distanceVelue,colisionValue);
            double currentResult = car.curkCarResultWithOtherCarWithRespawn(map, nRandom,cars,botsNeuro,distanceToWin,maxTimeGeneration,timeValue,distanceVelue,colisionValue,spawnRandom,botSpawingPosCorrector);

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
            if(ISSPAWNPOSITIONRANDOM && curentGeneration > generationToStarntRandomPositon) {
//                    spawnRandom = new Vector2(Math.random() * radiusSpawnRanmon, Math.random() * radiusSpawnRanmon);
                spawnRandom.x = Math.random() * radiusSpawnRanmon;
                spawnRandom.y = Math.random() * radiusSpawnRanmon;
                bestResultView = Double.MAX_VALUE;
            }
            System.out.println("current Generation: " + curentGeneration);

            for (int myltIndex = 0; myltIndex < myltipliers.length; myltIndex++) {
                double currenMylt = myltipliers[myltIndex];



                for (int curentChangeIndex = 0; curentChangeIndex < countOfALLNeurons + countOfAllWeght; curentChangeIndex++) {
                    double[] results = new double[LEARNTYPECOUNT];

//                    System.out.println(curentChangeIndex * 100 / (countOfALLNeurons + countOfAllWeght));

                    if (curentChangeIndex < countOfAllWeght) {

                        for (int i = 0; i < LEARNTYPECOUNT; i++) {
                            myNeuros[i] = new NeuroModel(nModel);
                            myNeuros[i].setWeith(curentChangeIndex, ((i - ((LEARNTYPECOUNT - 1.0) / 2)) * currenMylt) + nModel.getWeith(curentChangeIndex));

                        }

                        results = IntStream.range(0, LEARNTYPECOUNT).parallel().mapToDouble(i -> {
                            // 1. Беремо або готуємо об'єкт для поточного індексу i


                            double result = myCars[i].curkCarResultWithOtherCarWithRespawn(map, myNeuros[i], cars, botsNeuro, distanceToWin, maxTimeGeneration, timeValue, distanceVelue, colisionValue, spawnRandom,botSpawingPosCorrector);

                            // 3. Повертаємо результат
                            return result;
                        }).toArray();
//                        for(int i = 0; i < LEARNTYPECOUNT; i++){
//                            results[i] = myCars[i].curkCarResultWithOtherCarWithRespawn(map, myNeuros[i], cars, botsNeuro, distanceToWin, maxTimeGeneration, timeValue, distanceVelue, colisionValue, botSpawingPosCorrector);
//                        }


                        double minV = results[0];
                        int minI = 0;
                        for (int i = 0; i < results.length; i++) {
                            if (results[i] < minV) {
                                minV = results[i];
                                minI = i;
                            }
                        }
                        if (minV < bestResultView) {
                            bestResultView = minV;
                            System.out.println("best result " + minV + " current mylt " + currenMylt);
                        }

//                        nModel = new NeuroModel(myNeuros[minI]);
                    }else {
                        for (int i = 0; i < LEARNTYPECOUNT; i++) {
                            myNeuros[i] = new NeuroModel(nModel);
                            myNeuros[i].setB(curentChangeIndex - countOfAllWeght, ((i - ((LEARNTYPECOUNT - 1.0) / 2)) * currenMylt) + nModel.getB(curentChangeIndex - countOfAllWeght));

                        }

//                        for(int i = 0; i < myCars.length; i++){
//                            myCars[i] = new Car(new Vector2(map.startStop.p1),  0 ,wightCar, heightCar, (countOfInput - 1) / 2, radiusVIEW, carFov);
//                        }

                        results = IntStream.range(0, LEARNTYPECOUNT).parallel().mapToDouble(i -> {
                            // 1. Беремо або готуємо об'єкт для поточного індексу i


                            double result = myCars[i].curkCarResultWithOtherCarWithRespawn(map, myNeuros[i], cars, botsNeuro, distanceToWin, maxTimeGeneration, timeValue, distanceVelue, colisionValue,spawnRandom, botSpawingPosCorrector);

                            // 3. Повертаємо результат
                            return result;
                        }).toArray();

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
                        nModel = new NeuroModel(myNeuros[minI]);

                    }
                }



                nModel.saveNeruonModel(NOFMODEL);


            }


            nModel.saveNeruonModel(NOFMODEL);

        }



    }





}