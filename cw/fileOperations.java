import java.util.Scanner; // Import the Scanner class
import java.io.File;
import java.util.ArrayList; // import the ArrayList class
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

class fileOperations {

    private static List<String> getRecordFromLine(String line) {
        String COMMA_DELIMITER = ",";
        List<String> values = new ArrayList<String>();
        try (Scanner rowScanner = new Scanner(line)) {
            rowScanner.useDelimiter(COMMA_DELIMITER);
            while (rowScanner.hasNext()) {
                values.add(rowScanner.next());
            }
        }
        return values;
    }

    public List<List<String>> getValues(String filename) throws FileNotFoundException {
        List<List<String>> records = new ArrayList<>();
        try (Scanner scanner = new Scanner(new File(filename));) {
            while (scanner.hasNextLine()) {
                records.add(getRecordFromLine(scanner.nextLine()));
            }
        }
        return records;
    }

    public static void createFile(String fileName) {
        try {
            File myObj = new File("./reportfiles/" + fileName);
            if (myObj.createNewFile()) {
                System.out.println("File created: " + myObj.getName());
            } else {
                System.out.println("File already exists.");
            }
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    public static void writeArrayToFileAsLines(String[] inputArray, String fileName) {
        try {
            FileWriter myWriter = new FileWriter("./reportfiles/" + fileName);
            for (int i = 0; i < inputArray.length; i++) {
                myWriter.write((inputArray[i]));
                if (i != inputArray.length - 1) {
                    myWriter.write("\n");
                }
            }
            myWriter.close();
            System.out.println("Successfully wrote to the file.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    public String[] convertArrayToStringArray(double[] inputArray) {
        String[] stringArray = new String[inputArray.length];
        for (int i = 0; i < inputArray.length; i++) {
            stringArray[i] = Double.toString(inputArray[i]);
        }
        return stringArray;
    }

    public String[] mergeTwoArraysAsIsAndReturnAsStringArray(double[] inputArray1, double[] inputArray2) {
        String[] mergedArray = new String[inputArray1.length];
        for (int i = 0; i < inputArray1.length; i++) {
            mergedArray[i] = Double.toString(inputArray1[i]) + "," + Double.toString(inputArray2[i]);
        }
        return mergedArray;
    }

    public String createUniqueIdentifier() {
        Date date = Calendar.getInstance().getTime();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-hh-mm-ss");
        String strDate = dateFormat.format(date);
        System.out.println("Converted String: " + strDate);
        return strDate;
    }

    public static void main(String[] args) throws FileNotFoundException {
    }
}