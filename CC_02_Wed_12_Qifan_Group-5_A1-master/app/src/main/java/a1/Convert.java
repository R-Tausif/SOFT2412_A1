package a1;

//import org.json.simple.*;
//import org.json.simple.JSONArray;
//import org.json.simple.JSONObject;
//import org.json.simple.parser.JSONParser;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.FileReader;
import java.util.*;

public class Convert{
    public JSONObject info;
//    public String today_date;
    private Double money_amount;
    private String ori_currency;
    private String desired_currency;
    private Update update;

    public Convert(Update update) {
        this.money_amount = 0.0;
//        this.today_date = "";
        this.ori_currency = "";
        this.desired_currency = "";
        this.info = null;
        this.update = update;
    }

    public boolean check_currency(String curr_code) {
        curr_code = curr_code.toUpperCase();
        ArrayList<String> symbols = update.getSymbols();
        if (!symbols.contains(curr_code))  {
                System.out.println("No existing currency in the database");
                return false;
        }
        return true;
    }

    public void set_latestJSON(JSONObject info) {
        this.info = info;
    }

    public void display_convert() {
        if (this.info == null) {
            System.out.println("this.info is null in method");
        }

//        JSONObject date_obj = (JSONObject) (this.info).get(this.today_date);
        JSONObject ori_currency = (JSONObject) info.get(this.ori_currency);

        double desired_rate = ori_currency.getDouble(this.desired_currency);

        Double final_money = this.money_amount * desired_rate;
        System.out.println(String.format("\nThe money amount after conversion is: %.2f", final_money));

        // initialize attributes
        this.money_amount = 0.0;
        // this.today_date = "";
        this.ori_currency = "";
        this.desired_currency = "";
    }

    public void get_require(Scanner sc) {
        System.out.println("\nYou choose the CONVERT functionality");

       while (true) {
            System.out.print("Please enter the money amount: ");
            String money = sc.nextLine();

            try {
                this.money_amount = Double.parseDouble(money);
                break;
            } catch (NumberFormatException e) {
                System.out.println("Invalid input");
                continue;
            }
       }

        while (true) {
            System.out.print("Please enter its currency symbol: ");
            String nextline = sc.nextLine();
            if (!check_currency(nextline)) {
                continue;
            }else {
                this.ori_currency = nextline.toUpperCase();
                break;
            }
        }
            
        while(true) {
            System.out.print("Please enter the desired currency symbol you want to convert: ");
            String nextline = sc.nextLine();
            if (!check_currency(nextline)) {
                continue;
            }else {
                this.desired_currency = nextline.toUpperCase();
                break;
            }
        }
    }

    public Double getAmount() { return this.money_amount; }
    public String getOC() { return this.ori_currency; }
    public String getDC() { return this.desired_currency; }
    public JSONObject getInfo() { return this.info; }

}