
package a1;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.*;

import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
import java.io.*;

public class UpdateTest {
    private ArrayList<ArrayList<Double>> currMatrix;
    private ArrayList<String> symbols;
    private JSONObject jsonCurr;
    private JSONArray jsonAll;
    private Update update;
    private App converter;
    private InputStream sysInBackup;
    private PrintStream sysOutBackup;

    @BeforeEach
    public void setUp() {
        converter = new App();
        update = converter.config("test");

        sysInBackup = System.in;
        sysOutBackup = System.out;
    }

    @AfterEach
    public void cleanWork() {
        String currencies = "AUD,SGD,USD,GBP,JPY,CNY";
        String popular = "SGD,CNY,GBP,AUD";
        String curr_path = "src/test/resources/currencies.csv";
        String pop_path = "src/test/resources/popular.csv";

        // Rewrite the currencies.csv and popular.csv back to original state
        PrintWriter output = null;
        PrintWriter output2 = null;
        try {
            output = new PrintWriter(new File(curr_path));
            output.printf(currencies);
            output2 = new PrintWriter(new File(pop_path));
            output2.printf(popular);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        output.close();
        output2.close();

        // Rewrite the config.json and history.json back to their original state
        String config_path = "src/test/resources/config.json";
        String hist_path = "src/test/resources/history.json";
        Scanner scan = null;
        PrintWriter output3 = null;

        try {
            scan = new Scanner(new File(config_path));
            // Read input until the end of the input
            String json = scan.useDelimiter("\\Z").next();
            output3 = new PrintWriter(new File(hist_path));
            output3.printf(json);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        scan.close();
        output3.close();

        // Set the console back to normal
        System.setIn(sysInBackup);
        System.setOut(sysOutBackup);
    }

    @Test
    public void testToJson() {
        update.toJson("2022-09-07");
        JSONObject obj = update.getUpdatedCurr();
        String recorded = obj.toString();
        String path = "src/test/resources/answer.json";
        Scanner scan = null;
        String answer = null;

        try {
            scan = new Scanner(new File(path));
            answer = scan.useDelimiter("\\Z").next();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        // Take out all the white spaces and new lines
        answer = answer.replaceAll("[\r\n\\s]+", "");
        assertEquals(recorded, answer, "Json String is not correctly configured");
    }

    @Test
    public void testGetSymbol() {
        ArrayList<String> symbols = update.getSymbols();
        String path = "src/test/resources/currencies.csv";
        ArrayList<String> ans = converter.readCsvReturnArrayListString(String.format(path));
        assertEquals(symbols, ans, "currency symbols are not correctly stored");
    }

    @Test
    public void testAddSymbol() {
        update.addSymbols("EUR");
        ArrayList<String> symbols = update.getSymbols();
        assertEquals(symbols.size(), 7, "new currency symbol is not added");
    }

    @Test
    public void testSetCurrMatrix() {
        update.setCurrMatrix(1, 2, 5.15);
        ArrayList<ArrayList<Double>> matrix = update.getCurrMatrix();
        ArrayList<Double> y = matrix.get(1);
        assertEquals(y.get(2), 5.15, "new currency rate is not set in currMatrix");
    }


    @Test
    public void testUpdateCurrent() {
        update.updateCurrent("AUD", "USD", 1.2563);
        ArrayList<ArrayList<Double>> matrix = update.getCurrMatrix();
        ArrayList<Double> fromY = matrix.get(0);
        ArrayList<Double> toY = matrix.get(2);
        assertEquals(fromY.get(2), 1.256);
        assertEquals(toY.get(0), 0.796, "The currency rate and its inverse needs to be set");
    }

    @Test
    public void testUpdateNew() {
        Double[] newCurr = {1.8, 1.9, 2.0, 2.1, 2.2, 2.3};
        ArrayList<Double> currList = new ArrayList<>(Arrays.asList(newCurr));
        update.updateNew("TEST", currList);
        ArrayList<ArrayList<Double>> matrix = update.getCurrMatrix();
        assertEquals(matrix.size(), 7, "new currency is not added to the matrix");
    }

    @Test
    public void testWriteSymbols() {
        update.addSymbols("TEST");
        update.writeSymbols();
        String path = "src/test/resources/currencies.csv";
        Scanner scan = null;
        String ans = "AUD,SGD,USD,GBP,JPY,CNY,TEST";
        String recorded = null;
        try {
            scan = new Scanner(new File(path));
            recorded = scan.nextLine();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        assertEquals(recorded, ans, "Symbols are not written to currencies.csv");
    }

    @Test
    public void testWriteJson() {
        Double[] newCurr = {1.8, 1.9, 2.0, 2.1, 2.2, 2.3};
        ArrayList<Double> currList = new ArrayList<>(Arrays.asList(newCurr));
        update.updateNew("TEST", currList);
        update.toJson("2022-09-09");
        update.writeJson();

        Scanner scan = null;
        String path = "src/test/resources/history.json";
        App newConverter = new App();
        Update update2 = newConverter.config("test");
        JSONArray updatedJson = update2.getJsonAll();
        JSONObject updatedCurr = converter.part1("test");

        assertEquals(updatedJson.length(), 6, "There should be 6 days of entries in history.json");
        assertEquals(updatedCurr.length(), 7, "The most recent record should include 7 currencies");
    }


    @Test
    public void testWriteMostPopular() {
        String[] popCurr = {"ABC", "DEF", "GHI", "JKL"};
        ArrayList<String> mostPopular = new ArrayList<>();
        for (int i = 0; i < popCurr.length; i++) {
            mostPopular.add(popCurr[i]);
        }
        update.writeMostPopular(mostPopular);

        String path = "src/test/resources/popular.csv";

        Scanner scan = null;
        String ans = null;
        try{
            scan = new Scanner(new File(path));
            ans = scan.nextLine();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        String test = "ABC,DEF,GHI,JKL";
        String msg = "The new most popular currencies are not correctly written to popular.csv";
        assertEquals(test, ans.trim(), msg);
    }

    @Test
    public void testUpdateExistingNormal() {
        String userInput = "SgD\nuSd\n5.15\n";
        this.writeInputOutput(userInput);

        update.updateExisting(new Scanner(System.in));
        ArrayList<ArrayList<Double>> matrix = update.getCurrMatrix();
        ArrayList<Double> y = matrix.get(1);
        ArrayList<Double> y2 = matrix.get(2);
        assertEquals(y.get(2), 5.15, "SGD to USD should be 5.15");
        assertEquals(y2.get(1), 0.194, "USD to SGD should be 0.194");
    }

    @Test
    public void testUpdatedExistingWrongFrom() {
        String userInput = "EUR\nYNG\nSgd\nuSd\n3.155\n";
        ByteArrayOutputStream baos = this.writeInputOutput(userInput);

        update.updateExisting(new Scanner(System.in));

        String[] lines = baos.toString().split("\n");

        ArrayList<ArrayList<Double>> matrix = update.getCurrMatrix();
        ArrayList<Double> y = matrix.get(1);
        ArrayList<Double> y2 = matrix.get(2);
        assertEquals(y.get(2), 3.155, "SGD to USD should be 3.155");
        assertEquals(y2.get(1), 0.317, "USD to SGD should be 0.317");
        String msg = "Need to inform user to only select from existing currencies";
        assertEquals(lines[2].trim(), "Please only select from below:", msg);
    }

    @Test
    public void testUpdatedExistingWrongTo() {
        String userInput = "Sgd\nSGD\neur\nuSd\n4.56\n";
        ByteArrayOutputStream baos = this.writeInputOutput(userInput);

        update.updateExisting(new Scanner(System.in));

        String[] lines = baos.toString().split("\n");

        ArrayList<ArrayList<Double>> matrix = update.getCurrMatrix();
        ArrayList<Double> y = matrix.get(1);
        ArrayList<Double> y2 = matrix.get(2);

        assertEquals(y.get(2), 4.56,
                "SGD to USD should be 4.56");
        assertEquals(y2.get(1), 0.219,
                "USD to SGD should be 0.219");
        String msg = "Need to inform user to that the specified currency already chosen";
        assertEquals(lines[3].trim(),
                "Please enter another currency from below:",
                msg);
        String msg2 = "Need to output the existing currencies besides from already chosen SGD";
        assertEquals(lines[4].trim(),
                "[AUD, USD, GBP, JPY, CNY]",
                msg2);
        String msg3 = "Need to inform user to only select from existing currencies";
        assertEquals(lines[6].trim(),
                "Please enter another currency from below:",
                msg3);
    }

    @Test
    public void testUpdatedExistingCurrTooSmall() {
        String userInput = "Sgd\nuSd\n0.00001\n-1.2445\n1.245\n";
        ByteArrayOutputStream baos = this.writeInputOutput(userInput);

        update.updateExisting(new Scanner(System.in));

        String[] lines = baos.toString().split("\n");

        ArrayList<ArrayList<Double>> matrix = update.getCurrMatrix();
        ArrayList<Double> y = matrix.get(1);
        ArrayList<Double> y2 = matrix.get(2);
        assertEquals(y.get(2), 1.245,
                "SGD to USD should be 1.245");
        assertEquals(y2.get(1), 0.803,
                "USD to SGD should be 0.803");

        assertEquals(lines[1].trim(),
                "Input currency to update from:",
                "ask for from currency");
        assertEquals(lines[2].trim(),
                "Input currency to update to:",
                "ask for to currency");
        assertEquals(lines[3].trim(),
                "Input new currency rate from SGD, to USD:",
                "ask for rate");
        assertEquals(lines[4].trim(),
                "This currency rate is too small, please try again",
                "rate need to be larger than 0.0001");
    }

    @Test
    public void testUpdatedNewNormal() {
        String userInput = "EuR\n1.0\n2.0\n3.0\n4.0\n5.0\n6.0\n";
        ByteArrayOutputStream baos = this.writeInputOutput(userInput);

        update.updateNewCurrency(new Scanner(System.in));

        String[] lines = baos.toString().split("\n");

        ArrayList<ArrayList<Double>> matrix = update.getCurrMatrix();

        assertEquals(matrix.size(), 7,
                "new currency needs to be added to the matrix");
        assertEquals(matrix.get(6).size(), 7,
                "new currency needs to be added");
        assertEquals(matrix.get(6).get(2), 3.0,
                "EUR to USD should be 3.0");
        assertEquals(matrix.get(2).get(6), 0.333,
                "USD to EUR should be 0.333");

        assertEquals(lines[1].trim(),
                "Input new currency symbol:",
                "Need to ask user for entering the currency");
        assertEquals(lines[2].trim(),
                "Input the exchange rate from EUR to AUD:",
                "Need to ask user for the new exchange rate");
    }

    @Test
    public void testUpdatedNewCurrAlreadyExists() {
        String userInput = "Aud\nEURN\nEUR\n1.0\n2.0\n3.0\n4.0\n5.0\n6.0\n";
        ByteArrayOutputStream baos = this.writeInputOutput(userInput);

        update.updateNewCurrency(new Scanner(System.in));

        String[] lines = baos.toString().split("\n");

        ArrayList<ArrayList<Double>> matrix = update.getCurrMatrix();

        assertEquals(matrix.get(6).get(3), 4.0,
                "EUR to GBP should be 4.0");
        assertEquals(matrix.get(3).get(6), 0.25,
                "GBP to EUR should be 0.25");

        assertEquals(lines[2].trim(),
                "The following symbols already exists, please try again!",
                "Need to inform user that symbol already exists");
        assertEquals(lines[5].trim(),
                "The currency symbol can only contain 3 characters, please try again!",
                "The symbol should be only 3 characters long");
    }

    @Test
    public void testUpdatedNewCurrTooSmall() {
        String userInput = "EUR\n0.00001\n-1.024\n1.5\n2.0\n3.0\n4.0\n5.0\n6.0\n";
        ByteArrayOutputStream baos = this.writeInputOutput(userInput);

        update.updateNewCurrency(new Scanner(System.in));

        String[] lines = baos.toString().split("\n");

        ArrayList<ArrayList<Double>> matrix = update.getCurrMatrix();

        assertEquals(matrix.get(6).get(0), 1.5,
                "EUR to AUD should be 1.5");
        assertEquals(matrix.get(0).get(6), 0.667,
                "AUD to EUR should be 0.667");
        assertEquals(lines[3].trim(),
                "This currency rate is too small, please try again!",
                "Currency rate should not be less than 0.0001");
    }

    @Test
    public void testUpdateMostPopularNormal() {
        String userInput = "AuD\nsGd\nusD\nGbp\n";
        ByteArrayOutputStream baos = this.writeInputOutput(userInput);

        update.updateMostPopular(new Scanner(System.in));

        String[] lines = baos.toString().split("\n");

        ArrayList<String> popular = update.getMostPopular();
        ArrayList<String> answer = new ArrayList<>();
        answer.add("AUD");
        answer.add("SGD");
        answer.add("USD");
        answer.add("GBP");

        assertEquals(answer, popular,
                "New most popular currencies are not recorded");
        assertEquals(lines.length, 5,
                "Need to ask user for four different currencies");
        assertEquals(lines[1].trim(),
                "Please input the first popular currency:",
                "Need to ask user for the specific new popular currency");
    }

    @Test
    public void testUpdateMostPopularCurrRepeated() {
        String userInput = "AUD\nauD\nEUR\nSgd\nUSD\nGBP\n";
        ByteArrayOutputStream baos = this.writeInputOutput(userInput);

        update.updateMostPopular(new Scanner(System.in));

        String[] lines = baos.toString().split("\n");

        ArrayList<String> popular = update.getMostPopular();
        ArrayList<String> answer = new ArrayList<>();
        answer.add("AUD");
        answer.add("SGD");
        answer.add("USD");
        answer.add("GBP");

        assertEquals(answer, popular);
        assertEquals(lines.length, 9);
        String[] s1 = lines[2].trim().split(": ");
        String[] s2 = lines[4].trim().split(": ");

        assertEquals(s1[1],
                "This currency is already selected, please try again!",
                "Same currency cannot be added to most popular");
        assertEquals(s2[1],
                "This currency symbol does not exist, please try again!",
                "Non-existing currency in the database cannot be added to most popular");
    }

    /*
    * Helper method to get the input string and feed it into the input stream
    * and return the output string
    */
    public ByteArrayOutputStream writeInputOutput(String input) {
        ByteArrayInputStream bais = new ByteArrayInputStream(input.getBytes());
        System.setIn(bais);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(baos);
        System.setOut(printStream);
        return baos;
    }

}

