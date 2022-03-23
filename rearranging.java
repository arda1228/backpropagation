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
        System.out.println("originals: " + originals);
        return originals;
    }

    public double[][] casting(List<List<String>> inputs) {
        // make each list an array, cast the whole wider list or lists to an array, then
        // go through the arrays and convert all the values to doubles, weeding out
        // non-numerical values
        double[][] inputsAsDoubles={};
        List<String> tempI = inputs.get(0);
        double tempDouble;
        // inputsAsArray = inputs.toArray();
        for (int i = 0; i < inputs.size(); i++) {
            tempI = inputs.get(i);
            // System.out.println(tempI.get(6));
            for (int j = 0; j < tempI.size(); j++) {
                // System.out.println(tempJ);
                tempDouble = Double.valueOf(tempI.get(j));
                System.out.println(tempDouble);
                // inputsAsDoubles[i][j] = tempDouble;
            }
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
        wd.casting(wd.deleteDates());
    }
}
