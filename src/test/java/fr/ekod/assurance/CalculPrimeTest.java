package fr.ekod.assurance;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class CalculPrimeTest {

    private final CalculPrime calculPrime = new CalculPrime();

    @Test
    void un_conducteur_standard_paie_la_prime_de_base_de_500_euros() {
        Conducteur conducteur = new Conducteur(30, 10, 0, 1.00);
        Vehicule vehicule = new Vehicule(110, false, false);

        double prime = calculPrime.calculerPrime(conducteur, vehicule);

        assertEquals(500.00, prime, 0.001);
    }

    @Test
    void un_jeune_conducteur_au_permis_recent_cumule_les_majorations_age_et_permis() {
        Conducteur conducteur = new Conducteur(22, 2, 0, 1.00);
        Vehicule vehicule = new Vehicule(100, false, false);

        double prime = calculPrime.calculerPrime(conducteur, vehicule);

        assertEquals(825.00, prime, 0.001);
    }

    @Test
    void un_permis_de_moins_de_3_ans_ajoute_25_pour_cent() {
        Conducteur conducteur = new Conducteur(30, 2, 0, 1.00);
        Vehicule vehicule = new Vehicule(110, false, false);

        double prime = calculPrime.calculerPrime(conducteur, vehicule);

        assertEquals(625.00, prime, 0.001);
    }

    @Test
    void un_conducteur_de_plus_de_70_ans_ajoute_20_pour_cent() {
        Conducteur conducteur = new Conducteur(73, 30, 0, 1.00);
        Vehicule vehicule = new Vehicule(100, false, false);

        double prime = calculPrime.calculerPrime(conducteur, vehicule);

        assertEquals(600.00, prime, 0.001);
    }

    @Test
    void un_sinistre_responsable_ajoute_15_pour_cent() {
        Conducteur conducteur = new Conducteur(30, 10, 1, 1.00);
        Vehicule vehicule = new Vehicule(110, false, false);

        double prime = calculPrime.calculerPrime(conducteur, vehicule);

        assertEquals(575.00, prime, 0.001);
    }

    @Test
    void trois_sinistres_responsables_ajoutent_45_pour_cent() {
        Conducteur conducteur = new Conducteur(30, 10, 3, 1.00);
        Vehicule vehicule = new Vehicule(110, false, false);

        double prime = calculPrime.calculerPrime(conducteur, vehicule);

        assertEquals(725.00, prime, 0.001);
    }

    @Test
    void quatre_sinistres_responsables_sont_plafonnes_a_trois() {
        Conducteur conducteur = new Conducteur(30, 10, 4, 1.00);
        Vehicule vehicule = new Vehicule(110, false, false);

        double prime = calculPrime.calculerPrime(conducteur, vehicule);

        assertEquals(725.00, prime, 0.001);
    }

    @Test
    void un_vehicule_de_plus_de_150_chevaux_ajoute_30_pour_cent() {
        Conducteur conducteur = new Conducteur(30, 10, 0, 1.00);
        Vehicule vehicule = new Vehicule(200, false, false);

        double prime = calculPrime.calculerPrime(conducteur, vehicule);

        assertEquals(650.00, prime, 0.001);
    }

    @Test
    void un_vehicule_electrique_en_garage_ferme_cumule_les_deux_reductions() {
        Conducteur conducteur = new Conducteur(40, 15, 0, 0.80);
        Vehicule vehicule = new Vehicule(120, true, true);

        double prime = calculPrime.calculerPrime(conducteur, vehicule);

        assertEquals(340.00, prime, 0.001);
    }

    @Test
    void un_bonus_de_0_50_divise_la_prime_par_deux() {
        Conducteur conducteur = new Conducteur(30, 10, 0, 0.50);
        Vehicule vehicule = new Vehicule(110, false, false);

        double prime = calculPrime.calculerPrime(conducteur, vehicule);

        assertEquals(250.00, prime, 0.001);
    }

    @Test
    void un_malus_de_3_50_multiplie_la_prime_par_trois_et_demi() {
        Conducteur conducteur = new Conducteur(30, 10, 0, 3.50);
        Vehicule vehicule = new Vehicule(110, false, false);

        double prime = calculPrime.calculerPrime(conducteur, vehicule);

        assertEquals(1750.00, prime, 0.001);
    }

    @Test
    void un_nombre_dannees_de_permis_superieur_a_lage_moins_18_est_refuse() {
        Conducteur conducteur = new Conducteur(30, 20, 0, 1.00);
        Vehicule vehicule = new Vehicule(110, false, false);

        IllegalArgumentException erreur = assertThrows(IllegalArgumentException.class,
                () -> calculPrime.calculerPrime(conducteur, vehicule));

        assertEquals("permis incohérent", erreur.getMessage());
    }

    @Test
    void un_conducteur_mineur_est_refuse() {
        Conducteur conducteur = new Conducteur(17, 0, 0, 1.00);
        Vehicule vehicule = new Vehicule(110, false, false);

        IllegalArgumentException erreur = assertThrows(IllegalArgumentException.class,
                () -> calculPrime.calculerPrime(conducteur, vehicule));

        assertEquals("conducteur mineur", erreur.getMessage());
    }
}
