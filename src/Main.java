import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.MouseInfo;
import java.util.ArrayList;

public class Main extends JPanel implements MouseListener{
    static int WINDOWHEINGH= 1500;
    static int WINDOWWITH = 1000;

    int CAMERAMODE = 1; /// 1 -> on car
    static int MAXTIMEFORGENERATION = 10000;


    static final boolean ISRENDERCAR= true;
    static final boolean ISRENDERRAYS= false;
    static final boolean ISRENDERMAP= true;

    static final boolean ISRENDERFIRSVIE= false;



    int COUNTOFRAYS = 10;
    int COUNTOFCAR = 10;
    //learn
    int timeValue = 1;
    int distanceVelue = 10;

    boolean isRenderMap = true;

    double carMoument = 0;
    double carR = 0;
    boolean leftPressed = false;
    boolean rightPressed = false;

    int time = 0;

    boolean isMapEditinEctive = true;
    int activeWall= 1;

    NeuroModel nModel = new NeuroModel(COUNTOFRAYS,2,3,2);

    Map map = new Map();
    ArrayList<Line2d> mapWalls = new ArrayList<>();
    Vector2 mouse = new Vector2();
    Vector2 mouseDelta = new Vector2();

    Vector2 oldmouse = new Vector2();
    Camera cam = new Camera();



//    Car car1 = new Car(new Vector2(0,0), 0 ,100, 50, 12, 500, 240d);



    //Car cars[] = new Car[COUNTOFCAR];


    double resultsWeight[][];
    double resultsNeuron[][];

    Car carsWeights[][];
    Car carsNeurons[][];

    NeuroModel neuroModelsWeightsOrig[] ;
    NeuroModel neuroModelsNeuronsOrig[] ;

    NeuroModel neuroModelsWeightsLearn[][] ;
    NeuroModel neuroModelsNeuronsLearn[][] ;

    final static int LEARNTYPECOUNT = 3;


    boolean isCarAliveWeight[][];
    boolean isCarAliveNeurons[][];
//    Vector2 carsResults[][];

    int currentWeight= 0;
    int currentNeuron= 0;

    int countOfAllWeght;
    int countOfALLNeurons;
    int[] currentWeightInProgress = new int[COUNTOFCAR];

    int currentGeneration = 0;

    double deltaLearn = 1;
// Vector2[] carsResults


    void setLearn(){
        for(int gWeight = 0; gWeight < countOfAllWeght; gWeight++){
            neuroModelsWeightsLearn[gWeight][0] = new NeuroModel(nModel);
            neuroModelsWeightsLearn[gWeight][1] = new NeuroModel(nModel);
            neuroModelsWeightsLearn[gWeight][2] = new NeuroModel(nModel);
        }
        for(int gNeurons = 0; gNeurons < countOfALLNeurons; gNeurons++){
            neuroModelsNeuronsLearn[gNeurons][0] = new NeuroModel(nModel);
            neuroModelsNeuronsLearn[gNeurons][1] = new NeuroModel(nModel);
            neuroModelsNeuronsLearn[gNeurons][2] = new NeuroModel(nModel);
        }

        for(int w = 0; w < countOfAllWeght; w++) {
            for (int i = 0; i < LEARNTYPECOUNT; i++) {
                carsWeights[w][i] =  new Car(new Vector2(map.startStop.p1),  0 ,100, 50, COUNTOFRAYS, 500, 240);
            }
        }
        for(int w = 0; w < countOfALLNeurons; w++) {
            for (int i = 0; i < LEARNTYPECOUNT; i++) {
                carsNeurons[w][i] =  new Car(new Vector2(map.startStop.p1),  0 ,100, 50, COUNTOFRAYS, 500, 240);
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
            }
            for(int i = 0 ; i < countOfALLNeurons; i++){
                isCarAliveNeurons[i][j] = true;
                carsNeurons[i][j].position = new Vector2(map.startStop.p1);
                carsNeurons[i][j].rotation = 0;
            }

        }
    }

    void changeLearn(double mylt){
        for(int gWeight = 0; gWeight < countOfAllWeght; gWeight++){

            neuroModelsWeightsLearn[gWeight][1].changeWeith(gWeight, mylt);
            neuroModelsWeightsLearn[gWeight][2].changeWeith(gWeight, mylt);
        }
        for(int gNeurons = 0; gNeurons < countOfALLNeurons; gNeurons++){

            neuroModelsNeuronsLearn[gNeurons][1].changeB(gNeurons, mylt);
            neuroModelsNeuronsLearn[gNeurons][2].changeB(gNeurons, mylt );
        }
    }


    void learn(){

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
        }


        for(int w = 0; w < countOfAllWeght; w++){
            for(int i = 0; i < LEARNTYPECOUNT; i++ ){

                Car car1 = carsWeights[w][i];
                double distances[] = new double[car1.countOfRays];
                for (Line2d l1 : car1.lines) {
                    for (Line2d l2 : mapWalls) {
                        Vector2 colP = l1.colision(l2);
                        if (colP != null) {
//                            g.setColor(Color.RED);
                            //                    if(ISRENDERCAR) l1.render(g,cam);
//                            g.setColor(Color.BLUE);
                            //                        car1.position = new Vector2(-200, 0);
                            //                        car1.rotation = 0;
                            isCarAliveWeight[w][i] = false;
                            resultsWeight[w][i] =car1.position.subR(map.startStop.p2).length() * distanceVelue + time * timeValue;

                        }
                    }
                }

                for (Line2d l2 : mapWalls) {

                    for (int reyI = 0; reyI < car1.countOfRays; reyI++) {
                        distances[reyI] = 1;
                        Vector2 colP = car1.rays[reyI].colision(l2);
                        if (colP != null) {
                            g.setColor(Color.RED);
                            if (ISRENDERRAYS) car1.rays[reyI].render(g, cam);
                            g.setColor(Color.BLUE);

                            double currnetDistance = car1.position.subR(colP).length();
                            if (distances[reyI] > currnetDistance / car1.radiusView) {
                                distances[reyI] = currnetDistance / car1.radiusView;
                                if (ISRENDERRAYS)
                                    g.fillRect((int) colP.subR(cam.position).x, (int) colP.subR(cam.position).y, 10, 10);
                            }

                        }
                    }
                }
                if(isCarAliveWeight[w][i]) {
                    double[] vecMove = neuroModelsWeightsLearn[w][i].curk(distances);

                    car1.move(vecMove[0]);
                    car1.rot(vecMove[1]);
                    car1.curc();
                }

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
        cam.position = carsWeights[0][0].position.subR(new Vector2(750, 500));

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

//
//        if(ISRENDERFIRSVIE) {
//            for (int i = 0; i < distances.length; i++) {
//                g.fillRect(i * WINDOWWITH / cars[0].countOfRays, WINDOWHEINGH / 3, WINDOWWITH / cars[0].countOfRays, (int) -(distances[0][i] - 500));
//                g.fillRect(i * WINDOWWITH / cars[0].countOfRays, WINDOWHEINGH / 3, WINDOWWITH / cars[0].countOfRays, (int) (distances[0][i] - 500));
//            }
//        }
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
        if(!isOneAlive) time = 0;


    }

    public Main() {
        map.readFromFile("map.txt");
        mapWalls.addAll(map.wall1);
        mapWalls.addAll(map.wall2);

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

        isCarAliveWeight =  new boolean[countOfAllWeght][LEARNTYPECOUNT];
        isCarAliveNeurons =  new boolean[countOfALLNeurons][LEARNTYPECOUNT];

        setLearn();




        Timer timer = new Timer(16, e -> repaint()); // ~60 FPS
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
            map.readFromFile("map.txt");
            System.out.println("Map open");
        }
        if (e.getKeyCode() == KeyEvent.VK_K) {
            nModel.safeToFile("N_" + "1" + "_" + nModel.countOfInpunts + "_" + nModel.wightOfModel + "_" +nModel.heightOfModel + "_" + nModel.countofOutput);
            System.out.println("NeuroModel saved");
        }
        if (e.getKeyCode() == KeyEvent.VK_J) {
            nModel.readFromFile("N_" + "1" + "_" + nModel.countOfInpunts + "_" + nModel.wightOfModel + "_" +nModel.heightOfModel + "_" + nModel.countofOutput);
            System.out.println("NeuroModel open");
        }

        if (e.getKeyCode() == KeyEvent.VK_L) {
            nModel.setRandom(1,1);
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