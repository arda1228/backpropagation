import java.io.FileNotFoundException;//catches error if no file found
// import java.lang.reflect.Array;
import java.util.ArrayList;//adaptable-length array
import java.util.Arrays;
import java.util.List;
import java.util.Random;


public class dataPreprocessing {

    public List<List<String>> deleteDates(String filename) throws FileNotFoundException {// function to delete the dates at the
                                                                          // beginning of every row of inputs
        // argument is taken in form List<List<String>> as this is how it is read from
        // the .csv file
        fileOperations test = new fileOperations();
        List<List<String>> originals = test.getValues(filename);// reads values from file into a list
        originals.remove(0);// takes away the place names of each input
        for (List list : originals) {
            list.remove(0);// removes first value

        }
        return originals;
    }

    public double[][] castingToDouble(List<List<String>> inputs) {
        // make each list an array, cast the whole wider list or lists to an array, then
        // go through the arrays and convert all the values to doubles, weeding out
        // non-numerical values
        List<String> tempI;// declare a variable to temporarily hold each list within inputs
        double tempDouble;// declare a variable to temporarily hold each value within a list in inputs
        boolean rowHasNoNegatives;// flag indicating if the row is free of non-numerical values
        boolean noNegatives;// flag indicating the row has no negative values
        System.out.println(inputs.size());
        int initialSize = inputs.size();
        List<List<String>> inputsNumerical = new ArrayList<>();
        for (int i = 0; i < initialSize; i++) {// iterating through inputs
            rowHasNoNegatives = true;
            tempI = inputs.get(i);
            for (int j = 0; j < tempI.size(); j++) {// iterating through an individual row of inputs
                try {
                    tempDouble = Double.parseDouble(tempI.get(j));// attempts to cast numerical value to type double
                } catch (Exception e) {// if value non-numerical
                    rowHasNoNegatives = false;
                }
            }
            noNegatives = true;
            if (rowHasNoNegatives) {// no non-numerical values detected
                for (int k = 0; k < tempI.size(); k++) {
                    if (Double.parseDouble(tempI.get(k)) < 0) {// checks if the value, when parsed to a double, is
                                                               // negative
                        noNegatives = false;
                    }
                }
                if (noNegatives) {// no negatives detected
                    inputsNumerical.add(tempI);// new 2d array of the numerical non-negative inputs
                }
            }
        }

        double[][] inputsAsDoubles = new double[inputsNumerical.size()][inputsNumerical.get(0).size()];
        // allocates space in memory for the inputs, as a 2d array of type double
        for (int i = 0; i < inputsNumerical.size(); i++) {// iterating through inputs
            tempI = inputsNumerical.get(i);
            for (int j = 0; j < tempI.size(); j++) {// iterating through an individual list
                tempDouble = Double.valueOf(tempI.get(j));// attempts to cast numerical value to type double
                inputsAsDoubles[i][j] = tempDouble;// assigns to index
            }
        }
        // for (double[] i : inputsAsDoubles) {
        // for (double j : i)
        // System.out.println(j);
        // }
        return inputsAsDoubles;
    }

    public double[][] eliminateOutliers(double[][] inputsWithOutliers) {// finding and eliminating outliers
        double[] totals = new double[inputsWithOutliers[0].length];// 1d array of the running totals for each predictor
        double[] means = new double[inputsWithOutliers[0].length];// 1d array of the means for each predictor
        double[] totalDeviationsSquared = new double[inputsWithOutliers[0].length];// 1d array of the total deviation
                                                                                   // from the mean squared for each
                                                                                   // predictor
        double[] standardDeviations = new double[inputsWithOutliers[0].length];// 1d array of the standard deviations
                                                                               // for each predictor
        double[] upperBounds = new double[means.length];// 1d array of all the means for each predictor
        double[] lowerBounds = new double[means.length];// 1d array of all the means for each predictor
        boolean validRow;// flag that is true if the row of inputs contains no outliers (more than 2
                         // standard deviations from the mean)
        // go through every value for each predictor and add it to its respective total
        for (int i = 0; i < inputsWithOutliers[0].length; i++) {
            for (int j = 0; j < inputsWithOutliers.length; j++) {
                totals[i] += inputsWithOutliers[j][i];
            }
            // System.out.println("totals " + i + ": " + totals[i]);
        }
        // go through every value for each predictor and add it to its respective total
        // squared deviation
        for (int i = 0; i < inputsWithOutliers[0].length; i++) {
            // finding means
            means[i] = totals[i] / inputsWithOutliers.length;
            // System.out.println("means " + i + ": " + means[i]);
            for (int j = 0; j < inputsWithOutliers.length; j++) {
                totalDeviationsSquared[i] += Math.pow(inputsWithOutliers[j][i] - means[i], 2);// total squared deviation
            }
            standardDeviations[i] = Math.sqrt(totalDeviationsSquared[i] / inputsWithOutliers.length);// final standard
                                                                                                     // deviations
            // System.out.println("standard deviations " + i + ": " +
            // standardDeviations[i]);
        }
        // establishing upper and lower bounds and weeding outliers out
        for (int i = 0; i < upperBounds.length; i++) {
            upperBounds[i] = means[i] + (2 * standardDeviations[i]);// 2 SD from mean
            // System.out.println("upper bound " + i + ": " + upperBounds[i]);
            lowerBounds[i] = means[i] - (2 * standardDeviations[i]);// 2 SD from mean
            // System.out.println("lower bound " + i + ": " + lowerBounds[i]);
        }
        // eliminating outliers
        int numberOfValidRows = 0;
        boolean[] validityRegister = new boolean[inputsWithOutliers.length];

        for (int i = 0; i < inputsWithOutliers.length; i++) {
            validRow = true;
            for (int j = 0; j < inputsWithOutliers[0].length; j++) {
                // checking if there are any outliers in each row
                if (inputsWithOutliers[i][j] > upperBounds[j] || inputsWithOutliers[i][j] < lowerBounds[j]) {
                    validRow = false;
                }
            }
            if (validRow) {
                numberOfValidRows++;
            }
            validityRegister[i] = validRow;
        }

        double[][] outliersEliminated = new double[numberOfValidRows][inputsWithOutliers[0].length];
        int outliersEliminated2Index = 0;

        for (int i = 0; i < inputsWithOutliers.length; i++) {
            if (validityRegister[i]) {
                outliersEliminated[outliersEliminated2Index] = inputsWithOutliers[i];
                outliersEliminated2Index++;
            }
        }
        // System.out.println("outliersEliminated2.length" + outliersEliminated.length);
        // System.out.println("inputsWithOutliers.length" + inputsWithOutliers.length);
        // for (double[] i : outliersEliminated) {
        // System.out.println(Arrays.toString(i));
        // }
        return outliersEliminated;
    }

    public standardisedPackager standardiseInputs(double[][] unstandardisedInputs) {// standardising inputs in range
                                                                                    // [0.1,0.9]
        double[][] inputsStandardised = new double[unstandardisedInputs.length][unstandardisedInputs[0].length];// for
                                                                                                                // final
                                                                                                                // return
        // FIX

        double[] mins = { 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000 };
        double[] maxes = { 0, 0, 0, 0, 0, 0, 0, 0 };
        // double min = unstandardisedInputs[0][0];
        // double max = unstandardisedInputs[0][0];
        // finding max and min values for each input
        for (int j = 0; j < unstandardisedInputs.length; j++) {
            for (int i = 0; i < unstandardisedInputs[0].length; i++) {
                if (unstandardisedInputs[j][i] > maxes[i]) {
                    maxes[i] = unstandardisedInputs[j][i];
                }
                if (unstandardisedInputs[j][i] < mins[i]) {
                    mins[i] = unstandardisedInputs[j][i];
                }
            }
        }
        for (int i = 0; i < mins.length; i++) {
            System.out.println("min value" + i + ": " + mins[i]);
            System.out.println("max value" + i + ": " + maxes[i]);
        }
        // standardising inputs
        for (int j = 0; j < inputsStandardised.length; j++) {
            for (int i = 0; i < maxes.length; i++) {
                // FIX
                if (maxes[i] == mins[i]) {
                    inputsStandardised[j][i] = 0.9;
                } else {
                    inputsStandardised[j][i] = (0.8 * (((unstandardisedInputs[j][i] - mins[i]) / (maxes[i] - mins[i]))))
                            + 0.1;
                }
                // System.out.println("standardised value: " + inputsStandardised[j][i]);
            }
        }
        // for (double[] i : inputsStandardised) {
        // System.out.println(Arrays.toString(i));
        // }
        return new standardisedPackager(inputsStandardised, mins, maxes);
    }

    public double[][] shuffleArray(double[][] inputArray) {
        Random rand = new Random(42);
        int randomIndextoSwap;
        double[] tempArray = new double[inputArray[0].length];
        for (int i = 0; i < inputArray.length; i++) {
            randomIndextoSwap = rand.nextInt(inputArray.length);
            tempArray = inputArray[randomIndextoSwap];
            inputArray[randomIndextoSwap] = inputArray[i];
            inputArray[i] = tempArray;
        }
        // for (double[] i : inputArray) {
        // System.out.println(Arrays.toString(i));
        // }
        return inputArray;
    }

    public double[][] repositionOutputToEnd(double[][] inputArray, int targetColumn) {
        double[][] repositionedArray = new double[inputArray.length][inputArray[0].length];
        int runningIndex;
        for (int i = 0; i < inputArray.length; i++) {
            runningIndex = 0;
            for (int j = 0; j < inputArray[0].length; j++) {
                if (j != targetColumn) {
                    repositionedArray[i][runningIndex] = inputArray[i][j];
                    runningIndex++;
                }
            }
            repositionedArray[i][repositionedArray[0].length - 1] = inputArray[i][targetColumn];
        }
        // System.out.println("===repositionedArray START===");
        // for (double[] i : repositionedArray) {
        // System.out.println(Arrays.toString(i));
        // }
        // System.out.println("===repositionedArray END===");
        return repositionedArray;
    }

    public double[][] getOneDayAheadOutputInTheRawAsOutput(double[][] inputArray) {
        double[][] outputRepositionedFromNextDayArray = new double[inputArray.length - 1][inputArray[0].length];
        for (int i = 0; i < inputArray.length - 1; i++) {
            outputRepositionedFromNextDayArray[i] = inputArray[i];
            outputRepositionedFromNextDayArray[i][inputArray[0].length - 1] = inputArray[i + 1][inputArray[0].length
                    - 1];
        }
        // System.out.println("===outputRepositionedFromNextDayArray START===");
        // for (double[] i : outputRepositionedFromNextDayArray) {
        // System.out.println(Arrays.toString(i));
        // }
        // System.out.println("inputArray.length: "+inputArray.length);
        // System.out.println("outputRepositionedFromNextDayArray.length: "+outputRepositionedFromNextDayArray.length);
        // System.out.println("===outputRepositionedFromNextDayArray END===");
        return outputRepositionedFromNextDayArray;
    }

    class standardisedPackager {//contains all data needed to (de)standardise a value
        double[][] inputsStandardised;
        double[] mins;
        double[] maxes;

        standardisedPackager(double[][] inputsStandardised, double[] mins, double[] maxes) {
            this.inputsStandardised = inputsStandardised;
            this.mins = mins;
            this.maxes = maxes;
        }
    }

    class dataSplitter {
        double[][] trainingSet;
        double[][] validationSet;
        double[][] testSet;

        dataSplitter(double[][] v1, double[][] v2, double[][] v3) {
            trainingSet = v1;
            validationSet = v2;
            testSet = v3;
        }
    }

    public double destandardisedValue(double Si, double Min, double Max) {
        return ((((Si - 0.1) / 0.8) * (Max - Min)) + Min);
    }

    public dataSplitter splitData(double[][] inputArray, double percentTrainingSet, double percentvalidationSet,
            double percentTestSet) {
        int trainingDataCount = (int) Math.floor(inputArray.length * percentTrainingSet);
        int validationDataCount = (int) Math.floor(inputArray.length * percentvalidationSet);
        int testDataCount = (int) Math.floor(inputArray.length * percentTestSet);
        double[][] trainingSet = new double[trainingDataCount][inputArray[0].length];
        double[][] validationSet = new double[validationDataCount][inputArray[0].length];
        double[][] testSet = new double[testDataCount][inputArray[0].length];
        for (int trainingIndex = 0; trainingIndex < trainingDataCount; trainingIndex++) {
            trainingSet[trainingIndex] = inputArray[trainingIndex];
        }
        for (int validationIndex = 0; validationIndex < validationDataCount; validationIndex++) {
            validationSet[validationIndex] = inputArray[trainingDataCount + validationIndex];
        }
        for (int testIndex = 0; testIndex < testDataCount; testIndex++) {
            testSet[testIndex] = inputArray[trainingDataCount + validationDataCount + testIndex];
        }
        // for (double[] i : testSet) {
        //     System.out.println(Arrays.toString(i));
        // }       
        return new dataSplitter(trainingSet, validationSet, testSet);
    }

    public static void main(String[] args) throws FileNotFoundException {
        // rearranging trial = new rearranging();
        // List<List<String>> deleteddates = trial.deleteDates("arda.csv");
        // double[][] cast = trial.castingToDouble(deleteddates);
        // // trial.eliminateOutliers(cast); //BASRI COMMENTED OUT
        // // trial.standardiseInputs(sd);
        // // BASRI
        // double[][] cleanedData = trial.eliminateOutliers(cast);
        // double[][] repositionedArray = trial.repositionOutputToEnd(cleanedData, 3); // targetColumn: skelton column -after date removed
        // double[][] outputRepositionedFromNextDayArray = trial.getOneDayAheadOutputInTheRawAsOutput(repositionedArray); 

        // double[][] shuffledArray = trial.shuffleArray(outputRepositionedFromNextDayArray);
        // standardisedPackager standardizedPack = trial.standardiseInputs(shuffledArray);

        // System.out.println("min value 7:====> " + standardizedPack.mins[7]);
        // System.out.println("max value 7:====> " + standardizedPack.maxes[7]);
        // System.out.println("standardised value - row 0  output:====> " + standardizedPack.inputsStandardised[0][7]);
        // System.out.println("Destandardised Value of output of some random row output: " + trial.destandardisedValue(
        //         standardizedPack.inputsStandardised[0][7], standardizedPack.mins[7], standardizedPack.maxes[7]));
        // System.out.println("Destandardised Value of 0th element of some random row output: "
        //         + trial.destandardisedValue(standardizedPack.inputsStandardised[0][0], standardizedPack.mins[0],
        //                 standardizedPack.maxes[0]));
        // System.out.println("Destandardised Value of 1st element of some random row output: "
        //         + trial.destandardisedValue(standardizedPack.inputsStandardised[0][1], standardizedPack.mins[1],
        //                 standardizedPack.maxes[1]));
                        
        // dataSplitter splittedData = trial.splitData(standardizedPack.inputsStandardised, 0.9, 0.01, 0.09);
        // System.out.println("=====Listing validationSet - START ");
        // for (double[] i : splittedData.validationSet) {
        //     System.out.println(Arrays.toString(i));
        // }
        // System.out.println("Total Count - validationSet: "+splittedData.validationSet.length);
        // System.out.println("=====Listing validationSet - END ");

    }
}
