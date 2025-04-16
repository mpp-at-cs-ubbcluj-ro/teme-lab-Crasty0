package ro.mpp2024;

import ro.mpp2024.Domain.CazCaritabil;
import ro.mpp2024.Repo.CazCaritabilDBRepo;

import java.io.FileReader;
import java.io.IOException;
import java.util.Optional;
import java.util.Properties;

public class Main {
    public static void main(String[] args) {
        Properties props=new Properties();
        try {
            props.load(new FileReader("bd.config"));
        } catch (IOException e) {
            System.out.println("Cannot find bd.config "+e);
        }

        CazCaritabilDBRepo repo = new CazCaritabilDBRepo(props);

        // Test save
        CazCaritabil caz1 = new CazCaritabil(1, "Charity Case 1", 1000.0);
        repo.save(caz1);

        // Test getById
        CazCaritabil retrievedCaz = repo.getById(1);
        System.out.println("Retrieved by ID: " + retrievedCaz);

        // Test findByName
        Optional<CazCaritabil> foundCaz = repo.findByName("Charity Case 1");
        foundCaz.ifPresent(caz -> System.out.println("Found by name: " + caz));

        // Test update
        CazCaritabil updatedCaz = new CazCaritabil(1, "Updated Charity Case 1", 2000.0);
        repo.update(updatedCaz);
        CazCaritabil updatedRetrievedCaz = repo.getById(1);
        System.out.println("Updated and retrieved by ID: " + updatedRetrievedCaz);

        // Test getAll
        Iterable<CazCaritabil> allCazuri = repo.getAll();
        System.out.println("All charitable cases:");
        allCazuri.forEach(System.out::println);

        // Test delete
        repo.delete(1);
        CazCaritabil deletedCaz = repo.getById(1);
        System.out.println("Deleted and tried to retrieve by ID: " + deletedCaz);
    }
}