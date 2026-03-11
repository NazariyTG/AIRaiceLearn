import java.awt.*;

public class Vector2 {
    double x,y;

    Vector2(){
        this.x = 0;
        this.y = 0;
    }

    Vector2(double x, double y){
        this.x = x;
        this.y = y;
    }

    Vector2(Vector2 vec){
        this.x = vec.x;
        this.y = vec.y;
    }

    void add(Vector2 vec){
        this.x += vec.x;
        this.y += vec.y;
    }
    
    Vector2 addR(Vector2 vec){
        Vector2 res = new Vector2();
        res.x = this.x + vec.x;
        res.y = this.y + vec.y;
        return res;
    }

    void sub(Vector2 vec){
        this.x -= vec.x;
        this.y -= vec.y;
    }

    Vector2 subR(Vector2 vec){
        Vector2 res = new Vector2();
        res.x = this.x - vec.x;
        res.y = this.y - vec.y;
        return res;
    }

    void multiply(Vector2 vec){
        this.x *= vec.x;
        this.y *= vec.y;
    }
    Vector2 divadeR(Vector2 vec){
        Vector2 res = new Vector2();
        res.x = this.x / vec.x;
        res.y = this.y / vec.y;
        return res;
    }

    double length(){
        return Math.sqrt((this.x * this.x) + (this.y * this.y));
    }


    void normalize(){
        double length = length();

        this.x = x / length;
        this.y = y / length;
    }

//    boolean getAngel(){
//
//    }


    void drawPoint(int radius , Graphics g , Camera cam){
        g.setColor(Color.RED);
        g.fillRect((int) (x- cam.position.x- radius/2), (int) (y- cam.position.y  - radius/2),  radius,radius);
        g.setColor(Color.BLUE);
    }


}
