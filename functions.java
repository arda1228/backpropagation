import java.util.Random;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.*;

class functions {
    public double sigmoidactivate(double input){
        double sigmoidactivated = 1 / (1 + Math.exp(-input));
        return sigmoidactivated;
    }
    public ArrayList<Float>[][]  initPathWeights() {
    Random rd = new Random();
    int layerCounter = 1;
    ArrayList<Float>[][] initPathWeights = new ArrayList[6][8]; 
    boolean keepgoing = true; 
    Scanner myObj = new Scanner(System.in);  // Create a Scanner object
    System.out.println("Enter number of nodes in input layer");
    int noOfNodesPrev = myObj.nextInt();
    while (keepgoing == true){
        System.out.println("Enter number of nodes in next layer");
        int noOfNodesCurrent = myObj.nextInt();
        if (noOfNodesCurrent == 0){
            keepgoing = false;
            System.out.println("stopped");
        }
        else {
            for (int i = 0; i < noOfNodesCurrent * noOfNodesPrev ; i++){
                System.out.println("ok");
                initPathWeights[1][0].add(rd.nextFloat());
            }
            layerCounter++;
            noOfNodesPrev = noOfNodesCurrent;
        }
    }
    return initPathWeights;
    }

    public static void main(String[] args) {
        functions test = new functions();
        System.out.println(test.initPathWeights());
    }
}

// get no. of inputs (i)
// get no. of nodes in first hidden layer (h1), number of weights between them =
// i*h1
// get no. of nodes in second hidden layer (h2), number of weights on paths
// between hidden layers 1 & 2 = h2*h1
// |w|=hn*hn-1
// use this to add to a 2d arraylist
// stop when nodes is input as 0

// FUNCTION FOR CREATING ARRAYS OF INPUTS AND HIDDEN LAYERS AND OUTPUTS