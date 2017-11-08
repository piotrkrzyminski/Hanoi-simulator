package model;

import org.junit.Test;
import services.StackException;

import static org.junit.Assert.*;

public class TowerTest {
    private Tower tower = new Tower(10);
    @Test (expected = StackException.class)

    public void removeDisk() throws Exception {
        tower.addDisk(new Disk(1));
        tower.addDisk(new Disk(2));

        Disk removedDisk = tower.removeDisk();

        assertEquals(2, removedDisk.getIndex()); /* sprawdza czy został usunięty krążek na szczycie wieży */
        tower.removeDisk();
        tower.removeDisk();
    }

    @Test
    public void addDisk() throws Exception {
        tower.addDisk(new Disk(1));
        assertEquals(1, tower.size()); /* sprawdza czy wieża zawiera jeden krążek */
        Disk removedDisk = tower.removeDisk();
        assertEquals(1, removedDisk.getIndex());
    }

    @Test
    public void size() throws Exception {
        assertEquals(0, tower.size());
    }

}