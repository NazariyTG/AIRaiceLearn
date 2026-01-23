import java.awt.*;
import java.lang.management.GarbageCollectorMXBean;

public class Car extends Square{
    double speed = 5;
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
}
