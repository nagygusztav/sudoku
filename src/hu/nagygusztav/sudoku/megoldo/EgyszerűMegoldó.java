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
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Nagy Gusztáv
 */
public class EgyszerűMegoldó extends AbsztraktMegoldó {

    private static final Logger LOG = Logger.getLogger(EgyszerűMegoldó.class.getName());

    /**
     * Megoldást keres elemi lépéseket követve.
     *
     * @param tábla
     */
    @Override
    public void megold(AbsztraktTábla tábla) {
        LOG.info("megold");

        int korábbiHelyéreKerültElemekSzáma = tábla.helyéreKerültElemekSzáma();
        while (!tábla.megoldva()) {

            // Műveletsor feldolgozása (NakedSingle)
            if (tábla.vanMégMűvelet()) {
                műveletFeldolgozó(tábla);
            }

            // HiddenSingle
            egyediCellaKeresés(tábla);

            // Intersection
            mettszetKeresés(tábla);

            if (korábbiHelyéreKerültElemekSzáma == tábla.helyéreKerültElemekSzáma()) { // nincs javulás
                LOG.info("Nem sikerült megoldani :-(");
                break;
            }
            korábbiHelyéreKerültElemekSzáma = tábla.helyéreKerültElemekSzáma();
        }
    }

    private void műveletFeldolgozó(AbsztraktTábla tábla) {
        AbsztraktMűvelet művelet = tábla.következőMűvelet();
        while (művelet != null) {
            if (művelet instanceof LehetőségTörölveMűvelet) { // NakedSingle
                LehetőségTörölveMűvelet lehetőségTörölveMűvelet = (LehetőségTörölveMűvelet) művelet;
                lehetőségekTörlése(lehetőségTörölveMűvelet);
                if (!Tesztelő.érvényesE(tábla)) {
                    return;
                }
            }
            LOG.info(tábla.tartalomEllenőrzéshez());
            művelet = tábla.következőMűvelet();
        }
    }

    private void lehetőségekTörlése(LehetőségTörölveMűvelet lehetőségTörölveMűvelet) {
        Cella cellaTörölt = lehetőségTörölveMűvelet.getCella();
        int töröltLehetőség = lehetőségTörölveMűvelet.getTöröltLehetőség();
        if (cellaTörölt.lehetMég(töröltLehetőség)) {
            for (Iterator<SorOszlopBlokk> iSob = cellaTörölt.sorOszlopBlokkBejáró(); iSob.hasNext();) {
                SorOszlopBlokk blokk = iSob.next();
                for (Iterator<Cella> iCella = blokk.cellaBejáró(); iCella.hasNext();) {
                    Cella cellaTörölhető = iCella.next();
                    if (cellaTörölhető.töröl(töröltLehetőség)) {
                        ;
                    }
                }
            }
            cellaTörölt.törölMindenLehetőséget();
        }
    }

    /**
     * Van olyan sor/oszlop/blokk, amiben egyedi elem van? (HiddenSingle)
     *
     * @param tábla
     */
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
                        LOG.log(Level.INFO, "Van egyedi cella ({0} / {1}) itt: {2}", new Object[]{cella, i, sorOszlopBlokk});
                        cella.kitölt(i);
                        egyediCellák.add(cella);
                    }
                }
            }
        }
    }

    /**
     * Ha egy háznak csak egy másik házzal alkotott mettszetében található meg
     * egy jelölt, akkor a másik ház mettszeten kívüli részéből törölhető a
     * jelölt.
     *
     * @param tábla
     */
    private void mettszetKeresés(AbsztraktTábla tábla) {

        // párosítás: minden mindennel, de önmagával nem
        for (Iterator<SorOszlopBlokk> egyikBejáró = tábla.sorOszlopBlokkokBejáró(); egyikBejáró.hasNext();) {
            SorOszlopBlokk egyikSorOszlopBlokk = egyikBejáró.next();
            for (Iterator<SorOszlopBlokk> másikBejáró = tábla.sorOszlopBlokkokBejáró(); másikBejáró.hasNext();) {
                SorOszlopBlokk másikSorOszlopBlokk = másikBejáró.next();
                if (!egyikSorOszlopBlokk.equals(másikSorOszlopBlokk)) {
                    Set<Cella> egyikCellák = egyikSorOszlopBlokk.getCellák();
                    Set<Cella> másikCellák = másikSorOszlopBlokk.getCellák();
                    Set<Cella> mettszetCellák = new HashSet<>(egyikCellák);
                    mettszetCellák.retainAll(másikCellák);
                    if (mettszetCellák.size() > 1) {

                        Set<Cella> egyikKülönbség = new HashSet<>(egyikCellák);
                        egyikKülönbség.removeAll(másikCellák);

                        Set<Cella> másikKülönbség = new HashSet<>(másikCellák);
                        másikKülönbség.removeAll(egyikCellák);

                        for (int jelölt = 1; jelölt <= tábla.elemszám(); jelölt++) {
                            if (benneVanACellákban(mettszetCellák, jelölt)) {
//                                System.out.println(mettszetCellák);
                                if (benneVanACellákban(egyikKülönbség, jelölt)) {
                                    törölCellákból(másikKülönbség, jelölt);
                                } else if (benneVanACellákban(másikKülönbség, jelölt)) {
                                    törölCellákból(egyikKülönbség, jelölt);
                                }
                            }
                        }
                    }
                }
            }
        }

        // mettszetek képzése
    }

    private boolean benneVanACellákban(Set<Cella> cellák, int jelölt) {
//        for (Cella cella : cellák) {
//            if (cella.lehetMég(jelölt)) {
//                return true;
//            }
//        }
        if (cellák.stream().anyMatch((cella) -> (cella.lehetMég(jelölt)))) {
            return true;
        }
        return false;
    }

    private void törölCellákból(Set<Cella> cellák, int jelölt) {
        for (Cella cella : cellák) {
            cella.töröl(jelölt);
        }
    }
}
