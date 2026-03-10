import java.awt.*;
import java.util.ArrayList;

public class Traektoria {
    int countOFpoint;


    Car[] cars;


    Traektoria(Car[] cars , double[] speeds){
        this.cars = new Car[cars.length];
        for(int i = 0; i < cars.length; i++){
            this.cars[i] = cars[i];
        }

    }
    Traektoria(Traektoria other){
        this.cars = new Car[other.cars.length];
        for(int i = 0; i < other.cars.length; i++){
            this.cars[i] = other.cars[i];
        }


    }


    void render(Graphics g, Camera cam){
        for(Car c : this.cars){
            g.setColor(new Color((int) (255-(Math.abs( c.speed) * 255/c.maxSpeed)),(int) (Math.abs( c.speed) * 255/c.maxSpeed),0,200));
//            System.out.println(c.speed);
            //c.render(g,cam);
            c.lines[0].render(g, cam);
            c.lines[2].render(g, cam);
            g.setColor(Color.BLUE);

        }
    }
}
