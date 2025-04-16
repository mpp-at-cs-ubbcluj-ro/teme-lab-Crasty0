package ro.mpp2024.Service;

import ro.mpp2024.Domain.CazCaritabil;
import ro.mpp2024.Repo.CazCaritabilDBRepo;

import java.util.Optional;

public class ServiceCazCaritabil {
    private CazCaritabilDBRepo cazCaritabilRepo;

    public ServiceCazCaritabil(CazCaritabilDBRepo cazCaritabilRepo) {
        this.cazCaritabilRepo = cazCaritabilRepo;
    }

    public Iterable<CazCaritabil> getAllCazCaritabil() {
        return cazCaritabilRepo.getAll();
    }

    public Optional<CazCaritabil> getCazCaritabilById(int id) {
        return Optional.ofNullable(cazCaritabilRepo.getById(id));
    }

    public CazCaritabil saveCazCaritabil(CazCaritabil cazCaritabil) {
        return cazCaritabilRepo.save(cazCaritabil);
    }

    public CazCaritabil deleteCazCaritabil(int id) {
        return cazCaritabilRepo.delete(id);
    }

    public CazCaritabil updateCazCaritabil(CazCaritabil cazCaritabil) {
        return cazCaritabilRepo.update(cazCaritabil);
    }
}