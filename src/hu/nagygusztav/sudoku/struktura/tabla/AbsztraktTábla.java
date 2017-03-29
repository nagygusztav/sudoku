package hu.nagygusztav.sudoku.struktura.tabla;

import hu.nagygusztav.sudoku.muvelet.AbsztraktMűvelet;
import hu.nagygusztav.sudoku.struktura.SorOszlopBlokk;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Nagy Gusztáv
 */
public abstract class AbsztraktTábla {

    public abstract int elemszám();

    public abstract int cellaszám();

    public abstract void kiírTartalomEllenőrzéshez();

    protected final List<AbsztraktMűvelet> műveletSor = new LinkedList<>();
    protected final List<SorOszlopBlokk> sorOszlopBlokkok = new ArrayList<>();

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
        System.out.println("Várakozó műveletek:");
        műveletSor.forEach((művelet) -> {
            System.out.println(művelet);
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
