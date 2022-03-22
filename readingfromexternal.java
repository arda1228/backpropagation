import java.util.Scanner; // Import the Scanner class
import java.io.File;
import java.util.ArrayList; // import the ArrayList class
import java.util.List;
import java.io.FileNotFoundException;

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
        try (Scanner scanner = new Scanner(new File("Ouse93-96test.csv"));) {
            while (scanner.hasNextLine()) {
                records.add(getRecordFromLine(scanner.nextLine()));
            }
        }
        return records;
    }

    public static void main(String[] args) throws FileNotFoundException {
        readingfromexternal test = new readingfromexternal();
        System.out.println("records: " + test.getValues());
    }
}