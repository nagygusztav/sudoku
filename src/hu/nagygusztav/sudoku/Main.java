package hu.nagygusztav.sudoku;

import hu.nagygusztav.sudoku.feladvany.Feladvány;
import hu.nagygusztav.sudoku.megoldo.AbsztraktMegoldó;
import hu.nagygusztav.sudoku.megoldo.EgyszerűMegoldó;
import hu.nagygusztav.sudoku.struktura.tabla.AbsztraktTábla;
import hu.nagygusztav.sudoku.struktura.tabla.KilencszerKilencesTábla;
import hu.nagygusztav.sudoku.struktura.tabla.NégyszerNégyesTábla;
import hu.nagygusztav.sudoku.tesztelo.Tesztelő;

/**
 *
 * @author Nagy Gusztáv
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // Megoldva: 

        // Teszt:
        AbsztraktTábla tábla = new KilencszerKilencesTábla(
                Feladvány.kilencszerKilencesek[4]);
        if (!Tesztelő.érvényesE(tábla)) {
            return;
        }

        AbsztraktMegoldó megoldó = new EgyszerűMegoldó();
        megoldó.megold(tábla);

        if (!Tesztelő.érvényesE(tábla)) {
            return;
        }

        if (!Tesztelő.elkészültE(tábla)) {
            System.out.println("Eddig jutott:");
            tábla.kiírTartalomEllenőrzéshez();
        }
    }
}
