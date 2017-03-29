package hu.nagygusztav.sudoku.megoldo;

import hu.nagygusztav.sudoku.muvelet.AbsztraktMűvelet;
import hu.nagygusztav.sudoku.muvelet.LehetőségTörölveMűvelet;
import hu.nagygusztav.sudoku.struktura.Cella;
import hu.nagygusztav.sudoku.struktura.SorOszlopBlokk;
import hu.nagygusztav.sudoku.struktura.tabla.AbsztraktTábla;
import hu.nagygusztav.sudoku.tesztelo.Tesztelő;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 *
 * @author Nagy Gusztáv
 */
public class EgyszerűMegoldó extends AbsztraktMegoldó {

    /**
     * Megoldást keres elemi lépéseket követve.
     *
     * @param tábla
     */
    @Override
    public void megold(AbsztraktTábla tábla) {
        System.out.println("Megold:");

        int korábbiHelyéreKerültElemekSzáma = tábla.helyéreKerültElemekSzáma();
        while (!tábla.megoldva()) {

            // Műveletsor feldolgozása
            if (tábla.vanMégMűvelet()) {
                műveletFeldolgozó(tábla);
            }

            // Van olyan sor/oszlop/blokk, amiben egyedi elem van?
            egyediCellaKeresés(tábla);

            if (korábbiHelyéreKerültElemekSzáma == tábla.helyéreKerültElemekSzáma()) { // nincs javulás
                System.out.println("Nem sikerült megoldani :-(");
                break;
            }
            korábbiHelyéreKerültElemekSzáma = tábla.helyéreKerültElemekSzáma();
        }
    }

    private void műveletFeldolgozó(AbsztraktTábla tábla) {
        AbsztraktMűvelet művelet = tábla.következőMűvelet();
        while (művelet != null) {
            if (művelet instanceof LehetőségTörölveMűvelet) {
                LehetőségTörölveMűvelet lehetőségTörölveMűvelet = (LehetőségTörölveMűvelet) művelet;
                lehetőségekTörlése(lehetőségTörölveMűvelet);
                if (!Tesztelő.érvényesE(tábla)) {
                    return;
                }
            }
            tábla.kiírTartalomEllenőrzéshez();
            művelet = tábla.következőMűvelet();
        }
    }

    private void lehetőségekTörlése(LehetőségTörölveMűvelet lehetőségTörölveMűvelet) {
        Cella cellaTörölt = lehetőségTörölveMűvelet.getCella();
        int töröltLehetőség = lehetőségTörölveMűvelet.getTöröltLehetőség();
        if (cellaTörölt.lehetMég(töröltLehetőség)) {
            System.out.println("Művelet: " + lehetőségTörölveMűvelet);
            for (Iterator<SorOszlopBlokk> iSob = cellaTörölt.sorOszlopBlokkBejáró(); iSob.hasNext();) {
                SorOszlopBlokk blokk = iSob.next();
                System.out.println("Töröl SOB-ból: " + blokk + '\n');
                for (Iterator<Cella> iCella = blokk.cellaBejáró(); iCella.hasNext();) {
                    Cella cellaTörölhető = iCella.next();
                    if (cellaTörölhető.töröl(töröltLehetőség)) {
//                                tábla.kiírTartalomEllenőrzéshez();
                    }
                }
            }
            cellaTörölt.törölMindenLehetőséget();
        }
    }

    private void egyediCellaKeresés(AbsztraktTábla tábla) {

        // Minden cellát csak "egyszer szabad megtalálni"
        Set<Cella> egyediCellák = new HashSet<>();

        for (Iterator<SorOszlopBlokk> iSob = tábla.sorOszlopBlokkokBejáró(); iSob.hasNext();) {

            // előkészítés
            List<Cella>[] számokElőfordulása = new List[tábla.elemszám() + 1];
            for (int i = 1; i <= tábla.elemszám(); i++) {
                számokElőfordulása[i] = new LinkedList<>();
            }

            // sor/oszlop/blokkok bejárása
            SorOszlopBlokk sorOszlopBlokk = iSob.next();
            for (Iterator<Cella> iCella = sorOszlopBlokk.cellaBejáró(); iCella.hasNext();) {
                Cella cella = iCella.next();
                for (int i = 1; i <= tábla.elemszám(); i++) {
                    if (cella.lehetMég(i)) {
                        számokElőfordulása[i].add(cella);
                    }
                }
            }

            // van-e egyedi cella?
            for (int i = 1; i <= tábla.elemszám(); i++) {
                if (számokElőfordulása[i].size() == 1) {
                    Cella cella = számokElőfordulása[i].get(0);
                    if (!egyediCellák.contains(cella)) {
                        System.out.println("Van egyedi cella (" + cella + " / " + i + ") itt: " + sorOszlopBlokk);
                        cella.kitölt(i);
                        egyediCellák.add(cella);
                    }
                }
            }
        }
    }
}
