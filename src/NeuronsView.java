import java.awt.*;

public class NeuronsView extends Object2d{

    NeuroModel n;
    Camera cam;


    int radius = 10;

    NeuronsView(NeuroModel n){
        this.n = new NeuroModel(n);
        cam = new Camera();
    }



    void render(Graphics g, Vector2 mouseDelta, boolean rightClick , int wheelSpin){
        if(rightClick){
            this.position.sub(mouseDelta);
            this.cam.position.sub(mouseDelta);
        }
        float zoom;

        zoom =  wheelSpin + 10;
        zoom = zoom /10;
        if(zoom <= 0){
            zoom = 0.001F;
        }
//        Vector2 test = new Vector2(0,0);
//        Vector2 test2 = new Vector2(50,50);

        cam.scale = zoom;

//        g.fillOval( (int) ((((0 * zoom) - radius/2) - this.position.x) ),(int) ((((0 * zoom) - radius/2) - this.position.y)), (int)(radius * zoom),(int)(radius * zoom));
//        g.fillOval( (int) ((((50 * zoom) - radius/2) - this.position.x) ),(int) ((((50 * zoom) - radius/2) - this.position.y)), (int)(radius * zoom),(int)(radius * zoom));
//        g.fillOval( (int) ((((70 * zoom) - radius/2) - this.position.x) ),(int) ((((80 * zoom) - radius/2) - this.position.y)), (int)(radius * zoom),(int)(radius * zoom));
//        test.drawPointWithZoom(10,g,cam, Color.red);
//        test.drawLine(g, cam,Color.BLUE , test2);
//        test2.drawPointWithZoom(10,g,cam, Color.BLUE);

        for(int i = 0; i < n.countOfInpunts; i++){

            new Vector2(0, i *radius *2).drawPointWithZoom(radius,g,cam,new Color( (int) Math.abs ((n.bOnInput[i]) * 255)  ,255,255));
        }
        for(int i = 0; i < n.wightOfModel; i++){
            for(int j=0; j< n.heightOfModel; j++){
                new Vector2((i * radius* 3) + radius *5, j *radius *2).drawPointWithZoom(radius,g,cam,Color.red);

            }
        }
        for(int i = 0; i < n.countofOutput; i++){
            new Vector2((n.wightOfModel * radius* 3) + radius *6, i *radius *2).drawPointWithZoom(radius,g,cam,Color.red);

        }
    }
    void renderRealTime(Graphics g, Vector2 mouseDelta, boolean rightClick , int wheelSpin, double inputs[]){


        int maxValueColorRender = 1;

        int veticalSpace = 3;  // in radius
        int horizontalSpace = 40; // in radius



        if(rightClick){
            this.position.sub(mouseDelta);
            this.cam.position.sub(mouseDelta);
        }
        float zoom;

        zoom =  wheelSpin + 10;
        zoom = zoom /10;
        if(zoom <= 0){
            zoom = 0.001F;
        }
//        Vector2 test = new Vector2(0,0);
//        Vector2 test2 = new Vector2(50,50);

        cam.scale = zoom;

//        g.fillOval( (int) ((((0 * zoom) - radius/2) - this.position.x) ),(int) ((((0 * zoom) - radius/2) - this.position.y)), (int)(radius * zoom),(int)(radius * zoom));
//        g.fillOval( (int) ((((50 * zoom) - radius/2) - this.position.x) ),(int) ((((50 * zoom) - radius/2) - this.position.y)), (int)(radius * zoom),(int)(radius * zoom));
//        g.fillOval( (int) ((((70 * zoom) - radius/2) - this.position.x) ),(int) ((((80 * zoom) - radius/2) - this.position.y)), (int)(radius * zoom),(int)(radius * zoom));
//        test.drawPointWithZoom(10,g,cam, Color.red);
//        test.drawLine(g, cam,Color.BLUE , test2);
//        test2.drawPointWithZoom(10,g,cam, Color.BLUE);

//        double[] curk(double inputs[]){


        for(int i = 0; i < inputs.length; i++){
            int color = (int)  Math.max( -255,Math.min((inputs[i] / maxValueColorRender ) * 255,255));//// -255   -->   255
            if(color>0){
                new Vector2(0, i *radius *veticalSpace).drawPointWithZoomText(radius,g,cam,new Color(  0 ,color,0), inputs[i]);
            }else{
                new Vector2(0, i *radius *veticalSpace).drawPointWithZoomText(radius,g,cam,new Color(  -color ,0,0), inputs[i]);
            }


        }

        double neurons[][] = new double[n.wightOfModel][n.heightOfModel];
        double ammoun = 0;
        for (int i= 0; i <n.heightOfModel; i++){
            ammoun = 0;
            for(int j = 0; j < n.countOfInpunts; j++){
                ammoun += n.weightOfIput[j][i] * inputs[i];
                int color = (int)  Math.max(-255, Math.min((n.weightOfIput[j][i] * inputs[i] / maxValueColorRender ) * 255,255));//// -255   -->   255
                if(color>0){
                    new Vector2(0, j *radius *veticalSpace).drawLineWithZoom(g,cam,new Color(  0 ,color,0), new Vector2(horizontalSpace * radius, i * radius *veticalSpace));
                }else{
                    new Vector2(0, j *radius *veticalSpace).drawLineWithZoom(g,cam,new Color(  -color ,0,0), new Vector2(horizontalSpace * radius, i * radius *veticalSpace));
                }

            }
            ammoun += n.bOnNeurons[0][i];
            neurons[0][i] = n.normalize(ammoun);

            int color = (int)  Math.max(-255, Math.min((ammoun / maxValueColorRender ) * 255,255));//// -255   -->   255
            if(color>0){
                new Vector2(horizontalSpace * radius, i * radius *veticalSpace).drawPointWithZoomText(radius,g,cam,new Color(  0 ,color,0), ammoun);
            }else{
                new Vector2(horizontalSpace * radius, i * radius *veticalSpace).drawPointWithZoomText(radius,g,cam,new Color(  -color ,0,0), ammoun);
            }


        }
        for(int h = 0; h < n.wightOfModel -1; h++){
            double[] secondneurons = new double[n.heightOfModel];
            for (int i= 0; i <n.heightOfModel; i++){
                ammoun = 0;
                for(int j = 0; j < n.heightOfModel; j++){
                    ammoun += n.weithOfNeurons[h][j][i] * neurons[h][i];

                    int color = (int)  Math.max(-255, Math.min((n.weithOfNeurons[h][j][i] * neurons[h][i] / maxValueColorRender ) * 255,255));//// -255   -->   255
                    if(color>0){
                        new Vector2((h+ 1) * radius* horizontalSpace , j *radius *veticalSpace).drawLineWithZoom(g,cam,new Color( 0,color,0), new Vector2( (h + 2)* radius* horizontalSpace , i * radius *veticalSpace));
                    }else{
                        new Vector2((h+ 1) * radius* horizontalSpace , j *radius *veticalSpace).drawLineWithZoom(g,cam,new Color(  -color ,0,0), new Vector2((h + 2) * radius* horizontalSpace + 2, i * radius *veticalSpace));
                    }
                }
                ammoun += n.bOnNeurons[h+1][i];
                neurons[h+1][i] = n.normalize(ammoun);


                int color = (int)  Math.max(-255, Math.min((ammoun / maxValueColorRender ) * 255,255));//// -255   -->   255
                if(color>0){
                    new Vector2((h + 2)* radius* horizontalSpace , i * radius *veticalSpace).drawPointWithZoomText(radius,g,cam,new Color(  0 ,color,0), ammoun);
                }else{
                    new Vector2((h + 2)* radius* horizontalSpace , i * radius *veticalSpace).drawPointWithZoomText(radius,g,cam,new Color(  -color ,0,0), ammoun);
                }

            }

        }

        double result[] = new double[n.countofOutput];
        for (int i= 0; i <n.countofOutput; i++){
            ammoun = 0;
            for(int j = 0; j < n.heightOfModel; j++){
                ammoun += n.weightOfOutput[j][i] * neurons[n.wightOfModel-1][j] ;

                int color = (int)  Math.max(-255, Math.min((n.weightOfOutput[j][i] * neurons[n.wightOfModel-1][j]  / maxValueColorRender ) * 255,255));//// -255   -->   255
                if(color>0){
                    new Vector2(n.wightOfModel  * radius* horizontalSpace , j *radius *veticalSpace).drawLineWithZoom(g,cam,new Color(  0 ,color,0), new Vector2((n.wightOfModel + 1) * radius* horizontalSpace , i * radius *veticalSpace));
                }else{
                    new Vector2(n.wightOfModel  * radius* horizontalSpace , j *radius *veticalSpace).drawLineWithZoom(g,cam,new Color(  -color ,0,0), new Vector2((n.wightOfModel + 1) * radius* horizontalSpace , i * radius *veticalSpace));
                }
            }
            ammoun += n.bOnOutputs[i];
            result[i] = n.normalize(ammoun);


            int color = (int)  Math.max(-255, Math.min((ammoun / maxValueColorRender ) * 255,255));//// -255   -->   255
            if(color>0){
                new Vector2( (n.wightOfModel + 1) * radius* horizontalSpace , i * radius *veticalSpace).drawPointWithZoomText(radius,g,cam,new Color(  0 ,color,0), ammoun);
            }else{
                new Vector2((n.wightOfModel + 1) * radius* horizontalSpace , i * radius *veticalSpace).drawPointWithZoomText(radius,g,cam,new Color(  -color ,0,0), ammoun);
            }
        }
        if( result[0] > result[1]){
            new Vector2( (n.wightOfModel + 1) * radius* horizontalSpace + radius, 0 * radius *veticalSpace).drawPointWithZoom(radius,g,cam,new Color(  0 ,255,0));

        }else{
            new Vector2( (n.wightOfModel + 1) * radius* horizontalSpace + radius , 1 * radius *veticalSpace).drawPointWithZoom(radius,g,cam,new Color(  0 ,255,0));
        }


        if( result[2] > result[3] && result[2] > result[4]){
            new Vector2( (n.wightOfModel + 1) * radius* horizontalSpace + radius , 2 * radius *veticalSpace).drawPointWithZoom(radius,g,cam,new Color(  0 ,255,0));
        }
        if( result[3] > result[2] && result[3] > result[4]){
            new Vector2( (n.wightOfModel + 1) * radius* horizontalSpace + radius , 3 * radius *veticalSpace).drawPointWithZoom(radius,g,cam,new Color(  0 ,255,0));
        }if(result[4] > result[2] && result[4] > result[3]){
            new Vector2( (n.wightOfModel + 1) * radius* horizontalSpace + radius , 4 * radius *veticalSpace).drawPointWithZoom(radius,g,cam,new Color(  0 ,255,0));
        }


    }




}
