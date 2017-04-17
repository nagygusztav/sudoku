package hu.nagygusztav.sudoku.struktura;

import hu.nagygusztav.sudoku.feladvany.Feladvány;
import hu.nagygusztav.sudoku.struktura.tabla.AbsztraktTábla;
import hu.nagygusztav.sudoku.struktura.tabla.KilencszerKilencesTábla;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
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
public class CellaTest {

    private AbsztraktTábla tábla = new KilencszerKilencesTábla(Feladvány.kilencszerKilencesek[1]);

    public CellaTest() {
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
     * Test of újSorOszlopBlokk method, of class Cella.
     */
    @Test
    public void testÚjSorOszlopBlokk() {
        System.out.println("\u00fajSorOszlopBlokk");

        Set<SorOszlopBlokk> sobOk = new HashSet<>();
        sobOk.add(new SorOszlopBlokk(SorOszlopBlokk.Típus.SOR));
        sobOk.add(new SorOszlopBlokk(SorOszlopBlokk.Típus.OSZLOP));
        sobOk.add(new SorOszlopBlokk(SorOszlopBlokk.Típus.BLOKK));

        Cella cella = new Cella("1x1", 9, tábla);
        for (SorOszlopBlokk sob : sobOk) {
            cella.újSorOszlopBlokk(sob);
        }

        for (Iterator<SorOszlopBlokk> iSob = cella.sorOszlopBlokkBejáró(); iSob.hasNext();) {
            SorOszlopBlokk sob = iSob.next();
            assertTrue(sobOk.remove(sob));
        }
        assertTrue(sobOk.isEmpty());
    }

    /**
     * Test of toString method, of class Cella.
     */
    @Test
    public void testToString() {
        System.out.println("toString");

        Cella cella = new Cella("1x1", 9, tábla);
        assertEquals("1x1: -(123456789)", cella.toString());

        cella.törölLehetőség(3);
        cella.törölLehetőség(7);
        assertEquals("1x1: -(12.456.89)", cella.toString());

        cella.kitölt(5);
        assertEquals("1x1: 5(12.456.89)", cella.toString());
    }

    /**
     * Test of getSzövegesAzonosító method, of class Cella.
     */
    @Test
    public void testGetSzövegesAzonosító() {
        System.out.println("getSz\u00f6vegesAzonos\u00edt\u00f3");

        String szövegesAzonosító = "Almma";
        Cella cella = new Cella(new String(szövegesAzonosító), 9, tábla);
        assertEquals(szövegesAzonosító, cella.getSzövegesAzonosító());
    }

    /**
     * Test of kitöltve method, of class Cella.
     */
    @Test
    public void testKitöltve() {
        System.out.println("kit\u00f6ltve");

        int lehetőségekSzáma = 23;
        Cella cella = new Cella("xy", lehetőségekSzáma, tábla);
        assertFalse(cella.kitöltve());
        cella.kitölt(3);
        assertTrue(cella.kitöltve());
    }

    /**
     * Test of nincsKitöltve method, of class Cella.
     */
    @Test
    public void testNincsKitöltve() {
        int lehetőségekSzáma = 23;
        Cella cella = new Cella("xy", lehetőségekSzáma, tábla);

        assertTrue(cella.nincsKitöltve());
    }

    /**
     * Test of kitölt method, of class Cella.
     */
    @Test
    public void testKitölt() {
        System.out.println("kit\u00f6lt");

        int lehetőségekSzáma = 23;
        Cella cella = new Cella("xy", lehetőségekSzáma, tábla);
        assertFalse(cella.kitöltve());
        assertTrue(cella.getAdat() == cella.NINCSKITOLTVE);

        cella.kitölt(3);

        assertTrue(cella.kitöltve());
        assertFalse(cella.getAdat() == cella.NINCSKITOLTVE);
    }

    /**
     * Test of equals method, of class Cella.
     */
    @Test
    public void testEquals() {
        System.out.println("equals");
        Cella cella1 = new Cella("xy", 23, tábla);
        Cella cella2 = new Cella("xy", 23, tábla);
        Cella cella3 = new Cella("xyz", 23, tábla);
        Cella cella4 = new Cella("xy", 24, tábla);

        assertEquals(cella1, cella2);
        assertNotEquals(cella1, cella3);
        assertNotEquals(cella1, cella4);
    }

    /**
     * Test of töröl method, of class Cella.
     */
    @Test
    public void testTöröl() {
        System.out.println("t\u00f6r\u00f6l");

        int lehetőségekSzáma = 23;
        Cella cella = new Cella("xy", lehetőségekSzáma, tábla);

        for (int i = 1; i <= lehetőségekSzáma; i += 2) {
            cella.törölLehetőség(i);
        }
        for (int i = 1; i <= lehetőségekSzáma; i++) {
            assertTrue(cella.lehetMég(i) == (i % 2 == 0));
        }
    }

    /**
     * Test of lehetMég method, of class Cella.
     */
    @Test
    public void testLehetMég() {
        System.out.println("lehetM\u00e9g");
        int lehetőségekSzáma = 23;
        Cella cella = new Cella("xy", lehetőségekSzáma, tábla);
        assertFalse(cella.lehetMég(0));
        for (int i = 1; i <= lehetőségekSzáma; i++) {
            assertTrue(cella.lehetMég(i));
        }
    }

    /**
     * Test of törölMindenLehetőséget method, of class Cella.
     */
    @Test
    public void testTörölMindenLehetőséget() {
        System.out.println("t\u00f6r\u00f6lMindenLehet\u0151s\u00e9get");
        int lehetőségekSzáma = 23;
        Cella cella = new Cella("xy", lehetőségekSzáma, tábla);
        cella.törölMindenLehetőséget();

        for (int i = 1; i <= lehetőségekSzáma; i++) {
            assertFalse(cella.lehetMég(i));
        }
    }
}
