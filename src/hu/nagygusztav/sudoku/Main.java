package hu.nagygusztav.sudoku;

import hu.nagygusztav.sudoku.feladvany.Feladvány;
import hu.nagygusztav.sudoku.megoldo.AbsztraktMegoldó;
import hu.nagygusztav.sudoku.megoldo.EgyszerűMegoldó;
import hu.nagygusztav.sudoku.struktura.tabla.AbsztraktTábla;
import hu.nagygusztav.sudoku.struktura.tabla.KilencszerKilencesTábla;
import hu.nagygusztav.sudoku.struktura.tabla.NégyszerNégyesTábla;
import hu.nagygusztav.sudoku.tesztelo.Tesztelő;
import java.util.logging.Logger;



/**
 *
 * @author Nagy Gusztáv
 */
public class Main {
    
    private static final Logger LOG = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) {

        // Teszt:
//        AbsztraktTábla tábla = new KilencszerKilencesTábla(
//                Feladvány.kilencszerKilencesek[4]);
        AbsztraktTábla tábla = new NégyszerNégyesTábla(
                Feladvány.négyszerNégyesek[0]);

        if (!Tesztelő.érvényesE(tábla)) {
            LOG.fine("A feladvány érvénytelen.");
            return;
        }

        AbsztraktMegoldó megoldó = new EgyszerűMegoldó();
        megoldó.megold(tábla);

        if (!Tesztelő.érvényesE(tábla)) {
            LOG.fine("A megoldás hibába ütközött.");
            return;
        }

        if (!Tesztelő.elkészültE(tábla)) {
            LOG.info("Eddig jutott:\n");
            LOG.info(tábla.tartalomEllenőrzéshez());
        }
    }
}
