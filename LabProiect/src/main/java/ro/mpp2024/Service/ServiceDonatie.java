package ro.mpp2024.Service;

import ro.mpp2024.Domain.Donatie;
import ro.mpp2024.Repo.DonatieDBRepo;

import java.util.List;
import java.util.Optional;

public class ServiceDonatie {
    private DonatieDBRepo DonatieRepo;

    public ServiceDonatie(DonatieDBRepo DonatieRepo) {
        this.DonatieRepo = DonatieRepo;
    }

    public Iterable<Donatie> getAllDonatie() {
        return DonatieRepo.getAll();
    }

    public Optional<Donatie> getDonatieById(int id) {
        return Optional.ofNullable(DonatieRepo.getById(id));
    }

    public Donatie saveDonatie(Donatie donatie) {
        return DonatieRepo.save(donatie);
    }

    public Donatie deleteDonatie(int id) {
        return DonatieRepo.delete(id);
    }

    public Donatie updateDonatie(Donatie donatie) {
        return DonatieRepo.update(donatie);
    }

    public List<Donatie> findByDonatorId(Integer id) {
        return DonatieRepo.findByDonatorId(id);
    }
}