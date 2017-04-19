package hu.nagygusztav.sudoku.megoldo;

import hu.nagygusztav.sudoku.struktura.Cella;
import hu.nagygusztav.sudoku.struktura.SorOszlopBlokk;
import hu.nagygusztav.sudoku.struktura.tabla.AbsztraktTábla;
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

            // Alaplépés            
            LOG.info("alaplépés előtt\ntábla:\n" + tábla.toString());
            ismertCellaKeresés(tábla);
            LOG.info("alaplépés után\ntábla:\n" + tábla.toString());

            // Naked Single
            egyJelöltesCellaKeresés(tábla);
            LOG.info("egyjelöltes keresés után\ntábla:\n" + tábla.toString());

            // HiddenSingle
            egyediCellaKeresés(tábla);
            LOG.info("egyedi cella keresés után\ntábla:\n" + tábla.toString());

            // Pointing Pair; Box Line Reduction
            mettszetKeresés(tábla);
            LOG.info("mettszet keresés után\ntábla:\n" + tábla.toString());

            // Naked Pair
            párKeresés(tábla);
            LOG.info("pár keresés után\ntábla:\n" + tábla.toString());

            // Hidden Pair
            rejtettpárKeresés(tábla);
            LOG.info("rejtett pár keresés után\ntábla:\n" + tábla.toString());

            if (korábbiHelyéreKerültElemekSzáma == tábla.helyéreKerültElemekSzáma()) { // nincs javulás
                LOG.info("Nem sikerült megoldani :-(");
                break;
            }
            korábbiHelyéreKerültElemekSzáma = tábla.helyéreKerültElemekSzáma();
        }
    }

    /**
     * Amennyiben egy cella értéke ismert, nincsenek jelöltjei, a vele egy
     * sorban, oszlopban és blokkban lévő üres cellák jelöltjei közül
     * eltávolíthatjuk ezt az értéket. Sorfolytonosan bejárjuk a táblát, ha
     * olyan cellához érünk, aminek van értéke, akkor azt az értéket a cellával
     * egy sorba, oszlopba, vagy blokkba, azaz egy házba tartozó mezők jelöltjei
     * közül eltávolítjuk.
     *
     * @param tábla
     */
    private void ismertCellaKeresés(AbsztraktTábla tábla) {
        for (Iterator<Cella> iCella = tábla.cellaBejáró(); iCella.hasNext();) {
            Cella cella = iCella.next();
            if (cella.kitöltve() && cella.jelöltekSzáma() > 0) {
                int adat = cella.getAdat();
                for (int i = 1; i <= tábla.elemszám(); i++) {
                    if (i != adat && cella.lehetMég(i)) {
                        cella.törölLehetőség(i);
                        ismertCella(cella, adat);
                    }
                }
                cella.törölLehetőség(adat);
            }
        }
    }

    private void ismertCella(Cella cella, int adat) {
        for (Iterator<SorOszlopBlokk> iSob = cella.sorOszlopBlokkBejáró(); iSob.hasNext();) {
            SorOszlopBlokk sob = iSob.next();
            sob.törölLehetőség(adat);
        }
    }

    /**
     * Olyan mezőket keresünk, amelyekben csak egyetlen jelölt van, ekkor ezt az
     * értéket veszi fel a mező.
     *
     * @param tábla
     */
    private void egyJelöltesCellaKeresés(AbsztraktTábla tábla) {
        for (Iterator<Cella> iCella = tábla.cellaBejáró(); iCella.hasNext();) {
            Cella cella = iCella.next();
            if (cella.nincsKitöltve() && cella.jelöltekSzáma() == 1) {
                LOG.log(Level.INFO, "Egy jelöltes cella: {0}", cella);
                egyJelöltesCella(cella);
            }
        }
    }

    private void egyJelöltesCella(Cella cella) {
        int adat = cella.kitöltEgyetlenSzám();
        for (Iterator<SorOszlopBlokk> iSob = cella.sorOszlopBlokkBejáró(); iSob.hasNext();) {
            SorOszlopBlokk sob = iSob.next();
            sob.törölLehetőség(adat);
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
                        LOG.log(Level.INFO, "Egyedi cella ({0} / {1}) itt: {2}", new Object[]{cella, i, sorOszlopBlokk});
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
                                LOG.log(Level.INFO, "Mettszet jelölt: {0}, cellák: {1}", new Object[]{jelölt, mettszetCellák});
                                if (!benneVanACellákban(egyikKülönbség, jelölt)) {
                                    törölCellákból(másikKülönbség, jelölt);
                                } else if (!benneVanACellákban(másikKülönbség, jelölt)) {
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
        if (cellák.stream().anyMatch((cella) -> (cella.lehetMég(jelölt)))) {
            return true;
        }
        return false;
    }

    private void törölCellákból(Set<Cella> cellák, int jelölt) {
        for (Cella cella : cellák) {
            cella.törölLehetőség(jelölt);
        }
    }

    /**
     * Olyan cella párokat keresünk, amelyek egy házban találhatók, két
     * lehetséges értéket tartalmaznak és ugyanazt a kettőt. Amennyiben létezik
     * ilyen pár, a két érték mindenképp azokban a mezőkben lesznek, így ezek az
     * értékek a házon beül eltávolíthatók a többi cella jelöltjei közül.
     *
     * @param tábla
     */
    private void párKeresés(AbsztraktTábla tábla) {
        // minden ház bejárása
        for (Iterator<SorOszlopBlokk> iSob = tábla.sorOszlopBlokkokBejáró(); iSob.hasNext();) {
            SorOszlopBlokk sob = iSob.next();

            // minden lehetséges párosítás, önmagán kívül
            for (Iterator<Cella> iEgyik = sob.cellaBejáró(); iEgyik.hasNext();) {
                Cella egyikCella = iEgyik.next();
                if (egyikCella.jelöltekSzáma() == 2) {
                    for (Iterator<Cella> iMásik = sob.cellaBejáró(); iMásik.hasNext();) {
                        Cella másikCella = iMásik.next();
                        Set<Integer> közösJelöltek = new TreeSet<>();
                        if (egyikCella != másikCella
                                && másikCella.jelöltekSzáma() == 2
                                && pontosanUgyanazokAJelöltekVannak(egyikCella, másikCella, közösJelöltek)) {
                            LOG.log(Level.INFO, "Közös jelöltek itt: {0} és {1}: {2}", new Object[]{egyikCella, másikCella, közösJelöltek});

                            // jelöltek törlése a ház többi cellájából:
                            for (Iterator<Cella> iTörlendő = sob.cellaBejáró(); iTörlendő.hasNext();) {
                                Cella törlendőCella = iTörlendő.next();
                                if (törlendőCella != egyikCella && törlendőCella != másikCella) {
                                    törlendőCella.törölLehetőségek(közösJelöltek);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private boolean pontosanUgyanazokAJelöltekVannak(
            Cella egyikCella, Cella másikCella, Set<Integer> közösJelöltek) {
        for (int i = 1; i <= egyikCella.lehetőségekSzáma(); i++) {
            if (egyikCella.lehetMég(i) != másikCella.lehetMég(i)) {
                return false;
            } else if (egyikCella.lehetMég(i)) {
                közösJelöltek.add(i);
            }
        }
        return true;
    }

    /**
     * A cél olyan két cellát találni, ami tartalmaz két jelöltet, egy házban
     * vannak és ezen a házon belül egyetlen más cella sem tartalmazza a két
     * jelölt bármelyikét. Találat esetén nem tudjuk még megmondani, hogy melyik
     * mező melyik értéket fogja tartalmazni, de a két cellának mindenképpen
     * tartalmaznia kell a két jelölt egyikét, ezért azokból a cellákból
     * eltávolíthatjuk a többi lehetséges értéket.
     *
     * @param tábla
     */
    private void rejtettpárKeresés(AbsztraktTábla tábla) {
        // minden ház bejárása
        for (Iterator<SorOszlopBlokk> iSob = tábla.sorOszlopBlokkokBejáró(); iSob.hasNext();) {
            SorOszlopBlokk sob = iSob.next();
            List<Cella>[] jelöltElőfordulások = sob.jelöltElőfordulásokKeresése();
            for (int egyikJelölt = 1; egyikJelölt < jelöltElőfordulások.length; egyikJelölt++) {
                if (jelöltElőfordulások[egyikJelölt].size() == 2) {
                    for (int másikJelölt = egyikJelölt + 1; másikJelölt < jelöltElőfordulások.length; másikJelölt++) {
                        if (jelöltElőfordulások[másikJelölt].size() == 2
                                && jelöltElőfordulások[egyikJelölt].equals(jelöltElőfordulások[másikJelölt])) {
                            LOG.log(Level.INFO, "Rejtett pár ({0} és {1}): {2}",
                                    new Object[]{egyikJelölt, másikJelölt, jelöltElőfordulások[egyikJelölt]});
                            
                            Set<Integer> maradóJelöltek = new TreeSet<>();
                            maradóJelöltek.add(egyikJelölt);
                            maradóJelöltek.add(másikJelölt);
                            
                            jelöltElőfordulások[egyikJelölt].get(0).törölMásLehetőségek(maradóJelöltek);
                        }
                    }
                }
            }
        }
    }
}
