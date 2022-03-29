import java.io.FileNotFoundException;//catches error if no file found
// import java.lang.reflect.Array;
import java.util.ArrayList;//adaptable-length array
// import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class dataPreprocessing {

    public List<List<String>> deleteDates(String filename) throws FileNotFoundException {
        // function to delete the dates at the beginning of every row of inputs
        // return is given in form List<List<String>> as this is how it is read from
        // the .csv file
        fileOperations reader = new fileOperations();
        List<List<String>> originals = reader.getValues(filename);// reads values from file into a list
        originals.remove(0);// takes away the place names of each input
        for (List list : originals) {
            list.remove(0);// removes first value - the date

        }
        return originals;
    }

    public double[][] castingToDouble(List<List<String>> inputs) {
        // make each list an array, cast the whole wider list of lists to an array, then
        // go through the arrays and convert all the values to doubles, weeding out
        // non-numerical values
        List<String> tempI;// declare a variable to temporarily hold each list within inputs
        double tempDouble;// declare a variable to temporarily hold each value within a list in inputs
        boolean rowHasOnlyNumbers;// flag indicating if the row is free of non-numerical values
        boolean noNegatives;// flag indicating the row has no negative values
        int initialSize = inputs.size();
        List<List<String>> inputsNumerical = new ArrayList<>();
        for (int i = 0; i < initialSize; i++) {// iterating through inputs
            rowHasOnlyNumbers = true;// assumes true until proven otherwise
            tempI = inputs.get(i);
            for (int j = 0; j < tempI.size(); j++) {// iterating through an individual row of inputs
                try {
                    tempDouble = Double.parseDouble(tempI.get(j));// attempts to cast numerical value to type double
                } catch (Exception e) {// if value non-numerical, row not used
                    rowHasOnlyNumbers = false;
                }
            }
            noNegatives = true;
            if (rowHasOnlyNumbers) {// no non-numerical values detected
                for (int k = 0; k < tempI.size(); k++) {
                    // checks if the value, when parsed to a double, is negative
                    if (Double.parseDouble(tempI.get(k)) < 0) {
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
        }
        // go through every value for each predictor and add it to its respective total
        // squared deviation
        for (int i = 0; i < inputsWithOutliers[0].length; i++) {
            // finding means
            means[i] = totals[i] / inputsWithOutliers.length;
            for (int j = 0; j < inputsWithOutliers.length; j++) {
                totalDeviationsSquared[i] += Math.pow(inputsWithOutliers[j][i] - means[i], 2);// total squared deviation
            }
            // final standard deviations
            standardDeviations[i] = Math.sqrt(totalDeviationsSquared[i] / inputsWithOutliers.length);

        }
        // establishing upper and lower bounds and weeding outliers out
        for (int i = 0; i < upperBounds.length; i++) {
            upperBounds[i] = means[i] + (2 * standardDeviations[i]);// 2 SD from mean
            lowerBounds[i] = means[i] - (2 * standardDeviations[i]);// 2 SD from mean
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
        return outliersEliminated;
    }

    public standardisedPackager standardiseInputs(double[][] unstandardisedInputs) {
        // standardising inputs in range [0.1,0.9]

        // for final return
        double[][] inputsStandardised = new double[unstandardisedInputs.length][unstandardisedInputs[0].length];
        // initial minimum and maximum values, to be changed
        double[] mins = { 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000 };
        double[] maxes = { 0, 0, 0, 0, 0, 0, 0, 0 };
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
        // standardising inputs
        for (int j = 0; j < inputsStandardised.length; j++) {
            for (int i = 0; i < maxes.length; i++) {
                if (maxes[i] == mins[i]) {
                    inputsStandardised[j][i] = 0.9;
                } else {
                    inputsStandardised[j][i] = (0.8 * (((unstandardisedInputs[j][i] - mins[i]) / (maxes[i] - mins[i]))))
                            + 0.1;
                }
            }
        }
        return new standardisedPackager(inputsStandardised, mins, maxes);
    }

    public double[][] shuffleArray(double[][] inputArray) {
        // randomly shuffles the data so that seasonal affections do not occur
        Random rand = new Random(42);
        int randomIndextoSwap;
        double[] tempArray = new double[inputArray[0].length];
        for (int i = 0; i < inputArray.length; i++) {
            randomIndextoSwap = rand.nextInt(inputArray.length);
            tempArray = inputArray[randomIndextoSwap];
            inputArray[randomIndextoSwap] = inputArray[i];
            inputArray[i] = tempArray;
        }
        return inputArray;
    }

    public double[][] repositionOutputToEnd(double[][] inputArray, int targetColumn) {
        // the skelton value is in the middle of each row
        // much easier to deal with at the end 
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
        return repositionedArray;
    }

    public double[][] getOneDayAheadOutputInTheRawAsOutput(double[][] inputArray) {
        // changes the output value to that of the next day
        // essentially lags all predictors by 1 day
        double[][] outputRepositionedFromNextDayArray = new double[inputArray.length - 1][inputArray[0].length];
        for (int i = 0; i < inputArray.length - 1; i++) {
            outputRepositionedFromNextDayArray[i] = inputArray[i];
            outputRepositionedFromNextDayArray[i][inputArray[0].length - 1] = inputArray[i + 1][inputArray[0].length
                    - 1];
        }
        return outputRepositionedFromNextDayArray;
    }

    class standardisedPackager {// contains all data needed to (de)standardise a value
        double[][] inputsStandardised;
        double[] mins;
        double[] maxes;

        standardisedPackager(double[][] inputsStandardised, double[] mins, double[] maxes) {
            this.inputsStandardised = inputsStandardised;
            this.mins = mins;
            this.maxes = maxes;
        }
    }

    class dataSplitter {//returned by splitData() method
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
        // destandardises a value to the raw using its minimum and maximum values
        return ((((Si - 0.1) / 0.8) * (Max - Min)) + Min);
    }

    public dataSplitter splitData(double[][] inputArray, double percentTrainingSet, double percentvalidationSet,
            double percentTestSet) {
        // separates the data into training, validtion and testing sets
        // arguments are taken as multipliers to find percentages, e.g. 0.6 = 60%
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
        return new dataSplitter(trainingSet, validationSet, testSet);
    }
}
