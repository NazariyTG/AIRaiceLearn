import java.awt.*;
import java.util.ArrayList;
import java.io.*;
import java.util.ArrayList;


public class Map {


    ArrayList<Line2d> wall1 = new ArrayList<>();
    ArrayList<Line2d> wall2 = new ArrayList<>();

    ArrayList<Car> staticBotPositions = new ArrayList<>();
    boolean isUsingStaticBot = false;


    Line2d startStop = new Line2d();

    Vector2 activePointWall1 = null;
    Vector2 activePointWall2 = null;
    boolean activePointWall3 = true;
    Vector2 activePointWall4 = null;


    int selectionPoint = 0;
    int wallsIndexSelection = 0;

    Map(){

    }
    Map(Map otherMap){
        this.wall1 = (ArrayList<Line2d>) otherMap.wall1.clone();
        this.wall2 = (ArrayList<Line2d>) otherMap.wall2.clone();
        this.staticBotPositions = (ArrayList<Car>) otherMap.staticBotPositions.clone();
        this.isUsingStaticBot = otherMap.isUsingStaticBot;
        this.startStop = new Line2d(otherMap.startStop);


    }


    void addWall(int activeWall, Vector2 p, Camera cam){
        if(activeWall == 1){
            if(activePointWall1 == null){
                activePointWall1 = p.addR(cam.position);
            }else {
                wall1.add(new Line2d(activePointWall1 ,p.addR(cam.position)));
                activePointWall1 = p.addR(cam.position);
            }

        } else if (activeWall == 2) {
            if(activePointWall2 == null){
                activePointWall2 = p.addR(cam.position);
            }else {
                wall2.add(new Line2d(activePointWall2 ,p.addR(cam.position)));
                activePointWall2 = p.addR(cam.position);
            }
        }
        else if (activeWall == 3) {
            if(activePointWall3){
                startStop.p1 = p.addR(cam.position);
                activePointWall3 = false;
            }else {
                startStop.p2 =p.addR(cam.position);
                activePointWall3 = true;
            }
        }
        else if (activeWall == 4) {

            staticBotPositions.add(new Car(p.addR(cam.position), 0, Config.wightCar, Config.heightCar, (Config.countOfInput - 1) / 2, Config.radiusVIEW, Config.carFov));


        }

    }
    void changePoint(Vector2 mouseP){

        if(wallsIndexSelection == 0){
            if(selectionPoint ==0)
                wall1.get(0).p1.add( mouseP);
            else if (selectionPoint > wall1.size()-1) {
                wall1.get(selectionPoint-1).p2.add( mouseP);
            } else {
                wall1.get(selectionPoint).p1.add( mouseP);
                wall1.get(selectionPoint-1).p2.add( mouseP);

            }
        }
        if(wallsIndexSelection ==1){
            if(selectionPoint ==0)
                wall2.get(0).p1.add( mouseP);
            else if (selectionPoint > wall2.size()-1) {
                wall2.get(selectionPoint-1).p2.add( mouseP);
            } else {
                wall2.get(selectionPoint).p1.add( mouseP);
                wall2.get(selectionPoint-1).p2.add( mouseP);

            }
        }
        if(wallsIndexSelection ==2){
            if(selectionPoint ==0)
                startStop.p1.add( mouseP);
            else
                startStop.p2.add( mouseP);

        }
    }

    void selectPoint(Vector2 mousePosition){
        double minDistaice = Float.MAX_VALUE;
        double curentDist;



        for( int i = 0; i < wall1.size() +1; i++){
            if( i < wall1.size())
            curentDist = mousePosition.subR(wall1.get(i).p1).length();
            else curentDist = mousePosition.subR(wall1.get(i-1).p2).length();

            if( curentDist < minDistaice){
                minDistaice = curentDist;
                selectionPoint = i;
                wallsIndexSelection = 0;

            }
        }
        for( int i = 0; i < wall2.size() +1; i++){
            if( i < wall2.size())
                curentDist = mousePosition.subR(wall2.get(i).p1).length();
            else curentDist = mousePosition.subR(wall2.get(i-1).p2).length();

            if( curentDist < minDistaice){
                minDistaice = curentDist;
                selectionPoint = i;
                wallsIndexSelection = 1;

            }
        }

        for(int i =0; i < 2; i++) {
            if(i ==0)
            curentDist = mousePosition.subR(startStop.p1).length();
            else
            curentDist = mousePosition.subR(startStop.p2).length();

            if (curentDist < minDistaice) {
                minDistaice = curentDist;
                selectionPoint = i;
                wallsIndexSelection = 2;

            }
        }




    }


    void safeToFile(String fileName){
        try (PrintWriter pw = new PrintWriter(new FileWriter(fileName))){

            pw.println("wall1");
            for(Line2d w1: wall1){
                pw.println(w1.p1.x + " " + w1.p1.y +" "+  w1.p2.x + " " + w1.p2.y );
            }
            pw.println("wall2");
            for(Line2d w2: wall2){
                pw.println(w2.p1.x + " " + w2.p1.y +" "+  w2.p2.x + " " + w2.p2.y );
            }
            pw.println("startStop");

            pw.println(startStop.p1.x + " " + startStop.p1.y +" "+  startStop.p2.x + " " + startStop.p2.y );

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void safeToFileV2(String fileName){
        try (PrintWriter pw = new PrintWriter(new FileWriter(fileName))){

            pw.println("Version2");
            pw.println("wall1");
            for(Line2d w1: wall1){
                pw.println(w1.p1.x + " " + w1.p1.y +" "+  w1.p2.x + " " + w1.p2.y );
            }
            pw.println("wall2");
            for(Line2d w2: wall2){
                pw.println(w2.p1.x + " " + w2.p1.y +" "+  w2.p2.x + " " + w2.p2.y );
            }
            pw.println("startStop");

            pw.println(startStop.p1.x + " " + startStop.p1.y +" "+  startStop.p2.x + " " + startStop.p2.y );
            if(staticBotPositions.size() == 0){
                pw.println("end");
            }else pw.println("staticBotPosition");
            for(Car carStatic: staticBotPositions){
                pw.println(carStatic.position.x + " " + carStatic.position.y +" "+  carStatic.rotation);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void readFromFileCheckVersion(String fileName){
        try (BufferedReader bf = new BufferedReader(new FileReader(fileName))){
            String line;
            ArrayList<Line2d> currnetWall = null;
            line = bf.readLine();
            if(line.equals("wall1")){
                readFromFile(fileName);

            }
            if(line.equals("Version2")){
                readFromFileV2(fileName);

            }



        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void readFromFile(String fileName){
        wall1.clear();
        wall2.clear();


        try (BufferedReader bf = new BufferedReader(new FileReader(fileName))){
            String line;
            ArrayList<Line2d> currnetWall = null;
            while ((line = bf.readLine()) != null) {
                if(line.equals("wall1")){
                    currnetWall = wall1;

                    continue;
                }
                if(line.equals("wall2")){
                    currnetWall = wall2;
                    continue;
                }

                if(line.equals("startStop")){
                    line = bf.readLine();
                    String parts[] = line.split(" ");
                    double x1 = Double.parseDouble(parts[0]);
                    double y1 = Double.parseDouble(parts[1]);
                    double x2 = Double.parseDouble(parts[2]);
                    double y2 = Double.parseDouble(parts[3]);

                    startStop = new Line2d(new Vector2(x1, y1), new Vector2(x2, y2));
                    continue;
                }

                String parts[] = line.split(" ");
                double x1 = Double.parseDouble(parts[0]);
                double y1 = Double.parseDouble(parts[1]);
                double x2 = Double.parseDouble(parts[2]);
                double y2 = Double.parseDouble(parts[3]);

                currnetWall.add(new Line2d(new Vector2(x1, y1), new Vector2(x2, y2)));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    void readFromFileV2(String fileName){
        wall1.clear();
        wall2.clear();

        int indexDutingreading = 0;

        try (BufferedReader bf = new BufferedReader(new FileReader(fileName))){
            String line;
            ArrayList<Line2d> currnetWall = null;
            while ((line = bf.readLine()) != null) {
                if(line.equals("wall1")){
                    currnetWall = wall1;
                    indexDutingreading = 1;
                    continue;
                }
                if(line.equals("wall2")){
                    currnetWall = wall2;
                    indexDutingreading = 2;
                    continue;
                }

                if(line.equals("startStop")){
                    line = bf.readLine();
                    indexDutingreading = 0;
                    String parts[] = line.split(" ");
                    double x1 = Double.parseDouble(parts[0]);
                    double y1 = Double.parseDouble(parts[1]);
                    double x2 = Double.parseDouble(parts[2]);
                    double y2 = Double.parseDouble(parts[3]);

                    startStop = new Line2d(new Vector2(x1, y1), new Vector2(x2, y2));
                    continue;
                }

                if(line.equals("staticBotPosition")){
                    indexDutingreading = 4;
                    continue;
                }
                if (line.equals("end")){
                    break;
                }

                if(indexDutingreading == 1 || indexDutingreading == 2) {
                    String parts[] = line.split(" ");
                    double x1 = Double.parseDouble(parts[0]);
                    double y1 = Double.parseDouble(parts[1]);
                    double x2 = Double.parseDouble(parts[2]);
                    double y2 = Double.parseDouble(parts[3]);

                    currnetWall.add(new Line2d(new Vector2(x1, y1), new Vector2(x2, y2)));
                }

                if(indexDutingreading == 4) {
                    String parts[] = line.split(" ");
                    double x1 = Double.parseDouble(parts[0]);
                    double y1 = Double.parseDouble(parts[1]);
                    double rot = Double.parseDouble(parts[2]);

                    staticBotPositions.add(new Car(new Vector2(x1,y1), rot, Config.wightCar, Config.heightCar, (Config.countOfInput - 1) / 2, Config.radiusVIEW, Config.carFov));
                }

            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    void render(Graphics g, Camera cam){
        int counter  =0;
        for(Line2d l: wall1){
//            l.p1.sub(cam.position);
//            l.p2.sub(cam.position);
//            if(wallsIndexSelection == 0 || selectionPoint == counter){
//                g.setColor(Color.RED);
//                g.drawLine((int) l.p1.subR(cam.position).x, (int)l.p1.subR(cam.position).y,(int) l.p2.subR(cam.position).x,(int) l.p2.subR(cam.position).y);
//                g.setColor(Color.BLUE);
//            }
//            else
                g.drawLine((int) l.p1.subR(cam.position).x, (int) l.p1.subR(cam.position).y, (int) l.p2.subR(cam.position).x, (int) l.p2.subR(cam.position).y);

            counter ++;
        }
        counter  =0;
        for(Line2d l: wall2){
//            l.p1.sub(cam.position);
//            l.p2.sub(cam.position);\
//            if(wallsIndexSelection == 1 || selectionPoint == counter){
//                g.setColor(Color.RED);
//                g.drawLine((int) l.p1.subR(cam.position).x, (int)l.p1.subR(cam.position).y,(int) l.p2.subR(cam.position).x,(int) l.p2.subR(cam.position).y);
//                g.setColor(Color.BLUE);
//            }

            g.drawLine((int) l.p1.subR(cam.position).x, (int) l.p1.subR(cam.position).y, (int) l.p2.subR(cam.position).x, (int) l.p2.subR(cam.position).y);

            for(Car car: staticBotPositions){
                car.render(g, cam);
            }
//            counter ++;

        }
    }
}
