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
        verifierEligibilite(conducteur);

        double majorations = majorationAge(conducteur)
                + majorationPermisRecent(conducteur)
                + majorationSinistres(conducteur)
                + majorationVehicule(vehicule);

        double prime = PRIME_BASE
                * (1 + majorations)
                * (1 - reductions(vehicule))
                * conducteur.coefficientBonusMalus();

        return arrondiAuCentime(prime);
    }

    private void verifierEligibilite(Conducteur conducteur) {
        if (conducteur.age() < AGE_MINIMUM) {
            throw new IllegalArgumentException("conducteur mineur");
        }
        if (conducteur.anneesPermis() > conducteur.age() - AGE_MINIMUM) {
            throw new IllegalArgumentException("permis incohérent");
        }
    }

    private double majorationAge(Conducteur conducteur) {
        if (conducteur.age() < AGE_JEUNE_CONDUCTEUR) {
            return MAJORATION_JEUNE;
        }
        if (conducteur.age() > AGE_SENIOR) {
            return MAJORATION_SENIOR;
        }
        return 0;
    }

    private double majorationPermisRecent(Conducteur conducteur) {
        return conducteur.anneesPermis() < ANCIENNETE_PERMIS_RECENT
                ? MAJORATION_PERMIS_RECENT
                : 0;
    }

    private double majorationSinistres(Conducteur conducteur) {
        int sinistres = conducteur.sinistresResponsables3Ans();
        if (sinistres > PLAFOND_SINISTRES) {
            sinistres = PLAFOND_SINISTRES;
        }
        return sinistres * MAJORATION_PAR_SINISTRE;
    }

    private double majorationVehicule(Vehicule vehicule) {
        return vehicule.puissanceCh() > PUISSANCE_ELEVEE_CH
                ? MAJORATION_FORTE_PUISSANCE
                : 0;
    }

    private double reductions(Vehicule vehicule) {
        double reductions = 0;
        if (vehicule.electrique()) {
            reductions = reductions + REDUCTION_ELECTRIQUE;
        }
        if (vehicule.garageFerme()) {
            reductions = reductions + REDUCTION_GARAGE_FERME;
        }
        return reductions;
    }

    private double arrondiAuCentime(double montant) {
        return Math.round(montant * CENTIMES_PAR_EURO) / CENTIMES_PAR_EURO;
    }
}
