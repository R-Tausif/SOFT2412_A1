package a1;


import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


public class Summary {

    JSONArray extractedDetailsByDate = new JSONArray();
    JSONArray extractedDetailsByCurr = new JSONArray();
    ArrayList<Double> currencyRates = new ArrayList<>();
    ArrayList<Set> extractedDates = new ArrayList<>();
    ArrayList<String> extractedDatesStr = new ArrayList<>();
    ArrayList<String> datesInRange = new ArrayList<>();;

    /*
    A method that takes the start and end dates entered by the user and
    checks if test_date falls within that range, returns true or false
     */
    boolean isWithinRange(Date testDate, Date startDate, Date endDate) {
        return !(testDate.before(startDate) || testDate.after(endDate));
    }
    /*
    A method that takes the currencies and the start and end dates
    entered by the user. It uses the JsonAll attribute to extract the dates
    that fall within that range and extracts the currency rates against them.
     */
    public void readJSON(Update obj, String currency_1, String currency_2, String date_1, String date_2) throws ParseException {
        // Extracts all the dates that are in the history.json file
        for (int i = 0; i < obj.getJsonAll().length(); i++) {
            extractedDates.add(obj.getJsonAll().getJSONObject(i).keySet());
        }
        // From the dates extracted, convert those dates to string
        for (int i = 0; i < extractedDates.size(); i++) {
            extractedDatesStr.add(extractedDates.get(i).toString().replace("[", "").replace("]", ""));
        }
        // From the dates extracted, only keep the rates information which fall within the range of dates user inputted
        for (int i = 0; i < extractedDatesStr.size(); i++) {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
            if (isWithinRange(formatter.parse(extractedDatesStr.get(i)), formatter.parse(date_1), formatter.parse(date_2))) {
                JSONObject extracted = obj.getJsonAll().getJSONObject(i).getJSONObject(extractedDatesStr.get(i));

                // Check if the 'from' currency exists in the specific history date
                if (extracted.has(currency_1)) {
                    // Check if the 'to' currency exists in the specific history date
                    if(extracted.getJSONObject(currency_1).has(currency_2)) {
                        datesInRange.add(extractedDatesStr.get(i));
                        extractedDetailsByDate.put(extracted);
                    }
                }
            }
        }
        // Extract the information of the 'from' currency
        for (int i = 0; i < extractedDetailsByDate.length(); i++) {
            if (extractedDetailsByDate.getJSONObject(i).has(currency_1)) {
                extractedDetailsByCurr.put(extractedDetailsByDate.getJSONObject(i).get(currency_1));
            }
        }
        // Extract the currency rates from 'to' currency
        for (int i = 0; i < extractedDetailsByCurr.length(); i++) {
            currencyRates.add((Double.valueOf(extractedDetailsByCurr.getJSONObject(i).get(currency_2).toString())));
        }
    }
    /*
    Calculates the average rate of all the currency rates present from the 'from'
    currency to the 'to' currency
     */
    public double calculateAvg(ArrayList<Double> currencyRates){
        double sum = 0;
        Collections.sort(currencyRates);
        for(int i = 0; i < currencyRates.size(); i++){
            sum += currencyRates.get(i);
        }
        return sum/currencyRates.size();
    }

    /*
    Iterates over all the currency rates present and returns the maximum
    currency rate during those range of dates (start_date, end_date)
     */
    public double calculateMax(ArrayList<Double> currencyRates){
        double maxVal = currencyRates.get(0);

        for(int i = 0; i < currencyRates.size(); i++){
            if (currencyRates.get(i) >= maxVal){
                maxVal = currencyRates.get(i);
            }
        }
        return maxVal;
    }
    /*
    Iterates over all the currency rates present and returns the minimum
    currency rate during those range of dates (start_date, end_date)
     */
    public double calculateMin(ArrayList<Double> currencyRates){
        double minVal = currencyRates.get(0);

        for(int i = 0; i < currencyRates.size(); i++){
            if (currencyRates.get(i) <= minVal){
                minVal = currencyRates.get(i);
            }
        }
        return minVal;
    }
    /*
       Iterates over all the currency rates present and returns the median
       currency rate during those range of dates (start_date, end_date)
    */
    public double calculateMedian(ArrayList<Double> currencyRates){
        Collections.sort(currencyRates);
        double median = 0;

        if (currencyRates.size() % 2 == 0){
            median = (currencyRates.get(currencyRates.size()/2) + currencyRates.get(currencyRates.size()/2-1))/2;
        }
        else{
            median = currencyRates.get(currencyRates.size()/2);
        }
        return median;
    }
    /*
   Iterates over all the currency rates present and returns the standard deviation
   currency rate during those range of dates (start_date, end_date)
    */
    public double calculateSD(ArrayList<Double> currencyRates){

        double mean = calculateAvg(currencyRates);
        double temp = 0;

        for(int i = 0; i < currencyRates.size(); i++){
            double val = currencyRates.get(i);
            double sqDiffToMean = Math.pow(currencyRates.get(i) - mean, 2);
            temp += sqDiffToMean;
        }

        double meanOfDiffs = temp/currencyRates.size();
        return Math.sqrt(meanOfDiffs);
    }
    /*
    Parses the date to check if the date is valid, returns a boolean value
     */
    public Boolean isValidDate(String date) {
        String[] dates = date.split("-");
        if (dates.length == 3) {
            String year = dates[0];
            String month = dates[1];
            String day = dates[2];
            if (year.length() == 4 && month.length() == 2 && day.length() == 2) {
                try {
                    int intYear = Integer.parseInt(year);
                    int intMonth = Integer.parseInt(month);
                    int intDay = Integer.parseInt(day);

                    if (intYear > 0 && intMonth > 0 && intDay > 0) {
                        if (intMonth <= 12 && intDay <= 31) {
                            return true;
                        }
                    }
                    return false;
                } catch (NumberFormatException e) {
                    return false;
                }
            } else {
                return false;
            }
        }
        return false;
    }
    /*
    Compares both the dates to check if they are in chronological order or not
    returns true or false
     */
    public Boolean isDateInOrder(String date1, String date2) {
        String[] dates1 = date1.split("-");
        String[] dates2 = date2.split("-");
        int date1Year = Integer.parseInt(dates1[0]);
        int date2Year = Integer.parseInt(dates2[0]);
        int date1Month = Integer.parseInt(dates1[1]);
        int date2Month = Integer.parseInt(dates2[1]);
        int date1Day = Integer.parseInt(dates1[2]);
        int date2Day = Integer.parseInt(dates2[2]);
        boolean valid = false;

        if (date1Year < date2Year) {
            valid = true;
        } else if (date1Year == date2Year){
            if (date1Month < date2Month) {
                valid = true;
            } else if (date1Month == date2Month) {
                if (date1Day < date2Day) {
                    valid = true;
                }
            }
        }
        return valid;
    }
    /*
    Iterates over the extracted dates in the range of start_date,
    end_date and prints the rates on those days
     */
    public void printHistory(){
        System.out.println("The rate history between those dates are: ");
        for (int i = 0; i < datesInRange.size(); i++) {
            String toPrint = datesInRange.get(i) + ": " + currencyRates.get(i);
            System.out.println(toPrint);
        }
        System.out.println();
    }
    /*
    Prints the summary of the currency rates during the period
    start_date and end_date
     */
    public void printSummary(){
        System.out.println("The maximum is: " + calculateMax(currencyRates));
        System.out.println("The median is: " + calculateMedian(currencyRates));
        System.out.println("The average is: " + Math.round(calculateAvg(currencyRates)*10000.0)/10000.0);
        System.out.println("The standard deviation is: " + Math.round(calculateSD(currencyRates)*10000.0)/10000.0);
        System.out.println("The minimum is: " + calculateMin(currencyRates));
        System.out.println();
    }

}



/*
References:
https://stackoverflow.com/questions/7503877/java-correct-way-convert-cast-object-to-double
https://stackoverflow.com/questions/23956154/store-json-data-in-java
https://stackoverflow.com/questions/42549545/get-value-by-key-jsonarray
https://mkyong.com/java8/java-8-how-to-convert-string-to-localdate/
https://www.baeldung.com/java-between-dates
 */