import java.io.FileNotFoundException;
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
        System.out.println(inputs.size());
        boolean noExceptionOccured;
        int initialSize = inputs.size();
        for (int i = 0; i < initialSize; i++) {// iterating through inputs
            System.out.println("i = " + i);
            tempI = inputs.get(i);
            noExceptionOccured = true;
            while (noExceptionOccured) {
                for (int j = 0; j < tempI.size(); j++) {// iterating through an individual list
                    try {
                        tempDouble = Double.valueOf(tempI.get(j));// attempts to cast numerical value to type double
                    } catch (Exception e) {// if value non-numerical
                        inputs.remove(i);
                        noExceptionOccured = false;
                    }
                }
            }
        }
        System.out.println(inputs.size());
        System.out.println(inputs);

        double[][] inputsAsDoubles = new double[inputs.size()][inputs.get(0).size()];
        // allocates space in memory for the inputs, as a 2d array of type double
        for (int i = 0; i < inputs.size(); i++) {// iterating through inputs
            tempI = inputs.get(i);
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
    // finding non-numercials
    // casting into double
    // finding outliers
    // standardising inputs

    public static void main(String[] args) throws FileNotFoundException {
        rearranging wd = new rearranging();
        // wd.deleteDates();
        System.out.println(wd.castingToDouble(wd.deleteDates()));
    }
}
