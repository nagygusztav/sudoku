package hu.nagygusztav.sudoku.tesztelo;

import hu.nagygusztav.sudoku.struktura.Cella;
import hu.nagygusztav.sudoku.struktura.SorOszlopBlokk;
import hu.nagygusztav.sudoku.struktura.tabla.AbsztraktTábla;
import java.util.Iterator;
import java.util.logging.Level;
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
        LOG.info("Érvényes-e ellenőrzés: \n");
        LOG.info(tábla.toString());
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
                    LOG.log(Level.INFO, "T\u00f6bbsz\u00f6r\u00f6s el\u0151fordul\u00e1s ({0}) ebben: {1}", new Object[]{i, blokk});
                }
            }
        }
        LOG.info(vissza ? "Ok." : "Hibás.");

        return vissza;
    }

    public static boolean elkészültE(AbsztraktTábla tábla) {
        LOG.info("Elkészült-e ellenőrzés: ");
        LOG.info(tábla.tartalomEllenőrzéshez());
        boolean vissza = true;
        for (Iterator<SorOszlopBlokk> iSob = tábla.sorOszlopBlokkokBejáró(); iSob.hasNext();) {
            SorOszlopBlokk blokk = iSob.next();
            for (Iterator<Cella> iCella = blokk.cellaBejáró(); iCella.hasNext();) {
                Cella cella = iCella.next();
                if (!cella.kitöltve()) {
                    vissza = false;
                    LOG.info("Nincs kitöltve: " + cella);
                }
            }
        }
        LOG.info(vissza ? "Elkészült." : "Nem készült el.");

        return vissza;
    }
}
