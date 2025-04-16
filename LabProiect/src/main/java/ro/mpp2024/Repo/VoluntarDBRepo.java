package ro.mpp2024.Repo;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ro.mpp2024.Domain.Voluntar;
import ro.mpp2024.Utils.JdbcUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

public class VoluntarDBRepo implements VoluntarRepo {

    private JdbcUtils dbUtils;

    private static final Logger logger = LogManager.getLogger();

    public VoluntarDBRepo(Properties props) {
        logger.info("Initializing VoluntarDBRepo with properties: {}", props);
        dbUtils = new JdbcUtils(props);
    }

    @Override
    public Voluntar findByUsername(String username) {
        logger.traceEntry("Searching for volunteer by username: {}", username);
        Connection conn = dbUtils.getConnection();
        Voluntar voluntar = null;
        try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM Voluntari WHERE username = ?")) {
            ps.setString(1, username);
            try (ResultSet result = ps.executeQuery()) {
                if (result.next()) {
                    int id = result.getInt("id");
                    String parola = result.getString("password");
                    voluntar = new Voluntar(id, username, parola);
                }
            }
        } catch (SQLException e) {
            logger.error("Error retrieving volunteer by username", e);
        }
        return voluntar;
    }

    @Override
    public Iterable<Voluntar> getAll() {
        logger.traceEntry();
        Connection conn = dbUtils.getConnection();
        List<Voluntar> voluntari = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM Voluntari")) {
            try (ResultSet result = ps.executeQuery()) {
                while (result.next()) {
                    int id = result.getInt("id");
                    String username = result.getString("username");
                    String password = result.getString("password");
                    Voluntar voluntar = new Voluntar(id, username, password);
                    voluntari.add(voluntar);
                }
            }
        } catch (SQLException e) {
            logger.error(e);
            System.err.println("Error DB " + e);
        }
        logger.traceExit(voluntari);
        return voluntari;
    }

    @Override
    public Voluntar getById(Integer id){
        logger.traceEntry("Searching for Voluntar by id");
        Connection conn = dbUtils.getConnection();
        Voluntar voluntar = null;
        try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM Voluntari WHERE id = ?")) {
            ps.setInt(1, id);
            try (ResultSet result = ps.executeQuery()) {
                if (result.next()) {
                    String username = result.getString("username");
                    String password = result.getString("password");
                    voluntar = new Voluntar(id, username, password);
                }
            }
        } catch (SQLException e) {
            logger.error("Error retrieving voluntar by id", e);
        }
        return logger.traceExit(voluntar);
    }

    @Override
    public Voluntar save(Voluntar entity){
        logger.traceEntry("Saving voluntari: {}", entity);
        Connection conn = dbUtils.getConnection();
        try (PreparedStatement ps = conn.prepareStatement("INSERT INTO Voluntari (id, username, password) VALUES (?, ?, ?)")) {
            ps.setInt(1, entity.getId());
            ps.setString(2, entity.getUsername());
            ps.setString(3, entity.getPassword());
            int result = ps.executeUpdate();
            if (result > 0) {
                return entity;
            }
        } catch (SQLException e) {
            logger.error("Error saving voluntar", e);
        }
        logger.traceExit("Failed to save voluntar: {}", entity);
        return null;
    }

    @Override
    public Voluntar delete(Integer id) {
        logger.traceEntry("Deleting volunteer by id: {}", id);
        Connection conn = dbUtils.getConnection();
        Voluntar voluntar = null;
        try (PreparedStatement ps = conn.prepareStatement("DELETE FROM Voluntari WHERE id = ?")) {
            ps.setInt(1, id);
            int result = ps.executeUpdate();
            if (result > 0) {
                voluntar = new Voluntar(id, null, null);
            }
        } catch (SQLException e) {
            logger.error("Error deleting volunteer by id", e);
        }
        return logger.traceExit(voluntar);
    }

    @Override
    public Voluntar update(Voluntar entity) {
        logger.traceEntry("Updating volunteer: {}", entity);
        Connection conn = dbUtils.getConnection();
        try (PreparedStatement ps = conn.prepareStatement("UPDATE Voluntari SET username = ?, password = ? WHERE id = ?")) {
            ps.setString(1, entity.getUsername());
            ps.setString(2, entity.getPassword());
            ps.setInt(3, entity.getId());
            int result = ps.executeUpdate();
            if (result > 0) {
                return entity;
            }
        } catch (SQLException e) {
            logger.error("Error updating volunteer", e);
        }
        logger.traceExit("Failed to update volunteer: {}", entity);
        return null;
    }
}