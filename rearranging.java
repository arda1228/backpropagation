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
                    for (int k = 0; k < tempI.size(); k++){
                        if (Double.parseDouble(tempI.get(k)) < 0){
                            noNegatives = false;
                        }
                    }
                    if (noNegatives){
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
    public double[][] eliminateOutliers (double[][] inputsWithOutliers){
        // finding outliers
        double[][] outliersEliminated = new double[inputsWithOutliers.length][inputsWithOutliers[0].length];
        return outliersEliminated;
    }
    public double[][] standardiseInputs (double[][] unstandardisedInputs){
        // standardising inputs
        double[][] inputsStandardised = new double[unstandardisedInputs.length][unstandardisedInputs[0].length];
        return inputsStandardised;
    }

    public static void main(String[] args) throws FileNotFoundException {
        rearranging trial = new rearranging();
        List<List<String>> deleteddates = trial.deleteDates();
        double[][] cast = trial.castingToDouble(deleteddates);
        System.out.println(cast);
    }
}
