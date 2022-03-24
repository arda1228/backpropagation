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
        double[] averages = new double[inputsWithOutliers[0].length];
        double[][] outliersEliminated = new double[inputsWithOutliers.length][inputsWithOutliers[0].length];
        for (int j = 0; j < inputsWithOutliers.length; j++) {
            for (int i = 0; i < inputsWithOutliers.length; i++) {
                averages[j] += inputsWithOutliers[i][j];
            }
        }
        // the values in averages are the totals, now find averages by dividing each
        // value by the amount of inputs
        for (int i = 0; i < averages.length; i++) {
            averages[i] = averages[i] / inputsWithOutliers.length;
        }

        return outliersEliminated;
    }

    public double[][] standardiseInputs(double[][] unstandardisedInputs) {
        double[][] inputsStandardised = new double[unstandardisedInputs.length][unstandardisedInputs[0].length];
        //FIX
        double[] mins = {1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000};
        double[] maxes = {0, 0, 0, 0, 0, 0, 0, 0};
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
            System.out.println("min value" + i +": " + mins[i]);
            System.out.println("max value" + i +": " + maxes[i]);
        }
            // standardising inputs
        for (int j = 0; j < inputsStandardised.length; j++) {
            for (int i = 0; i < maxes.length; i++) {
                //FIX
                inputsStandardised[i][j] = (0.8 * ((unstandardisedInputs[i][i] - mins[j] / maxes[j] - mins[j]))) + 0.1;
                System.out.println("standardised value: " + inputsStandardised[i][j]);
            }
        }
        // for (double[] i : inputsStandardised) {
        //     for (double j : i)
        //         System.out.println(j);
        // }        
        return inputsStandardised;
    }

    public static void main(String[] args) throws FileNotFoundException {
        rearranging trial = new rearranging();
        List<List<String>> deleteddates = trial.deleteDates();
        double[][] cast = trial.castingToDouble(deleteddates);
        trial.standardiseInputs(cast);
        // System.out.println(standardised);
    }
}
