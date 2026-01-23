import java.awt.*;

public class Line2d extends  Object2d{
    Vector2 p1;
    Vector2 p2;

    Line2d(){
        super();
        this.p1 = new Vector2();
        this.p2 = new Vector2();
    }
    Line2d(Line2d other){
        super(other.position, other.rotation, other.scale);
        this.p1 = other.p1;
        this.p2 = other.p2;
    }
    Line2d(Vector2 p1, Vector2 p2){
        this.position = p1;
        this.p1 = p1;
        this.p2 = p2;
    }

    Line2d(Vector2 p1, Vector2 p2, Vector2 position){
        this.position = position;
        this.p1 = p1;
        this.p2 = p2;
    }


    Vector2 colisionRays(Line2d other){
        Vector2 subtraction1 = this.p1.subR(this.p2);
        Vector2 subtraction2 = other.p1.subR(other.p2);
        double k1 = 0;
        double a1 = 0;

        double k2 = 0;
        double a2 = 0;
        if(subtraction1.x == 0){
            if(subtraction2.x == 0){
                return null;
            }else {
                k2= subtraction2.y / subtraction2.x;
                a2 = other.p1.y - (k2 * other.p1.x);

                Vector2 result = new Vector2();

                result.x = this.p1.x;
                result.y = (k2 * result.x) + a2;
                return  result;
            }

        }
        else {
            k1= subtraction1.y / subtraction1.x;
            a1 = this.p1.y - (k1 * this.p1.x);


        }



        if(subtraction2.x == 0) {
            if (subtraction1.x == 0) {
                return null;
            }else {
                Vector2 result = new Vector2();

                result.x = other.p1.x;
                result.y = (k1 * result.x) + a1;
                return  result;
            }


        } else {
            k2= subtraction2.y / subtraction2.x;
            a2 = other.p1.y - (k2 * other.p1.x);
        }

        if (k1 == k2){
            return null;
        }
        Vector2 result = new Vector2();

        result.x = (a2-a1)/(k1-k2);
        result.y = (k1 * result.x) + a1;

        if(k1 == 0){
            result.y = (k2 * result.x) + a2;
        }


        return result;
    }

    boolean isColision(Line2d other){
        Vector2 pointOfColision = colisionRays(other);
        if(pointOfColision == null){
            return false;
        }



        if ((pointOfColision.x >= this.p1.x & pointOfColision.x <= this.p2.x)  ||  (pointOfColision.x >= this.p2.x & pointOfColision.x <= this.p1.x)) {
            if ((pointOfColision.y >= this.p1.y & pointOfColision.y <= this.p2.y) || (pointOfColision.y >= this.p2.y & pointOfColision.y <= this.p1.y)) {
                if ((pointOfColision.x >= other.p1.x & pointOfColision.x <= other.p2.x)  ||  (pointOfColision.x >= other.p2.x & pointOfColision.x <= other.p1.x)) {
                    if ((pointOfColision.y >= other.p1.y & pointOfColision.y <= other.p2.y) || (pointOfColision.y >= other.p2.y & pointOfColision.y <= other.p1.y)) {
                        return true;

                    }
                }

            }
        }
        return false;
    }
    Vector2 colision(Line2d other){

        Vector2 pointOfColision = colisionRays(other);
        if(pointOfColision == null){
            return null;
        }

        if ((pointOfColision.x >= this.p1.x & pointOfColision.x <= this.p2.x)  ||  (pointOfColision.x >= this.p2.x & pointOfColision.x <= this.p1.x)) {
            if ((pointOfColision.y >= this.p1.y & pointOfColision.y <= this.p2.y) || (pointOfColision.y >= this.p2.y & pointOfColision.y <= this.p1.y)) {
                if ((pointOfColision.x >= other.p1.x & pointOfColision.x <= other.p2.x)  ||  (pointOfColision.x >= other.p2.x & pointOfColision.x <= other.p1.x)) {
                    if ((pointOfColision.y >= other.p1.y & pointOfColision.y <= other.p2.y) || (pointOfColision.y >= other.p2.y & pointOfColision.y <= other.p1.y)) {
                        return pointOfColision;

                    }
                }

            }
        }
        return null;
    }

    void move(Vector2 vec){
        this.p1.add(vec);
        this.p2.add(vec);
    }


    void rotate(double andle){
        double a = Math.toRadians(andle);
        Line2d result = new Line2d();
        result.position = this.position;

        result.p1.x = (p1.subR(position).x) * Math.cos(a) - p1.subR(position).y * Math.sin(a) ;
        result.p1.y = (p1.subR(position).x) * Math.sin(a) + p1.subR(position).y * Math.cos(a);

        result.p2.x = (p2.subR(position).x) * Math.cos(a) - p2.subR(position).y * Math.sin(a);
        result.p2.y = (p2.subR(position).x) * Math.sin(a) + p2.subR(position).y * Math.cos(a);
        this.p1 = result.p1.addR(position);
        this.p2 = result.p2.addR(position);
    }

    Line2d rotateR(double andle){
        double a = Math.toRadians(andle);
        Line2d result = new Line2d();
        result.position = this.position;

        result.p1.x = (p1.subR(position).x) * Math.cos(a) - p1.subR(position).y * Math.sin(a) ;
        result.p1.y = (p1.subR(position).x) * Math.sin(a) + p1.subR(position).y * Math.cos(a);

        result.p2.x = (p2.subR(position).x) * Math.cos(a) - p2.subR(position).y * Math.sin(a);
        result.p2.y = (p2.subR(position).x) * Math.sin(a) + p2.subR(position).y * Math.cos(a);
        result.p1.add(position);
        result.p2.add(position);

        return result;
    }


    double getLength(){
        return p1.subR(p2).length();
    }

    double getLength(Line2d ohter){
        return ohter.p1.subR(p2).length();
    }

    void render(Graphics g, Camera cam){

        g.drawLine((int) p1.subR(cam.position).x, (int)p1.subR(cam.position).y,(int) p2.subR(cam.position).x,(int) p2.subR(cam.position).y);
    }
}
