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
    //finding non-numercials, outliers, etc.
    //casting into double
    //standardising inputs

    public static void main(String[] args) throws FileNotFoundException {
        rearranging wd = new rearranging();
        wd.deleteDates();
    }
}
