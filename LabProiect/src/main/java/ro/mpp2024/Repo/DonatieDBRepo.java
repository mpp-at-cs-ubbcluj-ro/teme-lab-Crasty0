package ro.mpp2024.Repo;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ro.mpp2024.Domain.Donatie;
import ro.mpp2024.Domain.Donator;
import ro.mpp2024.Domain.CazCaritabil;
import ro.mpp2024.Service.ServiceDonator;
import ro.mpp2024.Service.ServiceCazCaritabil;
import ro.mpp2024.Utils.JdbcUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class DonatieDBRepo implements DonatieRepo {

    private JdbcUtils dbUtils;
    private static final Logger logger = LogManager.getLogger();
    private ServiceDonator serviceDonator;
    private ServiceCazCaritabil serviceCazCaritabil;

    public DonatieDBRepo(Properties props, ServiceDonator serviceDonator, ServiceCazCaritabil serviceCazCaritabil) {
        logger.info("Initializing DonatieDBRepo with properties: {}", props);
        dbUtils = new JdbcUtils(props);
        this.serviceDonator = serviceDonator;
        this.serviceCazCaritabil = serviceCazCaritabil;
    }

    @Override
    public List<Donatie> findByDonatorId(Integer donatorId) {
        logger.traceEntry("Searching for donations by donator ID: {}", donatorId);
        Connection conn = dbUtils.getConnection();
        List<Donatie> donations = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM Donatii WHERE donator_id = ?")) {
            ps.setInt(1, donatorId);
            try (ResultSet result = ps.executeQuery()) {
                while (result.next()) {
                    int id = result.getInt("id");
                    int cazId = result.getInt("caz_id");
                    float suma = result.getFloat("suma");

                    // Fetch complete Donator object
                    Donator donator = serviceDonator.getDonatorById(donatorId).orElseThrow(() -> new RuntimeException("Donator not found"));

                    // Fetch complete CazCaritabil object
                    CazCaritabil caz = serviceCazCaritabil.getCazCaritabilById(cazId).orElseThrow(() -> new RuntimeException("Caz not found"));

                    Donatie donatie = new Donatie(donator, caz, suma);
                    donations.add(donatie);
                }
            }
        } catch (SQLException e) {
            logger.error("Error retrieving donations by donator ID", e);
        }
        logger.traceExit("Retrieved donations for donator ID: {}", donatorId);
        return donations;
    }

    @Override
    public Donatie getById(Integer id) {
        logger.traceEntry("Searching for donation by id: {}", id);
        Connection conn = dbUtils.getConnection();
        try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM Donatii WHERE id = ?")) {
            ps.setInt(1, id);
            try (ResultSet result = ps.executeQuery()) {
                if (result.next()) {
                    int donatorId = result.getInt("donator_id");
                    int cazId = result.getInt("caz_id");
                    float suma = result.getFloat("suma");
                    Donator donator = new Donator(donatorId, null, null, null); // Assuming a constructor that takes an ID
                    CazCaritabil caz = new CazCaritabil(cazId, null, null); // Assuming a constructor that takes an ID
                    Donatie donatie = new Donatie(donator, caz, suma);
                    return donatie;
                }
            }
        } catch (SQLException e) {
            logger.error("Error retrieving donation by id", e);
        }
        logger.traceExit("No donation found with id: {}", id);
        return null;
    }

    @Override
    public Donatie save(Donatie entity) {
        logger.traceEntry("Saving donation: {}", entity);
        Connection conn = dbUtils.getConnection();
        try (PreparedStatement ps = conn.prepareStatement("INSERT INTO Donatii (donator_id, caz_id, suma) VALUES (?, ?, ?)", Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, entity.getDonator().getId());
            ps.setInt(2, entity.getCaz().getId());
            ps.setFloat(3, entity.getSuma());
            int result = ps.executeUpdate();
            if (result > 0) {
                try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        entity = new Donatie(new Donator(entity.getDonator().getId(), null, null, null),
                                new CazCaritabil(entity.getCaz().getId(), null, null),
                                entity.getSuma());
                    } else {
                        throw new SQLException("Creating donation failed, no ID obtained.");
                    }
                }
                return entity;
            }
        } catch (SQLException e) {
            logger.error("Error saving donation", e);
        }
        logger.traceExit("Failed to save donation: {}", entity);
        return null;
    }

    @Override
    public Donatie delete(Integer id) {
        logger.traceEntry("Deleting donation by id: {}", id);
        Connection conn = dbUtils.getConnection();
        try (PreparedStatement ps = conn.prepareStatement("DELETE FROM Donatii WHERE id = ?")) {
            ps.setInt(1, id);
            int result = ps.executeUpdate();
            if (result > 0) {
                return new Donatie(null, null, 0);
            }
        } catch (SQLException e) {
            logger.error("Error deleting donation by id", e);
        }
        logger.traceExit("Failed to delete donation by id: {}", id);
        return null;
    }

    @Override
    public Donatie update(Donatie entity) {
        logger.traceEntry("Updating donation: {}", entity);
        Connection conn = dbUtils.getConnection();
        try (PreparedStatement ps = conn.prepareStatement("UPDATE Donatii SET donator_id = ?, caz_id = ?, suma = ? WHERE id = ?")) {
            ps.setInt(1, entity.getDonator().getId());
            ps.setInt(2, entity.getCaz().getId());
            ps.setFloat(3, entity.getSuma());
            ps.setInt(4, entity.getId());
            int result = ps.executeUpdate();
            if (result > 0) {
                return entity;
            }
        } catch (SQLException e) {
            logger.error("Error updating donation", e);
        }
        logger.traceExit("Failed to update donation: {}", entity);
        return null;
    }

    @Override
    public Iterable<Donatie> getAll() {
        logger.traceEntry("Retrieving all donations");
        Connection conn = dbUtils.getConnection();
        List<Donatie> donations = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM Donatii")) {
            try (ResultSet result = ps.executeQuery()) {
                while (result.next()) {
                    int id = result.getInt("id");
                    int donatorId = result.getInt("donator_id");
                    int cazId = result.getInt("caz_id");
                    float suma = result.getFloat("suma");
                    Donator donator = new Donator(donatorId, null, null, null);
                    CazCaritabil caz = new CazCaritabil(cazId, null, null);
                    Donatie donatie = new Donatie(donator, caz, suma);
                    donations.add(donatie);
                }
            }
        } catch (SQLException e) {
            logger.error("Error retrieving all donations", e);
        }
        logger.traceExit("Retrieved all donations");
        return donations;
    }
}