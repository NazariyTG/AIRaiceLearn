import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class MainCarControlWithBot extends JPanel implements MouseListener{
//    static int WINDOWHEINGH= 1500;
//    static int WINDOWWITH = 1000;

    int CAMERAMODE = 1; /// 1 -> on car
    static int MAXTIMEFORGENERATION = 2000;

    int distanceTOwin = 0;

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

    Vector2 centerOfSCreen = new Vector2(Config.WINDOWWITH, Config.WINDOWHEINGH);
    Vector2 centerOfScreenWithRay = new Vector2 (Config.WINDOWHEINGH /2, Config.WINDOWWITH/2 + Config.WINDOWHEINGH/8);
    Vector2 centerOfScreenWithoutRay = new Vector2(Config.WINDOWHEINGH /2, Config.WINDOWWITH/2);

    //neuro
    int NOFMODEL = 5;
    int countOfInput = 21;
    int wightModel = 3;
    int heightOfModel = 11;

    int CountOfOutput = 5;

    //bot settings
    int botSettingNeuro[][] = {{4,11,3,11}};






    // ray settings
    int CarFov = 240; /// 240
    int radiusVIEW = 700;
    ///  cars
    int countOfCar = 1;

    int wightCar = 80;
    int heightCar = 40;

    Vector2 botSpawingPosCorrector = new Vector2(400, -50);
    /// bot neurons
    int NOFMODELBot = 4;
    int countOfInputBot = 11;
    int wightModelBot = 3;
    int heightOfModelBot = 11;

    int CountOfOutputBot = 5;

    NeuroModel[] botsNeuro = {new NeuroModel(countOfInputBot, wightModelBot,heightOfModelBot,CountOfOutputBot)};
    //botsNeuro[0].readFromFile("N_" + NOFMODELBot + "_" + botsNeuro[0].countOfInpunts + "_" + botsNeuro[0].wightOfModel + "_" +botsNeuro[0].heightOfModel + "_" + botsNeuro[0].countofOutput);




    // map settings
    int mapIndex = 4;

    double FOVCOF = 0.7;


    NeuroModel nModel = new NeuroModel(countOfInput,wightModel,heightOfModel,CountOfOutput);
    NeuroModel[] BotsNeuro;


    double carMoument = 0;
    double carR = 0;

    boolean leftPressed = false;
    boolean rightPressed = false;
    int wheelSpin = 0;

    int time = 0;

    boolean isMapEditinEctive = true;
    int activeWall= 1;



    Map map = new Map();
    ArrayList<Line2d> mapWalls = new ArrayList<>();


    Vector2 oldmouse = new Vector2();
    Camera cam = new Camera();


    int countOfAllWeght;
    int countOfALLNeurons;

    Car myCar;
    Car botCar =  new Car(new Vector2(map.startStop.p1.addR(botSpawingPosCorrector)),  0 ,wightCar, heightCar, countOfInput -1, radiusVIEW, CarFov);
    Traektoria tr;

    Car[] cars = new Car[countOfCar];


    NeuronsView nView ;



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


        //if(ISRENDERMAP) map.render(g, cam);

        if(isMapEditinEctive) map.startStop.render(g,cam);

//        botCar.moveByNeuroRender(g,cam,map, nModel);
        for(int i = 0; i < countOfCar; i++){
            cars[i].moveByNeuroRender(g,cam, map, botsNeuro[0]);
//           w cars[i].render(g,cam);
        }

        if(ISCARNEURONS){
            myCar.moveByNeuroRenderWithOtherCar(g, cam, map,nModel,cars, botsNeuro);

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
            g.fillRect(0 ,0 ,Config.WINDOWHEINGH, 300);
            g.setColor(Color.BLUE);
            for (int i = 0; i < myCar.rays.length; i++) {
                g.fillRect((int) (i * Config.WINDOWHEINGH / myCar.countOfRays*FOVCOF), Config.WINDOWHEINGH / 3 -380, (int) ( Config.WINDOWHEINGH / myCar.countOfRays *FOVCOF), (int) -((1 -distances[i]) *150));
                g.fillRect((int) (i * Config.WINDOWHEINGH / myCar.countOfRays*FOVCOF), Config.WINDOWHEINGH / 3- 380, (int) ( Config.WINDOWHEINGH / myCar.countOfRays* FOVCOF), (int) ((1- distances[i]) * 150));
            }
            double[] distancesToCar = myCar.distancesToOtherCars(cars);
            g.setColor(Color.RED);
            for (int i = 0; i < myCar.rays.length; i++) {
                g.fillRect((int) (i * Config.WINDOWHEINGH / myCar.countOfRays*FOVCOF), Config.WINDOWHEINGH / 3 - 300, (int) (Config.WINDOWHEINGH / myCar.countOfRays *FOVCOF), (int) -((1 -distancesToCar[i]) *50));
                g.fillRect((int) (i * Config.WINDOWHEINGH / myCar.countOfRays*FOVCOF), Config.WINDOWHEINGH / 3 - 300, (int) ( Config.WINDOWHEINGH / myCar.countOfRays* FOVCOF), (int) ((1- distancesToCar[i]) * 50));
            }
            g.setColor(Color.BLUE);
        }



        time++;

        if(CAMERAMODE == 1)
        cam.position = myCar.position.subR(centerOfSCreen);





//        botCar.curkCarTraektorieRender(g, cam, map, nModel,distanceTOwin,MAXTIMEFORGENERATION,timeValue,distanceVelue,colisionValue);
//        botCar.curkCarResultRender(g, cam, map, nModel,distanceTOwin,MAXTIMEFORGENERATION,timeValue,distanceVelue,colisionValue);
//        System.out.println("speed " + myCar.speed);
        Font oldFont = g.getFont();

        g.setFont(oldFont.deriveFont(30f));
        g.drawString("speed: " +(int) myCar.speed, 60, Config.WINDOWWITH - 60);





        if(ISRENDERMAP) map.render(g, cam);

        if(ISCARSTOP) {
            carMoument = 0;
            carR = 0;
        }
//        myCar.render(g, cam);
        if(myCar.isColisionWith(mapWalls) ) {
//            botCar = new Car(new Vector2(map.startStop.p1.addR(botSpawingPosCorrector)),  0 ,100, 50, countOfInput -1, radiusVIEW, CarFov);

            myCar = new Car(new Vector2(map.startStop.p1), 0, wightCar, heightCar,( countOfInput - 1) /2, radiusVIEW, CarFov);
            setCars();
        }
        if(myCar.isColisionWithCars(cars) ) {
            botCar = new Car(new Vector2(map.startStop.p1.addR(botSpawingPosCorrector)),  0 ,wightCar, heightCar, countOfInputBot -1, radiusVIEW, CarFov);

            myCar = new Car(new Vector2(map.startStop.p1), 0, wightCar, heightCar,( countOfInput - 1) /2, radiusVIEW, CarFov);
            setCars();
        }


        nView.renderRealTime(g,mouseDelta, rightPressed, wheelSpin, myCar.inputsLokal);


    }

    void setCars(){
        for( int i = 0; i < countOfCar; i++){
            cars[i] =  new Car(new Vector2(map.startStop.p1.addR(botSpawingPosCorrector)),  0 ,wightCar, heightCar, (countOfInputBot -1), radiusVIEW, CarFov);
            cars[i].speedLimiter = 27;
        }
    }

    public MainCarControlWithBot() {
        map.readFromFile("map" +mapIndex+ ".txt");
        mapWalls.addAll(map.wall1);
        mapWalls.addAll(map.wall2);
        myCar =  new Car(new Vector2(map.startStop.p1),  0 ,wightCar, heightCar, (countOfInput -1)/2, radiusVIEW, CarFov);


        countOfAllWeght = nModel.countOfInpunts * nModel.heightOfModel + (nModel.wightOfModel - 1) * nModel.heightOfModel * nModel.heightOfModel + nModel.heightOfModel * nModel.countofOutput;
        countOfALLNeurons = nModel.wightOfModel * nModel.heightOfModel + nModel.countofOutput;

        System.out.println("countOfAllWeght: " + countOfAllWeght + " countOfALLNeurons: " + countOfALLNeurons);

        nModel.readFromFile("N_" + NOFMODEL + "_" + nModel.countOfInpunts + "_" + nModel.wightOfModel + "_" +nModel.heightOfModel + "_" + nModel.countofOutput);

        Car traektoriaCar =  new Car(new Vector2(map.startStop.p1),  0 ,wightCar, heightCar, (countOfInput -1)/2, radiusVIEW, CarFov);
        botsNeuro[0].readFromFile("N_" + NOFMODELBot + "_" + botsNeuro[0].countOfInpunts + "_" + botsNeuro[0].wightOfModel + "_" +botsNeuro[0].heightOfModel + "_" + botsNeuro[0].countofOutput);
        setCars();
        tr = traektoriaCar.curkCarTraektorieWithOtherCar(map, nModel, cars,botsNeuro ,distanceTOwin,MAXTIMEFORGENERATION,timeValue,distanceVelue,colisionValue);


        nView = new NeuronsView(nModel);
        setCars();


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



        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
        // S — зберегти
        if (e.getKeyCode() == KeyEvent.VK_I) {
            map.safeToFile("map"+mapIndex+ ".txt");
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
                    //neuroModelsWeightsLearn[gWeight][i].setRandom(1,1);

                }
            }
            for(int gNeurons = 0; gNeurons < countOfALLNeurons; gNeurons++){
                for(int i = 0; i < LEARNTYPECOUNT; i++) {
                    //neuroModelsNeuronsLearn[gNeurons][i].setRandom(1,1);

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

        addMouseWheelListener(e -> {
            wheelSpin -= e.getWheelRotation();


        });


        setFocusable(true);
        requestFocusInWindow();
        }

    public static void main(String[] args) {

        JFrame frame = new JFrame("Java 2D");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(Config.WINDOWHEINGH, Config.WINDOWWITH);
        frame.add(new MainCarControlWithBot());
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