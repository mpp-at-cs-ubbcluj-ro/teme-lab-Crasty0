package ro.mpp2024.Service;

import ro.mpp2024.Domain.Donator;
import ro.mpp2024.Repo.DonatorDBRepo;

import java.util.Optional;

public class ServiceDonator {
    private DonatorDBRepo donatorRepo;

    public ServiceDonator(DonatorDBRepo donatorRepo) {
        this.donatorRepo = donatorRepo;
    }

    public Iterable<Donator> getAllDonatori() {
        return donatorRepo.getAll();
    }

    public Optional<Donator> getDonatorById(int id) {
        return Optional.ofNullable(donatorRepo.getById(id));
    }

    public Donator saveDonator(Donator donator) {
        return donatorRepo.save(donator);
    }

    public Donator deleteDonator(int id) {
        return donatorRepo.delete(id);
    }

    public Donator updateDonator(Donator donator) {
        return donatorRepo.update(donator);
    }

    public Optional<Donator> findByNume(String nume){
        return donatorRepo.findByNume(nume);
    }
}