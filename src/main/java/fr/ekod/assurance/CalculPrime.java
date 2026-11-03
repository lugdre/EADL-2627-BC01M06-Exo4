package fr.ekod.assurance;

public class CalculPrime {

    private static final double PRIME_BASE = 500.00;

    private static final int AGE_MINIMUM = 18;
    private static final int AGE_JEUNE_CONDUCTEUR = 25;
    private static final int AGE_SENIOR = 70;
    private static final double MAJORATION_JEUNE = 0.40;
    private static final double MAJORATION_SENIOR = 0.20;

    private static final int ANCIENNETE_PERMIS_RECENT = 3;
    private static final double MAJORATION_PERMIS_RECENT = 0.25;

    private static final int PLAFOND_SINISTRES = 3;
    private static final double MAJORATION_PAR_SINISTRE = 0.15;

    private static final int PUISSANCE_ELEVEE_CH = 150;
    private static final double MAJORATION_FORTE_PUISSANCE = 0.30;

    private static final double REDUCTION_ELECTRIQUE = 0.10;
    private static final double REDUCTION_GARAGE_FERME = 0.05;

    private static final double CENTIMES_PAR_EURO = 100;

    public double calculerPrime(Conducteur conducteur, Vehicule vehicule) {
        if (conducteur.age() < AGE_MINIMUM) {
            throw new IllegalArgumentException("conducteur mineur");
        }
        if (conducteur.anneesPermis() > conducteur.age() - AGE_MINIMUM) {
            throw new IllegalArgumentException("permis incohérent");
        }

        double prime = PRIME_BASE;
        double majorations = 0;
        double reductions = 0;

        if (conducteur.age() < AGE_JEUNE_CONDUCTEUR) {
            majorations = majorations + MAJORATION_JEUNE;
        } else if (conducteur.age() > AGE_SENIOR) {
            majorations = majorations + MAJORATION_SENIOR;
        }

        if (conducteur.anneesPermis() < ANCIENNETE_PERMIS_RECENT) {
            majorations = majorations + MAJORATION_PERMIS_RECENT;
        }

        int sinistres = conducteur.sinistresResponsables3Ans();
        if (sinistres > PLAFOND_SINISTRES) {
            sinistres = PLAFOND_SINISTRES;
        }
        majorations = majorations + sinistres * MAJORATION_PAR_SINISTRE;

        if (vehicule.puissanceCh() > PUISSANCE_ELEVEE_CH) {
            majorations = majorations + MAJORATION_FORTE_PUISSANCE;
        }

        prime = prime * (1 + majorations);

        if (vehicule.electrique()) {
            reductions = reductions + REDUCTION_ELECTRIQUE;
        }
        if (vehicule.garageFerme()) {
            reductions = reductions + REDUCTION_GARAGE_FERME;
        }
        prime = prime * (1 - reductions);

        prime = prime * conducteur.coefficientBonusMalus();

        prime = Math.round(prime * CENTIMES_PAR_EURO) / CENTIMES_PAR_EURO;
        return prime;
    }
}
