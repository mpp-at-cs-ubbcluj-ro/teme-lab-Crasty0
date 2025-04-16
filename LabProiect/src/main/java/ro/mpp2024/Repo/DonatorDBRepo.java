package ro.mpp2024.Repo;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ro.mpp2024.Domain.Donator;
import ro.mpp2024.Utils.JdbcUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

public class DonatorDBRepo implements DonatorRepo {

    private final JdbcUtils dbUtils;
    private static final Logger logger = LogManager.getLogger();

    public DonatorDBRepo(Properties props) {
        logger.info("Initializing DonatorDBRepo with properties: {}", props);
        dbUtils = new JdbcUtils(props);
    }

    public Optional<Donator> findByNume(String name) {
        logger.traceEntry("Searching for donor by name: {}", name);
        Connection conn = dbUtils.getConnection();
        try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM Donatori WHERE nume = ?")) {
            ps.setString(1, name);
            try (ResultSet result = ps.executeQuery()) {
                if (result.next()) {
                    int id = result.getInt("id");
                    String nume = result.getString("nume");
                    String adresa = result.getString("adresa");
                    String telefon = result.getString("telefon");
                    Donator donator = new Donator(id, nume, adresa, telefon);
                    return Optional.of(donator);
                }
            }
        } catch (SQLException e) {
            logger.error("Error retrieving donor by name", e);
        }
        logger.traceExit("No donor found with name: {}", name);
        return Optional.empty();
    }

    @Override
    public Donator getById(Integer id) {
        logger.traceEntry("Searching for donor by id: {}", id);
        Connection conn = dbUtils.getConnection();
        Donator donator = null;
        try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM Donatori WHERE id = ?")) {
            ps.setInt(1, id);
            try (ResultSet result = ps.executeQuery()) {
                if (result.next()) {
                    String nume = result.getString("nume");
                    String adresa = result.getString("adresa");
                    String telefon = result.getString("telefon");
                    donator = new Donator(id, nume, adresa, telefon);
                }
            }
        } catch (SQLException e) {
            logger.error("Error retrieving donor by id", e);
        }
        return logger.traceExit(donator);
    }

    public void add(Donator donator) {
        logger.traceEntry("Saving donor {}", donator);
        Connection conn = dbUtils.getConnection();
        try (PreparedStatement ps = conn.prepareStatement("INSERT INTO Donatori (nume, adresa, telefon) VALUES (?, ?, ?)")) {
            ps.setString(1, donator.getNume());
            ps.setString(2, donator.getAdresa());
            ps.setString(3, donator.getTelefon());
            int result = ps.executeUpdate();
            logger.trace("Saved {} instances", result);
        } catch (SQLException e) {
            logger.error("Error saving donor", e);
        }
    }

    @Override
    public Donator update(Donator entity) {
        logger.traceEntry("Updating donor: {}", entity);
        Connection conn = dbUtils.getConnection();
        try (PreparedStatement ps = conn.prepareStatement("UPDATE Donatori SET nume = ?, adresa = ?, telefon = ? WHERE id = ?")) {
            ps.setString(1, entity.getNume());
            ps.setString(2, entity.getAdresa());
            ps.setString(3, entity.getTelefon());
            ps.setInt(4, entity.getId());
            int result = ps.executeUpdate();
            if (result > 0) {
                return entity;
            }
        } catch (SQLException e) {
            logger.error("Error updating donor", e);
        }
        logger.traceExit("Failed to update donor: {}", entity);
        return null;
    }

    @Override
    public Donator save(Donator entity) {
        logger.traceEntry("Saving donor: {}", entity);
        Connection conn = dbUtils.getConnection();
        try (PreparedStatement ps = conn.prepareStatement("INSERT INTO Donatori (id, nume, adresa, telefon) VALUES (?, ?, ?, ?)")) {
            ps.setInt(1, entity.getId());
            ps.setString(2, entity.getNume());
            ps.setString(3, entity.getAdresa());
            ps.setString(4, entity.getTelefon());
            int result = ps.executeUpdate();
            if (result > 0) {
                return entity;
            }
        } catch (SQLException e) {
            logger.error("Error saving donor", e);
        }
        logger.traceExit("Failed to save donor: {}", entity);
        return null;
    }

    @Override
    public Donator delete(Integer id) {
        logger.traceEntry("Deleting donor by id: {}", id);
        Connection conn = dbUtils.getConnection();
        try (PreparedStatement ps = conn.prepareStatement("DELETE FROM Donatori WHERE id = ?")) {
            ps.setInt(1, id);
            int result = ps.executeUpdate();
            if (result > 0) {
                return new Donator(id, null, null, null);
            }
        } catch (SQLException e) {
            logger.error("Error deleting donor by id", e);
        }
        logger.traceExit("Failed to delete donor by id: {}", id);
        return null;
    }

    public Iterable<Donator> getAll() {
        logger.traceEntry();
        Connection conn = dbUtils.getConnection();
        List<Donator> donors = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM Donatori")) {
            try (ResultSet result = ps.executeQuery()) {
                while (result.next()) {
                    int id = result.getInt("id");
                    String nume = result.getString("nume");
                    String adresa = result.getString("adresa");
                    String telefon = result.getString("telefon");
                    Donator donator = new Donator(id, nume, adresa, telefon);
                    donors.add(donator);
                }
            }
        } catch (SQLException e) {
            logger.error("Error retrieving all donors", e);
        }
        logger.traceExit(donors);
        return donors;
    }
}