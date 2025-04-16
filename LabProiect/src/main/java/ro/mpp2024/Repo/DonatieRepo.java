package ro.mpp2024.Repo;
import ro.mpp2024.Domain.Donatie;
import java.util.List;

public interface DonatieRepo extends Repo<Integer, Donatie> {
    List<Donatie> findByDonatorId(Integer donatorId);
}
