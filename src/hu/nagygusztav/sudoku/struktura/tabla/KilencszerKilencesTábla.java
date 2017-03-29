package hu.nagygusztav.sudoku.struktura.tabla;

import hu.nagygusztav.sudoku.struktura.Cella;
import hu.nagygusztav.sudoku.struktura.Koordináta;
import hu.nagygusztav.sudoku.struktura.SorOszlopBlokk;
import java.util.logging.Logger;

/**
 *
 * @author Nagy Gusztáv
 */
public class KilencszerKilencesTábla extends TéglalapTábla {

    private static final Logger LOG = Logger.getLogger(KilencszerKilencesTábla.class.getName());
    private static final Koordináta[] BLOKK_KEZDOKOORDINATAK = {
        new Koordináta(1, 1),
        new Koordináta(1, 4),
        new Koordináta(1, 7),
        new Koordináta(4, 1),
        new Koordináta(4, 4),
        new Koordináta(4, 7),
        new Koordináta(7, 1),
        new Koordináta(7, 4),
        new Koordináta(7, 7)
    };

    private final Koordináta[] BLOKK_ELTOLASKOORDINATAK = {
        new Koordináta(0, 0),
        new Koordináta(0, 1),
        new Koordináta(0, 2),
        new Koordináta(1, 0),
        new Koordináta(1, 1),
        new Koordináta(1, 2),
        new Koordináta(2, 0),
        new Koordináta(2, 1),
        new Koordináta(2, 2)
    };

    public KilencszerKilencesTábla() {
        super(9, 9, 9);

        // blokkok
        for (Koordináta kezdő : BLOKK_KEZDOKOORDINATAK) {
            SorOszlopBlokk sob = new SorOszlopBlokk(SorOszlopBlokk.Típus.BLOKK);
            for (Koordináta eltolás : BLOKK_ELTOLASKOORDINATAK) {
                Cella cella = cellák[kezdő.sor + eltolás.sor][kezdő.oszlop + eltolás.oszlop];
                sob.újCella(cella);
                cella.újSorOszlopBlokk(sob);
            }
            sob.setTábla(this);
            sorOszlopBlokkok.add(sob);
        }
    }

    public KilencszerKilencesTábla(String kezdőkód) {
        this();
        if (kezdőkód.length() != sorokSzáma * oszlopokSzáma) {
            throw new IllegalArgumentException("Nem megfelelő kezdőkód!");
        }

        int index = 0;
        for (int sor = 1; sor <= sorokSzáma; sor++) {
            for (int oszlop = 1; oszlop <= oszlopokSzáma; oszlop++) {
                int szám = Integer.parseInt(kezdőkód.substring(index, index + 1));
                if (szám > 0) {
                    cellák[sor][oszlop].kitölt(szám);
                }
                index++;
            }
        }
    }

}
