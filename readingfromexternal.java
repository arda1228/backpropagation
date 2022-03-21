import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class readingfromexternal {

    public static void main(String[] args) throws FileNotFoundException {
        // TODO Auto-generated method stub

        ArrayList<Double> crakehill = getfromfile("crake");
        ArrayList<Double> skelton = getfromfile("skelt");
        ArrayList<Double> skipbridge = getfromfile("skip");
        ArrayList<Double> westwick = getfromfile("west");

    }

    public static ArrayList<Double> getfromfile(String x) throws FileNotFoundException {
        ArrayList<Double> localarr = new ArrayList<Double>();

        // File file = new File("C:\\Users\\boi\\Desktop\\uni stuff\\year 2\\sem 2\\AI
        // methods - COB107\\coursework\\datatoberead.txt");
        // C:\\Users\\boi\\Desktop\\uni stuff\\year 2\\sem 2\\AI methods -
        // COB107\\coursework\\crakehill.txt
        // C:\\Users\\boi\\Desktop\\uni stuff\\year 2\\sem 2\\AI methods -
        // COB107\\coursework\\skelton.txt
        // C:\\Users\\boi\\Desktop\\uni stuff\\year 2\\sem 2\\AI methods -
        // COB107\\coursework\\skipbridge.txt
        // C:\\Users\\boi\\Desktop\\uni stuff\\year 2\\sem 2\\AI methods -
        // COB107\\coursework\\westwick.txt
        String path = "C:\\Users\\boi\\Desktop\\uni stuff\\year 2\\sem 2\\AI methods - COB107\\coursework\\";

        if (x == "crake") {
            path += "crakehill.txt";

        } else if (x == "skelt") {

            path += "skelton.txt";
        } else if (x == "skip") {

            path += "skipbridge.txt";

        } else {
            path += "westwick.txt";

        }

        File file = new File(path);

        Scanner scan = new Scanner(file);

        while (scan.hasNextLine()) {

            System.out.println(scan.nextLine());

            localarr.add(Double.valueOf(scan.nextLine()));

        }

        return localarr;

    }

}
