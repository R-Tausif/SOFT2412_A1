/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package a1;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import static com.github.stefanbirkner.systemlambda.SystemLambda.*;

class DisplayTest {

    private App converter;

    private Update update;

    private Display display;

    @BeforeEach
    void setup() {
        this.converter= new App();
        this.update = converter.config("test");
        this.display = new Display();
    }

    @Test
    void displayTableTest() throws Exception {

        String text = tapSystemOut(() -> {

            display.displayTable(this.update);
        });

        String expected = display.getTable(this.update);

        assertEquals(expected, text, "displayTableTest fails");

    }

    @Test
    void symbolsTest() {

        String text = display.getTable(this.update);

        boolean exist = false;

        for (int i = 0; i < text.length(); i++) {

            if (text.charAt(i) == 'I' || text.charAt(i) == 'D') {
                if (text.charAt(i-1) == '(')
                    exist = true;
            }
        }

        assertTrue(exist, "increase or decrease symbol does not exist.");
    }

}
