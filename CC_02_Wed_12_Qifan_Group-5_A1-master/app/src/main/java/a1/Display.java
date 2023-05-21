package a1;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class Display {

    /*
     * Print the most popular currencies table
     * to the stdout.
     */
    public void displayTable(Update u) {

        String table = getTable(u);

        System.out.print(table);


    }

    /*
    * Retrieve the latest rates of the most popular currencies
    * and the previous rates to construct the most popular
    * currencies rates table. the output is a string.
     */
    public String getTable(Update u) {

        // Get the currencies symbols and the most popular currencies.
        ArrayList<String> mostPopular = u.getMostPopular();
        ArrayList<String> symbols = u.getSymbols();

        // get the latest matrix.
        ArrayList<ArrayList<Double>> upToDate = u.getCurrMatrix();

        // get the previous matrix.
        JSONArray jsonAll = u.getJsonAll();

        // get {date : {rates}} of the previous date.
        JSONObject dateAndRate = jsonAll.getJSONObject(jsonAll.length()-2);

        // get [date]
        JSONArray dateInArr = dateAndRate.names();

        // get date
        String prevDate = dateInArr.getString(0);

        // get {rates}
        JSONObject prevRates = dateAndRate.getJSONObject(prevDate);

        StringBuilder sb = new StringBuilder();

        //first row
        sb.append("\n  F/T  ");
        for (String curr : mostPopular) {
            sb.append(String.format("|  %s  ", curr));
        }
        sb.append("\n");

        //next rows
        for (String from : mostPopular) {
            sb.append(String.format("%39s\n", "").replace(" ", "_"));

            sb.append(String.format("  %s  ", from));

            // get the y-axis from the current matrix
            int y = symbols.indexOf(from);

            // get the y-axis from the previous matrix
            ArrayList<Double> xCurrMatrix = upToDate.get(y);

            JSONObject xPrevMatrix = prevRates.getJSONObject(from);

            for (String to : mostPopular) {

                int x = symbols.indexOf(to);

                if (y != x) {

                    double prevRate = xPrevMatrix.getDouble(to);
                    double currRate = xCurrMatrix.get(x);

                    // compare the prev rate with current, append Increase
                    // or Decrease symbol respectfully.
                    if (currRate > prevRate)
                        sb.append(String.format("|%-4.2f(I)", xCurrMatrix.get(x)));
                    else if (currRate < prevRate)
                        sb.append(String.format("|%-4.2f(D)", xCurrMatrix.get(x)));
                    else
                        sb.append(String.format("|%-7.2f", xCurrMatrix.get(x)));

                } else {
                    sb.append("|1.00   ");
                }
            }
            sb.append("\n");
        }

        return sb.toString();

    }
}
