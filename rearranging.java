import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class rearranging {

    public List<List<String>> deleteDates() throws FileNotFoundException {
        readingfromexternal test = new readingfromexternal();
        List<List<String>> originals = test.getValues();
        originals.remove(0);
        // System.out.println("originals: " + originals);
        for (List list : originals) {
            list.remove(0);

        }
        // System.out.println("deleted dates: " + originals);
        return originals;
    }

    public double[][] castingToDouble(List<List<String>> inputs) {
        // make each list an array, cast the whole wider list or lists to an array, then
        // go through the arrays and convert all the values to doubles, weeding out
        // non-numerical values
        List<String> tempI;// declare a variable to temporarily hold each list within inputs
        double tempDouble;// declare a variable to temporarily hold each value within a list in inputs
        boolean rowValid;
        boolean noNegatives;
        System.out.println(inputs.size());
        int initialSize = inputs.size();
        List<List<String>> inputsNumerical = new ArrayList<>();
        for (int i = 0; i < initialSize; i++) {// iterating through inputs
            rowValid = true;
            tempI = inputs.get(i);
            for (int j = 0; j < tempI.size(); j++) {// iterating through an individual list
                try {
                    tempDouble = Double.parseDouble(tempI.get(j));// attempts to cast numerical value to type double
                } catch (Exception e) {// if value non-numerical
                    rowValid = false;
                }
            }
            noNegatives = true;
            if (rowValid) {
                for (int k = 0; k < tempI.size(); k++) {
                    if (Double.parseDouble(tempI.get(k)) < 0) {
                        noNegatives = false;
                    }
                }
                if (noNegatives) {
                    inputsNumerical.add(tempI);
                }
            }
        }
        System.out.println(inputsNumerical.size());
        System.out.println(inputsNumerical);

        double[][] inputsAsDoubles = new double[inputsNumerical.size()][inputsNumerical.get(0).size()];
        // allocates space in memory for the inputs, as a 2d array of type double
        for (int i = 0; i < inputsNumerical.size(); i++) {// iterating through inputs
            tempI = inputsNumerical.get(i);
            for (int j = 0; j < tempI.size(); j++) {// iterating through an individual list
                tempDouble = Double.valueOf(tempI.get(j));// attempts to cast numerical value to type double
                inputsAsDoubles[i][j] = tempDouble;// assigns to index
            }
        }
        for (double[] i : inputsAsDoubles) {
            for (double j : i)
                System.out.println(j);
        }
        return inputsAsDoubles;
    }

    public double[][] eliminateOutliers(double[][] inputsWithOutliers) {
        // finding outliers
        double[] totals = new double[inputsWithOutliers[0].length];
        double[] means = new double[inputsWithOutliers[0].length];
        double[] standardDeviations = new double[inputsWithOutliers[0].length];
        double[] totalDeviationsSquared = new double[inputsWithOutliers[0].length];
        double[] upperBounds = new double[means.length];
        double[] lowerBounds = new double[means.length];
        double[][] outliersEliminated = new double[inputsWithOutliers.length][inputsWithOutliers[0].length];
        for (int i = 0; i < inputsWithOutliers[0].length; i++) {
            for (int j = 0; j < inputsWithOutliers.length; j++) {
                totals[i] += inputsWithOutliers[j][i];
            }
            System.out.println("totals " + i + ": " + totals[i]);
        }
        for (int i = 0; i < inputsWithOutliers[0].length; i++) {
            means[i] = totals[i] / inputsWithOutliers.length;
            for (int j = 0; j < inputsWithOutliers.length; j++) {
                totalDeviationsSquared[i] += Math.pow(inputsWithOutliers[j][i] - means[i], 2);
                // top, sqr
            }
            standardDeviations[i] = Math.sqrt(totalDeviationsSquared[i] / inputsWithOutliers.length);
            System.out.println("standard deviations " + i + ": " + standardDeviations[i]);
        }
        // establishing upper and lower bounds and weeding outliers out

        // the values in averages are the totals, now find averages by dividing each
        // value by the amount of inputs
        return outliersEliminated;
    }

    public double[][] standardiseInputs(double[][] unstandardisedInputs) {
        double[][] inputsStandardised = new double[unstandardisedInputs.length][unstandardisedInputs[0].length];
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
