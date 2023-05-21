package a1;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.*;
import java.util.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ConvertTest{
    private App app;
    private Convert convert;

    @BeforeEach
    public void setUp() {
        this.app = new App();
        Update update = this.app.config("main");
        this.convert = new Convert(update);
        JSONObject latestRates = app.part1("main");
        convert.set_latestJSON(latestRates);
    }

    @Test
    public void testObj() {
        assertNotNull(this.app);
        assertNotNull(this.convert);
        assertNotNull(this.convert.getInfo());
    }

    @Test
    public void testInput() {
        InputStream sysInBackup = System.in;

        try {
            InputStream f = new FileInputStream("src/test/resources/ConvertInput.txt");
            System.setIn(f);
        } catch (Exception e) {
            e.printStackTrace();
        }

        this.convert.get_require(new Scanner(System.in));

        Double money = Double.parseDouble("100");

        assertEquals(money, this.convert.getAmount());
        assertEquals("AUD", this.convert.getOC());
        assertEquals("CNY", this.convert.getDC());

        System.setIn(sysInBackup);
    }

    @Test
    public void testInput2() {
        InputStream sysInBackup = System.in;

        try {
            InputStream f = new FileInputStream("src/test/resources/ConvertInput2.txt");
            System.setIn(f);
        } catch (Exception e) {
            e.printStackTrace();
        }

        this.convert.get_require(new Scanner(System.in));

        Double money = Double.parseDouble("200");

        assertEquals(money, this.convert.getAmount());
        assertEquals("CNY", this.convert.getOC());
        assertEquals("AUD", this.convert.getDC());

        System.setIn(sysInBackup);
    }
}