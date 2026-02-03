import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.MouseInfo;
import java.util.ArrayList;

public class Main extends JPanel implements MouseListener{
    static int WINDOWHEINGH= 1500;
    static int WINDOWWITH = 1000;

    int CAMERAMODE = 1; /// 1 -> on car
    static int MAXTIMEFORGENERATION = 500;

    //learn
    int timeValue = 2;
    int distanceVelue = 5;
    int colisionValue = 1000;

    final static int LEARNTYPECOUNT = 7;  // neparni ///////////////////////////

//        double myltipliers[] = {2, 0.2 , 0.02, 0.002, 0.0002,0.00002, 20, 200,2000, };
    double myltipliers[] = {2, 0.2 , 0.02,  0.002,0.00002,20 };
    //    double myltipliers[] = { 0.02, 0.002, 0.0002, 0.000002,0.00000002,0.000000002};
//        double myltipliers[] = {  0.00000002,0.000000002};
//    double myltipliers[] = {0.2 ,2};
    int curentMultp = 0;

    static final boolean ISRENDERCAR1= true;
    static final boolean ISRENDERCAR= true;
    static final boolean ISRENDERRAYS= false;
    static final boolean ISRENDERRAY = true;
    static final boolean ISRENDERMAP= true;

    static final boolean ISRENDERFIRSVIE= false;

    boolean isControlCar = false;

    int COUNTOFRAYS = 10;

    int CarFov = 240; /// 240

    int radiusVIEW = 700;

    int mapIndex = 0;


    boolean isRenderMap = true;

    double carMoument = 0;
    double carR = 0;
    boolean leftPressed = false;
    boolean rightPressed = false;

    int time = 0;

    boolean isMapEditinEctive = true;
    int activeWall= 1;

    int CountOfOutput = 4;

    NeuroModel nModel = new NeuroModel(COUNTOFRAYS,4,10,CountOfOutput);

    double bestResult = Double.MAX_VALUE;
    int coutSameNeurons;

    Map map = new Map();
    ArrayList<Line2d> mapWalls = new ArrayList<>();
    Vector2 mouse = new Vector2();
    Vector2 mouseDelta = new Vector2();

    Vector2 oldmouse = new Vector2();
    Camera cam = new Camera();



    double resultsWeight[][];
    double resultsNeuron[][];

    Car carsWeights[][];
    Car carsNeurons[][];

//    NeuroModel neuroModelsWeightsOrig[] ;
//    NeuroModel neuroModelsNeuronsOrig[] ;

    NeuroModel neuroModelsWeightsLearn[][] ;
    NeuroModel neuroModelsNeuronsLearn[][] ;





    boolean isCarAliveWeight[][];
    boolean isCarAliveNeurons[][];
//    Vector2 carsResults[][];

    int currentWeight= 0;
    int currentNeuron= 0;

    int countOfAllWeght;
    int countOfALLNeurons;


    int currentGeneration = 0;

    Car myCar ;

    double deltaLearn = 1;
// Vector2[] carsResults

    void updateLearn(){
        for(int gWeight = 0; gWeight < countOfAllWeght; gWeight++){
            for(int i = 0; i < LEARNTYPECOUNT; i++) {
                neuroModelsWeightsLearn[gWeight][i] = new NeuroModel(nModel);

            }
        }
        for(int gNeurons = 0; gNeurons < countOfALLNeurons; gNeurons++){
            for(int i = 0; i < LEARNTYPECOUNT; i++) {
                neuroModelsNeuronsLearn[gNeurons][i] = new NeuroModel(nModel);

            }
        }
    }


    void setLearn(){
        for(int gWeight = 0; gWeight < countOfAllWeght; gWeight++){
            for(int i = 0; i < LEARNTYPECOUNT; i++) {
                neuroModelsWeightsLearn[gWeight][i] = new NeuroModel(nModel);
            }
        }
        for(int gNeurons = 0; gNeurons < countOfALLNeurons; gNeurons++){
            for(int i = 0; i < LEARNTYPECOUNT; i++) {
                neuroModelsNeuronsLearn[gNeurons][i] = new NeuroModel(nModel);
            }
        }

        for(int w = 0; w < countOfAllWeght; w++) {
            for (int i = 0; i < LEARNTYPECOUNT; i++) {
                carsWeights[w][i] =  new Car(new Vector2(map.startStop.p1),  0 ,100, 50, COUNTOFRAYS, radiusVIEW, CarFov);
            }
        }
        for(int w = 0; w < countOfALLNeurons; w++) {
            for (int i = 0; i < LEARNTYPECOUNT; i++) {
                carsNeurons[w][i] =  new Car(new Vector2(map.startStop.p1),  0 ,100, 50, COUNTOFRAYS, radiusVIEW, CarFov);
            }
        }



        for(int j = 0; j <LEARNTYPECOUNT; j++){
            for(int i = 0 ; i < countOfAllWeght; i ++){
                isCarAliveWeight[i][j] = true;
            }
            for(int i = 0 ; i < countOfALLNeurons; i++){
                isCarAliveNeurons[i][j] = true;
            }


        }




    }

    void resetAllCars(){


        for(int j = 0; j <LEARNTYPECOUNT; j++){
            for(int i = 0 ; i < countOfAllWeght; i ++){
                isCarAliveWeight[i][j] = true;
                carsWeights[i][j].position = new Vector2(map.startStop.p1);
                carsWeights[i][j].rotation = 0;
                carsWeights[i][j].curc();

            }
            for(int i = 0 ; i < countOfALLNeurons; i++){
                isCarAliveNeurons[i][j] = true;
                carsNeurons[i][j].position = new Vector2(map.startStop.p1);
                carsNeurons[i][j].rotation = 0;
                carsNeurons[i][j].curc();
            }

        }
    }

    void changeLearn(double mylt){
        for(int gWeight = 0; gWeight < countOfAllWeght; gWeight++){
            for(int i = 0; i < LEARNTYPECOUNT; i++){

                neuroModelsWeightsLearn[gWeight][i].setWeith(gWeight, i - ((LEARNTYPECOUNT - 1.0)  / 2)  + nModel.getWeith(gWeight)) ;


            }


        }
        for(int gNeurons = 0; gNeurons < countOfALLNeurons; gNeurons++){
            for(int i = 0; i < LEARNTYPECOUNT; i++){
                neuroModelsNeuronsLearn[gNeurons][i].setB(gNeurons, i - ((LEARNTYPECOUNT - 1.0)  / 2)  + nModel.getB(gNeurons));
            }

        }
    }
    void changeRandomLearn(double mylt){
        for(int gWeight = 0; gWeight < countOfAllWeght; gWeight++){
            for(int i = 0; i < LEARNTYPECOUNT; i++){

                neuroModelsWeightsLearn[gWeight][i].setWeith(gWeight, ((i -1.0)/2 * mylt) * Math.random()) ;

            }


        }
        for(int gNeurons = 0; gNeurons < countOfALLNeurons; gNeurons++){
            for(int i = 0; i < LEARNTYPECOUNT; i++){
                neuroModelsNeuronsLearn[gNeurons][i].setB(gNeurons, ((i -1.0)/2 * mylt) * Math.random());
            }

        }
    }
    void setRandom(){
        for(int gWeight = 0; gWeight < countOfAllWeght; gWeight++){
            for(int i = 0; i < LEARNTYPECOUNT; i++){
                neuroModelsWeightsLearn[gWeight][i].setRandom(1,1);
            }


        }
        for(int gNeurons = 0; gNeurons < countOfALLNeurons; gNeurons++){
            for(int i = 0; i < LEARNTYPECOUNT; i++){
                neuroModelsNeuronsLearn[gNeurons][i].setRandom(1,1);
            }

        }
    }




    void learn(double resultsWeight[][], double resultsNeuron[][]){


        double globalMinResult=  resultsWeight[0][0];
        int[] globalMinIndex = {0,0};
        boolean isItWeight = true;

        for(int i = 0; i < countOfAllWeght; i++){

//            double lMinResult=  resultsWeight[0][0];


            for(int j = 0; j < LEARNTYPECOUNT; j++){
                if (resultsWeight[i][j] < globalMinResult){


//                    globalMinResult = resultsWeight[i][j];
//                    nModel.setWeith(i, neuroModelsWeightsLearn[i][j].getWeith(i));
                    globalMinResult = resultsWeight[i][j];
                    globalMinIndex[0] = i;
                    globalMinIndex[1] = j;

                }
            }

//            nModel.setWeith(i, neuroModelsWeightsLearn[i][minJ].getWeith(i));

        }
        for(int i = 0; i < countOfALLNeurons; i++){
//            double lMinResult=  resultsNeuron[0][0];

            for(int j = 0; j < LEARNTYPECOUNT; j++){
                if (resultsNeuron[i][j] < globalMinResult){

                    globalMinResult = resultsNeuron[i][j];
                    globalMinIndex[0] = i;
                    globalMinIndex[1] = j;
                    isItWeight = false;
//                    nModel.setB(i, neuroModelsNeuronsLearn[i][j].getB(i));


                }
            }

            //nModel.setB(i, neuroModelsNeuronsLearn[i][minJ].getB(i));

        }
        if(globalMinResult < bestResult){
            bestResult = globalMinResult;
            System.out.println("new best result " + bestResult);
        }



//        if(isItWeight)  nModel.setWeith(globalMinIndex[0], neuroModelsWeightsLearn[globalMinIndex[0]][globalMinIndex[1]].getWeith(globalMinIndex[0]));
//        else nModel.setB(globalMinIndex[0], neuroModelsNeuronsLearn[globalMinIndex[0]][globalMinIndex[1]].getB(globalMinIndex[0]));
        if(isItWeight)  nModel = new NeuroModel(neuroModelsWeightsLearn[globalMinIndex[0]][globalMinIndex[1]]);
        else nModel = new NeuroModel(neuroModelsNeuronsLearn[globalMinIndex[0]][globalMinIndex[1]]);

        if (globalMinIndex[1] == (LEARNTYPECOUNT-1.0) /2){
            coutSameNeurons++;
        }else {
            coutSameNeurons = 0;
        }

        if(coutSameNeurons > myltipliers.length +1){
            coutSameNeurons = 0;
        }
        System.out.println(coutSameNeurons);

        updateLearn();
        changeLearn(myltipliers[curentMultp]);
//        changeRandomLearn(myltipliers[curentMultp]);



        curentMultp++;
        if(curentMultp > myltipliers.length - 1) curentMultp = 0;
        currentGeneration++;
        System.out.println("Generation: " + currentGeneration);

    }

    void drawPoint(Vector2 positon, int radius , Graphics g , Camera cam){
        g.fillRect((int) (positon.subR(cam.position).x - radius/2), (int) (positon.subR(cam.position).y  - radius/2),  radius,radius);
    }




    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        for(int w = 0; w < countOfAllWeght; w++) {
            for (int i = 0; i < LEARNTYPECOUNT; i++) {
                carsWeights[w][i].g = g;
            }
        }
        for(int w = 0; w < countOfALLNeurons; w++) {
            for (int i = 0; i < LEARNTYPECOUNT; i++) {
                carsNeurons[w][i].g = g;
            }
        }

        Point mouseP = MouseInfo.getPointerInfo().getLocation();
        SwingUtilities.convertPointFromScreen(mouseP, this);
        Vector2 mouse = new Vector2(mouseP.x, mouseP.y);
        Vector2 mouseDelta = mouse.subR(oldmouse);
        if (rightPressed) cam.position.sub(mouseDelta);
        oldmouse = mouse;

        g.setColor(Color.BLUE);

        if(time == 0){
            resetAllCars();
            learn(resultsWeight,resultsNeuron);

        }
        if(MAXTIMEFORGENERATION - time < 100){
            System.out.println("last time: " + (MAXTIMEFORGENERATION - time));
        }



        for(int w = 0; w < countOfAllWeght; w++){
            for(int i = 0; i < LEARNTYPECOUNT; i++ ){


                Car car1 = carsWeights[w][i];
                double distances[] = new double[car1.countOfRays];
                for (Line2d l1 : car1.lines) {
                    for (Line2d l2 : mapWalls) {
                        Vector2 colP = l1.colision(l2);
                        if (colP != null) {

                            isCarAliveWeight[w][i] = false;
                            resultsWeight[w][i] =car1.position.subR(map.startStop.p2).length() * distanceVelue + time * timeValue + colisionValue;

                        }
                    }
                }

                for(int dis = 0; dis < distances.length; dis++) distances[dis] = 1;

                for (Line2d l2 : mapWalls) {

                    for (int reyI = 0; reyI < car1.countOfRays; reyI++) {

                        Vector2 colP = car1.rays[reyI].colision(l2);
                        if (colP != null) {
                            g.setColor(Color.RED);
                            if (ISRENDERRAYS) car1.rays[reyI].render(g, cam);
                            g.setColor(Color.BLUE);

                            double currnetDistance = car1.position.subR(colP).length();
                            if (distances[reyI] > currnetDistance / car1.radiusView) {
                                distances[reyI] = currnetDistance / car1.radiusView;
                                if (ISRENDERRAYS)
//                                    g.fillRect((int) colP.subR(cam.position).x, (int) colP.subR(cam.position).y, 10, 10);
                                    drawPoint(colP, 10 , g ,cam);
                            }

                        }
                    }
                }
                if(isCarAliveWeight[w][i]) {
                    double[] vecMove = neuroModelsWeightsLearn[w][i].curk(distances);
//                    double[] vecMove = {Math.random() , Math.random() * 2 -1};

                    if( vecMove[0] > vecMove[1]){
                        car1.moveAcceleraion(1);
                    }else{
                        car1.moveAcceleraion(-1);
                    }


                    if( vecMove[2] > vecMove[3]){
                        car1.rot(1);
                    }else{
                        car1.rot(-1);
                    }

//                    car1.move(vecMove[0]);
//                    car1.rot(vecMove[1]);
                    car1.curc();
                }

                carsWeights[w][i] = car1;

            }
        }
        for(int w = 0; w < countOfALLNeurons; w++){
            for(int i = 0; i < LEARNTYPECOUNT; i++ ){

                Car car1 = carsNeurons[w][i];
                double distances[] = new double[car1.countOfRays];
                for (Line2d l1 : car1.lines) {
                    for (Line2d l2 : mapWalls) {
                        Vector2 colP = l1.colision(l2);
                        if (colP != null) {

                            isCarAliveNeurons[w][i] = false;
                            resultsNeuron[w][i] =car1.position.subR(map.startStop.p2).length() * distanceVelue + time * timeValue + colisionValue;

                        }
                    }
                }
                for(int dis = 0; dis < distances.length; dis++) distances[dis] = 1;


                for (Line2d l2 : mapWalls) {

                    for (int reyI = 0; reyI < car1.countOfRays; reyI++) {

                        Vector2 colP = car1.rays[reyI].colision(l2);
                        if (colP != null) {
                            g.setColor(Color.RED);
                            if (ISRENDERRAYS){
                                g.setColor(Color.RED);
                                car1.rays[reyI].render(g, cam);
                                g.setColor(Color.BLUE);
                            };


                            double currnetDistance = car1.position.subR(colP).length();
                            if (distances[reyI] > currnetDistance / car1.radiusView) {
                                distances[reyI] = currnetDistance / car1.radiusView;
                                if (ISRENDERRAYS)
                                    g.fillRect((int) colP.subR(cam.position).x, (int) colP.subR(cam.position).y, 10, 10);
                            }

                        }
                    }
                }
                if(isCarAliveNeurons[w][i]) {
                    double[] vecMove = neuroModelsNeuronsLearn[w][i].curk(distances);
//                    double[] vecMove = {Math.random() , Math.random() * 2 -1};
//                    car1.move(vecMove[0]);
//                    car1.rot(vecMove[1]);
//                    double[] vecMove = {Math.random() , Math.random() * 2 -1};

                    if( vecMove[0] > vecMove[1]){
                        car1.moveAcceleraion(1);
                    }else{
                        car1.moveAcceleraion(-1);
                    }


                    if( vecMove[2] > vecMove[3]){
                        car1.rot(1);
                    }else{
                        car1.rot(-1);
                    }
                    car1.curc();
                }
                carsNeurons[w][i] = car1;



            }
        }


//            for (int i = 0; i < car1.rays.length; i++) {
//                distances[carIndex][i] = 1;
//                for (Line2d l2 : mapWalls) {
//                    Vector2 colP = car1.rays[i].colision(l2);
//                    if (colP != null) {
//                        g.setColor(Color.RED);
//                        if (ISRENDERRAYS) car1.rays[i].render(g, cam);
//                        g.setColor(Color.BLUE);
//
//                        double currnetDistance = car1.position.subR(colP).length();
//                        if (distances[carIndex][i] > currnetDistance / car1.radiusView) {
//                            distances[carIndex][i] = currnetDistance / car1.radiusView;
//                            if (ISRENDERRAYS)
//                                g.fillRect((int) colP.subR(cam.position).x, (int) colP.subR(cam.position).y, 10, 10);
//                        }
//
//                    }
//                }
//            }




        //Colision
        if(CAMERAMODE == 1)
        cam.position = carsWeights[0][(int) ((LEARNTYPECOUNT-1.0) /2)].position.subR(new Vector2(750, 500));
        if(ISRENDERCAR1)
        carsWeights[0][(int) ((LEARNTYPECOUNT-1.0) /2)].render(g, cam);

        if( ISRENDERRAY){
            for(Line2d ray: carsWeights[0][(int) ((LEARNTYPECOUNT-1.0) /2)].rays) {
                boolean isRendered = false;
                for (Line2d mapLine : mapWalls) {
                    Vector2 colP = ray.colision(mapLine);

                    if( colP != null){
                        g.setColor(Color.RED);
                        drawPoint(colP, 6, g, cam);
                        g.setColor(Color.BLUE);
                        Line2d lineColision = new Line2d(ray.p1 , colP) ;
                        lineColision.render(g, cam);
                        isRendered = true;
                    }//else ray.render(g, cam);
                }
                if(!isRendered){
                    ray.render(g, cam);
                }
            }
        }
        if(ISRENDERCAR) {
            for(int w = 0; w < countOfAllWeght; w++) {
                for (int i = 0; i < LEARNTYPECOUNT; i++) {

                    carsWeights[w][i].render(g, cam);
                }
            }
            for(int w = 0; w < countOfALLNeurons; w++) {
                for (int i = 0; i < LEARNTYPECOUNT; i++) {

                    carsNeurons[w][i].render(g, cam);
                }
            }
        }


        if(ISRENDERFIRSVIE) {
            Car car1 = carsWeights[0][(int) ((LEARNTYPECOUNT-1.0) /2)];
            double distances[] = new double[car1.countOfRays];

            for(int dR= 0; dR < distances.length; dR++){
                distances[dR] = car1.radiusView;
            }

            for (Line2d l2 : mapWalls) {

                for (int reyI = 0; reyI < car1.countOfRays; reyI++) {

                    Vector2 colP = car1.rays[reyI].colision(l2);
                    if (colP != null) {


                        double currnetDistance = car1.position.subR(colP).length();
                        if (distances[reyI] > currnetDistance ) {
                            distances[reyI] = currnetDistance ;

                        }

                    }
                }
            }


            for (int i = 0; i < distances.length; i++) {
                g.fillRect(i * WINDOWWITH / car1.countOfRays*2, WINDOWHEINGH / 3 -250, WINDOWWITH / car1.countOfRays *2, (int) -(distances[i] - 500));
                g.fillRect(i * WINDOWWITH / car1.countOfRays*2, WINDOWHEINGH / 3- 250, WINDOWWITH / car1.countOfRays *2, (int) (distances[i] - 500));
            }
        }


        if(ISRENDERMAP)
        map.render(g, cam);
        if(isMapEditinEctive) map.startStop.render(g, cam);

        boolean isOneAlive = false;
        for(int j = 0; j <LEARNTYPECOUNT; j++){
            for(int i = 0 ; i < countOfAllWeght; i ++){
                if(isCarAliveWeight[i][j]) isOneAlive = true;

            }
            for(int i = 0 ; i < countOfALLNeurons; i++){
                if(isCarAliveNeurons[i][j]) isOneAlive = true;

            }

        }

        time++;
        if(time > MAXTIMEFORGENERATION){

            for(int j = 0; j <LEARNTYPECOUNT; j++){
                for(int i = 0 ; i < countOfAllWeght; i ++){
                    if(isCarAliveWeight[i][j]) {
                        resultsWeight[i][j] =carsWeights[i][j].position.subR(map.startStop.p2).length() * distanceVelue + time * timeValue;
                    }

                }
                for(int i = 0 ; i < countOfALLNeurons; i++){
                    if(isCarAliveNeurons[i][j]) {
                        resultsNeuron[i][j] =carsNeurons[i][j].position.subR(map.startStop.p2).length() * distanceVelue + time * timeValue;
                    }

                }

            }
            time = 0;
        }


        if(!isOneAlive) time = 0;





    }

    public Main() {
        map.readFromFile("map" +mapIndex+ ".txt");
        mapWalls.addAll(map.wall1);
        mapWalls.addAll(map.wall2);



        new Car(new Vector2(map.startStop.p1),  0 ,100, 50, COUNTOFRAYS, radiusVIEW, CarFov);

        countOfAllWeght = nModel.countOfInpunts * nModel.heightOfModel + (nModel.wightOfModel - 1) * nModel.heightOfModel * nModel.heightOfModel + nModel.heightOfModel * nModel.countofOutput;
        countOfALLNeurons = nModel.wightOfModel * nModel.heightOfModel + nModel.countofOutput;

        System.out.println("countOfAllWeght: " + countOfAllWeght + " countOfALLNeurons: " + countOfALLNeurons);

        nModel.readFromFile("N_" + "1" + "_" + nModel.countOfInpunts + "_" + nModel.wightOfModel + "_" +nModel.heightOfModel + "_" + nModel.countofOutput);

        neuroModelsWeightsLearn = new NeuroModel[countOfAllWeght][LEARNTYPECOUNT];
        neuroModelsNeuronsLearn = new NeuroModel[countOfALLNeurons][LEARNTYPECOUNT];



        carsWeights = new Car[countOfAllWeght][LEARNTYPECOUNT];
        carsNeurons = new Car[countOfALLNeurons][LEARNTYPECOUNT];

        resultsWeight = new double[countOfAllWeght][LEARNTYPECOUNT];
        resultsNeuron = new double[countOfALLNeurons][LEARNTYPECOUNT];

        for(int i = 0 ; i< countOfAllWeght; i++){
            for(int j = 0; j < LEARNTYPECOUNT; j ++){
                resultsWeight[i][j] =Double.MAX_VALUE;
            }
        }
        for(int i = 0 ; i< countOfALLNeurons; i++){
            for(int j = 0; j < LEARNTYPECOUNT; j ++){
                resultsNeuron[i][j] =Double.MAX_VALUE;
            }
        }

        isCarAliveWeight =  new boolean[countOfAllWeght][LEARNTYPECOUNT];
        isCarAliveNeurons =  new boolean[countOfALLNeurons][LEARNTYPECOUNT];

        setLearn();




        Timer timer = new Timer(16, e -> repaint()); // ~60 FPS = 16
//        Timer timer = new Timer(0, e -> repaint()); // ~60 FPS = 16
//        Timer timer = new Timer(0, e -> repaint()); // ~60 FPS = 16

        timer.start();

        addMouseListener(new MouseAdapter() {

        @Override
        public void mouseClicked(MouseEvent e) {
            if (isMapEditinEctive & (e.getButton() == MouseEvent.BUTTON1)){
                map.addWall(activeWall, new Vector2(e.getPoint().getLocation().x,e.getPoint().getLocation().y), cam);
            }
            repaint();
        }
        @Override
        public void mousePressed(MouseEvent e) {
            if (e.getButton() == MouseEvent.BUTTON1) {
                leftPressed = true;
            }
            if (e.getButton() == MouseEvent.BUTTON3) {
                rightPressed = true;
            }
            repaint();
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            if (e.getButton() == MouseEvent.BUTTON1) {
                leftPressed = false;
            }
            if (e.getButton() == MouseEvent.BUTTON3) {
                rightPressed = false;
            }
            repaint();
        }
        });

        setFocusable(true);
        requestFocusInWindow();

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
        // S — зберегти
        if (e.getKeyCode() == KeyEvent.VK_I) {
            map.safeToFile("map" +mapIndex+ ".txt");
            System.out.println("Map saved " + " map" +mapIndex+ " .txt");
        }
        if (e.getKeyCode() == KeyEvent.VK_E) {
            if(isMapEditinEctive){
                isMapEditinEctive = false;
                System.out.println("Map editing disactive");
            }else {
                isMapEditinEctive = true;
                System.out.println("Map editing active");
            }
        }
        if (e.getKeyCode() == KeyEvent.VK_O) {
            map.readFromFile("map" +mapIndex+ ".txt");
            System.out.println("Map open");
        }
        if (e.getKeyCode() == KeyEvent.VK_K) {
            nModel.safeToFile("N_" + "1" + "_" + nModel.countOfInpunts + "_" + nModel.wightOfModel + "_" +nModel.heightOfModel + "_" + nModel.countofOutput);
            System.out.println("NeuroModel saved");
        }
        if (e.getKeyCode() == KeyEvent.VK_J) {
            nModel.readFromFile("N_" + "1" + "_" + nModel.countOfInpunts + "_" + nModel.wightOfModel + "_" +nModel.heightOfModel + "_" + nModel.countofOutput);
            System.out.println("NeuroModel open");
            System.out.println("N_" + "1" + "_" + nModel.countOfInpunts + "_" + nModel.wightOfModel + "_" +nModel.heightOfModel + "_" + nModel.countofOutput);
            setLearn();
            time = 0;

        }

        if (e.getKeyCode() == KeyEvent.VK_L) {
            nModel.setRandom(1,1);
            for(int gWeight = 0; gWeight < countOfAllWeght; gWeight++){
                for(int i = 0; i < LEARNTYPECOUNT; i++) {
                    neuroModelsWeightsLearn[gWeight][i].setRandom(1,1);

                }
            }
            for(int gNeurons = 0; gNeurons < countOfALLNeurons; gNeurons++){
                for(int i = 0; i < LEARNTYPECOUNT; i++) {
                    neuroModelsNeuronsLearn[gNeurons][i].setRandom(1,1);

                }
            }

            System.out.println("MeuronsRandom");
        }
        if (e.getKeyCode() == KeyEvent.VK_W) {
            carMoument = 1;
        }
        if (e.getKeyCode() == KeyEvent.VK_S) {
            carMoument = -1;
        }
        if (e.getKeyCode() == KeyEvent.VK_A) {
            carR = -1;
        }
        if (e.getKeyCode() == KeyEvent.VK_D) {
            carR = 1;
        }
        if (e.getKeyCode() == KeyEvent.VK_C) {
            CAMERAMODE = 1;
        }
        if (e.getKeyCode() == KeyEvent.VK_V) {
            CAMERAMODE = 0;
        }
        if (e.getKeyCode() == KeyEvent.VK_1) {
            if (isMapEditinEctive) activeWall = 1;

            System.out.println("Map Editing : Wall_1");
        }
        if (e.getKeyCode() == KeyEvent.VK_2) {
            if (isMapEditinEctive) activeWall = 2;

            System.out.println("Map Editing : Wall_2");
        }
        if (e.getKeyCode() == KeyEvent.VK_3) {
            if (isMapEditinEctive) activeWall = 3;

            System.out.println("Map Editing : Wall_3");
        }
            if (e.getKeyCode() == KeyEvent.VK_4) {
                if (isMapEditinEctive) activeWall = 4;

                System.out.println("Map Editing : Wall_4");
            }
            }
        });
        }

    public static void main(String[] args) {

        JFrame frame = new JFrame("Java 2D");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(WINDOWHEINGH, WINDOWWITH);
        frame.add(new Main());
        frame.setVisible(true);

    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}