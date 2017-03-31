package hu.nagygusztav.sudoku.struktura.tabla;

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

//    protected final BitSet TELJES_CELLA_BITSET = new BitSet();

    protected final Cella[][] cellák;

    public TéglalapTábla(int sorokSzáma, int oszlopokSzáma, int elemszám) {

        this.sorokSzáma = sorokSzáma;
        this.oszlopokSzáma = oszlopokSzáma;
        this.elemszám = elemszám;

//        for (int i = 1; i <= elemszám; i++) {
//            TELJES_CELLA_BITSET.set(i);
//        }

        // Cellák létrehozása
        cellák = new Cella[sorokSzáma + 1][oszlopokSzáma + 1];
        for (int sor = 1; sor <= sorokSzáma; sor++) {
            for (int oszlop = 1; oszlop <= oszlopokSzáma; oszlop++) {
                cellák[sor][oszlop] = new Cella(sor + "x" + oszlop, elemszám, this);
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
    public String tartalomEllenőrzéshez() {
        StringBuilder író = new StringBuilder();
        író.append("Helyére került: ").append(helyéreKerültElemekSzáma)
                .append("\n Hátra van: ").append(elemszám * elemszám - helyéreKerültElemekSzáma)
                .append("\n");

        for (int sor = 1; sor <= sorokSzáma; sor++) {
            for (int oszlop = 1; oszlop <= oszlopokSzáma; oszlop++) {
                író.append(cellák[sor][oszlop].toString()).append(" ");
            }
            író.append('\n');
        }
        író.append('\n');
        return író.toString();
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
