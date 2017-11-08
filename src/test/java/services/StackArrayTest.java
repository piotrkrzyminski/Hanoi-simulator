package services;

import model.Disk;
import org.junit.Test;

import static org.junit.Assert.*;

public class StackArrayTest {
    private Stack<Disk> stack = new StackArray<>(10);
    @Test
    public void push() throws Exception {
        Disk disk1 = new Disk(1);
        Disk disk2 = new Disk(2);

        stack.push(disk1); /* dodanie nowego dysku do stosu */
        stack.push(disk2); /* dodanie nowego dysku do stosu */
        assertEquals(2, stack.size()); /* sprawdzenie czy wielkość stosu wynosi 1 */
    }

    @Test(expected = StackException.class)
    public void pop() throws Exception {
        Disk disk = stack.pop();

        assertEquals(2, disk.getIndex()); /* sprawdzenie czy został zdjęty krążek na szczycie stosu */
        assertEquals(1, stack.size()); /* sprawdzenie czy romiar stosu zmniejszył się o 1 */
    }

    @Test
    public void size() throws Exception {
        stack.push(new Disk(3));
        assertEquals(1, stack.size()); /* sprawdzenie czy wielkość stosu jest poprawna */
    }

    @Test(expected = StackException.class)
    public void isEmpty() throws Exception {
        stack = new StackArray<>(10);
        stack.pop();
    }

    @Test(expected = StackException.class)
    public void isFull() throws Exception {
        stack = new StackArray<>(10);

        for(int i=0; i<=10; i++) {
            stack.push(new Disk(i+1));
        }
    }

}