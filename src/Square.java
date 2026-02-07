import java.awt.*;

public class Square extends Object2d{
    double wigth;
    double heingt;

    Graphics g;

    Vector2[] points = new Vector2[4];
    Line2d[] lines = new Line2d[4];

    Square(Graphics g2d){
        super();
        this.wigth = 0;
        this.heingt = 0;
        curc();
        for (int i = 0; i < points.length; i++){
            points[i] = new Vector2();
            lines[i] = new Line2d();
        }

    }
//    Square(Vector2 position, Graphics g2d){
//        super(position);
//
//    }
    Square(Vector2 position, double rotaion,double wigth, double heingt){
        super(position, rotaion, 1);
        this.wigth = wigth;
        this.heingt = heingt;
        for (int i = 0; i < points.length; i++){
            points[i] = new Vector2();
            lines[i] = new Line2d();
        }
        curc();

    }
//    void curc(){
//        Vector2[] p = new Vector2[4]; // p = point before rotation
//        p[0] = position.addR( new Vector2(wigth /2 , heingt/2) );
//        p[1] = position.addR( new Vector2(wigth /-2 , heingt/2) );
//        p[2] = position.addR( new Vector2(wigth /-2 , heingt/-2) );
//        p[3] = position.addR( new Vector2(wigth /2 , heingt/-2) );
//
//        for (int i = 0; i < p.length; i++){
//            //points[i] = new Vector2();
//            double r  = Math.toRadians(rotation);
//            points[i].x = (p[i].x - position.x) * Math.cos(r) - (p[i].y - position.y) * Math.sin(r);
//            points[i].y =  (p[i].x - position.x) * Math.sin(r) + (p[i].y - position.y) * Math.cos(r);
//            points[i].add(position);
//        }
//
//        //points = p;
//
//    }
    void curc(){
        Vector2[] p = new Vector2[4]; // p = point before rotation
        p[0] = position.addR( new Vector2(wigth /2 , heingt/2) );
        p[1] = position.addR( new Vector2(wigth /-2 , heingt/2) );
        p[2] = position.addR( new Vector2(wigth /-2 , heingt/-2) );
        p[3] = position.addR( new Vector2(wigth /2 , heingt/-2) );

        for (int i= 0; i < points.length ; i++){
            lines[i].position = position;
            lines[i].p1 = p[i];
            if (i+1 < points.length) lines[i].p2 = p[i+1];
            else lines[i].p2 = p[0];
        }

        for(Line2d l: lines){
            l.rotate(rotation);
        }

    }



    void render(Graphics g , Camera cam){
//        for (int i = 0; i < points.length; i ++){
//            if (i +1 < points.length) g2d.drawLine((int)points[i].subR(cam.position).x ,(int)points[i].subR(cam.position).y,(int) points[i+1].subR(cam.position).x ,(int)points[i+1].subR(cam.position).y);
//            else g2d.drawLine((int)points[i].subR(cam.position).x,(int)points[i].subR(cam.position).y,(int) points[0].subR(cam.position).x ,(int)points[0].subR(cam.position).y);
//        }
        for(Line2d l: lines){
            l.render(g, cam);
        }

    }


}
