package fr.ekod.assurance;

public class CalculPrime {

    public double calculerPrime(Conducteur conducteur, Vehicule vehicule) {
        if (conducteur.age() < 18) {
            throw new IllegalArgumentException("conducteur mineur");
        }
        if (conducteur.anneesPermis() > conducteur.age() - 18) {
            throw new IllegalArgumentException("permis incohérent");
        }

        double prime = 500;
        double majorations = 0;
        double reductions = 0;

        if (conducteur.age() < 25) {
            // majoration jeune
            majorations = majorations + 0.40;
            if (conducteur.anneesPermis() < 3) {
                // permis recent
                majorations = majorations + 0.25;
            }
            // sinistres
            int sinistres = conducteur.sinistresResponsables3Ans();
            if (sinistres > 3) {
                sinistres = 3;
            }
            majorations = majorations + sinistres * 0.15;
        } else {
            if (conducteur.age() > 70) {
                // majoration senior
                majorations = majorations + 0.20;
                if (conducteur.anneesPermis() < 3) {
                    // senior avec permis recent
                    majorations = majorations + 0.25;
                }
            } else {
                // conducteur standard
                if (conducteur.anneesPermis() < 3) {
                    // permis recent
                    majorations = majorations + 0.25;
                }
            }
            // sinistres
            int sinistres = conducteur.sinistresResponsables3Ans();
            if (sinistres > 3) {
                sinistres = 3;
            }
            majorations = majorations + sinistres * 0.15;
        }

        // majoration vehicule
        if (vehicule.puissanceCh() > 150) {
            majorations = majorations + 0.30;
        }

        // application des majorations
        prime = prime * (1 + majorations);

        // reductions
        if (vehicule.electrique()) {
            reductions = reductions + 0.10;
        }
        if (vehicule.garageFerme()) {
            reductions = reductions + 0.05;
        }
        prime = prime * (1 - reductions);

        // bonus-malus
        prime = prime * conducteur.coefficientBonusMalus();

        // arrondi au centime
        prime = Math.round(prime * 100) / 100.0;
        return prime;
    }
}
