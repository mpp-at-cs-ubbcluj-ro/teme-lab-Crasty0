package ro.mpp2024.Service;

import ro.mpp2024.Domain.Voluntar;
import ro.mpp2024.Repo.VoluntarDBRepo;

import java.util.Optional;

public class ServiceVoluntar {
    private VoluntarDBRepo voluntarRepo;

    public ServiceVoluntar(VoluntarDBRepo voluntarRepo) {
        this.voluntarRepo = voluntarRepo;
    }

    public Iterable<Voluntar> getAllVoluntari() {
        return voluntarRepo.getAll();
    }

    public Optional<Voluntar> getVoluntarById(int id) {
        return Optional.ofNullable(voluntarRepo.getById(id));
    }

    public Voluntar saveVoluntar(Voluntar voluntar) {
        return voluntarRepo.save(voluntar);
    }

    public Voluntar deleteVoluntar(int id) {
        return voluntarRepo.delete(id);
    }

    public Voluntar updateVoluntar(Voluntar voluntar) {
        return voluntarRepo.update(voluntar);
    }

    public Optional<Voluntar> findByUsername(String username) {
        return Optional.ofNullable(voluntarRepo.findByUsername(username));
    }
}