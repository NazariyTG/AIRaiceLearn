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



    Car cars[] = new Car[COUNTOFCAR];
    NeuroModel neuroModels[] = new NeuroModel[COUNTOFCAR];

    boolean isCarAlive[] = new boolean[COUNTOFCAR];
    Vector2 carsResults[] = new Vector2[COUNTOFCAR];

    int currentWeight= 0;
    int currentNeuron= 0;

    int countOfAllWeght;
    int countOfALLNeurons;
    int[] currentWeightInProgress = new int[COUNTOFCAR];

    int currentGeneration = 0;

    double deltaLearn = 1;
// Vector2[] carsResults




    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        for(Car c: cars){
            c.g = g;
        }

        Point mouseP = MouseInfo.getPointerInfo().getLocation();
        SwingUtilities.convertPointFromScreen(mouseP, this);
        Vector2 mouse = new Vector2(mouseP.x, mouseP.y);
        Vector2 mouseDelta = mouse.subR(oldmouse);
        if (rightPressed) cam.position.sub(mouseDelta);
        oldmouse = mouse;

        g.setColor(Color.BLUE);


        double distances[][] = new double[COUNTOFCAR][cars[0].countOfRays];
        for(int carIndex = 0; carIndex < cars.length; carIndex++) {
            Car car1 = cars[carIndex];
            for (Line2d l1 : car1.lines) {
                for (Line2d l2 : mapWalls) {
                    Vector2 colP = l1.colision(l2);
                    if (colP != null) {
                        g.setColor(Color.RED);
//                    if(ISRENDERCAR) l1.render(g,cam);
                        g.setColor(Color.BLUE);
//                        car1.position = new Vector2(-200, 0);
//                        car1.rotation = 0;
                        isCarAlive[carIndex] = false;
                        carsResults[carIndex] = new Vector2(car1.position.subR(map.startStop.p2).length(), time);

                    }
                }
            }


            for (int i = 0; i < car1.rays.length; i++) {
                distances[carIndex][i] = 1;
                for (Line2d l2 : mapWalls) {
                    Vector2 colP = car1.rays[i].colision(l2);
                    if (colP != null) {
                        g.setColor(Color.RED);
                        if (ISRENDERRAYS) car1.rays[i].render(g, cam);
                        g.setColor(Color.BLUE);

                        double currnetDistance = car1.position.subR(colP).length();
                        if (distances[carIndex][i] > currnetDistance / car1.radiusView) {
                            distances[carIndex][i] = currnetDistance / car1.radiusView;
                            if (ISRENDERRAYS)
                                g.fillRect((int) colP.subR(cam.position).x, (int) colP.subR(cam.position).y, 10, 10);
                        }

                    }
                }
            }

            if(isCarAlive[carIndex]) {
                double[] vecMove =  {0,0};

                car1.move(vecMove[0]);
                car1.rot(vecMove[1]);
                car1.curc();
            }
        }
        //Colision
        if(CAMERAMODE == 1)
        cam.position = cars[0].position.subR(new Vector2(750, 500));

        if(ISRENDERCAR) {

            for (Car car : cars) {
                car.render(g, cam);
            }
        }


        if(ISRENDERFIRSVIE) {
            for (int i = 0; i < distances.length; i++) {
                g.fillRect(i * WINDOWWITH / cars[0].countOfRays, WINDOWHEINGH / 3, WINDOWWITH / cars[0].countOfRays, (int) -(distances[0][i] - 500));
                g.fillRect(i * WINDOWWITH / cars[0].countOfRays, WINDOWHEINGH / 3, WINDOWWITH / cars[0].countOfRays, (int) (distances[0][i] - 500));
            }
        }
        if(ISRENDERMAP)
        map.render(g, cam);
        if(isMapEditinEctive) map.startStop.render(g, cam);




    }

    public Main() {
        map.readFromFile("map.txt");
        mapWalls.addAll(map.wall1);
        mapWalls.addAll(map.wall2);





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