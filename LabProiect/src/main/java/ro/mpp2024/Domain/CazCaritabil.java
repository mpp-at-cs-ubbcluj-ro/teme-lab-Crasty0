package ro.mpp2024.Domain;

public class CazCaritabil extends Entity<Integer>{

    private String nume;
    private Double sumaStransa;

    public CazCaritabil(Integer id, String nume, Double sumaStransa) {
        super(id);
        this.nume = nume;
        this.sumaStransa = sumaStransa;
    }

    public String getNume() {
        return nume;
    }

    public Double getSumaStransa() {
        return sumaStransa;
    }

    @Override
    public String toString() {
        return "CazCaritabil{" +
                "idCaz=" + getId() +
                ", nume='" + nume + '\'' +
                ", sumaStransa=" + sumaStransa +
                '}';
    }
}