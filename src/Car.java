import java.awt.*;
import java.lang.management.GarbageCollectorMXBean;
import java.util.ArrayList;

public class Car extends Square{
    double speed = 5;
    double maxSpeed = 30;
    double acceleration = 0.2;
    double rotationSpeed= 2;

    Line2d rays[];

    double radiusView;
    double fov;
    int countOfRays;

    Car(Vector2 position, double rotaion,double wigth, double heingt, int countOfRays, double radiusView, double fov){
        super (position, rotaion, wigth,heingt);
        rays = new Line2d[countOfRays];
        this.countOfRays = countOfRays;
        this.radiusView = radiusView;
        this.fov = fov;

        Line2d ray = new Line2d(position,new  Vector2( position.x + radiusView,position.y));
        ray.rotate(-fov/2);
        for(int i = 0; i < rays.length; i++){
            rays[i] = ray.rotateR(fov/countOfRays * i);


        }
    }

    void move(double m){ // -1 <= m <= 1
        if( m >= 1) m =1 ;
        if( m <= -1) m = -1;

//        Vector2 deltaPosition = new Vector2(this.position);
//        deltaPosition.x += (speed * m) * Math.cos(Math.toRadians(this.rotation));
//        deltaPosition.y += (speed * m) * Math.sin(Math.toRadians(this.rotation));
//
//        this.position = deltaPosition;
        this.position.x += (speed * m) * Math.cos(Math.toRadians(this.rotation));
        this.position.y += (speed * m) * Math.sin(Math.toRadians(this.rotation));

        for(Line2d l: rays){
            l.position = this.position;
            Line2d ray = new Line2d(position,new  Vector2( position.x + radiusView,position.y));
            ray.rotate(-fov/2);
            for(int i = 0; i < rays.length; i++){
                rays[i] = ray.rotateR(fov/countOfRays * i);

            }
        }




    }
    void moveAcceleraion(double m){ // -1 <= m <= 1
        if( m >= 1) m =1 ;
        if( m <= -1) m = -1;

        speed += acceleration * m;

        if(speed > maxSpeed){
            speed = maxSpeed;
        }
        if(speed < -maxSpeed){
            speed = -maxSpeed;
        }



        this.position.x += (speed ) * Math.cos(Math.toRadians(this.rotation));
        this.position.y += (speed) * Math.sin(Math.toRadians(this.rotation));
        for(Line2d l: rays){
            l.position = this.position;
            Line2d ray = new Line2d(position,new  Vector2( position.x + radiusView,position.y));
            ray.rotate(-fov/2);
            for(int i = 0; i < rays.length; i++){
                rays[i] = ray.rotateR(fov/countOfRays * i);

            }
        }

    }


    void rot(double r){ // -1 <= m <= 1
        if( r >= 1) r =1 ;
        if( r <= -1) r = -1;

        double deltaRotation = rotationSpeed *r;
        this.rotation += deltaRotation;

        for (Line2d l: rays){
            l.rotate(this.rotation + deltaRotation);
        }

    }

    void render(Graphics g , Camera cam){
        super.render(g, cam);
//        for (Line2d l: rays){
//            l.render(g, cam);
//        }
    }

    boolean isColisionWith(ArrayList<Line2d> mapwalls){


        for(Line2d carline: this.lines){
            for(Line2d mapline: mapwalls){
                Vector2 colP = carline.colision(mapline);
                if(colP != null){
                    return true;
                }
            }
        }
        return false;

    }

    double[] distancesToWall(ArrayList<Line2d> mapWalls){
        double[] result = new double[countOfRays];

        for(int i = 0 ; i < countOfRays; i++){
            double mindis = 1;

//            for(int j = 0; j < mapWalls.size(); j++){
            for(Line2d mapLine: mapWalls){
                Vector2 colP= rays[i].colision(mapLine);
                double rayDis = 1; //// 0 -1

                if(colP != null) {
                    rayDis = colP.subR(this.position).length() / radiusView; ////  0 - 1
                    if (rayDis < mindis) {
                        mindis = rayDis;


                    }

                }

            }
            result[i] = mindis;
        }
        return result;

    }
    double[] distancesToWallRenderRay(ArrayList<Line2d> mapWalls, Graphics g, Camera cam){
        double[] result = new double[countOfRays];

        for(int i = 0 ; i < countOfRays; i++){
            double mindis = 1;
            Vector2 minPoint = null;

//            for(int j = 0; j < mapWalls.size(); j++){
            for(Line2d mapLine: mapWalls){
                Vector2 colP= rays[i].colision(mapLine);
                double rayDis = 1;


                if(colP != null) {
                    rayDis = colP.subR(this.position).length() / radiusView; ////  0 - 1
                    if (rayDis < mindis) {
                        mindis = rayDis;
                        minPoint = colP;

                    }

                }
            }
            result[i] = mindis;
            Line2d renderRay ;
            g.setColor(Color.RED);
            if(minPoint != null) {
                renderRay = new Line2d(this.position, minPoint);
                g.setColor(Color.RED);
                minPoint.drawPoint(6, g, cam);
                g.setColor(Color.BLUE);
            }else {
                renderRay = new Line2d(this.position, this.rays[i].p2);
                g.setColor(Color.RED);
                this.rays[i].p2.drawPoint(6, g, cam);
                g.setColor(Color.BLUE);
            }
            g.setColor(Color.BLUE);
            renderRay.render(g,cam);

        }
        return result;

    }
}
