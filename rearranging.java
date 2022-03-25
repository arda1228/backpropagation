import java.io.FileNotFoundException;//catches error if no file found
import java.util.ArrayList;//adaptable-length array
import java.util.List;

public class rearranging {

    public List<List<String>> deleteDates() throws FileNotFoundException {// function to delete the dates at the
                                                                          // beginning of every row of inputs
        // argument is taken in form List<List<String>> as this is how it is read from
        // the .csv file
        readingfromexternal test = new readingfromexternal();
        List<List<String>> originals = test.getValues();// reads values from file into a list
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
        double[][] outliersEliminated = new double[inputsWithOutliers.length][inputsWithOutliers[0].length];
        //go through every value for each predictor and add it to its respective total
        for (int i = 0; i < inputsWithOutliers[0].length; i++) {
            for (int j = 0; j < inputsWithOutliers.length; j++) {
                totals[i] += inputsWithOutliers[j][i];
            }
            System.out.println("totals " + i + ": " + totals[i]);
        }
        //go through every value for each predictor and add it to its respective total squared deviation
        for (int i = 0; i < inputsWithOutliers[0].length; i++) {
            //finding means 
            means[i] = totals[i] / inputsWithOutliers.length;
            for (int j = 0; j < inputsWithOutliers.length; j++) {
                totalDeviationsSquared[i] += Math.pow(inputsWithOutliers[j][i] - means[i], 2);//total squared deviation
            }
            standardDeviations[i] = Math.sqrt(totalDeviationsSquared[i] / inputsWithOutliers.length);// final standard deviations
            System.out.println("standard deviations " + i + ": " + standardDeviations[i]);
        }
        // establishing upper and lower bounds and weeding outliers out
        for (int i = 0; i < upperBounds.length; i++) {
            upperBounds[i] = means[i] + (2 * standardDeviations[i]);//2 SD from mean
            System.out.println("upper bound " + i + ": " + upperBounds[i]);
            lowerBounds[i] = means[i] - (2 * standardDeviations[i]);//2 SD from mean
            System.out.println("lower bound " + i + ": " + lowerBounds[i]);
        }
        // eliminating outliers
        for (int i = 0; i < inputsWithOutliers.length; i++) {
            validRow = true;
            for (int j = 0; j < inputsWithOutliers[0].length; j++) {
                //checking if there are any outliers in each row
                if (inputsWithOutliers[i][j] > upperBounds[j] || inputsWithOutliers[i][j] < lowerBounds[j]) {
                    validRow = false;
                }
            }
            if (validRow) {
                outliersEliminated[i] = inputsWithOutliers[i];
            }
        }
        for (double[] i : outliersEliminated) {
            for (double j : i)
                System.out.println(j);
        }
        return outliersEliminated;
    }

    public double[][] standardiseInputs(double[][] unstandardisedInputs) {//standardising inputs in range [0.1,0.9]
        double[][] inputsStandardised = new double[unstandardisedInputs.length][unstandardisedInputs[0].length];// for final return
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
                System.out.println("standardised value: " + inputsStandardised[j][i]);
            }
        }
        return inputsStandardised;
    }

    public static void main(String[] args) throws FileNotFoundException {
        rearranging trial = new rearranging();
        List<List<String>> deleteddates = trial.deleteDates();
        double[][] cast = trial.castingToDouble(deleteddates);
        trial.eliminateOutliers(cast);
        // trial.standardiseInputs(sd);
    }
}
