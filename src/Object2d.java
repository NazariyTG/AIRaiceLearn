public class Object2d {
    Vector2 position;
    double rotation;
    double scale;

    Object2d (){
        this.position = new Vector2(0,0);
        this.rotation = 0;
        this.scale = 0;

    }
    Object2d(double posX, double posY, double rotation, double scale){
        this.position.x = posX;
        this.position.y = posY;
        this.rotation = 0;
        this.scale = scale;
    }
    Object2d( Vector2 position, double rotation, double scale){
        this.position = position;
        this.rotation = rotation;
        this.scale  = scale;
    }

//    void move(Vector2 delta){
//        this.position.add(delta);
//    }
//
//    void rotate(double angle){
//        this.rotation = angle;
//    }
//
//    void scale(double scale)





}
