import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class MainCarControl extends JPanel implements MouseListener{
    static int WINDOWHEINGH= 1500;
    static int WINDOWWITH = 1000;

    int CAMERAMODE = 1; /// 1 -> on car
    static int MAXTIMEFORGENERATION = 700;

    int distanceTOwin = 100;

    //learn
    int timeValue = 2;
    int distanceVelue = 10;
    int colisionValue = 2000;

    final static int LEARNTYPECOUNT = 11;  // neparni ///////////////////////////

//        double myltipliers[] = {2, 0.2 , 0.02, 0.002, 0.0002,0.00002, 20, 200,2000, };
    double myltipliers[] = {2, 0.2 , 0.02,   };
    //    double myltipliers[] = { 0.02, 0.002, 0.0002, 0.000002,0.00000002,0.000000002};
//    double myltipliers[] = {0.2 ,2};
    int curentMultp = 0;

    static boolean ISRENDERCAR1= true;
    static boolean ISRENDERCAR= true;
    static  boolean ISRENDERRAYS= false;
    static boolean ISRENDERRAY = false;
    boolean ISRENDERMAP= true;

    boolean ISRENDERTRAEKTORIE = true;

    static final boolean ISRENDERFIRSVIE= true;

    boolean ISCARSTOP = false;
    boolean ISCARNEURONS = true;

    Vector2 centerOfSCreen = new Vector2(WINDOWWITH, WINDOWHEINGH);
    Vector2 centerOfScreenWithRay = new Vector2 (WINDOWHEINGH /2, WINDOWWITH/2 + WINDOWHEINGH/8);
    Vector2 centerOfScreenWithoutRay = new Vector2(WINDOWHEINGH /2, WINDOWWITH/2);

    int NOFMODEL = 4;
//    int COUNTOFRAYS = 10;
    int countOfInput = 11;

    int CountOfOutput = 5;

    NeuroModel nModel = new NeuroModel(countOfInput,3,11,CountOfOutput);


    int CarFov = 240; /// 240

    int radiusVIEW = 700;

    int mapIndex = 4;

    double FOVCOF = 0.7;


    boolean isRenderMap = true;

    double carMoument = 0;
    double carR = 0;
    boolean leftPressed = false;
    boolean rightPressed = false;

    int time = 0;

    boolean isMapEditinEctive = true;
    int activeWall= 1;



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

    double deltaLearn = 1;
// Vector2[] carsResults



    Car myCar;
    Car botCar =  new Car(new Vector2(map.startStop.p1),  0 ,100, 50, countOfInput -1, radiusVIEW, CarFov);
    Traektoria tr;

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
                carsWeights[w][i] =  new Car(new Vector2(map.startStop.p1),  0 ,100, 50, countOfInput -1, radiusVIEW, CarFov);
            }
        }
        for(int w = 0; w < countOfALLNeurons; w++) {
            for (int i = 0; i < LEARNTYPECOUNT; i++) {
                carsNeurons[w][i] =  new Car(new Vector2(map.startStop.p1),  0 ,100, 50, countOfInput - 1, radiusVIEW, CarFov);
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

                    if(j != (LEARNTYPECOUNT-1.0) /2 ) {
//                    globalMinResult = resultsWeight[i][j];
//                    nModel.setWeith(i, neuroModelsWeightsLearn[i][j].getWeith(i));
                        globalMinResult = resultsWeight[i][j];
                        globalMinIndex[0] = i;
                        globalMinIndex[1] = j;
                    }else {
                        if(coutSameNeurons < myltipliers.length){
                            globalMinResult = resultsWeight[i][j];
                            globalMinIndex[0] = i;
                            globalMinIndex[1] = j;
                        }

                    }
                }
            }

//            nModel.setWeith(i, neuroModelsWeightsLearn[i][minJ].getWeith(i));

        }
        for(int i = 0; i < countOfALLNeurons; i++){
//            double lMinResult=  resultsNeuron[0][0];

            for(int j = 0; j < LEARNTYPECOUNT; j++){
                if (resultsNeuron[i][j] < globalMinResult){
                    if(j != (LEARNTYPECOUNT-1.0) /2 ) {
                        globalMinResult = resultsNeuron[i][j];
                        globalMinIndex[0] = i;
                        globalMinIndex[1] = j;
                        isItWeight = false;
//                    nModel.setB(i, neuroModelsNeuronsLearn[i][j].getB(i));
                    }else {
                        if(coutSameNeurons < myltipliers.length){
                            globalMinResult = resultsNeuron[i][j];
                            globalMinIndex[0] = i;
                            globalMinIndex[1] = j;
                            isItWeight = false;
                        }
                    }

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

        Point mouseP = MouseInfo.getPointerInfo().getLocation();
        SwingUtilities.convertPointFromScreen(mouseP, this);
        Vector2 mouse = new Vector2(mouseP.x, mouseP.y);
        Vector2 mouseDelta = mouse.subR(oldmouse);
        if (rightPressed) cam.position.sub(mouseDelta);
        oldmouse = mouse;

        if(ISRENDERRAY)centerOfSCreen = centerOfScreenWithRay;
        else centerOfSCreen = centerOfScreenWithoutRay;

        // colision

        g.setColor(Color.BLUE);


        if(ISRENDERTRAEKTORIE) tr.render(g,cam);


        if(ISRENDERMAP) map.render(g, cam);


        if(ISCARNEURONS){
            myCar.moveByNeuro(g, cam, map,nModel);

        }else {
            myCar.moveAcceleraion(carMoument);
            myCar.rot(carR);
            myCar.render(g, cam);
            myCar.curc();
        }

        if(ISRENDERRAY) {
            double[] distances;
            if(ISRENDERMAP)
                distances = myCar.distancesToWallRenderRay(mapWalls , g ,cam);
            else distances = myCar.distancesToWall(mapWalls);

            g.setColor(Color.WHITE);
            g.fillRect(0 ,0 ,WINDOWHEINGH, 300);
            g.setColor(Color.BLUE);
            for (int i = 0; i < myCar.rays.length; i++) {
                g.fillRect((int) (i * WINDOWHEINGH / myCar.countOfRays*FOVCOF), WINDOWHEINGH / 3 -380, (int) ( WINDOWHEINGH / myCar.countOfRays *FOVCOF), (int) -((1 -distances[i]) *150));
                g.fillRect((int) (i * WINDOWHEINGH / myCar.countOfRays*FOVCOF), WINDOWHEINGH / 3- 380, (int) ( WINDOWHEINGH / myCar.countOfRays* FOVCOF), (int) ((1- distances[i]) * 150));
            }
        }



        time++;

        if(CAMERAMODE == 1)
        cam.position = myCar.position.subR(centerOfSCreen);



        botCar = new Car(new Vector2(map.startStop.p1),  0 ,100, 50, countOfInput -1, radiusVIEW, 240);


//        botCar.curkCarTraektorieRender(g, cam, map, nModel,distanceTOwin,MAXTIMEFORGENERATION,timeValue,distanceVelue,colisionValue);
//        botCar.curkCarResultRender(g, cam, map, nModel,distanceTOwin,MAXTIMEFORGENERATION,timeValue,distanceVelue,colisionValue);
//        System.out.println("speed " + myCar.speed);
        Font oldFont = g.getFont();

        g.setFont(oldFont.deriveFont(30f));
        g.drawString("speed: " +(int) myCar.speed, 60, WINDOWWITH - 60);





        if(ISRENDERMAP) map.render(g, cam);

        if(ISCARSTOP) {
            carMoument = 0;
            carR = 0;
        }
//        myCar.render(g, cam);
        if(myCar.isColisionWith(mapWalls) ) {

            myCar = new Car(new Vector2(map.startStop.p1), 0, 100, 50, countOfInput - 1, radiusVIEW, 240);

        }

    }

    public MainCarControl() {
        map.readFromFile("map" +mapIndex+ ".txt");
        mapWalls.addAll(map.wall1);
        mapWalls.addAll(map.wall2);
        myCar =  new Car(new Vector2(map.startStop.p1),  0 ,100, 50, countOfInput -1, radiusVIEW, CarFov);


        countOfAllWeght = nModel.countOfInpunts * nModel.heightOfModel + (nModel.wightOfModel - 1) * nModel.heightOfModel * nModel.heightOfModel + nModel.heightOfModel * nModel.countofOutput;
        countOfALLNeurons = nModel.wightOfModel * nModel.heightOfModel + nModel.countofOutput;

        System.out.println("countOfAllWeght: " + countOfAllWeght + " countOfALLNeurons: " + countOfALLNeurons);

        nModel.readFromFile("N_" + NOFMODEL + "_" + nModel.countOfInpunts + "_" + nModel.wightOfModel + "_" +nModel.heightOfModel + "_" + nModel.countofOutput);

        Car traektoriaCar =  new Car(new Vector2(map.startStop.p1),  0 ,100, 50, countOfInput -1, radiusVIEW, CarFov);

        tr = traektoriaCar.curkCarTraektorie(map, nModel,distanceTOwin,MAXTIMEFORGENERATION,timeValue,distanceVelue,colisionValue);




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
            map.safeToFile("map.txt");
            System.out.println("Map saved");
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
//            setLearn();
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
        if (e.getKeyCode() == KeyEvent.VK_X) {
            if(ISCARSTOP) ISCARSTOP = false;
            else ISCARSTOP = true;
        }
        if (e.getKeyCode() == KeyEvent.VK_Z) {
            if((ISCARNEURONS)) ISCARNEURONS = false;
            else ISCARNEURONS = true;
        }
        if (e.getKeyCode() == KeyEvent.VK_C) {
            if( CAMERAMODE == 1)CAMERAMODE = 0;
            else CAMERAMODE =1;
        }
//        if (e.getKeyCode() == KeyEvent.VK_V) {
//            CAMERAMODE = 0;
//        }
        if (e.getKeyCode() == KeyEvent.VK_M) {
            if(ISRENDERMAP) ISRENDERMAP = false;
            else  ISRENDERMAP = true;
        }
        if (e.getKeyCode() == KeyEvent.VK_N) {
            if(ISRENDERCAR1) ISRENDERCAR1 = false;
            else  ISRENDERCAR1 = true;
        }
        if (e.getKeyCode() == KeyEvent.VK_B) {
            if(ISRENDERRAY) ISRENDERRAY = false;
            else  ISRENDERRAY = true;
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
            }
        });
        }

    public static void main(String[] args) {

        JFrame frame = new JFrame("Java 2D");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(WINDOWHEINGH, WINDOWWITH);
        frame.add(new MainCarControl());
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