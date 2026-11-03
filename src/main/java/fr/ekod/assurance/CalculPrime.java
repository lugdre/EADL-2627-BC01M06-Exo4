package fr.ekod.assurance;

public class CalculPrime {

    // NE PAS TOUCHER sans prevenir la direction technique (cf. ticket 4512)
    public double calculerPrime(Conducteur c, Vehicule v) {
        double p = 500;
        double tmp = 0;
        double x = 0;

        if (c.age() >= 18) {
            if (c.anneesPermis() <= c.age() - 18) { //Smell jaune
                if (c.age() < 25) {
                    // majoration jeune
                    tmp = tmp + 0.40;
                    if (c.anneesPermis() < 3) { // Smell jaune
                        // permis recent
                        tmp = tmp + 0.25;
                    }
                    // sinistres
                    int n = c.sinistresResponsables3Ans();
                    if (n > 3) { // Smell jaune
                        n = 3; // Smell rouge
                    }
                    tmp = tmp + n * 0.15;
                } else {
                    if (c.age() > 70) {
                        // majoration senior
                        tmp = tmp + 0.20;
                        if (c.anneesPermis() < 3) { // Smell jaune
                            // senior avec permis recent
                            tmp = tmp + 0.25; // Smell rouge
                        }
                    } else {
                        // conducteur standard
                        if (c.anneesPermis() < 3) {
                            // permis recent
                            tmp = tmp + 0.25;
                        }
                    }
                    // sinistres
                    int n = c.sinistresResponsables3Ans();
                    if (n > 3) {
                        n = 3;
                    }
                    tmp = tmp + n * 0.15;
                }

                // majoration vehicule
                if (v.puissanceCh() > 150) {
                    tmp = tmp + 0.30;
                }

                // application des majorations
                p = p * (1 + tmp);

                // reductions
                if (v.electrique()) {
                    x = x + 0.10;
                }
                if (v.garageFerme()) {
                    x = x + 0.05;
                }
                p = p * (1 - x);

                // bonus-malus
                p = p * c.coefficientBonusMalus();
            } else {
                throw new IllegalArgumentException("permis incohérent"); // Smell rouge
            }
        } else {
            throw new IllegalArgumentException("conducteur mineur");
        }

        // arrondi au centime
        p = Math.round(p * 100) / 100.0;
        return p;
    }
}
