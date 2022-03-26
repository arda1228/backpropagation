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

class readingfromexternal {

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

    public List<List<String>> getValues() throws FileNotFoundException {
        List<List<String>> records = new ArrayList<>();
        try (Scanner scanner = new Scanner(new File("arda.csv"));) {
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

    public static void writeArrayToFileAsLines(double[] inputArray, String fileName) {
        try {
            FileWriter myWriter = new FileWriter(fileName);
            for (int i = 0; i < inputArray.length; i++) {
                myWriter.write(Double.toString(inputArray[i]));
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

    public String createUniqueIdentifier() {
        Date date = Calendar.getInstance().getTime();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-hh-mm-ss");
        String strDate = dateFormat.format(date);
        System.out.println("Converted String: " + strDate);
        return strDate;
    }

    public static void main(String[] args) throws FileNotFoundException {
        readingfromexternal test = new readingfromexternal();
        // System.out.println("records: " + test.getValues());
        String uniqueId = test.createUniqueIdentifier();
        test.createFile(uniqueId);
        String fileName = "./reportfiles/" + uniqueId;
        double[] inputArray = { 1, 2, 3, 4, 5, 6, 7, 1, 2, 3, 4, 5.00008, 6, 7, 1, 2, 3, 4, 5, 6, 7, 1, 2, 3, 4, 5, 6, 7, 1,
                2, 3, 4, 5, 6, 7 };
        test.writeArrayToFileAsLines(inputArray, fileName);
    }
}