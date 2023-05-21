/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package a1;

import org.junit.jupiter.api.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

class SummaryTest {
    ArrayList<Double> currencyRates = new ArrayList<Double>();
    Summary s1 = null;

    @BeforeEach
    public void setUp() {
        s1 = new Summary();
    }


    @Test
    void maxCheck() {
        currencyRates.add(4.9);
        currencyRates.add(0.9);
        currencyRates.add(65.0);
        currencyRates.add(23.0);
        assertEquals(s1.calculateMax(currencyRates), 65, "Fail maxCheck");

    }

    @Test
    void minCheck() {
        currencyRates.add(4.9);
        currencyRates.add(0.9);
        currencyRates.add(65.0);
        currencyRates.add(23.0);
        assertEquals(s1.calculateMin(currencyRates), 0.9, "Fail minCheck");

    }


    @Test
    void avgCheck() {
        currencyRates.add(4.9);
        currencyRates.add(0.9);
        currencyRates.add(65.0);
        currencyRates.add(23.0);
        assertEquals(s1.calculateAvg(currencyRates), 23.45, "Fail avgCheck");

    }

    @Test
    void medianCheckOddLength() {
        currencyRates.add(4.9);
        currencyRates.add(0.9);
        currencyRates.add(65.0);
        currencyRates.add(26.0);
        currencyRates.add(108.5);
        assertEquals(s1.calculateMedian(currencyRates), 26, "Fail medianCheckOddLength");
    }


    @Test
    void medianCheckEvenLength() {
        currencyRates.add(4.9);
        currencyRates.add(0.9);
        currencyRates.add(48.0);
        currencyRates.add(65.0);
        currencyRates.add(26.0);
        currencyRates.add(108.5);
        assertEquals(s1.calculateMedian(currencyRates), 37, "Fail medianCheckEvenLength");

    }

    @Test
    void sdCheck() {
        currencyRates.add(19.4);
        currencyRates.add(19.4);
        assertEquals(s1.calculateSD(currencyRates), 0, "Fail sdCheck");
    }

    @Test
    void dateWithinRangeCheck() throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        Date date1 = formatter.parse("2022-09-03");
        Date date2 = formatter.parse("2022-09-03");
        Date date3 = formatter.parse("2022-09-05");
        Date date4 = formatter.parse("2022-08-01");
        Date date5 = formatter.parse("2022-10-01");

        boolean result = s1.isWithinRange(date1,date2,date3);
        boolean result2 = s1.isWithinRange(date4, date2, date3);
        boolean result3 = s1.isWithinRange(date3, date2, date3);
        boolean result4 = s1.isWithinRange(date5, date1, date3);
        assertTrue(result, "2022-09-03 is within range");
        assertFalse(result2, "2022-08-01 is not within range");
        assertTrue(result3, "2022-09-05 is within range");
        assertFalse(result4, "2022-10-01 is not within range");
    }

    @Test
    public void validDateTest() {
        String validDate = "2022-03-01";
        assertTrue(s1.isValidDate(validDate),
                "Correctly formatted valid date");
    }

    @Test
    public void invalidDateLengthTest() {
        String invalidYearLength = "202213-03-02";
        String invalidMonthLength = "2022-0003-02";
        String invalidDayLength = "2022-03-0002";
        assertFalse(s1.isValidDate(invalidYearLength),
                "Invalid date - year too long");
        assertFalse(s1.isValidDate(invalidMonthLength),
                "Invalid date - month too long");
        assertFalse(s1.isValidDate(invalidDayLength),
                "Invalid date - day too long");
    }

    @Test
    public void invalidDateOutOfFormatTest() {
        String tooLongDate = "201232-01232-012321";
        String illFormatted = "20220301";
        String zeroDate = "0000-00-00";
        assertFalse(s1.isValidDate(tooLongDate),
                "Invalid date - too many digits");
        assertFalse(s1.isValidDate(illFormatted),
                "Invalid date - should be in the format of YYYY-MM-DD");
        assertFalse(s1.isValidDate(zeroDate),
                "Invalid date - year, month, day cannot be zero");
    }

    @Test
    public void tooSmallDateTest() {
        String smallYear = "0000-01-02";
        String smallMonth = "2012-00-01";
        String smallDay = "2012-03-00";
        assertFalse(s1.isValidDate(smallYear),
                "Invalid date - year cannot be zero");
        assertFalse(s1.isValidDate(smallMonth),
                "Invalid date - month cannot be zero");
        assertFalse(s1.isValidDate(smallDay),
                "Invalid date - day cannot be zero");
    }

    @Test
    public void invalidDateStringInvalidTest() {
        String alphabetDate = "2022-ab-01";
        String outOfRange = "2022-19-20";
        String dayOutOfRange = "2022-01-39";

        assertFalse(s1.isValidDate(alphabetDate),
                "Invalid date - contain alphabets");
        assertFalse(s1.isValidDate(outOfRange),
                "Invalid date - extreme month");
        assertFalse(s1.isValidDate(dayOutOfRange),
                "Invalid date - extreme day");
    }

    @Test
    public void isDateInOrderTrueTest() {
        assertTrue(s1.isDateInOrder("2022-09-09", "2022-09-10"),
                "The valid dates are in the correct order!");
        assertTrue(s1.isDateInOrder("2022-08-09", "2022-09-09"),
                "The valid dates are in the correct order!");
        assertTrue(s1.isDateInOrder("2021-09-09", "2022-09-09"),
                "The valid dates are in the correct order!");
    }

    @Test
    public void isDateInOrderFalseTest() {
        assertFalse(s1.isDateInOrder("2022-09-09", "2022-09-09"),
                "The dates are not in correct order!");
        assertFalse(s1.isDateInOrder("2022-09-09", "2022-08-09"),
                "The dates are not in correct order!");
        assertFalse(s1.isDateInOrder("2022-09-09", "2021-09-09"),
                "The dates are not in correct order!");
    }
}
