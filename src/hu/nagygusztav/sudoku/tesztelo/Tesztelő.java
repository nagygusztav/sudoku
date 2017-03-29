package hu.nagygusztav.sudoku.tesztelo;

import hu.nagygusztav.sudoku.struktura.Cella;
import hu.nagygusztav.sudoku.struktura.SorOszlopBlokk;
import hu.nagygusztav.sudoku.struktura.tabla.AbsztraktTábla;
import java.util.Iterator;
import java.util.logging.Logger;

/**
 * Az osztály célja egyszerű tesztek megvalósítása. (Később lehet belőle Unit
 * Test is?!)
 *
 * @author nagy.gusztav
 */
public class Tesztelő {

    private static final Logger LOG = Logger.getLogger(Tesztelő.class.getName());

    public static boolean érvényesE(AbsztraktTábla tábla) {
        System.out.println("Érvényes-e ellenőrzés: ");
        System.out.println(tábla);
        boolean vissza = true;
        for (Iterator<SorOszlopBlokk> iSob = tábla.sorOszlopBlokkokBejáró(); iSob.hasNext();) {
            SorOszlopBlokk blokk = iSob.next();
            int[] előfordulások = new int[tábla.elemszám() + 1];
            for (Iterator<Cella> iCella = blokk.cellaBejáró(); iCella.hasNext();) {
                Cella cella = iCella.next();
                if (cella.kitöltve()) {
                    előfordulások[cella.getAdat()]++;
                }
            }
            for (int i = 1; i <= tábla.elemszám(); i++) {
                if (előfordulások[i] > 1) {
                    vissza = false;
                    System.out.println("Többszörös előfordulás (" + i + ") ebben: " + blokk);
                }
            }
        }
        System.out.println(vissza ? "Ok." : "Hibás.");

        return vissza;
    }

    public static boolean elkészültE(AbsztraktTábla tábla) {
        System.out.println("Elkészült-e ellenőrzés: ");
        System.out.println(tábla);
        boolean vissza = true;
        for (Iterator<SorOszlopBlokk> iSob = tábla.sorOszlopBlokkokBejáró(); iSob.hasNext();) {
            SorOszlopBlokk blokk = iSob.next();
            for (Iterator<Cella> iCella = blokk.cellaBejáró(); iCella.hasNext();) {
                Cella cella = iCella.next();
                if (!cella.kitöltve()) {
                    vissza = false;
                    System.out.println("Nincs kitöltve: " + cella);
                }
            }
        }
        System.out.println(vissza ? "Elkészült." : "Nem készült el.");

        return vissza;
    }
}
