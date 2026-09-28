import java.awt.*;
import java.lang.management.GarbageCollectorMXBean;
import java.util.ArrayList;
import java.util.Vector;

public class Car extends Square{
    double speed = 0;
    double maxSpeed = 30;//30
    double acceleration = 0.4;//0.4
    double rotationSpeed= 2;//2
    int speedLimiter = 30;

//    double speedLimeter = 20;
    Line2d rays[];

    double radiusView;
    double fov;
    int countOfRays;


    double[] inputsLokal;

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
    }Car(Car other){
        super (other.position, other.rotation, other.wigth,other.heingt);
        rays = new Line2d[other.countOfRays];
        this.countOfRays = other.countOfRays;
        this.radiusView = other.radiusView;
        this.fov = other.fov;

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

        if(speed > speedLimiter){
            speed = speedLimiter;
        }
        if(speed < -speedLimiter){
            speed = -speedLimiter;
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

        double deltaRotation = rotationSpeed * r;
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

    double[] distancesToOtherCars(Car[] cars){
        double[] result = new double[countOfRays];

        for(int i = 0 ; i < countOfRays; i++){
            double mindis = 1;

//            for(int j = 0; j < mapWalls.size(); j++){
            for(Car car: cars){
                for(Line2d line: car.lines){


                    Vector2 colP= rays[i].colision(line);
                    double rayDis = 1; //// 0 -1

                    if(colP != null) {
                        rayDis = colP.subR(this.position).length() / radiusView; ////  0 - 1
                        if (rayDis < mindis) {
                            mindis = rayDis;


                        }

                    }
                }

            }
            result[i] = mindis;
        }
        return result;

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

    boolean isColisionWithCars(Car[] cars){
        for(int i = 0; i < cars.length; i++){
            for (Line2d oherLine: cars[i].lines){
                for(Line2d thisLine: this.lines){
                    if(thisLine.isColision(oherLine)){
                        return true;
                    }
                }
            }
        }
        return false;
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

    double curkCarResult(Map map, NeuroModel n,int distanceTowin, int maxTime , double timeValue, double distanceValue, double colisionValue){

//        this.position = new Vector2(map.startStop.p1);
//        this.rotation = 0;
//
//        this.speed = 0;
//
//        this.curc();

        ArrayList<Line2d> allMapWalls = new ArrayList<>();
        allMapWalls.addAll(map.wall1);
        allMapWalls.addAll(map.wall2);

        boolean isCarActive = true;

        int timer = 0;

        while (true){

            double ltowin = this.position.subR(map.startStop.p2).length();
            if(ltowin < distanceTowin){
//                System.out.println("winner");

//                System.out.println(this.position.x + " " + this.position.y);
                return timer * timeValue;


            }

            if(this.isColisionWith(allMapWalls)){
//                System.out.println(this.position.x + " " + this.position.y + "colision " );
                return maxTime * timeValue + this.position.subR(map.startStop.p2).length() * distanceValue + colisionValue;
            }
            timer++;
            if(timer > maxTime){
//                System.out.println(this.position.x + " " + this.position.y + "max TIme " + timer) ;
                return maxTime * timeValue + this.position.subR(map.startStop.p2).length() * distanceValue + colisionValue;

            }

            double[] inputs = new double[this.countOfRays + 1];
            double[] distances = this.distancesToWall(allMapWalls);
            for(int i = 0; i < distances.length; i++){
                inputs[i] = distances[i];
            }
            inputs [distances.length] = this.speed/ this.maxSpeed;
            double[] resul = n.curk(inputs);

            if( resul[0] > resul[1]){
                this.moveAcceleraion(1);
            }else{
                this.moveAcceleraion(-1);
            }


            if( resul[2] > resul[3] && resul[2] > resul[4]){
                this.rot(1);
            }
            if( resul[3] > resul[2] && resul[3] > resul[4]){
                this.rot(-1);
            }if(resul[4] > resul[2] && resul[4] > resul[3]){
                this.rot(0);
            }

            this.curc();




        }

    }
    double curkCarResultWithOtherCar(Map map, NeuroModel n , Car[] otherCars, NeuroModel[] otherneurons,int distanceTowin, int maxTime , double timeValue, double distanceValue, double colisionValue){



        ArrayList<Line2d> allMapWalls = new ArrayList<>();
        allMapWalls.addAll(map.wall1);
        allMapWalls.addAll(map.wall2);

        boolean isCarActive = true;

        int timer = 0;

        while (true){

            double ltowin = this.position.subR(map.startStop.p2).length();
            if(ltowin < distanceTowin){
//                System.out.println("winner");

//                System.out.println(this.position.x + " " + this.position.y);
                return timer * timeValue;


            }
            if(this.isColisionWithCars(otherCars)){
                return maxTime * timeValue + this.position.subR(map.startStop.p2).length() * distanceValue + colisionValue;
            }

            if(this.isColisionWith(allMapWalls)){
//                System.out.println(this.position.x + " " + this.position.y + "colision " );
                return maxTime * timeValue + this.position.subR(map.startStop.p2).length() * distanceValue + colisionValue;
            }
            timer++;
            if(timer > maxTime){
//                System.out.println(this.position.x + " " + this.position.y + "max TIme " + timer) ;
                return maxTime * timeValue + this.position.subR(map.startStop.p2).length() * distanceValue + colisionValue;

            }
            for(int i = 0; i< otherCars.length; i++){
                otherCars[i].moveByNeuro(map, otherneurons[i] );
            }

            double[] inputs = new double[this.countOfRays * 2 + 1];
            double[] distances = this.distancesToWall(allMapWalls);
            for(int i = 0; i < distances.length; i++){
                inputs[i] = distances[i];
            }
            double[] distancesToCar = this.distancesToOtherCars(otherCars);
            for(int i = distances.length; i < inputs.length - 1; i++){
                inputs[i] = distancesToCar[ i - distancesToCar.length];
            }
            inputs [distances.length] = this.speed/ this.maxSpeed;
            double[] resul = n.curk(inputs);

            if( resul[0] > resul[1]){
                this.moveAcceleraion(1);
            }else{
                this.moveAcceleraion(-1);
            }


            if( resul[2] > resul[3] && resul[2] > resul[4]){
                this.rot(1);
            }
            if( resul[3] > resul[2] && resul[3] > resul[4]){
                this.rot(-1);
            }if(resul[4] > resul[2] && resul[4] > resul[3]){
                this.rot(0);
            }

            this.curc();




        }

    }

    double curkCarResultRender(Graphics g, Camera cam, Map map, NeuroModel n,int distanceTowin, int maxTime , double timeValue, double distanceValue, double colisionValue){

//        this.position = new Vector2(map.startStop.p1);
//        this.rotation = 0;
//
//        this.speed = 0;
//
//        this.curc();

        ArrayList<Line2d> allMapWalls = new ArrayList<>();
        allMapWalls.addAll(map.wall1);
        allMapWalls.addAll(map.wall2);

        boolean isCarActive = true;

        int timer = 0;

        while (true){
            double ltowin = this.position.subR(map.startStop.p2).length();
            if(ltowin < distanceTowin){
                System.out.println("winner");
                return timer * timeValue;


            }

            if(this.isColisionWith(allMapWalls)){
                System.out.println(this.position.x + " " + this.position.y + "colision " );
                return maxTime * timeValue + this.position.subR(map.startStop.p2).length() * distanceValue + colisionValue;
//                return  maxTime;
            }
            timer++;
            if(timer > maxTime){
                System.out.println(this.position.x + " " + this.position.y + "max TIme " + timer) ;
                return maxTime * timeValue + this.position.subR(map.startStop.p2).length() * distanceValue + colisionValue;

            }




            double[] inputs = new double[this.countOfRays + 1];
            double[] distances = this.distancesToWall(allMapWalls );
            for(int i = 0; i < distances.length; i++){
                inputs[i] = distances[i];
            }
            inputs [distances.length] = this.speed/ this.maxSpeed;


            double[] resul = n.curk(inputs);

            if( resul[0] > resul[1]){
                this.moveAcceleraion(1);
            }else{
                this.moveAcceleraion(-1);
            }


            if( resul[2] > resul[3] && resul[2] > resul[4]){
                this.rot(1);
            }
            if( resul[3] > resul[2] && resul[3] > resul[4]){
                this.rot(-1);
            }if(resul[4] > resul[2] && resul[4] > resul[3]){
                this.rot(0);
            }

            this.curc();

            this.render(g, cam);




        }

    }
    Traektoria curkCarTraektorie(Map map, NeuroModel n,int distanceTowin, int maxTime , double timeValue, double distanceValue, double colisionValue){

//        this.position = new Vector2(map.startStop.p1);
//        this.rotation = 0;
//
//        this.speed = 0;
//
//        this.curc();
        ArrayList<Line2d> allMapWalls = new ArrayList<>();
        allMapWalls.addAll(map.wall1);
        allMapWalls.addAll(map.wall2);

        boolean isCarActive = true;

        int timer = 0;
        Vector2 lastPoint = new Vector2(map.startStop.p1);

        ArrayList<Car> cars = new ArrayList<>();
        ArrayList<Double> speeds = new ArrayList<>();

        while (true){
            double ltowin = this.position.subR(map.startStop.p2).length();
            if(ltowin < distanceTowin){
                System.out.println (timer * timeValue);
                break;


            }

            if(this.isColisionWith(allMapWalls)){
                break;
            }
            timer++;
            if(timer > maxTime){
                break;
            }

            double[] inputs = new double[this.countOfRays + 1];
            double[] distances = this.distancesToWall(allMapWalls );
            for(int i = 0; i < distances.length; i++){
                inputs[i] = distances[i];
            }
            inputs [distances.length] = this.speed/ this.maxSpeed;

            double[] resul = n.curk(inputs);

            if( resul[0] > resul[1]){
                this.moveAcceleraion(1);
            }else{
                this.moveAcceleraion(-1);
            }


            if( resul[2] > resul[3] && resul[2] > resul[4]){
                this.rot(1);
            }
            if( resul[3] > resul[2] && resul[3] > resul[4]){
                this.rot(-1);
            }if(resul[4] > resul[2] && resul[4] > resul[3]){
                this.rot(0);
            }

            this.curc();


            Car oneCar = new Car( this);
            oneCar.speed = this.speed;
            cars.add(oneCar);
            speeds.add(this.speed);


        }
        Car[] carsM = new Car[cars.size()];
        carsM = cars.toArray(carsM);

        Double[] speedsM = new Double[speeds.size()];
        speedsM = speeds.toArray(speedsM);

        double[] speedsMprimitive  = new double[speeds.size()];
        for(int i = 0; i < speedsM.length; i++){
            speedsMprimitive[i] = speedsM[i];
        }

        Traektoria tr = new Traektoria(carsM, speedsMprimitive);
        return tr;
    }Traektoria curkCarTraektorieWithOtherCar(Map map, NeuroModel n,Car[] othercars, NeuroModel[] nbots,int distanceTowin, int maxTime , double timeValue, double distanceValue, double colisionValue){

//        this.position = new Vector2(map.startStop.p1);
//        this.rotation = 0;
//
//        this.speed = 0;
//
//        this.curc();
        ArrayList<Line2d> allMapWalls = new ArrayList<>();
        allMapWalls.addAll(map.wall1);
        allMapWalls.addAll(map.wall2);

        boolean isCarActive = true;

        int timer = 0;
        Vector2 lastPoint = new Vector2(map.startStop.p1);

        ArrayList<Car> cars = new ArrayList<>();
        ArrayList<Double> speeds = new ArrayList<>();

        while (true){
            double ltowin = this.position.subR(map.startStop.p2).length();
            if(ltowin < distanceTowin){
                System.out.println (timer * timeValue);
                break;


            }

            timer++;
            if(timer > maxTime){
                break;
            }

            if(this.isColisionWithCars(othercars)){
                System.out.println( maxTime * timeValue + this.position.subR(map.startStop.p2).length() * distanceValue + colisionValue);
                break;
            }

            if(this.isColisionWith(allMapWalls)){
//                System.out.println(this.position.x + " " + this.position.y + "colision " );
                System.out.println( maxTime * timeValue + this.position.subR(map.startStop.p2).length() * distanceValue + colisionValue);
                break;
            }
            timer++;

            for(int i = 0; i< othercars.length; i++){
                othercars[i].moveByNeuro(map, nbots[i] );
            }

            double[] inputs = new double[this.countOfRays * 2 + 1];
            double[] distances = this.distancesToWall(allMapWalls);
            for(int i = 0; i < distances.length; i++){
                inputs[i] = distances[i];
            }
            double[] distancesToCar = this.distancesToOtherCars(othercars);
            for(int i = distances.length; i < inputs.length - 1; i++){
                inputs[i] = distancesToCar[ i - distancesToCar.length];
            }
            inputs [distances.length] = this.speed/ this.maxSpeed;
            double[] resul = n.curk(inputs);

            if( resul[0] > resul[1]){
                this.moveAcceleraion(1);
            }else{
                this.moveAcceleraion(-1);
            }


            if( resul[2] > resul[3] && resul[2] > resul[4]){
                this.rot(1);
            }
            if( resul[3] > resul[2] && resul[3] > resul[4]){
                this.rot(-1);
            }if(resul[4] > resul[2] && resul[4] > resul[3]){
                this.rot(0);
            }

            this.curc();



            Car oneCar = new Car( this);
            oneCar.speed = this.speed;
            cars.add(oneCar);
            speeds.add(this.speed);


        }
        Car[] carsM = new Car[cars.size()];
        carsM = cars.toArray(carsM);

        Double[] speedsM = new Double[speeds.size()];
        speedsM = speeds.toArray(speedsM);

        double[] speedsMprimitive  = new double[speeds.size()];
        for(int i = 0; i < speedsM.length; i++){
            speedsMprimitive[i] = speedsM[i];
        }

        Traektoria tr = new Traektoria(carsM, speedsMprimitive);
        return tr;
    }
    void moveByNeuroRender(Graphics g, Camera cam, Map map, NeuroModel n){


        ArrayList<Line2d> allMapWalls = new ArrayList<>();
        allMapWalls.addAll(map.wall1);
        allMapWalls.addAll(map.wall2);



        double[] inputs = new double[this.countOfRays + 1];
        double[] distances = this.distancesToWall(allMapWalls );
        for(int i = 0; i < distances.length; i++){
            inputs[i] = distances[i];
        }
        inputs [distances.length] = this.speed/ this.maxSpeed;



        double[] resul = n.curk(inputs);

        if( resul[0] > resul[1]){
            this.moveAcceleraion(1);
        }else{
            this.moveAcceleraion(-1);
        }


        if( resul[2] > resul[3] && resul[2] > resul[4]){
            this.rot(1);
        }
        if( resul[3] > resul[2] && resul[3] > resul[4]){
            this.rot(-1);
        }if(resul[4] > resul[2] && resul[4] > resul[3]){
            this.rot(0);
        }

        this.curc();

        this.render(g, cam);

    }
    void moveByNeuroRenderWithOtherCar(Graphics g, Camera cam, Map map, NeuroModel n, Car[] othercars, NeuroModel[] nbots){


        ArrayList<Line2d> allMapWalls = new ArrayList<>();
        allMapWalls.addAll(map.wall1);
        allMapWalls.addAll(map.wall2);



        inputsLokal = new double[this.countOfRays * 2 + 1];
        double[] distances = this.distancesToWall(allMapWalls);
        for(int i = 0; i < distances.length; i++){
            inputsLokal[i] = distances[i];
        }
        double[] distancesToCar = this.distancesToOtherCars(othercars);
        for(int i = distances.length; i < inputsLokal.length - 1; i++){
            inputsLokal[i] = distancesToCar[ i - distancesToCar.length];
        }



        inputsLokal [distances.length] = this.speed/ this.maxSpeed;



        double[] resul = n.curk(inputsLokal);

        if( resul[0] > resul[1]){
            this.moveAcceleraion(1);
        }else{
            this.moveAcceleraion(-1);
        }


        if( resul[2] > resul[3] && resul[2] > resul[4]){
            this.rot(1);
        }
        if( resul[3] > resul[2] && resul[3] > resul[4]){
            this.rot(-1);
        }if(resul[4] > resul[2] && resul[4] > resul[3]){
            this.rot(0);
        }

        this.curc();

        this.render(g, cam);

    }



    void moveByNeuro(Map map, NeuroModel n){


        ArrayList<Line2d> allMapWalls = new ArrayList<>();
        allMapWalls.addAll(map.wall1);
        allMapWalls.addAll(map.wall2);



        double[] inputs = new double[this.countOfRays + 1];
        double[] distances = this.distancesToWall(allMapWalls );
        for(int i = 0; i < distances.length; i++){
            inputs[i] = distances[i];
        }



        inputs [distances.length] = this.speed/ this.maxSpeed;


        double[] resul = n.curk(inputs);

        if( resul[0] > resul[1]){
            this.moveAcceleraion(1);
        }else{
            this.moveAcceleraion(-1);
        }

        if( resul[2] > resul[3] && resul[2] > resul[4]){
            this.rot(1);
        }
        if( resul[3] > resul[2] && resul[3] > resul[4]){
            this.rot(-1);
        }if(resul[4] > resul[2] && resul[4] > resul[3]){
            this.rot(0);
        }

        this.curc();
    }
}
