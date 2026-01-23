import java.awt.*;
import java.util.ArrayList;
import java.io.*;
import java.util.ArrayList;


public class Map {


    ArrayList<Line2d> wall1 = new ArrayList<>();
    ArrayList<Line2d> wall2 = new ArrayList<>();

    Line2d startStop = new Line2d();

    Vector2 activePointWall1 = null;
    Vector2 activePointWall2 = null;
    boolean activePointWall3 = true;


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
    void render(Graphics g, Camera cam){
        for(Line2d l: wall1){
//            l.p1.sub(cam.position);
//            l.p2.sub(cam.position);
            g.drawLine((int) l.p1.subR(cam.position).x, (int)l.p1.subR(cam.position).y,(int) l.p2.subR(cam.position).x,(int) l.p2.subR(cam.position).y);
        }
        for(Line2d l: wall2){
//            l.p1.sub(cam.position);
//            l.p2.sub(cam.position);
            g.drawLine((int) l.p1.subR(cam.position).x, (int)l.p1.subR(cam.position).y,(int) l.p2.subR(cam.position).x,(int) l.p2.subR(cam.position).y);
        }
    }
}
