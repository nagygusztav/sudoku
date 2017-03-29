package hu.nagygusztav.sudoku.muvelet;

import hu.nagygusztav.sudoku.struktura.Cella;
import java.util.Objects;

/**
 * Az osztály példányai jelzik a
 *
 * @author nagy.gusztav
 */
public class LehetőségTörölveMűvelet extends AbsztraktMűvelet {

    private final Cella cella;
    private final int töröltLehetőség;

    public LehetőségTörölveMűvelet(Cella cella, int töröltLehetőség) {
        this.cella = cella;
        this.töröltLehetőség = töröltLehetőség;
    }

    public int getTöröltLehetőség() {
        return töröltLehetőség;
    }

    public Cella getCella() {
        return cella;
    }

    @Override
    public String toString() {
        return "Lehet\u0151s\u00e9gT\u00f6r\u00f6lveM\u0171velet{" + "cella=" + cella + ", t\u00f6r\u00f6ltLehet\u0151s\u00e9g=" + töröltLehetőség + '}';
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + Objects.hashCode(this.cella);
        hash = 29 * hash + this.töröltLehetőség;
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
        final LehetőségTörölveMűvelet other = (LehetőségTörölveMűvelet) obj;
        if (this.töröltLehetőség != other.töröltLehetőség) {
            return false;
        }
        if (!Objects.equals(this.cella, other.cella)) {
            return false;
        }
        return true;
    }

}
