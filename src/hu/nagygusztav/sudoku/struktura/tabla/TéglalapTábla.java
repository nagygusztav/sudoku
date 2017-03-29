package hu.nagygusztav.sudoku.struktura.tabla;

import hu.nagygusztav.sudoku.muvelet.LehetőségTörölveMűvelet;
import hu.nagygusztav.sudoku.struktura.Cella;
import hu.nagygusztav.sudoku.struktura.SorOszlopBlokk;
import java.util.BitSet;
import java.util.logging.Logger;

/**
 *
 * @author Nagy Gusztáv
 */
abstract class TéglalapTábla extends AbsztraktTábla {

    protected final int sorokSzáma;
    protected final int oszlopokSzáma;
    protected final int elemszám;

    private static final Logger LOG = Logger.getLogger(TéglalapTábla.class.getName());
    
    protected final BitSet TELJES_CELLA_BITSET = new BitSet();

    protected final Cella[][] cellák;

    public TéglalapTábla(int sorokSzáma, int oszlopokSzáma, int elemszám) {

        this.sorokSzáma = sorokSzáma;
        this.oszlopokSzáma = oszlopokSzáma;
        this.elemszám = elemszám;

        for (int i = 1; i <= elemszám; i++) {
            TELJES_CELLA_BITSET.set(i);
        }

        // Cellák létrehozása
        cellák = new Cella[sorokSzáma + 1][oszlopokSzáma + 1];
        for (int sor = 1; sor <= sorokSzáma; sor++) {
            for (int oszlop = 1; oszlop <= oszlopokSzáma; oszlop++) {
                cellák[sor][oszlop] = new Cella(sor + "x" + oszlop,
                        (BitSet) TELJES_CELLA_BITSET.clone(), this);
            }
        }

        // sorok
        for (int sor = 1; sor <= sorokSzáma; sor++) {
            SorOszlopBlokk sob = new SorOszlopBlokk(SorOszlopBlokk.Típus.SOR);
            for (int oszlop = 1; oszlop <= oszlopokSzáma; oszlop++) {
                sob.újCella(cellák[sor][oszlop]);
                cellák[sor][oszlop].újSorOszlopBlokk(sob);
            }
            sob.setTábla(this);
            sorOszlopBlokkok.add(sob);
        }

        // oszlopok
        for (int oszlop = 1; oszlop <= oszlopokSzáma; oszlop++) {
            SorOszlopBlokk sob = new SorOszlopBlokk(SorOszlopBlokk.Típus.OSZLOP);
            for (int sor = 1; sor <= sorokSzáma; sor++) {
                sob.újCella(cellák[sor][oszlop]);
                cellák[sor][oszlop].újSorOszlopBlokk(sob);
            }
            sob.setTábla(this);
            sorOszlopBlokkok.add(sob);
        }
    }

    @Override
    public void kiírTartalomEllenőrzéshez() {
        System.out.println("Helyére került: " + helyéreKerültElemekSzáma
                + " Hátra van: " + (elemszám * elemszám - helyéreKerültElemekSzáma));
        for (int sor = 1; sor <= sorokSzáma; sor++) {
            for (int oszlop = 1; oszlop <= oszlopokSzáma; oszlop++) {
                System.out.print(cellák[sor][oszlop].toString() + " ");
            }
            System.out.println();
        }
        System.out.println();
    }

    @Override
    public int elemszám() {
        return elemszám;
    }

    @Override
    public int cellaszám() {
        return sorokSzáma * oszlopokSzáma;
    }
}
