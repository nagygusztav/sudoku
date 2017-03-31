package hu.nagygusztav.sudoku.struktura;

import static org.junit.Assert.*;
import org.junit.Test;

/**
 *
 * @author nagy.gusztav
 */
public class KoordinátaTest {
    
    @Test
    public void testSomeMethod() {
        Koordináta koordináta = new Koordináta(3, 5);
        assertTrue(koordináta.sor == 3);
        assertTrue(koordináta.oszlop == 5);        
    }
    
}
