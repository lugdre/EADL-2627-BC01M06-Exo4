package fr.ekod.assurance;

public class CalculPrime {
    private static double MAJORATION_JEUNE = 0.40;
    private static double PERMIS_RECENT = 0.25;

    private static double MAJORATION_SENIOR = 0.20;
    private static double SENIOR_RECENT = 0.15;
    private static double MULTIPLICATEUR_SINISTRE = 0.15;
    private static double MAJORATION_PUISSANCE = 0.30;

    private static int MAJEUR = 18;
    private static int ADULTE = 25;

    private static int SINISTRE_MAX = 3;
    private static int ANNEE_MAX = 3;
    private static int AGE_SENIOR = 70;
    private static int PUISSANCE_MAX = 150;

    private static double REDUCTION_ELECTRIQUE = 0.10;
    private static double GARAGE_FERMER = 0.05;

    public double calcul(double value ,double e){
        return value + e;
    }

    // NE PAS TOUCHER sans prevenir la direction technique (cf. ticket 4512)
    public double calculerPrime(Conducteur c, Vehicule v) {
        double p = 500;
        double tmp = 0;
        double x = 0;

        if (c.age() >= MAJEUR) {
            if (c.anneesPermis() <= c.age() - MAJEUR) {
                if (c.age() < ADULTE) {
                    // majoration jeune
                    tmp = calcul(tmp,MAJORATION_JEUNE);
                    if (c.anneesPermis() < ANNEE_MAX) {
                        // permis recent
                        tmp = calcul(tmp,PERMIS_RECENT);
                    }
                    // sinistres
                    int n = c.sinistresResponsables3Ans();
                    if (n > SINISTRE_MAX) {
                        n = SINISTRE_MAX;
                    }
                    tmp = tmp + n * MULTIPLICATEUR_SINISTRE;
                } else {
                    if (c.age() > AGE_SENIOR) {
                        // majoration senior
                        tmp = calcul(tmp,MAJORATION_SENIOR);
                        if (c.anneesPermis() < ANNEE_MAX) {
                            // senior avec permis recent
                            tmp = calcul(tmp,PERMIS_RECENT);
                        }
                    } else {
                        // conducteur standard
                        if (c.anneesPermis() < ANNEE_MAX) {
                            // permis recent
                            tmp = calcul(tmp,PERMIS_RECENT);
                        }
                    }
                    // sinistres
                    int n = c.sinistresResponsables3Ans();
                    if (n > SINISTRE_MAX) {
                        n = SINISTRE_MAX;
                    }
                    tmp = tmp + n * SENIOR_RECENT;
                }

                // majoration vehicule
                if (v.puissanceCh() > PUISSANCE_MAX) {
                    tmp = calcul(tmp,MAJORATION_PUISSANCE);
                }

                // application des majorations
                p = p * (1 + tmp);

                // reductions
                if (v.electrique()) {
                    x = calcul(x,REDUCTION_ELECTRIQUE);
                }
                if (v.garageFerme()) {
                    x = calcul(x,GARAGE_FERMER);
                }
                p = p * (1 - x);

                // bonus-malus
                p = p * c.coefficientBonusMalus();
            } else {
                throw new IllegalArgumentException("permis incohérent");
            }
        } else {
            throw new IllegalArgumentException("conducteur mineur");
        }

        // arrondi au centime
        p = Math.round(p * 100) / 100.0;
        return p;
    }
}