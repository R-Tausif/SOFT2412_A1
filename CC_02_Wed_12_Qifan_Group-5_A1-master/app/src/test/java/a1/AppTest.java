/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package a1;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;
import java.util.*;
import java.io.*;

class AppTest {
    private App converter;
    private Update update;
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

        String config_path = "src/test/resources/config.json";
        String hist_path = "src/test/resources/history.json";
        Scanner scan = null;
        PrintWriter output3 = null;
        try {
            scan = new Scanner(new File(config_path));
            String json = scan.useDelimiter("\\Z").next();
            System.out.println(json);
            output3 = new PrintWriter(new File(hist_path));
            output3.printf(json);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        scan.close();
        output3.close();

        System.setIn(sysInBackup);
        System.setOut(sysOutBackup);
    }

    @Test
    public void readCsvReturnArrayListStringTest() throws Exception{

        String path = "src/test/resources/currencies.csv";

        // initialise expected.
        String currArr[] = {"AUD", "SGD", "USD", "GBP", "JPY", "CNY"};
        ArrayList<String> expectedLs = new ArrayList<>();

        for (String s : currArr) {
            expectedLs.add(s);
        }

        ArrayList<String> returnedLs = null;
        returnedLs = converter.readCsvReturnArrayListString(path);
        boolean correct = true;

        for (int i = 0; i < returnedLs.size(); i++) {

            String returned = returnedLs.get(i);
            String expected = expectedLs.get(i);
            if (!returned.equals(expected))
                correct = false;
        }

        assertTrue(correct, "readCsvReturnArrayListStringTest Fails");

    }

    @Test
    public void toDateStrTest() throws Exception{
        String date = converter.toDateStr();
        String dateLs[] = date.split("-");
        assertEquals(3, dateLs.length, "toDateStr fails, date not in the format YYYY-mm-dd.");
        assertEquals(4, dateLs[0].length(), "year is not 4 digits");
        assertEquals(2, dateLs[1].length(), "month is not 2 digits");
        assertEquals(2, dateLs[2].length(), "day is not 2 digits");
    }

    @Test
    public void addDateTest() throws Exception{
        String date = converter.toDateStr();
        String dateLs[] = date.split("-");
        int day = Integer.parseInt(dateLs[2]);

        converter.addDate();
        String date2 = converter.toDateStr();
        String dateLs2[] = date2.split("-");

        int day2 = Integer.parseInt(dateLs2[2]);
        int result = day2 - day;

        if (day2 == 0)
            assertEquals(-day, result, "addDate fails");
        else
            assertEquals(1, result, "addDate fails");
    }

    @Test
    public void subtractDateTest() throws Exception{
        String date = converter.toDateStr();
        String dateLs[] = date.split("-");
        int day = Integer.parseInt(dateLs[2]);

        converter.subtractDate();
        String date2 = converter.toDateStr();
        String dateLs2[] = date2.split("-");

        int day2 = Integer.parseInt(dateLs2[2]);
        int result = day2 - day;

        if (day2 == 0)
            assertEquals(-day, result, "subtractDate fails");
        else
            assertEquals(-1, result, "subtractDate fails");
    }

    @Test
    public void writeUpdateTest() throws Exception{
        // simulate updating the rates.
        converter.addDate();
        String date = converter.toDateStr();

        ArrayList<String> symbols = update.getSymbols();
        ArrayList<ArrayList<Double>> currMatrix = update.getCurrMatrix();

        update.updateCurrent(symbols.get(0), symbols.get(1), currMatrix.get(0).get(1) + 0.5);
        converter.writeUpdate(update);

        // Creates another converter that reads the history.json file
        // after it is updated.
        App converter2 = new App();
        Update u2 = converter.config("test");

        JSONArray jsonAll = u2.getJsonAll();
        // below is to get {date: {rates}}
        JSONObject dateAndRates = jsonAll.getJSONObject(jsonAll.length()-1);

        // below is to get [date]
        JSONArray dateInArray = dateAndRates.names();

        // to get date
        String latestDate = dateInArray.getString(0);

        assertEquals(date, latestDate, "writeUpdateTest fails");
    }

    @Test
    public void userUpdateTest() {
        String userInput = "nonExit\nuSer\nUpdate\nEXIT\n";
        ByteArrayOutputStream baos = this.writeInputOutput(userInput);
        converter.main(null);
        String[] lines = baos.toString().split("\n");

        assertEquals(lines[0].trim(), "Welcome to the Currency Converter!", "No welcome message");
        assertEquals(lines[2].trim(),"Please select the identity you want to login from below:",
                "Did not specify user account");
        assertEquals(lines[4].trim(), "Enter: Error: You inputted a non-existent mode.",
                "Error message for invalid command");
        assertEquals(lines[9].trim(), "Welcome USER!",
                "Welcome USER!");
        assertEquals(lines[12].trim(), "EXIT, CONVERT, DISPLAY, SUMMARY",
                "Need to display command options");
        assertEquals(lines[13].trim(), "Enter: Error: USER does not have the access for this command, "
                + "please select another command.",
                "Error: no access permission for user to UPDATE");
    }

    @Test
    public void adminTest() {
        String userInput = "nonExit\nadmin\nUpdate\nn\nNo\nNOPE\nNO\nNya\nNO\nEXIT\n";
        ByteArrayOutputStream baos = this.writeInputOutput(userInput);
        converter.main(null);
        String[] lines = baos.toString().split("\n");

        assertEquals(lines[4].trim(), "Enter: Error: You inputted a non-existent mode.",
                "Invalid command option");
        assertEquals(lines[9].trim(), "Welcome ADMIN!",
                "Welcome ADMIN");
        assertEquals(lines[12].trim(), "EXIT, CONVERT, DISPLAY, SUMMARY, UPDATE",
                "Need to display all five command options");
        assertEquals(lines[14].trim(), "You choose the UPDATE functionality",
                "Fail to display confirmation of UPDATE");
        assertEquals(lines[17].trim(), "Please try again! Please only enter YES or NO!",
                "Error: only yes or no response");
        assertEquals(lines[20].trim(), "Please try again! Please only enter YES or NO!",
                "Error: only yes or no response");
        assertEquals(lines[24].trim(), "Please try again! Please only enter YES or NO!",
                "Error: only yes or no response");
        assertEquals(lines[lines.length-2].trim(), "Thank you for using the system~",
                "Fail to display goodbye message");
    }

    @Test
    public void updateDisplayAdminTest() {
        String userInput = "admin\ninvalid\nupdate\nYES\nGBP\nAUD\n3.25\nNO\nNO\ndisplay\nEXIT\n";
        ByteArrayOutputStream baos = this.writeInputOutput(userInput);
        converter.askUser(converter, "test");
        String[] lines = baos.toString().split("\n");

        assertEquals(lines[8].trim(), "EXIT, CONVERT, DISPLAY, SUMMARY, UPDATE",
                "Need to display all five command options for admin");
        assertEquals(lines[9].trim(), "Enter: Error: You inputted a non-existent command",
                "Need to deal with invalid command option");
        assertEquals(lines[15].trim(), "Please update currencies for 2022-09-08",
                "Fail to specify the date to update");
        assertEquals(lines[19].trim(), "Input new currency rate from GBP, to AUD:",
                "Need to ask the new exchange rate");
        assertEquals(lines[24].trim(), "EXIT, CONVERT, DISPLAY, SUMMARY, UPDATE",
                "Command options should be displayed again after each function finishes");


        String actual = "";
        for (int i = 0; i < 9; i++) {
            actual = actual + lines[26+i];
        }
        String expected = "  F/T  |  SGD  |  CNY  |  GBP  |  AUD  " +
                "_______________________________________" +
                "  SGD  |1.00   |4.92   |0.62   |1.15   " +
                "_______________________________________" +
                "  CNY  |0.20   |1.00   |0.13   |0.21   " +
                "_______________________________________" +
                "  GBP  |1.62   |7.97   |1.00   |3.25(I)" +
                "_______________________________________" +
                "  AUD  |0.87   |4.69   |0.31(D)|1.00   ";
        assertEquals(expected, actual,
                "Displayed information not updated");
    }

    @Test
    public void updatePopularDisplayTest() {
        String userInput = "admin\nupdate\nNO\nNO\nYES\nWHY\nAUD\nAUD\nGBP\nSGD\nUSD\ndisplay\nEXIT\n";
        ByteArrayOutputStream baos = this.writeInputOutput(userInput);
        converter.askUser(converter, "test");
        String[] lines = baos.toString().split("\n");

        assertEquals(lines[12].trim(), "Do you want to update existing currency rates? (YES/NO):",
                "Fail to ask update existing");
        assertEquals(lines[13].trim(), "Do you want to add a new currency to the system? (YES/NO):",
                "Fail to ask update new currency");
        assertEquals(lines[14].trim(), "Current most popular currencies are: [SGD, CNY, GBP, AUD]",
                "Fail to display current popular currencies");
        assertEquals(lines[15].trim(), "Do you want to update the most popular currencies? (YES/NO):",
                "Fail to ask update popular currencies");
        assertEquals(lines[16].trim(), "Please input the first popular currency: " +
                "This currency symbol does not exist, please try again!",
                "Error message for invalid currency should be displayed");
        assertEquals(lines[19].trim(), "Please input the second popular currency: " +
                "This currency is already selected, please try again!",
                "Error message for already selected currency should be displayed");
        assertEquals(lines[22].trim(), "Please input the third popular currency:",
                "Fail to ask for the third currency");
        assertEquals(lines[23].trim(), "Please input the fourth popular currency:",
                "Fail to ask for the fourth currency");
        assertEquals(lines[24].trim(), "The new most popular currencies are [AUD, GBP, SGD, USD]",
                "Fail to confirm the newly selected popular currencies");
        assertEquals(lines[27].trim(), "EXIT, CONVERT, DISPLAY, SUMMARY, UPDATE",
                "Need to display all command option again");

        String actual = "";
        for (int i = 0; i < 9; i++) {
            actual = actual + lines[29+i];
        }

        String expected = "  F/T  |  AUD  |  GBP  |  SGD  |  USD  " +
                "_______________________________________" +
                "  AUD  |1.00   |0.59   |0.87(I)|0.68   " +
                "_______________________________________" +
                "  GBP  |1.70   |1.00   |1.62   |1.15   " +
                "_______________________________________" +
                "  SGD  |1.15(D)|0.62   |1.00   |0.71   " +
                "_______________________________________" +
                "  USD  |1.47   |0.87   |1.40   |1.00   ";
        assertEquals(expected, actual,
                "Display information not updated");
    }

    @Test
    public void updateConvertTest() {
        String userInput = "admin\nupdate\nno\nyes\nEUR\n1.5\n2.0\n3.0\n4.0\n5.0\n6.0\nNO\n" +
                "Convert\n100.0\nAUD\nEUR\nEXIT\n";
        ByteArrayOutputStream baos = this.writeInputOutput(userInput);
        converter.askUser(converter, "test");
        String[] lines = baos.toString().split("\n");

        assertEquals(lines[5].trim(), "Welcome ADMIN!");
        assertEquals(lines[8].trim(), "EXIT, CONVERT, DISPLAY, SUMMARY, UPDATE");
        assertEquals(lines[10].trim(), "You choose the UPDATE functionality");
        assertEquals(lines[14].trim(), "Input new currency symbol:");
        assertEquals(lines[15].trim(), "Input the exchange rate from EUR to AUD:");
        assertEquals(lines[20].trim(), "Input the exchange rate from EUR to CNY:");
        assertEquals(lines[24].trim(), "EXIT, CONVERT, DISPLAY, SUMMARY, UPDATE");
        assertEquals(lines[26].trim(), "You choose the CONVERT functionality");

        String[] commands = lines[27].trim().split(": ");
        assertEquals(commands[0], "Please enter the money amount");
        assertEquals(commands[1], "Please enter its currency symbol");
        assertEquals(commands[2], "Please enter the desired currency symbol you want to convert:");
        String[] money = lines[28].trim().split(": ");
        assertEquals(money[0], "The money amount after conversion is");
        assertEquals(money[1], "66.70");
    }

    @Test
    public void summaryTest() {
        String userInput = "user\nsummary\nEUR\nAUD\nAUD\nSGD\n20220904\n20220905\n2022-09-05\n2022-09-04\n" +
                "2022-09-04\n2022-09-08\nEXIT\n";
        ByteArrayOutputStream baos = this.writeInputOutput(userInput);
        converter.askUser(converter, "test");
        String[] lines = baos.toString().split("\n");

        assertEquals(lines[9].trim(), "Enter: " +
                "Please enter the two currencies you want the summaries of (from, to):",
                "Need to ask user for currencies in summary");
        assertEquals(lines[10].trim(), "The currency you entered doesn't exist. " +
                "Please enter some other currencies.",
                "Fail to display error message - invalid currency");
        assertEquals(lines[11].trim(), "Please enter the start and end dates (start, end):",
                "Need to ask user for query dates in summary");
        assertEquals(lines[12].trim(), "The date is invalid, its format should be in YYYY-MM-DD. " +
                "Please try again!",
                "Fail to display error message - invalid date format");
        assertEquals(lines[14].trim(), "First date needs to be earlier than the second date!",
                "Fail to display error message - date1 should be before date2");
        assertEquals(lines[16].trim(), "The rate history between those dates are:",
                "Need to display all the rates within the date range");
        assertEquals(lines[17].trim(), "2022-09-04: 0.96",
                "Fail to display rates correctly for 2022-09-04");
        assertEquals(lines[18].trim(), "2022-09-05: 0.96",
                "Fail to display rates correctly for 2022-09-05");
        assertEquals(lines[19].trim(), "2022-09-06: 0.76",
                "Fail to display rates correctly for 2022-09-06");
        assertEquals(lines[20].trim(), "2022-09-07: 0.87",
                "Fail to display rates correctly for 2022-09-07");

        assertEquals(lines[22].trim(), "The summary of AUD to SGD between 2022-09-04 and 2022-09-08 is:",
                "Need to print summary message");
        assertEquals(lines[23].trim(), "The maximum is: 0.96",
                "Fail to display maximum rate correctly");
        assertEquals(lines[24].trim(), "The median is: 0.915",
                "Fail to display median rate correctly");
        assertEquals(lines[25].trim(), "The average is: 0.8875",
                "Fail to display average rate correctly");
        assertEquals(lines[26].trim(), "The standard deviation is: 0.0823",
                "Fail to display standard deviation of the rates correctly");
        assertEquals(lines[27].trim(), "The minimum is: 0.76",
                "Fail to display minimum rate correctly");
    }

    @Test
    public void updateSummaryTest() {
        String userInput = "admin\nupdate\nno\nyes\nEUR\n1.5\n2.0\n3.0\n4.0\n5.0\n6.0\nno\n" +
                "summary\nEuR\nAUD\n2022-09-04\n2022-09-08\nyes\nsummary\nAUD\nEUR\n2022-09-04\n2022-09-08\nno\n";
        ByteArrayOutputStream baos = this.writeInputOutput(userInput);
        converter.askUser(converter, "test");
        String[] lines = baos.toString().split("\n");

        assertEquals(lines[11].trim(), "Please update currencies for 2022-09-08",
                "Need to confirm update date");
        assertEquals(lines[15].trim(), "Input the exchange rate from EUR to AUD:",
                "Need to ask rate from EUR to AUD");
        assertEquals(lines[20].trim(), "Input the exchange rate from EUR to CNY:",
                "Need to ask rate from EUR to CNY");
        assertEquals(lines[28].trim(), "2022-09-08: 1.5",
                "Fail to display the rate for 2022-09-08 correctly");
        assertEquals(lines[30].trim(), "The summary of EUR to AUD between 2022-09-04 and 2022-09-08 is:",
                "Need to print summary message");
        assertEquals(lines[31].trim(), "The maximum is: 1.5",
                "Fail to display maximum rate correctly");
        assertEquals(lines[32].trim(), "The median is: 1.5",
                "Fail to display median rate correctly");
        assertEquals(lines[33].trim(), "The average is: 1.5",
                "Fail to display average rate correctly");
        assertEquals(lines[34].trim(), "The standard deviation is: 0.0",
                "Fail to display standard deviation of the rates correctly");
        assertEquals(lines[35].trim(), "The minimum is: 1.5",
                "Fail to display minimum rate correctly");

        assertEquals(lines[44].trim(), "2022-09-08: 0.667",
                "Fail to display the rate for 2022-09-08 correctly");
        assertEquals(lines[46].trim(), "The summary of AUD to EUR between 2022-09-04 and 2022-09-08 is:",
                "Need to print summary message");
        assertEquals(lines[47].trim(), "The maximum is: 0.667",
                "Fail to display maximum rate correctly");
        assertEquals(lines[48].trim(), "The median is: 0.667",
                "Fail to display median rate correctly");
        assertEquals(lines[49].trim(), "The average is: 0.667",
                "Fail to display average rate correctly");
        assertEquals(lines[50].trim(), "The standard deviation is: 0.0",
                "Fail to display standard deviation of the rates correctly");
        assertEquals(lines[51].trim(), "The minimum is: 0.667",
                "Fail to display minimum rate correctly");
    }

    @Test
    public void updateNewConvertTest() {
        String userInput = "admin\nupdate\nno\nyes\nEUR\n1.5\n2.0\n3.0\n4.0\n5.0\n6.0\nno\n" +
                "convert\nabc\n1000.0\nABC\nEUR\nefg\nAUD\nEXIT\n";
        ByteArrayOutputStream baos = this.writeInputOutput(userInput);
        converter.askUser(converter, "test");
        String[] lines = baos.toString().split("\n");

        assertEquals(lines[26].trim(), "You choose the CONVERT functionality",
                "Need to display confirmation of choosing CONVERT");
        assertEquals(lines[27].trim(), "Please enter the money amount: Invalid input",
                "Fail to display error message - invalid money amount");
        assertEquals(lines[28].trim(), "Please enter the money amount: " +
                "Please enter its currency symbol: " +
                "No existing currency in the database",
                "Fail to display error message - invalid currency");
        assertEquals(lines[29].trim(), "Please enter its currency symbol: " +
                "Please enter the desired currency symbol you want to convert: " +
                "No existing currency in the database",
                "Fail to display error message - invalid desire currency");
        assertEquals(lines[30].trim(),"Please enter the desired currency symbol you want to convert:",
                "Need to ask again for the desired currency");
        assertEquals(lines[31].trim(), "The money amount after conversion is: 1500.00",
                "Fail to display converted money correctly");
    }
    public ByteArrayOutputStream writeInputOutput(String input) {
        ByteArrayInputStream bais = new ByteArrayInputStream(input.getBytes());
        System.setIn(bais);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(baos);
        System.setOut(printStream);
        return baos;
    }


}
