package hu.nagygusztav.sudoku.struktura;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author nagy.gusztav
 */
public class SorOszlopBlokkTest {
    
    public SorOszlopBlokkTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of újCella method, of class SorOszlopBlokk.
     */
    @Test
    public void testÚjCella() {
        System.out.println("\u00fajCella");

        // TODO review the generated test code and remove the default call to fail.
    }

    /**
     * Test of cellaBejáró method, of class SorOszlopBlokk.
     */
    @Test
    public void testCellaBejáró() {
        System.out.println("cellaBej\u00e1r\u00f3");

        // TODO review the generated test code and remove the default call to fail.
    }

    /**
     * Test of getTípus method, of class SorOszlopBlokk.
     */
    @Test
    public void testGetTípus() {
        System.out.println("getT\u00edpus");
        assertEquals(new SorOszlopBlokk(SorOszlopBlokk.Típus.SOR).getTípus(), SorOszlopBlokk.Típus.SOR);
        assertEquals(new SorOszlopBlokk(SorOszlopBlokk.Típus.OSZLOP).getTípus(), SorOszlopBlokk.Típus.OSZLOP);
        assertEquals(new SorOszlopBlokk(SorOszlopBlokk.Típus.BLOKK).getTípus(), SorOszlopBlokk.Típus.BLOKK);
        assertNotEquals(new SorOszlopBlokk(SorOszlopBlokk.Típus.SOR).getTípus(), SorOszlopBlokk.Típus.OSZLOP);
        // TODO review the generated test code and remove the default call to fail.
    }

    /**
     * Test of setTábla method, of class SorOszlopBlokk.
     */
    @Test
    public void testSetTábla() {
        System.out.println("setT\u00e1bla");

        // TODO review the generated test code and remove the default call to fail.
    }
}
