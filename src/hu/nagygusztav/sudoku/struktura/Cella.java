package hu.nagygusztav.sudoku.struktura;

import hu.nagygusztav.sudoku.muvelet.LehetőségTörölveMűvelet;
import hu.nagygusztav.sudoku.struktura.tabla.AbsztraktTábla;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;

/**
 *
 * @author Nagy Gusztáv
 */
public class Cella {

    private static final Logger LOG = Logger.getLogger(Cella.class.getName());

    public static final int NINCSKITOLTVE = 0;

    private boolean kitöltve = false;
    private final String szövegesAzonosító;
    private final List<SorOszlopBlokk> sorOszlopBlokkokRésze = new ArrayList<>();
    private int adat = NINCSKITOLTVE;
    private final BitSet lehetőségek;
    private int lehetőségekSzáma;
    private final AbsztraktTábla tábla;

    /**
     * @param szövegesAzonosító
     * @param lehetőségekSzáma
     * @param tábla
     */
    public Cella(String szövegesAzonosító, int lehetőségekSzáma, AbsztraktTábla tábla) {
        this.szövegesAzonosító = szövegesAzonosító;
        lehetőségek = new BitSet();
        this.lehetőségekSzáma = lehetőségekSzáma;
        for (int i = 1; i <= lehetőségekSzáma; i++) {
            lehetőségek.set(i);
        }
        this.tábla = tábla;
    }

    public void újSorOszlopBlokk(SorOszlopBlokk új) {
        sorOszlopBlokkokRésze.add(új);
    }

    public Iterator<SorOszlopBlokk> sorOszlopBlokkBejáró() {
        return sorOszlopBlokkokRésze.iterator();
    }

    @Override
    public String toString() {
        StringBuilder építő = new StringBuilder();
        építő.append(szövegesAzonosító);
        építő.append(": ");
        építő.append(kitöltve ? "" + adat : "-");
        építő.append("(");
        for (int i = 1; i <= tábla.elemszám(); i++) {
            if (lehetőségek.get(i)) {
                építő.append(i);
            } else {
                építő.append(".");
            }
        }
        építő.append(")");

        return építő.toString();
    }

    public String getSzövegesAzonosító() {
        return szövegesAzonosító;
    }

    /**
     * A cella végleges tartalma.
     *
     * @return
     */
    public int getAdat() {
        return adat;
    }

    /**
     * Ha a cellának már nincsenek opcionális lehetőségei, akkor kitöltve van.
     *
     * @return
     */
    public boolean kitöltve() {
        return kitöltve;
    }

    /**
     * Ha a cellának még vannak opcionális lehetőségei, akkor nincs kitöltve.
     *
     * @return
     */
    public boolean nincsKitöltve() {
        return !kitöltve;
    }

    /**
     * Az eddigi lehetőségek megszűnnek, mert a szám lesz a megoldás.
     *
     * @param szám
     */
    public void kitölt(int szám) {
        if (kitöltve) {
            throw new IllegalArgumentException("Már ki van töltve!");
        }
        if (szám < 0 || szám > tábla.elemszám()/* || !lehetőségek.get(szám)*/) {
            throw new IllegalArgumentException("Nem jó szám: " + szám);
        }
        kitöltve = true;
        adat = szám;

        tábla.helyemreKerültem();
        tábla.újMűvelet(new LehetőségTörölveMűvelet(this, szám));
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + Objects.hashCode(this.szövegesAzonosító);
        hash = 97 * hash + Objects.hashCode(this.tábla);
        hash = 97 * hash + lehetőségekSzáma;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Cella other = (Cella) obj;
        if (lehetőségekSzáma != other.lehetőségekSzáma) {
            return false;
        }
        if (!Objects.equals(this.szövegesAzonosító, other.szövegesAzonosító)) {
            return false;
        }
        if (!Objects.equals(this.tábla, other.tábla)) {
            return false;
        }
        return true;
    }

    /**
     * A lehetőségek közül egyet kitörlünk.
     *
     * @param töröltLehetőség
     * @return Volt-e tényleges törlés.
     */
    public boolean töröl(int töröltLehetőség) {
        if (lehetőségek.get(töröltLehetőség)) {
            lehetőségek.clear(töröltLehetőség);
            if (!kitöltve && lehetőségek.cardinality() == 1) { // most lett kész!
                try {
                    kitölt(lehetőségek.nextSetBit(0));
                } catch (IllegalArgumentException ex) {
                    ;
                }
            }
            return true;
        } else {
            return false;
        }
    }

    /**
     * Még áll-e az adott lehetőség?
     *
     * @param lehetőség
     * @return
     */
    public boolean lehetMég(int lehetőség) {
        return lehetőségek.get(lehetőség);
    }

    /**
     * Minden lehetőséget kitörlünk.
     */
    public void törölMindenLehetőséget() {
        lehetőségek.clear();
    }
}
