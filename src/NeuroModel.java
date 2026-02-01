import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

public class NeuroModel {

    int countOfInpunts;
    int wightOfModel;
    int heightOfModel;
    int countofOutput = 2;

    double weightOfIput[][];
    double bOnInput[];

    double weithOfNeurons[][][];
    double bOnNeurons[][];


    double weightOfOutput[][];
    double bOnOutputs[];


    double normalize(double x){
//        double result = 1 / (1 + Math.pow(Math.E, -x));

//        double result = Math.tanh(x);
//        return result;
        return x;
    }


    NeuroModel(int countOfInpunts, int wightOfModel, int heightOfModel, int countofOutput){
        this.countOfInpunts = countOfInpunts;
        this.wightOfModel = wightOfModel;
        this.heightOfModel = heightOfModel;
        this.countofOutput = countofOutput;

        weightOfIput = new double[countOfInpunts][heightOfModel];
        bOnInput = new double[countOfInpunts];

        weithOfNeurons = new double[wightOfModel - 1][ heightOfModel][heightOfModel];
        bOnNeurons = new double[wightOfModel][heightOfModel];

        weightOfOutput = new double[heightOfModel][countofOutput];
        bOnOutputs = new double[countofOutput];


    }

    NeuroModel(NeuroModel other){
        this.countOfInpunts = other.countOfInpunts;
        this.wightOfModel = other.wightOfModel;
        this.heightOfModel = other.heightOfModel;
        this.countofOutput = other.countofOutput;

        weightOfIput = new double[countOfInpunts][heightOfModel];
        bOnInput = new double[countOfInpunts];

        weithOfNeurons = new double[wightOfModel - 1][ heightOfModel][heightOfModel];
        bOnNeurons = new double[wightOfModel][heightOfModel];

        weightOfOutput = new double[heightOfModel][countofOutput];
        bOnOutputs = new double[countofOutput];

        for(int i = 0; i < weightOfIput.length; i++){
            for(int j = 0; j < weightOfIput[i].length ; j++){
                weightOfIput[i][j] = other.weightOfIput[i][j];
            }
        }

        for(int i = 0; i < weithOfNeurons.length; i++){
            for(int j = 0; j < weithOfNeurons[i].length ; j++){
                for(int h = 0; h < weithOfNeurons[i][j].length ; h++){
                    weithOfNeurons[i][j][h] = other.weithOfNeurons[i][j][h];
                }
            }
        }
        for(int i = 0; i < weightOfOutput.length; i++){
            for(int j = 0; j < weightOfOutput[i].length ; j++){
                weightOfOutput[i][j] = other.weightOfOutput[i][j];
            }
        }


        for(int i = 0;i < bOnInput.length ; i++){
            bOnInput[i] = other.bOnInput[i];
        }

        for(int i = 0; i < bOnNeurons.length; i++){
            for(int j = 0; j < bOnNeurons[i].length ; j++){
                bOnNeurons[i][j] = other.bOnNeurons[i][j];
            }
        }
        for(int i = 0;i < bOnOutputs.length ; i++){
            bOnOutputs[i] = other.bOnOutputs[i];
        }



    }

    void setRandom( int max, int maxB){
        for(int i = 0; i < weightOfIput.length; i++){
            for(int j = 0; j < weightOfIput[i].length ; j++){
                weightOfIput[i][j] = (Math.random() - 0.5) * 2 * max;
            }
        }

        for(int i = 0; i < weithOfNeurons.length; i++){
            for(int j = 0; j < weithOfNeurons[i].length ; j++){
                for(int h = 0; h < weithOfNeurons[i][j].length ; h++){
                    weithOfNeurons[i][j][h] = (Math.random() - 0.5) * 2 * max;
                }
            }
        }
        for(int i = 0; i < weightOfOutput.length; i++){
            for(int j = 0; j < weightOfOutput[i].length ; j++){
                weightOfOutput[i][j] = (Math.random() - 0.5) * 2 * max;
            }
        }


        for(int i = 0;i < bOnInput.length ; i++){
            bOnInput[i] = (Math.random() - 0.5) * 2 * maxB;
        }

        for(int i = 0; i < bOnNeurons.length; i++){
            for(int j = 0; j < bOnNeurons[i].length ; j++){
                bOnNeurons[i][j] = (Math.random() - 0.5) * 2 * maxB;
            }
        }
        for(int i = 0;i < bOnOutputs.length ; i++){
            bOnOutputs[i] = (Math.random() - 0.5) * 2 * maxB;
        }

    }
    void setZero( int max, int maxB){
        for(int i = 0; i < weightOfIput.length; i++){
            for(int j = 0; j < weightOfIput[i].length ; j++){
                weightOfIput[i][j] = 0;
            }
        }

        for(int i = 0; i < weithOfNeurons.length; i++){
            for(int j = 0; j < weithOfNeurons[i].length ; j++){
                for(int h = 0; h < weithOfNeurons[i][j].length ; h++){
                    weithOfNeurons[i][j][h] = 0;
                }
            }
        }
        for(int i = 0; i < weightOfOutput.length; i++){
            for(int j = 0; j < weightOfOutput[i].length ; j++){
                weightOfOutput[i][j] = 0;
            }
        }


        for(int i = 0;i < bOnInput.length ; i++){
            bOnInput[i] = 0;
        }

        for(int i = 0; i < bOnNeurons.length; i++){
            for(int j = 0; j < bOnNeurons[i].length ; j++){
                bOnNeurons[i][j] = 0;
            }
        }
        for(int i = 0;i < bOnOutputs.length ; i++){
            bOnOutputs[i] = 0;
        }

    }

    double[] curk(double inputs[]){
        double neurons[][] = new double[wightOfModel][heightOfModel];
        double ammoun = 0;
        for (int i= 0; i <heightOfModel; i++){
            ammoun = 0;
            for(int j = 0; j < countOfInpunts; j++){
                ammoun += weightOfIput[j][i] * inputs[i];
            }
            ammoun += bOnNeurons[0][i];
            neurons[0][i] = normalize(ammoun);
        }
        for(int n = 0; n < wightOfModel -1; n++){
            double[] secondneurons = new double[heightOfModel];
            for (int i= 0; i <heightOfModel; i++){
                ammoun = 0;
                for(int j = 0; j < heightOfModel; j++){
                    ammoun += weithOfNeurons[n][j][i] * neurons[n][i];
                }
                ammoun += bOnNeurons[n+1][i];
                neurons[n+1][i] = normalize(ammoun);
            }

        }

        double result[] = new double[countofOutput];
        for (int i= 0; i <countofOutput; i++){
            ammoun = 0;
            for(int j = 0; j < heightOfModel; j++){
                ammoun += weightOfOutput[j][i] * neurons[wightOfModel-1][j] ;
            }
            ammoun += bOnOutputs[i];
            result[i] = normalize(ammoun);
        }
        return result;
    }


    void safeToFile(String fileName){
        try (PrintWriter pw = new PrintWriter(new FileWriter(fileName))){
            pw.println("weightOfIput");
            for(int i = 0; i < weightOfIput.length; i++){
                for(int j = 0; j < weightOfIput[i].length ; j++){
                    pw.print(weightOfIput[i][j] + " ");
                }
            }
            pw.println();


            pw.println("weithOfNeurons");
            for(int i = 0; i < weithOfNeurons.length; i++){
                for(int j = 0; j < weithOfNeurons[i].length ; j++){
                    for(int h = 0; h < weithOfNeurons[i][j].length ; h++){
                        pw.print( weithOfNeurons[i][j][h] + " ");
                    }
                }
            }
            pw.println();

            pw.println("weightOfOutput");
            for(int i = 0; i < weightOfOutput.length; i++){
                for(int j = 0; j < weightOfOutput[i].length ; j++){
                    pw.print(weightOfOutput[i][j] + " ");
                }
            }
            pw.println();


            pw.println("bOnInput");
            for(int i = 0;i < bOnInput.length ; i++){
                pw.print(bOnInput[i] + " ");
            }
            pw.println();

            pw.println("bOnNeurons");
            for(int i = 0; i < bOnNeurons.length; i++){
                for(int j = 0; j < bOnNeurons[i].length ; j++){
                    pw.print(bOnNeurons[i][j] + " ");
                }
            }
            pw.println();


            pw.println("bOnOutputs");
            for(int i = 0;i < bOnOutputs.length ; i++){
                pw.print(bOnOutputs[i] + " ");
            }
            pw.println();




        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void readFromFile(String fileName){


        try (BufferedReader bf = new BufferedReader(new FileReader(fileName))){
            String line;

            while ((line = bf.readLine()) != null) {
                int index = 0;
                if(line.equals("weightOfIput")){
                    line = bf.readLine();
                    String parts[] = line.split(" ");
                    for(int i = 0; i < weightOfIput.length; i++){
                        for(int j = 0; j < weightOfIput[i].length ; j++){
                            this.weightOfIput[i][j] = Double.parseDouble( parts[index++]);
                        }
                    }

                }

                if(line.equals("weithOfNeurons")){
                    line = bf.readLine();
                    index = 0;
                    String parts[] = line.split(" ");
                    for(int i = 0; i < weithOfNeurons.length; i++){
                        for(int j = 0; j < weithOfNeurons[i].length ; j++){
                            for(int h = 0; h < weithOfNeurons[i][j].length ; h++){
                                weithOfNeurons[i][j][h] = Double.parseDouble( parts[index++]);
                            }
                        }
                    }
                }
                if(line.equals("weightOfOutput")){
                    line = bf.readLine();
                    index = 0;
                    String parts[] = line.split(" ");
                    for(int i = 0; i < weightOfOutput.length; i++){
                        for(int j = 0; j < weightOfOutput[i].length ; j++){
                            this.weightOfOutput[i][j] = Double.parseDouble( parts[index++]);
                        }
                    }
                }
                if(line.equals("bOnInput")){
                    line = bf.readLine();
                    index = 0;
                    String parts[] = line.split(" ");
                    for(int i = 0;i < bOnInput.length ; i++){
                        this.bOnInput[i] = Double.parseDouble( parts[index++]);
                    }
                }
                if(line.equals("bOnNeurons")){
                    line = bf.readLine();
                    index = 0;
                    String parts[] = line.split(" ");
                    for(int i = 0; i < bOnNeurons.length; i++){
                        for(int j = 0; j < bOnNeurons[i].length ; j++){
                            this.bOnNeurons[i][j] = Double.parseDouble( parts[index++]);
                        }
                    }

                }
                if(line.equals("bOnOutputs")){
                    line = bf.readLine();
                    index = 0;
                    String parts[] = line.split(" ");
                    for(int i = 0;i < bOnOutputs.length ; i++){
                        this.bOnOutputs[i] = Double.parseDouble( parts[index++]);
                    }
                }



            }

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("NeuroFile not found. Setting random neurouns");
            this.setRandom(1,1);
        }
    }
    void setWeith(int apsoluteI, double value){
        if (apsoluteI < countOfInpunts * heightOfModel){
            int x = apsoluteI / heightOfModel;
            int y = apsoluteI % heightOfModel;

            weightOfIput[x][y] = value;
            return;
        }

        if (apsoluteI >= countOfInpunts * heightOfModel & apsoluteI < countOfInpunts * heightOfModel + ((wightOfModel -1) * heightOfModel * heightOfModel)) {
            int x = (apsoluteI - countOfInpunts * heightOfModel)/ (heightOfModel * heightOfModel);
            int remainder = apsoluteI % (heightOfModel * heightOfModel);

            int y = remainder / heightOfModel;
            int z = remainder % heightOfModel;

            weithOfNeurons[x][y][z] = value;
            return;
        }else {
            int x = (apsoluteI-  (countOfInpunts * heightOfModel + ((wightOfModel -1) * heightOfModel * heightOfModel))) / countofOutput;     // номер входу
            int y = (apsoluteI-  (countOfInpunts * heightOfModel + ((wightOfModel -1) * heightOfModel * heightOfModel))) % countofOutput;     // позиція у цьому вході

            weightOfOutput[x][y] = value;


        }
    }
    void setB(int apsoluteI, double value){
        if (apsoluteI < countofOutput){
            bOnOutputs[apsoluteI] = value;
            return;
        }

        if (apsoluteI >= countofOutput &  apsoluteI <= wightOfModel * heightOfModel + countofOutput) {
            int x = (apsoluteI - countofOutput) / heightOfModel;
            int y = (apsoluteI - countofOutput) % heightOfModel;

            bOnNeurons[x][y] = value;
            return;

        }
    }

    void changeWeith(int apsoluteI, double value){
        if (apsoluteI < countOfInpunts * heightOfModel){
            int x = apsoluteI / heightOfModel;
            int y = apsoluteI % heightOfModel;

            weightOfIput[x][y] += value;
            return;
        }

        if (apsoluteI >= countOfInpunts * heightOfModel & apsoluteI < countOfInpunts * heightOfModel + ((wightOfModel -1) * heightOfModel * heightOfModel)) {
            int x = (apsoluteI - countOfInpunts * heightOfModel)/ (heightOfModel * heightOfModel);
            int remainder = apsoluteI % (heightOfModel * heightOfModel);

            int y = remainder / heightOfModel;
            int z = remainder % heightOfModel;

            weithOfNeurons[x][y][z] += value;
            return;
        }else {
            int x = (apsoluteI- (countOfInpunts * heightOfModel + ((wightOfModel -1) * heightOfModel * heightOfModel))) / countofOutput;     // номер входу
            int y = (apsoluteI- (countOfInpunts * heightOfModel + ((wightOfModel -1) * heightOfModel * heightOfModel))) % countofOutput;     // позиція у цьому вході

            weightOfOutput[x][y] += value;


        }
    }
    void changeB(int apsoluteI, double value){
        if (apsoluteI < countofOutput){
            bOnOutputs[apsoluteI] += value;
            return;
        }

        if (apsoluteI >= countofOutput &  apsoluteI <= wightOfModel * heightOfModel + countofOutput) {
            int x = (apsoluteI - countofOutput) / heightOfModel;
            int y = (apsoluteI - countofOutput) % heightOfModel;

            bOnNeurons[x][y] += value;
            return;

        }
    }

    double getWeith(int apsoluteI){
        if (apsoluteI < countOfInpunts * heightOfModel){
            int x = apsoluteI / heightOfModel;
            int y = apsoluteI % heightOfModel;


            return weightOfIput[x][y];
        }

        if (apsoluteI >= countOfInpunts * heightOfModel & apsoluteI < countOfInpunts * heightOfModel + ((wightOfModel-1) * heightOfModel * heightOfModel)) {
            int x = (apsoluteI - countOfInpunts * heightOfModel)/ (heightOfModel * heightOfModel);
            int remainder = apsoluteI % (heightOfModel * heightOfModel);

            int y = remainder / heightOfModel;
            int z = remainder % heightOfModel;


            return weithOfNeurons[x][y][z];
        }else {
            int x = (apsoluteI-(countOfInpunts * heightOfModel + ((wightOfModel-1) * heightOfModel * heightOfModel))) / countofOutput;     // номер входу
            int y = (apsoluteI-(countOfInpunts * heightOfModel + ((wightOfModel-1) * heightOfModel * heightOfModel))) % countofOutput;     // позиція у цьому вході


            return weightOfOutput[x][y];

        }
    }
    double getB(int apsoluteI){
        if (apsoluteI < countofOutput){
            return bOnOutputs[apsoluteI];
        }

        if (apsoluteI >= countofOutput &  apsoluteI <= wightOfModel * heightOfModel + countofOutput) {
            int x = (apsoluteI - countofOutput) / heightOfModel;
            int y = (apsoluteI - countofOutput) % heightOfModel;

            return bOnNeurons[x][y];

        }
        else {
            return 0;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        NeuroModel that = (NeuroModel) o;

        return countOfInpunts == that.countOfInpunts &&
                wightOfModel == that.wightOfModel &&
                heightOfModel == that.heightOfModel &&
                countofOutput == that.countofOutput &&
                Arrays.deepEquals(weightOfIput, that.weightOfIput) &&
                Arrays.equals(bOnInput, that.bOnInput) &&
                Arrays.deepEquals(weithOfNeurons, that.weithOfNeurons) &&
                Arrays.deepEquals(bOnNeurons, that.bOnNeurons) &&
                Arrays.deepEquals(weightOfOutput, that.weightOfOutput) &&
                Arrays.equals(bOnOutputs, that.bOnOutputs);
    }
}


