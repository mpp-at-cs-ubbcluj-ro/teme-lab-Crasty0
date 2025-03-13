package ro.mpp2024.Domain;

public class Donatie extends Entity<Integer> {

    private Donator donator;
    private CazCaritabil caz;
    private float suma;

    public Donatie(Integer idDonatie, Donator donator, CazCaritabil caz, float suma) {
        super(idDonatie);
        this.donator = donator;
        this.caz = caz;
        this.suma = suma;
    }

    public Donator getDonator() {
        return donator;
    }

    public CazCaritabil getCaz() {
        return caz;
    }

    public float getSuma() {
        return suma;
    }

    @Override
    public String toString() {
        return "Donatie{" +
                "idDonatie=" + getId() +
                ", donator=" + donator +
                ", caz=" + caz +
                ", suma=" + suma +
                '}';
    }
}