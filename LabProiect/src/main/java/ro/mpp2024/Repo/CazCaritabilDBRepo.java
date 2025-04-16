package ro.mpp2024.Repo;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ro.mpp2024.Domain.CazCaritabil;
import ro.mpp2024.Utils.JdbcUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

public class CazCaritabilDBRepo implements CazCaritabilRepo {

    private JdbcUtils dbUtils;
    private static final Logger logger = LogManager.getLogger();

    public CazCaritabilDBRepo(Properties props) {
        logger.info("Initializing CazCaritabilDBRepo with properties: {}", props);
        dbUtils = new JdbcUtils(props);
    }

    @Override
    public Optional<CazCaritabil> findByName(String name) {
        logger.traceEntry("Searching for charitable case by name: {}", name);
        Connection conn = dbUtils.getConnection();
        try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM CazuriCaritabile WHERE name = ?")) {
            ps.setString(1, name);
            try (ResultSet result = ps.executeQuery()) {
                if (result.next()) {
                    int id = result.getInt("id");
                    double sumaStransa = result.getDouble("Suma Stransa");
                    CazCaritabil cazCaritabil = new CazCaritabil(id, name, sumaStransa);
                    return Optional.of(cazCaritabil);
                }
            }
        } catch (SQLException e) {
            logger.error("Error retrieving charitable case by name", e);
        }
        logger.traceExit("No charitable case found with name: {}", name);
        return Optional.empty();
    }

    @Override
    public CazCaritabil getById(Integer id) {
        logger.traceEntry("Searching for charitable case by id: {}", id);
        Connection conn = dbUtils.getConnection();
        try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM CazuriCaritabile WHERE id = ?")) {
            ps.setInt(1, id);
            try (ResultSet result = ps.executeQuery()) {
                if (result.next()) {
                    String name = result.getString("name");
                    double sumaStransa = result.getDouble("Suma Stransa");
                    CazCaritabil cazCaritabil = new CazCaritabil(id, name, sumaStransa);
                    return cazCaritabil;
                }
            }
        } catch (SQLException e) {
            logger.error("Error retrieving charitable case by id", e);
        }
        logger.traceExit("No charitable case found with id: {}", id);
        return null;
    }

    @Override
    public CazCaritabil save(CazCaritabil entity) {
        logger.traceEntry("Saving charitable case: {}", entity);
        Connection conn = dbUtils.getConnection();
        try (PreparedStatement ps = conn.prepareStatement("INSERT INTO CazuriCaritabile (id, name, \"Suma Stransa\") VALUES (?, ?, ?)")) {
            ps.setInt(1, entity.getId());
            ps.setString(2, entity.getNume());
            ps.setDouble(3, entity.getSumaStransa());
            int result = ps.executeUpdate();
            if (result > 0) {
                return entity;
            }
        } catch (SQLException e) {
            logger.error("Error saving charitable case", e);
        }
        logger.traceExit("Failed to save charitable case: {}", entity);
        return null;
    }

    @Override
    public CazCaritabil delete(Integer id) {
        logger.traceEntry("Deleting charitable case by id: {}", id);
        Connection conn = dbUtils.getConnection();
        try (PreparedStatement ps = conn.prepareStatement("DELETE FROM CazuriCaritabile WHERE id = ?")) {
            ps.setInt(1, id);
            int result = ps.executeUpdate();
            if (result > 0) {
                return new CazCaritabil(id, null, null);
            }
        } catch (SQLException e) {
            logger.error("Error deleting charitable case by id", e);
        }
        logger.traceExit("Failed to delete charitable case by id: {}", id);
        return null;
    }

    @Override
    public CazCaritabil update(CazCaritabil entity) {
        logger.traceEntry("Updating charitable case: {}", entity);
        Connection conn = dbUtils.getConnection();
        try (PreparedStatement ps = conn.prepareStatement("UPDATE CazuriCaritabile SET name = ?, \"Suma Stransa\" = ? WHERE id = ?")) {
            ps.setString(1, entity.getNume());
            ps.setDouble(2, entity.getSumaStransa());
            ps.setInt(3, entity.getId());
            int result = ps.executeUpdate();
            if (result > 0) {
                return entity;
            }
        } catch (SQLException e) {
            logger.error("Error updating charitable case", e);
        }
        logger.traceExit("Failed to update charitable case: {}", entity);
        return null;
    }

    @Override
    public Iterable<CazCaritabil> getAll() {
        logger.traceEntry("Retrieving all charitable cases");
        Connection conn = dbUtils.getConnection();
        List<CazCaritabil> cazuriCaritabile = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM CazuriCaritabile")) {
            try (ResultSet result = ps.executeQuery()) {
                while (result.next()) {
                    int id = result.getInt("id");
                    String name = result.getString("name");
                    double sumaStransa = result.getDouble("Suma Stransa");
                    CazCaritabil cazCaritabil = new CazCaritabil(id, name, sumaStransa);
                    cazuriCaritabile.add(cazCaritabil);
                }
            }
        } catch (SQLException e) {
            logger.error("Error retrieving all charitable cases", e);
        }
        logger.traceExit("Retrieved all charitable cases");
        return cazuriCaritabile;
    }

}