package services;

import org.junit.Test;

import static org.junit.Assert.*;

public class CommandsTest {
    String line = "1 3";
    int towerCount = 3;

    @Test
    public void parse() throws Exception {
        int[] command = new int[2];

        assertTrue(line.matches("\\d+\\s\\d+$")); /* sprawdza poprawność wyrażenia regularnego */

        String[] commandString = line.split("\\s");

        assertEquals("1", commandString[0]);
        assertEquals("3", commandString[1]);

        int from = Integer.valueOf(commandString[0]) - 1;
        int to = Integer.valueOf(commandString[1]) - 1;

        assertEquals(0, from);
        assertEquals(2, to);

        assertFalse((from<0 || from>towerCount) && (to<0 || to>towerCount));
    }

}