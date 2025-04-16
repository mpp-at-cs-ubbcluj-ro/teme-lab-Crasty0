package ro.mpp2024.Repo;
import ro.mpp2024.Domain.Voluntar;

import java.util.Optional;

public interface VoluntarRepo extends Repo<Integer, Voluntar> {
    Voluntar findByUsername(String username);
}
