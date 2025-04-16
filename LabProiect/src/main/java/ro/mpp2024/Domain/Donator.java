package ro.mpp2024.Domain;

public class Donator extends Entity<Integer> {

    private String nume;
    private String adresa;
    private String telefon;

    public Donator(Integer id, String nume, String adresa, String telefon) {
        super(id);
        this.nume = nume;
        this.adresa = adresa;
        this.telefon = telefon;
    }

    public String getNume() {
        return nume;
    }

    public String getAdresa() {
        return adresa;
    }

    public String getTelefon() {
        return telefon;
    }

    @Override
    public String toString() {
        return "Donator{" +
                "id=" + getId() +
                ", nume='" + nume + '\'' +
                ", adresa='" + adresa + '\'' +
                ", telefon='" + telefon + '\'' +
                '}';
    }
}
