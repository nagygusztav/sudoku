package hu.nagygusztav.sudoku.struktura.tabla;

import hu.nagygusztav.sudoku.muvelet.AbsztraktMűvelet;
import hu.nagygusztav.sudoku.struktura.SorOszlopBlokk;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

/**
 *
 * @author Nagy Gusztáv
 */
public abstract class AbsztraktTábla {

    public abstract int elemszám();

    public abstract int cellaszám();

    public abstract String tartalomEllenőrzéshez();

    protected final List<AbsztraktMűvelet> műveletSor = new LinkedList<>();
    protected final List<SorOszlopBlokk> sorOszlopBlokkok = new ArrayList<>();
    private static final Logger LOG = Logger.getLogger(AbsztraktTábla.class.getName());

    protected int helyéreKerültElemekSzáma = 0;

    public Iterator<SorOszlopBlokk> sorOszlopBlokkokBejáró() {
        return sorOszlopBlokkok.iterator();
    }

    /**
     * A feladvány meg van oldva.
     *
     * @return
     */
    public boolean megoldva() {
        return cellaszám() <= helyéreKerültElemekSzáma;
    }

    /**
     * Tesztelés: a tábla struktúrája megfelelő-e
     *
     * @param művelet
     */
    public void újMűvelet(AbsztraktMűvelet művelet) {
        műveletSor.add(művelet);
    }

    public void kiírMűveletek() {
        LOG.info("Várakozó műveletek:");
        műveletSor.forEach((művelet) -> {
            LOG.info(művelet.toString());
        });
    }

    public AbsztraktMűvelet következőMűvelet() {
        if (műveletSor.isEmpty()) {
            return null;
        } else {
            return műveletSor.remove(0);
        }
    }

    public boolean vanMégMűvelet() {
        return !műveletSor.isEmpty();
    }

    public int helyéreKerültElemekSzáma() {
        return helyéreKerültElemekSzáma;
    }

    public void helyemreKerültem() {
        helyéreKerültElemekSzáma++;
    }
}
