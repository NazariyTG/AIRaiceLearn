import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;


public class MapEditing extends JPanel implements MouseListener {
    static int WINDOWHEINGH= 1500;
    static int WINDOWWITH = 1000;

    int CAMERAMODE = 1; /// 1 -> on car




    boolean ISRENDERMAP= true;

    boolean ISRENDERTRAEKTORIE = true;

    static final boolean ISRENDERFIRSVIE= true;

    boolean ISCARSTOP = false;
    boolean ISCARNEURONS = true;

    Vector2 centerOfSCreen = new Vector2(WINDOWWITH, WINDOWHEINGH);
    Vector2 centerOfScreenWithRay = new Vector2 (WINDOWHEINGH /2, WINDOWWITH/2 + WINDOWHEINGH/8);
    Vector2 centerOfScreenWithoutRay = new Vector2(WINDOWHEINGH /2, WINDOWWITH/2);

    //neuro
    int NOFMODEL = 501;
    int countOfInput = 22;
    int wightModel = 3;
    int heightOfModel = 11;

    int CountOfOutput = 5;

    //bot settings
    int botSettingNeuro[][] = {{4,11,3,11}};

    //learn
    int timeValue = 2;
    int distanceVelue = 10;
    int colisionValue = 2000;

    static int MAXTIMEFORGENERATION = 2000;

    int distanceTOwin = 0;



    // ray settings
    int CarFov = 240; /// 240
    int radiusVIEW = 700;
    ///  cars
    int countOfCar = 1;

    int wightCar = 80;
    int heightCar = 40;

    Vector2 botSpawingPosCorrector = new Vector2(400, -50);
    /// bot neurons
    int NOFMODELBot = 1;
    int countOfInputBot = 21;
    int wightModelBot = 3;
    int heightOfModelBot = 11;

    int CountOfOutputBot = 5;

    NeuroModel[] botsNeuro = {new NeuroModel(countOfInputBot, wightModelBot,heightOfModelBot,CountOfOutputBot)};
    //botsNeuro[0].readFromFile("N_" + NOFMODELBot + "_" + botsNeuro[0].countOfInpunts + "_" + botsNeuro[0].wightOfModel + "_" +botsNeuro[0].heightOfModel + "_" + botsNeuro[0].countofOutput);




    // map settings
    int mapIndex = 4                        ;

    double FOVCOF = 0.7;


    NeuroModel nModel = new NeuroModel(countOfInput,wightModel,heightOfModel,CountOfOutput);
    NeuroModel[] BotsNeuro;


    double carMoument = 0;
    double carR = 0;
    boolean leftPressed = false;
    boolean rightPressed = false;

    int time = 0;

    boolean isMapEditinEctive = true;
    int activeWall= 1;

    boolean ISMOVINGPOINT = false;



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



    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Point mouseP = MouseInfo.getPointerInfo().getLocation();
        SwingUtilities.convertPointFromScreen(mouseP, this);
        Vector2 mouse = new Vector2(mouseP.x, mouseP.y);
        Vector2 mouseDelta = mouse.subR(oldmouse);
        if (rightPressed) cam.position.sub(mouseDelta);
        oldmouse = mouse;




        // colision

        g.setColor(Color.BLUE);
        Car traektoriaCar =  new Car(new Vector2(map.startStop.p1),  0 ,wightCar, heightCar, (countOfInput -1)/2, radiusVIEW, CarFov);
        botsNeuro[0].readFromFile("N_" + NOFMODELBot + "_" + botsNeuro[0].countOfInpunts + "_" + botsNeuro[0].wightOfModel + "_" +botsNeuro[0].heightOfModel + "_" + botsNeuro[0].countofOutput);
        setCars();
        tr = traektoriaCar.curkCarTraektorieWithOtherCar(map, nModel, cars,botsNeuro ,distanceTOwin,MAXTIMEFORGENERATION,timeValue,distanceVelue,colisionValue);



        if (ISRENDERTRAEKTORIE) tr.render(g, cam);




        //if(ISRENDERMAP) map.render(g, cam);

//        if (isMapEditinEctive)
        map.startStop.render(g, cam);


        time++;


//        botCar.curkCarTraektorieRender(g, cam, map, nModel,distanceTOwin,MAXTIMEFORGENERATION,timeValue,distanceVelue,colisionValue);
//        botCar.curkCarResultRender(g, cam, map, nModel,distanceTOwin,MAXTIMEFORGENERATION,timeValue,distanceVelue,colisionValue);


//        if (ISRENDERMAP)
        map.render(g, cam);

        if(!ISMOVINGPOINT){
            if(map.wallsIndexSelection == 0){
                if(map.selectionPoint < map.wall1.size())
                map.wall1.get(map.selectionPoint).p1.drawPoint(10,g,cam);
                else map.wall1.get(map.selectionPoint -1).p2.drawPoint(10,g,cam);
            }
            if(map.wallsIndexSelection == 1){
                if(map.selectionPoint < map.wall1.size())
                    map.wall2.get(map.selectionPoint).p1.drawPoint(10,g,cam);
                else map.wall2.get(map.selectionPoint -1).p2.drawPoint(10,g,cam);
            }
            if(map.wallsIndexSelection == 2){
                if(map.selectionPoint == 0)
                    map.startStop.p1.drawPoint(10,g,cam);
                else
                    map.startStop.p2.drawPoint(10,g,cam);
            }
        }else{
            if(leftPressed){
                map.changePoint(mouseDelta);
            }
        }

    }
    void setCars(){
        for( int i = 0; i < countOfCar; i++){
            cars[i] =  new Car(new Vector2(map.startStop.p1.addR(botSpawingPosCorrector)),  0 ,wightCar, heightCar, (countOfInputBot -1), radiusVIEW, CarFov);
            cars[i].speedLimiter = 27;
        }
    }

    public MapEditing() {
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



        setCars();


        Timer timer = new Timer(16, e -> repaint()); // ~60 FPS = 16
//        Timer timer = new Timer(0, e -> repaint()); // ~60 FPS = 16
//        Timer timer = new Timer(0, e -> repaint()); // ~60 FPS = 16

        timer.start();

        addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                if (!isMapEditinEctive & (e.getButton() == MouseEvent.BUTTON1)){
                    map.addWall(activeWall, new Vector2(e.getPoint().getLocation().x,e.getPoint().getLocation().y), cam);
                }
//                if (isMapEditinEctive & (e.getButton() == MouseEvent.BUTTON1)){
//                    if(!ISMOVINGPOINT)
//                    map.selectPoint( (new Vector2(e.getPoint().getLocation().x,e.getPoint().getLocation().y)).addR(cam.position));
//
//
//                }
                repaint();
            }
            @Override
            public void mousePressed(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    leftPressed = true;
                    map.selectPoint( (new Vector2(e.getPoint().getLocation().x,e.getPoint().getLocation().y)).addR(cam.position));


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

                    }
                    for(int gNeurons = 0; gNeurons < countOfALLNeurons; gNeurons++){

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
                if (e.getKeyCode() == KeyEvent.VK_M) {
                    if( ISMOVINGPOINT == true){
                        ISMOVINGPOINT = false;
                        System.out.println("movingPoint unActive");
                    }
                    else ISMOVINGPOINT =true;
                }
//        if (e.getKeyCode() == KeyEvent.VK_V) {
//            CAMERAMODE = 0;
//        }
//                if (e.getKeyCode() == KeyEvent.VK_M) {
//                    if(ISRENDERMAP) ISRENDERMAP = false;
//                    else  ISRENDERMAP = true;
//                }
                if (e.getKeyCode() == KeyEvent.VK_N) {

                }
                if (e.getKeyCode() == KeyEvent.VK_B) {

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
        frame.add(new MapEditing());
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


