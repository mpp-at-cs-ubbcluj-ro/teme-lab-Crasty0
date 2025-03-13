package ro.mpp2024;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class CarsDBRepository implements CarRepository{

    private JdbcUtils dbUtils;

    private static final Logger logger= LogManager.getLogger();

    public CarsDBRepository(Properties props) {
        logger.info("Initializing CarsDBRepository with properties: {} ",props);
        dbUtils=new JdbcUtils(props);
    }

    @Override
    public List<Car> findByManufacturer(String manufacturerN) {
        logger.traceEntry("Searching for cars by manufacturer: {}", manufacturerN);
        List<Car> cars = new ArrayList<>();
        Connection conn = dbUtils.getConnection();
        try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM Masini WHERE manufacturer = ?")) {
            ps.setString(1, manufacturerN);
            try (ResultSet result = ps.executeQuery()) {
                while (result.next()) {
                    int id = result.getInt("id");
                    String manufacturer = result.getString("manufacturer");
                    String model = result.getString("model");
                    int year = result.getInt("year");
                    Car car = new Car(manufacturer, model, year);
                    car.setId(id);
                    cars.add(car);
                }
            }
        } catch (SQLException e) {
            logger.error("Error retrieving cars by manufacturer", e);
        }
        logger.traceExit(cars);
        return cars;
    }

    @Override
    public List<Car> findBetweenYears(int min, int max) {

        logger.traceEntry("Searching for cars between years: {} and {}", min, max);
        List<Car> cars = new ArrayList<>();
        Connection conn = dbUtils.getConnection();
        try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM Masini WHERE year BETWEEN ? AND ?")) {
            ps.setInt(1, min);
            ps.setInt(2, max);
            try (ResultSet result = ps.executeQuery()) {
                while (result.next()) {
                    int id = result.getInt("id");
                    String manufacturer = result.getString("manufacturer");
                    String model = result.getString("model");
                    int year = result.getInt("year");
                    Car car = new Car(manufacturer, model, year);
                    car.setId(id);
                    cars.add(car);
                }
            }
        } catch (SQLException e) {
            logger.error("Error retrieving cars between years", e);
        }
        logger.traceExit(cars);
        return cars;
    }

    @Override
    public void add(Car elem) {
        logger.traceEntry("saving task {}",elem);
        Connection conn = dbUtils.getConnection();

        try(PreparedStatement ps = conn.prepareStatement("insert into Masini (manufacturer,model,year) values (?,?,?)")){
            ps.setString(1, elem.getManufacturer());
            ps.setString(2,elem.getModel());
            ps.setInt(3,elem.getYear());

            int result = ps.executeUpdate();
            logger.trace("saved {} instances",result);
        } catch (SQLException e) {
            logger.error(e);
            System.out.println("Error " + e);
        }
    }

    @Override
    public void update(Integer integer, Car elem) {
        logger.traceEntry("Updating car with id {}: {}", integer, elem);
        Connection conn = dbUtils.getConnection();
        try (PreparedStatement ps = conn.prepareStatement("UPDATE Masini SET manufacturer = ?, model = ?, year = ? WHERE id = ?")) {
            ps.setString(1, elem.getManufacturer());
            ps.setString(2, elem.getModel());
            ps.setInt(3, elem.getYear());
            ps.setInt(4, integer);
            int result = ps.executeUpdate();
            logger.trace("Updated {} instances", result);
        } catch (SQLException e) {
            logger.error("Error updating car with id " + integer, e);
        }
    }

    @Override
    public Iterable<Car> findAll() {

        logger.traceEntry();
        Connection con=dbUtils.getConnection();
        List<Car> cars=new ArrayList<>();
        try (PreparedStatement preStmt=con.prepareStatement( "select * from Masini")) { try(ResultSet result=preStmt.executeQuery()) {
            while (result.next()) {
                int id = result.getInt( "id");
                String manufacturer = result.getString( "manufacturer");
                String model = result.getString(  "model");
                int year=result.getInt( "year");
                Car car=new Car (manufacturer, model, year);
                car.setId(id);
                cars.add(car);
            }
        }
        } catch (SQLException e) {
            logger.error(e);
            System.err.println("Error DB "+e);
        }

        logger.traceExit(cars);
        return cars;

    }
}
