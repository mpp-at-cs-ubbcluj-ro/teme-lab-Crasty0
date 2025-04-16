package ro.mpp2024.Repo;
import ro.mpp2024.Domain.Donator;
import java.util.Optional;

public interface DonatorRepo extends Repo<Integer, Donator>{
    Optional<Donator> findByNume (String nume);
}
