package a1;

import java.util.ArrayList;
import java.io.*;
import java.util.List;
import java.util.Scanner;

import org.checkerframework.checker.units.qual.A;
import org.json.JSONObject;
import org.json.JSONArray;

public class Update {
    private ArrayList<ArrayList<Double>> currMatrix;
    private ArrayList<String> symbols;
    private ArrayList<String> mostPopular;
    private JSONObject jsonCurr;
    private JSONArray jsonAll;
    private String dir;

    public Update(ArrayList<ArrayList<Double>> currMatrix, ArrayList<String> symbols, ArrayList<String> mostPopular,
                  JSONArray jsonAll, String dir) {
        this.symbols = symbols;
        this.currMatrix = currMatrix;
        this.mostPopular = mostPopular;
        this.jsonAll = jsonAll;
        this.dir = dir;
    }

    /*
     * Convert the matrix which stores all current exchange rates
     * into a nested Json array, with date being the key, and stores
     * each currency's exchange rate as a json object
     */
    public void toJson(String dateStr) {
        JSONObject curr = new JSONObject();
        JSONObject rates = new JSONObject();

        ArrayList<Double> xAxis = null;

        int len = this.symbols.size();
        for (int i = 0; i < len; i++) {

            xAxis = this.currMatrix.get(i);

            String strFrom = this.symbols.get(i);
            JSONObject objTo = new JSONObject();

            for (int j = 0; j < len; j++) {

                if (i != j) {

                    String strTo = this.symbols.get(j);
                    objTo.put(strTo, xAxis.get(j));
                }
            }

            rates.put(strFrom, objTo);
        }

        curr.put(dateStr, rates);
        this.jsonCurr = curr;
        this.jsonAll.put(curr);
    }

    /*
     * Write the dateArray to the file history.json
     */
    public void writeJson() {
        String path = String.format("src/%s/resources/history.json", dir);
        try (PrintWriter out = new PrintWriter(new FileOutputStream(path))) {
            out.write(this.jsonAll.toString(4));
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
     * Returns the current currency rates as JSONObject
     */
    public JSONObject getUpdatedCurr() {
        return this.jsonCurr;
    }

    /*
     * Return the JSONArray that stores all the history of rates
     */
    public JSONArray getJsonAll() {
        return this.jsonAll;
    }

    /*
     * Returns the array list of the most popular currencies
     */
    public ArrayList<String> getMostPopular() {
        return this.mostPopular;
    }

    /*
     * Given the index of an exchange rate in the currMatrix
     * update this exchange rate to num and the inverse exchange
     * rate to 1/num
     */
    public void setCurrMatrix(int y, int x, double num) {
        ArrayList<Double> from_currency = this.currMatrix.get(y);
        from_currency.set(x, num);
        this.currMatrix.set(y, from_currency);
    }

    /*
     * Add a new symbol to the symbols array
     */
    public void addSymbols(String symb) {
        this.symbols.add(symb.toUpperCase());
    }

    /*
     * Return all symbols
     */
    public ArrayList<String> getSymbols() {
        return this.symbols;
    }

    /*
    * return currMatrix
     */
    public ArrayList<ArrayList<Double>> getCurrMatrix() {
        return this.currMatrix;
    }


    /*
    * Given the symbols of the exchange rate from and to,
    * and the new exchange rate, update this in the currMatrix
    * as well as the exchange rate to and from
     */
    public void updateCurrent(String from, String to, double newCurr) {
        int from_i = symbols.indexOf(from);
        int to_i = symbols.indexOf(to);
        double roundCurr = Math.round(newCurr*1000.0)/1000.0;
        double roundInv = Math.round((1/newCurr)*1000.0)/1000.0;
        setCurrMatrix(from_i, to_i, roundCurr);
        setCurrMatrix(to_i, from_i, roundInv);
    }

    /*
     * Given a new symbol, and its exchange rate to all other existing
     * currencies, expand the currMatrix to store this new data
     */
    public void updateNew(String newSymbol, ArrayList<Double> newCurr) {
        this.addSymbols(newSymbol);
        this.writeSymbols();

        ArrayList<Double> newArray = null;
        for (int i = 0; i < this.currMatrix.size(); i++) {
            newArray = this.currMatrix.get(i);
            double roundNewCurr = Math.round((1/newCurr.get(i))*1000.0)/1000.0;
            newArray.add(roundNewCurr);
            this.currMatrix.set(i, newArray);
        }

        newCurr.add(1.00);
        this.currMatrix.add(newCurr);
    }

    /*
    * write the updated currencies to the currencies.csv
     */
    public void writeSymbols() {
        String path = String.format("src/%s/resources/currencies.csv", dir);
        File file = new File(path);
        try {
            PrintWriter writer = new PrintWriter(file);
            int len = this.symbols.size();

            for (int i = 0; i < len; i++) {
                if (i == len-1)
                    writer.println(this.symbols.get(i));
                else
                    writer.print(String.format("%s,", this.symbols.get(i)));
            }
            writer.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /*
     * write the updated most popular currencies to the
     * popular.csv
     */
    public void writeMostPopular(ArrayList<String> mostPopular) {
        String path = String.format("src/%s/resources/popular.csv", dir);
        File file = new File(path);

        try {
            PrintWriter writer = new PrintWriter(file);
            int len = mostPopular.size();

            for (int i = 0; i < len; i++) {
                if (i == len-1)
                    writer.println(mostPopular.get(i));
                else
                    writer.print(String.format("%s,", mostPopular.get(i)));
            }
            writer.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /*
     * Interact with user to ask details regarding updating existing
     * exchange rates.
     */
    public void updateExisting(Scanner scan) {
        System.out.print("\nInput currency to update from: ");
        String from = scan.nextLine();
        from = from.toUpperCase();

        // Check if the from currency exists in the database
        while (!symbols.contains(from)) {
            System.out.println("\nPlease only select from below: ");
            System.out.println(this.symbols);
            System.out.print("Input currency to update from: ");
            from = scan.nextLine();
            from = from.toUpperCase();
        }

        // Filter the currencies that is not equal to the from currency
        List<String> symbWithoutFrom = new ArrayList<>(this.symbols);
        symbWithoutFrom.remove(from);

        System.out.print("\nInput currency to update to: ");
        String to = scan.nextLine();
        to = to.toUpperCase();

        // Check if the to currency exists in the database
        while(!symbols.contains(to) || to.equals(from)) {
            System.out.println("\nPlease enter another currency from below: ");
            System.out.println(symbWithoutFrom);
            System.out.print("Input currency to update to: ");
            to = scan.nextLine();
            to = to.toUpperCase();
        }

        System.out.print("\nInput new currency rate from " + from + ", to " + to + ": ");
        double newCurrency = scan.nextDouble();
        scan.nextLine();

        // Check if the exchange rate is too small to prevent division by zero error
        // when computing the inverse rate
        while (newCurrency <= 0.0001) {
            System.out.println("\nThis currency rate is too small, please try again");
            System.out.print("Input new currency rate from " + from + ", to " + to + ": ");
            newCurrency = scan.nextDouble();
            scan.nextLine();
        }

        this.updateCurrent(from, to, newCurrency);
    }

    /*
     * Interact with user to ask the new currency symbol
     * and its exchange rate with all existing currencies.
     * This information is then passed on and stored in Update
     * class's currMatrix, and jsonAll
     */
    public void updateNewCurrency(Scanner scan) {
        ArrayList<Double> newCurrency2 = new ArrayList<>();
        System.out.print("\nInput new currency symbol: ");
        String newSymb = scan.nextLine();
        newSymb = newSymb.toUpperCase();

        // Check if the new symbol is the same as existing symbols
        while (symbols.contains(newSymb) || newSymb.length() != 3) {
            if (symbols.contains(newSymb)) {
                System.out.println("\nThe following symbols already exists, please try again!");
                System.out.println(symbols);
            } else {
                System.out.println("\nThe currency symbol can only contain 3 characters, please try again!");
            }
            System.out.print("Input new currency symbol: ");
            newSymb = scan.nextLine();
            newSymb = newSymb.toUpperCase();
        }

        // Input for the exchange rate to existing currencies
        for (int i = 0; i < symbols.size(); i++) {
            System.out.print("\nInput the exchange rate from " + newSymb + " to " + symbols.get(i) + ": ");
            double curRate = scan.nextDouble();
            scan.nextLine();

            // Ensure that the currency is not too small, to avoid divide by
            // zero exception when converting the inverse exchange rate
            while (curRate <= 0.0001) {
                System.out.println("\nThis currency rate is too small, please try again!");
                System.out.print("Input the exchange rate from " + newSymb + " to " + symbols.get(i) + ": ");
                curRate = scan.nextDouble();
                scan.nextLine();
            }
            newCurrency2.add(curRate);
        }

        this.updateNew(newSymb, newCurrency2);
    }

    /*
     * Ask users to input the four most popular currencies
     * and record the updated popular currencies into
     * popular.csv
     */
    public void updateMostPopular(Scanner scan) {
        String[] msgs = {"first", "second", "third", "fourth"};
        ArrayList<String> mostPopularCurr = new ArrayList<>();

        for (int i = 0; i < msgs.length; i++) {
            String msg = "\nPlease input the " + msgs[i] + " popular currency: ";
            String curr = this.askPopular(scan, msg, mostPopularCurr);
            mostPopularCurr.add(curr);
        }
        this.mostPopular = mostPopularCurr;
        this.writeMostPopular(mostPopularCurr);
    }

    /*
     * Interact with the admin, check if the currency selected exists
     * and return the currency as a string
     */
    public String askPopular(Scanner scan, String msg, ArrayList<String> popCurr) {
        System.out.print(msg);
        String popular = scan.nextLine();
        popular = popular.toUpperCase();

        // Check if the currency is already selected or does not exist in the database
        while (!this.symbols.contains(popular) || popCurr.contains(popular)) {
            if (!this.symbols.contains(popular)) {
                System.out.println("This currency symbol does not exist, please try again!");
            } else {
                System.out.println("This currency is already selected, please try again!");
            }
            System.out.print(msg);
            popular = scan.nextLine();
            popular = popular.toUpperCase();
        }
        return popular;
    }

}