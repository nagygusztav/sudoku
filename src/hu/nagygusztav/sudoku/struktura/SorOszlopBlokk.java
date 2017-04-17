package hu.nagygusztav.sudoku.struktura;

import hu.nagygusztav.sudoku.struktura.tabla.AbsztraktTábla;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 *
 * @author Nagy Gusztáv
 */
public final class SorOszlopBlokk {

    public enum Típus {
        SOR, OSZLOP, BLOKK
    }

    private AbsztraktTábla tábla;
    private final List<Cella> cellák;
    private final Típus típus;

    public SorOszlopBlokk(Típus típus) {
        this.típus = típus;
        cellák = new ArrayList<>();
    }

    public void újCella(Cella új) {
        cellák.add(új);
    }

    public Iterator<Cella> cellaBejáró() {
        return cellák.iterator();
    }

    public Típus getTípus() {
        return típus;
    }

    public void setTábla(AbsztraktTábla tábla) {
        this.tábla = tábla;
    }

    @Override
    public String toString() {
        return "SorOszlopBlokk{t\u00edpus=" + típus + ", cell\u00e1k=" + cellák + '}';
    }

    public Set<Cella> getCellák() {
        return new HashSet<>(cellák);
    }

    public void törölLehetőség(int lehetőség) {
        for (Cella cella : cellák) {
            if (cella.getAdat() != lehetőség) {
                cella.törölLehetőség(lehetőség);
            }
        }
    }
}
