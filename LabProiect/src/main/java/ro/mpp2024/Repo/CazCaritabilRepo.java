package ro.mpp2024.Repo;
import ro.mpp2024.Domain.CazCaritabil;
import java.util.Optional;

public interface CazCaritabilRepo extends Repo<Integer, CazCaritabil> {
    Optional<CazCaritabil> findByName(String name);
}